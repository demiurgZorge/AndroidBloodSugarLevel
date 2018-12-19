package com.bloodsugarlevel.androidbloodsugarlevel.httpClient;

import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class HttptRequestBase extends JsonObjectRequest {

    public HttptRequestBase(String url,
                            @Nullable JSONObject jsonRequest,
                            Response.Listener<JSONObject> listener,
                            @Nullable Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

}
