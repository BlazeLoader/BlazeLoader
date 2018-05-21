package com.blazeloader.api.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;

public interface IProjectile<Thrower extends Entity> {
	
	public default boolean isThrowable() {
		return this instanceof EntityThrowable ||
				this instanceof EntityArrow ||
				this instanceof EntityFireball;
	}
	
	default boolean isThrownBy(Thrower e) {
		return e != null && e.equals(this.getThrowingEntity());
	}
	
	Thrower getThrowingEntity();
	
	void setThrowingEntity(Thrower thrower);
	
	default boolean setHeading(double x, double y, double z, float velocity, float inaccuracy) {
		Object o = this;
		if (o instanceof net.minecraft.entity.IProjectile) {
			((net.minecraft.entity.IProjectile)o).setThrowableHeading(x, y, z, velocity, inaccuracy);
			return true;
		}
		return false;
	}
}
