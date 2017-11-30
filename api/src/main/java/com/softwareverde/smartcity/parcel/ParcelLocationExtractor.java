package com.softwareverde.smartcity.parcel;

import com.softwareverde.geo.util.LocationExtractor;

public class ParcelLocationExtractor implements LocationExtractor<Parcel> {
    @Override
    public Double getLatitude(final Parcel parcel) {
        return parcel.getLatitude();
    }

    @Override
    public Double getLongitude(final Parcel parcel) {
        return parcel.getLongitude();
    }
}
