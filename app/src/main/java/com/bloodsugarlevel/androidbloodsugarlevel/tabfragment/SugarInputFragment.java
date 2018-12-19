package com.bloodsugarlevel.androidbloodsugarlevel.tabfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bloodsugarlevel.androidbloodsugarlevel.R;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarCreateDto;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.HttpRequestFactory;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.IUiUpdateEntityListener;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.IUiUpdateListListener;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.SugarCreateUiUpdateEntityListener;
import com.bloodsugarlevel.androidbloodsugarlevel.input.DatePickerFragment;
import com.bloodsugarlevel.androidbloodsugarlevel.input.DateTimeListener;
import com.bloodsugarlevel.androidbloodsugarlevel.input.TimePickerFragment;

import static com.bloodsugarlevel.androidbloodsugarlevel.tabfragment.GraphFragment.FRAPH_SUGAR_VOLEY_TAG;

public class SugarInputFragment extends Fragment {
    public static final String SUGAR_CREATE_VOLEY_TAG = "SUGAR_CREATE_VOLEY_TAG";

    static RequestQueue mRequestQueue;
    IUiUpdateEntityListener mSugarCreateListenerImpl;
    DateTimeListener mDateTime;
    EditText mEditText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sugar_input_fragment, container, false);
        Button timeButton = view.findViewById(R.id.timeButton);
        Button dateButton = view.findViewById(R.id.dateButton);
        mSugarCreateListenerImpl = new SugarCreateUiUpdateEntityListener();
        mRequestQueue = Volley.newRequestQueue(getContext());
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
                setSugarListRequestListener();
            }
        });

        mEditText = view.findViewById(R.id.sugarLevelEditText);
        return view;
    }

    private void setSugarListRequestListener() {
        Float level = getValidateAndGetFloat(mEditText.getText().toString());

        SugarCreateDto dto = new SugarCreateDto(mDateTime.getCalendar().getTime(), level);
        JsonObjectRequest jsonObjectRequest = HttpRequestFactory.createSugarRequest(getContext(),
                mSugarCreateListenerImpl,
                dto,
                SUGAR_CREATE_VOLEY_TAG);
        mRequestQueue.add(jsonObjectRequest);
    }

    private Float getValidateAndGetFloat(String s) {
        return Float.parseFloat(s);
    }

    @Override
    public void onStop () {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(SUGAR_CREATE_VOLEY_TAG);
        }
    }
}
