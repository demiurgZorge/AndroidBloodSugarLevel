package com.bloodsugarlevel.androidbloodsugarlevel.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

public class BaseDto {

    public JSONObject toJSONObject(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return new JSONObject(this.toString());
        }
        catch (Exception e) {
            return null;
        }
    }
}
