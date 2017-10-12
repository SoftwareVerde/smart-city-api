package com.softwareverde.smartcity.api.parkingmeter.response;

import com.softwareverde.json.Json;
import com.softwareverde.smartcity.api.response.JsonResult;
import com.softwareverde.smartcity.parkingmeter.ParkingMeter;
import com.softwareverde.smartcity.parkingmeter.ParkingMeterJsonAdapter;

public class GetParkingMeterResult extends JsonResult {
    protected final ParkingMeterJsonAdapter _parkingMeterJsonAdapter = new ParkingMeterJsonAdapter();
    protected ParkingMeter _parkingMeter = null;

    public GetParkingMeterResult() {
        super(true, null);
    }

    public void setParkingMeter(final ParkingMeter parkingMeter) {
        _parkingMeter = parkingMeter;
    }

    @Override
    public Json toJson() {
        final Json json =  super.toJson();

        final Json parkingMeterJson = _parkingMeterJsonAdapter.toJson(_parkingMeter);
        json.put("parkingMeter", parkingMeterJson);

        return json;
    }
}
