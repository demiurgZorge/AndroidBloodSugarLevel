package com.bloodsugarlevel.androidbloodsugarlevel.httpClient;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bloodsugarlevel.androidbloodsugarlevel.UrlBuilder;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarCreateDto;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarDto;
import com.bloodsugarlevel.common.FilterState;
import com.bloodsugarlevel.common.PagingState;
import com.bloodsugarlevel.common.QueryState;
import com.bloodsugarlevel.common.SortState;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HttpRequestFactory {

    public static JsonObjectRequest createSugarListRequest(Context context,
                                                           IUiUpdateListListener uiUpdateListener,
                                                           Date beginDate, Date endDate,
                                                           String tag){
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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                UrlBuilder.getSugarListUrl(1L),
                query.toJSONObject(),
                new  ListResponseListenerBase<>(context, SugarDto.class, uiUpdateListener),
                new ErroResponseListenerImpl(context));
        request.setTag(tag);
        return request;
    }

    public static JsonObjectRequest createSugarRequest(Context context,
                                                       IUiUpdateEntityListener uiUpdateListener,
                                                       SugarCreateDto sugarDto,
                                                           String tag){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                UrlBuilder.createSugarUrl(1L),
                sugarDto.toJSONObject(),
                new  EntityResponseListenerBase<>(context, SugarDto.class, uiUpdateListener),
                new ErroResponseListenerImpl(context));
        request.setTag(tag);
        return request;
    }
}
