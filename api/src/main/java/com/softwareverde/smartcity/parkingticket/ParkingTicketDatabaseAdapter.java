package com.softwareverde.smartcity.parkingticket;

import com.softwareverde.database.DatabaseConnection;
import com.softwareverde.database.DatabaseException;
import com.softwareverde.database.Query;
import com.softwareverde.database.Row;
import com.softwareverde.geo.util.GeoUtil;
import com.softwareverde.smartcity.licenseplate.LicensePlate;
import com.softwareverde.smartcity.licenseplate.LicensePlateDatabaseAdapter;
import com.softwareverde.smartcity.util.SmartCityUtil;
import com.softwareverde.util.DateUtil;

import java.sql.Connection;
import java.text.SimpleDateFormat;
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

    public ParkingTicket inflateById(final Long id) throws DatabaseException {
        final Query query = new Query("SELECT * FROM parking_tickets WHERE id = ?");
        query.setParameter(id);

        final List<Row> rows = _databaseConnection.query(query);
        if (rows.size() == 0) {
            return null;
        }
        else {
            final Row row = rows.get(0);
            return _fromRow(row);
        }
    }

    public ParkingTicket inflateByTicketNumber(final String ticketNumber) throws DatabaseException {
        final Query query = new Query("SELECT * FROM parking_tickets WHERE ticket_number = ?");
        query.setParameter(ticketNumber);

        final List<Row> rows = _databaseConnection.query(query);
        if (rows.size() == 0) {
            return null;
        }
        else {
            final Row row = rows.get(0);
            return _fromRow(row);
        }
    }

    public List<ParkingTicket> inflateBySearchCriteria(final Double radius, final Double latitude, final Double longitude, final String street, final Date dateAfter, final Date dateBefore, final String licensePlateNumber, final String licensePlateState, final String violationCode, final Double fineAmountGreaterThan, final Double fineAmountLessThan, final Double paidAmountGreaterThan, final Double paidAmountLessThan, final Double dueAmountGreaterThan, final Double dueAmountLessThan, final String disposition) throws DatabaseException {
        SimpleDateFormat mysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String dateAfterString = dateAfter == null ? null : mysqlDateFormat.format(dateAfter);
        final String dateBeforeString = dateBefore == null ? null : mysqlDateFormat.format(dateBefore);

        Double latitudeMax = null;
        Double latitudeMin = null;
        Double longitudeMax = null;
        Double longitudeMin = null;
        if (radius != null) {
            latitudeMax = latitude == null ? null : GeoUtil.addMetersToLatitude(latitude, longitude, radius);
            latitudeMin = latitude == null ? null : GeoUtil.addMetersToLatitude(latitude, longitude, -radius);
            longitudeMax = longitude == null ? null : GeoUtil.addMetersToLongitude(latitude, longitude, radius);
            longitudeMin = longitude == null ? null : GeoUtil.addMetersToLongitude(latitude, longitude, -radius);
        }

        final List<Row> rows = _databaseConnection.query(
                "SELECT *, license_plates.number AS license_plate_number, license_plates.state AS license_plate_state FROM parking_tickets LEFT OUTER JOIN license_plates ON license_plates.id = parking_tickets.license_plate_id WHERE"
                        +" (LENGTH(?) = 0 OR location LIKE ?)"
                        +" AND (LENGTH(?) = 0 OR latitude <= ?)"
                        +" AND (LENGTH(?) = 0 OR latitude >= ?)"
                        +" AND (LENGTH(?) = 0 OR longitude <= ?)"
                        +" AND (LENGTH(?) = 0 OR longitude >= ?)"
                        +" AND (LENGTH(?) = 0 OR date >= CAST(? AS DATE))"
                        +" AND (LENGTH(?) = 0 OR date <= CAST(? AS DATE))"
                        +" AND (LENGTH(?) = 0 OR license_plates.number LIKE ?)"
                        +" AND (LENGTH(?) = 0 OR license_plates.state = ?)"
                        +" AND (LENGTH(?) = 0 OR violation_code LIKE ?)"
                        +" AND (LENGTH(?) = 0 OR fine_amount >= ?)"
                        +" AND (LENGTH(?) = 0 OR fine_amount <= ?)"
                        +" AND (LENGTH(?) = 0 OR paid_amount >= ?)"
                        +" AND (LENGTH(?) = 0 OR paid_amount <= ?)"
                        +" AND (LENGTH(?) = 0 OR due_amount >= ?)"
                        +" AND (LENGTH(?) = 0 OR due_amount <= ?)"
                        +" AND (LENGTH(?) = 0 OR disposition LIKE ?)",
                new String[] {
                        SmartCityUtil.toEmptyStringIfNull(street),                  SmartCityUtil.toEmptyStringIfNull(street),
                        SmartCityUtil.toEmptyStringIfNull(latitudeMax),             SmartCityUtil.toEmptyStringIfNull(latitudeMax),
                        SmartCityUtil.toEmptyStringIfNull(latitudeMin),             SmartCityUtil.toEmptyStringIfNull(latitudeMin),
                        SmartCityUtil.toEmptyStringIfNull(longitudeMax),            SmartCityUtil.toEmptyStringIfNull(longitudeMax),
                        SmartCityUtil.toEmptyStringIfNull(longitudeMin),            SmartCityUtil.toEmptyStringIfNull(longitudeMin),
                        SmartCityUtil.toEmptyStringIfNull(dateAfter),               SmartCityUtil.toEmptyStringIfNull(dateAfterString),
                        SmartCityUtil.toEmptyStringIfNull(dateBefore),              SmartCityUtil.toEmptyStringIfNull(dateBeforeString),
                        SmartCityUtil.toEmptyStringIfNull(licensePlateNumber),      SmartCityUtil.toEmptyStringIfNull(licensePlateNumber),
                        SmartCityUtil.toEmptyStringIfNull(licensePlateState),       SmartCityUtil.toEmptyStringIfNull(licensePlateState),
                        SmartCityUtil.toEmptyStringIfNull(violationCode),           SmartCityUtil.toEmptyStringIfNull(violationCode),
                        SmartCityUtil.toEmptyStringIfNull(fineAmountGreaterThan),   SmartCityUtil.toEmptyStringIfNull(fineAmountGreaterThan),
                        SmartCityUtil.toEmptyStringIfNull(fineAmountLessThan),      SmartCityUtil.toEmptyStringIfNull(fineAmountLessThan),
                        SmartCityUtil.toEmptyStringIfNull(paidAmountGreaterThan),   SmartCityUtil.toEmptyStringIfNull(paidAmountGreaterThan),
                        SmartCityUtil.toEmptyStringIfNull(paidAmountLessThan),      SmartCityUtil.toEmptyStringIfNull(paidAmountLessThan),
                        SmartCityUtil.toEmptyStringIfNull(dueAmountGreaterThan),    SmartCityUtil.toEmptyStringIfNull(dueAmountGreaterThan),
                        SmartCityUtil.toEmptyStringIfNull(dueAmountLessThan),       SmartCityUtil.toEmptyStringIfNull(dueAmountLessThan),
                        SmartCityUtil.toEmptyStringIfNull(disposition),             SmartCityUtil.toEmptyStringIfNull(disposition)
                }
        );

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
