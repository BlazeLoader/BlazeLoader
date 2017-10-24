package com.blazeloader.api.recipe;

import com.blazeloader.util.registry.ObjectRegistry;
import com.blazeloader.util.resources.ResourceDirectoryWalker;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.World;

import java.util.*;

public class ApiCrafting {

	private static final Map<Integer, ICraftingManager> instances = new HashMap<Integer, ICraftingManager>();
	private static int nextId = 1;
	
	static {
		instances.put(0, new BLCraftingManager(0, CraftingManager.REGISTRY));
	}
	
	/**
	 * Gets a wrapped instance of the normal CraftingManager.
	 * @return Manager instance of CraftingManager
	 */
	public static ICraftingManager getVanillaCraftingManager() {
		return instances.get(0);
	}
	
	/**
	 * Gets a CraftingManager from the pool by it's unique id.
	 * 
	 * @param id	integer id of the manager you'd like to find.
	 * 
	 * @return Manager or null if not found.
	 */
	public static ICraftingManager getManagerFromId(int id) {
		return instances.get(id);
	}
	
	/**
	 * Creates a brand spanking **new** Crafting Manager.
	 */
	public static ICraftingManager createCraftingManager() {
		return createCraftingManager(ObjectRegistry.createNamespacedRegistry());
	}
	
	private static ICraftingManager createCraftingManager(RegistryNamespaced<ResourceLocation, IRecipe> startingRecipes) {
		int id = nextId++;
		instances.put(id, new BLCraftingManager(id, startingRecipes));
		return instances.get(id);
	}
	
	public static final class BLCraftingManager implements ICraftingManager {
		private final int id;
		private final RegistryNamespaced<ResourceLocation, IRecipe> recipes;
		private int nextRecipeId = 0;
		
		
		private BLCraftingManager(int n, RegistryNamespaced<ResourceLocation, IRecipe> recipes) {
			id = n;
			this.recipes = recipes;
		}
		
		public int getId() {
			return id;
		}
		
	    public ShapedRecipe addRecipe(ResourceLocation name, ItemStack output, Object... input) {
	    	ShapedRecipe result = createShaped(name.toString(), output, false, input);
	    	addRecipe(name, result);
	        return result;
	    }
	    
	    public ShapelessRecipe addShapelessRecipe(ResourceLocation name, ItemStack output, Object... input) {
	    	ShapelessRecipe result = createShapeless(name.toString(), output, false, input);
	    	addRecipe(name, result);
	        return result;
	    }
	    
	    public ReversibleShapedRecipe addReverseRecipe(ResourceLocation name, ItemStack output, Object... input) {
	    	ShapedRecipe result = createShaped(name.toString(), output, true, input);
	    	addRecipe(name, result);
	        return (ReversibleShapedRecipe)result;
	    }
	    
	    public ReversibleShapelessRecipe addReverseShapelessRecipe(ResourceLocation name, ItemStack output, Object... input) {
	    	ShapelessRecipe result = createShapeless(name.toString(), output, true, input);
	    	addRecipe(name, result);
	        return (ReversibleShapelessRecipe)result;
	    }
	    
	    public void addRecipe(ResourceLocation name, IRecipe recipe) {
	    	recipes.register(nextRecipeId++, name, recipe);
	    }
	    
	    public boolean removeRecipe(IRecipe recipe) {
	    	return ObjectRegistry.of(recipes).removeObjectFromRegistry(recipes.getNameForObject(recipe)) != null;
	    }
	    
	    public int removeRecipe(ItemStack result) {
	    	return removeRecipe(result, -1);
	    }
	    
	    public int removeRecipe(ItemStack result, int maxRemovals) {
	    	int count = 0;
	    	for (IRecipe i : recipes) {
	    		if (i.getRecipeOutput() == result) {
	    			count++;
	    			ObjectRegistry.of(recipes).removeObjectFromRegistry(recipes.getNameForObject(i));
	    			if (maxRemovals > 0 && count >= maxRemovals) return count;
	    		}
	    	}
	    	return count;
	    }
	    
