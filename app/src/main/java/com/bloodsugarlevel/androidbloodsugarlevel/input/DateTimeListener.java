package com.bloodsugarlevel.androidbloodsugarlevel.input;

import android.text.format.DateFormat;
import android.widget.Button;

import java.util.Calendar;
import java.util.Date;

public class DateTimeListener implements IDateTimeListener {

    public static final String TIME_PICKER_FORMAT = "hh:mm:ss";
    public static final String DATE_PICKER_FORMAT = "yyyy-MM-dd";

    public int hourOfDay;
    public int minute;
    public int year;
    public int month;
    public int day;

    public Button timeButton;
    public Button dateButton;

    public DateTimeListener() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
    }

    public DateTimeListener(Button dateButton, Button timeButton) {
        this();
        this.dateButton = dateButton;
        this.timeButton = timeButton;
    }

    @Override
    public void onTimeSet(int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        if (timeButton != null) {
            timeButton.setText(getHourMinuteString());
        }
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        if (dateButton != null) {
            dateButton.setText(getYearMonthDayString());
        }
    }


    @Override
    public Calendar getCalendar() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    public CharSequence getYearMonthDayString() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        Date date = calendar.getTime();
        return DateFormat.format(DATE_PICKER_FORMAT, date);
    }

    public CharSequence getHourMinuteString() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        return DateFormat.format(TIME_PICKER_FORMAT, date);
    }

    public void setDefaultStart() {
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
    }
}
