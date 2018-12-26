package com.bloodsugarlevel.androidbloodsugarlevel.dto;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Date;

public class SugarCreateDto extends BaseDto{
    public Float level;
    public Long date;

    public SugarCreateDto() {

    }

    public SugarCreateDto(Date date, Float level) {
        this.date = date.getTime();
        this.level = level;
    }
}
