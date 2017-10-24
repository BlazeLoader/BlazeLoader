package com.blazeloader.api.world.gen;

import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

public abstract class WorldSavedDataCollection extends WorldSavedData {
	public WorldSavedDataCollection(String name) {
		super(name);
	}
	
	/**
	 * In the case of multiple items initialises each one.
	 * <p>
	 * Used in VillagerCollection and made available to mod added WorldSaveData.
	 * 
	 * @param w	The world this collection is being initialised to.
	 */
	public abstract void setWorldsForAll(World w);
}
