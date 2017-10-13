package com.softwareverde.smartcity.licenseplate;

public class LicensePlate {
    private Long _id;
    private String _number;
    private String _state;

    public LicensePlate() {}

    public LicensePlate(final Long licensePlateId, final String licensePlateNumber, final String licensePlateState) {
        _id = licensePlateId;
        _number = licensePlateNumber;
        _state = licensePlateState;
    }

    public Long getId() {
        return _id;
    }

    public void setId(final Long id) {
        _id = id;
    }

    public String getNumber() {
        return _number;
    }

    public void setNumber(final String number) {
        _number = number;
    }

    public String getState() {
        return _state;
    }

    public void setState(final String state) {
        _state = state;
    }
}
