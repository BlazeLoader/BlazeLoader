package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.blazeloader.event.handlers.InternalEventHandler;
import net.minecraft.command.CommandHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;

@Mixin(IntegratedServer.class)
public abstract class MIntegratedServer extends MinecraftServer {
	
	public MIntegratedServer() {super(null, null, null, null, null, null, null);}

	@Inject(method = "createCommandManager()Lnet/minecraft/command/ServerCommandManager;", at = @At(value = "RETURN", shift = Shift.BEFORE), cancellable = true)
	private void onCreateNewCommandManager(CallbackInfoReturnable<CommandHandler> info) {
		InternalEventHandler.eventCreateNewCommandManager((MinecraftServer)(Object)this, info);
	}
}
