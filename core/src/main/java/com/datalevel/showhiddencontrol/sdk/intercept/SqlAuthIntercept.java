package com.datalevel.showhiddencontrol.sdk.intercept;

import com.datalevel.showhiddencontrol.sdk.auth.UserAuth;
import com.datalevel.showhiddencontrol.sdk.table.TableFieldValue;
import com.google.common.collect.Lists;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
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
@Component
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
        Map<String, TableFieldValue> tableFieldValueMap = UserAuth.getUserAuthInfo();
        String sql = boundSql.getSql().trim();
        String startSqlCommandType = sql.substring(0, sql.indexOf(" ")).toUpperCase();
        switch (startSqlCommandType){
            case "SELECT":
                sql = getAuthSelect(sql, tableFieldValueMap);
            break;
            case "INSERT":
                sql = getAuthInsert(sql, tableFieldValueMap);
                break;
            case "UPDATE":
                sql = getAuthUpdate(sql, tableFieldValueMap);
                break;
            case "DELETE":
                sql = getAuthDelete(sql, tableFieldValueMap);
                break;
        }
        Field sqlField = BoundSql.class.getDeclaredField("sql");
        sqlField.setAccessible(true);
        sqlField.set(boundSql,sql);
        return invocation.proceed();
    }



    public String getAuthSelect(String sql, Map<String, TableFieldValue> tableAuthMap) throws JSQLParserException {
        Select select = (Select) CCJSqlParserUtil.parse(sql);
        PlainSelect selectBody = (PlainSelect)select.getSelectBody();
        // 过滤查询的字段
        List<SelectItem> selectItems = selectBody.getSelectItems();

        Table fromTable = (Table)selectBody.getFromItem();
        getFullExpression(tableAuthMap,fromTable, ()->new MultiAndExpression(Lists.newArrayList(selectBody.getWhere())),(e)->selectBody.setWhere(e));

        List<Join> joins = selectBody.getJoins();
        if(joins!=null){
            for (Join join:joins) {
                Table table = (Table)join.getRightItem();
                getFullExpression(tableAuthMap,table, ()->new MultiAndExpression(join.getOnExpressions().stream().collect(Collectors.toList())), (e)->join.setOnExpressions(Lists.newArrayList(e)));
            }
        }
        return select.toString();
    }

    public <T> void getFullExpression(Map<String, TableFieldValue> tableAuthMap, Table table,
                                             Supplier<MultiAndExpression> conversion, Consumer<AndExpression> setExpression) {
        TableFieldValue tableFieldValue = tableAuthMap.get(table.getName());
        if(tableFieldValue==null){
            return ;
        }
        MultiAndExpression multiAndExpression = conversion.get();// new MultiAndExpression((Lists.newArrayList(expression)));//多租户拼接
        Column column = new Column(table, tableFieldValue.getDbTableField());
        RegExpMySQLOperator regExpMatchOperator = new RegExpMySQLOperator(RegExpMatchOperatorType.MATCH_CASEINSENSITIVE);
        regExpMatchOperator.setLeftExpression(column);
        regExpMatchOperator.setRightExpression(new StringValue(tableFieldValue.getValue()));
        AndExpression andExpression = new AndExpression(multiAndExpression, regExpMatchOperator);
        setExpression.accept(andExpression);
    }

    public String getAuthInsert(String sql, Map<String, TableFieldValue> tableAuthMap) throws JSQLParserException {
//        Insert insert = (Insert)CCJSqlParserUtil.parse(sql);
        // 插入不需要验证
        return sql;
    }

    /**
     * 更新的时候验证是否具有当前数据的权限
     * @param sql
     * @param tableAuthMap
     * @return
     * @throws JSQLParserException
     */
    public String getAuthUpdate(String sql, Map<String, TableFieldValue> tableAuthMap) throws JSQLParserException {
        Update insert = (Update)CCJSqlParserUtil.parse(sql);
        Table table = insert.getTable();
        TableFieldValue tableFieldValue = tableAuthMap.get(table.getName());
        if(tableFieldValue==null){
            return sql;
        }
        getFullExpression(tableAuthMap, table,()->new MultiAndExpression(Lists.newArrayList(insert.getWhere())),(e)->insert.setWhere(e));
        return insert.toString();
    }

    public String getAuthDelete(String sql, Map<String, TableFieldValue> tableAuthMap) throws JSQLParserException {
        Delete delete = (Delete)CCJSqlParserUtil.parse(sql);
        Table table = delete.getTable();
        TableFieldValue tableFieldValue = tableAuthMap.get(table.getName());
        if(tableFieldValue==null){
            return sql;
        }
        getFullExpression(tableAuthMap, table,()->new MultiAndExpression(Lists.newArrayList(delete.getWhere())),(e)->delete.setWhere(e));
        return delete.toString();
    }
}
