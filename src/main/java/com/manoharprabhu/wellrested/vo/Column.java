package com.manoharprabhu.wellrested.vo;

/**
 * Created by mprabhu on 12/8/2016.
 */
public class Column {

    public Column(String columnType, String columnName) {
        this.columnName = columnName;
        this.columnType = columnType;
    }

    private String columnType;
    private String columnName;


    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }


    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }


}
