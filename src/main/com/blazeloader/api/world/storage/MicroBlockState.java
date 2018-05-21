package com.blazeloader.api.world.storage;

import com.blazeloader.util.data.INBTSerialisable;

import net.minecraft.nbt.NBTTagCompound;

public class MicroBlockState implements IMicroBlockState {
	
	public static IMicroBlockState EMPTY = new IMicroBlockState() {
		
		public boolean isEmpty() {
			return true;
		}
		
		@Override
		public void writeToNBT(NBTTagCompound tag) { }

		@Override
		public INBTSerialisable<NBTTagCompound> readFromNBT(NBTTagCompound tag) {
			return new MicroBlockState().readFromNBT(tag);
		}

		@Override
		public int getProperty(String key) {
			return 0;
		}

		@Override
		public IMicroBlockState withProperty(String key, int value) {
			return new MicroBlockState(key, value);
		}
		
	};
	
	private NBTTagCompound properties;
	
	private MicroBlockState(String key, int value) {
		properties = new NBTTagCompound();
		properties.setInteger(key, value);
	}
	
	MicroBlockState() {
		
	}
	
	@Override
	public boolean isEmpty() {
		return properties.hasNoTags();
	}
	
	@Override
	public int getProperty(String key) {
		if (properties.hasKey(key)) return properties.getInteger(key);
		return 0;
	}
	
	@Override
	public MicroBlockState withProperty(String key, int value) {
		NBTTagCompound copy = properties.copy();
		copy.setInteger(key, value);
		return new MicroBlockState().readFromNBT(copy);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		properties.merge(tag);
	}
	
	@Override
	public MicroBlockState readFromNBT(NBTTagCompound tag) {
		properties = tag;
		return this;
	}
}
