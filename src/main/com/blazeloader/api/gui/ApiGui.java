package com.blazeloader.api.gui;

import com.blazeloader.event.mixin.common.MCreativeTabs;
import com.blazeloader.util.JSArrayUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * GUI functions
 */
public class ApiGui {
	protected static final CreativeTabs[] originalCreativeTabsArray = getCreativeTabsRegistry();
	
	/**
	 * Opens a mod added container.
	 * 
	 * @param player      The player to open the container for
	 * @param inventory	  The tile entity providing the inventory
	 */
    public static void openContainer(EntityPlayerMP player, IModInventory inventory) {
        if (player.openContainer != player.inventoryContainer) player.closeScreen();
        
        if (inventory instanceof IModLockableInventory) {
            IModLockableInventory lockable = (IModLockableInventory)inventory;
            if (lockable.isLocked() && !player.canOpen(lockable.getLockCode()) && !player.isSpectator()) {
            	player.connection.sendPacket(new SPacketChat(new TextComponentTranslation(lockable.getLockMessageString(), inventory.getDisplayName()), ChatType.GAME_INFO));
                player.connection.sendPacket(new SPacketSoundEffect(lockable.getLockSound(), SoundCategory.BLOCKS, player.posX, player.posY, player.posZ, 1.0F, 1.0F));
                return;
            }
        }
        
        int windowId = ((IGuiResponder)player).incrementWindowId();
        Container container = inventory.createContainer(player.inventory, player);
        player.connection.sendPacket(new SPacketOpenWindow(windowId, inventory.getGuiID(), inventory.getDisplayName(), inventory.getSizeInventory()));
        player.openContainer = container;
        player.openContainer.windowId = windowId;
        player.openContainer.addListener(player);
    }
    
    /**
     * Registers an additional creative tab for a mod.
     * 
     * @param mod				Mod name
     * @param name				Name of creative tab
     * @param iconItem			Item to be displayed on the tab
     * @param iconMetadata		Metadata to be used with the item
     * @return	A CreativeTabs instance.
     */
    public static CreativeTabs registerCreativeTab(String mod, String name, Item iconItem, int iconMetadata) {
    	return registerCreativeTab(mod, name, new ItemStack(iconItem, 1, iconMetadata));
    }
    
    /**
     * Registers an additional creative tab for a mod.
     * 
     * @param mod				Mod name
     * @param name				Name of creative tab
     * @param stack				The itemstack to be displayed on the tab
     * @return	A CreativeTabs instance.
     */
    public static CreativeTabs registerCreativeTab(String mod, String name, ItemStack stack) {
    	MCreativeTabs.setCreativeTabsArray(JSArrayUtils.push(getCreativeTabsRegistry(), (CreativeTabs)null));
    	return new CreativeTabs(getCreativeTabsRegistry().length - 1, mod + "." + name) {
			public ItemStack getTabIconItem() {
				return stack;
			}
			public int getTabColumn() {
		        return (getTabIndex() - 12) % 5;
		    }
			public boolean isTabInFirstRow() {
				return (getTabIndex() - 12) % 10 < 5;
			}
    	};
    }
    
    public static CreativeTabs[] getCreativeTabsRegistry() {
    	return CreativeTabs.CREATIVE_TAB_ARRAY;
    }
    
    public static CreativeTabs getCreativeTabByName(String name) {
    	for (CreativeTabs i : getCreativeTabsRegistry()) {
    		if (i.getTabLabel().contentEquals(name)) return i;
    	}
    	return CreativeTabs.SEARCH;
    }
}
