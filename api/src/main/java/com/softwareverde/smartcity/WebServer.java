package com.softwareverde.smartcity;

import com.softwareverde.httpserver.DirectoryServlet;
import com.softwareverde.httpserver.HttpServer;
import com.softwareverde.servlet.Endpoint;
import com.softwareverde.servlet.Servlet;
import com.softwareverde.smartcity.api.parkingmeter.ParkingMeterApi;
import com.softwareverde.smartcity.api.parkingticket.ParkingTicketApi;
import com.softwareverde.smartcity.environment.Environment;

import java.io.File;

public class WebServer {
    private final Configuration.ServerProperties _serverProperties;
    private final Environment _environment;

    private final HttpServer _apiServer = new HttpServer();

    private void _assignEndpoint(final String path, final Servlet apiServlet) {
        final Endpoint endpoint = new Endpoint(apiServlet);
        endpoint.setStrictPathEnabled(true);
        endpoint.setPath(path);
        _apiServer.addEndpoint(endpoint);
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
        _assignEndpoint("/api/v1/parking-tickets", new ParkingTicketApi(_environment));

        { // Static Content
            final File servedDirectory = new File(_serverProperties.getRootDirectory() +"/");
            final DirectoryServlet indexEndpoint = new DirectoryServlet(servedDirectory);
            indexEndpoint.setShouldServeDirectories(true);
            indexEndpoint.setIndexFile("index.html");
            indexEndpoint.setShouldServeDirectories(true);

            final Endpoint endpoint = new Endpoint(indexEndpoint);
            endpoint.setStrictPathEnabled(false);
            endpoint.setPath("/");
            _apiServer.addEndpoint(endpoint);
        }

        _apiServer.start();
    }

    public void stop() {
        _apiServer.stop();
    }
}
