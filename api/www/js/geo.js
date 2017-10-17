
const EARTH_RADIUS_IN_M = 6378137.0;
const DEGREES_TO_RADIANS = Math.PI / 180.0
const RADIANS_TO_DEGREES = 180.0 / Math.PI;

/*
 * Returns the latitude 'meters' north of 'latitude'.
 *
 * Should not be used for large distances the earth is not perfectly spherical.
 */
function addMetersToLatitude(latitude, longitude, meters) {
    const offset = (meters / EARTH_RADIUS_IN_M) * RADIANS_TO_DEGREES;
    let newLatitude = latitude + offset;
    // positive overflow
    if (newLatitude > 90) {
        newLatitude = newLatitude - 180;
    }
    // negative overflow
    if (newLatitude < -90) {
        newLatitude = newLatitude + 180;
    }
    return newLatitude;
}

/*
 * Returns the longitude 'meters' east of 'longitude' at 'latitude'.
 *
 * Should not be used for large distances the earth is not perfectly spherical.
 */
function addMetersToLongitude(latitude, longitude, meters) {
    const offset = (meters / EARTH_RADIUS_IN_M) * RADIANS_TO_DEGREES / Math.cos(latitude * DEGREES_TO_RADIANS);
    let newLongitude = longitude + offset;
    // positive overflow
    if (newLongitude > 180) {
        newLongitude = newLongitude - 360;
    }
    // negative overflow
    if (newLongitude < -180) {
        newLongitude = newLongitude + 360;
    }
    return newLongitude;
}
