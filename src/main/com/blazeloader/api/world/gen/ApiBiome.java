package com.blazeloader.api.world.gen;

import com.blazeloader.event.mixin.common.MBiome;
import com.blazeloader.util.registry.Registry;
import com.blazeloader.util.registry.RegistryIdManager;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.biome.Biome;

public final class ApiBiome {
	private static final RegistryIdManager REGISTRY = new RegistryIdManager(() -> Registry.of(Biome.REGISTRY));
	
	public static void registerBiome(String name, Biome biome) {
		MBiome.register(REGISTRY.getNextFreeId(), name, biome);
	}
	
	public static RegistryNamespaced<ResourceLocation, Biome> getBiomeegistry() {
		return Biome.REGISTRY;
	}
}
