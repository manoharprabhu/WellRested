package com.manoharprabhu.wellrested.service;

import org.json.JSONObject;

/**
 * Created by manoharprabhu on 12/9/2016.
 */

/**
 * Supports AND and OR conditions
 * The JSON can be of following formats:
 *
 * Single condition
 * {$and : [{column: {$eq: value}}] }
 * {$and : [{column: {$lt: value}}] }
 * {$and : [{column: {$gt: value}}] }
 *
 * Multiple condition
 * {$and : [SINGLE_CONDITION, SINGLE_CONDITION, ...] }
 * {$or : [SINGLE_CONDITION, SINGLE_CONDITION, ...] }
 * {$and : [MULTIPLE_CONDITION, SINGLE_CONDITION, ...] }
 * {$or : [SINGLE_CONDITION, MULTIPLE_CONDITION, ...] }
 */
public class WhereClauseBuilder {
    private JSONObject clauseJson;

    public WhereClauseBuilder(JSONObject clauseJson) {
        this.clauseJson = clauseJson;
    }

    public String build() {
        return this._build(this.clauseJson);
    }

    private String _build(JSONObject root) {
        if(root.keySet().size() != 1) {
            return null;
        }

        if(root.keySet().contains("$and")) {
            // Loop through the value array and build expression for each value with AND separator
        } else if(root.keySet().contains("$or")) {
            // Loop through the value array and build expression for each value with OR separator
        } else {
            // Parse and return the single expression value
            String columnName = (String)root.keySet().toArray()[0];
            JSONObject conditionJson = (JSONObject) root.get(columnName);
            if(conditionJson.keySet().size() != 1) {
                return null;
            }
            if(conditionJson.keySet().contains("$eq")) {
                return " ( " + columnName + " = " + conditionJson.get("$eq") + " ) ";
            } else if(conditionJson.keySet().contains("$gt")) {
                return " ( " + columnName + " > " + conditionJson.get("$gt") + " ) ";
            } else if(conditionJson.keySet().contains("$lt")) {
                return " ( " + columnName + " < " + conditionJson.get("$lt") + " ) ";
            } else {
                return null;
            }
        }
        return null;
     }
}
