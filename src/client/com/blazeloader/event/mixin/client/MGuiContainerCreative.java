package com.blazeloader.event.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.blazeloader.api.privileged.ICreativeMenuForge;

import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.creativetab.CreativeTabs;

@Mixin(GuiContainerCreative.class)
public abstract class MGuiContainerCreative implements ICreativeMenuForge {
	@SuppressWarnings("unused")
	private static int tabPage;
	@SuppressWarnings("unused")
	private int maxPages;
	
	@Override
	public void setPages(int pages) {
		tabPage = maxPages = pages;
	}
	
	@Override
	@Invoker("setCurrentCreativeTab")
	public abstract void setCurrentTab(CreativeTabs tab);
}
