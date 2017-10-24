package com.blazeloader.api.client.render;

import java.util.Set;

import com.blazeloader.api.client.ApiClient;
import com.blazeloader.event.mixin.client.MModelBakery;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ApiRenderItem {
	
	/**
	 * Registers an item with the render engine to use a specific model. 
	 * <p>
	 * Same as:
	 * <br><code>registerItem(item, 0, identifier);</code>
	 * 
	 * @param item			The item to register
	 * @param identifier	String identifier for the model that the game must use
	 */
	public static void registerItem(Item item, String identifier) {
		registerItem(item, 0, identifier);
	}
		
	/**
	 * Registers an item with the render engine to use a specific model for the given item data. 
	 * 
	 * @param item			The item to register
	 * @param subType		Data for the item
	 * @param identifier	String identifier for the model that the game must use
	 */
	public static void registerItem(Item item, int subType, String identifier) {
		ApiClient.getRenderItem().getItemModelMesher().register(item, subType, new ModelResourceLocation(identifier, "inventory"));
	}
	
	/**
	 * 
	 * Registers an item with a custom ItemMeshDefinition.
	 * <p>
	 * The item mesh definition is used for deciding which model an item must use.
	 * A good example is potions, which gives back the game engine a model for a splash potion of a regular one depending on the type of potion being rendered.
	 * 
	 * @param item	The item to register
	 * @param mesh	A mesh definition used to render the model for this item
	 */
	public static void registerItem(Item item, ItemMeshDefinition mesh) {
		ApiClient.getRenderItem().getItemModelMesher().register(item, mesh);
	}
	
	/**
	 * Registers a texture to be added to the block/items atlas without need for an item.
	 * @param resources ResourceLocation to add.
	 */
	public static void registerBuiltInTextures(ResourceLocation... resources) {
		Set<ResourceLocation> s = MModelBakery.getBuiltinTextures();
		for (ResourceLocation i : resources) {
			if (!s.contains(i)) s.add(i);
		}
	}
}
