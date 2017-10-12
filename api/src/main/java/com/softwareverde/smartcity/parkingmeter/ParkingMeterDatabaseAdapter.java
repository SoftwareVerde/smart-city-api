package com.softwareverde.smartcity.parkingmeter;

import com.softwareverde.database.DatabaseConnection;
import com.softwareverde.database.DatabaseException;
import com.softwareverde.database.Query;
import com.softwareverde.database.Row;
import com.softwareverde.smartcity.util.SmartCityUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ParkingMeterDatabaseAdapter {

    private DatabaseConnection<Connection> _databaseConnection;

    public ParkingMeterDatabaseAdapter(final DatabaseConnection<Connection> databaseConnection) {
        _databaseConnection = databaseConnection;
    }

    public List<ParkingMeter> inflateAll() throws DatabaseException {
        final List<Row> rows = _databaseConnection.query("SELECT * FROM parking_meters", null);

        final List<ParkingMeter> parkingMeters = new ArrayList<>();
        for (final Row row : rows) {
            final ParkingMeter parkingMeter = _fromRow(row);
            parkingMeters.add(parkingMeter);
        }
        return parkingMeters;
    }

    public ParkingMeter inflateById(final long parkingMeterId) throws DatabaseException {
        final Query query = new Query("SELECT * FROM parking_meters WHERE id = ?");
        query.setParameter(parkingMeterId);

        final List<Row> rows = _databaseConnection.query(query);
        if (rows.size() == 0) {
            return null;
        } else {
            final Row row = rows.get(0);
            return _fromRow(row);
        }
    }

    public ParkingMeter inflateByMeterId(final String parkingMeterMeterId) throws DatabaseException {
        final Query query = new Query("SELECT * FROM parking_meters WHERE meter_id = ?");
        query.setParameter(parkingMeterMeterId);

        List<Row> rows = _databaseConnection.query(query);
        if (rows.size() == 0) {
            return null;
        } else {
            final Row row = rows.get(0);
            return _fromRow(row);
        }
    }

    public List<ParkingMeter> inflateBySearchCriteria(final String street, final Integer maxDwellDurationGreaterThan, final Integer maxDwellDurationLessThan, final Float rateGreaterThan, final Float rateLessThan, final Boolean isHandicap, final Boolean isChargingStation) throws DatabaseException {
        final Integer isHandicapInt = _convertBooleanToInteger(isHandicap);
        final Integer isChargingStationInt = _convertBooleanToInteger(isChargingStation);

        final List<Row> rows = _databaseConnection.query(
                "SELECT * FROM parking_meters WHERE"
                        +" (LENGTH(?) = 0 OR location LIKE ?)"
                        +" AND (LENGTH(?) = 0 OR max_dwell_duration >= ?)"
                        +" AND (LENGTH(?) = 0 OR max_dwell_duration <= ?)"
                        +" AND (LENGTH(?) = 0 OR rate >= ?)"
                        +" AND (LENGTH(?) = 0 OR rate <= ?)"
                        +" AND (LENGTH(?) = 0 OR is_handicap = ?)"
                        +" AND (LENGTH(?) = 0 OR is_charging_station = ?)",
                new String[] {
                        SmartCityUtil.toEmptyStringIfNull(street),                      SmartCityUtil.toEmptyStringIfNull(street),
                        SmartCityUtil.toEmptyStringIfNull(maxDwellDurationGreaterThan), SmartCityUtil.toEmptyStringIfNull(maxDwellDurationGreaterThan),
                        SmartCityUtil.toEmptyStringIfNull(maxDwellDurationLessThan),    SmartCityUtil.toEmptyStringIfNull(maxDwellDurationLessThan),
                        SmartCityUtil.toEmptyStringIfNull(rateGreaterThan),             SmartCityUtil.toEmptyStringIfNull(rateGreaterThan),
                        SmartCityUtil.toEmptyStringIfNull(rateLessThan),                SmartCityUtil.toEmptyStringIfNull(rateLessThan),
                        SmartCityUtil.toEmptyStringIfNull(isHandicapInt),               SmartCityUtil.toEmptyStringIfNull(isHandicapInt),
                        SmartCityUtil.toEmptyStringIfNull(isChargingStationInt),        SmartCityUtil.toEmptyStringIfNull(isChargingStationInt)
                }
        );

        final List<ParkingMeter> parkingMeters = new ArrayList<>();
        for (final Row row : rows) {
            final ParkingMeter parkingMeter = _fromRow(row);
            parkingMeters.add(parkingMeter);
        }
        return parkingMeters;
    }

    private Integer _convertBooleanToInteger(Boolean value) {
        if (value == null) {
            return null;
        }
        return value ? 1 : 0;
    }

    private ParkingMeter _fromRow(final Row row) {
        final ParkingMeter parkingMeter = new ParkingMeter();

        parkingMeter.setId(row.getLong("id"));
        parkingMeter.setMeterId(row.getString("meter_id"));
        parkingMeter.setLocation(row.getString("location"));
        parkingMeter.setMaxDwellDuration(row.getLong("max_dwell_duration"));
        parkingMeter.setIsHandicap(row.getBoolean("is_handicap"));
        parkingMeter.setRateTimes100(row.getLong("rate") * 100L);
        parkingMeter.setIsChargingStation(row.getBoolean("is_charging_station"));
        parkingMeter.setLatitude(row.getFloat("latitude"));
        parkingMeter.setLongitude(row.getFloat("longitude"));

        return parkingMeter;
    }
}
