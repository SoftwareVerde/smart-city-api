package com.softwareverde.smartcity.parkingmeter;

import com.softwareverde.database.DatabaseConnection;
import com.softwareverde.database.DatabaseException;
import com.softwareverde.database.Query;
import com.softwareverde.database.Row;
import com.softwareverde.geo.util.GeoUtil;
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

    public List<ParkingMeter> inflateBySearchCriteria(final Double radius, final Double latitude, final Double longitude, final String street, final Integer maxDwellDurationGreaterThan, final Integer maxDwellDurationLessThan, final Float rateGreaterThan, final Float rateLessThan, final Boolean isHandicap, final Boolean isChargingStation) throws DatabaseException {
        final Integer isHandicapInt = _convertBooleanToInteger(isHandicap);
        final Integer isChargingStationInt = _convertBooleanToInteger(isChargingStation);

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
                "SELECT * FROM parking_meters WHERE"
                        +" (LENGTH(?) = 0 OR location LIKE ?)"
                        +" AND (LENGTH(?) = 0 OR latitude <= ?)"
                        +" AND (LENGTH(?) = 0 OR latitude >= ?)"
                        +" AND (LENGTH(?) = 0 OR longitude <= ?)"
                        +" AND (LENGTH(?) = 0 OR longitude >= ?)"
                        +" AND (LENGTH(?) = 0 OR max_dwell_duration >= ?)"
                        +" AND (LENGTH(?) = 0 OR max_dwell_duration <= ?)"
                        +" AND (LENGTH(?) = 0 OR rate >= ?)"
                        +" AND (LENGTH(?) = 0 OR rate <= ?)"
                        +" AND (LENGTH(?) = 0 OR is_handicap = ?)"
                        +" AND (LENGTH(?) = 0 OR is_charging_station = ?)",
                new String[] {
                        SmartCityUtil.toEmptyStringIfNull(street),                      SmartCityUtil.toEmptyStringIfNull(street),
                        SmartCityUtil.toEmptyStringIfNull(latitudeMax),                 SmartCityUtil.toEmptyStringIfNull(latitudeMax),
                        SmartCityUtil.toEmptyStringIfNull(latitudeMin),                 SmartCityUtil.toEmptyStringIfNull(latitudeMin),
                        SmartCityUtil.toEmptyStringIfNull(longitudeMax),                SmartCityUtil.toEmptyStringIfNull(longitudeMax),
                        SmartCityUtil.toEmptyStringIfNull(longitudeMin),                SmartCityUtil.toEmptyStringIfNull(longitudeMin),
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
        parkingMeter.setRate(row.getDouble("rate"));
        parkingMeter.setIsChargingStation(row.getBoolean("is_charging_station"));
        parkingMeter.setLatitude(row.getDouble("latitude"));
        parkingMeter.setLongitude(row.getDouble("longitude"));

        return parkingMeter;
    }
}
