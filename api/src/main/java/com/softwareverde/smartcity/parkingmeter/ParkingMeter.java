package com.softwareverde.smartcity.parkingmeter;

public class ParkingMeter {
    protected Long _id;
    protected String _meterId;
    protected String _location;
    protected Long _maxDwellDuration;
    protected Boolean _isHandicap;
    protected Long _rateTimes100;
    protected Boolean _isChargingStation;
    protected Float _latitude;
    protected Float _longitude;

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

    public void setRateTimes100(final Long rateTimes100) { _rateTimes100 = rateTimes100; }
    public Long getRateTimes100() { return _rateTimes100; }

    public void setIsChargingStation(final Boolean isChargingStation) { _isChargingStation = isChargingStation; }
    public Boolean isChargingStation() { return _isChargingStation; }

    public void setLatitude(final Float latitude) { _latitude = latitude; }
    public Float getLatitude() { return _latitude; }

    public void setLongitude(final Float longitude) { _longitude = longitude; }
    public Float getLongitude() { return _longitude; }
}
