package com.bloodsugarlevel.androidbloodsugarlevel.httpClient;

import android.support.v7.widget.RecyclerView;

import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarDto;
import com.bloodsugarlevel.androidbloodsugarlevel.tabfragment.RecyclerViewAdapter;
import com.jjoe64.graphview.GraphView;

import java.util.List;

public class EditSugarListenerImpl implements IUiUpdateListListener<SugarDto>{

    RecyclerViewAdapter recyclerViewAdapter;
    RecyclerView recyclerView;

    public EditSugarListenerImpl(RecyclerViewAdapter recyclerViewAdapter, RecyclerView recyclerView){
        super();
        this.recyclerViewAdapter = recyclerViewAdapter;
        this.recyclerView = recyclerView;
    }
    @Override
    public void onResponse(List<SugarDto> list) {
        recyclerViewAdapter.setItemList(list);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}
