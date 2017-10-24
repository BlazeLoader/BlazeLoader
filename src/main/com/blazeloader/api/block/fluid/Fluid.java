package com.blazeloader.api.block.fluid;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Interface for blocks that act like a fluid.
 */
public interface Fluid {
	/**
	 * Checks if a block adjacent to this block should be considered as water.
	 * Used when determining if a block must freeze.
	 * 
	 * @param w		The world.
	 * @param pos	Current block location.
	 * @param side	Adjacent side to check.
	 * @param state	Current block state.
	 * @return	True if there is water against the applicable side of this block.
	 */
	boolean isAdjacentWater(World w, BlockPos pos, EnumFacing side, IBlockState state);
	
	/**
	 * Gets the blockstate this fluid must turn into when it freezes.
	 */
	IBlockState getFrozenState();
	
	/**
	 * Checks if this block can freeze. (be replaced with ice)
	 * 
	 * Called after environmental conditions have already been considered.
	 * 
	 * eg. It is already cold where this block is.
	 * 
	 * @param w		The world.
	 * @param pos	Current block location.
	 * @param state	Current block state.
	 * @return	True if this block can greeze here.
	 */
	boolean canFreeze(World w, BlockPos pos, IBlockState state);
	
	/**
	 * Gets the non-flowing variant of this block.
	 */
	FluidStatic getStaticBlock();
	
	/**
	 * Gets the flowing variant of this block.
	 */
	FluidFlowing getFlowingBlock();
}
