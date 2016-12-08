package com.manoharprabhu.wellrested.service;

import com.manoharprabhu.wellrested.vo.Column;
import com.manoharprabhu.wellrested.vo.Table;
import com.manoharprabhu.wellrested.vo.TableRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manoharprabhu on 12/8/2016.
 */
public class MySQLDatabaseServiceImpl implements DatabaseService {

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
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + hostName + ":" + port + "/" + database + "?user=" + username + "&password=" + password);
            return connection;
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
            e.printStackTrace();
            return null;
        }
        return columns;
    }

    @Override
    public List<TableRow> getDataFromTable(String table, String database, String hostName, int port, String username, String password) {
        return null;
    }
}
