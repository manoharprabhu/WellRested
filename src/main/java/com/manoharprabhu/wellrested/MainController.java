package com.manoharprabhu.wellrested;

import com.manoharprabhu.wellrested.service.DatabaseService;
import com.manoharprabhu.wellrested.service.MySQLDatabaseServiceImpl;
import com.manoharprabhu.wellrested.service.SQLServerDatabaseServiceImpl;
import com.manoharprabhu.wellrested.vo.Column;
import com.manoharprabhu.wellrested.vo.Table;
import com.manoharprabhu.wellrested.vo.TableRow;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        } else if(Configuration.databaseType == DatabaseType.SQLSERVER){
            this.databaseService = new SQLServerDatabaseServiceImpl();
        }
    }

    /**
     * List the tables available for the selected database
     * @return List of tables
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity listOfAvailableTables() {
        List<Table> result = databaseService.getListOfAvailableTables(
                Configuration.database,
                Configuration.hostName,
                Configuration.port,
                Configuration.username,
                Configuration.password
        );
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/{table}/columns", method = RequestMethod.GET)
    public ResponseEntity listOfColumnsOnTable(@PathVariable("table") String table) {
        List<Column> result = databaseService.getColumnInformationForTable(
                table,
                Configuration.database,
                Configuration.hostName,
                Configuration.port,
                Configuration.username,
                Configuration.password
        );
        return ResponseEntity.ok(result);
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
    @RequestMapping(value = "/{table}/getdata", method = RequestMethod.POST)
    public ResponseEntity selectDataFromTable(@PathVariable("table") String table, @RequestBody(required = false) String payload) {

        JSONObject payloadJSON;
        JSONArray columns;
        JSONObject conditions;
        JSONObject limits;
        if(payload == null) {
            columns = new JSONArray();
            conditions = new JSONObject();
            limits = new JSONObject();
        } else {
            payloadJSON = new JSONObject(payload);
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

            try {
                limits = payloadJSON.getJSONObject("limits");
            } catch (Exception e) {
                limits = new JSONObject();
            }
        }

        try {
            List<TableRow> result = databaseService.getDataFromTable(
                    table,
                    Configuration.database,
                    Configuration.hostName,
                    Configuration.port,
                    Configuration.username,
                    Configuration.password,
                    columns,
                    conditions,
                    limits
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check the conditions in JSON for syntax errors.");
        }
    }

}
