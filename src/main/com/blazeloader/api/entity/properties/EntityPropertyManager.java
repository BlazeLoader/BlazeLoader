package com.blazeloader.api.entity.properties;

import net.minecraft.entity.Entity;

/**
 * Static registry for managing extended entity properties.
 */
public final class EntityPropertyManager {
	/**
	 * Registers a new EntityProperty object for an entity instance.
	 * 
	 * @param e		The entity to register with
	 * @param p		The property object
	 */
	public static void registerEntityProperties(Entity e, IEntityProperties p) {
		if (e == null) return;
		((IEntityPropertyOwner)e).getExtendedProperties().registerHandler(p);
	}
	
	/**
	 * Removes an entity property object previously set by registerEntityProperties
	 * 
	 * @param e		The entity
	 * @param p		The property object
	 */
	public static void unRegisterEntityProperties(Entity e, IEntityProperties p) {;
		((IEntityPropertyOwner)e).getExtendedProperties().unRegisterHandler(p);
	}
	
	/**
	 * Gets an entity property instance associated with the given class. Returns null if one had not been added.
	 * 
	 * @param e		The entity
	 * @param c		The type of entity properties
	 * @return		Null or the property instance
	 */
	public static <E extends IEntityProperties> E getEntityPropertyObject(Entity e, Class<E> c) {
		return ((IEntityPropertyOwner)e).getExtendedProperties().getHandler(e, c);
	}
}
