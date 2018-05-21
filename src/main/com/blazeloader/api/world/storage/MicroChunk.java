package com.blazeloader.api.world.storage;

import com.blazeloader.util.data.INBTSerialisable;
import com.blazeloader.util.data.INBTWritable;
import com.blazeloader.util.data.NBTCollection;
import com.blazeloader.util.data.NBTType;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

public class MicroChunk implements INBTWritable {
	
	private boolean dirty = false;
	
	public final int x;
	public final int z;
	
	private MicroBlockStorage[] storage;
	
	public MicroChunk(int x, int z, int storageSize) {
		storage = new MicroBlockStorage[storageSize];
		this.x = x;
		this.z = z;
	}
	
	public IMicroBlockState getState(BlockPos pos) {
		MicroBlockStorage layer = storage[pos.getY() >> 4];
		if (layer == null) return ((IMicroBlock)Blocks.AIR).getDefaultMicroState();
		return layer.getState(pos);
	}
	
	public boolean dirty() {
		return dirty;
	}
	
	public void setState(IMicroBlockState state, BlockPos pos) {
		int y = pos.getY() >> 4;
		MicroBlockStorage layer = storage[y];
		if (layer == null) {
			layer = storage[y] = new MicroBlockStorage();
		}
		layer.setState(state, pos);
		dirty = true;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		NBTTagList list = new NBTTagList();
		tagCompound.setTag("storage", list);
		for (int i = 0; i < storage.length; i++) {
			list.appendTag(storage[i].writeToNBT());
		}
	}

	@Override
	public MicroChunk readFromNBT(NBTTagCompound tagCompound) {
		NBTTagList list = tagCompound.getTagList("storage", NBTType.INT_ARRAY.value());
		
		for (int i = 0; i < list.tagCount() && i < storage.length; i++) {
			storage[i] = (new MicroBlockStorage()).readFromNBT((NBTTagList)list.get(i));
		}
		
		return this;
	}
	
	private class MicroBlockStorage implements INBTSerialisable<NBTTagList> {
		private final NBTCollection<IMicroBlockState> data = new NBTCollection<IMicroBlockState>() {
			private static final long serialVersionUID = 5823435075920315511L;
			
			@Override
			public IMicroBlockState createElement() {
				return new MicroBlockState();
			}
			
		};
		
		protected MicroBlockStorage() {
			
		}
		
		protected int getIndex(BlockPos pos) {
	        return (pos.getY() & 15) << 8 | (pos.getZ() & 15) << 4 | (pos.getX() & 15);
	    }
		
		public IMicroBlockState getState(BlockPos pos) {
			int index = getIndex(pos);
			if (data.get(index) == null) return ((IMicroBlock)Blocks.AIR).getDefaultMicroState();
			return data.get(index);
		}
		
		public void setState(IMicroBlockState state, BlockPos pos) {
			int index = getIndex(pos);
			data.set(index, state);
		}
		
		@Override
		public NBTTagList writeToNBT() {
			return data.writeToNBT();
		}
		
		@Override
		public MicroBlockStorage readFromNBT(NBTTagList tagCompound) {
			return this;
		}
		
	}
}
