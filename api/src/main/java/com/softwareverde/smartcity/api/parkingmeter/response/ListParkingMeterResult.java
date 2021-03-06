package com.softwareverde.smartcity.api.parkingmeter.response;

import com.softwareverde.json.Json;
import com.softwareverde.smartcity.api.response.JsonResult;
import com.softwareverde.smartcity.parkingmeter.ParkingMeter;
import com.softwareverde.smartcity.parkingmeter.ParkingMeterJsonAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListParkingMeterResult extends JsonResult {
    protected final ParkingMeterJsonAdapter _parkingMeterJsonAdapter = new ParkingMeterJsonAdapter();
    protected final List<ParkingMeter> _parkingMeters = new ArrayList<ParkingMeter>();

    public ListParkingMeterResult() {
        super(true, null);
    }

    public void addParkingMeter(final ParkingMeter parkingMeter) {
        _parkingMeters.add(parkingMeter);
    }
    public void clearParkingMeters() {
        _parkingMeters.clear();
    }

    @Override
    public Json toJson() {
        final Json json =  super.toJson();

        final Json parkingMetersJson = new Json();
        for (final ParkingMeter parkingMeter : _parkingMeters) {
            final Json parkingMeterJson = _parkingMeterJsonAdapter.toJson(parkingMeter);
            parkingMetersJson.add(parkingMeterJson);
        }
        json.put("parkingMeters", parkingMetersJson);

        return json;
    }
}
