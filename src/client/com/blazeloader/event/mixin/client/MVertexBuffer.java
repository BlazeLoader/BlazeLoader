package com.blazeloader.event.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.vertex.VertexBuffer;

@Mixin(VertexBuffer.class)
public interface MVertexBuffer {
	@Accessor("quadList")
	public TexturedQuad[] getQuadList();
	
	@Accessor("vertexPositions")
	public PositionTextureVertex[] getVertexPositions();
}
