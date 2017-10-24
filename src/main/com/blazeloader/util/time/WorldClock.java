package com.blazeloader.util.time;

import net.minecraft.world.World;

/**
 * A clock bound to the current in-game time.
 * 
 */
public class WorldClock implements IPreciseTime, IDate {
	
	private World world;
	
	public WorldClock(World referenceFrame) {
		world = referenceFrame;
	}
	
	public String getDate() {
		return padded(getDay()) + "-" + padded(getMonth().ordinal() + 1) + "-" + padded(getYears());
	}
	
	public String getTime() {
		return padded(getHours()) + ":" + padded(getMinutes()) + ":" + padded(getSeconds());
	}
	
	protected String padded(int v) {
		return v < 10 ? "0" + v : v + "";
	}
	
	public int getDays() {
		return getExtendedWorldTime(1);
	}
	
	public int getWeeks() {
		return getExtendedWorldTime(7);
	}
	
	public int getMonths() {
		return getExtendedWorldTime(30);
	}
	
	public int getYears() {
		return getExtendedWorldTime(360) + 1;
	}
	
	public int getDay() {
		return getDays() - getMonths()*30;
	}
	
	/**
	 * Minecraft does not have leap years. :P
	 */
	public boolean isLeap() {
		return false;
	}
	
	/**
	 * AN = After Notch.
	 * <p>
	 * There is only one era.
	 */
	public String getEra() {
		return "AN";
	}
	
	public int getHours() {
		long time = getWorldTime() - 18000;
		if (time < 0) time += 24000;
		return (int)Math.floor(time / 1000) % 24;
	}
	
	public int getMinutes() {
		return (int)Math.floor(getWorldTime() * 60 / 1000) % 60;
	}
	
	public int getSeconds() {
		return (int)Math.floor(getWorldTime() * 60 * 60 / 1000) % 60;
	}
	
	public String getTimePrecise() {
		return getTime() + ":" + padded(getMilliseconds());
	}
	
	public int getMilliseconds() {
		return (int)Math.floor(getWorldTime() * 60 * 60 / 10) % 100 + (int)(getWorldTime() % 10);
	}
	
	private long getWorldTime() {
		long time = world.getWorldTime();
		if (time == 1) time = 0;
		return time;
	}
	
	private int getExtendedWorldTime(long dayFactor) {
		long time = getWorldTime();
		long timeOfDay = time % 24000;
		return (int)Math.floor((time - timeOfDay) / (24000 * dayFactor));
	}
	
	public String toString() {
		return getDate() + " " + getTimePrecise();
	}
	
	public long getTotalMilliseconds() {
		return (long)Math.floor(getWorldTime() / 100);
	}
}
