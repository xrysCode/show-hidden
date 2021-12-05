package com.datalevel.showhiddencontrol.sdk.intercept;

import com.datalevel.showhiddencontrol.sdk.table.TableFieldValue;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.IsBooleanExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SqlAuthInterceptTest {

    public static void main(String[] args) throws JSQLParserException {
//        select();
//        insert();
//        update();
        select2("SELECT * FROM a_table where app_name=? ");
    }
    public static void select2(String sql) throws JSQLParserException {
        Select select = (Select) CCJSqlParserUtil.parse(sql);
        PlainSelect selectBody = (PlainSelect)select.getSelectBody();
        // 过滤查询的字段
        List<SelectItem> selectItems = selectBody.getSelectItems();

        Table fromTable = (Table)selectBody.getFromItem();
        Expression where = selectBody.getWhere();
        IsBooleanExpression isBooleanExpression = new IsBooleanExpression();
        isBooleanExpression.setLeftExpression(where);
        selectBody.setWhere(isBooleanExpression);
        IsBooleanExpression isBooleanExpression2 = new IsBooleanExpression();
        isBooleanExpression2.setLeftExpression(isBooleanExpression);
        selectBody.setWhere(isBooleanExpression2);
        System.out.println(isBooleanExpression2);

    }


    public static void select() throws JSQLParserException {
        String sql1="SELECT * FROM a_table where app_name=? ";
        String sql13="SELECT * FROM a_table where app_name='x' ";
        String sql11="SELECT * FROM a_table where app_name=? and xx=?";
        String sql12="SELECT * FROM a_table where app_name=? or xx=?";

        String sql2="SELECT name,age FROM a_table a where a.app_name=? and a.xx=?";

        String sql3="SELECT a.*,b.* FROM a_table a,b_table b where b.id=a.id and a.app_name=? and a.xx=?";
        String sql4="SELECT a.*,b.* FROM a_table a left join b_table b on  b.id=a.id and a.app_name=? where  a.xx=?";
        String sql5="SELECT a.*,b.* FROM a_table a right join b_table b on  b.id=a.id and a.app_name=? " +
                "left join c_table c on b.id=c.id where b.b_name='bbb' and a.xx=?";

        Map<String, TableFieldValue> tableAuthMap=new HashMap<>();
        tableAuthMap.put("a_table",new TableFieldValue("a_table","auth_flag","权限"));
        tableAuthMap.put("c_table",new TableFieldValue("c_table","auth_flag","权限"));

        SqlAuthIntercept sqlAuthIntercept = new SqlAuthIntercept();
        String sql = sqlAuthIntercept.getAuthSelect(sql1);

        System.out.println(sql);
    }
    public static void insert() throws JSQLParserException {
        String sql1="INSERT INTO a_table(id,label) VALUES (?,'aaaa') ";

        Map<String, TableFieldValue> tableAuthMap=new HashMap<>();
        tableAuthMap.put("a_table",new TableFieldValue("a_table","auth_flag","权限"));
        tableAuthMap.put("c_table",new TableFieldValue("c_table","auth_flag","权限"));

        SqlAuthIntercept sqlAuthIntercept = new SqlAuthIntercept();
        String sql = sqlAuthIntercept.getAuthInsert(sql1);

        System.out.println(sql);
    }
    public static void update() throws JSQLParserException {
        String sql1="UPDATE a_table SET sort = 4 WHERE id = 7 ";

        Map<String, TableFieldValue> tableAuthMap=new HashMap<>();
        tableAuthMap.put("a_table",new TableFieldValue("a_table","auth_flag","权限"));
        tableAuthMap.put("c_table",new TableFieldValue("c_table","auth_flag","权限"));

        SqlAuthIntercept sqlAuthIntercept = new SqlAuthIntercept();
        String sql = sqlAuthIntercept.getAuthUpdate(sql1);

        System.out.println(sql);
    }
}