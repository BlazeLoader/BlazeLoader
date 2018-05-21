package com.blazeloader.bl.interop;

import net.minecraft.world.DimensionType;

public interface ForgeDimensionType {
	
	public boolean shouldLoadSpawn();
	
	public DimensionType setLoadSpawn(boolean keepLoaded);
}
