package com.bloodsugarlevel.androidbloodsugarlevel.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.bloodsugarlevel.androidbloodsugarlevel.room.dao.SugarDao;
import com.bloodsugarlevel.androidbloodsugarlevel.room.dao.UserDao;
import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.Sugar;
import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.User;

@Database(entities = {User.class, Sugar.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class BloodSugarLevelDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract SugarDao sugarDao();
}
