package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.blazeloader.event.handlers.EventHandler;
import com.blazeloader.event.handlers.InternalEventHandler;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

@Mixin(Chunk.class)
public abstract class MChunk {
	@Inject(method = "onLoad()V", at = @At("RETURN"))
	private void onOnLoad(CallbackInfo info) {
		EventHandler.eventOnChunkLoad((Chunk)(Object)this);
	}
	
	@Inject(method = "onUnload()V", at = @At("RETURN"))
	private void onOnUnload(CallbackInfo info) {
		EventHandler.eventOnChunkUnload((Chunk)(Object)this);
	}
	
	@Inject(method = "populate(Lnet/minecraft/world/chunk/IChunkProvider;Lnet/minecraft/world/gen/IChunkGenerator;)V", at = @At("RETURN"))
	private void onPopulate(IChunkProvider one, IChunkGenerator two, CallbackInfo info) {
		InternalEventHandler.eventPopulateChunk((Chunk)(Object)this, one, two);
	}
}
