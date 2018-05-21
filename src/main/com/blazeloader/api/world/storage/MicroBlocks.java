package com.blazeloader.api.world.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.blazeloader.api.world.gen.WorldSavedDataCollection;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.RegionFileCache;
import net.minecraft.world.storage.IThreadedFileIO;
import net.minecraft.world.storage.ThreadedFileIOBase;

public class MicroBlocks extends WorldSavedDataCollection implements IThreadedFileIO {
	
	Long2ObjectMap<MicroChunk> chunks = new Long2ObjectOpenHashMap<MicroChunk>(8192);
	Map<MicroChunk, NBTTagCompound> chunksToRemove = new HashMap<MicroChunk, NBTTagCompound>();
	
	private File worldDir = null;
	
	private final MicroChunk empty = new MicroChunk(0, 0, 0) {
		public IMicroBlockState getState(BlockPos pos) {
			return ((IMicroBlock)Blocks.AIR).getDefaultMicroState();
		}
	};
	
	public MicroBlocks(World w) {
		super("blazeloader_microblocks");
		setWorldsForAll(w);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		saveChangedChunks();
		return compound;
	}
	
	private void saveChangedChunks() {
		for (MicroChunk ch : chunks.values()) {
			if (ch.dirty() && !chunksToRemove.containsKey(ch)) {
				NBTTagCompound tag = new NBTTagCompound();
				ch.writeToNBT(tag);
				chunksToRemove.put(ch, tag);
				ThreadedFileIOBase.getThreadedIOInstance().queueIO(this);
			}
		}
	}
	
	private MicroChunk loadChunk(int x, int z) throws IOException {
		long pos = ChunkPos.asLong(x, z);
		MicroChunk ch = chunks.get(pos);
		if (ch == null) {
			DataInputStream input = RegionFileCache.getChunkInputStream(worldDir, x, z);
			if (input == null) return null;
			
			NBTTagCompound tag = CompressedStreamTools.read(input);
			
			ch = new MicroChunk(x, z, 0);
			ch.readFromNBT(tag);
			chunks.put(pos, ch);
		}
		return ch;
	}
	
	public MicroChunk getChunkAt(BlockPos pos) {
		markDirty();
		MicroChunk ch = null;
		try {
			ch = loadChunk(pos.getX() >> 4, pos.getZ() >> 4);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ch == null ? empty : ch;
	}

	@Override
	public boolean writeNextIO() {
		MicroChunk ch = chunksToRemove.keySet().iterator().next();
		NBTTagCompound tag = chunksToRemove.remove(ch);
		
		if (tag != null) {
	        try {
	        	DataOutputStream output = RegionFileCache.getChunkOutputStream(worldDir, ch.x, ch.z);
				CompressedStreamTools.write(tag, output);
				
				output.close();
				
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
	        
		}
		
		return false;
	}

	@Override
	public void setWorldsForAll(World w) {
		worldDir = new File(new File(w.getSaveHandler().getWorldDirectory(), "microblocks"), w.provider.getDimensionType().getName());
		worldDir.mkdirs();
	}
}
