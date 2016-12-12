package com.manoharprabhu.wellrested.vo;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mprabhu on 12/8/2016.
 */
public class TableRow {

    public TableRow() {
        this.tableRow = new HashMap<>();
    }

    public void addColumn(String key, String value) {
        this.getTableRow().put(key, value);
    }

    private Map<String, String> tableRow;

    @JsonValue
    public Map<String, String> getTableRow() {
        return tableRow;
    }
}
