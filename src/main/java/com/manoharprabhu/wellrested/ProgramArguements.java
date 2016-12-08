package com.manoharprabhu.wellrested;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;
import org.kohsuke.args4j.spi.IntOptionHandler;

/**
 * Created by mprabhu on 12/8/2016.
 */
public class ProgramArguements {

    @Option(name = "-h", usage = "Hostname of the database", required = true)
    private String hostName;

    @Option(name = "-u", usage = "Username for connecting to the database", required = true)
    private String username;

    @Option(name = "-pwd", usage = "Password for connecting to the database", required = true)
    private String password;

    @Option(name = "-p", usage = "Port to connect to the database", required = true)
    private int port;

    @Option(name = "-d", usage = "Name of the database to connect", required = true)
    private String database;

    @Option(name = "-t", handler = IntOptionHandler.class, usage = "Type of the database. 1 = MYSQL", required = true)
    private int type;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
