package com.blazeloader.api.entity;

import net.minecraft.entity.Entity;

public interface IProjectile<Thrower extends Entity> {
	boolean isThrowable();
	
	default boolean isThrownBy(Thrower e) {
		return e != null && e.equals(this.getThrowingEntity());
	}
	
	Thrower getThrowingEntity();
	
	void setThrowingEntity(Thrower thrower);
	
	boolean setHeading(double x, double y, double z, float velocity, float inaccuracy);
}
