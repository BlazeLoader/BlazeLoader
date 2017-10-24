package com.blazeloader.api.potion;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public interface IPotionEffect {
	String getName();
	
	boolean isBad();
	
	boolean isGood();
	
	int getColour();
	
	boolean isInstant();
	
	int getCooldown(int amplifier);
	
	void performEffect(EntityLivingBase target, int amplifier);
	
	void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, EntityLivingBase target, int amplifier, double health);
	
	void registerModifiers(IModifierList modifiers);
}
