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
    private static final String $_AND = "$and";
    private static final String $_OR = "$or";
    private static final String $_EQ = "$eq";
    private static final String $_GT = "$gt";
    private static final String $_LT = "$lt";
    private static final String AND = "AND";
    private static final String OR = "OR";
    private JSONObject clauseJson;

    public WhereClauseBuilder(JSONObject clauseJson) {
        this.clauseJson = clauseJson;
    }

    public String build() {
        if(this.clauseJson.keySet().size() == 0) {
            return " ( 1 = 1 ) ";
        }
        return this._build(this.clauseJson).trim().replaceAll(" +", " ");
    }

    private String _build(JSONObject root) {
        // Only 1 root operator or comparision clause possible
        if(this.doesObjectContainMultipleAttribute(root)) {
            return null;
        }

        if(root.keySet().contains($_AND)) {
            // Loop through the value array and build expression for each value with AND separator
            return this.generateExpression(root, AND, $_AND);
        } else if(root.keySet().contains($_OR)) {
            // Loop through the value array and build expression for each value with AND separator
            return this.generateExpression(root, OR, $_OR);
        } else {
            // Parse and return the single expression value
            String columnName = this.getFirstAttributeName(root);
            JSONObject conditionJson = (JSONObject) root.get(columnName);
            if(this.doesObjectContainMultipleAttribute(conditionJson)) {
                return null;
            }
            if(conditionJson.keySet().contains($_EQ)) {
                return this.buildSimpleExpression(columnName, " = ", conditionJson.get($_EQ));
            } else if(conditionJson.keySet().contains($_GT)) {
                return this.buildSimpleExpression(columnName, " > ", conditionJson.get($_GT));
            } else if(conditionJson.keySet().contains($_LT)) {
                return this.buildSimpleExpression(columnName, " < ", conditionJson.get($_LT));
            } else {
                return null;
            }
        }
     }

     private boolean doesObjectContainMultipleAttribute(JSONObject object) {
        return (object.keySet().size() != 1);
     }

     private String getFirstAttributeName(JSONObject object) {
         return (String)object.keySet().toArray()[0];
     }

     private String generateExpression(JSONObject root, String operator, String attribute) {
         JSONArray conditionsArray = root.getJSONArray(attribute);
         StringBuilder result = new StringBuilder(" ( ");
         for(int i = 0; i < conditionsArray.length(); i++) {
             String parsed = this._build(conditionsArray.getJSONObject(i));
             result.append(" ").append(parsed);
             if(i != conditionsArray.length() - 1) {
                 result.append(" ").append(operator).append(" ");
             }
         }
         result.append(" ) ");
         return result.toString();
     }

     private String buildSimpleExpression(String columnName, String operator, Object value) {
        if(value instanceof String) {
            return " ( `" + columnName + "` " + operator + " '" + value + "' ) ";
        } else {
            return " ( `" + columnName + "` " + operator + " " + value + " ) ";
        }
     }
}
