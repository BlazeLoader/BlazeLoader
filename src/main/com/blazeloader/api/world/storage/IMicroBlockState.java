package com.blazeloader.api.world.storage;

import com.blazeloader.util.data.INBTWritable;
import com.blazeloader.util.data.NBTCollection.Item;

public interface IMicroBlockState extends INBTWritable, Item {
	
	int getProperty(String key);
	
	IMicroBlockState withProperty(String key, int value);
}
