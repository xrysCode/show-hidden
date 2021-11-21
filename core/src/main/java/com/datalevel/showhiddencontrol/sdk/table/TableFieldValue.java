package com.datalevel.showhiddencontrol.sdk.table;

import lombok.Data;

@Data
public class TableFieldValue {
    private String dbTable;
    private String dbTableField;
    private String value;

    public TableFieldValue(String dbTable, String dbTableField, String value) {
        this.dbTable = dbTable;
        this.dbTableField = dbTableField;
        this.value = value;
    }
}
