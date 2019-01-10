package com.bloodsugarlevel.androidbloodsugarlevel.tabfragment;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bloodsugarlevel.MyApplication;
import com.bloodsugarlevel.androidbloodsugarlevel.LoginActivity;
import com.bloodsugarlevel.androidbloodsugarlevel.R;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarCreateDto;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.UserDto;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.request.HttpRequestFactory;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.IUiUpdateEntityListener;
import com.bloodsugarlevel.androidbloodsugarlevel.httpClient.SugarCreateUiUpdateEntityListener;
import com.bloodsugarlevel.androidbloodsugarlevel.input.DatePickerFragment;
import com.bloodsugarlevel.androidbloodsugarlevel.input.DateTimeListener;
import com.bloodsugarlevel.androidbloodsugarlevel.input.TimePickerFragment;
import com.bloodsugarlevel.androidbloodsugarlevel.livedata.SugarRangeRequestDto;
import com.bloodsugarlevel.androidbloodsugarlevel.livedata.UserViewModel;
import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.Sugar;
import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.User;

public class SugarInputFragment extends Fragment {
    public static final String SUGAR_CREATE_VOLEY_TAG = "SUGAR_CREATE_VOLEY_TAG";
    public static final Float WRONG_RESULT = -1f;

    IUiUpdateEntityListener mSugarCreateListenerImpl;
    DateTimeListener mDateTime;
    EditText mEditText;
    private RequestQueue mRequestQueue;

    private UserViewModel userViewModel;
    Observer<Sugar> mSugarObserver;


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
                hideInputMethod();
            }
        });

        mEditText = view.findViewById(R.id.sugarLevelEditText);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        mSugarObserver = new Observer<Sugar> () {

            @Override
            public void onChanged(@Nullable Sugar sugar) {
                if(sugar != null) {
                    AlertDialogManager.showAlertDialog(getContext(),"Succes", "Sugar Level " + sugar.level + " accepted in inner DB");
                }else{
                    AlertDialogManager.showAlertDialog(getContext(),"Error", "Cant write into inner DB");
                }
                return;
            }
        };

        return view;
    }

    private void createSugarRequestListener() {
        Float level = getValidateAndGetFloat(mEditText.getText());
        if(level == WRONG_RESULT){
            return;
        }
        SugarCreateDto dto = new SugarCreateDto(mDateTime.getCalendar().getTime(), level);
        Context ctx = getContext();

        if(MyApplication.getInstance().isInternetAllow()) {
            JsonObjectRequest jsonObjectRequest = HttpRequestFactory.createSugarRequest(ctx,
                    mSugarCreateListenerImpl,
                    dto,
                    SUGAR_CREATE_VOLEY_TAG,
                    MyApplication.getInstance().getSessionCookies());
            mRequestQueue.add(jsonObjectRequest);
        }else{
            saveSugareOffline(dto);
        }
    }

    private void saveSugareOffline(SugarCreateDto dto) {
        String login = MyApplication.getInstance().getLoggedUserName();
        dto.userLogin = login;
        MutableLiveData<Sugar> sugarModelData = new MutableLiveData<Sugar>();
        sugarModelData.removeObservers(this);
        sugarModelData.observe(this, mSugarObserver);
        userViewModel.setSugarData(sugarModelData);
        userViewModel.insert(dto);
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

        hideInputMethod();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideInputMethod();
    }

    public void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}
