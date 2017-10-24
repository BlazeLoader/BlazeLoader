package com.blazeloader.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

/**
 * Interface for an crafting recipe that can be done in reverse. (uncrafting)
 */
public interface IReversibleRecipe extends IRecipe {
	
	/**
	 * Sets whether this recipe will only allow reverse crafting.
	 */
	public IReversibleRecipe setReverseOnly();
	
	/**
	 * Checks if this recipe matches for the given output result and crafting grid size
	 * 
	 * @param output	Stack generated when crafting normally
	 * @param width		Width of crafting area
	 * @param height	Height of crafting area
	 * @return True if this recipe will create the given output for the given size, otherwise false
	 */
	public boolean matchReverse(ItemStack output, int width, int height);
	
	/**
	 * Gets the crafting arrangement taken in order to generate this recipe's output.
	 */
	public NonNullList<Ingredient> getRecipeInput();
}
