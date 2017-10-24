package com.blazeloader.api.potion;

import net.minecraft.entity.ai.attributes.IAttribute;

public interface IModifierList {
	public void add(IAttribute attribute, String uniqueId, double ammount, int operation);
}
