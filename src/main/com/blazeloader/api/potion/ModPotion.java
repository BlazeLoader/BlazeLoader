package com.blazeloader.api.potion;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

class ModPotion extends Potion {
	private static int nextPotionId = 1;
	
	private final IPotionEffect effect;
	
	protected ModPotion(ResourceLocation name, IPotionEffect effect) {
		super(effect.isBad(), effect.getColour());
		this.effect = effect;
		setPotionName(effect.getName());
		if (effect.isGood()) setBeneficial();
		effect.registerModifiers(new IModifierList() {
			public void add(IAttribute attribute, String uniqueId, double ammount, int operation) {
				ModPotion.this.registerPotionAttributeModifier(attribute, uniqueId, ammount, operation);
			}
		});
		
		REGISTRY.register(getNextPotionId(), name, this);
	}
	
	private static int getNextPotionId() {
		while (getPotionById(nextPotionId) != null) nextPotionId++;
		return nextPotionId;
	}
	
	public void performEffect(EntityLivingBase target, int amplifier) {
		effect.performEffect(target, amplifier);
	}
	
	public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, EntityLivingBase target, int amplifier, double health) {
		effect.affectEntity(source, indirectSource, target, amplifier, health);
	}
	
	public boolean isReady(int duration, int amplifier) {
		int k = effect.getCooldown(amplifier);
        return k <= 0 || duration % k == 0;
	}
	
	public boolean isInstant() {
		return effect.isInstant();
	}
}
