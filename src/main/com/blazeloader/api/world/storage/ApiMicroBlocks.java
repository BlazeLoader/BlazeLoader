package com.blazeloader.api.world.storage;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.blazeloader.api.privileged.IWorld;
import com.blazeloader.api.world.ApiWorld;

public class ApiMicroBlocks {
	
	public static IMicroBlockState getMicroState(World w, BlockPos pos) {
		if (((IWorld)w).isOutsideBuildHeight(pos)) return ((IMicroBlock)Blocks.AIR).getDefaultMicroState();
		
		MicroBlocks microblocks = ApiWorld.registerWorldData(w, MicroBlocks.class, "blazeloader_microblocks");;
		return microblocks.getChunkAt(pos).getState(pos);
	}
	
	public static void setMicroState(World w, BlockPos pos, IMicroBlockState state) {
		if (((IWorld)w).isOutsideBuildHeight(pos)) return;
		
		MicroBlocks microblocks = ApiWorld.registerWorldData(w, MicroBlocks.class, "blazeloader_microblocks");;
		microblocks.getChunkAt(pos).setState(state, pos);
	}
}
