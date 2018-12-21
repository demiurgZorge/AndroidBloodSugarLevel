package com.bloodsugarlevel.androidbloodsugarlevel.dto;

import org.json.JSONObject;
import com.google.gson.Gson;
import java.util.Date;

public class SugarCreateDto {
    public Float level;
    public Long date;

    public SugarCreateDto() {

    }

    public SugarCreateDto(Date date, Float level) {
        this.date = date.getTime();
        this.level = level;
    }

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
