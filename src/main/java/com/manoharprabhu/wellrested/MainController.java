package com.manoharprabhu.wellrested;

import com.manoharprabhu.wellrested.service.DatabaseService;
import com.manoharprabhu.wellrested.service.MySQLDatabaseServiceImpl;
import com.manoharprabhu.wellrested.vo.Column;
import com.manoharprabhu.wellrested.vo.Table;
import com.manoharprabhu.wellrested.vo.TableRow;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

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

    /**
     *  Payload format:
     *  {
     *      columns: ["col1", "col2", "col3"...],
     *      conditions: {
     *          $and: [
     *              {
     *                  col1: {$lt: 6}
     *              },{
     *                  col2: {$eq: "Fire"}
     *              }
     *          ]
     *      }
     *  }
     * @param table
     * @param payload
     * @return list of rows selected from the table satisfying the condition
     */
    @RequestMapping(value = "/{table}/data", method = RequestMethod.POST)
    public List<TableRow> selectDataFromTable(@PathVariable("table") String table, @RequestBody String payload) {
        JSONObject payloadJSON = new JSONObject(payload);
        JSONArray columns;
        JSONObject conditions;
        try {
            columns = payloadJSON.getJSONArray("columns");
        } catch (Exception e) {
            columns = new JSONArray();
        }

        try {
            conditions = payloadJSON.getJSONObject("conditions");
        } catch (Exception e) {
            conditions = new JSONObject();
        }

        return databaseService.getDataFromTable(
                table,
                Configuration.database,
                Configuration.hostName,
                Configuration.port,
                Configuration.username,
                Configuration.password,
                columns,
                conditions
        );
    }

}
