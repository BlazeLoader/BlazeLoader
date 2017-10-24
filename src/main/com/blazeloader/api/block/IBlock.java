package com.blazeloader.api.block;

import java.util.Collection;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockLever.EnumOrientation;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Default implementations for the block class.
 * 
 * These are separated from the primary mixin so they may be overridden higher up by forge methods.
 * 
 * Not to be used directly.
 */
public interface IBlock extends IRotateable, ISided {
	@Deprecated
	public default boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		IBlockState state = world.getBlockState(pos);
		EnumFacing rotation = getBlockRotation(world, pos, state);
		return rotation != null && rotateBlockTo(world, pos, state, rotation.rotateAround(axis.getAxis()));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public default boolean rotateBlockTo(World w, BlockPos pos, IBlockState state, EnumFacing facing) {
		if (this instanceof BlockLever) {
			w.setBlockState(pos, state.withProperty(BlockLever.FACING, EnumOrientation.forFacings(facing, EnumFacing.NORTH)));
			return true;
		}
		if (this instanceof BlockSlab) {
			w.setBlockState(pos, state.withProperty(BlockSlab.HALF, facing == EnumFacing.UP ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM));
			return true;
		}
		if (this instanceof BlockLog) {
			w.setBlockState(pos, state.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.fromFacingAxis(facing.getAxis())));
			return true;
		}
		if (this instanceof BlockRotatedPillar) {
			w.setBlockState(pos, state.withProperty(BlockRotatedPillar.AXIS, facing.getAxis()));
			return true;
		}
		for (IProperty i : state.getProperties().keySet()) {
			if (i instanceof PropertyEnum || i.getValueClass() == EnumFacing.class) {
				Collection<EnumFacing> allowedValues = (Collection<EnumFacing>)Lists.newArrayList(i.getAllowedValues());
				if (allowedValues.contains(facing)) {
					w.setBlockState(pos, state.withProperty(i, facing));
					return true;
				}
				return false;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public default EnumFacing[] getValidRotations(World world, BlockPos pos) {
		if (this instanceof BlockLever || this instanceof BlockRotatedPillar) {
			return EnumFacing.values();
		}
		if (this instanceof BlockSlab) {
			return EnumFacing.Plane.VERTICAL.facings();
		}
		for (IProperty<?> i : world.getBlockState(pos).getProperties().keySet()) {
			if (i instanceof PropertyEnum || i.getValueClass() == EnumFacing.class) {
				Collection<EnumFacing> result = (Collection<EnumFacing>)i.getAllowedValues();
				return result.toArray(new EnumFacing[result.size()]);
			}
		}
		return null;
	}
	
	public default EnumFacing getBlockRotation(World w, BlockPos pos, IBlockState state) {
    	for (Entry<IProperty<?>, Comparable<?>> i : state.getProperties().entrySet()) {
			if (i.getKey() instanceof PropertyEnum || i.getKey().getValueClass() == EnumFacing.class) {
				return (EnumFacing)i.getValue();
			}
		}
    	return null;
	}
	
	public default boolean isSideSolid(IBlockAccess w, BlockPos pos, EnumFacing side) {
		return isSideSolid(w, pos, side, false);
	}
	
	public default boolean isSideSolid(IBlockAccess w, BlockPos pos, EnumFacing side, boolean def) {
		IBlockState state = w.getBlockState(pos);
        Block block = (Block)this;
        
		if (side == EnumFacing.UP) {
			return state.isTopSolid();
		}
		if (side == EnumFacing.DOWN) {
			if (block instanceof BlockStairs) {
				return !state.isTopSolid();
			}
			if (block instanceof BlockSlab) {
				return ((BlockSlab)state.getBlock()).isDouble() || !state.isTopSolid();
			}
			if (block instanceof BlockHopper) return true;
		}
    	return def;
	}
}
