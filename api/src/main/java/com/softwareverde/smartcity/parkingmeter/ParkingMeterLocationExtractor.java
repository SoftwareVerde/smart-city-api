package com.softwareverde.smartcity.parkingmeter;

import com.softwareverde.geo.util.LocationExtractor;

public class ParkingMeterLocationExtractor implements LocationExtractor<ParkingMeter> {
    @Override
    public Double getLatitude(ParkingMeter parkingMeter) {
        return parkingMeter.getLatitude();
    }

    @Override
    public Double getLongitude(ParkingMeter parkingMeter) {
        return parkingMeter.getLongitude();
    }
}
