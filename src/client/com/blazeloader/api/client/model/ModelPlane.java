package com.blazeloader.api.client.model;

import com.blazeloader.event.mixin.client.MVertexBuffer;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.BufferBuilder;

public class ModelPlane extends ModelUVMappedBox {
	
	private boolean[] sidesHidden = new boolean[6];
	
	public ModelPlane(ModelRenderer renderer, int texOffsetX, int texOffsetY, float originX, float originY, float originZ, int width, int height, int depth, float scale) {
    	this(renderer, texOffsetX, texOffsetY, originX, originY, originZ, width, height, depth, scale, renderer.mirror);
    }
    
    public ModelPlane(ModelRenderer renderer, int texOffsetX, int texOffsetY, float originX, float originY, float originZ, int width, int height, int depth, float scale, boolean mirror) {
    	super(renderer, texOffsetX, texOffsetY, originX, originY, originZ, width, height, depth, scale, mirror);
    }
    
    public ModelPlane setHiddenSides(boolean... sides) {
    	sidesHidden = sides;
    	return this;
    }
    
    public ModelPlane hideAllSides() {
    	setHiddenSides(true,true,true,true,true,true);
    	return this;
    }
    
    public ModelPlane setSideVisibility(int side, boolean visible) {
    	sidesHidden[side] = !visible;
    	return this;
    }
    
    @Override
    public void render(BufferBuilder renderer, float scale) {
    	TexturedQuad[] quadList = ((MVertexBuffer)(Object)this).getQuadList();
        for (int i = 0; i < quadList.length; i++) {
        	if (!sidesHidden[i]) quadList[i].draw(renderer, scale);
        }
    }
}
