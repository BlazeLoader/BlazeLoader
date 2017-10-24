package com.blazeloader.api.client.tick;

import net.minecraft.util.Timer;

public interface ITimer {
	/**
	 * Gets the system time of the last moment this timer was updated.
	 */
	public abstract long getSystemTime();
	
	/**
	 * Gets the total ticks elapsed since this timer was first created.
	 */
	public int getElapsedTicks();
	
	/**
	 * Gets the time since the last full tick
	 * @return partial ticks in the range 0. to 1.
	 */
	public float getPartialTicks();
	
	/**
	 * Gets the wait duration between ticks in milliseconds
	 */
	public float getTickLength();
	
	/**
	 * Gets the tick rate per second (1000/{@link getTickLength})
	 * @return
	 */
	public float getTicksPerSecond();
	
	/**
	 * Sets the tick rate of this timer
	 * @param tps	The new tick rate to set in ticks per second
	 */
	public void setTicksPerSecond(float tps);
	
	/**
	 * Gets the underlying timer instance (this)
	 * 
	 * Shorthand for {@code (Timer)(Object)itimer}
	 */
	public Timer getRawType();
}
