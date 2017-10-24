package com.blazeloader.event.mixin.client;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;

@Mixin(RenderManager.class)
public interface MRenderManager {
	@Accessor("skinMap")
	public Map<String, RenderPlayer> getSkinMap();
}
