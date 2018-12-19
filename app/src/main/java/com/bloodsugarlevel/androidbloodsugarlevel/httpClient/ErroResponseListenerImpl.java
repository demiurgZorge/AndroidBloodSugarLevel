package com.bloodsugarlevel.androidbloodsugarlevel.httpClient;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class ErroResponseListenerImpl implements Response.ErrorListener {

    private final Context context;

    public ErroResponseListenerImpl(Context context){
        this.context = context;
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        showAlertDialog(error.toString());

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
