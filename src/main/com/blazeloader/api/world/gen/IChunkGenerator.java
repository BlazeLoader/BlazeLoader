package com.blazeloader.api.world.gen;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.Random;

/**
 * Represents a chunk generator.
 */
public interface IChunkGenerator {
    /**
     * Generate foliage, ores, structures, etc in the chunk.
     *
     * @param chunk        The chunk
     * @param provider    The chunk provider that owns the given chunk
     * @param generator    The vanilla chunk generator used to populate this chunk. Usually the same as primary but not always.
     * @param chunkX    X coordinate of the chunk
     * @param chunkZ    Z coordinate of the chunk
     * @param seed        Random generator set to the chunk specific seed
     */
    public void populateChunk(Chunk chunk, IChunkProvider provider, net.minecraft.world.gen.IChunkGenerator generator, int chunkX, int chunkZ, Random seed);
}