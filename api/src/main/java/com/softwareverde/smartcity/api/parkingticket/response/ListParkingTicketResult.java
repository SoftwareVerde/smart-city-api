package com.softwareverde.smartcity.api.parkingticket.response;

import com.softwareverde.json.Json;
import com.softwareverde.smartcity.api.response.JsonResult;
import com.softwareverde.smartcity.parkingticket.ParkingTicket;
import com.softwareverde.smartcity.parkingticket.ParkingTicketJsonAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListParkingTicketResult extends JsonResult {
    protected final ParkingTicketJsonAdapter _parkingTicketJsonAdapter = new ParkingTicketJsonAdapter();
    protected final List<ParkingTicket> _parkingTickets = new ArrayList<ParkingTicket>();

    public ListParkingTicketResult() {
        super(true, null);
    }

    public void addParkingTicket(final ParkingTicket parkingTicket) {
        _parkingTickets.add(parkingTicket);
    }
    public void clearParkingTickets() {
        _parkingTickets.clear();
    }

    @Override
    public Json toJson() {
        final Json json =  super.toJson();

        final Json parkingTicketsJson = new Json();
        for (final ParkingTicket parkingTicket : _parkingTickets) {
            final Json parkingTicketJson = _parkingTicketJsonAdapter.toJson(parkingTicket);
            parkingTicketsJson.add(parkingTicketJson);
        }
        json.put("parkingTickets", parkingTicketsJson);

        return json;
    }
}
