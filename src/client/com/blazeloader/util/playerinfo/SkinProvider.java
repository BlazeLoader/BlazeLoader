package com.blazeloader.util.playerinfo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.blazeloader.api.client.ApiClient;
import com.blazeloader.bl.main.BLMain;
import com.google.common.base.MoreObjects;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * Unchecked downloading of player skins and capes. Because the vanilla skin manager doesn't always work.
 */
public class SkinProvider {
	private File skincache;
	
	private Map<Type, ResourceLocation> playerSkins = new HashMap<Type, ResourceLocation>(); 
	
	private PlayerInfoProvider playerInfo;
	
	/**
	 * Creates a new SkinProvider.
	 * 
	 * @param profile	GameProfile of the player's skins this provider must fetch.
	 */
	public SkinProvider(GameProfile profile) {
		this(EntityPlayer.getUUID(profile));
	}
	
	/**
	 * Creates a new SkinProvider.
	 * 
	 * @param uuid	Id of the player's skins this provider must fetch.
	 */
	public SkinProvider(UUID uuid) {
		this(new PlayerInfoProvider(uuid));
	}
	
	protected SkinProvider(PlayerInfoProvider provider) {
		playerInfo = provider;
		skincache = new File(BLMain.instance().environment.getAssetsDirectory(), "skins");
	}
	
	/**
	 * Returns true if a skin location has been loaded.
	 */
	public boolean hasSkin() {
        return playerSkins.containsKey(Type.SKIN);
    }
	
	/**
	 * Returns true if a cape location has been loaded.
	 */
	public boolean hasCape() {
		return provideCape() != null;
	}
	
	/**
	 * Returns true if an elytra location has been loaded.
	 */
	public boolean hasElytra() {
		return provideElytra() != null;
	}
	
	/**
	 * Gets the type of askin associated with a player.
	 * @return	Associated skin type, or a default based on their id hash.
	 */
	public String getSkinType() {
		return MoreObjects.firstNonNull(playerInfo.getSkinType(), DefaultPlayerSkin.getSkinType(playerInfo.uuid));
	}
	
	/**
	 * Gets the skin resource for a player.
	 * 
	 * @return	The skin resource location, or the default if one is not available.
	 */
	public ResourceLocation provideSkin() {
    	return MoreObjects.firstNonNull(provideTexture(Type.SKIN), DefaultPlayerSkin.getDefaultSkin(playerInfo.uuid));
	}
	
	/**
	 * Gets the cape resource for a player.
	 * 
	 * @return	The cape resource location, or null if one is not available.
	 */
	public ResourceLocation provideCape() {
		return provideTexture(Type.CAPE);
	}
	
	/**
	 * Gets the elytra resource for a player.
	 * 
	 * @return	The elytra resource location, or null if one is not available.
	 */
	public ResourceLocation provideElytra() {
		return provideTexture(Type.ELYTRA);
	}
	
	private ResourceLocation provideTexture(Type type) {
		if (!playerSkins.containsKey(type)) {
			String url = playerInfo.popUrl(type);
    		if (url != null) {
        		getDownloadImageTexture(url, type);
        	}
    	}
    	return playerSkins.get(type);
	}
	
	private File getCacheLocation(String hash) {
    	File cacheFolder = new File(skincache, hash.substring(0, 2));
        return new File(cacheFolder, hash);
    }
    
    private String getHash(String url) {
    	String[] split = url.split("/|\\\\");
    	return split[split.length - 1];
    }
	
    private void getDownloadImageTexture(String url, Type type) {
    	String hash = getHash(url);
        ResourceLocation loc = new ResourceLocation("skins/" + hash);
        if (ApiClient.getTextureManager().getTexture(loc) != null) {
        	playerSkins.put(type, loc);
        	return;
        }
        ITextureObject texture = new ThreadDownloadImageData(getCacheLocation(hash), url, DefaultPlayerSkin.getDefaultSkinLegacy(), type == Type.SKIN ? new ImageBufferDownload() {
        	public void skinAvailable() {
        		super.skinAvailable();
        		playerSkins.put(type, loc);
        	}
        } : new IImageBuffer() {
            public BufferedImage parseUserSkin(BufferedImage image) {
                return image;
            }
            public void skinAvailable() {
            	playerSkins.put(type, loc);
            }
        });
        ApiClient.getTextureManager().loadTexture(loc, texture);
    }
}
