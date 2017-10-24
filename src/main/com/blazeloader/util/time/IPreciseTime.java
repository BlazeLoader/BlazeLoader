package com.blazeloader.util.time;

public interface IPreciseTime extends ITime {
	public String getTimePrecise();
	
	public int getMilliseconds();
	
	public long getTotalMilliseconds();
}
