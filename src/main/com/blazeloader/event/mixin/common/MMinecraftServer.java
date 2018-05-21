package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.blazeloader.event.handlers.InternalEventHandler;

import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandSender;

import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;

@Mixin(MinecraftServer.class)
public abstract class MMinecraftServer implements ICommandSender, Runnable, IThreadListener, ISnooperInfo {
	@Inject(method = "createCommandManager()Lnet/minecraft/command/ServerCommandManager;", at = @At("RETURN"), cancellable = true)
	private void onCreateNewCommandManager(CallbackInfoReturnable<CommandHandler> info) {
		InternalEventHandler.eventCreateNewCommandManager((MinecraftServer)(Object)this, info);
	}
	
	@Inject(method = "getServerModName()Ljava/lang/String;", at = @At("RETURN"), cancellable = true)
	private void onGetServerModName(CallbackInfoReturnable<String> info) {
		InternalEventHandler.eventGetModName(info);
	}
}
