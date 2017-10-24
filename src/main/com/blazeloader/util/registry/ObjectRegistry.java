package com.blazeloader.util.registry;

import java.util.Map;

import com.mumfrey.liteloader.client.ducks.IMutableRegistry;

import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.registry.RegistrySimple;

public interface ObjectRegistry<K, V> extends IMutableRegistry<K, V> {
	/**
	 * Converts a regular minecraft registry to an enhanced one.
	 * 
	 * Note: Conversion makes no change to the passed object. (in.equals(out) == true)
	 * 
	 * @param registry Registry to convert
	 * @return the same registry
	 */
	@SuppressWarnings("unchecked")
	public static <K, V, T extends RegistrySimple<K, V>> ObjectRegistry<K, V> of(T registry) {
		return (ObjectRegistry<K, V>)(Object)registry;
	}
	
	public static <K, V> RegistryNamespaced<K, V> createNamespacedRegistry() {
		return new RegistryNamespaced<K, V>();
	}
	
	public Map<K, V> getUnderlyingMap();
	
	public void remove(V value);
}
