package com.bloodsugarlevel.androidbloodsugarlevel.dto;

import java.util.List;

public class DeleteSugarDto {
    public List<Long> idList;

    public DeleteSugarDto(List<Long> sugarIdList) {
        this.idList = sugarIdList;
    }
}
