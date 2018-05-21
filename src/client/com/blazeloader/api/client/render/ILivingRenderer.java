package com.blazeloader.api.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public interface ILivingRenderer<T extends EntityLivingBase> {
	
	RenderLivingBase<T> unwrap();
	
	<V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(U layer);
	
	<V extends EntityLivingBase, U extends LayerRenderer<V>> boolean removeLayer(U layer);
	
	ModelBase getModel();
	
	ResourceLocation getTexture(T entity);
}
