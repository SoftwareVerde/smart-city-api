package com.softwareverde.smartcity.parkingticket;

import com.softwareverde.database.DatabaseConnection;
import com.softwareverde.database.DatabaseException;
import com.softwareverde.database.Row;
import com.softwareverde.smartcity.licenseplate.LicensePlate;
import com.softwareverde.smartcity.licenseplate.LicensePlateDatabaseAdapter;
import com.softwareverde.util.DateUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParkingTicketDatabaseAdapter {

    private DatabaseConnection<Connection> _databaseConnection;

    public ParkingTicketDatabaseAdapter(final DatabaseConnection<Connection> databaseConnection) {
        _databaseConnection = databaseConnection;
    }

    public List<ParkingTicket> inflateAll() throws DatabaseException {
        final List<Row> rows = _databaseConnection.query("SELECT parking_tickets.*, license_plates.number AS license_plate_number, license_plates.state AS license_plate_state FROM parking_tickets LEFT OUTER JOIN license_plates ON parking_tickets.license_plate_id = license_plates.id", null);

        final List<ParkingTicket> parkingTickets = new ArrayList<>();
        for (final Row row : rows) {
            final ParkingTicket parkingTicket = _fromRowWithInlineChildren(row);
            parkingTickets.add(parkingTicket);
        }
        return parkingTickets;
    }

    private ParkingTicket _fromRow(final Row row) throws DatabaseException {
        LicensePlateDatabaseAdapter licensePlateDatabaseAdapter = new LicensePlateDatabaseAdapter(_databaseConnection);
        final Long licensePlateId = row.getLong("license_plate_id");
        final LicensePlate licensePlate = licensePlateDatabaseAdapter.inflateById(licensePlateId);

        final ParkingTicket parkingTicket = _fromRowWithoutChildren(row);
        parkingTicket.setLicensePlate(licensePlate);
        return parkingTicket;
    }

    private ParkingTicket _fromRowWithInlineChildren(final Row row) throws DatabaseException {
        final Long licensePlateId = row.getLong("license_plate_id");
        final String licensePlateNumber = row.getString("license_plate_number");
        final String licensePlateState = row.getString("license_plate_state");

        final LicensePlate licensePlate = new LicensePlate(licensePlateId, licensePlateNumber, licensePlateState);

        final ParkingTicket parkingTicket = _fromRowWithoutChildren(row);
        parkingTicket.setLicensePlate(licensePlate);
        return parkingTicket;
    }

    private ParkingTicket _fromRowWithoutChildren(final Row row) throws DatabaseException {
        final Long id = row.getLong("id");
        final String dateString = row.getString("date");
        final Date date = new Date(DateUtil.datetimeToTimestamp(dateString));
        final String ticketNumber = row.getString("ticket_number");
        final String violationCode = row.getString("violation_code");
        final String location = row.getString("location");
        final Double fineAmount = row.getDouble("fine_amount");
        final Double paidAmount = row.getDouble("paid_amount");
        final Double dueAmount = row.getDouble("due_amount");
        final String disposition = row.getString("disposition");
        final Double latitude = row.getDouble("latitude");
        final Double longitude = row.getDouble("longitude");

        final ParkingTicket parkingTicket = new ParkingTicket();

        parkingTicket.setId(id);
        parkingTicket.setDate(date);
        parkingTicket.setTicketNumber(ticketNumber);
        parkingTicket.setViolationCode(violationCode);
        parkingTicket.setLocation(location);
        parkingTicket.setFineAmount(fineAmount);
        parkingTicket.setPaidAmount(paidAmount);
        parkingTicket.setDueAmount(dueAmount);
        parkingTicket.setDisposition(disposition);
        parkingTicket.setLatitude(latitude);
        parkingTicket.setLongitude(longitude);

        return parkingTicket;
    }
}
