package com.softwareverde.smartcity.api.parkingticket;

import com.softwareverde.database.DatabaseConnection;
import com.softwareverde.database.DatabaseException;
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
import com.softwareverde.smartcity.util.SmartCityUtil;
import com.softwareverde.util.Util;

import java.sql.Connection;
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
         * List all parking meters.
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
}
