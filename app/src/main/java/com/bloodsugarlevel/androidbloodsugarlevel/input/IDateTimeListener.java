package com.bloodsugarlevel.androidbloodsugarlevel.input;

public interface IDateTimeListener {
    void onTimeSet(int hourOfDay, int minute);
    void onDateSet(int year, int month, int day);
}
