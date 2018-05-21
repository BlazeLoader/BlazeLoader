package com.blazeloader.api.privileged;

import net.minecraft.creativetab.CreativeTabs;

public interface ICreativeMenuForge {
	void setPages(int pages);
	
	void setCurrentTab(CreativeTabs tab);
}
