package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.blazeloader.event.handlers.EventHandler;
import com.blazeloader.event.handlers.InternalEventHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

@Mixin(WorldServer.class)
public abstract class MWorldServer {
	@Inject(method = "init()Lnet/minecraft/world/World;", at = @At(value = "RETURN"))
	private void onInit(CallbackInfoReturnable<World> info) {
		EventHandler.eventInit((WorldServer)(Object)this);
	}
	
	@ModifyArg(
		method = "updateBlocks()V",
		index = 1,
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/WorldServer;setBlockState(Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;)Z"
		)
	)
	private IBlockState internalSetBlockState(BlockPos pos, IBlockState state) {
		return InternalEventHandler.getFluidFrozenState((World)(Object)this, pos, state);
	}
}
