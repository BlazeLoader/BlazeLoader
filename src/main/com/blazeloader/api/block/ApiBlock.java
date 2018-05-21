package com.blazeloader.api.block;

import com.blazeloader.api.item.ApiItem;
import com.blazeloader.util.registry.Registry;
import com.blazeloader.util.registry.RegistryIdManager;
import com.blazeloader.util.version.Versions;
import com.google.common.collect.ImmutableList;
import com.mumfrey.liteloader.client.ducks.IMutableRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemMultiTexture.Mapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Api for block-specific functions
 */
public class ApiBlock {
	private static final RegistryIdManager REGISTRY = new RegistryIdManager(() -> Registry.of(Block.REGISTRY));
	
    /**
     * 
     * Registers the given block as 'flammable' by fire.
     * 
     * @param block				The block to register
     * @param encouragement		How likely it is that this block will spread fire
     * @param flamability		How flamable this block is
     * 
     * @return the block for simplicity
     */
    public static <T extends Block> T registerFireInfo(T block, int encouragement, int flamability) {
    	Blocks.FIRE.setFireInfo(block, encouragement, flamability);
    	return block;
    }
    
    /**
     * Utility method to register a block.
     * Will register the block together with an item and assign unlocalized names for both with the following format:
     * <p>
     * {mod}.{block name}
     *
     * @param id        The ID of the block.
     * @param mod        The domain used for this mod. eg. "minecraft:stone" has the domain "minecraft"
     * @param name    The name to register the block as. Will be set as the name for both the block and the item
     * @param block    The block to add
     *
     * @return the block for simplicity
     */
    @SuppressWarnings("unchecked")
	public static <T extends Block> T quickRegisterBlock(int id, String mod, String name, T block) {
    	return registerBlock(id, new ResourceLocation(mod, name), (T) block.setUnlocalizedName(mod + "." + name), (ItemBlock)(new ItemBlock(block)).setUnlocalizedName(mod + "." + name));
    }

    /**
     * Registers and initialises a block in the block registry together with an item for it.
     *
     * @param id        The ID of the block.
     * @param mod       The domain used for this mod. eg. "minecraft:stone" has the domain "minecraft"
     * @param name      The name to register the block as
     * @param block     The block to add
     * @param itemBlock The item to be used for this block
     * @return the block for simplicity
     */
    public static <T extends Block> T registerBlock(int id, String mod, String name, T block, ItemBlock itemBlock) {
        return registerBlock(id, new ResourceLocation(mod, name), block, itemBlock);
    }

    /**
     * Registers and initialises a block in the block registry together with an item for it.
     *
     * @param id        The ID of the block.
     * @param name      The name to register the block as
     * @param block     The block to add
     * @param itemBlock The item to be used for this block
     * @return the block for simplicity
     */
    public static <T extends Block> T registerBlock(int id, ResourceLocation name, T block, ItemBlock itemBlock) {
        registerBlock(id, name, block);
        ApiItem.registerItemBlock(block, itemBlock);
        return block;
    }

    /**
     * Registers and initialises a block in the block registry.
     *
     * @param id    The ID of the block.
     * @param mod	The domain used for this mod. eg. "minecraft:stone" has the domain "minecraft"
     * @param name  The name to register the block as
     * @param block The block to add
     *
     * @return the block for simplicity
     */
    public static <T extends Block> T registerBlock(int id, String mod, String name, T block) {
        return registerBlock(id, new ResourceLocation(mod, name), block);
    }
    
    /**
     * Registers and initialises a block in the block registry.
     *
     * @param id    The ID of the block.
     * @param name  The name to register the block as
     * @param block The block to add
     *
     * @return the block for simplicity
     */
    public static <T extends Block> T registerBlock(int id, ResourceLocation name, T block) {
        injectBlock(id, name, block);
        applyPostRegisterConditions(block);
        return block;
    }
    
    /**
     * Registers names for all the variants the given block has.
     * 
     * @param block		The block
     * @param variants	Names for all the item's variants
     *
     * @return the block for simplicity
     */
    public static <T extends Block> T registerBlockVarientNames(T block, String... variants) {
        ApiItem.registerItemVariantNames(ApiItem.getItemByBlock(block), variants);
        return block;
    }
    
    /**
     * Replaces an existing block with the given block and gives it an
     *  ItemMultiTexture with the given function for determining what name to display.
     * <p>
     * Works best if the replacement block supports all the states of the one it is replacing.
     * @param original	Original block to replace
     * @param block		New block to insert
     * @param nameFunc	Function used by the itemblock to get the display name
     * @param <K> The type of the block you are replacing
     * @param <V> The type of the new block you are replacing it with. Must extend the original.
     * 
     * @return the block for simplicity
     */
    public static <K extends Block,V extends K> V replaceBlock(K original, V block, Mapper nameFunc) {
    	return replaceBlock(original, block, new ItemMultiTexture(block, block, nameFunc));
    }
    
    /**
     * Replaces an existing block with the given block and gives it the provided itemblock.
     * <p>
     * Works best if the replacement block supports all the states of the one it is replacing.
     * @param original	Original block to replace
     * @param block		New block to insert
     * @param item		An item to be used for the replacement block
     * @param <K> The type of the block you are replacing
     * @param <V> The type of the new block you are replacing it with. Must extend the original.
     * 
     * @return the block for simplicity
     */
    public static <K extends Block,V extends K> V replaceBlock(K original, V block, ItemBlock item) {
    	replaceBlock(original, block);
    	ApiItem.registerItemBlock(block, item);
    	return block;
    }
    
