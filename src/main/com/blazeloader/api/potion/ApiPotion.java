package com.blazeloader.api.potion;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;

public class ApiPotion extends PotionType {
	public static Potion registerPotionEffect(ResourceLocation name, IPotionEffect potionEffect) {
		return new ModPotion(name, potionEffect);
	}
	
	public static void registerPotionType(ResourceLocation name, PotionEffect... effects) {
		PotionType.registerPotionType(name.toString(), new PotionType(effects));
	}
	
	public static void registerPotionType(ResourceLocation name, ResourceLocation base, PotionEffect... effects) {
		PotionType.registerPotionType(name.toString(), new PotionType(base.toString(), effects));
	}
}
