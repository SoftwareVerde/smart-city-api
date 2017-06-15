package com.softwareverde.example.api.databasetime;

import com.softwareverde.database.Database;
import com.softwareverde.httpserver.GetParameters;
import com.softwareverde.httpserver.endpoint.Endpoint;
import com.softwareverde.httpserver.request.Request;
import com.softwareverde.httpserver.response.JsonResponse;
import com.softwareverde.httpserver.response.JsonResult;
import com.softwareverde.httpserver.response.Response;
import com.softwareverde.util.Util;

import java.util.List;

public class DatabaseTimeApi extends Endpoint {
    protected final Database _database;

    public DatabaseTimeApi(final Database database) {
        _database = database;
    }

    @Override
    public Response onRequest(final Request request) {
        if (! _database.isConnected()) {
            _database.connect();
        }

        if (! _database.isConnected()) {
            return new JsonResponse(Response.ResponseCodes.SERVER_ERROR, new JsonResult(false, "Could not connect to the database."));
        }

        final GetParameters getParameters = request.getGetParameters();

        if (Util.parseInt(getParameters.get("select")) > 0) {
            final List<Database.Row> rows = _database.query("SELECT UNIX_TIMESTAMP() AS time, NOW() AS formatted_time", null);
            if (rows.isEmpty()) {
                return new JsonResponse(Response.ResponseCodes.SERVER_ERROR, new JsonResult(false, "Server error."));
            }

            final Database.Row row = rows.get(0);
            final Long serverTime = (Util.parseLong(row.getValue("time")) * 1000L);
            final String formattedTime = row.getValue("formatted_time");

            final DatabaseTimeResult jsonResult = new DatabaseTimeResult();
            jsonResult.setTime(serverTime);
            jsonResult.setFormattedTime(formattedTime);
            return new JsonResponse(Response.ResponseCodes.OK, jsonResult);
        }

        return new JsonResponse(Response.ResponseCodes.BAD_REQUEST, new JsonResult(false, "Nothing to do."));
    }
}
