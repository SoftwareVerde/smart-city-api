package com.softwareverde.smartcity.licenseplate;

import com.softwareverde.json.Json;

public class LicensePlateJsonAdapter {

    public Json toJson(final LicensePlate licensePlate) {
        final Long id = licensePlate.getId();
        final String number = licensePlate.getNumber();
        final String state = licensePlate.getState();

        final Json json = new Json();

        json.put("id", id);
        json.put("number", number);
        json.put("state", state);

        return json;
    }
}
