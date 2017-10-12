package com.softwareverde.geo.util;

public interface LocationExtractor<T> {
    Double getLatitude(final T t);
    Double getLongitude(final T t);
}
