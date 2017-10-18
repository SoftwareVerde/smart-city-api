package com.softwareverde.smartcity.parkingmeter;

public class ParkingMeter {
    protected Long _id;
    protected String _meterId;
    protected String _location;
    protected Long _maxDwellDuration;
    protected Boolean _isHandicap;
    protected Double _rate;
    protected Boolean _isChargingStation;
    protected Double _latitude;
    protected Double _longitude;

    public ParkingMeter() { }

    public void setId(final Long id) { _id = id; }
    public Long getId() { return _id; }

    public void setMeterId(final String meterId) { _meterId = meterId; }
    public String getMeterId() { return _meterId; }

    public void setLocation(final String location) { _location = location; }
    public String getLocation() { return _location; }

    public void setMaxDwellDuration(final Long maxDwellDuration) { _maxDwellDuration = maxDwellDuration; }
    public Long getMaxDwellDuration() { return _maxDwellDuration; }

    public void setIsHandicap(final Boolean isHandicap) { _isHandicap = isHandicap; }
    public Boolean isHandicap() { return _isHandicap; }

    public void setRate(final Double rate) { _rate = rate; }
    public Double getRate() { return _rate; }

    public void setIsChargingStation(final Boolean isChargingStation) { _isChargingStation = isChargingStation; }
    public Boolean isChargingStation() { return _isChargingStation; }

    public void setLatitude(final Double latitude) { _latitude = latitude; }
    public Double getLatitude() { return _latitude; }

    public void setLongitude(final Double longitude) { _longitude = longitude; }
    public Double getLongitude() { return _longitude; }

    public boolean hasLatitudeAndLongitude() {
        return _latitude != null && _longitude != null;
    }
}
