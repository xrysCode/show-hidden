package com.datalevel.showhiddencontrol.sdk.intercept;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.util.deparser.SelectDeParser;

public class SqlAuthIntercept {

    public static void main(String[] args) throws JSQLParserException {
        String sql="SELECT a.*,b.* FROM base_application a,base_service b where b.id=a.id and a.app_name=?";
        Select parse = (Select)CCJSqlParserUtil.parse(sql);
        PlainSelect selectBody = (PlainSelect)parse.getSelectBody();

        System.out.println(parse);
    }
}
