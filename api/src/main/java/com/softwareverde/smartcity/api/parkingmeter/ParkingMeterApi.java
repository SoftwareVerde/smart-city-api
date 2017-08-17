package com.softwareverde.smartcity.api.parkingmeter;

import com.softwareverde.database.DatabaseConnection;
import com.softwareverde.database.DatabaseException;
import com.softwareverde.database.Query;
import com.softwareverde.database.Row;
import com.softwareverde.servlet.GetParameters;
import com.softwareverde.servlet.PostParameters;
import com.softwareverde.servlet.Servlet;
import com.softwareverde.servlet.request.Request;
import com.softwareverde.servlet.response.JsonResponse;
import com.softwareverde.servlet.response.Response;
import com.softwareverde.smartcity.api.parkingmeter.response.GetParkingMeterResult;
import com.softwareverde.smartcity.api.parkingmeter.response.ListParkingMeterResult;
import com.softwareverde.smartcity.api.response.JsonResult;
import com.softwareverde.smartcity.environment.Environment;
import com.softwareverde.smartcity.parkingmeter.ParkingMeter;
import com.softwareverde.smartcity.parkingmeter.ParkingMeterInflater;
import com.softwareverde.util.Util;

import java.sql.Connection;
import java.util.List;

public class ParkingMeterApi implements Servlet {
    protected final Environment _environment;

    public ParkingMeterApi(final Environment environment) {
        _environment = environment;
    }

    protected Double _measureDistance(final Double latitude, final Double longitude, final Double latitude2, final Double longitude2) {
        final Double R = 6378.137D; // Radius of earth in KM
        final Double dLat = latitude2 * Math.PI / 180D - latitude * Math.PI / 180D;
        final Double dLon = longitude2 * Math.PI / 180D - longitude * Math.PI / 180D;
        final Double a = Math.sin(dLat / 2D) * Math.sin(dLat / 2D) + Math.cos(latitude * Math.PI / 180D) * Math.cos(latitude2 * Math.PI / 180D) * Math.sin(dLon / 2D) * Math.sin(dLon / 2D);
        final Double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
        final Double d = R * c;
        return d * 1000D; // In meters...
    }

