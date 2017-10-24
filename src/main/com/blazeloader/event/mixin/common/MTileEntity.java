package com.blazeloader.event.mixin.common;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;

public interface MTileEntity {
	@Accessor("REGISTRY")
	public static RegistryNamespaced<ResourceLocation, Class<? extends TileEntity >> getRegistry() {
		throw new NotImplementedException("MTileEntity mixin failed");
	}
}
