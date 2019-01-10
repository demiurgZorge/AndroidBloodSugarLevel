package com.bloodsugarlevel.androidbloodsugarlevel.livedata;

import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.Sugar;

import java.util.Date;
import java.util.List;

public class SugarRangeRequestDto {
    public Date dateMin;
    public Date dateMax;
    public String userLogin;
    public List<Sugar> listForDelete;

    public SugarRangeRequestDto (Date dateMin, Date dateMax, String login){
        this.dateMax = dateMax;
        this.dateMin = dateMin;
        this.userLogin = login;
    }

    public SugarRangeRequestDto(Date dateMin, Date dateMax, String login, List<Sugar> listForDelete) {
        this.dateMin = dateMin;
        this.dateMax = dateMax;
        this.userLogin = login;
        this.listForDelete = listForDelete;
    }
}
