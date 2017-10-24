package com.blazeloader.bl.interop;

import com.blazeloader.api.privileged.IWorld;

import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.storage.MapStorage;

import com.google.common.collect.ImmutableSetMultimap;

public interface ForgeWorldAccess {
	
	public default World asWorld() {
		return (World)this;
	}
	
	/**
	 * Checks if the given side of a block is solid.
	 * 
	 * @forge This is part of the Forge API specification
	 * @param pos	The location
	 * @param side	The face
	 */
	public default boolean isSideSolid(BlockPos pos, EnumFacing side) {
		return isSideSolid(pos, side, false);
	}
	
	/**
	 * Checks if the given side of a block is solid.
	 * 
	 * @forge This is part of the Forge API specification
	 * @param pos	The location
	 * @param side	The face
	 */
	public default boolean isSideSolid(BlockPos pos, EnumFacing side, boolean def) {
		return def;
	}
	
	/**
	 * Gets the set of chunks persisted by Forge Modloader.
	 * 
	 * @forge This is part of the Forge API specification
	 * @param <Ticket> A forge chunk manager ticket.
	 */
	public default <Ticket> ImmutableSetMultimap<ChunkPos, Ticket> getPersistentChunks() {
		return ImmutableSetMultimap.<ChunkPos, Ticket>of();
	}
	
	/**
	 * Gets the amount of light a block will allow through
	 */
	public default int getBlockLightOpacity(BlockPos pos) {
		if (!((IWorld)asWorld()).isCoordValid(pos)) return 0;
        return asWorld().getChunkFromBlockCoords(pos).getBlockLightOpacity(pos);
	}
	
	/**
	 * Counts the number of entities with the given creature type.
	 * 
	 * @forge This is part of the Forge API specification
	 * @param type			Type of entity
	 * @param forSpawnCount	True if we are checking for spawn count limits
	 */
	public default int countEntities(EnumCreatureType type, boolean forSpawnCount) {
		return asWorld().countEntities(type.getCreatureClass());
	}
	
	/**
     * Rotate the block at the given coordinates. For vanilla blocks this rotates around the axis passed in (generally, it should be the "face" that was hit).
     * Note: for mod blocks, this is up to the block and modder to decide. It is not mandated that it be a rotation around the
     * face, but could be a rotation to orient *to* that face, or a visiting of possible rotations.
     * The method should return true if the rotation was successful though.
     *
     * @forge This is part of the Forge API specification
     * @param pos Block position in world
     * @param axis The axis to rotate around
     * @return True if the rotation was successful, False if the rotation failed, or is not possible
     */
	@Deprecated
	public default boolean rotateBlock(BlockPos pos, EnumFacing axis) {
		IBlockState state = asWorld().getBlockState(pos);
		for (IProperty<?> i : state.getProperties().keySet()) {
			if (i.getName().contentEquals("facing")) {
				asWorld().setBlockState(pos, state.cycleProperty(i));
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the per-world map storage introduced by forge
	 */
	public default MapStorage getPerWorldStorage() {
		return asWorld().getMapStorage();
	}
	
	/**
	 * Gets the maximum entity size. Used when checking if an entity is within a given region.
	 * 
	 * @forge This is an accessor method for {@code World.MAX_ENTITY_RADIUS} in the Forge API specification
	 * @param def	A default value to return if none others are found
	 */
	public double getMaxEntitySize(double def);
	
	/**
	 * Sets the maximum entity size. Used when checking if an entity is within a given region.
	 * 
	 * @forge This is an accessor method for {@code World.MAX_ENTITY_RADIUS} in the Forge API specification
	 * @param size	The maximum entity size
	 */
	public void setMaxEntitySize(double size);
}
