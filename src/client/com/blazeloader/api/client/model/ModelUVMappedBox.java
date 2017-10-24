package com.blazeloader.api.client.model;

import com.blazeloader.event.mixin.client.MVertexBuffer;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;

public class ModelUVMappedBox extends ModelBox {
	public static final int SIDE_NORTH = 0;
    public static final int SIDE_SOUTH = 1;
    public static final int SIDE_TOP = 2;
    public static final int SIDE_BOTTOM = 3;
    public static final int SIDE_EAST = 4;
    public static final int SIDE_WEST = 5;
	
	private final float textureWidth;
	private final float textureHeight;
	
	private final int boxDepth;
	private final int boxWidth;
	private final int boxHeight;
	
	private final boolean mirrored;
	
    public ModelUVMappedBox(ModelRenderer renderer, int texOffsetX, int texOffsetY, float originX, float originY, float originZ, int width, int height, int depth, float scale) {
    	this(renderer, texOffsetX, texOffsetY, originX, originY, originZ, width, height, depth, scale, renderer.mirror);
    }
    
    public ModelUVMappedBox(ModelRenderer renderer, int texOffsetX, int texOffsetY, float originX, float originY, float originZ, int width, int height, int depth, float scale, boolean mirror) {
    	super(renderer, texOffsetX, texOffsetY, originX, originY, originZ, width, height, depth, scale, mirror);
    	textureWidth = renderer.textureWidth;
    	textureHeight = renderer.textureHeight;
    	boxDepth = depth;
    	boxWidth = width;
    	boxHeight = height;
    	mirrored = mirror;
    }
    
    public ModelUVMappedBox setFaceUV(int side, int x, int y) {
    	switch (side) {
    	case SIDE_NORTH: return setNorthFaceUV(x, y);
    	case SIDE_SOUTH: return setSouthFaceUV(x, y);
    	case SIDE_EAST: return setEastFaceUV(x, y);
    	case SIDE_WEST: return setWestFaceUV(x, y);
    	case SIDE_TOP: return setTopFaceUV(x, y);
    	case SIDE_BOTTOM: return setBottomFaceUV(x, y);
    	}
    	return this;
    }
    
    public ModelUVMappedBox setNorthFaceUV(int x, int y) {//^^ Front
    	TexturedQuad[] quadList = ((MVertexBuffer)(Object)this).getQuadList();
    	PositionTextureVertex[] vertexPositions = ((MVertexBuffer)(Object)this).getVertexPositions();
    	
    	quadList[0] = new TexturedQuad(new PositionTextureVertex[] {vertexPositions[5], vertexPositions[1], vertexPositions[2], vertexPositions[6]},
    			x, y, x + boxDepth, y + boxHeight, textureWidth, textureHeight);
    	if (mirrored) quadList[0].flipFace();
    	return this;
    }
    
    public ModelUVMappedBox setSouthFaceUV(int x, int y) {//^^ back
    	TexturedQuad[] quadList = ((MVertexBuffer)(Object)this).getQuadList();
    	PositionTextureVertex[] vertexPositions = ((MVertexBuffer)(Object)this).getVertexPositions();
    	
    	quadList[1] = new TexturedQuad(new PositionTextureVertex[] {vertexPositions[0], vertexPositions[4], vertexPositions[7], vertexPositions[3]},
    			x, y, x + boxDepth, y + boxHeight, textureWidth, textureHeight);
    	if (mirrored) quadList[1].flipFace();
    	return this;
    }
    
    public ModelUVMappedBox setTopFaceUV(int x, int y) { //^^
    	TexturedQuad[] quadList = ((MVertexBuffer)(Object)this).getQuadList();
    	PositionTextureVertex[] vertexPositions = ((MVertexBuffer)(Object)this).getVertexPositions();
    	
    	quadList[2] = new TexturedQuad(new PositionTextureVertex[] {vertexPositions[5], vertexPositions[4], vertexPositions[0], vertexPositions[1]},
    			x, y, x + boxWidth, y + boxDepth, textureWidth, textureHeight);
    	if (mirrored) quadList[2].flipFace();
    	return this;
    }
    
    public ModelUVMappedBox setBottomFaceUV(int x, int y) {//^^
    	TexturedQuad[] quadList = ((MVertexBuffer)(Object)this).getQuadList();
    	PositionTextureVertex[] vertexPositions = ((MVertexBuffer)(Object)this).getVertexPositions();
    	
    	quadList[3] = new TexturedQuad(new PositionTextureVertex[] {vertexPositions[2], vertexPositions[3], vertexPositions[7], vertexPositions[6]},
    			x, y + boxDepth, x + boxWidth, y, textureWidth, textureHeight);
    	if (mirrored) quadList[3].flipFace();
    	return this;
    }
    
    public ModelUVMappedBox setEastFaceUV(int x, int y) {//^^ left
    	TexturedQuad[] quadList = ((MVertexBuffer)(Object)this).getQuadList();
    	PositionTextureVertex[] vertexPositions = ((MVertexBuffer)(Object)this).getVertexPositions();
    	
    	quadList[4] = new TexturedQuad(new PositionTextureVertex[] {vertexPositions[1], vertexPositions[0], vertexPositions[3], vertexPositions[2]},
    			x, y, x + boxWidth, y + boxHeight, textureWidth, textureHeight);
    	if (mirrored) quadList[4].flipFace();
    	return this;
    }
    
    public ModelUVMappedBox setWestFaceUV(int x, int y) {//^^ Right
    	TexturedQuad[] quadList = ((MVertexBuffer)(Object)this).getQuadList();
    	PositionTextureVertex[] vertexPositions = ((MVertexBuffer)(Object)this).getVertexPositions();
    	
    	quadList[5] = new TexturedQuad(new PositionTextureVertex[] {vertexPositions[4], vertexPositions[5], vertexPositions[6], vertexPositions[7]},
    			x, y, x + boxWidth, y + boxHeight, textureWidth, textureHeight);
    	if (mirrored) quadList[5].flipFace();
    	return this;
    }
}
