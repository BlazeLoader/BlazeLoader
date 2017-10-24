package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.blazeloader.api.gui.IGuiResponder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IContainerListener;

@Mixin(EntityPlayerMP.class)
public abstract class MEntityPlayerMP extends EntityPlayer implements IContainerListener, IGuiResponder {
	public MEntityPlayerMP() {super(null, null);}
	
	@Shadow public abstract void getNextWindowId();
	
	@Accessor("currentWindowId") public abstract int getCurrentWindowId();
	
	public int incrementWindowId() {
		getNextWindowId();
		return getCurrentWindowId();
	}
}
