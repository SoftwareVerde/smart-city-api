package com.softwareverde.smartcity.api.parcel.response;

import com.softwareverde.json.Json;
import com.softwareverde.smartcity.api.response.JsonResult;
import com.softwareverde.smartcity.parcel.Parcel;
import com.softwareverde.smartcity.parcel.ParcelJsonAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListParcelsResult extends JsonResult {
    protected final ParcelJsonAdapter _parcelJsonAdapter = new ParcelJsonAdapter();
    protected final List<Parcel> _parcels = new ArrayList<Parcel>();

    public ListParcelsResult() {
        super(true, null);
    }

    public void addParcel(final Parcel parcel) {
        _parcels.add(parcel);
    }
    public void clearParcels() {
        _parcels.clear();
    }

    @Override
    public Json toJson() {
        final Json json =  super.toJson();

        final Json parcelsJson = new Json();
        for (final Parcel parcel : _parcels) {
            final Json parcelJson = _parcelJsonAdapter.toJson(parcel);
            parcelsJson.add(parcelJson);
        }
        json.put("parcels", parcelsJson);

        return json;
    }
}
