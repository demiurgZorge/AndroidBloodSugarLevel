package com.bloodsugarlevel.androidbloodsugarlevel.tabfragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bloodsugarlevel.androidbloodsugarlevel.R;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.GraphListenerImpl;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.HttpRequestFactory;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.IUiUpdateListener;
import com.bloodsugarlevel.androidbloodsugarlevel.input.DatePickerFragment;
import com.bloodsugarlevel.androidbloodsugarlevel.input.DateTimeListener;
import com.jjoe64.graphview.GraphView;


public class GraphFragment extends Fragment implements Button.OnClickListener{
    public static final String FRAPH_SUGAR_VOLEY_TAG = "FRAPH_SUGAR_VOLEY_TAG";
    private static final long MIN_CLICK_INTERVAL=600;
    private long mLastClickTime = SystemClock.uptimeMillis();
    View mFragmentView;
    static RequestQueue mRequestQueue;
    IUiUpdateListener mGraphListenerImpl;
    Button beginDateButton;
    Button endDateButton;

    DateTimeListener mBeginDate;
    DateTimeListener mEndDate;

    @Override
    public void onClick(View view1) {
        long currentClickTime= SystemClock.uptimeMillis();
        long elapsedTime=currentClickTime-mLastClickTime;
        mLastClickTime=currentClickTime;

        if(elapsedTime<=MIN_CLICK_INTERVAL)
            return;
        setSugarListRequestListener();
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.graph_fragment, container, false);
        final GraphView graph = (GraphView) mFragmentView.findViewById(R.id.shugarGraph);
        mGraphListenerImpl = new GraphListenerImpl(graph);
        mRequestQueue = Volley.newRequestQueue(getContext());
        Button button = mFragmentView.findViewById(R.id.buttonGraphicLevel);
        button.setOnClickListener(this);

        initBeginDateButton(mFragmentView);
        initEndDateButton(mFragmentView);
        return mFragmentView;
    }
    private void initBeginDateButton(View mFragmentView) {
        beginDateButton = mFragmentView.findViewById(R.id.beginDateButton);
        mBeginDate = new DateTimeListener(beginDateButton,null);
        mBeginDate.setDefaultStart();
        beginDateButton.setText(mBeginDate.getYearMonthDayString());
        beginDateButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setDateTimeListener(mBeginDate);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }
    private void initEndDateButton(View mFragmentView) {
        endDateButton = mFragmentView.findViewById(R.id.endDateButton);
        mEndDate = new DateTimeListener(endDateButton,null);
        endDateButton.setText(mEndDate.getYearMonthDayString());
        endDateButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setDateTimeListener(mEndDate);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }

    private void setSugarListRequestListener() {
        JsonObjectRequest jsonObjectRequest = HttpRequestFactory.create(getContext(),
                mGraphListenerImpl, mBeginDate.getCalendar().getTime(),
                mEndDate.getCalendar().getTime(),
                FRAPH_SUGAR_VOLEY_TAG);
        mRequestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onStop () {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(FRAPH_SUGAR_VOLEY_TAG);
        }
    }
}