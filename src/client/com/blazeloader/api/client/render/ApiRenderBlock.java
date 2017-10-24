package com.blazeloader.api.client.render;

import com.blazeloader.api.client.ApiClient;
import com.blazeloader.api.client.privileged.IMinecraft;
import com.blazeloader.event.mixin.client.MBlockStateMapper;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.item.Item;

public class ApiRenderBlock {
	
	/**
	 * Registers a block with a fallback texture for use with particles.
	 * <p>
	 * Useful for blocks like chests that are not rendered with .json models.
	 * 
	 * @param block			The block to register
	 * @param texturePath	Texture to use. eg. "minecraft:blocks/planks_oak" is used for chests and signs.
	 */
	public static void registerFallbackTexture(Block block, String texturePath) {
		BlockRenderRegistry.specialRenderBlocks.put(block, texturePath);
	}
	
	/**
	 * Registers a block with the render engine to use a specific model. 
	 * <p>
	 * Same as:
	 * <br><code>registerBlock(block, 0, identifier);</code>
	 * 
	 * @param item			the block to register
	 * @param subType		metadata value
	 * @param identifier	String identifier for the model that the game must use
	 */
	public static void registerBlock(Block block, String identifier) {
		registerBlock(block, 0, identifier);
	}
	
	/**
	 * Registers a block with the render engine to use a specific model for the given data value. 
	 * 
	 * @param item			the block to register
	 * @param subType		metadata value
	 * @param identifier	String identifier for the model that the game must use
	 */
	public static void registerBlock(Block block, int subType, String identifier) {
		ApiRenderItem.registerItem(Item.getItemFromBlock(block), subType, identifier);
	}
	
    /**
     * Registers a mapper for the given block that takes a given BlockState and gives back a prebaked model.
     *  
     * @param block		Block for rendering
     * @param mapper	IStateMapper to provide the models
     */
    public static void registerBlockModelMapper(Block block, IStateMapper mapper) {
    	getModelManager().getBlockModelShapes().registerBlockWithStateMapper(block, mapper);
    }
    
    /**
     * Registers the given blocks with the game to be rendered by some other managed code.
     * <p>
     * Currently used for air, water, lava, pistons, heads, banners, and signs.
     * 
     * @param blocks The blocks to register.
     */
    public static void registerBuiltInBlocks(Block... blocks) {
    	getModelManager().getBlockModelShapes().registerBuiltInBlocks(blocks);
    }
    
    /**
     * Remaps the models from an already registered block onto the given one.
     * 
     * @param original	Original block
     * @param block		Block to assign the models to
     */
    public static void swapoutBlockModels(Block original, Block block) {
    	BlockModelShapes mapper = getModelManager().getBlockModelShapes();
    	mapper.getBlockStateMapper().registerBlockStateMapper(block, ((MBlockStateMapper)mapper.getBlockStateMapper()).getBlockStateMap().get(original));
    	mapper.reloadModels();
    }
    
    private static ModelManager getModelManager() {
    	return ((IMinecraft)ApiClient.getClient()).getModelManager();
    }
}
