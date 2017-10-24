package com.blazeloader.api.block.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Base class for any block of static liquid.
 */
public abstract class FluidStatic extends BlockStaticLiquid implements Fluid {

	protected FluidStatic(Material materialIn) {
		super(materialIn);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!checkForMixing(worldIn, pos, state)) {
            startFlowing(worldIn, pos, state);
        }
    }
	
	/**
	 * Updates this fluid block and converts it into it's flowing variant.
	 */
	protected void startFlowing(World worldIn, BlockPos pos, IBlockState state) {
        BlockDynamicLiquid blockdynamicliquid = getFlowingBlock();
        worldIn.setBlockState(pos, blockdynamicliquid.getDefaultState().withProperty(LEVEL, state.getValue(LEVEL)), 2);
        worldIn.scheduleUpdate(pos, blockdynamicliquid, tickRate(worldIn));
    }
}
