package com.blazeloader.api.entity.properties;

import net.minecraft.entity.Entity;

public interface IEntityPropertyOwner {
	EntityProperties getExtendedProperties();
	
	default Entity self() {
		return (Entity)(Object)this;
	}
	
}
