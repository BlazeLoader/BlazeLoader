package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.blazeloader.event.handlers.EventHandler;
import com.blazeloader.event.handlers.InternalEventHandler;
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(EntityPlayer.class)
public abstract class MEntityPlayer extends EntityLivingBase {
	public MEntityPlayer(World worldIn) {super(worldIn);}
	
	@Inject(method = "<init>(Lnet/minecraft/world/World;Lcom/mojang/authlib/GameProfile;)V", at = @At("RETURN"))
	private void onInit(World w, GameProfile profile, CallbackInfo info) {
		EventHandler.initEntityPlayer((EntityPlayer)(Object)this, w, profile);
	}

	@Inject(method = "copyFrom(Lnet/minecraft/entity/player/EntityPlayer;Z)V", at = @At("RETURN"))
	private void onClonePlayer(EntityPlayer old, boolean respawnedFromEnd, CallbackInfo info) {
		InternalEventHandler.eventClonePlayer((EntityPlayer)(Object)this, old, respawnedFromEnd);
	}
	
	@Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/item/EntityItem;", at = @At("HEAD"), cancellable = true)
	private void onDropItem(ItemStack droppedItem, boolean dropAround, boolean traceItem, CallbackInfoReturnable<EntityItem> info) {
		EventHandler.eventDropItem((EntityPlayer)(Object)this, info, droppedItem, dropAround, traceItem);
	}
	
	@Inject(method = "dropOneItem(Z)Lnet/minecraft/entity/item/EntityItem;", at = @At("HEAD"), cancellable = true)
	private void onDropOneItem(boolean dropAll, CallbackInfoReturnable<EntityItem> info) {
		EventHandler.eventDropOneItem((EntityPlayer)(Object)this, info, dropAll);
	}
	
	@Inject(method = "collideWithPlayer(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"))
	private void onCollideWithplayer(Entity entity, CallbackInfo info) {
		EventHandler.eventCollideWithPlayer((EntityPlayer)(Object)this, info, entity);
	}
	
	@Inject(method = "fall(FF)V", at = @At("HEAD"))
	private void onFall(float distance, float multiplier, CallbackInfo info) {
		EventHandler.eventFall((EntityPlayer)(Object)this, info, distance, multiplier);
	}
}
