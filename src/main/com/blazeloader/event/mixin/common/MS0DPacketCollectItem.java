package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.blazeloader.event.handlers.EventHandler;

import net.minecraft.network.play.server.SPacketCollectItem;

@Mixin(SPacketCollectItem.class)
public abstract class MS0DPacketCollectItem {
	@Inject(method = "<init>(III)V", at = @At("RETURN"))
	private void initSPacketCollectItem(int itemId, int entityId, int quantity, CallbackInfo info) {
		EventHandler.initS0DPacketCollectItem(itemId, entityId, quantity);
	}
}
