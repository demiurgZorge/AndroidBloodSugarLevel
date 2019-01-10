package com.bloodsugarlevel.androidbloodsugarlevel.room.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.bloodsugarlevel.androidbloodsugarlevel.dto.UserDto;

import java.util.Date;
@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "patient_id"),
        indices = {@Index("patient_id")})
public class Sugar {

    @PrimaryKey
    public Long id;

    @ColumnInfo(name = "level")
    public Float level;

    @ColumnInfo(name = "date")
    public Date date;

    @ColumnInfo(name = "patient_id")
    public Long patientId;

    public Sugar(){}

    @Ignore
    public Sugar(Float level, Date date, Long patientId){
        this.date = date;
        this.level = level;
        this.patientId = patientId;
    }
}
