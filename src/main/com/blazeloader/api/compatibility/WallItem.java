package com.blazeloader.api.compatibility;

/**
 * A shortcut reference to an item on the wall.
 * Allows for most functionality of the Wall applied directly to the underlying item.
 */
public class WallItem<T> implements IUnwatchable<T> {
	
	private final Wall.Entry<T> item;
	
	protected WallItem(Wall.Entry<T> item) {
		this.item = item;
	}
	
	/**
	 * Gets the value for this entry on the wall.
	 * 
	 * @return <I> value referenced by this item's key
	 */
	public T get() {
		return item.getValue();
	}
	
	/**
	 * Sets the value for this entry on the wall
	 * 
	 * @param <I> value		value to be stored under this item's key on the wall.
	 */
	public void set(T value) {
		item.setValue(value);
	}
	
	public WallItem<T> watch(ISubscription<T> subscriptionObject) {
		item.subscribe(subscriptionObject);
		return this;
	}
	
	public WallItem<T> unwatch(ISubscription<T> subscriptionObject) {
		item.unsubscribe(subscriptionObject);
		return this;
	}
	
	public String getType() {
		return item.getType();
	}
}
