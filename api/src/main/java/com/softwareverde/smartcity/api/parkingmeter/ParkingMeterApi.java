package com.softwareverde.smartcity.api.parkingmeter;

import com.softwareverde.database.DatabaseConnection;
import com.softwareverde.database.DatabaseException;
import com.softwareverde.database.Query;
import com.softwareverde.database.Row;
import com.softwareverde.httpserver.GetParameters;
import com.softwareverde.httpserver.PostParameters;
import com.softwareverde.httpserver.endpoint.Endpoint;
import com.softwareverde.httpserver.request.Request;
import com.softwareverde.httpserver.response.JsonResponse;
import com.softwareverde.httpserver.response.JsonResult;
import com.softwareverde.httpserver.response.Response;
import com.softwareverde.smartcity.api.parkingmeter.response.GetParkingMeterResult;
import com.softwareverde.smartcity.api.parkingmeter.response.ListParkingMeterResult;
import com.softwareverde.smartcity.environment.Environment;
import com.softwareverde.smartcity.parkingmeter.ParkingMeter;
import com.softwareverde.smartcity.parkingmeter.ParkingMeterInflater;
import com.softwareverde.util.Util;

import java.sql.Connection;
import java.util.List;

public class ParkingMeterApi extends Endpoint {
    protected final Environment _environment;

    public ParkingMeterApi(final Environment environment) {
        _environment = environment;
    }

    @Override
    public Response onRequest(final Request request) {
        final DatabaseConnection<Connection> databaseConnection;
        try {
            databaseConnection = _environment.database.newConnection();
        }
        catch (final DatabaseException databaseException) {
            return new JsonResponse(Response.ResponseCodes.SERVER_ERROR, new JsonResult(false, "Could not connect to the database."));
        }

        final GetParameters getParameters = request.getGetParameters();
        final PostParameters postParameters = request.getPostParameters();

        /**
         * Get a parking meter by its Id.
         * GET:     get[=1]
         * POST:    id[>0]
         */
        if (Util.parseInt(getParameters.get("get")) > 0) {
            try {
                final ParkingMeterInflater parkingMeterInflater = new ParkingMeterInflater();

                final List<Row> rows = databaseConnection.query(
                    new Query("SELECT * FROM parking_meters WHERE id = ?")
                        .setParameter(postParameters.get("id"))
                );

                if (rows.isEmpty()) {
                    return new JsonResponse(Response.ResponseCodes.BAD_REQUEST, new JsonResult(false, "Parking Meter Id not found."));
                }

                final GetParkingMeterResult jsonResult = new GetParkingMeterResult();
                final ParkingMeter parkingMeter = parkingMeterInflater.fromRow(rows.get(0));
                jsonResult.setParkingMeter(parkingMeter);

                return new JsonResponse(Response.ResponseCodes.OK, jsonResult);
            }
            catch (final DatabaseException databaseException) {
                return new JsonResponse(Response.ResponseCodes.SERVER_ERROR, new JsonResult(false, "An error occurred while trying to communicate with the database."));
            }
        }

        /**
         * List all parking meters.
         * GET:     list[=1]
         * POST:
         */
        if (Util.parseInt(getParameters.get("list")) > 0) {
            try {
                final ParkingMeterInflater parkingMeterInflater = new ParkingMeterInflater();

                final List<Row> rows = databaseConnection.query("SELECT * FROM parking_meters", null);

                final ListParkingMeterResult jsonResult = new ListParkingMeterResult();
                for (final Row row : rows) {
                    final ParkingMeter parkingMeter = parkingMeterInflater.fromRow(row);
                    jsonResult.addParkingMeter(parkingMeter);
                }

                return new JsonResponse(Response.ResponseCodes.OK, jsonResult);
            }
            catch (final DatabaseException databaseException) {
                return new JsonResponse(Response.ResponseCodes.SERVER_ERROR, new JsonResult(false, "An error occurred while trying to communicate with the database."));
            }
        }

        return new JsonResponse(Response.ResponseCodes.BAD_REQUEST, new JsonResult(false, "Nothing to do."));
    }
}
