package com.datalevel.showhiddencontrol.sdk;

import com.mysql.cj.MysqlType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class TableFieldDto implements Serializable {
    /**
     * 字段
     */
    private String field;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 字符
     */
    private String collation;

    /**
     * 是否允许为空
     */
    private String nullable;

    /**
     * 是否主键，索引
     */
    private String key;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 扩展
     */
    private String extra;

    /**
     * 描述
     */
    private String comment;

    private String propertyName;//转换的驼峰名
//    private DbColumnType columnType;//类型
    private MysqlType columnType;//类型


    public TableFieldDto setField(String field) {
        this.field = field;
//        this.propertyName= UnderlineCamelUtils.removePrefixAndCamel(field);
        return this;
    }

    public TableFieldDto setType(String type) {
        this.type = type;
        String fieldType=type.replaceAll("\\(.+","");
        this.columnType=MysqlType.getByName(fieldType);
        return this;
    }
    public int getLength(){
        if(!type.contains("(")){//datetime等没有长度的字段
            return 0;
        }
        String[] split = type.replaceAll("[a-zA-Z() ]", "").split(",");
        return Integer.valueOf(split[0]);
    }
    public int getDecimal(){
        String[] split = type.replaceAll("[a-zA-Z() ]", "").split(",");
        if(split.length==1){
            return 0;
        }else {
            return Integer.valueOf(split[1]);
        }
    }
}
