package com.softwareverde.smartcity.api.parkingmeter;

import com.softwareverde.database.DatabaseConnection;
import com.softwareverde.database.DatabaseException;
import com.softwareverde.database.Row;
import com.softwareverde.geo.util.GeoUtil;
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
import com.softwareverde.smartcity.parkingmeter.ParkingMeterDatabaseAdapter;
import com.softwareverde.smartcity.util.SmartCityUtil;
import com.softwareverde.util.Util;

import java.sql.Connection;
import java.util.List;

public class ParkingMeterApi implements Servlet {
    protected final Environment _environment;

    protected Response _getParkingMeterById(final PostParameters postParameters, final DatabaseConnection<Connection> databaseConnection) throws DatabaseException {
        final ParkingMeterDatabaseAdapter parkingMeterDatabaseAdapter = new ParkingMeterDatabaseAdapter(databaseConnection);

        final List<Row> rows;

        final Long id = Util.parseLong(postParameters.get("id"));
        final String meterId = postParameters.get("meter_id");

        ParkingMeter parkingMeter;
        if (id > 0) {
            parkingMeter = parkingMeterDatabaseAdapter.inflateById(id);
        }
        else {
            parkingMeter = parkingMeterDatabaseAdapter.inflateByMeterId(meterId);
        }

        if (parkingMeter == null) {
            return new JsonResponse(Response.ResponseCodes.BAD_REQUEST, new JsonResult(false, "Parking Meter Id not found."));
        }

        final GetParkingMeterResult jsonResult = new GetParkingMeterResult();
        jsonResult.setParkingMeter(parkingMeter);

        return new JsonResponse(Response.ResponseCodes.OK, jsonResult);
    }

    protected Response _listParkingMeters(final PostParameters postParameters, final DatabaseConnection<Connection> databaseConnection) throws DatabaseException {
        final ParkingMeterDatabaseAdapter parkingMeterDatabaseAdapter = new ParkingMeterDatabaseAdapter(databaseConnection);

        final List<ParkingMeter> parkingMeters = parkingMeterDatabaseAdapter.inflateAll();

        final ListParkingMeterResult jsonResult = new ListParkingMeterResult();
        for (final ParkingMeter parkingMeter : parkingMeters) {
            jsonResult.addParkingMeter(parkingMeter);
        }

        return new JsonResponse(Response.ResponseCodes.OK, jsonResult);
    }

    protected Response _searchForParkingMeter(final PostParameters postParameters, final DatabaseConnection<Connection> databaseConnection) throws DatabaseException {
        final ParkingMeterDatabaseAdapter parkingMeterDatabaseAdapter = new ParkingMeterDatabaseAdapter(databaseConnection);

        final Double radius = (postParameters.containsKey("radius") ? Util.parseDouble(postParameters.get("radius")) : null);
        final Double radiusLatitude = Util.parseDouble(postParameters.get("latitude"));
        final Double radiusLongitude = Util.parseDouble(postParameters.get("longitude"));

        final String street = (postParameters.containsKey("street") ? postParameters.get("street") : null);
        final Integer maxDwellDurationGreaterThan = (postParameters.containsKey("max_dwell_duration_greater_than") ? Util.parseInt(postParameters.get("max_dwell_duration_greater_than")) : null);
        final Integer maxDwellDurationLessThan = (postParameters.containsKey("max_dwell_duration_less_than") ? Util.parseInt(postParameters.get("max_dwell_duration_less_than")) : null);
        final Float rateGreaterThan = (postParameters.containsKey("rate_greater_than") ? Util.parseFloat(postParameters.get("rate_greater_than")) : null);
        final Float rateLessThan = (postParameters.containsKey("rate_less_than") ? Util.parseFloat(postParameters.get("rate_less_than")) : null);
        final Boolean isHandicap = (postParameters.containsKey("is_handicap") ? Util.parseBool(postParameters.get("is_handicap")) : null);
        final Boolean isChargingStation = (postParameters.containsKey("is_charging_station") ? Util.parseBool(postParameters.get("is_charging_station")) : null);

        List<ParkingMeter> matchingParkingMeters = parkingMeterDatabaseAdapter.inflateBySearchCriteria(street, maxDwellDurationGreaterThan, maxDwellDurationLessThan, rateGreaterThan, rateLessThan, isHandicap, isChargingStation);

        final ListParkingMeterResult jsonResult = new ListParkingMeterResult();
        for (final ParkingMeter parkingMeter : matchingParkingMeters) {
            final Boolean shouldBeAdded;
            if (radius == null) {
                shouldBeAdded = true;
            }
            else if (!parkingMeter.hasLatitudeAndLongitude()) {
                // when using radius, ignore any record with null lat/long
                shouldBeAdded = false;
            }
            else {
                final Double latitude = Util.coalesce(parkingMeter.getLatitude()).doubleValue();
                final Double longitude = Util.coalesce(parkingMeter.getLongitude()).doubleValue();

                final Double distance = GeoUtil.greatCircleDistanceInMeters(latitude, longitude, radiusLatitude, radiusLongitude);
                shouldBeAdded = (distance <= radius);
            }

            if (shouldBeAdded) {
                jsonResult.addParkingMeter(parkingMeter);
            }
        }

        return new JsonResponse(Response.ResponseCodes.OK, jsonResult);
    }

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
            return SmartCityUtil.generateDatabaseErrorResponse(databaseException);
        }

        final GetParameters getParameters = request.getGetParameters();
        final PostParameters postParameters = request.getPostParameters();

        /*
         * Get a parking meter by its Id.
         * GET:     get[=1]
         * POST:    id[>0]      # The Id (primary key) used for this API. Mutually exclusive from meter_id.
         *          meter_id[*] # The Columbus-Assigned Id. Mutually exclusive from id.
         */
        if (Util.parseInt(getParameters.get("get")) > 0) {
            try {
                return _getParkingMeterById(postParameters, databaseConnection);
            }
            catch (final DatabaseException databaseException) {
                return SmartCityUtil.generateDatabaseErrorResponse(databaseException);
            }
        }

        /*
         * List all parking meters.
         * GET:     list[=1]
         * POST:
         */
        if (Util.parseInt(getParameters.get("list")) > 0) {
            try {
                return _listParkingMeters(postParameters, databaseConnection);
            }
            catch (final DatabaseException databaseException) {
                return SmartCityUtil.generateDatabaseErrorResponse(databaseException);
            }
        }

        /*
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
                return _searchForParkingMeter(postParameters, databaseConnection);
            }
            catch (final DatabaseException databaseException) {
                return SmartCityUtil.generateDatabaseErrorResponse(databaseException);
            }
        }

        return new JsonResponse(Response.ResponseCodes.BAD_REQUEST, new JsonResult(false, "Nothing to do."));
    }
}
