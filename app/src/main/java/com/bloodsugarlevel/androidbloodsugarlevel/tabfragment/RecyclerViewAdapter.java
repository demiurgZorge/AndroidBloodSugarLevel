package com.bloodsugarlevel.androidbloodsugarlevel.tabfragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bloodsugarlevel.androidbloodsugarlevel.R;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarDto;
import com.bloodsugarlevel.androidbloodsugarlevel.input.DateTimeListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private List<SugarDto> itemList = new ArrayList<>();
    private List<Long> itemIdList = new ArrayList<>();
    private Context context;
    interface IOnCheckedListener{
        void noChecked(Long id, Boolean b);
    }

    IOnCheckedListener mIOnCheckedListener;
    public RecyclerViewAdapter(Context context) {
        this.context = context;
        this.mIOnCheckedListener = new IOnCheckedListener() {
            @Override
            public void noChecked(Long id, Boolean b) {
                if(b){
                    itemIdList.add(id);
                }else{
                    itemIdList.remove(id);
                }
            }
        };
    }


    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, final int position) {
        holder.id.setText(itemList.get(position).id.toString());
        holder.level.setText(itemList.get(position).level.toString());
        Date date = itemList.get(position).date;
        holder.date.setText(DateFormat.format(DateTimeListener.DATE_PICKER_FORMAT, date));
        holder.time.setText(DateFormat.format(DateTimeListener.TIME_PICKER_FORMAT, date));
        holder.deleteSwitch.setChecked(false);
        CompoundButton.OnCheckedChangeListener onItemCheckListener = new CompoundButton.OnCheckedChangeListener() {
            Long id = itemList.get(position).id;
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mIOnCheckedListener.noChecked(id, b);
            }
        };
        holder.deleteSwitch.setOnCheckedChangeListener(onItemCheckListener);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public void setItemList(List<SugarDto> itemList){
        this.itemList = itemList;
    }

    public List<Long> getCheckedIdList() {
        return itemIdList;
    }
}
