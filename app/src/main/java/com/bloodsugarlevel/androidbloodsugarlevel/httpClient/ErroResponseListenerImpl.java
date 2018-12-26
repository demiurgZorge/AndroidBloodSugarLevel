package com.bloodsugarlevel.androidbloodsugarlevel.httpClient;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bloodsugarlevel.MyApplication;
import com.bloodsugarlevel.androidbloodsugarlevel.tabfragment.AlertDialogManager;

import java.util.Map;

public class ErroResponseListenerImpl implements Response.ErrorListener {

    Context context;
    public ErroResponseListenerImpl(Context context){
        this.context = context;
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        AlertDialogManager.showAlertDialog(context,"Error", error.toString());

    }
}