    protected <T> String _toNonNullString(T object) {
        if (object == null) {
            return "";
        }
        return object.toString();
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
         * POST:    id[>0]      # The Id (primary key) used for this API. Mutually exclusive from meter_id.
         *          meter_id[*] # The Columbus-Assigned Id. Mutually exclusive from id.
         */
        if (Util.parseInt(getParameters.get("get")) > 0) {
            try {
                final ParkingMeterInflater parkingMeterInflater = new ParkingMeterInflater();

                final List<Row> rows;

                final Long id = Util.parseLong(postParameters.get("id"));
                final String meterId = postParameters.get("meter_id");

                if (id > 0) {
                    rows = databaseConnection.query(
                        new Query("SELECT * FROM parking_meters WHERE id = ?")
                            .setParameter(id)
                    );
                }
                else {
                    rows = databaseConnection.query(
                        new Query("SELECT * FROM parking_meters WHERE meter_id = ?")
                            .setParameter(meterId)
                    );
                }

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

        /**
         * Search for parking meters.
         * GET:     search[=1]
         * POST:    radius[>=0], latitude[*], longitude[*]  # Radius (in meters) from its Latitude/Longitude to the provided latitude and longitude. (Inclusive) (Optional)
         *          street[*]                               # Match on Location, including wildcards (via "%"). (Optional) (e.x. "front%" will match both "Front ST N" and "Front ST S")
         *          max_dwell_duration_greater_than[>=0]    # Return Parking Meters whose Max Dwell Duration is greater than max_dwell_duration_greater_than (in minutes) (inclusive). (Optional)
         *          max_dwell_duration_less_than[>=0]       # Return Parking Meters whose Max Dwell Duration is less than max_dwell_duration_less_than (in minutes) (inclusive). (Optional)
         *          rate_greater_than[>=0]                  # Return Parking Meters whose Rate is greater than rate_greater_than (in dollars) (inclusive). (Optional)
         *          rate_less_than[>=0]                     # Return Parking Meters whose Rate is less than rate_less_than (in dollars) (inclusive). (Optional)
         *          is_handicap[=0|1]                       # Return only Parking Meters designated as handicap when set to 1, or not handicap when set to 0. (Optional)
         *          is_charging_station[=0|1]               # Return only Parking Meters designated as a charging station when set to 1, or not a charging station when set to 0. (Optional)
         */
        if (Util.parseInt(getParameters.get("search")) > 0) {
            try {
                final ParkingMeterInflater parkingMeterInflater = new ParkingMeterInflater();

                final Double radius = (postParameters.containsKey("radius") ? Util.parseDouble(postParameters.get("radius")) : null);
                final Double radiusLatitude = Util.parseDouble(postParameters.get("latitude"));
                final Double radiusLongitude = Util.parseDouble(postParameters.get("longitude"));

                final String street = (postParameters.containsKey("street") ? postParameters.get("street") : null);
                final Integer maxDwellDurationGreaterThan = (postParameters.containsKey("max_dwell_duration_greater_than") ? Util.parseInt(postParameters.get("max_dwell_duration_greater_than")) : null);
                final Integer maxDwellDurationLessThan = (postParameters.containsKey("max_dwell_duration_less_than") ? Util.parseInt(postParameters.get("max_dwell_duration_less_than")) : null);
                final Float rateGreaterThan = (postParameters.containsKey("rate_greater_than") ? Util.parseFloat(postParameters.get("rate_greater_than")) : null);
                final Float rateLessThan = (postParameters.containsKey("rate_less_than") ? Util.parseFloat(postParameters.get("rate_less_than")) : null);
                final Integer isHandicap = (postParameters.containsKey("is_handicap") ? (Util.parseBool(postParameters.get("is_handicap")) ? 1 : 0) : null);
                final Integer isChargingStation = (postParameters.containsKey("is_charging_station") ? (Util.parseBool(postParameters.get("is_charging_station")) ? 1 : 0) : null);

                final List<Row> rows = databaseConnection.query(
                    "SELECT * FROM parking_meters WHERE"
                        +" (LENGTH(?) = 0 OR location LIKE ?)"
                        +" AND (LENGTH(?) = 0 OR max_dwell_duration >= ?)"
                        +" AND (LENGTH(?) = 0 OR max_dwell_duration <= ?)"
                        +" AND (LENGTH(?) = 0 OR rate >= ?)"
                        +" AND (LENGTH(?) = 0 OR rate <= ?)"
                        +" AND (LENGTH(?) = 0 OR is_handicap = ?)"
                        +" AND (LENGTH(?) = 0 OR is_charging_station = ?)",
                    new String[] {
                        _toNonNullString(street),                       _toNonNullString(street),
                        _toNonNullString(maxDwellDurationGreaterThan),  _toNonNullString(maxDwellDurationGreaterThan),
                        _toNonNullString(maxDwellDurationLessThan),     _toNonNullString(maxDwellDurationLessThan),
                        _toNonNullString(rateGreaterThan),              _toNonNullString(rateGreaterThan),
                        _toNonNullString(rateLessThan),                 _toNonNullString(rateLessThan),
                        _toNonNullString(isHandicap),                   _toNonNullString(isHandicap),
                        _toNonNullString(isChargingStation),            _toNonNullString(isChargingStation)
                    }
                );

                final ListParkingMeterResult jsonResult = new ListParkingMeterResult();
                for (final Row row : rows) {
                    final ParkingMeter parkingMeter = parkingMeterInflater.fromRow(row);

                    final Boolean shouldBeAdded;
                    if (radius == null) {
                        shouldBeAdded = true;
                    }
                    else {
                        final Double latitude = Util.coalesce(parkingMeter.getLatitude()).doubleValue();
                        final Double longitude = Util.coalesce(parkingMeter.getLongitude()).doubleValue();

                        final Double distance = _measureDistance(latitude, longitude, radiusLatitude, radiusLongitude);
                        shouldBeAdded = (distance <= radius);
                    }

                    if (shouldBeAdded) {
                        jsonResult.addParkingMeter(parkingMeter);
                    }
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
