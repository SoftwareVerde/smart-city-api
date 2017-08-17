package com.softwareverde.smartcity.api.parkingticket;

import com.softwareverde.servlet.Servlet;
import com.softwareverde.servlet.request.Request;
import com.softwareverde.servlet.response.JsonResponse;
import com.softwareverde.servlet.response.Response;
import com.softwareverde.smartcity.api.response.JsonResult;
import com.softwareverde.smartcity.environment.Environment;

public class ParkingTicketApi implements Servlet {
    protected final Environment _environment;

    public ParkingTicketApi(final Environment environment) {
        _environment = environment;
    }

    @Override
    public Response onRequest(final Request request) {
        return new JsonResponse(Response.ResponseCodes.BAD_REQUEST, new JsonResult(false, "Nothing to do."));
    }
}
