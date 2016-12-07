package com.manoharprabhu.wellrested.service;

import com.manoharprabhu.wellrested.vo.Table;

import java.util.List;

/**
 * Created by manoharprabhu on 12/8/2016.
 */
public interface DatabaseService {
    public List<Table> getListOfAvailableTables(String database, String hostName, int port, String username, String password);
}
