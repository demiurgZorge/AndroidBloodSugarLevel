package com.bloodsugarlevel.androidbloodsugarlevel.room.entities;

import com.bloodsugarlevel.androidbloodsugarlevel.dto.InsulineType;

import java.util.Date;

public class Insulin {
    protected Long  id;
    protected Integer dose;
    protected InsulineType type;
    protected String name;
    protected User patient;
    protected Date  datetime = new Date();
}
