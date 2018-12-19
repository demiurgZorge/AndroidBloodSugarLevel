package com.bloodsugarlevel.androidbloodsugarlevel.dto;

import java.util.Date;

public class SugarCreateDto extends BaseDto {
    public Float level;
    //public Date date;

    public SugarCreateDto() {

    }

    public SugarCreateDto(Date date, Float level) {
        //this.date = date;
        this.level = level;
    }
}
