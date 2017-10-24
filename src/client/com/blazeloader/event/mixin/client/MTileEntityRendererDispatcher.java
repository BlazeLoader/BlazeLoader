package com.blazeloader.event.mixin.client;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

@Mixin(TileEntityRendererDispatcher.class)
public interface MTileEntityRendererDispatcher {
	@Accessor("renderers")
	public Map <Class<? extends TileEntity >, TileEntitySpecialRenderer<? extends TileEntity>> getRegistry();
}
