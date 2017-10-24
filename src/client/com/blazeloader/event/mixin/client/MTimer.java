package com.blazeloader.event.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.blazeloader.api.client.tick.ITimer;

import net.minecraft.util.Timer;

@Mixin(Timer.class)
public abstract class MTimer implements ITimer {
	
	@Accessor("elapsedTicks")
	public abstract int getElapsedTicks();
	
	@Accessor("renderPartialTick")
	public abstract float getPartialTicks();
	
	@Accessor("lastSyncSysClock")
	public abstract long getSystemTime();
	
	@Shadow
	private float tickLength;
	
	public float getTickLength() {
		return tickLength;
	}
	
	public float getTicksPerSecond() {
		return 1000/tickLength;
	}
	
	public void setTicksPerSecond(float tps) {
		tickLength = 1000/tps;
	}
	
	public Timer getRawType() {
		return (Timer)(Object)this;
	}
}
