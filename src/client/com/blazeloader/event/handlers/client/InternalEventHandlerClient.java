package com.blazeloader.event.handlers.client;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.blazeloader.api.block.ApiBlock;
import com.blazeloader.api.client.render.BlockRenderRegistry;
import com.blazeloader.api.entity.IMousePickHandler;
import com.blazeloader.event.handlers.EventHandler;
import com.blazeloader.event.mixin.Mix;

/**
 * Event handler for events that are not passed to mods, but rather to BL itself
 */
public class InternalEventHandlerClient {
	public static void eventDispatchKeypresses(Minecraft sender) {
		if (EventHandler.inventoryEventHandlers.size() > 0) {
			if (sender == null) return;
			if (sender.player != null && !sender.player.isSpectator()) {
				if (sender.currentScreen == null || sender.currentScreen.allowUserInput) {
					for (int i = 0; i < sender.gameSettings.keyBindsHotbar.length; i++) {
			            if (sender.gameSettings.keyBindsHotbar[i].isPressed()) {
		                    if (EventHandler.inventoryEventHandlers.all().onSlotSelectionChanged(sender.player, sender.player.inventory.getCurrentItem(), i)) {
		                    	KeyBinding.onTick(sender.gameSettings.keyBindsHotbar[i].getKeyCode());
		                    }
		                    break;
		                }
		            }
		        }
			}
		}
	}
	
    public static void eventGetTexture(BlockModelShapes sender, CallbackInfoReturnable<TextureAtlasSprite> info, IBlockState state) {
    	Block block = state.getBlock();
        IBakedModel model = sender.getModelForState(state);
        ModelManager manager = sender.getModelManager();
        if (model == null || model == manager.getMissingModel()) {
        	String texture = BlockRenderRegistry.lookupTexture(block);
        	if (texture != null) info.setReturnValue(manager.getTextureMap().getAtlasSprite(texture));
        }
    }
    
    public static void eventMiddleClickMouse(Minecraft sender, CallbackInfo info) {
    	IMousePickHandler handler = null;
    	boolean creative = sender.player.capabilities.isCreativeMode;
    	
    	if (sender.objectMouseOver != null) {
    		if (sender.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
	    		Entity entity = sender.objectMouseOver.entityHit;
	    		if (entity instanceof IMousePickHandler) {
	    			handler = (IMousePickHandler)entity;
	    		}
    		} else if (creative && sender.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
    			TileEntity tileentity = sender.world.getTileEntity(sender.objectMouseOver.getBlockPos());
    			if (tileentity != null && tileentity instanceof IMousePickHandler) {
    				handler = (IMousePickHandler)tileentity;
    			}
    		}
    	}
    	
    	if (handler != null) {
	    	ItemStack stack = handler.onPlayerMiddleClick(sender.player);
	    	if (stack != null && !stack.isEmpty()) {
				InventoryPlayer inventory = sender.player.inventory;
				
				int i = inventory.getSlotFor(stack);
				
				if (creative) {
					inventory.setPickedItemStack(stack);
	                sender.playerController.sendSlotPacket(sender.player.getHeldItem(EnumHand.MAIN_HAND), 36 + inventory.currentItem);
	            } else if (i > -1) {
                    if (InventoryPlayer.isHotbar(i)) {
                        inventory.currentItem = i;
                    } else {
                        sender.playerController.pickItem(i);
                    }
                }
				info.cancel();
			}
    	}
    }
    
    public static void eventRenderByItem(CallbackInfo info, ItemStack itemStack) {
    	Mix.intercept(BlockRenderRegistry.tryRenderTileEntity(itemStack), info);
    }
    
    public static void eventRenderItem(CallbackInfo info, ItemStack stack, IBakedModel model) {
    	Block block = ApiBlock.getBlockByItem(stack.getItem());
        if (BlockRenderRegistry.isTileEntityRendered(block)) {
        	GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            Mix.intercept(BlockRenderRegistry.doRenderTileEntity(block, stack), info);
            GlStateManager.popMatrix();
        }
    }
}