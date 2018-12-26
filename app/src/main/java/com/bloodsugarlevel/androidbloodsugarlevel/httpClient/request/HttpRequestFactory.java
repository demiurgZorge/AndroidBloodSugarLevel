package com.bloodsugarlevel.androidbloodsugarlevel.httpClient.request;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bloodsugarlevel.MyApplication;
import com.bloodsugarlevel.androidbloodsugarlevel.Utils.JsonUtils;
import com.bloodsugarlevel.androidbloodsugarlevel.common.FilterState;
import com.bloodsugarlevel.androidbloodsugarlevel.common.PagingState;
import com.bloodsugarlevel.androidbloodsugarlevel.common.QueryState;
import com.bloodsugarlevel.androidbloodsugarlevel.common.SortState;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.DeleteSugarDto;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.LoginDto;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.RegisterUserDto;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarCreateDto;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarDto;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.UserDto;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.EntityResponseListenerBase;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.ErroResponseListenerImpl;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.IUiUpdateEntityListener;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.IUiUpdateListListener;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.ListResponseListenerBase;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.UrlBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HttpRequestFactory {
    public static JsonObjectRequest createSugarListRequest(Context context,
                                                           IUiUpdateListListener uiUpdateListener,
                                                           Date beginDate, Date endDate,
                                                           String tag, String sessionIdCookie){
        List<FilterState> filters = new ArrayList<>();
        FilterState beginState = new FilterState();
        beginState.setFilterName("minDatetime");
        beginState.setFieldValue(beginDate);
        filters.add(beginState);

        FilterState endState = new FilterState();
        endState.setFilterName("maxDatetime");
        endState.setFieldValue(endDate);
        filters.add(endState);

        PagingState pagingState = new PagingState();
        SortState sortState = new SortState();
        QueryState query = new QueryState(pagingState, sortState, filters);

        CookieRequest request = new CookieRequest(Request.Method.POST,
                UrlBuilder.getSugarListUrl(),
                query.toJSONObject(),
                new ListResponseListenerBase<>(context, SugarDto.class, uiUpdateListener),
                new ErroResponseListenerImpl(context),
                tag,
                sessionIdCookie);
        return request;
    }

    public static JsonObjectRequest createSugarRequest(Context context,
                                                       IUiUpdateEntityListener uiUpdateListener,
                                                       SugarCreateDto sugarDto,
                                                           String tag, String sessionIdCookie){
        CookieRequest request = new CookieRequest(Request.Method.POST,
                UrlBuilder.createSugarUrl(),
                sugarDto.toJSONObject(),
                new EntityResponseListenerBase<>(context, SugarDto.class, uiUpdateListener),
                new ErroResponseListenerImpl(context),
                tag,
                sessionIdCookie);
        return request;
    }

    public static JsonObjectRequest deleteSugarListRequest(Context context,
                                                       IUiUpdateEntityListener uiUpdateListener,
                                                       List<Long> sugarIdList,
                                                           String tag, String sessionIdCookie){
        DeleteSugarDto dto = new DeleteSugarDto(sugarIdList);
        CookieRequest request = new CookieRequest(Request.Method.POST,
                UrlBuilder.deleteSugarUrl(),
                JsonUtils.toJSONObject(dto),
                new  EntityResponseListenerBase<>(context, Boolean.class, uiUpdateListener),
                new ErroResponseListenerImpl(context),
                tag,
                sessionIdCookie);
        return request;
    }

    public static JsonObjectRequest registerUserRequest(final Context context,
                                                        IUiUpdateEntityListener uiUpdateListener,
                                                        RegisterUserDto userRegisterDto,
                                                        String tag){
        AuthRequest request = new AuthRequest(Request.Method.POST,
                UrlBuilder.registerNewUserUrl(),
                userRegisterDto.toJSONObject(),
                new  EntityResponseListenerBase<>(context, UserDto.class, uiUpdateListener),
                new ErroResponseListenerImpl(context), tag);

        return request;
    }

    public static CookieRequest signoutUserRequest(final Context context,
                                                              IUiUpdateEntityListener uiUpdateListener,
                                                              String tag, String sessionIdCookie){
        CookieRequest request = new CookieRequest(Request.Method.GET,
                UrlBuilder.signoutUserUrl(),
                null,
                new  EntityResponseListenerBase<>(context, String.class, uiUpdateListener),
                new ErroResponseListenerImpl(context), tag, sessionIdCookie);
        return request;
    }

    public static JsonObjectRequest loginWithPasswordUserRequest(final Context context,
                                                                 IUiUpdateEntityListener uiUpdateListener,
                                                                 LoginDto loginDto,
                                                                 String tag){
        AuthRequest request = new AuthRequest(Request.Method.POST,
                UrlBuilder.loginWithPasswordUserUrl(),
                loginDto.toJSONObject(),
                new  EntityResponseListenerBase<>(context, UserDto.class, uiUpdateListener),
                new ErroResponseListenerImpl(context), tag);
        return request;
    }

    public static JsonObjectRequest loginWithTokenUserRequest(final Context context,
                                                              IUiUpdateEntityListener uiUpdateListener,
                                                              String token,
                                                              String tag){
        AuthRequest request = new AuthRequest(Request.Method.POST,
                UrlBuilder.loginWithTokenUserUrl(),
                JsonUtils.toJSONObject(token),
                new  EntityResponseListenerBase<>(context, UserDto.class, uiUpdateListener),
                new ErroResponseListenerImpl(context), tag);
        return request;
    }

    public static JsonObjectRequest loggedUserRequest(Context context,
                                                                 IUiUpdateEntityListener uiUpdateListener,
                                                      String tag, String sessionIdCookie){
        CookieRequest request = new CookieRequest(Request.Method.GET,
                UrlBuilder.loggedUserUrl(),
                null,
                new  EntityResponseListenerBase<>(context, UserDto.class, uiUpdateListener),
                new ErroResponseListenerImpl(context),
                tag,
                sessionIdCookie);
        return request;
    }
}
