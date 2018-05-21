package com.blazeloader.event.mixin.client;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.blazeloader.api.client.render.ILivingRenderer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

@Mixin(RenderLivingBase.class)
public abstract class MRenderLivingBase<T extends EntityLivingBase> extends Render<T> implements ILivingRenderer<T> {

	protected MRenderLivingBase() {super(null);}
	
	@Shadow
	protected List<LayerRenderer<T>> layerRenderers;
	
	@SuppressWarnings("unchecked")
	public RenderLivingBase<T> unwrap() {
		return (RenderLivingBase<T>)(Object)this;
	}
	
	@Invoker(value = "getMainModel")
	public abstract ModelBase getModel();
	
	@Invoker(value = "getEntityTexture")
	public abstract ResourceLocation getTexture(T entity);
	
	@SuppressWarnings("unchecked")
	public <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(U layer) {
		return layerRenderers.add((LayerRenderer<T>)layer);
	}
	
	public <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean removeLayer(U layer) {
		return layerRenderers.remove(layer);
	}
}
