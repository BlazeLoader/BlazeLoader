package com.blazeloader.event.mixin.common;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.biome.Biome;

@Mixin(Biome.class)
public interface MBiome {
	@Invoker("registerBiome")
	public static void register(int id, String name, Biome biome) {
		throw new NotImplementedException("MBiome mixin failed.");
	}
}
