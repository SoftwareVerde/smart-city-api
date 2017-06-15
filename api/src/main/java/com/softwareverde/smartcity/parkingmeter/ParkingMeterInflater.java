package com.softwareverde.smartcity.parkingmeter;

import com.softwareverde.database.Row;
import com.softwareverde.json.Json;

public class ParkingMeterInflater {
    public Json toJson(final ParkingMeter parkingMeter) {
        final Json json = new Json();

        json.put("id", parkingMeter.getId());
        json.put("meterId", parkingMeter.getMeterId());
        json.put("location", parkingMeter.getLocation());
        json.put("maxDwellDuration", parkingMeter.getMaxDwellDuration());
        json.put("isHandicap", parkingMeter.isHandicap());
        json.put("rateTimes100", parkingMeter.getRateTimes100());
        json.put("isChargingStation", parkingMeter.isChargingStation());
        json.put("latitude", parkingMeter.getLatitude());
        json.put("longitude", parkingMeter.getLongitude());

        return json;
    }

    public ParkingMeter fromRow(final Row row) {
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
