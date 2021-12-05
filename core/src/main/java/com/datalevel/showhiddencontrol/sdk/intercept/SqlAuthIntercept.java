package com.datalevel.showhiddencontrol.sdk.intercept;

import com.datalevel.showhiddencontrol.sdk.auth.UserAuth;
import com.datalevel.showhiddencontrol.sdk.table.TableFieldValue;
import com.google.common.collect.Lists;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperatorType;
import net.sf.jsqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.cnfexpression.MultiAndExpression;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Intercepts(
        {
//                @Signature(type = StatementHandler.class, method = "getBoundSql", args = {}),
                @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
//                @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
//                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
//                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
//                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        }
)
//@Component
public class SqlAuthIntercept implements Interceptor {
    static ThreadLocal<Boolean> needAuth=new ThreadLocal<>();

    public static void setNeedAuth(Boolean needAuth) {
        SqlAuthIntercept.needAuth.set(needAuth);
    }
    public static void removeNeedAuth() {
        SqlAuthIntercept.needAuth.remove();
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if(needAuth.get()!=null&&!needAuth.get()){
            return invocation.proceed();
        }
        Object target = invocation.getTarget();
        BoundSql boundSql = ((StatementHandler) target).getBoundSql();
        String sql = boundSql.getSql().trim();
        String startSqlCommandType = sql.substring(0, sql.indexOf(" ")).toUpperCase();
        switch (startSqlCommandType){
            case "SELECT":
                sql = getAuthSelect(sql);
            break;
            case "INSERT":
                sql = getAuthInsert(sql);
                break;
            case "UPDATE":
                sql = getAuthUpdate(sql);
                break;
            case "DELETE":
                sql = getAuthDelete(sql);
                break;
        }
        Field sqlField = BoundSql.class.getDeclaredField("sql");
        sqlField.setAccessible(true);
        sqlField.set(boundSql,sql);
        return invocation.proceed();
    }



    public String getAuthSelect(String sql ) throws JSQLParserException {
        Select select = (Select) CCJSqlParserUtil.parse(sql);
        PlainSelect selectBody = (PlainSelect)select.getSelectBody();
        // 过滤查询的字段
        List<SelectItem> selectItems = selectBody.getSelectItems();

        Table fromTable = (Table)selectBody.getFromItem();
        getFullExpression(fromTable, ()->new MultiAndExpression(Lists.newArrayList(selectBody.getWhere())),(e)->selectBody.setWhere(e));

        List<Join> joins = selectBody.getJoins();
        if(joins!=null){
            for (Join join:joins) {
                Table table = (Table)join.getRightItem();
                getFullExpression(table, ()->new MultiAndExpression(join.getOnExpressions().stream().collect(Collectors.toList())), (e)->join.setOnExpressions(Lists.newArrayList(e)));
            }
        }
        return select.toString();
    }

    public <T> void getFullExpression(Table table, Supplier<MultiAndExpression> conversion, Consumer<AndExpression> setExpression) {
        Map<String, String> userAuthInfo = UserAuth.getUserAuthInfo(table.getName());
        if(userAuthInfo==null||userAuthInfo.size()==0){
            return ;
        }
        LinkedList<RegExpMySQLOperator> regExpMySQLOperators = new LinkedList<>();
        userAuthInfo.forEach((field,value)->{
            Column column = new Column(table, field);
            if(value==null){//为空的时候 不能查看任何数据
//                new AndExpression(column,)
            }else {
                RegExpMySQLOperator regExpMatchOperator = new RegExpMySQLOperator(RegExpMatchOperatorType.MATCH_CASEINSENSITIVE);
                regExpMatchOperator.setLeftExpression(column);
                regExpMatchOperator.setRightExpression(new StringValue(value));
                regExpMySQLOperators.add(regExpMatchOperator);
            }

        });
        OrExpression orExpression=null;
        for (int i = 1; i < regExpMySQLOperators.size(); i++) {
            if(i==1){
                orExpression= new OrExpression(regExpMySQLOperators.get(0),regExpMySQLOperators.get(1));
            }else {
                orExpression= new OrExpression(orExpression,regExpMySQLOperators.get(i));
            }
        }
        MultiAndExpression multiAndExpression = conversion.get();// new MultiAndExpression((Lists.newArrayList(expression)));//多租户拼接
        if(orExpression==null){
            AndExpression andExpression = new AndExpression(multiAndExpression, regExpMySQLOperators.get(0));
            setExpression.accept(andExpression);
        }else {
            AndExpression andExpression = new AndExpression(multiAndExpression, orExpression);
            setExpression.accept(andExpression);
        }

    }

    public String getAuthInsert(String sql ) throws JSQLParserException {
//        Insert insert = (Insert)CCJSqlParserUtil.parse(sql);
        // 插入不需要验证
        return sql;
    }

    /**
     * 更新的时候验证是否具有当前数据的权限
     * @param sql
     * @return
     * @throws JSQLParserException
     */
    public String getAuthUpdate(String sql ) throws JSQLParserException {
        Update insert = (Update)CCJSqlParserUtil.parse(sql);
        Table table = insert.getTable();
        getFullExpression(table,()->new MultiAndExpression(Lists.newArrayList(insert.getWhere())),(e)->insert.setWhere(e));
        return insert.toString();
    }

    public String getAuthDelete(String sql ) throws JSQLParserException {
        Delete delete = (Delete)CCJSqlParserUtil.parse(sql);
        Table table = delete.getTable();
        getFullExpression(table,()->new MultiAndExpression(Lists.newArrayList(delete.getWhere())),(e)->delete.setWhere(e));
        return delete.toString();
    }
}
