package com.softwareverde.example.api.databasetime;

import com.softwareverde.example.api.response.JsonResult;
import com.softwareverde.json.Json;

public class DatabaseTimeResult extends JsonResult {
    protected Long _databaseTime = 0L;
    protected String _formattedDatabaseTime = null;

    public DatabaseTimeResult() { }
    public DatabaseTimeResult(final Long databaseTime, final String formattedDatabaseTime) {
        _databaseTime = databaseTime;
        _formattedDatabaseTime = formattedDatabaseTime;
    }

    public void setTime(final Long serverTime) {
        _databaseTime = serverTime;
    }

    public void setFormattedTime(final String formattedServerTime) {
        _formattedDatabaseTime = formattedServerTime;
    }

    @Override
    public Json toJson() {
        final Json json =  super.toJson();

        json.put("databaseTime", _databaseTime);
        json.put("formattedDatabaseTime", _formattedDatabaseTime);

        return json;
    }
}
