package com.blazeloader.api.toolset;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class ToolsetAttributes {
	
	private static final HashMap<ToolMaterial, ToolsetAttributes> conversionMapping = new HashMap<ToolMaterial, ToolsetAttributes>();
	
    public static final ToolsetAttributes
            WOOD = new ToolsetAttributes(ToolMaterial.WOOD, -3.2f),
            STONE = new ToolsetAttributes(ToolMaterial.STONE, -3.2f),
            IRON = new ToolsetAttributes(ToolMaterial.IRON, -3.1f),
            DIAMOND = new ToolsetAttributes(ToolMaterial.DIAMOND, -3),
            EMERALD = new ToolsetAttributes("EMERALD", Items.EMERALD, 3, 1561, 8, 3, -3, 10),
            GOLD = new ToolsetAttributes(ToolMaterial.GOLD, -3);

    private final String string;
    private final int hl;
    private final int uses;
    private final float efficiencyOnProperMaterial;
    private final float damageVsEntity;
    private final float attackSpeed;
    private final int enchant;
    
    private final Item item;
    private final int data;

    public ToolsetAttributes(String name, Item materialItem, int harvestLevel, int maxUses, float efficiency, float damage, float speed, int enchantability) {
        string = name;
        hl = harvestLevel;
        uses = maxUses;
        efficiencyOnProperMaterial = efficiency;
        damageVsEntity = damage;
        attackSpeed = speed;
        enchant = enchantability;
        item = materialItem;
        data = -1;
    }
    
    public ToolsetAttributes(String name, Block materialItem, int harvestLevel, int maxUses, float efficiency, float damage, float speed, int enchantability) {
        string = name;
        hl = harvestLevel;
        uses = maxUses;
        efficiencyOnProperMaterial = efficiency;
        damageVsEntity = damage;
        attackSpeed = speed;
        enchant = enchantability;
        item = Item.getItemFromBlock(materialItem);
        data = -1;
    }
    
    public ToolsetAttributes(String name, ItemStack materialItem, int harvestLevel, int maxUses, float efficiency, float damage, float speed, int enchantability) {
        string = name;
        hl = harvestLevel;
        uses = maxUses;
        efficiencyOnProperMaterial = efficiency;
        damageVsEntity = damage;
        attackSpeed = speed;
        enchant = enchantability;
        item = materialItem.getItem();
        data = materialItem.getItemDamage();
    }
    
    private ToolsetAttributes(ToolMaterial material, float speed) {
    	string = material.name();
    	hl = material.getHarvestLevel();
    	uses = material.getMaxUses();
    	efficiencyOnProperMaterial = material.getEfficiencyOnProperMaterial();
    	damageVsEntity = material.getDamageVsEntity();
    	attackSpeed = speed;
    	enchant = material.getEnchantability();
    	item = material.getRepairItem();
    	data = -1;
    	conversionMapping.put(material, this);
    }

    public int getMaxUses() {
        return uses;
    }

    public float getEfficiencyOnProperMaterial() {
        return efficiencyOnProperMaterial;
    }

    public float getDamageVsEntity(float offset) {
        return offset + damageVsEntity;
    }
    
    public float getAttackSpeed(float offset) {
    	return offset + attackSpeed;
    }

    public int getHarvestLevel() {
        return hl;
    }

    public int getEnchantability() {
        return enchant;
    }

    public Item getItem() {
        return item;
    }

    public boolean getIsRepairable(ItemStack stack) {
        return getItem() == stack.getItem() && (data < 0 || data == stack.getItemDamage());
    }

    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, Multimap<String, AttributeModifier> map, UUID damageId, UUID speedId, double damage, double speed, String name) {
    	if (slot == EntityEquipmentSlot.MAINHAND) {
    		map.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(damageId, name, damage, 0));
        	map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(speedId, name, speed, 0));
    	}
        return map;
    }

    public String toString() {
        return string;
    }
    
    /**
     * Converts the given vanilla tool material to an instance of ToolsetAttributes.
     */
    public static ToolsetAttributes fromEnum(ToolMaterial toolset) {
    	if (conversionMapping.containsKey(toolset)) {
    		return conversionMapping.get(toolset);
    	}
    	return new ToolsetAttributes(toolset, -3.2f);
    }
}
