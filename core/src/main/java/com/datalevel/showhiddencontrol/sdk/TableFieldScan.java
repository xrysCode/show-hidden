package com.datalevel.showhiddencontrol.sdk;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Slf4j
public class TableFieldScan {
    protected DataSource dataSource;
    //所有本地表信息 本项目的所有表
    protected static List<TableInfoDto> localTableList;

    public TableFieldScan(DataSource dataSource) {
        this.dataSource = dataSource;
        localTableList=parseLocalTableInfo();
    }

    private List<TableInfoDto> parseLocalTableInfo() {
        try (Connection connection=dataSource.getConnection()) {
            try (PreparedStatement preparedTableStatement = connection.prepareStatement("show table status");
                 ResultSet resultTableSet = preparedTableStatement.executeQuery()){
                List<TableInfoDto> tableInfoList = Lists.newArrayList();
                while (resultTableSet.next()){
                    TableInfoDto tableInfoDto = new TableInfoDto()
                            .setName(resultTableSet.getString("name"))
                            .setComment(resultTableSet.getString("comment"));
                    try (PreparedStatement preparedFieldStatement = connection.prepareStatement("show full fields from "+ tableInfoDto.getName());
                         ResultSet resultFieldSet = preparedFieldStatement.executeQuery()){
                        List<TableFieldDto> tableFieldList = Lists.newArrayList();
                        while (resultFieldSet.next()){
                            TableFieldDto tableFieldDto = new TableFieldDto()
                                    .setField(resultFieldSet.getString("field"))
                                    .setType(resultFieldSet.getString("type"))
                                    .setCollation(resultFieldSet.getString("collation"))
                                    .setNullable(resultFieldSet.getString("null"))
                                    .setKey(resultFieldSet.getString("key"))
                                    .setDefaultValue(resultFieldSet.getString("default"))
                                    .setExtra(resultFieldSet.getString("extra"))
                                    .setComment(resultFieldSet.getString("comment"));
                            tableFieldList.add(tableFieldDto);
                        }
                        tableInfoDto.setFieldList(tableFieldList);
                    }
                    tableInfoList.add(tableInfoDto);
                }
                return tableInfoList;
            }
        } catch (SQLException e) {
            throw new RuntimeException("解析数据库表异常",e);
        }
    }
    /**
     * 所有本地的表信息 与webKey无关
     * @return
     */
    public List<TableInfoDto> getLocalTableInfo(){
        if(localTableList==null){
            throw new RuntimeException("表配置没有准备好！");
        }
        return Collections.unmodifiableList(localTableList);
    }

}
