package com.blazeloader.api.gui;

import com.blazeloader.event.mixin.common.MCreativeTabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class CreativeTabToolbars extends CreativeTabs {
	public CreativeTabToolbars() {
		super(4, "hotbar");
		MCreativeTabs.setHotbarTab(this);
	}
	
	public ItemStack getTabIconItem() {
        return new ItemStack(Blocks.BOOKSHELF);
    }
    
    public void displayAllRelevantItems(NonNullList<ItemStack> items) {
        throw new RuntimeException("Implement exception client-side.");
    }
}
