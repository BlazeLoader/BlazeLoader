package com.blazeloader.api.block;

import com.blazeloader.util.registry.ObjectRegistry;

import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;

public class BlockMaterials {
	
	private static int nextId = 1;
	public static final RegistryNamespaced<ResourceLocation, Material> REGISTRY = ObjectRegistry.createNamespacedRegistry();
	
	public static Material getMaterialByName(String name) {
		return REGISTRY.getObject(new ResourceLocation(name));
	}
	
	public static void register(String name, Material material) {
		REGISTRY.register(nextId++, new ResourceLocation(name), material);
	}
	
	static {
		register("air", Material.AIR);
		register("grass", Material.GRASS);
		register("ground", Material.GROUND);
		register("wood", Material.WOOD);
		register("rock", Material.ROCK);
		register("iron", Material.IRON);
		register("anvil", Material.ANVIL);
		register("water", Material.WATER);
		register("lava", Material.LAVA);
		register("leaves", Material.LEAVES);
		register("plants", Material.PLANTS);
		register("vine", Material.VINE);
		register("sponge", Material.SPONGE);
		register("cloth", Material.CLOTH);
		register("fire", Material.FIRE);
		register("sand", Material.SAND);
		register("circuits", Material.CIRCUITS);
		register("carpet", Material.CARPET);
		register("glass", Material.GLASS);
		register("redstone_lit", Material.REDSTONE_LIGHT);
		register("explosives", Material.TNT);
		register("coral", Material.CORAL);
		register("ice", Material.ICE);
		register("packed_ice", Material.PACKED_ICE);
		register("snow", Material.SNOW);
		register("crafted_snow", Material.CRAFTED_SNOW);
		register("cactus", Material.CACTUS);
		register("clay", Material.CLAY);
		register("gourd", Material.GOURD);
		register("dragon_egg", Material.DRAGON_EGG);
		register("portal", Material.PORTAL);
		register("cake", Material.CAKE);
		register("web", Material.WEB);
		register("piston", Material.PISTON);
		register("barrier", Material.BARRIER);
		register("structure", Material.STRUCTURE_VOID);
	}
}
