package com.softwareverde.example.api.response;

import com.softwareverde.json.Json;
import com.softwareverde.json.Jsonable;

public class JsonResult implements Jsonable {
    protected Boolean _wasSuccess = true;
    protected String _errorMessage = null;

    public JsonResult() { }
    public JsonResult(final Boolean wasSuccess, final String errorMessage) {
        _wasSuccess = wasSuccess;
        _errorMessage = errorMessage;
    }

    public void setWasSuccess(final Boolean wasSuccess) {
        _wasSuccess = wasSuccess;
    }

    public void setErrorMessage(final String errorMessage) {
        _errorMessage = errorMessage;
    }

    @Override
    public Json toJson() {
        final Json json = new Json();
        json.put("wasSuccess", (_wasSuccess ? 1 : 0));
        json.put("errorMessage", _errorMessage);
        return json;
    }
}
