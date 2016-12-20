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
        return null;
    }

    @Override
    public List<TableRow> getDataFromTable(String table, String database, String hostName, int port, String username, String password, JSONArray columns, JSONObject conditions, JSONObject limits) throws SQLException {
        return null;
    }
}
