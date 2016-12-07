package com.manoharprabhu.wellrested.vo;

/**
 * Created by manoharprabhu on 12/8/2016.
 */
public class Table {

    public Table(String name) {
        this.tableName = name;
    }
    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
