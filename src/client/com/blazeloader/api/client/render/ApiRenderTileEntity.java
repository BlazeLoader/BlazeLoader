package com.blazeloader.api.client.render;

import com.blazeloader.event.mixin.client.MTileEntityRendererDispatcher;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class ApiRenderTileEntity {
	/**
	 * Checks if there is a renderer registered for the given type of TileEntity.
	 * 
	 * @param clazz		Tile entity class
	 * @return	True if there is a renderer, false otherwise.
	 */
	public static boolean hasSpecialRenderer(Class<? extends TileEntity> clazz) {
		return getSpecialRenderer(clazz) != null;
	}
	
	/**
	 * Checks if there is a renderer registered for the given TileEntity.
	 * 
	 * @param tileEntity		Tile entity
	 * @return	True if there is a renderer, false otherwise.
	 */
	public static <T extends TileEntity> boolean hasSpecialRenderer(T tileEntity) {
		return TileEntityRendererDispatcher.instance.getRenderer(tileEntity) != null;
	}
	
	/**
	 * Gets a renderer for the given type of TileEntity.
	 * If one is not registered will check for any renderer's associated with this one's parents.
	 * 
	 * @param clazz		Tile entity class
	 * @return	A renderer that may be used with this tile entity class.
	 */
	public static <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRenderer(Class<T> clazz) {
		return TileEntityRendererDispatcher.instance.getRenderer(clazz);
	}
	
	/**
	 * Gets a renderer for the given TileEntity.
	 * If one is not registered will check for any renderer's associated with this one's parent classes.
	 * 
	 * @param tileEntity		Tile entity
	 * @return	A renderer that may be used with this tile entity.
	 */
	public static <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRenderer(T tileEntity) {
		return TileEntityRendererDispatcher.instance.getRenderer(tileEntity);
	}
	
	/**
	 * Adds a Renderer for the given type of TileEntity.
	 * 
	 * @param clazz		Tile entity class
	 * @param renderer	The renderer to use
	 */
	public static <T extends TileEntity> void registerSpecialRenderer(Class<T> clazz, TileEntitySpecialRenderer<T> renderer) {
		((MTileEntityRendererDispatcher)TileEntityRendererDispatcher.instance).getRegistry().put(clazz, renderer);
	}
	
	/**
	 * Renders a TileEntity at the given coordinates.
	 * 
	 * @param tile			TileEntity to render
	 * @param x				X-coordinate
	 * @param y				Y-coordinate
	 * @param z				Z-coordinate
	 * @param partialTicks	Partial render ticks
	 * @param destroyStage	Destruction amount, or -1 for no destruction
	 */
	public static void renderTileEntity(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage) {
		TileEntityRendererDispatcher.instance.render(tile, x, y, z, partialTicks, destroyStage);
	}
}
