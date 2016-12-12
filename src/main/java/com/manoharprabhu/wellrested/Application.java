package com.manoharprabhu.wellrested;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by manoharprabhu on 12/7/2016.
 */
@SpringBootApplication
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) throws CmdLineException, ClassNotFoundException, SQLException {
        try {
            logger.info("Validating passed arguments...");
            parseArguements(args);
        } catch(CmdLineException e) {
            e.getParser().printUsage(System.out);
            throw e;
        }

        try {
            logger.info("Loading database drivers...");
            loadDatabaseDrivers();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        }

        try {
            logger.info("Testing connection to the database...");
            testConnectionToDatabase();
        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
        logger.info("Starting the REST services to the database...");
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
