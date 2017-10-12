package com.softwareverde.smartcity.api.parkingticket;

import com.softwareverde.database.DatabaseConnection;
import com.softwareverde.database.DatabaseException;
import com.softwareverde.geo.util.GeoUtil;
import com.softwareverde.servlet.GetParameters;
import com.softwareverde.servlet.PostParameters;
import com.softwareverde.servlet.Servlet;
import com.softwareverde.servlet.request.Request;
import com.softwareverde.servlet.response.JsonResponse;
import com.softwareverde.servlet.response.Response;
import com.softwareverde.smartcity.api.parkingticket.response.GetParkingTicketResult;
import com.softwareverde.smartcity.api.parkingticket.response.ListParkingTicketResult;
import com.softwareverde.smartcity.api.response.JsonResult;
import com.softwareverde.smartcity.environment.Environment;
import com.softwareverde.smartcity.parkingticket.ParkingTicket;
import com.softwareverde.smartcity.parkingticket.ParkingTicketDatabaseAdapter;
import com.softwareverde.smartcity.parkingticket.ParkingTicketLocationExtractor;
import com.softwareverde.smartcity.util.SmartCityUtil;
import com.softwareverde.util.DateUtil;
import com.softwareverde.util.Util;

import java.sql.Connection;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ParkingTicketApi implements Servlet {
    protected final Environment _environment;

    public ParkingTicketApi(final Environment environment) {
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
         * Get a parking ticket by its ID.
         * GET:     get[=1]
         * POST:    id[>0]           # The ID (primary key) used for this API. Mutually exclusive from ticket number.
         *          ticket_number[*] # The Columbus-assigned ticket number. Mutually exclusive from ID.
         */
        if (Util.parseInt(getParameters.get("get")) > 0) {
            try {
                return _getParkingTicketById(postParameters, databaseConnection);
            }
            catch (final DatabaseException databaseException) {
                return SmartCityUtil.generateDatabaseErrorResponse(databaseException);
            }
        }

        /*
         * List all parking tickets.
         * GET:     list[=1]
         * POST:
         */
        if (Util.parseInt(getParameters.get("list")) > 0) {
            try {
                return _listParkingTickets(postParameters, databaseConnection);
            }
            catch (final DatabaseException databaseException) {
                return SmartCityUtil.generateDatabaseErrorResponse(databaseException);
            }
        }

        /*
         * Search for parking tickets.
         * GET:     search[=1]
         * POST:    radius[>=0], latitude[*], longitude[*]  # Radius (in meters) from its Latitude/Longitude to the provided latitude and longitude (inclusive). (Optional)
         *          street[*]                               # Match on location, including wildcards (via "%"). (Optional) (e.x. "front%" will match both "Front ST N" and "Front ST S")
         *          date_after[yyyy-MM-dd HH:mm:ss]         # Return parking tickets whose date is after than date_greater_than (inclusive). (Optional)
         *          date_before[yyyy-MM-dd HH:mm:ss]        # Return parking tickets whose date is before than date_less_than (inclusive). (Optional)
         *          license_plate_number[*]                 # Match on license_plate_number, including wildcards (via "%"). (Optional)
         *          license_plate_state[*]                  # Return parking tickets with license plate two-letter state equal to license_plate_state. (Optional)
         *          violation_code[*]                       # Match on violation_code, including wildcards (via "%"). (Optional)
         *          fine_amount_greater_than[>=0]           # Return parking tickets whose fine amount is greater than fine_amount_greater_than. (Optional)
         *          fine_amount_less_than[>=0]              # Return parking tickets whose fine amount is less than fine_amount_less_than. (Optional)
         *          paid_amount_greater_than[>=0]           # Return parking tickets whose paid amount is greater than paid_amount_greater_than. (Optional)
         *          paid_amount_less_than[>=0]              # Return parking tickets whose paid amount is less than paid_amount_less_than. (Optional)
         *          due_amount_greater_than[>=0]            # Return parking tickets whose amount due is greater than due_amount_greater_than. (Optional)
         *          due_amount_less_than[>=0]               # Return parking tickets whose amount due is less than due_amount_less_than. (Optional)
         *          disposition[*]                          # Match on disposition, including wildcards (via "%"). (Optional)
         */
        if (Util.parseInt(getParameters.get("search")) > 0) {
            try {
                return _searchForParkingTicket(postParameters, databaseConnection);
            }
            catch (final DatabaseException databaseException) {
                return SmartCityUtil.generateDatabaseErrorResponse(databaseException);
            }
        }

        return new JsonResponse(Response.ResponseCodes.BAD_REQUEST, new JsonResult(false, "Nothing to do."));
    }

    private Response _listParkingTickets(final PostParameters postParameters, final DatabaseConnection<Connection> databaseConnection) throws DatabaseException {
        final ParkingTicketDatabaseAdapter parkingTicketDatabaseAdapter = new ParkingTicketDatabaseAdapter(databaseConnection);

        final ListParkingTicketResult jsonResult = new ListParkingTicketResult();
        final List<ParkingTicket> parkingTickets = parkingTicketDatabaseAdapter.inflateAll();
        for (final ParkingTicket parkingTicket : parkingTickets) {
            jsonResult.addParkingTicket(parkingTicket);
        }

        return new JsonResponse(Response.ResponseCodes.OK, jsonResult);
    }

    protected Response _getParkingTicketById(final PostParameters postParameters, final DatabaseConnection<Connection> databaseConnection) throws DatabaseException {
        final ParkingTicketDatabaseAdapter parkingTicketDatabaseAdapter = new ParkingTicketDatabaseAdapter(databaseConnection);

        final Long id = Util.parseLong(postParameters.get("id"));
        final String meterId = postParameters.get("ticket_number");

        ParkingTicket parkingTicket;
        if (id > 0) {
            parkingTicket = parkingTicketDatabaseAdapter.inflateById(id);
        }
        else {
            parkingTicket = parkingTicketDatabaseAdapter.inflateByTicketNumber(meterId);
        }

        if (parkingTicket == null) {
            return new JsonResponse(Response.ResponseCodes.BAD_REQUEST, new JsonResult(false, "Parking ticket not found."));
        }

        final GetParkingTicketResult jsonResult = new GetParkingTicketResult();
        jsonResult.setParkingTicket(parkingTicket);

        return new JsonResponse(Response.ResponseCodes.OK, jsonResult);
    }

    private Response _searchForParkingTicket(PostParameters postParameters, DatabaseConnection<Connection> databaseConnection) throws DatabaseException {
        final ParkingTicketDatabaseAdapter parkingTicketDatabaseAdapter = new ParkingTicketDatabaseAdapter(databaseConnection);

        final Double radius = (postParameters.containsKey("radius") ? Util.parseDouble(postParameters.get("radius")) : null);
        final Double radiusLatitude = Util.parseDouble(postParameters.get("latitude"));
        final Double radiusLongitude = Util.parseDouble(postParameters.get("longitude"));

        final String street = (postParameters.containsKey("street") ? postParameters.get("street") : null);
        final String dateAfterString = (postParameters.containsKey("date_after") ? postParameters.get("date_after") : null);
        final Date dateAfter = dateAfterString == null ? null : new Date(DateUtil.datetimeToTimestamp(dateAfterString));
        final String dateBeforeString = (postParameters.containsKey("date_before") ? postParameters.get("date_before") : null);
        final Date dateBefore = dateBeforeString == null ? null : new Date(DateUtil.datetimeToTimestamp(dateBeforeString));
        final String licensePlateNumber = (postParameters.containsKey("license_plate_number") ? postParameters.get("license_plate_number") : null);
        final String licensePlateState = (postParameters.containsKey("license_plate_state") ? postParameters.get("license_plate_state") : null);
        final String violationCode = (postParameters.containsKey("violation_code") ? postParameters.get("violation_code") : null);
        final Double fineAmountGreaterThan = (postParameters.containsKey("fine_amount_greater_than") ? Util.parseDouble(postParameters.get("fine_amount_greater_than")) : null);
        final Double fineAmountLessThan = (postParameters.containsKey("fine_amount_less_than") ? Util.parseDouble(postParameters.get("fine_amount_less_than")) : null);
        final Double paidAmountGreaterThan = (postParameters.containsKey("paid_amount_greater_than") ? Util.parseDouble(postParameters.get("paid_amount_greater_than")) : null);
        final Double paidAmountLessThan = (postParameters.containsKey("paid_amount_less_than") ? Util.parseDouble(postParameters.get("paid_amount_less_than")) : null);
        final Double dueAmountGreaterThan = (postParameters.containsKey("due_amount_greater_than") ? Util.parseDouble(postParameters.get("due_amount_greater_than")) : null);
        final Double dueAmountLessThan = (postParameters.containsKey("due_amount_less_than") ? Util.parseDouble(postParameters.get("due_amount_less_than")) : null);
        final String disposition = (postParameters.containsKey("disposition") ? postParameters.get("disposition") : null);

        List<ParkingTicket> matchingParkingTickets = parkingTicketDatabaseAdapter.inflateBySearchCriteria(street, dateAfter, dateBefore, licensePlateNumber, licensePlateState, violationCode, fineAmountGreaterThan, fineAmountLessThan, paidAmountGreaterThan, paidAmountLessThan, dueAmountGreaterThan, dueAmountLessThan, disposition);
        Collection<ParkingTicket> locationFilteredParkingTickets = GeoUtil.filterByLocation(matchingParkingTickets, new ParkingTicketLocationExtractor(), radius, radiusLatitude, radiusLongitude);

        final ListParkingTicketResult jsonResult = new ListParkingTicketResult();
        for (final ParkingTicket parkingTicket : locationFilteredParkingTickets) {
            jsonResult.addParkingTicket(parkingTicket);
        }

        return new JsonResponse(Response.ResponseCodes.OK, jsonResult);
    }
}
