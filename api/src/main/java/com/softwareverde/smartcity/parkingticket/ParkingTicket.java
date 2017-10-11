package com.softwareverde.smartcity.parkingticket;

import com.softwareverde.smartcity.licenseplate.LicensePlate;

import java.util.Date;

public class ParkingTicket {
    private Long _id;
    private Date _date;
    private String _ticketNumber;
    private LicensePlate _licensePlate;
    private String _violationCode;
    private String _location;
    private Double _fineAmount;
    private Double _paidAmount;
    private Double _dueAmount;
    private String _disposition;
    private Double _latitude;
    private Double _longitude;

    public Long getId() {
        return _id;
    }

    public void setId(final Long id) {
        _id = id;
    }

    public Date getDate() {
        return _date;
    }

    public void setDate(final Date date) {
        _date = date;
    }

    public String getTicketNumber() {
        return _ticketNumber;
    }

    public void setTicketNumber(final String ticketNumber) {
        _ticketNumber = ticketNumber;
    }

    public LicensePlate getLicensePlate() {
        return _licensePlate;
    }

    public void setLicensePlate(final LicensePlate licensePlate) {
        _licensePlate = licensePlate;
    }

    public String getViolationCode() {
        return _violationCode;
    }

    public void setViolationCode(final String violationCode) {
        _violationCode = violationCode;
    }

    public String getLocation() {
        return _location;
    }

    public void setLocation(final String location) {
        _location = location;
    }

    public Double getFineAmount() {
        return _fineAmount;
    }

    public void setFineAmount(final Double fineAmount) {
        _fineAmount = fineAmount;
    }

    public Double getPaidAmount() {
        return _paidAmount;
    }

    public void setPaidAmount(final Double paidAmount) {
        _paidAmount = paidAmount;
    }

    public Double getDueAmount() {
        return _dueAmount;
    }

    public void setDueAmount(final Double dueAmount) {
        _dueAmount = dueAmount;
    }

    public String getDisposition() {
        return _disposition;
    }

    public void setDisposition(final String disposition) {
        _disposition = disposition;
    }

    public Double getLatitude() {
        return _latitude;
    }

    public void setLatitude(final Double latitude) {
        _latitude = latitude;
    }

    public Double getLongitude() {
        return _longitude;
    }

    public void setLongitude(final Double longitude) {
        _longitude = longitude;
    }
}
