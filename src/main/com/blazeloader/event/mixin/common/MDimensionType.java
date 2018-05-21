package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import com.blazeloader.api.world.ApiDimension;
import com.blazeloader.bl.interop.ForgeDimensionType;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;

@Mixin(DimensionType.class)
public abstract class MDimensionType implements ForgeDimensionType {
	private boolean shouldLoadSpawn = false;
	
	public boolean shouldLoadSpawn() {
		return shouldLoadSpawn || ((DimensionType)(Object)this).getId() == 0;
	}
	
	public DimensionType setLoadSpawn(boolean keepLoaded) {
		shouldLoadSpawn = keepLoaded;
		return (DimensionType)(Object)this;
	}
	
	/*
	 * Forge
	 */
	//public static DimensionType register(String name, String suffix, int id, Class<? extends WorldProvider> provider, boolean keepLoaded) {
    //    return ApiDimension.registerDimension(id, new ResourceLocation(name), suffix, keepLoaded, provider);
    //}
}
