package com.manoharprabhu.wellrested;

import com.manoharprabhu.wellrested.service.DatabaseService;
import com.manoharprabhu.wellrested.service.MySQLDatabaseServiceImpl;
import com.manoharprabhu.wellrested.vo.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by manoharprabhu on 12/7/2016.
 */
@RestController
public class MainController {

    private DatabaseService databaseService;

    @PostConstruct
    public void initialize() {
        if(Configuration.databaseType == DatabaseType.MYSQL) {
            this.databaseService = new MySQLDatabaseServiceImpl();
        } else {

        }
    }

    @RequestMapping("/")
    public List<Table> listOfAvailableTables() {
        return databaseService.getListOfAvailableTables(
                Configuration.database,
                Configuration.hostName,
                Configuration.port,
                Configuration.username,
                Configuration.password
        );
    }

}
