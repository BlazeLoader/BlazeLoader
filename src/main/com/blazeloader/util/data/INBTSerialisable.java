package com.blazeloader.util.data;

import net.minecraft.nbt.NBTBase;

public interface INBTSerialisable<T extends NBTBase> {
	
	/**
	 * Writes this serialisable to a new NBTTag
	 */
	T writeToNBT();
	
	/**
	 * 
	 * @param tag	The tag to read from
	 */
	INBTSerialisable<T> readFromNBT(T tag);
}
