package com.blazeloader.api.client.render;

import java.util.HashMap;
import java.util.Map;

import com.blazeloader.api.block.ApiBlock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class BlockRenderRegistry {
	protected static final Map<Block, String> specialRenderBlocks = new HashMap<Block, String>();
	
	public static String lookupTexture(Block block) {
		if (specialRenderBlocks.containsKey(block)) {
			return specialRenderBlocks.get(block);
		}
		return null;
	}
	
	public static boolean isTileEntityRendered(Block block) {
		return block != null && block instanceof ITileEntityRendered;
	}
	
	public static boolean tryRenderTileEntity(ItemStack stack) {
		Block block = ApiBlock.getBlockByItem(stack.getItem());
		if (!isTileEntityRendered(block)) return false;
		return doRenderTileEntity(block, stack);
	}
	
	public static boolean doRenderTileEntity(Block block, ItemStack stack) {
		ITileRenderer<?> render = ((ITileEntityRendered<?>)block).getTileRenderer();
		if (render == null) return false;
		if (!render.customRenderPass(stack)) {
			TileEntity tile = render.getTileEntityForRender(stack); 
			if (tile == null) return false;
	        ApiRenderTileEntity.renderTileEntity(tile, 0, 0, 0, 0, -1);
		}
        return true;
	}
}
