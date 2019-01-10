package com.bloodsugarlevel.androidbloodsugarlevel.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    LiveData<User> getAll();

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    LiveData<List<User>> loadAllByIds(Long[] userIds);

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    List<User> getListByIds(Long[] userIds);

    @Insert
    Long[] insertAll(User... users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM user WHERE login = (:login)")
    User getUserByLogin(String login);

    @Query("SELECT * FROM user WHERE login = (:login)")
    User getLiveDataUserByLogin(String login);
}
