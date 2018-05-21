package com.blazeloader.api.privileged;

import com.blazeloader.util.time.WorldClock;

import net.minecraft.util.math.BlockPos;

public interface IWorld {
	/**
	 * Checks if a given block coordinate is within the world's bounds.
	 */
	public boolean isCoordValid(BlockPos pos);
	
	/**
	 * Checks if the given coordinate is above the current world height.
	 */
	boolean isOutsideBuildHeight(BlockPos pos);
	
	public WorldClock getWorldClock();
}
