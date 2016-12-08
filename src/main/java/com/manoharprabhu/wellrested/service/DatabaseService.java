package com.manoharprabhu.wellrested.service;

import com.manoharprabhu.wellrested.vo.Column;
import com.manoharprabhu.wellrested.vo.Table;
import com.manoharprabhu.wellrested.vo.TableRow;

import java.sql.Connection;
import java.util.List;

/**
 * Created by manoharprabhu on 12/8/2016.
 */
public interface DatabaseService {
    public List<Table> getListOfAvailableTables(String database, String hostName, int port, String username, String password);
    public Connection getConnectionToDatabase(String database, String hostName, int port, String username, String password);
    public List<Column> getColumnInformationForTable(String table, String database, String hostName, int port, String username, String password);
    public List<TableRow> getDataFromTable(String table, String database, String hostName, int port, String username, String password);
}