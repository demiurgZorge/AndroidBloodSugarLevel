package com.bloodsugarlevel.androidbloodsugarlevel.httpClient;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.android.volley.Response;
import com.bloodsugarlevel.common.ApiResponse;
import com.bloodsugarlevel.common.ApiSuccessResult;

import org.json.JSONObject;

public class EntityResponseListenerBase<T>  implements Response.Listener<JSONObject> {

    Context context;
    Class<T> klass;
    IUiUpdateEntityListener<Class<T>> uiListener;
    Class<T> data;

    public EntityResponseListenerBase(Context context, Class<T> klass, IUiUpdateEntityListener<Class<T>> uiListener){
        this.context = context;
        this.klass = klass;
        this.uiListener = uiListener;
    }

    @Override
    public void onResponse(JSONObject response) {
        ApiResponse resp = new ApiResponse(response);
        try {
            ApiSuccessResult<Class<T>> result = ApiSuccessResult.fromResponse(resp, klass).getSuccessResult();
            data = result.getData();
            uiListener.onResponse(data);
        } catch (Exception e) {
            showAlertDialog(e.getMessage());
        }
    }

    private void showAlertDialog(String string) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setTitle("Error");
        builder.setMessage(string);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

