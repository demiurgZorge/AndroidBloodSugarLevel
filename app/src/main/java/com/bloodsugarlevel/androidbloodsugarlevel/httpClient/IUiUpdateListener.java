package com.bloodsugarlevel.androidbloodsugarlevel.httpClient;

import java.util.List;

public interface IUiUpdateListener<T> {
    public void onResponse(List<T> list);
}
