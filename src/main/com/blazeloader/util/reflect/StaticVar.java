package com.blazeloader.util.reflect;

import com.blazeloader.bl.obf.BLOBF;

public class StaticVar<T, V> extends Variable<T, V> {
	
	public StaticVar(Class<T> declarer, Class<V> type, String name, String... aliases) {
		super(declarer, type, true, name, aliases);
	}
	
	public StaticVar(BLOBF obf) {
		super(true, obf);
	}
	
	public StaticVar(String descriptor) {
		super(true, descriptor);
	}
	
	protected StaticVar(Variable<T,V> original) {
		super(original);
	}
	
	/**
	 * Attempts to get the underlying value. Will return the default value if it fails.
	 * 
	 * @param def		A default value to return if it fails.
	 * @return	T object referenced by this field
	 */
	public V get(V def) {
		return retrieve(null, def);
	}
	
	/**
	 * Attempts to set the underlying value.
	 * 
	 * @param instance	The instance to act on
	 * @param val		The value to assign to the underlying field
	 */
	public void set(V val) {
		deposit(null, val);
	}
}
