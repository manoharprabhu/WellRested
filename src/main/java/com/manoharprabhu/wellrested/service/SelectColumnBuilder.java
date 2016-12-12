package com.manoharprabhu.wellrested.service;

import org.json.JSONArray;

/**
 * Created by manoharprabhu on 12/9/2016.
 */
public class SelectColumnBuilder {
    public static final String ALL_COLUMNS_WILDCARD = "*";
    private JSONArray columns;

    public SelectColumnBuilder(JSONArray columns) {
        this.columns = columns;
    }

    public String build() {
        if(columns == null || columns.length() == 0) {
            return ALL_COLUMNS_WILDCARD;
        }
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < this.columns.length(); i++) {
            String column = this.columns.getString(i);
            if(ALL_COLUMNS_WILDCARD.equals(column)) {
                return ALL_COLUMNS_WILDCARD;
            }
            builder.append("`").append(column.replace("`","``")).append("`");
            if(i != this.columns.length() - 1) {
                builder.append(",");
            }
        }
        return builder.toString();
    }
}
