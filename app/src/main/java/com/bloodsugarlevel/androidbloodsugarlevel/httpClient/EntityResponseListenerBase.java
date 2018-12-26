package com.bloodsugarlevel.androidbloodsugarlevel.httpClient;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.android.volley.Response;
import com.bloodsugarlevel.androidbloodsugarlevel.common.ApiResponse;
import com.bloodsugarlevel.androidbloodsugarlevel.common.ApiSuccessResult;
import com.bloodsugarlevel.androidbloodsugarlevel.tabfragment.AlertDialogManager;

import org.json.JSONObject;

public class EntityResponseListenerBase<T>  implements Response.Listener<JSONObject> {

    Context context;
    Class<T> klass;
    IUiUpdateEntityListener<T> uiListener;


    public EntityResponseListenerBase(Context context, Class<T> klass, IUiUpdateEntityListener<T> uiListener){
        this.context = context;
        this.klass = klass;
        this.uiListener = uiListener;
    }

    @Override
    public void onResponse(JSONObject response) {
        ApiResponse resp = new ApiResponse(response);
        try {
            ApiSuccessResult<T> result = ApiSuccessResult.fromResponse(resp, klass).getSuccessResult();
            T data = result.getData();
            uiListener.onResponse(data);
        } catch (Exception e) {
            AlertDialogManager.showAlertDialog(context, "Error", e.getMessage());
        }
    }
}

