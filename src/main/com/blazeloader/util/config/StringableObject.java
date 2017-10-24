package com.blazeloader.util.config;

import java.lang.reflect.Method;

/**
 * A wrapper class responsible for converting a single object to and from a string.
 *
 * @param <T> The type of object this contains
 */
public class StringableObject<T> implements IWrapObject<T> {
	
	private T object;
	
	public StringableObject() {
		this(null);
	}
	
	public StringableObject(T obj) {
		object = obj; 
	}
	
	public String toString() {
		return object.toString();
	}
	
	public T get() {
		return object;
	}
	
	public void set(T value) {
		object = value;
	}
	
	@SuppressWarnings({ "unchecked" })
	public void fromString(T def, String value) {
		if (def != null) {
			@SuppressWarnings("rawtypes")
			Class typeClass = def.getClass();
			try {
				if (typeClass.isEnum()) {
					object = (T)Enum.valueOf(typeClass, value);
				} else if (IStringable.class.isAssignableFrom(def.getClass())) {
					object = (T)((IStringable<?>)def).fromString(value);
				} else {
					Method m = typeClass.getMethod("valueOf", String.class);
					if (!m.isAccessible()) {
						m.setAccessible(true);
					}
					try {
						object = (T)m.invoke(typeClass.newInstance(), value);
					} catch (Throwable e) {
						object = (T)m.invoke(def, value);
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
				object = def;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T coaxType(T def, String value) {
		if (def != null) {
			@SuppressWarnings("rawtypes")
			Class typeClass = def.getClass();
			try {
				if (typeClass.isEnum()) {
					return (T)Enum.valueOf(typeClass, value);
				} else if (IStringable.class.isAssignableFrom(def.getClass())) {
					return (T)((IStringable<?>)def).fromString(value);
				} else {
					Method m = typeClass.getMethod("valueOf", String.class);
					if (!m.isAccessible()) {
						m.setAccessible(true);
					}
					try {
						return (T)m.invoke(typeClass.newInstance(), value);
					} catch (Throwable e) {
						return (T)m.invoke(def, value);
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return (T)def;
	}
	
	/**
	 * Returns a {@code StringableObject} with the value of the given string.
	 * @param value		The string to be parsed
	 * @return A StringableObject with the value of the given string.
	 */
	public static <T> StringableObject<T> valueOf(String value) {
		StringableObject<T> result = new StringableObject<T>();
		result.fromString(null, value);
		return result;
	}
	
	public StringableObject<T> fromString(String string) {
		return valueOf(string);
	}
}
