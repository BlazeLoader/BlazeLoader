package com.blazeloader.util.registry;

import java.util.Map;
import java.util.Random;

import com.mumfrey.liteloader.client.ducks.IMutableRegistry;

import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.registry.RegistryDefaulted;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.registry.RegistrySimple;

public interface Registry<K, V> extends IMutableRegistry<K, V>, IRegistry<K, V> {
	/**
	 * Converts a regular minecraft registry to an enhanced one.
	 * 
	 * Note: Conversion makes no change to the passed object. (in.equals(out) == true)
	 * 
	 * @param registry Registry to convert
	 * @return the same registry
	 */
	@SuppressWarnings("unchecked")
	public static <K, V, T extends RegistrySimple<K, V>> Registry<K, V> of(T registry) {
		return (Registry<K, V>)registry;
	}
	
	/**
	 * Creates a new namespaced registry.
	 */
	public static <K, V> RegistryNamespaced<K, V> createNamespacedRegistry() {
		return new RegistryNamespaced<K, V>();
	}
	
	/**
	 * Creates a new simple registry.
	 */
	public static <K, V> RegistrySimple<K, V> createSimpleRegistry() {
		return new RegistrySimple<K, V>();
	}
	
	public static final class Defaulted {
		
		/**
		 * Creates a new namespaced registry.
		 */
		public static <K, V> RegistryNamespacedDefaultedByKey<K, V> createNamespacedRegistry(K def) {
			return new RegistryNamespacedDefaultedByKey<K, V>(def);
		}
		
		/**
		 * Creates a new simple registry.
		 */
		public static <K, V> RegistryDefaulted<K, V> createSimpleRegistry(V def) {
			return new RegistryDefaulted<K, V>(def);
		}
	}
	
	Map<K, V> getUnderlyingMap();
	
	boolean hasKey(K key);
	
	V pickObject(Random random);
	
	void register(K key, V value);
}
