package com.blazeloader.event.mixin.common;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.blazeloader.api.entity.IProjectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.EnumFacing;

@Mixin({EntityThrowable.class,
	EntityArrow.class, EntityLlamaSpit.class, EntityShulkerBullet.class,
	EntityFireball.class})
public abstract class MProjectile implements IProjectile<Entity> {
	
	@Shadow
	private EntityLivingBase owner;
	
	@Shadow
	private EntityLivingBase thrower;
	@Shadow
    private String throwerName;
	
	@Override
	public boolean isThrowable() {
		Object o = this;
		return o instanceof EntityThrowable ||
				o instanceof EntityArrow ||
				o instanceof EntityFireball;
	}
	
	@Override
	public Entity getThrowingEntity() {
		Object o = this;
		if (o instanceof EntityThrowable) return ((EntityThrowable)o).getThrower();
		if (o instanceof EntityArrow) return ((EntityArrow)o).shootingEntity;
		if (o instanceof EntityFireball) return ((EntityFireball)o).shootingEntity;
		if (o instanceof EntityFishHook) return ((EntityFishHook)o).getAngler();
		if (o instanceof EntityLlamaSpit) return ((EntityLlamaSpit)o).owner;
		if (o instanceof EntityShulkerBullet) return this.owner;
		return null;
	}

	@Override
	public void setThrowingEntity(Entity thrower) {
		Object o = this;
		if (o instanceof EntityThrowable) {
			this.throwerName = null;
			this.thrower = (EntityLivingBase)thrower;
		} else if (o instanceof EntityArrow) {
			((EntityArrow)o).shootingEntity = thrower;
		} else if (o instanceof EntityFireball) {
			((EntityFireball)o).shootingEntity = (EntityLivingBase)thrower;
		} else if (o instanceof EntityLlamaSpit) {
			((EntityLlamaSpit)o).owner = (EntityLlama)thrower;
		} else if (o instanceof EntityShulkerBullet) {
			this.owner = (EntityLivingBase)thrower;
		}
	}
	
	@Shadow
	abstract void setDirection(@Nullable EnumFacing directionIn);
	
	@Override
	public boolean setHeading(double x, double y, double z, float velocity, float inaccuracy) {
		Object o = this;
		if (o instanceof net.minecraft.entity.IProjectile) {
			((net.minecraft.entity.IProjectile)o).setThrowableHeading(x, y, z, velocity, inaccuracy);
			return true;
		}
		
		velocity = (float)Math.sqrt(velocity);
		x *= velocity;
		y *= velocity;
		z *= velocity;
		if (o instanceof EntityFireball) {
			((EntityFireball)o).accelerationX = x;
			((EntityFireball)o).accelerationY = y;
			((EntityFireball)o).accelerationZ = z;
			return true;
		} else if (o instanceof EntityShulkerBullet) {
			this.setDirection(EnumFacing.getFacingFromVector((float)x, (float)y, (float)z));
			return true;
		}
		return false;
	}

}
