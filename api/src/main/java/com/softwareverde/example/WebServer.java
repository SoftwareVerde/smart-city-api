package com.softwareverde.example;

import com.softwareverde.database.Database;
import com.softwareverde.example.api.servertime.ServerTimeApi;
import com.softwareverde.httpserver.DirectoryServlet;
import com.softwareverde.httpserver.HttpServer;
import com.softwareverde.httpserver.endpoint.Endpoint;

import java.io.File;

public class WebServer {
    private final Configuration.ServerProperties _serverProperties;
    private final Database _database;

    private final HttpServer _apiServer = new HttpServer();

    private <T extends Endpoint> void _assignEndpoint(final String path, final T apiEndpoint) {
        apiEndpoint.setStrictPathEnabled(true);
        _apiServer.addEndpoint(path, apiEndpoint);
    }

    public WebServer(final Configuration.ServerProperties serverProperties, final Database database) {
        _serverProperties = serverProperties;
        _database = database;
    }

    public void start() {
        _apiServer.setPort(_serverProperties.getPort());

        _apiServer.setTlsPort(_serverProperties.getTlsPort());
        _apiServer.setCertificate(_serverProperties.getTlsCertificateFile(), _serverProperties.getTlsKeyFile());
        _apiServer.enableEncryption(true);
        _apiServer.redirectToTls(false);

        { // Server Time Api
            // Path:                /api/server/time
            // GET (Methods):       select
            // POST (Parameters):
            _assignEndpoint("/api/parking-meters/", new ServerTimeApi());
        }

        { // Static Content
            final File servedDirectory = new File(_serverProperties.getRootDirectory() +"/");
            final DirectoryServlet indexEndpoint = new DirectoryServlet(servedDirectory);
            indexEndpoint.setShouldServeDirectories(true);
            indexEndpoint.setIndexFile("index.html");
            _apiServer.addEndpoint("/", indexEndpoint);
        }

        _apiServer.start();
    }

    public void stop() {
        _apiServer.stop();
    }
}