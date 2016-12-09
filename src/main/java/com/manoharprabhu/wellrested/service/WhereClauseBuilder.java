package com.manoharprabhu.wellrested.service;

import org.json.JSONArray;
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
        return this._build(this.clauseJson).trim().replaceAll(" +", " ");
    }

    private String _build(JSONObject root) {
        if(!this.doesObjectContainSingleAttribute(root)) {
            return null;
        }

        if(root.keySet().contains("$and")) {
            // Loop through the value array and build expression for each value with AND separator
            JSONArray conditionsArray = root.getJSONArray("$and");
            StringBuilder result = new StringBuilder(" ( ");
            for(int i = 0; i < conditionsArray.length(); i++) {
                String parsed = this._build(conditionsArray.getJSONObject(i));
                result.append(" ").append(parsed);
                if(i != conditionsArray.length() - 1) {
                    result.append(" AND ");
                }
            }
            result.append(" ) ");
            return result.toString();
        } else if(root.keySet().contains("$or")) {
            // Loop through the value array and build expression for each value with AND separator
            JSONArray conditionsArray = root.getJSONArray("$or");
            StringBuilder result = new StringBuilder(" ( ");
            for(int i = 0; i < conditionsArray.length(); i++) {
                String parsed = this._build(conditionsArray.getJSONObject(i));
                result.append(" ").append(parsed);
                if(i != conditionsArray.length() - 1) {
                    result.append(" OR ");
                }
            }
            result.append(" ) ");
            return result.toString();
        } else {
            // Parse and return the single expression value
            String columnName = this.getFirstAttributeName(root);
            JSONObject conditionJson = (JSONObject) root.get(columnName);
            if(!this.doesObjectContainSingleAttribute(conditionJson)) {
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
     }

     private boolean doesObjectContainSingleAttribute(JSONObject object) {
        return (object.keySet().size() == 1);
     }

     private String getFirstAttributeName(JSONObject object) {
         return (String)object.keySet().toArray()[0];
     }
}
