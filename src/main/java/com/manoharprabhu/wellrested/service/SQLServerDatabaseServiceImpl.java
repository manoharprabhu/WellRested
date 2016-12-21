package com.manoharprabhu.wellrested.service;

import com.manoharprabhu.wellrested.Configuration;
import com.manoharprabhu.wellrested.DatabaseType;
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
 * Created by mprabhu on 12/20/2016.
 */
public class SQLServerDatabaseServiceImpl implements DatabaseService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Table> getListOfAvailableTables(String database, String hostName, int port, String username, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Table> tables = new ArrayList<>();
        try {
            connection = this.getConnectionToDatabase(database, hostName, port, username, password);
            statement = connection.prepareStatement("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_CATALOG=?");
            statement.setString(1, database);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tables.add(new Table(resultSet.getString(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
            }
            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
            }
            if(resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return tables;
    }

    @Override
    public Connection getConnectionToDatabase(String database, String hostName, int port, String username, String password) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://" + hostName + ":" + port + "/" + database + ";user=" + username + ";password=" + password + ";TDS=7.0");
        } catch (SQLException e) {
            logger.error("Error while connecting to the database.", e);
        }
        return connection;
    }

    @Override
    public List<Column> getColumnInformationForTable(String table, String database, String hostName, int port, String username, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Column> columns = new ArrayList<>();
        try {
            connection = this.getConnectionToDatabase(database, hostName, port, username, password);
            statement = connection.prepareStatement("SELECT c.name 'COLUMN_NAME', t.Name 'DATA_TYPE' FROM sys.columns c INNER JOIN sys.types t ON c.user_type_id = t.user_type_id LEFT OUTER JOIN sys.index_columns ic ON ic.object_id = c.object_id AND ic.column_id = c.column_id LEFT OUTER JOIN sys.indexes i ON ic.object_id = i.object_id AND ic.index_id = i.index_id WHERE c.object_id = OBJECT_ID(?)");
            statement.setString(1, table);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                columns.add(new Column(resultSet.getString("DATA_TYPE"), resultSet.getString("COLUMN_NAME")));
            }
        } catch (SQLException e) {
            logger.error("Error while getting the column names.", e);
            return null;
        }  finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
            }
            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
            }
            if(resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return columns;
    }

    @Override
    public List<TableRow> getDataFromTable(String table, String database, String hostName, int port, String username, String password, DatabaseType databaseType, JSONArray columns, JSONObject conditions, JSONObject limits) throws SQLException {
        String columnsToSelect = new SelectColumnBuilder(columns).build();
        String conditionsToApply = new WhereClauseBuilder(conditions, databaseType).build();
        String rowsLimitString = new RowsLimitBuilder(limits, Configuration.databaseType).build();
        String sqlToRun = "SELECT " + rowsLimitString + " " + columnsToSelect + " FROM " +"["+database+"].[dbo]." + table + " WHERE " + conditionsToApply;
        logger.info("Running query: " + sqlToRun);
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        List<TableRow> tableRowList = new ArrayList<>();
        try {
            connection = this.getConnectionToDatabase(database, hostName, port, username, password);
            statement = connection.prepareStatement(sqlToRun);
            resultSet = statement.executeQuery();
            int colCount = resultSet.getMetaData().getColumnCount();
            logger.info("Column count: " + colCount);
            while(resultSet.next()) {
                TableRow tableRow = new TableRow();
                for(int i = 0; i < colCount; i++) {
                    tableRow.addColumn(resultSet.getMetaData().getColumnLabel(i + 1), resultSet.getString(i + 1));
                }
                tableRowList.add(tableRow);
            }
        } catch (SQLSyntaxErrorException e) {
            logger.error("SQL Syntax is incorrect. Check the request JSON and make sure it is constructed properly.", e);
            throw e;
        }  finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
            }
            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
            }
            if(resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return tableRowList;
    }
}
