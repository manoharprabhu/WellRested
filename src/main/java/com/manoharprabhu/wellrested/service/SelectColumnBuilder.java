package com.manoharprabhu.wellrested.service;

import org.json.JSONArray;

/**
 * Created by manoharprabhu on 12/9/2016.
 */
public class SelectColumnBuilder {
    private JSONArray columns;

    public SelectColumnBuilder(JSONArray columns) {
        this.columns = columns;
    }

    public String build() {
        if(columns == null || columns.length() == 0) {
            return "*";
        }
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < this.columns.length(); i++) {
            String column = this.columns.getString(i);
            if("*".equals(column)) {
                return "*";
            }
            builder.append("`").append(column.replace("`","``")).append("`");
            if(i != this.columns.length() - 1) {
                builder.append(",");
            }
        }
        return builder.toString();
    }
}
