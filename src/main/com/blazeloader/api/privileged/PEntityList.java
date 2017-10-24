package com.blazeloader.api.privileged;

import net.minecraft.entity.EntityList;

public class PEntityList extends EntityList {
	public static EntityList.EntityEggInfo addSpawnInfo(String id, int primaryColor, int secondaryColor) {
		return EntityList.addSpawnInfo(id, primaryColor, secondaryColor);
	}
}
