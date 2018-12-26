package com.bloodsugarlevel.androidbloodsugarlevel.tabfragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bloodsugarlevel.MyApplication;
import com.bloodsugarlevel.androidbloodsugarlevel.R;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarCreateDto;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.UserDto;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.request.HttpRequestFactory;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.IUiUpdateEntityListener;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.SugarCreateUiUpdateEntityListener;
import com.bloodsugarlevel.androidbloodsugarlevel.input.DatePickerFragment;
import com.bloodsugarlevel.androidbloodsugarlevel.input.DateTimeListener;
import com.bloodsugarlevel.androidbloodsugarlevel.input.TimePickerFragment;

public class SugarInputFragment extends Fragment {
    public static final String SUGAR_CREATE_VOLEY_TAG = "SUGAR_CREATE_VOLEY_TAG";
    public static final Float WRONG_RESULT = -1f;

    IUiUpdateEntityListener mSugarCreateListenerImpl;
    DateTimeListener mDateTime;
    EditText mEditText;
    private RequestQueue mRequestQueue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sugar_input_fragment, container, false);
        mRequestQueue = MyApplication.getInstance().getRequestQueue();
        Button timeButton = view.findViewById(R.id.timeButton);
        Button dateButton = view.findViewById(R.id.dateButton);
        mSugarCreateListenerImpl = new SugarCreateUiUpdateEntityListener(getContext());


        mDateTime = new DateTimeListener(dateButton, timeButton);
        timeButton.setText(mDateTime.getHourMinuteString());
        timeButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.setDateTimeListener(mDateTime);
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });


        dateButton.setText(mDateTime.getYearMonthDayString());
        dateButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setDateTimeListener(mDateTime);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        Button inputButton = view.findViewById(R.id.inputButton);
        inputButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                createSugarRequestListener();
            }
        });

        mEditText = view.findViewById(R.id.sugarLevelEditText);
        return view;
    }

    private void createSugarRequestListener() {
        Float level = getValidateAndGetFloat(mEditText.getText());
        if(level == WRONG_RESULT){
            return;
        }
        SugarCreateDto dto = new SugarCreateDto(mDateTime.getCalendar().getTime(), level);
        Context ctx = getContext();
        JsonObjectRequest jsonObjectRequest = HttpRequestFactory.createSugarRequest(ctx,
                mSugarCreateListenerImpl,
                dto,
                SUGAR_CREATE_VOLEY_TAG,
                MyApplication.getInstance().getSessionCookies());
        mRequestQueue.add(jsonObjectRequest);
    }

    private Float getValidateAndGetFloat(Editable s) {
        if(s.toString() == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder.setTitle("Empty level!");
            builder.setMessage("Enter number for Sugar level!");
            AlertDialog dialog = builder.create();
            dialog.show();
            return WRONG_RESULT;
        }
        try {
            Float res = Float.parseFloat(s.toString());
            if(res < 0f || res > 100f){
                throw new Exception();
            }
            return Float.parseFloat(s.toString());
        }catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder.setTitle("Error");
            builder.setMessage("Enter number {min=0 : max=100.0} ");
            AlertDialog dialog = builder.create();
            dialog.show();
            return WRONG_RESULT;
        }
    }

    @Override
    public void onStop () {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(SUGAR_CREATE_VOLEY_TAG);
        }
    }
}
