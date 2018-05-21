package com.blazeloader.event.mixin.common.projectiles;

import org.spongepowered.asm.mixin.Mixin;

import com.blazeloader.api.entity.IProjectile;

import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.projectile.EntityLlamaSpit;

@Mixin(EntityLlamaSpit.class)
public abstract class MEntityLlamaSpit implements IProjectile<EntityLlama> {

	@Override
	public EntityLlama getThrowingEntity() {
		return ((EntityLlamaSpit)(Object)this).owner;
	}

	@Override
	public void setThrowingEntity(EntityLlama thrower) {
		((EntityLlamaSpit)(Object)this).owner = thrower;
	}
}
