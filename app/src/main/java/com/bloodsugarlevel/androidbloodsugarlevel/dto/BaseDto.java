package com.bloodsugarlevel.androidbloodsugarlevel.dto;

import com.google.gson.Gson;

import org.json.JSONObject;

public class BaseDto {
    public JSONObject toJSONObject(){
        String str = toJsonString();
        try {
            JSONObject mJSONObject = new JSONObject(str);
            return mJSONObject;
        }
        catch (Exception e) {
            return null;
        }
    }

    public String toJsonString(){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }
}
