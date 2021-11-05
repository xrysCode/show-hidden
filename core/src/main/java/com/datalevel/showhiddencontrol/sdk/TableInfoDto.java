package com.datalevel.showhiddencontrol.sdk;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 表信息，关联到当前字段信息
 *
 */
@Data
@Accessors(chain = true)
public class TableInfoDto implements Cloneable{
    /**
     * 数据库表名
     */
    private String name;
    /**
     * 注解
     */
    private String comment;

    /**
     * k 表示表字段名，v更全的信息
     */
    private List<TableFieldDto> fieldList;

    @Override
    public TableInfoDto clone() throws CloneNotSupportedException {
        return (TableInfoDto)super.clone();
    }
}
