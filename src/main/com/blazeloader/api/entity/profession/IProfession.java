package com.blazeloader.api.entity.profession;

import java.util.Random;

import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * A custom villager profession
 */
public interface IProfession {
	
	/**
	 * Checks if the given stack is sufficient for the villager to begin mating.
	 * <p>
	 * Called on each item in the villager's inventory.
	 * 
	 * @param stack				Stack to check
	 * @param condition			The current mating condition being tested
	 * 
	 * @return	True if it may mate, false otherwise
	 */
	boolean checkMatingConditions(ItemStack stack, MatingCondition condition);
	
	/**
	 * The amount this villager loves to eat.
	 */
	int getStackMultiplier();
	
	int getNewCareer(Random rand);
	
	/**
	 * Gets the entries for a villager's trades.
	 * 
	 * @param villager	The villager
	 */
	ITradeList[] getTradeList(int careerId, int careerLevel);
	
	/**
	 * Gets a display name for the villager.
	 */
	ITextComponent getDisplayName();
	
	/**
	 * Gets a villager texture location for this profession.
	 * 
	 * @param villager	The villager
	 */
	ResourceLocation getResourceLocation();
	
	/**
	 * Sets profession specific AI tasks for the given villager.
	 * 
	 * @param aiTasks the villager's set of AI tasks.
	 */
	void setAdditionalAItasks(EntityAITasks aiTasks);
}
