package com.blazeloader.event.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.blazeloader.event.handlers.VillagerEventHandler;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;

@Mixin(RenderVillager.class)
public abstract class MRenderVillager extends RenderLiving<EntityVillager> {
	public MRenderVillager() {super(null, null, -1);}
	
	@Inject(method = "getEntityTexture(Lnet/minecraft/entity/passive/EntityVillager;)Lnet/minecraft/util/ResourceLocation;", at = @At("HEAD"), cancellable = true)
	private void onGetEntityTexture(EntityVillager villager, CallbackInfoReturnable<ResourceLocation> info) {
		VillagerEventHandler.eventGetEntityTexture(villager, info);
	}
}
