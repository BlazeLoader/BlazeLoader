package com.blazeloader.event.mixin.common;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

@Mixin(Item.class)
public interface MItem {
	
	@Accessor("BLOCK_TO_ITEM")
	public abstract Map<Block, Item> getBlockItemMap();
}
