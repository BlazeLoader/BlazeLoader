package com.blazeloader.util.config;

/**
 * Utility interface for wrapping an object.
 *
 * @param <T> the type of object
 */
public interface IWrapObject<T> extends IStringable<IWrapObject<T>> {
	
	@SuppressWarnings("unchecked")
	public default Class<T> getObjectClass() {
		return (Class<T>)get().getClass();
	}
	
	/**
	 * Gets the underlying object
	 */
	public T get();
	
	/**
	 * Sets the underlying object to the desired value
	 */
	public void set(T value);
	
	public void fromString(T def, String string);
}
