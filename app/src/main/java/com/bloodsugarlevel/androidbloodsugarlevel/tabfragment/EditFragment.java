package com.bloodsugarlevel.androidbloodsugarlevel.tabfragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bloodsugarlevel.MyApplication;
import com.bloodsugarlevel.androidbloodsugarlevel.R;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarDto;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.EditSugarListenerImpl;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.request.HttpRequestFactory;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.IUiUpdateEntityListener;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.IUiUpdateListListener;
import com.bloodsugarlevel.androidbloodsugarlevel.input.DatePickerFragment;
import com.bloodsugarlevel.androidbloodsugarlevel.input.DateTimeListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.bloodsugarlevel.androidbloodsugarlevel.tabfragment.GraphFragment.FRAPH_SUGAR_VOLEY_TAG;


public class EditFragment extends Fragment implements Button.OnClickListener{
    public static final String EDIT_SUGAR_VOLEY_TAG = "EDIT_SUGAR_VOLEY_TAG";

    private static final long MIN_CLICK_INTERVAL=600;
    private long mLastClickTime = SystemClock.uptimeMillis();


    private LinearLayoutManager lLayout;
    RequestQueue mRequestQueue;
    IUiUpdateListListener mEditSugarListenerImpl;
    View mFragmentView;
    Button beginDateButton;
    Button endDateButton;

    DateTimeListener mBeginDate;
    DateTimeListener mEndDate;

    RecyclerViewAdapter mRecyclerViewAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.edit_fragment, container, false);
        mRequestQueue = MyApplication.getInstance().getRequestQueue();


        initBeginDateButton(mFragmentView);
        initEndDateButton(mFragmentView);

        List<SugarDto> rowListItem = getAllItemList();
        lLayout = new LinearLayoutManager(getContext());

        RecyclerView recyclerView = (RecyclerView) mFragmentView.findViewById(R.id.sugarRecyclerView);
        recyclerView.setLayoutManager(lLayout);
        mRecyclerViewAdapter = new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(mRecyclerViewAdapter);

        mEditSugarListenerImpl = new EditSugarListenerImpl(mRecyclerViewAdapter, recyclerView);


        setSugarListRequestListener();
        Button button = mFragmentView.findViewById(R.id.buttonEditRequest);
        button.setOnClickListener(this);

        Button buttonDelete = mFragmentView.findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObjectRequest jsonObjectRequest = HttpRequestFactory.deleteSugarListRequest(getContext(),
                        new IUiUpdateEntityListener<Object>(){
                            @Override
                            public void onResponse(Object object) {
                                setSugarListRequestListener();
                            }
                        },
                        mRecyclerViewAdapter.getCheckedIdList(),
                        EDIT_SUGAR_VOLEY_TAG, MyApplication.getInstance().getSessionCookies());
                mRequestQueue.add(jsonObjectRequest);
            }
        });
        return mFragmentView;
    }

    private void initBeginDateButton(View mFragmentView) {
        beginDateButton = mFragmentView.findViewById(R.id.beginEditDateButton);
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
        endDateButton = mFragmentView.findViewById(R.id.endEditDateButton);
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
    private List<SugarDto> getAllItemList() {
        List<SugarDto> allItems = new ArrayList<SugarDto>();
        SugarDto sugar = new SugarDto();
        sugar.id = 5l;
        sugar.level = 5.6f;
        sugar.date = new Date();
        allItems.add(sugar);
        return allItems;
    }

    private void setSugarListRequestListener() {
        JsonObjectRequest jsonObjectRequest = HttpRequestFactory.createSugarListRequest(getContext(),
                mEditSugarListenerImpl,
                mBeginDate.getCalendar().getTime(),
                mEndDate.getCalendar().getTime(),
                EDIT_SUGAR_VOLEY_TAG, MyApplication.getInstance().getSessionCookies());
        mRequestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View view) {
        long currentClickTime= SystemClock.uptimeMillis();
        long elapsedTime=currentClickTime-mLastClickTime;
        mLastClickTime=currentClickTime;

        if(elapsedTime<=MIN_CLICK_INTERVAL)
            return;
        setSugarListRequestListener();
    }

    @Override
    public void onStop () {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(FRAPH_SUGAR_VOLEY_TAG);
        }
    }
}
