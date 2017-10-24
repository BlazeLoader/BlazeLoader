package com.blazeloader.api.toolset;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

//Common tool interface.
// Intended to absorb inconsistencies, because in vanilla ItemHoe and ItemSword
// have no relation to the other items in a toolset.
public interface ITool {

	/**
	 * Checks if an item represents a tool, either in vanilla or by implementing this interface.
	 */
	public static boolean isTool(Item item) {
		return item != null && (item instanceof ITool || item instanceof ItemTool);
	}
	
	/**
	 * Checks if an itemstack contains a tool, either in vanilla or by implementing this interface.
	 */
	public static boolean isTool(ItemStack stack) {
		return stack != null && isTool(stack.getItem());
	}
	
	/**
	 * Checks if the item represented by a given entity contains a tool, either in vanilla or by implementing this interface.
	 */
	public static boolean isTool(Entity entity) {
		return entity instanceof EntityItem && isTool(((EntityItem)entity).getItem());
	}
	
	/**
	 * Gets the attributes object for this tool.
	 */
	public ToolsetAttributes getToolAttributes();
	
	/**
     * Return the name for this tool's material.
     * 
     * @default Delegates to the ItemTool implementation.
     */
	public default String getToolMaterialName() {
		if (!(this instanceof ItemTool)) throw new AbstractMethodError();
		return ((ItemTool)this).getToolMaterialName();
	}
}
