package com.blazeloader.api.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ReversibleShapedRecipe extends ShapedRecipe implements IReversibleRecipe {
	private boolean anyDirection = true;
	
	public ReversibleShapedRecipe(String group, int width, int height, NonNullList<Ingredient> input, ItemStack output) {
		super(group, width, height, input, output);
	}

	public ReversibleShapedRecipe setReverseOnly() {
		anyDirection = false;
		return this;
	}

	public boolean matches(InventoryCrafting craftingInventory, World w) {
		return anyDirection && super.matches(craftingInventory, w);
	}
	
	public boolean matchReverse(ItemStack output, int width, int height) {
		return ItemStack.areItemStacksEqual(output, getRecipeOutput());
	}
	
	public NonNullList<Ingredient> getRecipeInput() {
		return recipeItems;
	}
}