	    private ShapedRecipe createShaped(String group, ItemStack output, boolean reverse, Object... input) {
	        String recipe = "";
	        int index = 0;
	        int width = 0;
	        int height = 0;

	        if (input[index] instanceof String[]) {
	            for (String i : (String[])input[index++]) {
	                ++height;
	                width = i.length();
	                recipe += i;
	            }
	        } else {
	            while (input[index] instanceof String) {
	                String line = (String)input[index++];
	                ++height;
	                width = line.length();
	                recipe += line;
	            }
	        }

	        HashMap<Character, ItemStack> stackmap = new HashMap<Character, ItemStack>();
	        while (index < input.length) {
				char var13 = (Character) input[index];
				ItemStack var15 = null;
				if (input[index + 1] instanceof Item) {
	                var15 = new ItemStack((Item)input[index + 1]);
	            } else if (input[index + 1] instanceof Block) {
	                var15 = new ItemStack((Block)input[index + 1], 1, 32767);
	            } else if (input[index + 1] instanceof ItemStack) {
	                var15 = (ItemStack)input[index + 1];
	            }
	            stackmap.put(var13, var15);
	            index += 2;
	        }

	        Ingredient[] stacks = new Ingredient[width * height];
	        for (int i = 0; i < width * height; i++) {
	            char key = recipe.charAt(i);
	            if (stackmap.containsKey(key)) {
	                stacks[i] = Ingredient.fromStacks(stackmap.get(key).copy());
	            }
	        }
	        NonNullList<Ingredient> ingredients = NonNullList.from(Ingredient.EMPTY, stacks);
	        if (reverse) return new ReversibleShapedRecipe(group, width, height, ingredients, output);
	        return new ShapedRecipe(group, width, height, ingredients, output);
	    }
	    
	    private ShapelessRecipe createShapeless(String group, ItemStack output, boolean reverse, Object ... input) {
	    	NonNullList<Ingredient> ingredients = NonNullList.from(Ingredient.EMPTY);
			for (Object obj : input) {
				if (obj instanceof ItemStack) {
					ingredients.add(Ingredient.fromStacks(((ItemStack) obj).copy()));
				} else if (obj instanceof Item) {
					ingredients.add(Ingredient.fromItem((Item)obj));
				} else {
					if (!(obj instanceof Block))
						throw new IllegalArgumentException("Invalid shapeless recipe: unknown type " + obj.getClass().getName() + "!");
					ingredients.add(Ingredient.fromStacks(new ItemStack((Block) obj)));
				}
			}
			if (reverse) return new ReversibleShapelessRecipe(group, output, ingredients);
	        return new ShapelessRecipe(group, output, ingredients);
	    }
	    
	    public ItemStack findMatchingRecipe(InventoryCrafting inventory, World world) {
	        for (IRecipe i : recipes) {
	        	if (i.matches(inventory, world)) return i.getCraftingResult(inventory);
	        }
	        return null;
	    }
	    
	    public NonNullList<Ingredient> findRecipeInput(ItemStack recipeOutput, int width, int height) {
	    	for (IRecipe i : recipes) {
	    		if (i instanceof IReversibleRecipe) {
	    			IReversibleRecipe recipe = ((IReversibleRecipe)i);
	    			if (recipe.matchReverse(recipeOutput, width, height)) return recipe.getRecipeInput();
	    		}
	    	}
	    	return null;
	    }
	    
	    @Override
	    public NonNullList<ItemStack> getUnmatchedInventory(InventoryCrafting inventory, World world) {
	        for (IRecipe i : recipes) {
	            if (i.matches(inventory, world)) return i.getRemainingItems(inventory);
	        }
	        ItemStack[] newInventory = new ItemStack[inventory.getSizeInventory()];
	        for (int i = 0; i < newInventory.length; i++) {
	            newInventory[i] = inventory.getStackInSlot(i);
	        }
	        return NonNullList.from(null, newInventory);
	    }
	    
	    public boolean equals(Object obj) {
			if (obj instanceof ICraftingManager) {
				return ((ICraftingManager) obj).getId() == getId();
			}
	    	if (obj instanceof CraftingManager) {
	    		return recipes.equals(CraftingManager.REGISTRY);
	    	}
	    	if (obj instanceof RegistryNamespaced<?, ?>) {
	    		return obj.equals(recipes);
	    	}
	    	return super.equals(obj);
	    }

		@Override
		public void loadRecipesFromJSON(String path) {
			(new ResourceDirectoryWalker() {
				public void parseJSONEntry(ResourceLocation name, JsonObject jsonObject) {
					String s = JsonUtils.getString(jsonObject, "type");

			        if ("crafting_shaped".equals(s)) {
			        	addRecipe(name, ShapedRecipes.deserialize(jsonObject));
			        } else if ("crafting_shapeless".equals(s)) {
			        	addRecipe(name, ShapelessRecipes.deserialize(jsonObject));
			        } else {
			            throw new JsonSyntaxException("Invalid or unsupported recipe type '" + s + "'");
			        }
				}
			}).tryWalk(path);
		}
	}
}