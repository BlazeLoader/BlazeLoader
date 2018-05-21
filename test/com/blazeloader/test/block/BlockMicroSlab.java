package com.blazeloader.test.block;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.item.ItemStack;

public class BlockMicroSlab extends BlockSlab {

	public BlockMicroSlab(Material materialIn) {
		super(materialIn);
	}

	@Override
	public String getUnlocalizedName(int meta) {
		return null;
	}

	@Override
	public boolean isDouble() {
		return false;
	}

	@Override
	public IProperty<?> getVariantProperty() {
		return null;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
		return null;
	}

}
