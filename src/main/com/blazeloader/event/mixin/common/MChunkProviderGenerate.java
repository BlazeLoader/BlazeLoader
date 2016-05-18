package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.blazeloader.event.handlers.InternalEventHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;

@Mixin(ChunkProviderGenerate.class)
public abstract class MChunkProviderGenerate implements IChunkProvider {
	@Shadow
	private World worldObj;
	
	@ModifyArg(
		method = "populate(Lnet/minecraft/world/chunk/IChunkProvider;II)V",
		index = 1,
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Z"
		)
	)
	private IBlockState internalSetBlockState(BlockPos pos, IBlockState state, int flag) {
		return InternalEventHandler.getFluidFrozenState(worldObj, pos, state);
	}
}
