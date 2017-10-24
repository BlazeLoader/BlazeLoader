package com.blazeloader.event.listeners.args;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

/**
 * Contains args for a ContainerOpenedEvent
 */
public class ContainerOpenedEventArgs {
	public final boolean hasSlots;
	public final int slotsCount;
	
	public final String inventoryId;
	public final ITextComponent inventoryTitle;

    public final int posX;
    public final int posY;
    public final int posZ;

    public ContainerOpenedEventArgs(EntityPlayer player, SPacketOpenWindow packet) {
        hasSlots = packet.hasSlots();
        slotsCount = packet.getSlotCount();
        inventoryId = packet.getGuiId();
        inventoryTitle = packet.getWindowTitle();
        
        posX = MathHelper.floor(player.posX);
        posY = MathHelper.floor(player.posY);
        posZ = MathHelper.floor(player.posZ);
    }
}