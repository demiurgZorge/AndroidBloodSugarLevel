package com.bloodsugarlevel.androidbloodsugarlevel.input;

import java.util.Calendar;
import java.util.Date;

public interface IDateTimeListener {
    void onTimeSet(int hourOfDay, int minute);
    void onDateSet(int year, int month, int day);
    Calendar getCalendar();
}
