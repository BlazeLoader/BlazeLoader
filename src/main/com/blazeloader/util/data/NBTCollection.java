package com.blazeloader.util.data;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagList;

public abstract class NBTCollection<T extends NBTCollection.Item> extends ArrayList<T> implements INBTSerialisable<NBTTagList> {
	private static final long serialVersionUID = -5124037615058187777L;
	
	@Override
	public NBTTagList writeToNBT() {
		NBTTagList list = new NBTTagList();
		for (T el : this) {
			if (!el.isEmpty()) list.appendTag(el.writeToNBT());
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public NBTCollection<T> readFromNBT(NBTTagList tag) {
		for (int i = 0; i < tag.tagCount(); i++) {
			add((T)createElement().readFromNBT(tag.getCompoundTagAt(i)));
		}
		return this;
	}
	
	public abstract T createElement();
	
	public static interface Item extends INBTWritable {
		public boolean isEmpty();
	}
}
