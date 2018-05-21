package com.blazeloader.event.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;

@Mixin(ModelBox.class)
public interface MModelBox {
	@Accessor("quadList")
	public TexturedQuad[] getQuadList();
	
	@Accessor("vertexPositions")
	public PositionTextureVertex[] getVertexPositions();
}
