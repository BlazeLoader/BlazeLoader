package com.blazeloader.event.mixin.common;

import java.util.Map;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.blazeloader.util.registry.Registry;

import net.minecraft.util.registry.RegistrySimple;

@Mixin(RegistrySimple.class)
public interface MRegistrySimple<K, V> extends Registry<K, V> {
	
	@Accessor("registryObjects")
	Map<K, V> getUnderlyingMap();
	
	@Invoker("containsKey")
	boolean hasKey(K key);
	
	@Invoker("getRandomObject")
	V pickObject(Random random);
	
	@Invoker("putObject")
	void register(K key, V value);
}
