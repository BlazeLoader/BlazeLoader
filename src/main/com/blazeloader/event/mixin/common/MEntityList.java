package com.blazeloader.event.mixin.common;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

@Mixin(EntityList.class)
public interface MEntityList {
	@Invoker("register")
	public static void register(int id, String name, Class <? extends Entity > clazz, String oldName) {
		throw new NotImplementedException("MEntityList mixin failed.");
	}
}
