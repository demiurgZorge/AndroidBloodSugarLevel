package com.bloodsugarlevel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.bloodsugarlevel.androidbloodsugarlevel.R;
import com.bloodsugarlevel.androidbloodsugarlevel.tabfragment.AlertDialogManager;

import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {

    public static final String HTTP_VOLEY_TAG = "HTTP_VOLEY_TAG";
    public static final String TOKEN_AUTH_KEY = "TOKEN_AUTH_KEY";
    public static final String NO_AUTH_TOKEN = "NO_AUTH_TOKEN";


    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Set-Cookie";
    public static final String SESSION_ID_COOKIE = "JSESSIONID";
    public static final String NO_COOKIE = "NO_COOKIE";

    public static MyApplication getInstance() {
        return instance;
    }

    static MyApplication instance;
    private RequestQueue mRequestQueue;
    private SharedPreferences mPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mPreferences = getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE);
        mRequestQueue = Volley.newRequestQueue(this);
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public final void addSessionCookie(Map<String, String> headers) {
        if (headers.containsKey(COOKIE_KEY)) {
            String cookie = headers.get(COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                SharedPreferences.Editor prefEditor = mPreferences.edit();
                prefEditor.putString(SESSION_ID_COOKIE, cookie);
                prefEditor.commit();
            }
        }
    }

    public String getSessionCookies(){
        return "JSESSIONID=" + getSessionId();
    }

    public String getSessionId(){
        String sessionIdCookie = mPreferences.getString(SESSION_ID_COOKIE, NO_COOKIE);
        if(sessionIdCookie.equals(NO_COOKIE)){
            throw new RuntimeException("");
        }
        return sessionIdCookie;
    }

    public boolean isUserLogIn(){
        String sessionIdCookie = mPreferences.getString(SESSION_ID_COOKIE, NO_COOKIE);
        if(sessionIdCookie.equals(NO_COOKIE)){
            return false;
        }
        return true;
    }

    public void logout(){
        SharedPreferences.Editor prefEditor = mPreferences.edit();
        prefEditor.putString(SESSION_ID_COOKIE, NO_COOKIE);
        prefEditor.putString(TOKEN_AUTH_KEY, NO_AUTH_TOKEN);
        prefEditor.commit();
    }

    public String getToken(){
        String token = mPreferences.getString(MyApplication.TOKEN_AUTH_KEY, NO_AUTH_TOKEN);
        return token;
    }

    public boolean tokenAuthAllow(){
        String token = mPreferences.getString(MyApplication.TOKEN_AUTH_KEY, NO_AUTH_TOKEN);
        return !token.equals(NO_AUTH_TOKEN);
    }

    public void setToken(String authToken) {
        mPreferences.edit().putString(TOKEN_AUTH_KEY, authToken).commit();
    }
}