    /**
     * Replaces an existing block with the given block.
     * <p>
     * Works best if the replacement block supports all the states of the one it is replacing.
     * @param original	Original block to replace
     * @param block		New block to insert
     * @param <K> The type of the block you are replacing
     * @param <V> The type of the new block you are replacing it with. Must extend the original.
     * 
     * @return the block for simplicity
     */
    @SuppressWarnings("deprecation")
	public static <K extends Block,V extends K> V replaceBlock(K original, V block) {
    	
    	Map<Integer, IBlockState> builtStates = new HashMap<Integer, IBlockState>();
    	
    	for (IBlockState original_state : (List<IBlockState>)original.getBlockState().getValidStates()) {
    		int original_metadata = getBlockId(original) << 4 | original.getMetaFromState(original_state);
    		builtStates.put(original_metadata, original_state);
    	}
    	
    	for (IBlockState state : (List<IBlockState>)block.getBlockState().getValidStates()) {
    		int metadata = getBlockId(original) << 4 | block.getMetaFromState(state);
    		builtStates.put(metadata, state);
    	}
    	
    	for (Map.Entry<Integer, IBlockState> i : builtStates.entrySet()) {
    		if (i.getValue().getBlock() == original) {
    			Block.BLOCK_STATE_IDS.put(block.getStateFromMeta(i.getKey()), i.getKey());
    		}
    		Block.BLOCK_STATE_IDS.put(i.getValue(), i.getKey());
    	}
    	
    	injectBlock(getBlockId(original), getBlockName(original), block);
    	if (Versions.isClient()) {
    		com.blazeloader.api.client.render.ApiRenderBlock.swapoutBlockModels(original, block);
    	}
    	return block;
    }
    
    /**
     * Registers a block in the block registry.
     * <p>
     * Is like registerBlock but does not perform any blockstate initialisation.
     *
     * @param id    The ID of the block.
     * @param mod	The domain used for this mod. eg. "minecraft:stone" has the domain "minecraft"
     * @param name  The name to register the block as
     * @param block The block to add
     */
    public static void injectBlock(int id, String mod, String name, Block block) {
    	injectBlock(id, new ResourceLocation(mod, name), block);
    }
    
    /**
     * Registers a block in the block registry.
     * <p>
     * Is like registerBlock but does not perform any blockstate initialisation.
     *
     * @param id    The ID of the block.
     * @param name  The name to register the block as
     * @param block The block to add
     */
    @SuppressWarnings("unchecked")
	public static void injectBlock(int id, ResourceLocation name, Block block) {
    	boolean exists = getBlockRegistry().containsKey(name);
    	if (exists) {
    		Block existing = getBlockRegistry().getObject(name);
    		((IMutableRegistry<ResourceLocation, Block>)getBlockRegistry()).removeObjectFromRegistry(name);
    		try {
	    		for (Field field : Blocks.class.getDeclaredFields()) {
	                if (field.get(null).equals(existing)) {
	                    field.set(null, block);
	                }
	            }
    		} catch (Exception e) {}
    	}
    	getBlockRegistry().register(id, name, block);
    }
    
    private static void applyPostRegisterConditions(Block block) {
    	for (IBlockState state : (ImmutableList<IBlockState>)block.getBlockState().getValidStates()) {
            int metadata = getBlockRegistry().getIDForObject(block) << 4 | block.getMetaFromState(state);
            Block.BLOCK_STATE_IDS.put(state, metadata);
        }
    }
    
    /**
     * Checks if the given id is safe to use.
     * <p>
     * An id is determined as 'free' only when neither the Blocks registry nor
     * the Items registry contain mappings for the given id.
     * <p>
     * If you wish to only check for the existance of a block use isHasBlock.
     * 
     * @param id	Id to check
     * 
     * @return true if it is free, false otherwise
     */
    public static boolean isIdFree(int id) {
    	return !REGISTRY.hasId(id) && ApiItem.isIdFree(id);
    }
    
    /**
     * Gets a free block ID.
     * <p>
     * An id is determined as 'free' only when neither the Blocks registry nor
     * the Items registry contain mappings for the given id.
     * 
     * @return return a free block ID.
     */
    public static int getFreeBlockId() {
    	return REGISTRY.getNextFreeId();
    }
    
    /**
     * Gets the name of a block.
     *
     * @param block The block to get the name for
     * @return Return a string of the name belonging to param block
     */
    public static ResourceLocation getBlockName(Block block) {
        return getBlockRegistry().getNameForObject(block);
    }
    
    /**
     * Gets the name of a block.
     *
     * @param block The block to get the name for
     * @return Return a string of the name belonging to param block
     */
    public static String getStringBlockName(Block block) {
        return getBlockName(block).toString();
    }
    
    /**
     * Gets a block by it's name
     *
     * @param name The name of the block
     * @return Gets the block defined by param name.
     */
    public static Block getBlockByName(String name) {
        return Block.getBlockFromName(name);
    }

    /**
     * Gets a block by it's BlockId.
     *
     * @param id The ID of the block.
     * @return Return the block defined by param id.
     */
    public static Block getBlockById(int id) {
        return Block.getBlockById(id);
    }

    /**
     * Gets a block by it's item version.
     *
     * @param item The item to get the block from.
     * @return Return the block associated with param item.
     */
    public static Block getBlockByItem(Item item) {
        return Block.getBlockFromItem(item);
    }
    
    /**
     * Gets the id associated with the given block.
     * 
     * @param block The block
     * 
     * @return the block's id
     */
    public static int getBlockId(Block block) {
    	return Block.getIdFromBlock(block);
    }
    
    public static RegistryNamespacedDefaultedByKey<ResourceLocation, Block> getBlockRegistry() {
    	return Block.REGISTRY;
    }
}
