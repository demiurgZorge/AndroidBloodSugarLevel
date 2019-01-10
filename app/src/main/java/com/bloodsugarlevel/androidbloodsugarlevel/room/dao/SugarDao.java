package com.bloodsugarlevel.androidbloodsugarlevel.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.Sugar;
import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.User;

import java.util.Date;
import java.util.List;

@Dao
public interface SugarDao {
    @Query("SELECT * FROM sugar WHERE (date >= (:dateMin)) AND (date <= (:dateMax)) AND (patient_id = (:patientId)) ORDER BY date ASC")
    List<Sugar> getRangeSugar(Date dateMin, Date dateMax, Long patientId);

    @Insert
    Long[] insertAll(Sugar... sugars);

    @Delete
    void delete(Sugar... sugar);

    @Query("SELECT * FROM sugar WHERE id IN (:sugarIds)")
    List<Sugar> getListByIds(Long[] sugarIds);
}
