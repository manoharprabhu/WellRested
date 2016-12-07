package com.manoharprabhu.wellrested.service;

import com.manoharprabhu.wellrested.vo.Table;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manoharprabhu on 12/8/2016.
 */
public class MySQLDatabaseServiceImpl implements DatabaseService {
    @Override
    public List<Table> getListOfAvailableTables(String database, String hostName, int port, String username, String password) {
        return new ArrayList<>();
    }
}
