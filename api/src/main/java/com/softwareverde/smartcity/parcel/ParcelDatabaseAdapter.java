package com.softwareverde.smartcity.parcel;

import com.softwareverde.database.DatabaseConnection;
import com.softwareverde.database.DatabaseException;
import com.softwareverde.database.Query;
import com.softwareverde.database.Row;
import com.softwareverde.geo.util.GeoUtil;
import com.softwareverde.smartcity.util.SmartCityUtil;
import com.softwareverde.util.DateUtil;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParcelDatabaseAdapter {

    private DatabaseConnection<Connection> _databaseConnection;

    public ParcelDatabaseAdapter(final DatabaseConnection<Connection> databaseConnection) {
        _databaseConnection = databaseConnection;
    }

    public List<Parcel> inflateAll() throws DatabaseException {
        final List<Row> rows = _databaseConnection.query("SELECT * FROM parcels", null);

        final List<Parcel> parcels = new ArrayList<>();
        for (final Row row : rows) {
            final Parcel parcel = _fromRow(row);
            parcels.add(parcel);
        }
        return parcels;
    }

    public Parcel inflateById(final Long id) throws DatabaseException {
        final Query query = new Query("SELECT * FROM parcels WHERE id = ?");
        query.setParameter(id);

        final List<Row> rows = _databaseConnection.query(query);
        if (rows.size() == 0) {
            return null;
        }
        else {
            final Row row = rows.get(0);
            return _fromRow(row);
        }
    }

    public Parcel inflateByParcelId(final String parcelId) throws DatabaseException {
        final Query query = new Query("SELECT * FROM parcels WHERE parcel_id = ?");
        query.setParameter(parcelId);

        final List<Row> rows = _databaseConnection.query(query);
        if (rows.size() == 0) {
            return null;
        }
        else {
            final Row row = rows.get(0);
            return _fromRow(row);
        }
    }

    public List<Parcel> inflateBySearchCriteria(final Double radius, final Double latitude, final Double longitude, final String address, final String ownerName, final Date transferDateAfter, final Date transferDateBefore, final Double lastSalePriceMin, final Double lastSalePriceMax, final String city, final String zipcode, final Double acreageMin, final Double acreageMax, final Double primaryBuildingAreaSquareFeetMin, final Double primaryBuildingAreaSquareFeetMax) throws DatabaseException {
        SimpleDateFormat mysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String transferDateAfterString = transferDateAfter == null ? null : mysqlDateFormat.format(transferDateAfter);
        final String transferDateBeforeString = transferDateBefore == null ? null : mysqlDateFormat.format(transferDateBefore);

        Double latitudeMax = null;
        Double latitudeMin = null;
        Double longitudeMax = null;
        Double longitudeMin = null;
        if (radius != null) {
            latitudeMax = latitude == null ? null : GeoUtil.addMetersToLatitude(latitude, longitude, radius);
            latitudeMin = latitude == null ? null : GeoUtil.addMetersToLatitude(latitude, longitude, -radius);
            longitudeMax = longitude == null ? null : GeoUtil.addMetersToLongitude(latitude, longitude, radius);
            longitudeMin = longitude == null ? null : GeoUtil.addMetersToLongitude(latitude, longitude, -radius);
        }

        final List<Row> rows = _databaseConnection.query(
                "SELECT * FROM parcels WHERE"
                        +" (LENGTH(?) = 0 OR CONCAT(owner_address1, ' ', "
                        +                          "owner_address2) LIKE ?)"
                        +" AND (LENGTH(?) = 0 OR latitude <= ?)"
                        +" AND (LENGTH(?) = 0 OR latitude >= ?)"
                        +" AND (LENGTH(?) = 0 OR longitude <= ?)"
                        +" AND (LENGTH(?) = 0 OR longitude >= ?)"
                        +" AND (LENGTH(?) = 0 OR owner_name1 LIKE ? OR owner_name2 LIKE ? OR owner_name3 LIKE ?)"
                        +" AND (LENGTH(?) = 0 OR transfer_date >= CAST(? AS DATE))"
                        +" AND (LENGTH(?) = 0 OR transfer_date <= CAST(? AS DATE))"
                        +" AND (LENGTH(?) = 0 OR last_sale_price >= ?)"
                        +" AND (LENGTH(?) = 0 OR last_sale_price <= ?)"
                        +" AND (LENGTH(?) = 0 OR city = ?)"
                        +" AND (LENGTH(?) = 0 OR zipcode LIKE ?)"
                        +" AND (LENGTH(?) = 0 OR acreage >= ?)"
                        +" AND (LENGTH(?) = 0 OR acreage <= ?)"
                        +" AND (LENGTH(?) = 0 OR primary_building_area_sqft >= ?)"
                        +" AND (LENGTH(?) = 0 OR primary_building_area_sqft <= ?)",
                new String[] {
                        SmartCityUtil.toEmptyStringIfNull(address),                             SmartCityUtil.toEmptyStringIfNull(address),
                        SmartCityUtil.toEmptyStringIfNull(latitudeMax),                         SmartCityUtil.toEmptyStringIfNull(latitudeMax),
                        SmartCityUtil.toEmptyStringIfNull(latitudeMin),                         SmartCityUtil.toEmptyStringIfNull(latitudeMin),
                        SmartCityUtil.toEmptyStringIfNull(longitudeMax),                        SmartCityUtil.toEmptyStringIfNull(longitudeMax),
                        SmartCityUtil.toEmptyStringIfNull(longitudeMin),                        SmartCityUtil.toEmptyStringIfNull(longitudeMin),
                        SmartCityUtil.toEmptyStringIfNull(ownerName),                           SmartCityUtil.toEmptyStringIfNull(ownerName),
                        SmartCityUtil.toEmptyStringIfNull(ownerName),                           SmartCityUtil.toEmptyStringIfNull(ownerName), // repeated for owner_name2/3
                        SmartCityUtil.toEmptyStringIfNull(transferDateAfter),                   SmartCityUtil.toEmptyStringIfNull(transferDateAfterString),
                        SmartCityUtil.toEmptyStringIfNull(transferDateBefore),                  SmartCityUtil.toEmptyStringIfNull(transferDateBeforeString),
                        SmartCityUtil.toEmptyStringIfNull(lastSalePriceMin),                    SmartCityUtil.toEmptyStringIfNull(lastSalePriceMin),
                        SmartCityUtil.toEmptyStringIfNull(lastSalePriceMax),                    SmartCityUtil.toEmptyStringIfNull(lastSalePriceMax),
                        SmartCityUtil.toEmptyStringIfNull(city),                                SmartCityUtil.toEmptyStringIfNull(city),
                        SmartCityUtil.toEmptyStringIfNull(zipcode),                             SmartCityUtil.toEmptyStringIfNull(zipcode),
                        SmartCityUtil.toEmptyStringIfNull(acreageMin),                          SmartCityUtil.toEmptyStringIfNull(acreageMin),
                        SmartCityUtil.toEmptyStringIfNull(acreageMax),                          SmartCityUtil.toEmptyStringIfNull(acreageMax),
                        SmartCityUtil.toEmptyStringIfNull(primaryBuildingAreaSquareFeetMin),    SmartCityUtil.toEmptyStringIfNull(primaryBuildingAreaSquareFeetMin),
                        SmartCityUtil.toEmptyStringIfNull(primaryBuildingAreaSquareFeetMax),    SmartCityUtil.toEmptyStringIfNull(primaryBuildingAreaSquareFeetMax)
                }
        );

        final List<Parcel> parcels = new ArrayList<>();
        for (final Row row : rows) {
            final Parcel parcel = _fromRow(row);
            parcels.add(parcel);
        }
        return parcels;
    }

    private Parcel _fromRow(final Row row) throws DatabaseException {
        final Long id = row.getLong("id");
        final String parcelId = row.getString("parcel_id");
        final Double exemptPropertyAppraisalLandValue = row.getDouble("exempt_property_appraisal_land_value");
        final Double exemptPropertyAppraisalBuildingValue = row.getDouble("exempt_property_appraisal_building_value");
        final Double exemptPropertyAppraisalTotalValue = row.getDouble("exempt_property_appraisal_total_value");
        final Double taxablePropertyAppraisalLandValue = row.getDouble("taxable_property_appraisal_land_value");
        final Double taxablePropertyAppraisalBuildingValue = row.getDouble("taxable_property_appraisal_building_value");
        final Double taxablePropertyAppraisalTotalValue = row.getDouble("taxable_property_appraisal_total_value");
        final String auditorsMapNumber = row.getString("auditors_map_number");
        final String auditorsRoutingSequence = row.getString("auditors_routing_sequence");
        final String appraisalLandUseCode = row.getString("appraisal_land_use_code");
        final String currentAgriculturalUseValue = row.getString("current_agricultural_use_value");
        final String schoolDistrictCode = row.getString("school_district_code");
        final Boolean containsHomesteadExemptionValues = row.getBoolean("contains_homestead_exemption_values");
        final String taxBillMailingAddressLine1 = row.getString("tax_bill_mailing_address_line1");
        final String taxBillMailingAddressLine2 = row.getString("tax_bill_mailing_address_line2");
        final String taxBillMailingAddressLine3 = row.getString("tax_bill_mailing_address_line3");
        final String taxBillMailingAddressLine4 = row.getString("tax_bill_mailing_address_line4");
        final String transferDateString = row.getString("transfer_date");
        final Long transferYear = row.getLong("transfer_year");
        final String ownerName1 = row.getString("owner_name1");
        final String ownerName2 = row.getString("owner_name2");
        final String ownerName3 = row.getString("owner_name3");
        final String ownerAddress1 = row.getString("owner_address1");
        final String ownerAddress2 = row.getString("owner_address2");
        final String auditorsNeighborhoodCode = row.getString("auditors_neighborhood_code");
        final String floodInfo = row.getString("flood_info");
        final String propertyClass = row.getString("property_class");
        final Long numberOfPropertyCards = row.getLong("number_of_property_cards");
        final Double acreage = row.getDouble("acreage");
        final Double lastSalePrice = row.getDouble("last_sale_price");
        final Double totalTaxableValue = row.getDouble("total_taxable_value");
        final String city = row.getString("city");
        final String state = row.getString("state");
        final String zipcode = row.getString("zipcode");
        final String descriptionLine1 = row.getString("description_line1");
        final String descriptionLine2 = row.getString("description_line2");
        final String descriptionLine3 = row.getString("description_line3");
        final String taxDesignation = row.getString("tax_designation");
        final Double primaryBuildingAreaSquareFeet = row.getDouble("primary_building_area_sqft");
        final String dwellingType = row.getString("dwelling_type");
        final Long totalRoomCount = row.getLong("total_room_count");
        final Long fullBathCount = row.getLong("full_bath_count");
        final Long halfBathCount = row.getLong("half_bath_count");
        final Long bedroomCount = row.getLong("bedroom_count");
        final String centralAirStyle = row.getString("central_air_style");
        final String buildingCondition = row.getString("building_condition");
        final Boolean hasFireplaces = row.getBoolean("has_fireplaces");
        final String buildingGrade = row.getString("building_grade");
        final String height = row.getString("height");
        final Double storyCount = row.getDouble("story_count");
        final String yearBuilt = row.getString("year_built");
        final String propertyType = row.getString("property_type");
        final String wallCode = row.getString("wall_code");
        final Double latitude = row.getDouble("latitude");
        final Double longitude = row.getDouble("longitude");

        Date transferDate = new Date(DateUtil.datetimeToTimestamp(transferDateString));

        final Parcel parcel = new Parcel();

        parcel.setId(id);
        parcel.setParcelId(parcelId);
        parcel.setExemptPropertyAppraisalLandValue(exemptPropertyAppraisalLandValue);
        parcel.setExemptPropertyAppraisalBuildingValue(exemptPropertyAppraisalBuildingValue);
        parcel.setExemptPropertyAppraisalTotalValue(exemptPropertyAppraisalTotalValue);
        parcel.setTaxablePropertyAppraisalLandValue(taxablePropertyAppraisalLandValue);
        parcel.setTaxablePropertyAppraisalBuildingValue(taxablePropertyAppraisalBuildingValue);
        parcel.setTaxablePropertyAppraisalTotalValue(taxablePropertyAppraisalTotalValue);
        parcel.setAuditorsMapNumber(auditorsMapNumber);
        parcel.setAuditorsRoutingSequence(auditorsRoutingSequence);
        parcel.setAppraisalLandUseCode(appraisalLandUseCode);
        parcel.setCurrentAgriculturalUseValue(currentAgriculturalUseValue);
        parcel.setSchoolDistrictCode(schoolDistrictCode);
        parcel.setContainsHomesteadExemptionValues(containsHomesteadExemptionValues);
        parcel.setTaxBillMailingAddressLine1(taxBillMailingAddressLine1);
        parcel.setTaxBillMailingAddressLine2(taxBillMailingAddressLine2);
        parcel.setTaxBillMailingAddressLine3(taxBillMailingAddressLine3);
        parcel.setTaxBillMailingAddressLine4(taxBillMailingAddressLine4);
        parcel.setTransferDate(transferDate);
        parcel.setTransferYear(transferYear);
        parcel.setOwnerName1(ownerName1);
        parcel.setOwnerName2(ownerName2);
        parcel.setOwnerName3(ownerName3);
        parcel.setOwnerAddress1(ownerAddress1);
        parcel.setOwnerAddress2(ownerAddress2);
        parcel.setAuditorsNeighborhoodCode(auditorsNeighborhoodCode);
        parcel.setFloodInfo(floodInfo);
        parcel.setPropertyClass(propertyClass);
        parcel.setNumberOfPropertyCards(numberOfPropertyCards);
        parcel.setAcreage(acreage);
        parcel.setLastSalePrice(lastSalePrice);
        parcel.setTotalTaxableValue(totalTaxableValue);
        parcel.setCity(city);
        parcel.setState(state);
        parcel.setZipcode(zipcode);
        parcel.setDescriptionLine1(descriptionLine1);
        parcel.setDescriptionLine2(descriptionLine2);
        parcel.setDescriptionLine3(descriptionLine3);
        parcel.setTaxDesignation(taxDesignation);
        parcel.setPrimaryBuildingAreaSquareFeet(primaryBuildingAreaSquareFeet);
        parcel.setDwellingType(dwellingType);
        parcel.setTotalRoomCount(totalRoomCount);
        parcel.setFullBathCount(fullBathCount);
        parcel.setHalfBathCount(halfBathCount);
        parcel.setBedroomCount(bedroomCount);
        parcel.setCentralAirStyle(centralAirStyle);
        parcel.setBuildingCondition(buildingCondition);
        parcel.setHasFireplaces(hasFireplaces);
        parcel.setBuildingGrade(buildingGrade);
        parcel.setHeight(height);
        parcel.setStoryCount(storyCount);
        parcel.setYearBuilt(yearBuilt);
        parcel.setPropertyType(propertyType);
        parcel.setWallCode(wallCode);
        parcel.setLatitude(latitude);
        parcel.setLongitude(longitude);

        return parcel;
    }
}
