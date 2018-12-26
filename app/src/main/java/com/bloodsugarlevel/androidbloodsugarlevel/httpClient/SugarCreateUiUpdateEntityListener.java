package com.bloodsugarlevel.androidbloodsugarlevel.httpClient;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarDto;
import com.bloodsugarlevel.androidbloodsugarlevel.tabfragment.AlertDialogManager;

public class SugarCreateUiUpdateEntityListener implements IUiUpdateEntityListener<SugarDto> {
    Context context;
    public SugarCreateUiUpdateEntityListener(Context context) {

        this.context = context;
    }

    @Override
    public void onResponse(SugarDto object) {
        AlertDialogManager.showAlertDialog(context,"Succes", "Sugar Level " + object.level + " accepted");
    }
}
