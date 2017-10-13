package com.softwareverde.smartcity.api.parkingticket.response;

import com.softwareverde.json.Json;
import com.softwareverde.smartcity.api.response.JsonResult;
import com.softwareverde.smartcity.parkingticket.ParkingTicket;
import com.softwareverde.smartcity.parkingticket.ParkingTicketJsonAdapter;

public class GetParkingTicketResult extends JsonResult {
    private final ParkingTicketJsonAdapter _parkingTicketJsonAdapter = new ParkingTicketJsonAdapter();
    private ParkingTicket _parkingTicket;

    public GetParkingTicketResult() {
        super(true, null);
    }

    public void setParkingTicket(final ParkingTicket parkingTicket) {
        _parkingTicket = parkingTicket;
    }

    @Override
    public Json toJson() {
        final Json json =  super.toJson();

        final Json parkingTicketJson = _parkingTicketJsonAdapter.toJson(_parkingTicket);
        json.put("parkingTicket", parkingTicketJson);

        return json;
    }
}
