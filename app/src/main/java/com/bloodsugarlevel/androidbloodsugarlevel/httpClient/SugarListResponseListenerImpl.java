package com.bloodsugarlevel.androidbloodsugarlevel.httpClient;

import android.content.Context;

import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarDto;

public class SugarListResponseListenerImpl extends ListResponseListenerBase<SugarDto> {

    public SugarListResponseListenerImpl(Context context, IUiUpdateListener uiListener) {
        super(context, SugarDto.class, uiListener);
    }

}
