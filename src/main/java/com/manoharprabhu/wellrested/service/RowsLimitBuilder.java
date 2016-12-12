package com.manoharprabhu.wellrested.service;

import com.manoharprabhu.wellrested.DatabaseType;
import org.json.JSONObject;

/**
 * Created by mprabhu on 12/12/2016.
 */
public class RowsLimitBuilder {
    private JSONObject limits;
    private DatabaseType databaseType;

    public RowsLimitBuilder(JSONObject limits, DatabaseType databaseType) {
        this.limits = limits;
        this.databaseType = databaseType;
    }

    public String build() {

        if(limits.keySet().contains("fromRow") == false
                || limits.keySet().contains("count") == false) {
            return "";
        }

        if(this.databaseType == DatabaseType.MYSQL) {
            return " LIMIT " + limits.getString("fromRow") + " ," + limits.getString("count");
        } else {
            return "";
        }
    }
}
