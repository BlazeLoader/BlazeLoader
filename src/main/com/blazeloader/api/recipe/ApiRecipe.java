package com.blazeloader.api.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;

/**
 * API function related to vanilla crafting
 */
public class ApiRecipe {

    /**
     * Registers a shaped recipe.
     *
     * @param output An itemStack with the recipe output
     * @param args   An object array on how to form the recipe e.g. "##", "##", "##", Character.valueOf('#'), new ItemStack(Block.door)
     */
    public static void addShapedCraftingRecipe(ResourceLocation name, ItemStack output, Object... input) {
    	ApiCrafting.getVanillaCraftingManager().addRecipe(name, output, input);
    }

    /**
     * Registers a shapeless recipe.
     *
     * @param output An itemStack with the recipe output
     * @param args   An object array of itemStacks to use
     */
    public static void addShapelessCraftingRecipe(ResourceLocation name, ItemStack output, Object... input) {
    	ApiCrafting.getVanillaCraftingManager().addShapelessRecipe(name, output, input);
    }

    /**
     * Registers a furnace recipe.
     *
     * @param input  Block type to be smelted
     * @param output An itemStack with the recipe output
     * @param xp     Float value with the amount of xp received when cooking an item/block
     */
    public static void addSmeltingRecipe(Block input, ItemStack output, float xp) {
        FurnaceRecipes.instance().addSmeltingRecipeForBlock(input, output, xp);
    }

    /**
     * Registers a furnace recipe.
     *
     * @param input  item to be smelted
     * @param output An itemStack with the recipe output
     * @param xp     Float value with the amount of xp received when cooking an item/block
     */
    public static void addSmeltingRecipe(Item input, ItemStack output, float xp) {
        FurnaceRecipes.instance().addSmelting(input, output, xp);
    }

    /**
     * Registers a furnace recipe.
     *
     * @param input  itemStack to be smelted
     * @param output An itemStack with the recipe output
     * @param xp     Float value with the amount of xp received when cooking an item/block
     */
    public static void addSmeltingRecipe(ItemStack input, ItemStack output, float xp) {
        FurnaceRecipes.instance().addSmeltingRecipe(input, output, xp);
    }
}
