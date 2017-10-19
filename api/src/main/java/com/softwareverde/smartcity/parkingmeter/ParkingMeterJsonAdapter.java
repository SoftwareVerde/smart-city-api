package com.softwareverde.smartcity.parkingmeter;

import com.softwareverde.json.Json;

public class ParkingMeterJsonAdapter {

    public Json toJson(final ParkingMeter parkingMeter) {
        final Json json = new Json();

        json.put("id", parkingMeter.getId());
        json.put("meterId", parkingMeter.getMeterId());
        json.put("location", parkingMeter.getLocation());
        json.put("maxDwellDuration", parkingMeter.getMaxDwellDuration());
        json.put("isHandicap", parkingMeter.isHandicap());
        json.put("rate", parkingMeter.getRate());
        json.put("isChargingStation", parkingMeter.isChargingStation());
        json.put("latitude", parkingMeter.getLatitude());
        json.put("longitude", parkingMeter.getLongitude());

        return json;
    }
}
