package com.blazeloader.util.time;

public interface IDate {
	public String getDate();
	
	public int getDays();
	
	public int getWeeks();
	
	public int getMonths();
	
	public int getYears();
	
	public int getDay();
	
	public String getEra();
	
	public default Day getDayOfWeek() {
		return Day.values()[getDays() % 7];
	}
	
	public default Month getMonth() {
		return Month.values()[getMonths() % 12];
	}
	
	public boolean isLeap();
}
