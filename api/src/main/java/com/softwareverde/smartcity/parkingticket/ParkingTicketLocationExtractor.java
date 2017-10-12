package com.softwareverde.smartcity.parkingticket;

import com.softwareverde.geo.util.LocationExtractor;

public class ParkingTicketLocationExtractor implements LocationExtractor<ParkingTicket> {
    @Override
    public Double getLatitude(ParkingTicket parkingTicket) {
        return parkingTicket.getLatitude();
    }

    @Override
    public Double getLongitude(ParkingTicket parkingTicket) {
        return parkingTicket.getLongitude();
    }
}
