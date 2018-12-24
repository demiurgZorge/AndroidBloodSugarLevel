package com.bloodsugarlevel.androidbloodsugarlevel.Utils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtils {
    public static JSONObject toJSONObject(Object obj){
        String str = toJsonString(obj);
        try {
            JSONObject mJSONObject = new JSONObject(str);
            return mJSONObject;
        }
        catch (Exception e) {
            return null;
        }
    }

    public static JSONArray toJSONArray(Object obj){
        String str = toJsonString(obj);
        try {
            JSONArray mJSONArray = new JSONArray(str);
            return mJSONArray;
        }
        catch (Exception e) {
            return null;
        }
    }

    public static String toJsonString(Object obj){
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        return json;
    }
}
