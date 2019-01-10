package com.bloodsugarlevel.androidbloodsugarlevel.dto;

import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.User;

import java.util.Date;

public class InsulinDto {
    protected Long  id;
    protected Integer dose;
    protected InsulineType type;
    protected String name;
    protected User patient;
    protected Date datetime = new Date();
}
