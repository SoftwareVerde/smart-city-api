package com.softwareverde.smartcity.api.parcel.response;

import com.softwareverde.json.Json;
import com.softwareverde.smartcity.api.response.JsonResult;
import com.softwareverde.smartcity.parcel.Parcel;
import com.softwareverde.smartcity.parcel.ParcelJsonAdapter;

public class GetParcelResult extends JsonResult {
    protected final ParcelJsonAdapter _parcelJsonAdapter = new ParcelJsonAdapter();
    protected Parcel _parcel = null;

    public GetParcelResult() {
        super(true, null);
    }

    public void setParcel(final Parcel parcel) {
        _parcel = parcel;
    }

    @Override
    public Json toJson() {
        final Json json =  super.toJson();

        final Json parcelJson = _parcelJsonAdapter.toJson(_parcel);
        json.put("parcel", parcelJson);

        return json;
    }
}
