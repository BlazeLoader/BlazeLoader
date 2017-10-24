package com.blazeloader.event.mixin.common;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.blazeloader.util.annotations.AccessTransform;

import net.minecraft.creativetab.CreativeTabs;

@Mixin(CreativeTabs.class)
public interface MCreativeTabs {
	
	@Accessor("CREATIVE_TAB_ARRAY")
	@AccessTransform(action="Non-Final")
	public static void setCreativeTabsArray(CreativeTabs[] tabs) {
        throw new NotImplementedException("MCreativeTabs mixin failed.");
	}
}
