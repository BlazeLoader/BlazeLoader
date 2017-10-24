package com.blazeloader.api.client.render;

import java.util.Map;

import com.blazeloader.api.client.ApiClient;
import com.blazeloader.event.mixin.client.MRenderManager;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;

public class ApiRenderPlayer {
	private static RenderPlayer defaultRenderer = null;
	
	/**
	 * Sets the renderer for an existing skin type.
	 * 
	 * @param skinType	The player skin type to replace
	 * @param renderer	The renderer to use
	 * @return	The passed in SkinType for chaining
	 */
	public static SkinType setPlayerRenderer(SkinType skinType, RenderPlayer renderer) {
		if (defaultRenderer == null) {
			defaultRenderer = getPlayerRenderer(SkinType.STEVE);
		}
		skinMap().put(skinType.key, renderer);
		return skinType;
	}
	
	/**
	 * Adds a new type of skin for the player.
	 * 
	 * @param skinType	String identifier for the skin type
	 * @param renderer	The renderer to use
	 * @return A SkinType associated with you registered renderer
	 */
	public static SkinType setPlayerRenderer(String skinType, RenderPlayer renderer) {
		return setPlayerRenderer(SkinType.getOrCreate(skinType), renderer);
	}
	
	/**
	 * Removes the renderer associated with the given SkinType if one exists.
	 * <p>
	 * Optional operation. 
	 * 
	 * @param skinType The skin type to unregister
	 * @return true if a renderer was removed, false otherwise.
	 */
	public static boolean removePlayerRenderer(SkinType skinType) {
		if (hasPlayerRenderer(skinType)) {
			if (skinType == SkinType.STEVE) {
				setPlayerRenderer(skinType, defaultRenderer);
			} else {
				skinMap().keySet().remove(skinType.key);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the renderer associated with the given SkinType.
	 * <p>
	 * If none are registered will return the default renderer (STEVE).
	 * 
	 * @param skinType	SkinType to retrieve
	 * @return	The renderer associated with the SkinType or the default
	 */
	public static RenderPlayer getPlayerRenderer(SkinType skinType) {
		RenderPlayer result = skinMap().get(skinType.key);
		if (result == null) {
			return skinMap().get(SkinType.STEVE.key);
		}
		return result;
	}
	
	/**
	 * Gets a renderer for the given player based on the type of skin they use.
	 * 
	 * @param player	The EntityPlayer to render
	 * @return	A player renderer for the given player
	 */
	public static RenderPlayer getPlayerRenderer(EntityPlayer player) {
		return (RenderPlayer)ApiClient.getRenderManager().<AbstractClientPlayer>getEntityRenderObject(player);
	}
	
	/**
	 * Checks if there is a renderer registered for the given SkinType
	 * 
	 * @param skinType	SkinType to check
	 * @return True if a renderer exists, otherwise false
	 */
	public static boolean hasPlayerRenderer(SkinType skinType) {
		return hasPlayerRenderer(skinType.key);
	}
	
	/**
	 * Checks if there is a renderer registered for the given skin identifier
	 * 
	 * @param skinType	SkinType identifier
	 * @return True if a renderer exists, otherwise false
	 */
	public static boolean hasPlayerRenderer(String skinType) {
		return skinMap().containsKey(skinType);
	}
	
	private static Map<String, RenderPlayer> skinMap() {
		return ((MRenderManager)ApiClient.getRenderManager()).getSkinMap();
	}
}
