package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.blazeloader.event.handlers.InternalEventHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;

@Mixin(EntityTracker.class)
public abstract class MEntityTracker {
	@Inject(method = "track(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
	private void onTrackEntity(Entity entity, CallbackInfo info) {
		InternalEventHandler.eventTrackEntity((EntityTracker)(Object)this, info, entity);
	}
}
