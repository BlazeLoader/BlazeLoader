package com.blazeloader.event.mixin.common.projectiles;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.blazeloader.api.entity.IProjectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.util.EnumFacing;

@Mixin(EntityShulkerBullet.class)
public abstract class MEntityShulkerBullet implements IProjectile<EntityLivingBase> {
	
	@Shadow
	private EntityLivingBase owner;
	
	
	@Override
	public EntityLivingBase getThrowingEntity() {
		return this.owner;
	}

	@Override
	public void setThrowingEntity(EntityLivingBase thrower) {
		owner = thrower;
	}
	
	@Shadow
	abstract void setDirection(@Nullable EnumFacing directionIn);
	
	@Override
	public boolean setHeading(double x, double y, double z, float velocity, float inaccuracy) {
		velocity = (float)Math.sqrt(velocity);
		x *= velocity;
		y *= velocity;
		z *= velocity;
		setDirection(EnumFacing.getFacingFromVector((float)x, (float)y, (float)z));
		return true;
	}

}
