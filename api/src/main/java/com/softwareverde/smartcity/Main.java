package com.softwareverde.smartcity;

import com.softwareverde.database.Database;
import com.softwareverde.database.DatabaseException;
import com.softwareverde.database.mysql.MysqlDatabase;
import com.softwareverde.smartcity.environment.Environment;

import java.io.File;
import java.sql.Connection;

public class Main {
    private static void _exitFailure() {
        System.exit(1);
    }

    private static void _printError(final String errorMessage) {
        System.err.println(errorMessage);
    }

    private static void _printUsage() {
        _printError("Usage: java -jar " + System.getProperty("java.class.path") + " <configuration-file>");
    }

    private static Database<Connection> _loadDatabase(final Configuration.DatabaseProperties databaseProperties) {
        final MysqlDatabase database = new MysqlDatabase(
            databaseProperties.getConnectionUrl(),
            databaseProperties.getUsername(),
            databaseProperties.getPassword()
        );

        try {
            database.setDatabase(databaseProperties.getSchema());
        }
        catch (final DatabaseException e) {
            return null;
        }

        return database;
    }

    private static Configuration _loadConfigurationFile(final String configurationFilename) {
        final File configurationFile =  new File(configurationFilename);
        if (! configurationFile.isFile()) {
            _printError("[ERROR: Invalid configuration file.]");
            _exitFailure();
        }

        return new Configuration(configurationFile);
    }

    public static void main(final String[] commandLineArguments) {
        if (commandLineArguments.length != 1) {
            _printUsage();
            _exitFailure();
        }

        final String configurationFilename = commandLineArguments[0];

        final Configuration configuration = _loadConfigurationFile(configurationFilename);
        final Database<Connection> database = _loadDatabase(configuration.getDatabaseProperties());
        if (database == null) {
            _printError("[NOTICE: Unable to connect to database.]");
            _exitFailure();
        }

        final Environment environment = new Environment(database);

        final Configuration.ServerProperties serverProperties = configuration.getServerProperties();

        System.out.println("[Starting Web Server]");
        final WebServer webServer = new WebServer(serverProperties, environment);
        webServer.start();

        System.out.println("[Server Online]");

        System.out.println();
        while (true) {
            try { Thread.sleep(500); } catch (final Exception e) { }
        }
    }
}