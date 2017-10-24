package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.blazeloader.event.handlers.InternalEventHandler;

import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(BlockDynamicLiquid.class)
public class MBlockDynamicLiquid extends BlockLiquid {
	protected MBlockDynamicLiquid() {super(null);}

	@Inject(method = "placeStaticBlock(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)V", at = @At("HEAD"))
	private void internalPlaceStaticBlock(World w, BlockPos pos, IBlockState state, CallbackInfo info) {
		InternalEventHandler.eventPlaceStaticBlock((BlockDynamicLiquid)(Object)this, w, pos, state, info);
    }

}
