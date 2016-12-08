package com.manoharprabhu.wellrested.vo;

import java.util.Map;

/**
 * Created by mprabhu on 12/8/2016.
 */
public class TableRow {

    public TableRow(Map<String, String> tableRow) {
        this.tableRow = tableRow;
    }

    public Map<String, String> getTableRow() {
        return tableRow;
    }

    public void setTableRow(Map<String, String> tableRow) {
        this.tableRow = tableRow;
    }

    private Map<String, String> tableRow;
}
