package com.bloodsugarlevel.androidbloodsugarlevel.tabfragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bloodsugarlevel.androidbloodsugarlevel.R;

public class RecyclerViewHolders extends RecyclerView.ViewHolder{

    public TextView id;
    public TextView level;
    public TextView date;
    public TextView time;
    public Switch   deleteSwitch;

    public RecyclerViewHolders(View itemView) {
        super(itemView);

        id = (TextView)itemView.findViewById(R.id.sugarIdItem);
        level = (TextView)itemView.findViewById(R.id.sugarLevelItem);
        date = (TextView)itemView.findViewById(R.id.sugarDateItem);
        time = (TextView)itemView.findViewById(R.id.sugarTimeItem);
        deleteSwitch = (Switch)itemView.findViewById(R.id.sugarDeleteSwitch);
    }
}