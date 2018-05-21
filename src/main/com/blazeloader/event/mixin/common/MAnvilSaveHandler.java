package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.blazeloader.event.handlers.InternalEventHandler;

import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.SaveHandler;

@Mixin(SaveHandler.class)
public abstract class MAnvilSaveHandler {
	
	@Shadow @Final
	protected DataFixer dataFixer;
	
	@Inject(method = "getChunkLoader(Lnet/minecraft/world/WorldProvider;)Lnet/minecraft/world/chunk/storage/IChunkLoader;", at = @At("HEAD"))
	private void onGetChunkLoader(WorldProvider provider, CallbackInfoReturnable<IChunkLoader> info) {
		InternalEventHandler.eventGetChunkLoader(((SaveHandler)(Object)this), dataFixer, provider, info);
	}
}
