package com.softwareverde.smartcity.parkingticket;

import com.softwareverde.json.Json;
import com.softwareverde.smartcity.licenseplate.LicensePlate;
import com.softwareverde.smartcity.licenseplate.LicensePlateDatabaseAdapter;
import com.softwareverde.smartcity.licenseplate.LicensePlateJsonAdapter;
import com.softwareverde.util.DateUtil;

public class ParkingTicketJsonAdapter {
    public Json toJson(final ParkingTicket parkingTicket) {
        final LicensePlateJsonAdapter licensePlateJsonAdapter = new LicensePlateJsonAdapter();

        final Long id = parkingTicket.getId();
        final long dateUnixTime = parkingTicket.getDate().getTime();
        final String date = DateUtil.timestampToDatetimeString(dateUnixTime);
        final String ticketNumber = parkingTicket.getTicketNumber();
        final LicensePlate licensePlate = parkingTicket.getLicensePlate();
        final Json licensePlateJson = licensePlateJsonAdapter.toJson(licensePlate);
        final String violationCode = parkingTicket.getViolationCode();
        final String location = parkingTicket.getLocation();
        final Double fineAmount = parkingTicket.getFineAmount();
        final Double paidAmount = parkingTicket.getPaidAmount();
        final Double dueAmount = parkingTicket.getDueAmount();
        final String disposition = parkingTicket.getDisposition();
        final Double latitude = parkingTicket.getLatitude();
        final Double longitude = parkingTicket.getLongitude();

        final Json json = new Json();

        json.put("id", id);
        json.put("date", date);
        json.put("ticketNumber", ticketNumber);
        json.put("licensePlate", licensePlateJson);
        json.put("violationCode", violationCode);
        json.put("location", location);
        json.put("fineAmount", fineAmount);
        json.put("paidAmount", paidAmount);
        json.put("dueAmount", dueAmount);
        json.put("disposition", disposition);
        json.put("latitude", latitude);
        json.put("longitude", longitude);

        return json;
    }
}
