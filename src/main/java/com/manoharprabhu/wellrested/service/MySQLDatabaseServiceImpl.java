package com.manoharprabhu.wellrested.service;

import com.manoharprabhu.wellrested.Configuration;
import com.manoharprabhu.wellrested.vo.Column;
import com.manoharprabhu.wellrested.vo.Table;
import com.manoharprabhu.wellrested.vo.TableRow;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manoharprabhu on 12/8/2016.
 */
public class MySQLDatabaseServiceImpl implements DatabaseService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Table> getListOfAvailableTables(String database, String hostName, int port, String username, String password) {
        Connection connection = null;
        List<Table> tables = new ArrayList<>();
        try {
            connection = this.getConnectionToDatabase(database, hostName, port, username, password);
            PreparedStatement statement = connection.prepareStatement("SHOW TABLES");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tables.add(new Table(resultSet.getString(1)));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return tables;
    }

    @Override
    public Connection getConnectionToDatabase(String database, String hostName, int port, String username, String password) {
        try {
            return DriverManager.getConnection("jdbc:mysql://" + hostName + ":" + port + "/" + database + "?user=" + username + "&password=" + password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Column> getColumnInformationForTable(String table, String database, String hostName, int port, String username, String password) {
        Connection connection = null;
        List<Column> columns = new ArrayList<>();
        try {
            connection = this.getConnectionToDatabase(database, hostName, port, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?");
            statement.setString(1, database);
            statement.setString(2, table);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                columns.add(new Column(resultSet.getString("DATA_TYPE"), resultSet.getString("COLUMN_NAME")));
            }
            connection.close();
        } catch (SQLException e) {
            logger.error("Error while getting the column names.", e);
            return null;
        }
        return columns;
    }

    @Override
    public List<TableRow> getDataFromTable(String table, String database, String hostName, int port, String username, String password, JSONArray columns, JSONObject conditions, JSONObject limits) throws SQLException {
        String columnsToSelect = new SelectColumnBuilder(columns).build();
        String conditionsToApply = new WhereClauseBuilder(conditions).build();
        String rowsLimitString = new RowsLimitBuilder(limits, Configuration.databaseType).build();
        String sqlToRun = "SELECT " + columnsToSelect + " FROM `" + table + "` WHERE " + conditionsToApply + " " + rowsLimitString;
        logger.info("Running query: " + sqlToRun);
        Connection connection = null;
        List<TableRow> tableRowList = new ArrayList<>();
        try {
            connection = this.getConnectionToDatabase(database, hostName, port, username, password);
            PreparedStatement statement = connection.prepareStatement(sqlToRun);
            ResultSet resultSet = statement.executeQuery();
            int colCount = resultSet.getMetaData().getColumnCount();
            logger.info("Column count: " + colCount);
            while(resultSet.next()) {
                TableRow tableRow = new TableRow();
                for(int i = 0; i < colCount; i++) {
                    tableRow.addColumn(resultSet.getMetaData().getColumnLabel(i + 1), resultSet.getString(i + 1));
                }
                tableRowList.add(tableRow);
            }
            connection.close();
        } catch (SQLSyntaxErrorException e) {
            logger.error("SQL Syntax is incorrect. Check the request JSON and make sure it is constructed properly.", e);
            throw e;
        }
        return tableRowList;
    }
}
