package com.bloodsugarlevel.androidbloodsugarlevel.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonDateTime  {
	public static final SimpleDateFormat DATE_FORMAT_yyyyMM = new SimpleDateFormat("yyyyMM");
	public static final SimpleDateFormat DATE_FORMAT_yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final SimpleDateFormat DATE_FORMAT_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat DATE_FORMAT_hhmmss = new SimpleDateFormat("HHmmss");
    public static final SimpleDateFormat DATE_yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
	private Integer date;
	private Integer time;	
	private Long isoDate = new Date().getTime();
	
	private void init(Date d) {
		date = convertDateyyyyMMdd(d);
		time = convertDateHHmmss(d);
		isoDate = d.getTime();
	}
	
	public JsonDateTime() {
		init(new Date());
	}
	
	public JsonDateTime(Date date) {
		init(date);
	}
	
	public Integer getDate() {
		return date;
	}
	public void setDate(Integer date) {
		
		this.date = date;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}

	public Long getIsoDate() {
		return isoDate;
	}

	public void setIsoDate(Long isoDate) {
		this.isoDate = isoDate;
	}
	
	public static JsonDateTime createDate(Date date){
		return new JsonDateTime(date);
	}
	
	public static Integer convertDateyyyyMM(Date date){
		return Integer.valueOf(JsonDateTime.DATE_FORMAT_yyyyMM.format(date));
	}
	public static Integer convertDateyyyyMMdd(Date date){
		SimpleDateFormat formatter = JsonDateTime.DATE_FORMAT_yyyyMMdd;
		return Integer.valueOf(formatter.format(date));
	}
	
	public static Integer convertDateHHmmss(Date date){
		SimpleDateFormat formatter = JsonDateTime.DATE_FORMAT_hhmmss;
		return Integer.valueOf(formatter.format(date));
	}
	
	public static String convertDateyyyyMMddHHmmss(Date date){
		SimpleDateFormat formatter = JsonDateTime.DATE_FORMAT_yyyyMMddHHmmss;
		return formatter.format(date);
	}
}
