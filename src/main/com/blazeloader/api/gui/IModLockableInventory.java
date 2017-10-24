package com.blazeloader.api.gui;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.ILockableContainer;

public interface IModLockableInventory extends ILockableContainer {
	
	/**
	 * Used to get the localization string for the message to be printed when a player fails to open this gui.
	 * <p>The default string used by chests is "container.isLocked"
	 * 
	 * @return String of unlocalized message
	 */
	public String getLockMessageString();
	
	/**
	 * Used to get the sound played when a player fails to open this gui.
	 * <p>The default sound used by chests is "random.door_close"
	 * 
	 * @return String id of the sound
	 * @deprecated Use getLockSound instead.
	 */
	@Deprecated
	public String getLockSoundString();
	
	/**
	 * Used to get the sound played when a player fails to open this gui.
	 * <p>The default sound used by chests is "random.door_close"
	 * 
	 * @see SoundEvents
	 * 
	 * @return SoundEvent of the sound
	 */
	public default SoundEvent getLockSound() {
		return SoundEvent.REGISTRY.getObject(new ResourceLocation(getLockSoundString()));
	}
}