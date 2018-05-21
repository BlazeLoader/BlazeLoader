package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Mixin;

import com.blazeloader.util.data.INBTWritable;
import com.blazeloader.util.data.NBTType;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.Village;
import net.minecraft.world.storage.WorldSavedData;

@Mixin({TileEntity.class, Entity.class, ItemStack.class,
	Village.class, MerchantRecipe.class,
	InventoryPlayer.class, WorldSavedData.class})
public abstract class MNBTWritable implements INBTWritable {
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		Object o = this;
		if (o instanceof TileEntity) {
			((TileEntity)o).writeToNBT(tag);
		} else if (o instanceof Entity) {
			((Entity)o).writeToNBT(tag);
		} else if (o instanceof ItemStack) {
			((ItemStack)o).writeToNBT(tag);
		} else if (o instanceof Village) {
			((Village)o).writeVillageDataToNBT(tag);
		} else if (o instanceof MerchantRecipe) {
			tag.setTag("merchant", ((MerchantRecipe)o).writeToTags());
		} else if (o instanceof InventoryPlayer) {
			NBTTagList list = new NBTTagList();
			((InventoryPlayer)o).writeToNBT(list);
			tag.setTag("inventory", list);
		} else if (o instanceof WorldSavedData) {
			((WorldSavedData)o).writeToNBT(tag);
		}
	}
	
	@Override
	public INBTWritable readFromNBT(NBTTagCompound tag) {
		if ((Object)this instanceof ItemStack) {
			return (INBTWritable)(Object)new ItemStack(tag);
		}
		Object o = this;
		if (o instanceof TileEntity) {
			((TileEntity)o).readFromNBT(tag);
		} else if (o instanceof Entity) {
			((Entity)o).readFromNBT(tag);
		} else if (o instanceof Village) {
			((Village)o).readVillageDataFromNBT(tag);
		} else if (o instanceof MerchantRecipe) {
			((MerchantRecipe)o).readFromTags(tag.getCompoundTag("merchant"));
		} else if (o instanceof InventoryPlayer) {
			NBTTagList list = tag.getTagList("inventory", NBTType.COMPOUND.value());
			((InventoryPlayer)o).writeToNBT(list);
			tag.setTag("inventory", list);
		} else if (o instanceof WorldSavedData) {
			((WorldSavedData)o).readFromNBT(tag);
		}
		return (INBTWritable)o;
	}

}
