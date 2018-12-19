package com.bloodsugarlevel.androidbloodsugarlevel.httpClient;

import java.util.List;

public interface IUiUpdateListListener<T> {
    public void onResponse(List<T> list);
}
