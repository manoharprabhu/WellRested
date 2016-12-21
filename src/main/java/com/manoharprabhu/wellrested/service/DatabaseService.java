package com.manoharprabhu.wellrested.service;

import com.manoharprabhu.wellrested.DatabaseType;
import com.manoharprabhu.wellrested.vo.Column;
import com.manoharprabhu.wellrested.vo.Table;
import com.manoharprabhu.wellrested.vo.TableRow;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by manoharprabhu on 12/8/2016.
 */
public interface DatabaseService {
    public List<Table> getListOfAvailableTables(String database, String hostName, int port, String username, String password);
    public Connection getConnectionToDatabase(String database, String hostName, int port, String username, String password);
    public List<Column> getColumnInformationForTable(String table, String database, String hostName, int port, String username, String password);
    public List<TableRow> getDataFromTable(String table, String database, String hostName, int port, String username, String password, DatabaseType databaseType, JSONArray columns, JSONObject conditions, JSONObject limits) throws SQLException;
}