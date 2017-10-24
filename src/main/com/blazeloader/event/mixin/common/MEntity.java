package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.blazeloader.api.entity.properties.EntityProperties;
import com.blazeloader.api.entity.properties.IEntityPropertyOwner;
import com.blazeloader.event.handlers.EventHandler;

import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

@Mixin(Entity.class)
public abstract class MEntity implements ICommandSender, IEntityPropertyOwner {
	
	private EntityProperties extendedEntityProperties;
	
	public EntityProperties getExtendedProperties() {
		if (extendedEntityProperties == null) {
			extendedEntityProperties = new EntityProperties();
		}
		return extendedEntityProperties;
	}
	
	@Inject(method = "<init>(Lnet/minecraft/world/World;)V", at = @At("RETURN"))
	private void onInitEntity(World w, CallbackInfo info) {
		EventHandler.initEntity((Entity)(Object)this, w);
		getExtendedProperties().entityInit(self(), w);
	}
	
	@Inject(method = "writeToNBT(Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/nbt/NBTTagCompound;", at = @At("HEAD"))
	private void onWriteToNBT(NBTTagCompound tag, CallbackInfoReturnable<NBTTagCompound> info) {
		getExtendedProperties().writeToNBT(tag);
	}
	
	@Inject(method = "readFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)V", at = @At("HEAD"))
	private void onReadFromNBT(NBTTagCompound tag, CallbackInfo info) {
		getExtendedProperties().readFromNBT(tag);
	}
	
	@Inject(method = "copyDataFromOld(Lnet/minecraft/entity/Entity;)V", at = @At("RETURN"))
	private void onCopyDataFromOld(Entity old, CallbackInfo info) {
		extendedEntityProperties = ((IEntityPropertyOwner)old).getExtendedProperties();
	}
	
	@Inject(method = "addEntityCrashInfo(Lnet/minecraft/crash/CrashReportCategory;)V", at = @At("RETURN"))
	private void onAddEntityCrashInfo(CrashReportCategory section, CallbackInfo info) {
		getExtendedProperties().addCrashInfo(self(), section);
    }
	
	@Inject(method = "onUpdate()V", at = @At("RETURN"))
	private void onOnUpdate(CallbackInfo info) {
		getExtendedProperties().onEntityUpdate(self());
	}
	
	@Inject(method = "entityDropItem(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/item/EntityItem;", at = @At("HEAD"), cancellable = true)
	private void onEntityDropItem(ItemStack droppedItem, float yOffset, CallbackInfoReturnable<EntityItem> info) {
		EventHandler.eventEntityDropItem(self(), info, droppedItem, yOffset);
	}
	
	@Inject(method = "doBlockCollisions()V", at = @At("HEAD"))
	private void onDoBlockCollisions(CallbackInfo info) {
		EventHandler.eventDoBlockCollisions(this);
	}
}
