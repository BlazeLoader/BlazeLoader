package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.blazeloader.util.data.INBTWritable;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

@Mixin({TileEntity.class, Entity.class, ItemStack.class})
public abstract class MNBTWritable implements INBTWritable {
	
	@Shadow(prefix = "te")
	public abstract void te$readFromNBT(NBTTagCompound compound);
  //public abstract void en$readFromNBT(NBTTagCompound compound);
	
	@Shadow(prefix = "te")
	public abstract NBTTagCompound te$writeToNBT(NBTTagCompound compound);
  //public abstract NBTTagCompound en$writeToNBT(NBTTagCompound compound);
  //public abstract NBTTagCompound is$writeToNBT(NBTTagCompound nbt);
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		te$writeToNBT(tag);
	}
	
	@Override
	public INBTWritable readFromNBT(NBTTagCompound tag) {
		if ((Object)this instanceof ItemStack) {
			return (INBTWritable)(Object)new ItemStack(tag);
		}
		te$readFromNBT(tag);
		return (INBTWritable)(Object)this;
	}

}
