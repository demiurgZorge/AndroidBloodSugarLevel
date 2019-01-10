package com.bloodsugarlevel.androidbloodsugarlevel.tabfragment;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bloodsugarlevel.MyApplication;
import com.bloodsugarlevel.androidbloodsugarlevel.R;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarDto;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.GraphListenerImpl;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.request.HttpRequestFactory;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.IUiUpdateListListener;
import com.bloodsugarlevel.androidbloodsugarlevel.input.DatePickerFragment;
import com.bloodsugarlevel.androidbloodsugarlevel.input.DateTimeListener;
import com.bloodsugarlevel.androidbloodsugarlevel.livedata.SugarRangeRequestDto;
import com.bloodsugarlevel.androidbloodsugarlevel.livedata.UserViewModel;
import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.Sugar;
import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.User;
import com.github.mikephil.charting.charts.LineChart;
import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;
import java.util.List;


public class GraphFragment extends Fragment implements Button.OnClickListener{
    public static final String FRAPH_SUGAR_VOLEY_TAG = "FRAPH_SUGAR_VOLEY_TAG";
    private static final long MIN_CLICK_INTERVAL=600;
    private long mLastClickTime = SystemClock.uptimeMillis();
    View mFragmentView;
    RequestQueue mRequestQueue;
    IUiUpdateListListener mGraphListenerImpl;
    Button beginDateButton;
    Button endDateButton;

    DateTimeListener mBeginDate;
    DateTimeListener mEndDate;

    private UserViewModel userViewModel;
    Observer<List<Sugar>> mSugarObserver;

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
        LineChart graph = (LineChart) mFragmentView.findViewById(R.id.shugarGraph);
        mGraphListenerImpl = new GraphListenerImpl(graph);
        mRequestQueue = MyApplication.getInstance().getRequestQueue();
        Button button = mFragmentView.findViewById(R.id.buttonGraphicLevel);
        button.setOnClickListener(this);

        initBeginDateButton(mFragmentView);
        initEndDateButton(mFragmentView);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        mSugarObserver = new Observer<List<Sugar>> () {
            @Override
            public void onChanged(@Nullable List<Sugar> sugarList) {
                if (sugarList != null) {
                    List<SugarDto> listDto = SugarDto.createList(sugarList);
                    mGraphListenerImpl.onResponse(listDto);
                } else {
                }
                return;
            }
        };
        return mFragmentView;
    }
    private void initBeginDateButton(View mFragmentView) {
        beginDateButton = mFragmentView.findViewById(R.id.beginDateButton);
        mBeginDate = new DateTimeListener(beginDateButton,null);
        int startShift = MyApplication.getInstance().getDefaultShiftStartDays();
        mBeginDate.setDefaultStart(startShift);
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
        int endShift = MyApplication.getInstance().getDefaultShiftEndDays();
        mEndDate.setDefaultEnd(endShift);
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
        if (MyApplication.getInstance().isInternetAllow()) {
            JsonObjectRequest jsonObjectRequest = HttpRequestFactory.createSugarListRequest(getContext(),
                    mGraphListenerImpl,
                    mBeginDate.getCalendar().getTime(),
                    mEndDate.getCalendar().getTime(),
                    FRAPH_SUGAR_VOLEY_TAG, MyApplication.getInstance().getSessionCookies());
            mRequestQueue.add(jsonObjectRequest);
        } else {
            String login = MyApplication.getInstance().getLoggedUserName();
            MutableLiveData<List<Sugar>> sugarModelData = new MutableLiveData<List<Sugar>>();
            sugarModelData.removeObservers(this);
            sugarModelData.observe(this, mSugarObserver);
            userViewModel.setSugarRangeData(sugarModelData);
            SugarRangeRequestDto rangeDto = new SugarRangeRequestDto(mBeginDate.getCalendar().getTime(),
                    mEndDate.getCalendar().getTime(), login);
            userViewModel.getRange(rangeDto);
        }
    }


    @Override
    public void onStop () {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(FRAPH_SUGAR_VOLEY_TAG);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}