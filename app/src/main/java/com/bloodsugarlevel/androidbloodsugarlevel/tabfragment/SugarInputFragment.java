package com.bloodsugarlevel.androidbloodsugarlevel.tabfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bloodsugarlevel.androidbloodsugarlevel.R;
import com.bloodsugarlevel.androidbloodsugarlevel.input.DatePickerFragment;
import com.bloodsugarlevel.androidbloodsugarlevel.input.DateTimeListener;
import com.bloodsugarlevel.androidbloodsugarlevel.input.TimePickerFragment;

public class SugarInputFragment extends Fragment {



    DateTimeListener mDateTime;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sugar_input_fragment, container, false);
        Button timeButton = view.findViewById(R.id.timeButton);
        Button dateButton = view.findViewById(R.id.dateButton);
        mDateTime = new DateTimeListener(dateButton,timeButton);
        timeButton.setText(mDateTime.getHourMinuteString());
        timeButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.setDateTimeListener(mDateTime);
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });


        dateButton.setText(mDateTime.getYearMonthDayString());
        dateButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setDateTimeListener(mDateTime);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        Button inputButton = view.findViewById(R.id.inputButton);
        inputButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
}
