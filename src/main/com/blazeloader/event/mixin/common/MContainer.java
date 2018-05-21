package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.blazeloader.event.handlers.EventHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

@Mixin(Container.class)
public abstract class MContainer {
	@Inject(method = "slotClick(IILnet/minecraft/inventory/ClickType;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;", at = @At("RETURN"))
	private void onSlotClick(int slotId, int clickedButton, ClickType mode, EntityPlayer player, CallbackInfoReturnable<ItemStack> info) {
		EventHandler.eventSlotClick(info, player);
	}
}
