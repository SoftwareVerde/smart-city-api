package com.softwareverde.smartcity.api.parcel;

import com.softwareverde.database.DatabaseConnection;
import com.softwareverde.database.DatabaseException;
import com.softwareverde.geo.util.GeoUtil;
import com.softwareverde.servlet.GetParameters;
import com.softwareverde.servlet.PostParameters;
import com.softwareverde.servlet.Servlet;
import com.softwareverde.servlet.request.Request;
import com.softwareverde.servlet.response.JsonResponse;
import com.softwareverde.servlet.response.Response;
import com.softwareverde.smartcity.api.parcel.response.GetParcelResult;
import com.softwareverde.smartcity.api.parcel.response.ListParcelsResult;
import com.softwareverde.smartcity.api.response.JsonResult;
import com.softwareverde.smartcity.environment.Environment;
import com.softwareverde.smartcity.parcel.Parcel;
import com.softwareverde.smartcity.parcel.ParcelDatabaseAdapter;
import com.softwareverde.smartcity.parcel.ParcelLocationExtractor;
import com.softwareverde.smartcity.util.SmartCityUtil;
import com.softwareverde.util.DateUtil;
import com.softwareverde.util.Util;

import java.sql.Connection;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ParcelApi implements Servlet {
    protected final Environment _environment;

    public ParcelApi(final Environment environment) {
        _environment = environment;
    }

    @Override
    public Response onRequest(final Request request) {

        final DatabaseConnection<Connection> databaseConnection;
        try {
            databaseConnection = _environment.database.newConnection();
        } catch (final DatabaseException databaseException) {
            return SmartCityUtil.generateDatabaseErrorResponse(databaseException);
        }

        final GetParameters getParameters = request.getGetParameters();
        final PostParameters postParameters = request.getPostParameters();

        /*
         * Get a parcel by its ID.
         * GET:     get[=1]
         * POST:    id[>0]           # The ID (primary key) used for this API. Mutually exclusive from parcel ID.
         *          parcel_id[*] # The Columbus-assigned parcel ID. Mutually exclusive from ID.
         */
        if (Util.parseInt(getParameters.get("get")) > 0) {
            try {
                return _getParcelById(postParameters, databaseConnection);
            } catch (final DatabaseException databaseException) {
                return SmartCityUtil.generateDatabaseErrorResponse(databaseException);
            }
        }

        /*
         * List all parcels.
         * GET:     list[=1]
         * POST:
         */
        if (Util.parseInt(getParameters.get("list")) > 0) {
            try {
                return _listParcels(postParameters, databaseConnection);
            } catch (final DatabaseException databaseException) {
                return SmartCityUtil.generateDatabaseErrorResponse(databaseException);
            }
        }

        /*
         * Search for parcels.
         * GET:     search[=1]
         * POST:    radius[>=0], latitude[*], longitude[*]      # Radius (in meters) from its Latitude/Longitude to the provided latitude and longitude (inclusive). (Optional)
         *          address[*]                                  # Match one of the owner address lines, including wildcards (via "%"). (Optional) (e.x. "front%" will match both "Front ST N" and "Front ST S")
         *          transfer_date_after[yyyy-MM-dd HH:mm:ss]    # Return parcels whose last transfer date is after transfer_date_after (inclusive). (Optional)
         *          transfer_date_before[yyyy-MM-dd HH:mm:ss]   # Return parcels whose last transfer date is before transfer_date_before (inclusive). (Optional)
         *          owner_name[*]                               # Match one of the owner names, including wildcards (via "%"). (Optional) (e.x. "%ryan%" will match both "Ryan Jones" and "Bryan Patel")
         *          last_sale_price_greater_than[>=0]           # Return parcels whose last sale price is greater than last_sale_price_greater_than. (Optional)
         *          last_sale_price_less_than[>=0]              # Return parcels whose last sale price is less than last_sale_price_less_than. (Optional)
         *          city[*]                                     # Return parcels whose city is equal to city. (Optional)
         *          zipcode[*]                                  # Match zip code, including wildcards (via "%"). (Optional) (e.x. "432%" will match both 43210 and 43201)
         *          acreage_greater_than[>=0]                   # Return parcels whose acreage is greater than acreage_greater_than. (Optional)
         *          acreage_less_than[>=0]                      # Return parcels whose acreage is less than acreage_less_than. (Optional)
         *          primary_building_area_greater_than[>=0]     # Return parcels whose primary building area is greater than primary_building_area_greater_than. (Optional)
         *          primary_building_area_less_than[>=0]        # Return parcels whose primary building area is less than primary_building_area_less_than. (Optional)
         */
        if (Util.parseInt(getParameters.get("search")) > 0) {
            try {
                return _searchForParcel(postParameters, databaseConnection);
            } catch (final DatabaseException databaseException) {
                return SmartCityUtil.generateDatabaseErrorResponse(databaseException);
            }
        }

        return new JsonResponse(Response.ResponseCodes.BAD_REQUEST, new JsonResult(false, "Nothing to do."));
    }

    private Response _listParcels(final PostParameters postParameters, final DatabaseConnection<Connection> databaseConnection) throws DatabaseException {
        final ParcelDatabaseAdapter parcelDatabaseAdapter = new ParcelDatabaseAdapter(databaseConnection);

        final ListParcelsResult jsonResult = new ListParcelsResult();
        final List<Parcel> parcels = parcelDatabaseAdapter.inflateAll();
        for (final Parcel parcel : parcels) {
            jsonResult.addParcel(parcel);
        }

        return new JsonResponse(Response.ResponseCodes.OK, jsonResult);
    }

    protected Response _getParcelById(final PostParameters postParameters, final DatabaseConnection<Connection> databaseConnection) throws DatabaseException {
        final ParcelDatabaseAdapter parcelDatabaseAdapter = new ParcelDatabaseAdapter(databaseConnection);

        final Long id = Util.parseLong(postParameters.get("id"));
        final String parcelId = postParameters.get("parcel_id");

        Parcel parcel;
        if (id > 0) {
            parcel = parcelDatabaseAdapter.inflateById(id);
        }
        else {
            parcel = parcelDatabaseAdapter.inflateByParcelId(parcelId);
        }

        if (parcel == null) {
            return new JsonResponse(Response.ResponseCodes.BAD_REQUEST, new JsonResult(false, "Parcel not found."));
        }

        final GetParcelResult jsonResult = new GetParcelResult();
        jsonResult.setParcel(parcel);

        return new JsonResponse(Response.ResponseCodes.OK, jsonResult);
    }

    private Response _searchForParcel(PostParameters postParameters, DatabaseConnection<Connection> databaseConnection) throws DatabaseException {
        final ParcelDatabaseAdapter parcelDatabaseAdapter = new ParcelDatabaseAdapter(databaseConnection);

        final Double radius = (postParameters.containsKey("radius") ? Util.parseDouble(postParameters.get("radius")) : null);
        final Double radiusLatitude = Util.parseDouble(postParameters.get("latitude"));
        final Double radiusLongitude = Util.parseDouble(postParameters.get("longitude"));

        final String address = (postParameters.containsKey("address") ? postParameters.get("address") : null);
        final String owner = (postParameters.containsKey("owner") ? postParameters.get("owner") : null);
        final String transferDateAfterString = (postParameters.containsKey("transfer_date_after") ? postParameters.get("transfer_date_after") : null);
        final Date transferDateAfter = transferDateAfterString == null ? null : new Date(DateUtil.datetimeToTimestamp(transferDateAfterString));
        final String transferDateBeforeString = (postParameters.containsKey("transfer_date_before") ? postParameters.get("transfer_date_before") : null);
        final Date transferDateBefore = transferDateBeforeString == null ? null : new Date(DateUtil.datetimeToTimestamp(transferDateBeforeString));
        final Double lastSalePriceGreaterThan = (postParameters.containsKey("last_sale_price_greater_than") ? Util.parseDouble(postParameters.get("last_sale_price_greater_than")) : null);
        final Double lastSalePriceLessThan = (postParameters.containsKey("last_sale_price_less_than") ? Util.parseDouble(postParameters.get("last_sale_price_less_than")) : null);
        final String city = (postParameters.containsKey("city") ? postParameters.get("city") : null);
        final String zipcode = (postParameters.containsKey("zipcode") ? postParameters.get("zipcode") : null);
        final Double acreageGreaterThan = (postParameters.containsKey("acreage_greater_than") ? Util.parseDouble(postParameters.get("acreage_greater_than")) : null);
        final Double acreageLessThan = (postParameters.containsKey("acreage_less_than") ? Util.parseDouble(postParameters.get("acreage_less_than")) : null);
        final Double primaryBuildingAreaSquareFeetGreaterThan = (postParameters.containsKey("primary_building_area_square_feet_greater_than") ? Util.parseDouble(postParameters.get("primary_building_area_square_feet_greater_than")) : null);
        final Double primaryBuildingAreaSquareFeetLessThan = (postParameters.containsKey("primary_building_area_square_feet_less_than") ? Util.parseDouble(postParameters.get("primary_building_area_square_feet_less_than")) : null);

        List<Parcel> matchingParcels = parcelDatabaseAdapter.inflateBySearchCriteria(radius, radiusLatitude, radiusLongitude, address, owner, transferDateAfter, transferDateBefore, lastSalePriceGreaterThan, lastSalePriceLessThan, city, zipcode, acreageGreaterThan, acreageLessThan, primaryBuildingAreaSquareFeetGreaterThan, primaryBuildingAreaSquareFeetLessThan);
        Collection<Parcel> locationFilteredParcels = GeoUtil.filterByLocation(matchingParcels, new ParcelLocationExtractor(), radius, radiusLatitude, radiusLongitude);

        final ListParcelsResult jsonResult = new ListParcelsResult();
        for (final Parcel parcel : locationFilteredParcels) {
            jsonResult.addParcel(parcel);
        }

        return new JsonResponse(Response.ResponseCodes.OK, jsonResult);
    }

}
