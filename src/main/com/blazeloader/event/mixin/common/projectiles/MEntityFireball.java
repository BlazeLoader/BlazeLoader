package com.blazeloader.event.mixin.common.projectiles;

import org.spongepowered.asm.mixin.Mixin;

import com.blazeloader.api.entity.IProjectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;

@Mixin({EntityArrow.class, EntityFireball.class})
public abstract class MEntityFireball implements IProjectile<EntityLivingBase> {

	@Override
	public EntityLivingBase getThrowingEntity() {
		return ((EntityFireball)(Object)this).shootingEntity;
	}

	@Override
	public void setThrowingEntity(EntityLivingBase thrower) {
		((EntityFireball)(Object)this).shootingEntity = thrower;
	}

	public boolean setHeading(double x, double y, double z, float velocity, float inaccuracy) {
		Object o = this;
		velocity = (float)Math.sqrt(velocity);
		((EntityFireball)o).accelerationX = x * velocity;
		((EntityFireball)o).accelerationY = y * velocity;
		((EntityFireball)o).accelerationZ = z * velocity;
		return true;
	}
}
