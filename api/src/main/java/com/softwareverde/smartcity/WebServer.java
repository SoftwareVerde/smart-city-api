package com.softwareverde.smartcity;

import com.softwareverde.httpserver.DirectoryServlet;
import com.softwareverde.httpserver.HttpServer;
import com.softwareverde.httpserver.endpoint.Endpoint;
import com.softwareverde.smartcity.api.parkingmeter.ParkingMeterApi;
import com.softwareverde.smartcity.environment.Environment;

import java.io.File;

public class WebServer {
    private final Configuration.ServerProperties _serverProperties;
    private final Environment _environment;

    private final HttpServer _apiServer = new HttpServer();

    private <T extends Endpoint> void _assignEndpoint(final String path, final T apiEndpoint) {
        apiEndpoint.setStrictPathEnabled(true);
        _apiServer.addEndpoint(path, apiEndpoint);
    }

    public WebServer(final Configuration.ServerProperties serverProperties, final Environment environment) {
        _serverProperties = serverProperties;
        _environment = environment;
    }

    public void start() {
        _apiServer.setPort(_serverProperties.getPort());

        _apiServer.setTlsPort(_serverProperties.getTlsPort());
        _apiServer.setCertificate(_serverProperties.getTlsCertificateFile(), _serverProperties.getTlsKeyFile());
        _apiServer.enableEncryption(true);
        _apiServer.redirectToTls(false);

        _assignEndpoint("/api/v1/parking-meters", new ParkingMeterApi(_environment));

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