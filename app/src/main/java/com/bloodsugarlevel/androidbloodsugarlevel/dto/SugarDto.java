package com.bloodsugarlevel.androidbloodsugarlevel.dto;

import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.Sugar;
import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SugarDto {
    public Float level;
    public Long id;
    public Date date;
    public UserDto userDto;

    public SugarDto() {
    }
    public static SugarDto create(Sugar sugar){
        SugarDto dto = new SugarDto();
        dto.id = sugar.id;
        dto.date = sugar.date;
        dto.level = sugar.level;
        return dto;
    }

    public static List<SugarDto> createList(List<Sugar> list){
        List<SugarDto> dtoList = new ArrayList<SugarDto>();
        for(Sugar sugar : list){
            dtoList.add(create(sugar));
        }
        return dtoList;
    }
}
