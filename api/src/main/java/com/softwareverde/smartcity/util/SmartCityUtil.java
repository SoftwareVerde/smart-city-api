package com.softwareverde.smartcity.util;

import com.softwareverde.database.DatabaseException;
import com.softwareverde.servlet.response.JsonResponse;
import com.softwareverde.servlet.response.Response;
import com.softwareverde.smartcity.api.response.JsonResult;

public class SmartCityUtil {

    public static Response generateDatabaseErrorResponse(final DatabaseException databaseException) {
        return new JsonResponse(Response.ResponseCodes.SERVER_ERROR, new JsonResult(false, "An error occurred while trying to communicate with the database."));
    }

    public static String toEmptyStringIfNull(final Object object) {
        return ((object == null) ? "" : object.toString());
    }
}
