package com.blazeloader.event.mixin.client;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.blazeloader.api.particles.ParticlesRegister;
import com.blazeloader.event.handlers.client.EventHandlerClient;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;

@Mixin(ParticleManager.class)
public abstract class MEffectRenderer {
	
	@Shadow @Final
	private Map<Integer, IParticleFactory> particleTypes;
	
	@Inject(method = "spawnEffectParticle(IDDDDDD[I)Lnet/minecraft/client/particle/EntityFX;", at = @At("HEAD"), cancellable = true)
	private void onSPawnEffectParticle(int particleId, double x, double y, double z, double xOffset, double yOffset, double zOffset, int[] args, CallbackInfoReturnable<Particle> info) {
		EventHandlerClient.eventSpawnEffectParticle((ParticleManager)(Object)this, info, particleId, x, y, z, xOffset, yOffset, zOffset, args);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Inject(method = "registerVanillaParticles()V", at = @At("RETURN"))
	private void onRegisterVanillaParticles(CallbackInfo info) {
		ParticlesRegister.instance().init((Map)particleTypes);
	}
}
