package com.softwareverde.geo.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GeoUtil {
    public static final double EARTH_RADIUS_IN_KM = 6378.137D;
    public static final double EARTH_RADIUS_IN_M = EARTH_RADIUS_IN_KM * 1000;
    public static final double DEGREES_TO_RADIANS = Math.PI / 180D;
    public static final double RADIANS_TO_DEGREES = 180D / Math.PI;

    /**
     * <p>Uses the <a href="http://www.movable-type.co.uk/scripts/latlong.html">haversine formula</a> to determine the
     * great-circle distance between (latitude, longitude) and (latitude2, longitude2).</p>
     *
     * @param latitude
     * @param longitude
     * @param latitude2
     * @param longitude2
     * @return
     */
    public static double greatCircleDistanceInMeters(final double latitude, final double longitude, final double latitude2, final double longitude2) {
        final double lat1Radians = latitude * DEGREES_TO_RADIANS;
        final double lat2Radians = latitude2 * DEGREES_TO_RADIANS;
        final double dLat = lat2Radians - lat1Radians;
        final double dLon = (longitude2 - longitude) * DEGREES_TO_RADIANS;
        final double a = Math.pow(Math.sin(dLat / 2D), 2) + Math.cos(lat1Radians) * Math.cos(lat2Radians) * Math.pow(Math.sin(dLon / 2D), 2);
        final double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
        final double d = EARTH_RADIUS_IN_KM * c;
        return d * 1000D; // In meters
    }

    public static <T> Collection<T> filterByLocation(final Collection<T> originals, final LocationExtractor<T> locationExtractor, final Double radius, final Double targetLatitude, final Double targetLongitude) {
        if (radius == null || targetLatitude == null || targetLongitude == null) {
            // required data not all specified, return copy of original collection (i.e. don't filter)
            return new ArrayList<>(originals);
        }

        final List<T> filteredList = new ArrayList<>();
        for (final T object : originals) {
            final Double latitude = locationExtractor.getLatitude(object);
            final Double longitude = locationExtractor.getLongitude(object);

            final Boolean shouldBeAdded;
            if (latitude == null || longitude == null) {
                // when using radius, filter out any object with null lat/long
                shouldBeAdded = false;
            }
            else {
                final Double distance = GeoUtil.greatCircleDistanceInMeters(latitude, longitude, targetLatitude, targetLongitude);
                shouldBeAdded = (distance <= radius);
            }

            if (shouldBeAdded) {
                filteredList.add(object);
            }
        }
        return filteredList;
    }

    /**
     * <p>Returns the latitude <code>meters</code> north of <code>latitude</code>.</p>
     *
     * <p>Should not be used for large distances the earth is not perfectly spherical.</p>
     *
     * @param latitude
     * @param longitude
     * @param meters
     * @return
     */
    public static double addMetersToLatitude(final double latitude, final double longitude, final double meters) {
        final double offset = (meters / EARTH_RADIUS_IN_M) * RADIANS_TO_DEGREES;
        double newLatitude = latitude + offset;
        if (newLatitude > 90) {
            newLatitude = newLatitude - 180;
        }
        return newLatitude;
    }

    /**
     * <p>Returns the longitude <code>meters</code> east of <code>longitude</code> at <code>latitude</code>.</p>
     *
     * <p>The latitude is required because </p>
     *
     * <p>Should not be used for large distances the earth is not perfectly spherical.</p>
     *
     * @param latitude
     * @param meters
     * @return
     */
    public static double addMetersToLongitude(final double latitude, final double longitude, final double meters) {
        final double offset = (meters / EARTH_RADIUS_IN_M) * RADIANS_TO_DEGREES / Math.cos(latitude * DEGREES_TO_RADIANS);
        double newLongitude = longitude + offset;
        if (newLongitude > 180) {
            newLongitude = newLongitude - 360;
        }
        return newLongitude;
    }
}
