package com.bloodsugarlevel.common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class ApiResponse {
    String string = null;
    JSONArray jsonArray = null;
    JSONObject jsonObject = null;
    private boolean statusCode = false;

    public ApiResponse() {

    }

    public ApiResponse(Object r) {
        this();
        if (r instanceof JSONArray) {
            jsonArray = (JSONArray) r;
            string = jsonArray.toString();
        }
        if (r instanceof JSONObject) {
            jsonObject = (JSONObject) r;
            string = jsonObject.toString();
        }
        if (r instanceof String) {
            string = (String) r;
        }
    }

    public ApiResponse jsonPath() {
        return null;
    }

    public Map<String, Object> get() {
        return null;
    }

    public boolean getStatusCode() {
        return statusCode;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void setStatusCode(boolean statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return string;
    }
}
