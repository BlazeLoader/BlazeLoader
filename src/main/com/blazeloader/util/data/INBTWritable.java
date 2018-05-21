package com.blazeloader.util.data;

import net.minecraft.nbt.NBTTagCompound;

public interface INBTWritable extends INBTSerialisable<NBTTagCompound> {
	
	default NBTTagCompound writeToNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return tag;
	}
	
	/**
	 * @param tag	The tag to write to
	 */
	void writeToNBT(NBTTagCompound tag);
}
