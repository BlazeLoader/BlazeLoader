package com.blazeloader.api.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ShapedRecipe extends ShapedRecipes {
	
	private int recipeWidth, recipeHeight;
	protected NonNullList<Ingredient> recipeItems;
	
	public ShapedRecipe(String group, int width, int height, NonNullList<Ingredient> input, ItemStack output) {
		super(group, width, height, input, output);
		recipeWidth = width;
		recipeHeight = height;
		recipeItems = input;
	}
	
    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting inventory, World worldIn) {
        for (int x = 0; x <= inventory.getWidth() - recipeWidth; x++) {
            for (int y = 0; y <= inventory.getHeight() - recipeHeight; y++) {
                if (checkMatch(inventory, x, y, true)) return true;
                if (checkMatch(inventory, x, y, false)) return true;
            }
        }
        return false;
    }

    /**
     * Checks if the region of a crafting inventory is match for the recipe.
     */
    private boolean checkMatch(InventoryCrafting inventory, int x, int y, boolean flag) {
        for (int X = 0; X < inventory.getWidth(); X++) {
            for (int Y = 0; Y < inventory.getHeight(); ++Y) {
                int k = X - x;
                int l = Y - y;
                Ingredient ingredient = Ingredient.EMPTY;

                if (k >= 0 && l >= 0 && k < recipeWidth && l < recipeHeight) {
                	int index = flag ? recipeWidth - k - 1 + l * recipeWidth : k + l * recipeWidth;
                    ingredient = recipeItems.get(index);
                }

                if (!ingredient.apply(inventory.getStackInRowAndColumn(X, Y))) {
                    return false;
                }
            }
        }

        return true;
    }
}
