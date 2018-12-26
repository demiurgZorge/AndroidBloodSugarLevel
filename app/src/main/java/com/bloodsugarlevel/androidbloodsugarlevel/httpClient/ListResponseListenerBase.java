package com.bloodsugarlevel.androidbloodsugarlevel.httpClient;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.android.volley.Response;
import com.bloodsugarlevel.androidbloodsugarlevel.common.ApiListResult;
import com.bloodsugarlevel.androidbloodsugarlevel.common.ApiResponse;
import com.bloodsugarlevel.androidbloodsugarlevel.tabfragment.AlertDialogManager;

import org.json.JSONObject;

import java.util.List;

public class ListResponseListenerBase<T>  implements Response.Listener<JSONObject> {

    Context context;
    Class<T> klass;
    List<Class<T>> data = null;
    IUiUpdateListListener<Class<T>> uiListener;
    public ListResponseListenerBase(Context context, Class<T> klass, IUiUpdateListListener<Class<T>> uiListener){
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
            AlertDialogManager.showAlertDialog(context, "Error", e.getMessage());
        }
    }
}
