package com.blazeloader.api.compatibility;

/**
 * Subscription interface for listening to values on the wall.
 *
 * @param <T>	The type of the value you are subscribed to
 */
public interface ISubscription<T> {
	
	/**
	 * Event triggered when the values subscribed to on the wall is about to be changed.
	 * 
	 * @param key				Key identifier for the value being changed
	 * @param originalValue		The value currently present
	 * @param newValue			The new value that will be assigned
	 * 
	 * @return true to accept the value, false to reject it.
	 */
	public boolean valueChanged(String key, T originalValue, T newValue);
}
