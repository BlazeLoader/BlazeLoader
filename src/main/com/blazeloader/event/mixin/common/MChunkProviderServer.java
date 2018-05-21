package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.blazeloader.bl.interop.ForgeDimensionType;

import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;

@Mixin(ChunkProviderServer.class)
public class MChunkProviderServer {
	@Shadow @Final
	private WorldServer world;
	
	@Inject(method = "queueUnload(Lnet/minecraft/world/chunk/Chunk;)V", at = @At("HEAD"), cancellable = true)
	public void onQueueUnload(Chunk chunk, CallbackInfo info) {
		if (world.isSpawnChunk(chunk.x, chunk.z)) {
			if (((ForgeDimensionType)(Object)world.provider.getDimensionType()).shouldLoadSpawn()) {
				info.cancel();
			}
		}
	}
}
