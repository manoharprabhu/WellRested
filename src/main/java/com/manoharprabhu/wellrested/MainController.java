package com.manoharprabhu.wellrested;

import com.manoharprabhu.wellrested.service.DatabaseService;
import com.manoharprabhu.wellrested.service.MySQLDatabaseServiceImpl;
import com.manoharprabhu.wellrested.vo.Column;
import com.manoharprabhu.wellrested.vo.Table;
import com.manoharprabhu.wellrested.vo.TableRow;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.xml.ws.Response;
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
            this.databaseService = new MySQLDatabaseServiceImpl(null);
        } else {
            //TODO: Add more database support
        }
    }

    /**
     * List the tables available for the selected database
     * @return List of tables
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Table> listOfAvailableTables() {
        return databaseService.getListOfAvailableTables(
                Configuration.database,
                Configuration.hostName,
                Configuration.port,
                Configuration.username,
                Configuration.password
        );
    }

    @RequestMapping(value = "/{table}/columns", method = RequestMethod.GET)
    public List<Column> listOfColumnsOnTable(@PathVariable("table") String table) {
        return databaseService.getColumnInformationForTable(
                table,
                Configuration.database,
                Configuration.hostName,
                Configuration.port,
                Configuration.username,
                Configuration.password
        );
    }

    @RequestMapping(value = "/{table}/data", method = RequestMethod.POST)
    public List<TableRow> selectDataFromTable(@PathVariable("table") String table, @RequestBody String payload) {

        return null;
    }

}
