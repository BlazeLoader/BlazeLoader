package com.blazeloader.event.mixin.common.projectiles;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.blazeloader.api.entity.IProjectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;

@Mixin(EntityThrowable.class)
public abstract class MEntityThrowable implements IProjectile<EntityLivingBase> {
	
	@Shadow
	private EntityLivingBase thrower;
	@Shadow
    private String throwerName;
	
	
	@Override
	public EntityLivingBase getThrowingEntity() {
		return ((EntityThrowable)(Object)this).getThrower();
	}

	@Override
	public void setThrowingEntity(EntityLivingBase thrower) {
		this.throwerName = null;
		this.thrower = thrower;
	}
}
