package com.bloodsugarlevel.androidbloodsugarlevel.httpClient;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.android.volley.Response;
import com.bloodsugarlevel.common.ApiListResult;
import com.bloodsugarlevel.common.ApiResponse;

import org.json.JSONObject;

import java.util.List;

public class ListResponseListenerBase<T>  implements Response.Listener<JSONObject> {

    Context context;
    Class<T> klass;
    List<Class<T>> data = null;
    IUiUpdateListener<Class<T>> uiListener;
    public ListResponseListenerBase(Context context, Class<T> klass, IUiUpdateListener<Class<T>> uiListener){
        this.context = context;
        this.klass = klass;
        this.uiListener = uiListener;
    }

    @Override
    public void onResponse(JSONObject response) {
        ApiResponse resp = new ApiResponse(response);
        try {
            ApiListResult<List<Class<T>>> listResult = ApiListResult.fromResponse(resp, klass).getListResult();
            data = listResult.getData();
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
