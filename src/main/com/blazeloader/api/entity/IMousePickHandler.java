package com.blazeloader.api.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Interface for entities that want to return a custom item when a player uses the PickBlock key on them.
 * 
 * e.g Boats give boats, and mobs give spawn eggs
 */
public interface IMousePickHandler {
	
	/**
	 * Called when he player presses the pick-block button whilst looking a this entity.
	 * 
	 * @return the item that must be picked or null for default behaviour.
	 */
	ItemStack onPlayerMiddleClick(EntityPlayer player);
}
