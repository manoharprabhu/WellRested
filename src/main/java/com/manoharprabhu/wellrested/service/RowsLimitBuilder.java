package com.manoharprabhu.wellrested.service;

import com.manoharprabhu.wellrested.DatabaseType;
import org.json.JSONObject;

/**
 * Created by mprabhu on 12/12/2016.
 */
public class RowsLimitBuilder {
    public static final String FROM_ROW = "fromRow";
    public static final String COUNT = "count";
    private JSONObject limits;
    private DatabaseType databaseType;

    public RowsLimitBuilder(JSONObject limits, DatabaseType databaseType) {
        this.limits = limits;
        this.databaseType = databaseType;
    }

    public String build() {
        if(limits.keySet().contains(FROM_ROW) == false
                || limits.keySet().contains(COUNT) == false) {
            return "";
        }

        if(this.databaseType == DatabaseType.MYSQL) {
            return "LIMIT " + limits.getInt(FROM_ROW) + " ," + limits.getInt(COUNT);
        } else if(this.databaseType == DatabaseType.SQLSERVER) {
            return "TOP " + limits.getInt(COUNT);
        } else {
            return "";
        }
    }
}
