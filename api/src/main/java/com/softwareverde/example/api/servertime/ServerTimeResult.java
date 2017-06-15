package com.softwareverde.example.api.servertime;

import com.softwareverde.example.api.response.JsonResult;
import com.softwareverde.json.Json;

public class ServerTimeResult extends JsonResult {
    protected Long _serverTime = 0L;
    protected String _formattedServerTime = null;

    public ServerTimeResult() { }
    public ServerTimeResult(final Long serverTime, final String formattedServerTime) {
        _serverTime = serverTime;
        _formattedServerTime = formattedServerTime;
    }

    public void setTime(final Long serverTime) {
        _serverTime = serverTime;
    }

    public void setFormattedTime(final String formattedServerTime) {
        _formattedServerTime = formattedServerTime;
    }

    @Override
    public Json toJson() {
        final Json json =  super.toJson();

        json.put("serverTime", _serverTime);
        json.put("formattedServerTime", _formattedServerTime);

        return json;
    }
}
