package com.blazeloader.api.toolset;

import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ToolSword extends ItemSword implements ITool {
    private final ToolsetAttributes attributes;

    private float damageValue;

    public ToolSword(ToolsetAttributes material) {
        super(ToolMaterial.WOOD);
        attributes = material;
        setMaxDamage(material.getMaxUses());
        damageValue = material.getDamageVsEntity(3);
    }

	@Override
	public ToolsetAttributes getToolAttributes() {
		return attributes;
	}

    @Override
    public float getDamageVsEntity() {
        return attributes.getDamageVsEntity(0);
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
    	return attributes.getAttributeModifiers(slot, super.getItemAttributeModifiers(slot), ATTACK_DAMAGE_MODIFIER, ATTACK_SPEED_MODIFIER, damageValue, -2.4000000953674316D, "Weapon modifier");
    }
}
