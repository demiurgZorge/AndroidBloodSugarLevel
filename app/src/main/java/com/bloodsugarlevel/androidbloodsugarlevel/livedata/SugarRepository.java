package com.bloodsugarlevel.androidbloodsugarlevel.livedata;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.bloodsugarlevel.MyApplication;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarCreateDto;
import com.bloodsugarlevel.androidbloodsugarlevel.dto.SugarDto;
import com.bloodsugarlevel.androidbloodsugarlevel.room.dao.SugarDao;
import com.bloodsugarlevel.androidbloodsugarlevel.room.dao.UserDao;
import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.Sugar;
import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.User;

import java.util.Date;
import java.util.List;

public class SugarRepository {

    private MutableLiveData<Sugar> mSugarModel;
    private MutableLiveData<List<Sugar>> mSugarRangeModel;
    public SugarDao mSugarDao;
    public UserDao mUserDao;

    public SugarRepository() {
        this.mSugarDao = MyApplication.getInstance().getDatabase().sugarDao();
        this.mUserDao = MyApplication.getInstance().getDatabase().userDao();
    }

    public LiveData<Sugar> getSugarModel() {
        return mSugarModel;
    }

    public MutableLiveData<List<Sugar>> getSugarRangeModel() {
        return mSugarRangeModel;
    }

    public void setSugarData(MutableLiveData<Sugar> sugarModel) {
        this.mSugarModel = sugarModel;
    }

    public void setSugarRangeData(MutableLiveData<List<Sugar>> sugarRangeModel) {
        this.mSugarRangeModel = sugarRangeModel;
    }

    public void insert(SugarCreateDto sugarDto) {

        new InsertAsyncTask(mSugarDao).execute(sugarDto);
    }

    public void getRange(SugarRangeRequestDto sugarRangeRequestDto) {
        new GetRangeAsyncTask(mSugarDao).execute(sugarRangeRequestDto);
    }

    public void delete(SugarRangeRequestDto sugarRangeRequestDto) {
        new DeleteAsyncTask(mSugarDao).execute(sugarRangeRequestDto);
    }

    private class InsertAsyncTask extends AsyncTask<SugarCreateDto, Void, Sugar>{
        private SugarDao mAsyncTaskDao;

        public InsertAsyncTask(SugarDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Sugar doInBackground(final SugarCreateDto... params) {
            User user = mUserDao.getUserByLogin(params[0].userLogin);
            Sugar sugar = new Sugar(params[0].level, new Date(params[0].date), user.id);
            Long[] list = mAsyncTaskDao.insertAll(sugar);
            List<Sugar> listUser = mAsyncTaskDao.getListByIds(list);
            Sugar sugarResult = listUser.get(0);
            return sugarResult;
        }

        @Override
        protected void onPostExecute(Sugar user) {
            super.onPostExecute(user);
            mSugarModel.postValue(user);
        }
    }

    private class GetRangeAsyncTask extends AsyncTask<SugarRangeRequestDto, Void, List<Sugar>>{
        private SugarDao mAsyncTaskDao;

        public GetRangeAsyncTask(SugarDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<Sugar> doInBackground(final SugarRangeRequestDto... params) {
            SugarRangeRequestDto dto = params[0];
            User user = mUserDao.getUserByLogin(dto.userLogin);
            List<Sugar> list = mAsyncTaskDao.getRangeSugar(dto.dateMin, dto.dateMax, user.id);
            return list;
        }

        @Override
        protected void onPostExecute(List<Sugar> list) {
            super.onPostExecute(list);
            mSugarRangeModel.postValue(list);
        }
    }

    private class DeleteAsyncTask extends AsyncTask<SugarRangeRequestDto, Void, List<Sugar>>{
        private SugarDao mAsyncTaskDao;

        public DeleteAsyncTask(SugarDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<Sugar> doInBackground(final SugarRangeRequestDto... params) {
            List<Sugar> list = params[0].listForDelete;
            Sugar[] arraySugar = list.toArray(new Sugar[list.size()]);
            mAsyncTaskDao.delete(arraySugar);
            SugarRangeRequestDto dto = params[0];
            User user = mUserDao.getUserByLogin(dto.userLogin);
            List<Sugar> actualRangeSugarList = mAsyncTaskDao.getRangeSugar(dto.dateMin, dto.dateMax, user.id);
            return actualRangeSugarList;
        }

        @Override
        protected void onPostExecute(List<Sugar> list) {
            super.onPostExecute(list);
            mSugarRangeModel.postValue(list);
        }
    }
}
