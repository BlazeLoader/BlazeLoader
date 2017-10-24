package com.blazeloader.util.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.blazeloader.api.block.ApiBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Collection of block data. Can also be read from or written to nbt.
 */
public class BlockStore extends ArrayList<BlockStore.Entry> implements INBTWritable {
	private static final long serialVersionUID = -4495071139322159361L;
	
	private final HashMap<BlockPos, BlockStore.Entry> map;
	
	public BlockStore() {
		super();
		map = new HashMap<BlockPos, Entry>();
	}
	
	public BlockStore(Collection<? extends Entry> c) {
		super(c);
		map = new HashMap<BlockPos, Entry>();
		for (Entry i : c) {
			map.put(i.position, i);
		}
	}
	
	public BlockStore(int initialCapacity) {
		super(initialCapacity);
		map = new HashMap<BlockPos, Entry>(initialCapacity);
	}
	
	/**
	 * Checks if an entry exists for the given position.
	 * @param pos	The position to check
	 * @return true if an entry exists, false otherwise.
	 */
	public boolean isDefinedFor(BlockPos pos) {
		return map.containsKey(pos);
	}
	
	/**
	 * Checks if an entry exists for the given position.
	 * @param x		X-coordinate
	 * @param y		Y-coordinate
	 * @param z		Z-coordinate
	 * @return true if an entry exists, false otherwise.
	 */
	public boolean isDefinedFor(int x, int y, int z) {
		return isDefinedFor(new BlockPos(x, y, z));
	}
	
	/**
	 * Stores the block information for a specific position in the world.
	 * @param pos	The position
	 */
	public void define(World w, BlockPos pos) {
		if (isDefinedFor(pos)) {
			Entry neu = new Entry(w, pos);
			Entry old = map.put(pos, neu);
			if (old != null) {
				set(indexOf(old), neu);
			}
		} else {
			add(new Entry(w, pos));
		}
	}
	
	/**
	 * Stores the block information for a specific position in the world.
	 * @param x		X-coordinate
	 * @param y		Y-coordinate
	 * @param z		Z-coordinate
	 */
	public void define(World w, int x, int y, int z) {
		define(w, new BlockPos(x, y, z));
	}
	
	/**
	 * Restored all saved data to the world.
	 * @param overwrite	true to replace blocks
	 */
	public void restoreAll(World w, boolean overwrite) {
		for (BlockStore.Entry i : map.values()) {
			i.restore(w, overwrite);
		}
		clear();
	}
	
	/**
	 * Reads this collection from nbt data
	 */
	public BlockStore readFromNBT(NBTTagCompound nbt) {
		clear();
		if (nbt.hasKey("BlocksLog")) {
			NBTTagList list = (NBTTagList) nbt.getTag("BlocksLog");
			
			for (int i = 0; i < list.tagCount(); i++) {
				add(new Entry(list.getCompoundTagAt(i)));
			}
		}
		return this;
	}
	
	/**
	 * Writes this collection to an nbt tag for saving.
	 */
	public void writeToNBT(NBTTagCompound nbt) {
		if (!nbt.hasKey("BlocksLog")) {
			nbt.setTag("BlocksLog", new NBTTagList());
		}
		
		NBTTagList list = (NBTTagList) nbt.getTag("BlocksLog");
		for (Entry i : this) {
			NBTTagCompound entry = new NBTTagCompound();
			i.writeToNBT(entry);
			list.appendTag(entry);
		}
	}
	
	@Override
	public void clear() {
		map.clear();
		super.clear();
	}

	@Override
	public boolean remove(Object o) {
		boolean result = super.remove(o);
		if (result) map.values().remove(o);
		return result;
	}

	@Override
	public Entry remove(int index) {
		Entry result = super.remove(index);
		if (result != null) {
			map.values().remove(result);
		}
		return result;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return super.removeAll(c) | map.values().removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return super.retainAll(c) | map.values().retainAll(c);
	}

	@Override
	public Entry set(int index, Entry element) {
		Entry old = super.set(index, element);
		if (old != null) {
			map.put(old.position, element);
		}
		return old;
	}

