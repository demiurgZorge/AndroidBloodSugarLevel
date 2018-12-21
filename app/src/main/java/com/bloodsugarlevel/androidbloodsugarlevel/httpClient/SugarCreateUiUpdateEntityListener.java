package com.bloodsugarlevel.androidbloodsugarlevel.httpClient;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarDto;

public class SugarCreateUiUpdateEntityListener implements IUiUpdateEntityListener<SugarDto> {
    Context context;
    public SugarCreateUiUpdateEntityListener(Context context) {
        this.context = context;
    }

    @Override
    public void onResponse(SugarDto object) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setTitle("Succes");
        builder.setMessage("Sugar Level " + object.level + " accepted");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
