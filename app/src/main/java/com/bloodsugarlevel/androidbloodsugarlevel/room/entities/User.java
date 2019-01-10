package com.bloodsugarlevel.androidbloodsugarlevel.room.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = {@Index(value = {"login"}, unique = true)})
public class User {
    @PrimaryKey
    public Long   id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "login")
    public String login;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "authToken")
    public String authToken;

    @ColumnInfo(name = "active")
    public boolean active;

    @ColumnInfo(name = "ip")
    public String ip;

    public User(){}

    @Ignore
    public User(String login, String password){
        this.login = login;
        this.password = password;
        this.name = login;
    }
}
