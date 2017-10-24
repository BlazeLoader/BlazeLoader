package com.blazeloader.event.mixin.client;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.blazeloader.api.item.ItemRegistry;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

@Mixin(ModelBakery.class)
public abstract class MModelBakery {
	@Shadow
	private Map<Item, List<String>> variantNames;
	
	@Accessor("LOCATIONS_BUILDIN_TEXTURES")
	public static Set<ResourceLocation> getBuiltinTextures() {
		throw new NotImplementedException("MModelBakery mixin failed.");
	}
	
	@Inject(method = "registerVariantNames()V", at = @At("RETURN"))
	private void onRegisterVarientNames(CallbackInfo info) {
		ItemRegistry.instance().insertItemVariantNames(variantNames);
	}
}
