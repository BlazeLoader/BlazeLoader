package com.blazeloader.api.toolset;

import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;

public class ToolSpade extends ItemSpade implements ITool {
    private final ToolsetAttributes attributes;
    
    public ToolSpade(ToolsetAttributes material) {
        super(ToolMaterial.WOOD);
        attributes = material;
        super.setMaxDamage(material.getMaxUses());
        efficiencyOnProperMaterial = material.getEfficiencyOnProperMaterial();
        damageVsEntity = material.getDamageVsEntity(1.5f);
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
    public boolean getIsRepairable(ItemStack repaired, ItemStack repairMaterial) {
        return attributes.getIsRepairable(repairMaterial);
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
    	return attributes.getAttributeModifiers(slot, super.getItemAttributeModifiers(slot), ATTACK_DAMAGE_MODIFIER, ATTACK_SPEED_MODIFIER, damageVsEntity, attackSpeed, "Tool modifier");
    }
}
