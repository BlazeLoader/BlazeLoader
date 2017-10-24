package com.blazeloader.event.mixin.common;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.blazeloader.util.registry.ObjectRegistry;

import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.registry.RegistryNamespaced;

@Mixin(RegistryNamespaced.class)
public abstract class MRegistryNamespaced<K, V> implements IRegistry<K, V>, ObjectRegistry<K, V> {
	
	@Accessor("registryObjects") public abstract Map<K, V> getUnderlyingMap();
}
