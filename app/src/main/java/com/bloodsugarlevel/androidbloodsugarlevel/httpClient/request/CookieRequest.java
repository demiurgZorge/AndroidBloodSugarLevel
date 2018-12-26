package com.bloodsugarlevel.androidbloodsugarlevel.httpClient.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bloodsugarlevel.MyApplication;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CookieRequest extends JsonObjectRequest {
    private Map<String, String> mHeaders = new HashMap<String, String>(1);

    public CookieRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener,
                         Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    public CookieRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener,
                         Response.ErrorListener errorListener, String tag, String cookie) {
        super(method, url, jsonRequest, listener, errorListener);
        setSessionCookie(cookie);
        setTag(tag);
    }

    public void setSessionCookie(String cookie) {
        mHeaders.put("Cookie", cookie);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }

    @Override
    protected Map<String, String> getParams() {

        return mHeaders;
    }

}