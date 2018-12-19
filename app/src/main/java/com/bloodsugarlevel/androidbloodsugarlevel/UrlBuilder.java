package com.bloodsugarlevel.androidbloodsugarlevel;

import android.support.annotation.NonNull;

public class UrlBuilder {

    public static final String SLASH = "/";
    public static final String TOMCAT_SERVER = "http://78.46.233.90:8080";
    public static final String NAME_SERVER = "auth";
    public static final String HTTP_78_46_233_90_8080_AUTH_SUGAR_GETRANGE_1 = "http://78.46.233.90:8080/auth/sugar/getrange/1";

    public static final String SUGAR = "sugar";
    public UrlBuilder(){

    }


    //"http://78.46.233.90:8080/auth/
    @NonNull
    private static StringBuilder getServerName() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TOMCAT_SERVER);
        stringBuilder.append(SLASH);
        stringBuilder.append(NAME_SERVER);
        stringBuilder.append(SLASH);
        return stringBuilder;
    }

    //http://78.46.233.90:8080/auth/sugar/;
    @NonNull
    public static StringBuilder sugarService(){
        return getServerName().append(SUGAR).append(SLASH);
    }


    //"http://78.46.233.90:8080/auth/sugar/getrange/1";
    public static String getSugarListUrl(Long userId){
        return sugarService().append("getrange").append(SLASH).append(userId).toString();
    }

    //"http://78.46.233.90:8080/auth/sugar/create/1";
    public static String createSugarUrl(Long userId){
        return sugarService().append("create").append(SLASH).append(userId).toString();
    }
}
