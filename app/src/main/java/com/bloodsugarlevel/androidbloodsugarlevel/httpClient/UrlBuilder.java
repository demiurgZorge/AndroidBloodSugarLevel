package com.bloodsugarlevel.androidbloodsugarlevel.httpClient;

import android.support.annotation.NonNull;

public class UrlBuilder {

    public static final String SLASH = "/";
    public static final String TOMCAT_SERVER = "http://78.46.233.90:8080";
    public static final String NAME_SERVER = "auth";
    public static final String HTTP_78_46_233_90_8080_AUTH_SUGAR_GETRANGE_1 = "http://78.46.233.90:8080/auth/sugar/getrange/1";

    public static final String SUGAR = "sugar";
    public static final String USER = "user";
    public static final String SECURITY = "security";
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

    //http://78.46.233.90:8080/auth/user/;
    @NonNull
    public static StringBuilder userService(){
        return getServerName().append(USER).append(SLASH);
    }

    //http://78.46.233.90:8080/auth/security/;
    @NonNull
    public static StringBuilder securityService(){
        return getServerName().append(SECURITY).append(SLASH);
    }

    //"http://78.46.233.90:8080/auth/sugar/getrange";
    public static String getSugarListUrl(){
        return sugarService().append("getrange").toString();
    }

    //"http://78.46.233.90:8080/auth/sugar/create/1";
    public static String createSugarUrl(){
        return sugarService().append("create").toString();
    }

    //"http://78.46.233.90:8080/auth/sugar/delete";
    public static String deleteSugarUrl(){
        return sugarService().append("delete").toString();
    }

    //"http://78.46.233.90:8080/auth/user/register";
    public static String registerNewUserUrl(){
        return userService().append("register").toString();
    }

    //"http://78.46.233.90:8080/auth/security/login";
    public static String loginWithPasswordUserUrl(){
        return securityService().append("login").toString();
    }


    //"http://78.46.233.90:8080/auth/security/loginWithToken";
    public static String loginWithTokenUserUrl(){
        return securityService().append("token").toString();
    }

    //"http://78.46.233.90:8080/auth/security/logged";
    public static String loggedUserUrl(){
        return securityService().append("logged").toString();
    }

    //"http://78.46.233.90:8080/auth/security/signout";
    public static String signoutUserUrl(){
        return securityService().append("signout").toString();
    }

    //"http://78.46.233.90:8080/auth/security/logged";
    public static String getLoggedUserUrl(){
        return securityService().append("logged").toString();
    }
}
