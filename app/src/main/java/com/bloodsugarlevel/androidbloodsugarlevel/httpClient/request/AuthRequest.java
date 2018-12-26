package com.bloodsugarlevel.androidbloodsugarlevel.httpClient.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bloodsugarlevel.MyApplication;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthRequest extends JsonObjectRequest {

    public AuthRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener,
                         Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    public AuthRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener,
                         Response.ErrorListener errorListener, String tag) {
        super(method, url, jsonRequest, listener, errorListener);
        setTag(tag);
    }
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        MyApplication.getInstance().addSessionCookie(response.headers);
        return super.parseNetworkResponse(response);
    }
}
