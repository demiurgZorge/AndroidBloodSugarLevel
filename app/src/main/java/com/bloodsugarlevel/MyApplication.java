package com.bloodsugarlevel;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.bloodsugarlevel.androidbloodsugarlevel.R;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.UserDto;
import com.bloodsugarlevel.androidbloodsugarlevel.room.BloodSugarLevelDatabase;
import com.bloodsugarlevel.androidbloodsugarlevel.tabfragment.AlertDialogManager;

import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {

    public static final String HTTP_VOLEY_TAG = "HTTP_VOLEY_TAG";
    public static final String LOGGED_USER_KEY = "LOGGED_USER_KEY";
    public static final String NO_USER_KEY = "NO_USER_KEY";
    public static final String TOKEN_AUTH_KEY = "TOKEN_AUTH_KEY";
    public static final String INNER_TOKEN = "INNER_TOKEN";
    public static final String NO_AUTH_TOKEN = "NO_AUTH_TOKEN";
    public static final String START_DAY_SHIFT_KEY = "START_DAY_SHIFT_KEY";
    public static final String END_DAY_SHIFT_KEY = "END_DAY_SHIFT_KEY";


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
    private BloodSugarLevelDatabase mDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //mPreferences = getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE);
        mPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        try {
            mDatabase = Room.databaseBuilder(this, BloodSugarLevelDatabase.class, "BloodSugarLevelDataBase").build();
        } catch (Exception e) {

            System.out.print(e.getMessage());
        }
        try {
            mRequestQueue = Volley.newRequestQueue(this);
        } catch (Exception e) {

        }

    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public BloodSugarLevelDatabase getDatabase() {
        return mDatabase;
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
        prefEditor.putString(LOGGED_USER_KEY, NO_USER_KEY);
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

    public void getLoggedUserToken(String authToken) {
        mPreferences.edit().putString(TOKEN_AUTH_KEY, authToken).commit();
    }

    public void setLoggedUser(String authToken, String name ) {
        mPreferences.edit().putString(TOKEN_AUTH_KEY, authToken).commit();
        mPreferences.edit().putString(LOGGED_USER_KEY, name).commit();
    }

    public void setLoggedUserName(String name ) {
        mPreferences.edit().putString(LOGGED_USER_KEY, name).commit();
    }

    public String getLoggedUserName(){
        String name = mPreferences.getString(MyApplication.LOGGED_USER_KEY, NO_USER_KEY);
        return name;
    }

    public void setDefaultShiftDays(int START_DAY_SHIFT, int END_DAY_SHIFT) {
        mPreferences.edit().putString(START_DAY_SHIFT_KEY, Integer.toString(START_DAY_SHIFT)).commit();
        mPreferences.edit().putString(END_DAY_SHIFT_KEY, Integer.toString(END_DAY_SHIFT)).commit();
    }

    public boolean isOnlineAndAuthenticated(){
        return isInternetAllow() && tokenAuthAllow();
    }

    public boolean isOfflineAndUserRegistered(){
        return !isInternetAllow() && !getLoggedUserName().equals(MyApplication.NO_USER_KEY);
    }

    public int getDefaultShiftStartDays() {
        String start = mPreferences.getString(MyApplication.START_DAY_SHIFT_KEY, "-7");
        return Integer.parseInt(start);
    }

    public int getDefaultShiftEndDays() {
        String end = mPreferences.getString(MyApplication.END_DAY_SHIFT_KEY, "1");
        return Integer.parseInt(end);
    }

    public boolean isInternetAllow() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
       // return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return false;
    }
}
