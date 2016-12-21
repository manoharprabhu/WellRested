package com.manoharprabhu.wellrested.service;

import com.manoharprabhu.wellrested.DatabaseType;
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
    public static final String ALL_MATCHING_EXPRESSION = "( 1 = 1 )";
    public static final String NO_MATCHING_EXPRESSION = "( 1 = 0 )";
    private JSONObject clauseJson;
    private DatabaseType databaseType;

    public WhereClauseBuilder(JSONObject clauseJson, DatabaseType databaseType) {
        this.clauseJson = clauseJson;this.databaseType = databaseType;
    }

    public String build() {
        if(this.clauseJson.keySet().size() == 0) {
            return ALL_MATCHING_EXPRESSION;
        }
        return this._build(this.clauseJson, databaseType).trim().replaceAll(" +", " ");
    }

    private String _build(JSONObject root, DatabaseType databaseType) {
        // Only 1 root operator or comparision clause possible
        if(this.doesObjectContainMultipleAttribute(root)) {
            return NO_MATCHING_EXPRESSION;
        }

        if(root.keySet().contains($_AND)) {
            // Loop through the value array and build expression for each value with AND separator
            return this.generateExpression(root, AND, $_AND, databaseType);
        } else if(root.keySet().contains($_OR)) {
            // Loop through the value array and build expression for each value with AND separator
            return this.generateExpression(root, OR, $_OR, databaseType);
        } else {
            // Parse and return the single expression value
            String columnName = this.getFirstAttributeName(root);
            JSONObject conditionJson = (JSONObject) root.get(columnName);
            if(this.doesObjectContainMultipleAttribute(conditionJson)) {
                return NO_MATCHING_EXPRESSION;
            }
            if(conditionJson.keySet().contains($_EQ)) {
                return this.buildSimpleExpression(columnName, " = ", conditionJson.get($_EQ), databaseType);
            } else if(conditionJson.keySet().contains($_GT)) {
                return this.buildSimpleExpression(columnName, " > ", conditionJson.get($_GT), databaseType);
            } else if(conditionJson.keySet().contains($_LT)) {
                return this.buildSimpleExpression(columnName, " < ", conditionJson.get($_LT), databaseType);
            } else {
                return NO_MATCHING_EXPRESSION;
            }
        }
     }

     private boolean doesObjectContainMultipleAttribute(JSONObject object) {
        return (object.keySet().size() != 1);
     }

     private String getFirstAttributeName(JSONObject object) {
         return (String)object.keySet().toArray()[0];
     }

     private String generateExpression(JSONObject root, String operator, String attribute, DatabaseType databaseType) {
         JSONArray conditionsArray = root.getJSONArray(attribute);
         StringBuilder result = new StringBuilder(" ( ");
         for(int i = 0; i < conditionsArray.length(); i++) {
             String parsed = this._build(conditionsArray.getJSONObject(i), databaseType);
             result.append(" ").append(parsed);
             if(i != conditionsArray.length() - 1) {
                 result.append(" ").append(operator).append(" ");
             }
         }
         result.append(" ) ");
         return result.toString();
     }

     private String buildSimpleExpression(String columnName, String operator, Object value, DatabaseType databaseType) {
        String openLimiter = "'";
        String closeLimiter = "'";
        if(databaseType == DatabaseType.MYSQL) {
            openLimiter = "`";
            closeLimiter = "`";
        } else if(databaseType == DatabaseType.SQLSERVER) {
            openLimiter = "[";
            closeLimiter = "]";
        }

        if(value instanceof String) {
            return " ( "+openLimiter + columnName + closeLimiter + " " + operator + " '" + value + "' ) ";
        } else {
            return " ( " + openLimiter + columnName + closeLimiter + " " + operator + " " + value + " ) ";
        }
     }
}
