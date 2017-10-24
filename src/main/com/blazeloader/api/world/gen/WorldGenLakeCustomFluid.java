package com.blazeloader.api.world.gen;

import java.util.Random;

import com.blazeloader.api.block.fluid.Fluid;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenLakes;

public class WorldGenLakeCustomFluid<T extends Block & Fluid> extends WorldGenLakes implements IChunkGenerator {
	
	private final T block;
	
	public WorldGenLakeCustomFluid(T blockIn) {
		super(blockIn);
		block = blockIn;
	}
	
	public boolean generate(World w, Random rand, BlockPos position) {
        if (!super.generate(w, rand, position)) {
            return false;
        }
    	for (position = position.add(-8, 0, -8); position.getY() > 5 && w.isAirBlock(position); position = position.down());
        position = position.down(4);
        for (int k2 = 0; k2 < 16; ++k2) {
            for (int l3 = 0; l3 < 16; ++l3) {
                int l4 = 4;
                if (w.canBlockFreezeWater(position.add(k2, l4, l3))) {
                    w.setBlockState(position.add(k2, l4, l3), block.getFrozenState(), 2);
                }
            }
        }
        return true;
    }
	
	public void populateChunk(Chunk chunk, IChunkProvider primary, net.minecraft.world.gen.IChunkGenerator secondary, int chunkX, int chunkZ, Random seed) {
        BlockPos blockpos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
        seed.setSeed(chunk.getWorld().getSeed());
        long k = seed.nextLong() / 2L * 2L + 1L;
        long l = seed.nextLong() / 2L * 2L + 1L;
        seed.setSeed((long)chunkX * k + (long)chunkZ * l ^ chunk.getWorld().getSeed());
        generate(chunk.getWorld(), seed, blockpos.add(seed.nextInt(16) + 8, seed.nextInt(256), seed.nextInt(16) + 8));
	}
}
