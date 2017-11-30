package com.softwareverde.smartcity.parcel;

import java.util.Date;

public class Parcel {
    private Long _id;
    private String _parcelId;
    private Double _exemptPropertyAppraisalLandValue;
    private Double _exemptPropertyAppraisalBuildingValue;
    private Double _exemptPropertyAppraisalTotalValue;
    private Double _taxablePropertyAppraisalLandValue;
    private Double _taxablePropertyAppraisalBuildingValue;
    private Double _taxablePropertyAppraisalTotalValue;
    private String _auditorsMapNumber;
    private String _auditorsRoutingSequence;
    private String _appraisalLandUseCode;
    private String _currentAgriculturalUseValue;
    private String _schoolDistrictCode;
    private Boolean _containsHomesteadExemptionValues;
    private String _taxBillMailingAddressLine1;
    private String _taxBillMailingAddressLine2;
    private String _taxBillMailingAddressLine3;
    private String _taxBillMailingAddressLine4;
    private Date _transferDate;
    private Long _transferYear;
    private String _ownerName1;
    private String _ownerName2;
    private String _ownerName3;
    private String _ownerAddress1;
    private String _ownerAddress2;
    private String _auditorsNeighborhoodCode;
    private String _floodInfo;
    private String _propertyClass;
    private Long _numberOfPropertyCards;
    private Double _acreage;
    private Double _lastSalePrice;
    private Double _totalTaxableValue;
    private String _city;
    private String _state;
    private String _zipcode;
    private String _descriptionLine1;
    private String _descriptionLine2;
    private String _descriptionLine3;
    private String _taxDesignation;
    private Double _primaryBuildingAreaSquareFeet;
    private String _dwellingType;
    private Long _totalRoomCount;
    private Long _fullBathCount;
    private Long _halfBathCount;
    private Long _bedroomCount;
    private String _centralAirStyle;
    private String _buildingCondition;
    private Boolean _hasFireplaces;
    private String _buildingGrade;
    private String _height;
    private Double _storyCount;
    private String _yearBuilt;
    private String _propertyType;
    private String _wallCode;
    private Double _latitude;
    private Double _longitude;

    public Long getId() {
        return _id;
    }

    public void setId(final Long id) {
        _id = id;
    }

    public String getParcelId() {
        return _parcelId;
    }

    public void setParcelId(final String parcelId) {
        _parcelId = parcelId;
    }

    public Double getExemptPropertyAppraisalLandValue() {
        return _exemptPropertyAppraisalLandValue;
    }

    public void setExemptPropertyAppraisalLandValue(final Double exemptPropertyAppraisalLandValue) {
        _exemptPropertyAppraisalLandValue = exemptPropertyAppraisalLandValue;
    }

    public Double getExemptPropertyAppraisalBuildingValue() {
        return _exemptPropertyAppraisalBuildingValue;
    }

    public void setExemptPropertyAppraisalBuildingValue(final Double exemptPropertyAppraisalBuildingValue) {
        _exemptPropertyAppraisalBuildingValue = exemptPropertyAppraisalBuildingValue;
    }

    public Double getExemptPropertyAppraisalTotalValue() {
        return _exemptPropertyAppraisalTotalValue;
    }

    public void setExemptPropertyAppraisalTotalValue(final Double exemptPropertyAppraisalTotalValue) {
        _exemptPropertyAppraisalTotalValue = exemptPropertyAppraisalTotalValue;
    }

    public Double getTaxablePropertyAppraisalLandValue() {
        return _taxablePropertyAppraisalLandValue;
    }

    public void setTaxablePropertyAppraisalLandValue(final Double taxablePropertyAppraisalLandValue) {
        _taxablePropertyAppraisalLandValue = taxablePropertyAppraisalLandValue;
    }

    public Double getTaxablePropertyAppraisalBuildingValue() {
        return _taxablePropertyAppraisalBuildingValue;
    }

    public void setTaxablePropertyAppraisalBuildingValue(final Double taxablePropertyAppraisalBuildingValue) {
        _taxablePropertyAppraisalBuildingValue = taxablePropertyAppraisalBuildingValue;
    }

    public Double getTaxablePropertyAppraisalTotalValue() {
        return _taxablePropertyAppraisalTotalValue;
    }

    public void setTaxablePropertyAppraisalTotalValue(final Double taxablePropertyAppraisalTotalValue) {
        _taxablePropertyAppraisalTotalValue = taxablePropertyAppraisalTotalValue;
    }

    public String getAuditorsMapNumber() {
        return _auditorsMapNumber;
    }

