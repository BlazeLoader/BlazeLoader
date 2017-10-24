package com.blazeloader.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;

public class ShapelessRecipe extends ShapelessRecipes {
	
	protected final NonNullList<Ingredient> recipeItems;
	
	public ShapelessRecipe(String group, ItemStack output, NonNullList<Ingredient> input) {
		super(group, output, input);
		recipeItems = input;
	}
}
