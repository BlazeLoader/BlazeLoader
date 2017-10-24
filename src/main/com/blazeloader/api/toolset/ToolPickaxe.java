package com.blazeloader.api.toolset;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

public class ToolPickaxe extends ItemPickaxe implements ITool {
    private final ToolsetAttributes attributes;
    
    public ToolPickaxe(ToolsetAttributes material) {
        super(ToolMaterial.WOOD);
        attributes = material;
        super.setMaxDamage(material.getMaxUses());
        efficiencyOnProperMaterial = material.getEfficiencyOnProperMaterial();
        damageVsEntity = material.getDamageVsEntity(1);
    }
    
	@Override
	public ToolsetAttributes getToolAttributes() {
		return attributes;
	}

    @Override
    public int getItemEnchantability() {
        return attributes.getEnchantability();
    }

    @Override
    public String getToolMaterialName() {
        return attributes.toString();
    }

    @Override
    public boolean getIsRepairable(ItemStack repairedItem, ItemStack repairMaterial) {
        return attributes.getIsRepairable(repairMaterial);
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
    	return attributes.getAttributeModifiers(slot, super.getItemAttributeModifiers(slot), ATTACK_DAMAGE_MODIFIER, ATTACK_SPEED_MODIFIER, damageVsEntity, attackSpeed, "Tool modifier");
    }

    @Override
    public boolean canHarvestBlock(IBlockState state) {
    	Block block = state.getBlock();
        if (block == Blocks.OBSIDIAN) return attributes.getHarvestLevel() > 2;
        
        if (block == Blocks.DIAMOND_BLOCK || block == Blocks.DIAMOND_ORE      || 
    		block == Blocks.EMERALD_BLOCK || block == Blocks.EMERALD_ORE      ||
    		block == Blocks.GOLD_BLOCK    || block == Blocks.GOLD_ORE         ||
    		block == Blocks.REDSTONE_ORE  || block == Blocks.LIT_REDSTONE_ORE) {
        	return attributes.getHarvestLevel() > 1;
        }
        
		if (block == Blocks.IRON_BLOCK || block == Blocks.IRON_ORE  ||
			block == Blocks.LAPIS_BLOCK || block == Blocks.LAPIS_ORE) {
			return attributes.getHarvestLevel() > 0;
		}
    	
    	Material material = state.getMaterial();
        return material == Material.ROCK  || material == Material.IRON || material == Material.ANVIL;
    }
}
