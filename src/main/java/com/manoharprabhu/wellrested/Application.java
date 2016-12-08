package com.manoharprabhu.wellrested;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manoharprabhu on 12/7/2016.
 */

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws CmdLineException, ClassNotFoundException, SQLException {
        try {
            System.out.println("Validating passed arguments...");
            parseArguements(args);
        } catch(CmdLineException e) {
            e.getParser().printUsage(System.out);
            throw e;
        }

        try {
            System.out.println("Loading database drivers...");
            loadDatabaseDrivers();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        }

        try {
            System.out.println("Testing connection to the database...");
            testConnectionToDatabase();
        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
        System.out.println("Starting the REST services to the database...");
        SpringApplication.run(Application.class, args);
    }

    private static void parseArguements(String[] args) throws CmdLineException {
        ProgramArguements arguements = new ProgramArguements();
        CmdLineParser parser = new CmdLineParser(arguements);
        parser.parseArgument(args);
        populateConfigurationFromArguements(arguements);
    }

    private static void populateConfigurationFromArguements(ProgramArguements arguements) {
        Configuration.database = arguements.getDatabase();
        Configuration.databaseType = (arguements.getType() == 1) ? DatabaseType.MYSQL : null;
        Configuration.hostName = arguements.getHostName();
        Configuration.username = arguements.getUsername();
        Configuration.password = arguements.getPassword();
        Configuration.port = arguements.getPort();
    }

    private static void loadDatabaseDrivers() throws ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
    }

    private static void testConnectionToDatabase() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://" + Configuration.hostName + ":" + Configuration.port + "/" + Configuration.database + "?user=" + Configuration.username + "&password=" + Configuration.password);
        connection.close();
    }
}