    public void setAuditorsMapNumber(final String auditorsMapNumber) {
        _auditorsMapNumber = auditorsMapNumber;
    }

    public String getAuditorsRoutingSequence() {
        return _auditorsRoutingSequence;
    }

    public void setAuditorsRoutingSequence(final String auditorsRoutingSequence) {
        _auditorsRoutingSequence = auditorsRoutingSequence;
    }

    public String getAppraisalLandUseCode() {
        return _appraisalLandUseCode;
    }

    public void setAppraisalLandUseCode(final String appraisalLandUseCode) {
        _appraisalLandUseCode = appraisalLandUseCode;
    }

    public String getCurrentAgriculturalUseValue() {
        return _currentAgriculturalUseValue;
    }

    public void setCurrentAgriculturalUseValue(final String currentAgriculturalUseValue) {
        _currentAgriculturalUseValue = currentAgriculturalUseValue;
    }

    public String getSchoolDistrictCode() {
        return _schoolDistrictCode;
    }

    public void setSchoolDistrictCode(final String schoolDistrictCode) {
        _schoolDistrictCode = schoolDistrictCode;
    }

    public Boolean containsHomesteadExemptionValues() {
        return _containsHomesteadExemptionValues;
    }

    public void setContainsHomesteadExemptionValues(final Boolean containsHomesteadExemptionValues) {
        _containsHomesteadExemptionValues = containsHomesteadExemptionValues;
    }

    public String getTaxBillMailingAddressLine1() {
        return _taxBillMailingAddressLine1;
    }

    public void setTaxBillMailingAddressLine1(final String taxBillMailingAddressLine1) {
        _taxBillMailingAddressLine1 = taxBillMailingAddressLine1;
    }

    public String getTaxBillMailingAddressLine2() {
        return _taxBillMailingAddressLine2;
    }

    public void setTaxBillMailingAddressLine2(final String taxBillMailingAddressLine2) {
        _taxBillMailingAddressLine2 = taxBillMailingAddressLine2;
    }

    public String getTaxBillMailingAddressLine3() {
        return _taxBillMailingAddressLine3;
    }

    public void setTaxBillMailingAddressLine3(final String taxBillMailingAddressLine3) {
        _taxBillMailingAddressLine3 = taxBillMailingAddressLine3;
    }

    public String getTaxBillMailingAddressLine4() {
        return _taxBillMailingAddressLine4;
    }

    public void setTaxBillMailingAddressLine4(final String taxBillMailingAddressLine4) {
        _taxBillMailingAddressLine4 = taxBillMailingAddressLine4;
    }

    public Date getTransferDate() {
        return _transferDate;
    }

    public void setTransferDate(final Date transferDate) {
        _transferDate = transferDate;
    }

    public Long getTransferYear() {
        return _transferYear;
    }

    public void setTransferYear(final Long transferYear) {
        _transferYear = transferYear;
    }

    public String getOwnerName1() {
        return _ownerName1;
    }

    public void setOwnerName1(final String ownerName1) {
        _ownerName1 = ownerName1;
    }

    public String getOwnerName2() {
        return _ownerName2;
    }

    public void setOwnerName2(final String ownerName2) {
        _ownerName2 = ownerName2;
    }

    public String getOwnerName3() {
        return _ownerName3;
    }

    public void setOwnerName3(final String ownerName3) {
        _ownerName3 = ownerName3;
    }

    public String getOwnerAddress1() {
        return _ownerAddress1;
    }

    public void setOwnerAddress1(final String ownerAddress1) {
        _ownerAddress1 = ownerAddress1;
    }

    public String getOwnerAddress2() {
        return _ownerAddress2;
    }

    public void setOwnerAddress2(final String ownerAddress2) {
        _ownerAddress2 = ownerAddress2;
    }

    public String getAuditorsNeighborhoodCode() {
        return _auditorsNeighborhoodCode;
    }

    public void setAuditorsNeighborhoodCode(final String auditorsNeighborhoodCode) {
        _auditorsNeighborhoodCode = auditorsNeighborhoodCode;
    }

    public String getFloodInfo() {
        return _floodInfo;
    }

    public void setFloodInfo(final String floodInfo) {
        _floodInfo = floodInfo;
    }

    public String getPropertyClass() {
        return _propertyClass;
    }

    public void setPropertyClass(final String propertyClass) {
        _propertyClass = propertyClass;
    }

    public Long getNumberOfPropertyCards() {
        return _numberOfPropertyCards;
    }

