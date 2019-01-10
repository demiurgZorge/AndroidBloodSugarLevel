package com.bloodsugarlevel.androidbloodsugarlevel.livedata;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarCreateDto;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarDto;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.UserDto;
import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.Sugar;
import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.User;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    UserRepository mUserRepository;
    SugarRepository mSugarRepository;

    public UserViewModel(Application application) {
        super(application);
        mUserRepository = new UserRepository();
        mSugarRepository = new SugarRepository();
    }

    public void setLogin(String login) {
        mUserRepository.getLoginUser(login);
    }

    public LiveData<User> getUser() {
        return mUserRepository.getUserModel();
    }

    public void tryCreateUser(String login, String password) {
        mUserRepository.insert(login, password);
    }

    public void setUserData(MutableLiveData<User> userData) {
        mUserRepository.setUserData(userData);
    }
    public void setSugarData(MutableLiveData<Sugar> sugarModel){
        mSugarRepository.setSugarData(sugarModel);
    }
    public void setSugarRangeData(MutableLiveData<List<Sugar>> sugarRangeModel){
        mSugarRepository.setSugarRangeData(sugarRangeModel);
    }

    public LiveData<Sugar> getSugarData(){
        return mSugarRepository.getSugarModel();
    }

    public MutableLiveData<List<Sugar>> getSugarRangeModel(){
        return mSugarRepository.getSugarRangeModel();
    }

    public void insert(SugarCreateDto sugarDto){
        mSugarRepository.insert(sugarDto);
    }

    public void getRange(SugarRangeRequestDto sugarRangeRequestDto){
        mSugarRepository.getRange(sugarRangeRequestDto);
    }

    public void delete(SugarRangeRequestDto sugarRangeRequestDto){
        mSugarRepository.delete(sugarRangeRequestDto);
    }
}
