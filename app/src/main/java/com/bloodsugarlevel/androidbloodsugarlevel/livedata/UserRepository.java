package com.bloodsugarlevel.androidbloodsugarlevel.livedata;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.bloodsugarlevel.MyApplication;
import com.bloodsugarlevel.androidbloodsugarlevel.room.dao.UserDao;
import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.User;

import java.util.List;

public class UserRepository {
    private MutableLiveData<User> userModel;
    public UserDao mUserDao;

    public UserRepository() {
        this.mUserDao = MyApplication.getInstance().getDatabase().userDao();
    }

    public LiveData<User> getUserModel() {
        return userModel;
    }

    public void insert(String login, String password) {
        User user = new User(login, password);
        new insertAsyncTask(mUserDao).execute(user);
    }

    public void getLoginUser(String login) {
        new getUserAsyncTask(mUserDao).execute(login);
    }

    public void setUserData(MutableLiveData<User> userData) {
        userModel = userData;
    }

    private class insertAsyncTask extends AsyncTask<User, Void, User> {

        private UserDao mAsyncTaskDao;

        insertAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected User doInBackground(final User... params) {
            Long[] list = mAsyncTaskDao.insertAll(params);
            List<User> listUser = mAsyncTaskDao.getListByIds(list);
            User user = listUser.get(0);
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            userModel.postValue(user);
        }
    }

    private class getUserAsyncTask extends AsyncTask<String, Void, User> {

        private UserDao mAsyncTaskDao;

        getUserAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected User doInBackground(final String... login) {
            User user =  mAsyncTaskDao.getLiveDataUserByLogin(login[0]);
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            userModel.postValue(user);
        }
    }
}
