package com.blazeloader.api.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ReversibleShapelessRecipe extends ShapelessRecipe implements IReversibleRecipe {
	private boolean anyDirection = true;
	
	public ReversibleShapelessRecipe(String group, ItemStack output, NonNullList<Ingredient> input) {
		super(group, output, input);
	}
	
	public ReversibleShapelessRecipe setReverseOnly() {
		anyDirection = false;
		return this;
	}

	public boolean matches(InventoryCrafting craftingInventory, World w) {
		return anyDirection && super.matches(craftingInventory, w);
	}
	
	public boolean matchReverse(ItemStack output, int width, int height) {
		return canFit(width, height) && ItemStack.areItemStacksEqual(output, getRecipeOutput());
	}
	
	public NonNullList<Ingredient> getRecipeInput() {
		return recipeItems;
	}
}