	@Override
	public boolean add(Entry e) {
		if (isDefinedFor(e.position)) {
			Entry old = map.get(e.position);
			if (e != old) {
				set(indexOf(old), e);
				return true;
			}
		} else {
			map.put(e.position, e);
			super.add(e);
		}
		return false;
	}

	@Override
	public void add(int index, Entry element) {
		if (index < 0 && index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		if (isDefinedFor(element.position)) {
			Entry old = map.get(element.position);
			if (element != old) {
				set(indexOf(old), element);
			}
		} else {
			map.put(element.position, element);
			super.add(index, element);
		}
	}

	@Override
	public boolean addAll(Collection<? extends Entry> c) {
		boolean result = false;
		for (Entry i : c) result |= add(i);
		return result;
	}

	@Override
	public boolean addAll(int index, Collection<? extends Entry> c) {
		for (Entry i : c) {
			if (isDefinedFor(i.position)) {
				super.remove(i);
			} else {
				map.put(i.position, i);
			}
		}
		return super.addAll(index, c);
	}
	
	public class Entry implements INBTWritable {
		private BlockPos position;
		
		private IBlockState blockState;
		private TileEntity blockEntity;
		
		private NBTTagCompound blockEntityNBT;
		
		private Entry(NBTTagCompound nbt) {
			readFromNBT(nbt);
		}
		
		private Entry(World w, BlockPos pos) {
			position = pos;
			blockState = w.getBlockState(position);
			blockEntity = w.getTileEntity(position);
		}
		
		public int getX() { return position.getX(); }
		public int getY() { return position.getY(); }
		public int getZ() { return position.getZ(); }
		
		public BlockPos getPos() {return position;}
		
		public Block getBlock() {
			return blockState.getBlock();
		}
		
		public String getBlockName() {
			return ApiBlock.getStringBlockName(getBlock());
		}
		
		/**
		 * @Deprecated Using metadata directly is highly discouraged
		 */
		@Deprecated
		public int getBlockMetadata() {
			return blockState.getBlock().getMetaFromState(blockState);
		}
		
		@SuppressWarnings("deprecation")
		public void setBlockMetadata(int data) {
			blockState = blockState.getBlock().getStateFromMeta(data);
		}
		
		public void restore(World w, boolean overwrite) {
			if (overwrite || w.getBlockState(position).getMaterial() == Material.AIR) {
				w.setBlockState(position, blockState, 2);
				if (blockEntity != null) {
					w.setTileEntity(position, blockEntity);
				} else if (blockEntityNBT != null) {
					blockEntity = ((BlockContainer)blockState.getBlock()).createNewTileEntity(w, blockState.getBlock().getMetaFromState(blockState));
					blockEntity.readFromNBT(blockEntityNBT);
					blockEntityNBT = null;
				}
			}
		}
		
		@Override
		@SuppressWarnings("deprecation")
		public Entry readFromNBT(NBTTagCompound nbt) {
			int posX = nbt.getInteger("posX");
			int posY = nbt.getInteger("posY");
			int posZ = nbt.getInteger("posZ");
			position = new BlockPos(posX, posY, posZ);
			
			Block block = ApiBlock.getBlockByName(nbt.getString("blockName"));
			blockState = block.getStateFromMeta(nbt.getInteger("metadata"));
			
			if (nbt.hasKey("tileEntity")) {
				if (block instanceof BlockContainer) {
					blockEntityNBT = nbt.getCompoundTag("tileEntity");
				}
			}
			return this;
		}
		
		@Override
		public void writeToNBT(NBTTagCompound nbt) {
			nbt.setInteger("posX", getX());
			nbt.setInteger("posY", getY());
			nbt.setInteger("posZ", getZ());
			nbt.setString("blockName", getBlockName());
			nbt.setInteger("metadata", getBlockMetadata());
			
			if (blockEntity != null) {
				NBTTagCompound entityTag = new NBTTagCompound();
				blockEntity.writeToNBT(entityTag);
				nbt.setTag("tileEntity", entityTag);
			} else if (blockEntityNBT != null) {
				nbt.setTag("tileEntity", blockEntityNBT);
			}
		}
	}
}