    public void setNumberOfPropertyCards(final Long numberOfPropertyCards) {
        _numberOfPropertyCards = numberOfPropertyCards;
    }

    public Double getAcreage() {
        return _acreage;
    }

    public void setAcreage(final Double acreage) {
        _acreage = acreage;
    }

    public Double getLastSalePrice() {
        return _lastSalePrice;
    }

    public void setLastSalePrice(final Double lastSalePrice) {
        _lastSalePrice = lastSalePrice;
    }

    public Double getTotalTaxableValue() {
        return _totalTaxableValue;
    }

    public void setTotalTaxableValue(final Double totalTaxableValue) {
        _totalTaxableValue = totalTaxableValue;
    }

    public String getCity() {
        return _city;
    }

    public void setCity(final String city) {
        _city = city;
    }

    public String getState() {
        return _state;
    }

    public void setState(final String state) {
        _state = state;
    }

    public String getZipcode() {
        return _zipcode;
    }

    public void setZipcode(final String zipcode) {
        _zipcode = zipcode;
    }

    public String getDescriptionLine1() {
        return _descriptionLine1;
    }

    public void setDescriptionLine1(final String descriptionLine1) {
        _descriptionLine1 = descriptionLine1;
    }

    public String getDescriptionLine2() {
        return _descriptionLine2;
    }

    public void setDescriptionLine2(final String descriptionLine2) {
        _descriptionLine2 = descriptionLine2;
    }

    public String getDescriptionLine3() {
        return _descriptionLine3;
    }

    public void setDescriptionLine3(final String descriptionLine3) {
        _descriptionLine3 = descriptionLine3;
    }

    public String getTaxDesignation() {
        return _taxDesignation;
    }

    public void setTaxDesignation(final String taxDesignation) {
        _taxDesignation = taxDesignation;
    }

    public Double getPrimaryBuildingAreaSquareFeet() {
        return _primaryBuildingAreaSquareFeet;
    }

    public void setPrimaryBuildingAreaSquareFeet(final Double primaryBuildingAreaSquareFeet) {
        _primaryBuildingAreaSquareFeet = primaryBuildingAreaSquareFeet;
    }

    public String getDwellingType() {
        return _dwellingType;
    }

    public void setDwellingType(final String dwellingType) {
        _dwellingType = dwellingType;
    }

    public Long getTotalRoomCount() {
        return _totalRoomCount;
    }

    public void setTotalRoomCount(final Long totalRoomCount) {
        _totalRoomCount = totalRoomCount;
    }

    public Long getFullBathCount() {
        return _fullBathCount;
    }

    public void setFullBathCount(final Long fullBathCount) {
        _fullBathCount = fullBathCount;
    }

    public Long getHalfBathCount() {
        return _halfBathCount;
    }

    public void setHalfBathCount(final Long halfBathCount) {
        _halfBathCount = halfBathCount;
    }

    public Long getBedroomCount() {
        return _bedroomCount;
    }

    public void setBedroomCount(final Long bedroomCount) {
        _bedroomCount = bedroomCount;
    }

    public String getCentralAirStyle() {
        return _centralAirStyle;
    }

    public void setCentralAirStyle(final String centralAirStyle) {
        _centralAirStyle = centralAirStyle;
    }

    public String getBuildingCondition() {
        return _buildingCondition;
    }

    public void setBuildingCondition(final String buildingCondition) {
        _buildingCondition = buildingCondition;
    }

    public Boolean hasFireplaces() {
        return _hasFireplaces;
    }

    public void setHasFireplaces(final Boolean hasFireplaces) {
        _hasFireplaces = hasFireplaces;
    }

    public String getBuildingGrade() {
        return _buildingGrade;
    }

    public void setBuildingGrade(final String buildingGrade) {
        _buildingGrade = buildingGrade;
    }

    public String getHeight() {
        return _height;
    }

    public void setHeight(final String height) {
        _height = height;
    }

    public Double getStoryCount() {
        return _storyCount;
    }

    public void setStoryCount(final Double storyCount) {
        _storyCount = storyCount;
    }

    public String getYearBuilt() {
        return _yearBuilt;
    }

    public void setYearBuilt(final String yearBuilt) {
        _yearBuilt = yearBuilt;
    }

    public String getPropertyType() {
        return _propertyType;
    }

    public void setPropertyType(final String propertyType) {
        _propertyType = propertyType;
    }

    public String getWallCode() {
        return _wallCode;
    }

    public void setWallCode(final String wallCode) {
        _wallCode = wallCode;
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
