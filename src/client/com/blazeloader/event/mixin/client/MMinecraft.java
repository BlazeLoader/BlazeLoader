package com.blazeloader.event.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.blazeloader.api.client.privileged.IMinecraft;
import com.blazeloader.event.handlers.client.EventHandlerClient;
import com.blazeloader.event.handlers.client.InternalEventHandlerClient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.util.IThreadListener;

@Mixin(Minecraft.class)
public abstract class MMinecraft implements IThreadListener, ISnooperInfo, IMinecraft {
	@Accessor("modelManager")
	public abstract ModelManager getModelManager();
	
	@Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V", at = @At("HEAD"))
	private void onLoadWorld(WorldClient world, String message, CallbackInfo info) {
		EventHandlerClient.eventLoadWorld((Minecraft)(Object)this, world, message);
	}
	
	@Inject(method = "displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V", at = @At("HEAD"), cancellable = true)
	private void onDisplayGuiScreen(GuiScreen screen, CallbackInfo info) {
		EventHandlerClient.eventDisplayGuiScreen((Minecraft)(Object)this, info, screen);
	}
	
	@Inject(method = "dispatchKeypresses()V", at = @At("RETURN"))
	private void onDispatchKeypresses(CallbackInfo info) {
		InternalEventHandlerClient.eventDispatchKeypresses((Minecraft)(Object)this);
	}
	
	@Inject(method = "middleClickMouse()V", at = @At("HEAD"), cancellable = true)
	private void onMiddleClickMouse(CallbackInfo info) {
		InternalEventHandlerClient.eventMiddleClickMouse((Minecraft)(Object)this, info);
	}
}
