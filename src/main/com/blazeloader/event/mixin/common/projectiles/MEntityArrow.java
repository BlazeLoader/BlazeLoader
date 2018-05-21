package com.blazeloader.event.mixin.common.projectiles;

import org.spongepowered.asm.mixin.Mixin;

import com.blazeloader.api.entity.IProjectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;

@Mixin(EntityArrow.class)
public abstract class MEntityArrow implements IProjectile<Entity> {

	@Override
	public Entity getThrowingEntity() {
		return ((EntityArrow)(Object)this).shootingEntity;
	}

	@Override
	public void setThrowingEntity(Entity thrower) {
		((EntityArrow)(Object)this).shootingEntity = thrower;
	}
}
