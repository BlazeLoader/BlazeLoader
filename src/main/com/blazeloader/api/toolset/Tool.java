package com.blazeloader.api.toolset;

import java.util.Set;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

/**
 * Base class for a tool. Can be used by mods to add their own types of tools.
 */
public abstract class Tool extends ItemTool implements ITool {
	private final ToolsetAttributes attributes;
	
	private final float damageValue;
	
	protected Tool(float attackDamage, float speed, ToolsetAttributes attributes, Set<Block> effectiveBlocks) {
		super(speed, attackDamage, ToolMaterial.WOOD, effectiveBlocks);
		this.attributes = attributes;
		this.damageValue = attackDamage;
	}
	
	public ToolsetAttributes getToolAttributes() {
		return attributes;
	}
	
    @Override
    public int getItemEnchantability() {
        return getToolAttributes().getEnchantability();
    }

    @Override
    public String getToolMaterialName() {
        return getToolAttributes().toString();
    }

    @Override
    public boolean getIsRepairable(ItemStack repaired, ItemStack repairMaterial) {
        return getToolAttributes().getIsRepairable(repairMaterial);
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
        return getToolAttributes().getAttributeModifiers(slot, super.getItemAttributeModifiers(slot), ATTACK_DAMAGE_MODIFIER, ATTACK_SPEED_MODIFIER, damageValue, attackSpeed, "Tool modifier");
    }
}
