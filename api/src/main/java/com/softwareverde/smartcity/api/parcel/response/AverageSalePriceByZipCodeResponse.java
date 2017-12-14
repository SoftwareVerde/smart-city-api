package com.softwareverde.smartcity.api.parcel.response;

import com.softwareverde.json.Json;
import com.softwareverde.smartcity.api.response.JsonResult;

import java.util.HashMap;
import java.util.Map;

public class AverageSalePriceByZipCodeResponse extends JsonResult {
    private final Map<String, Double> _zipCodeAveragePrice = new HashMap<String, Double>();
    public void setAverageSalePrice(final String zipCode, final Double averageSalePrice) {
        _zipCodeAveragePrice.put(zipCode, averageSalePrice);
    }

    @Override
    public Json toJson() {
        final Json json =  super.toJson();

        final Json averagePricesJson = new Json();
        for (final String zipCode : _zipCodeAveragePrice.keySet()) {
            final Double averagePrice = _zipCodeAveragePrice.get(zipCode);
            averagePricesJson.put(zipCode, averagePrice);
        }
        json.put("averageSalePrices", averagePricesJson);

        return json;
    }
}
