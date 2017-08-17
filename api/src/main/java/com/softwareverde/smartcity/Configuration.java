package com.softwareverde.smartcity;

import com.softwareverde.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
    public static class DatabaseProperties {
        private String _connectionUrl;
        private String _username;
        private String _password;
        private String _schema;

        public String getConnectionUrl() { return _connectionUrl; }
        public String getUsername() { return _username; }
        public String getPassword() { return _password; }
        public String getSchema() { return _schema; }
    }

    public static class ServerProperties {
        private String _rootDirectory;
        private String _tlsCertificateFile;
        private String _tlsKeyFile;
        private Integer _port;
        private Integer _tlsPort;
        private Integer _socketPort;

        public String getRootDirectory() { return _rootDirectory; }
        public String getTlsCertificateFile() { return _tlsCertificateFile; }
        public String getTlsKeyFile() { return _tlsKeyFile; }
        public Integer getPort() { return _port; }
        public Integer getTlsPort() { return _tlsPort; }
        public Integer getSocketPort() { return _socketPort; }
    }

    private final Properties _properties;
    private DatabaseProperties _databaseProperties;
    private ServerProperties _serverProperties;

    private void _loadDatabaseProperties() {
        _databaseProperties = new DatabaseProperties();
        _databaseProperties._connectionUrl = _properties.getProperty("database.url", "");
        _databaseProperties._username = _properties.getProperty("database.username", "");
        _databaseProperties._password = _properties.getProperty("database.password", "");
        _databaseProperties._schema = _properties.getProperty("database.schema", "");
    }

    private void _loadServerProperties() {
        _serverProperties = new ServerProperties();
        _serverProperties._rootDirectory = _properties.getProperty("server.rootDirectory", "");
        _serverProperties._port = Util.parseInt(_properties.getProperty("server.port", "80"));
        _serverProperties._tlsPort = Util.parseInt(_properties.getProperty("server.tlsPort", "443"));
        _serverProperties._socketPort = Util.parseInt(_properties.getProperty("server.socketPort", "444"));
        _serverProperties._tlsCertificateFile = _properties.getProperty("server.tlsCertificateFile", "");
        _serverProperties._tlsKeyFile = _properties.getProperty("server.tlsKeyFile", "");
    }

    public Configuration(final File configurationFile) {
        _properties = new Properties();

        try {
            _properties.load(new FileInputStream(configurationFile));
        } catch (final IOException e) { }

        _loadDatabaseProperties();

        _loadServerProperties();

    }

    public DatabaseProperties getDatabaseProperties() { return _databaseProperties; }
    public ServerProperties getServerProperties() { return _serverProperties; }
}
