package com.blazeloader.api.api.toolset;

import com.blazeloader.api.api.recipe.BLCraftingManager;
import com.blazeloader.api.util.java.Reflect;
import com.blazeloader.api.util.obf.BLOBF;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipesTools;

public class ToolRecipes {
    private static final String[][] patterns = Reflect.getFieldValue(RecipesTools.class, new RecipesTools(), BLOBF.getFieldMCP("net.minecraft.item.crafting.RecipesTools.recipePatterns").getValue());

    private final BLCraftingManager manager;

    public ToolRecipes(BLCraftingManager CraftingManager) {
        manager = CraftingManager;
    }

    public void AddToolSetRecipes(Item material, Item... tools) {
        AddRecipes(material, tools, false);
    }

    public void AddVanillaToolSetRecipes(Item material, Item... tools) {
        AddRecipes(material, tools, true);
    }

    private void AddRecipes(Item material, Item[] tools, boolean vanilla) {
        BLCraftingManager man = vanilla ? getVanillaManager() : manager;

        for (int i = 0; i < tools.length && i < patterns.length; i++) {
            man.addRecipe(new ItemStack(tools[i]),
                    patterns[i], '#', Items.stick, 'X', material);
        }
    }

    private BLCraftingManager getVanillaManager() {
        return BLCraftingManager.getInstance(BLCraftingManager.VanillaCraftingManagerKey);
    }
}