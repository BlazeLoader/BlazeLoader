package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.blazeloader.api.privileged.ITasked;
import com.blazeloader.event.handlers.EventHandler;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.item.EntityItem;

@Mixin(EntityLiving.class)
public abstract class MEntityLiving extends EntityLivingBase implements ITasked {
	private MEntityLiving() { super(null); }
	
	@Inject(method = "updateEquipmentIfNeeded(Lnet/minecraft/entity/item/EntityItem;)V", at = @At("HEAD"))
	private void onUpdateEquipmentIfNeeded(EntityItem item, CallbackInfo info) {
		EventHandler.eventUpdateEquipmentIfNeeded((EntityLiving)(Object)this, info, item);
	}
	
	@Accessor("tasks")
	public abstract EntityAITasks getAITasks();
}
