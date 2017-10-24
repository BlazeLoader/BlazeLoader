package com.blazeloader.api.entity;

import net.minecraft.entity.Entity;

public class ApiProjectile {
	
	/**
	 * Checks if the given entity is a projectile.
	 */
	public static boolean isProjectile(Entity e) {
		return e instanceof IProjectile || e instanceof net.minecraft.entity.IProjectile;
	}
	
	/**
	 * Checks if an entity is a thrown projectile.
	 */
	public static boolean isThrowable(Entity e) {
		return e instanceof IProjectile && ((IProjectile<?>)e).isThrowable(); 
	}
	
	/**
	 * Checks if the given projectile was thrown by the given entity
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Entity> boolean isProjectileThrownBy(Entity throwable, T e) {
		return e instanceof IProjectile && ((IProjectile<T>)throwable).isThrownBy(e);
	}
	
	/**
	 * Gets the thrower for a projectile or null
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Entity> T getThrowingEntity(Entity throwable) {
		if (throwable instanceof IProjectile) return ((IProjectile<T>)throwable).getThrowingEntity();
		return null;
	}
	
	/**
	 * Sets the thrower for a projectile.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Entity> void setThrowingEntity(Entity throwable, T thrower) {
		if (throwable instanceof IProjectile) {
			((IProjectile<T>)throwable).setThrowingEntity(thrower);
		}
	}
	
	/**
	 * Sets the velocity and heading for a projectile.
	 * 
	 * @param throwable		The projectile
	 * @param x				X Direction component
	 * @param y				Y Direction component
	 * @param z				Z Direction component
	 * @param velocity		Velocity
	 * @param inaccuracy	Inaccuracy
	 * @return				True the projectile's heading was set, false otherwise
	 */
	public static boolean setThrowableHeading(Entity throwable, double x, double y, double z, float velocity, float inaccuracy) {
		if (throwable instanceof IProjectile) {
			return ((IProjectile<?>)throwable).setHeading(x, y, z, velocity, inaccuracy);
		}
		((Entity)throwable).setVelocity(x, y, z);
		return false;
	}
}
