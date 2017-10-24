package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.blazeloader.api.world.IWorldType;
import com.blazeloader.util.annotations.AccessTransform;

import net.minecraft.world.WorldType;

@Mixin(WorldType.class)
public abstract class MWorldType implements IWorldType {
	
	@Shadow @Final private String name;
	@Shadow private boolean canBeCreated;
	@Shadow private boolean versioned;
	@Shadow private boolean hasInfoNotice;
	
	private String mod = null;
	
	@Accessor("WORLD_TYPES") @AccessTransform(action="Non-Final")
	public static void setWorldTypes(WorldType[] types) {}
	
	@Inject(method = "updateEquipmentIfNeeded(Lnet/minecraft/entity/item/EntityItem;)V", at = @At("RETURN"))
	public void onGetTranslationKey(CallbackInfoReturnable<String> info) {
		if (mod != null) info.setReturnValue(mod + ".generator." + name);
	}
	
	@Override
	public String getModName() {
		return mod == null ? "vanilla" : mod;
	}
	
	@Override
	public IWorldType setModName(String mod) {
		this.mod = mod;
		return this;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public IWorldType setCanBeCreated(boolean val) {
		canBeCreated = val;
		return this;
	}
	
	@Override
	public IWorldType setVersioned(boolean val) {
		versioned = val;
		return this;
	}
	
	@Override
	public IWorldType setEnableInfoNotice(boolean val) {
		hasInfoNotice = val;
		return this;
	}
}
