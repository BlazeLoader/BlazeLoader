package com.blazeloader.api.client;

import com.blazeloader.api.ApiServer;
import com.blazeloader.bl.main.BlazeLoaderCoreProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.GameType;

public class ApiClient {
	
	/**
	 * Enables or disables client profiling
	 * 
	 * @param enabled
	 */
	public static void setProfiling(boolean enabled) {
		Minecraft client = getClient();
		client.gameSettings.showDebugInfo = enabled;
		client.gameSettings.showDebugProfilerChart = enabled;
		client.gameSettings.hideGUI = enabled;
		client.mcProfiler.profilingEnabled = enabled;
	}
	
    /**
     * Client version of ApiPlayer.getPlayer. Will always return Minecraft.thePlayer
     *
     * @return the client's player
     */
    public static EntityPlayerSP getPlayer() {
        return getClient().player;
    }
    
    public static GameSettings getGameSettings() {
    	return getClient().gameSettings;
    }
    
    /**
     * Gets the render item currently used
     */
    public static RenderItem getRenderItem() {
    	return getClient().getRenderItem();
    }
    
    /**
     * Gets the resource manager
     */
    public static IReloadableResourceManager getResourceManager() {
    	return (IReloadableResourceManager)getClient().getResourceManager();
    }
    
    /**
     * Gets the texture manager
     */
    public static TextureManager getTextureManager() {
    	return getClient().getTextureManager();
    }
    
    /**
     * Gets the item renderer
     */
    public static ItemRenderer getItemRenderer() {
    	return getClient().getItemRenderer();
    }
    
    /**
     * Gets the render manager currently used.
     */
    public static RenderManager getRenderManager() {
    	return getClient().getRenderManager();
    }
    
    /**
     * Gets the client effect renderer
     */
    public static ParticleManager getEffectRenderer() {
    	return getClient().effectRenderer;
    }
    
    /**
     * Gets the resourcepack manager for the client
     */
    public static ResourcePackRepository getResourcePackRepository() {
    	return getClient().getResourcePackRepository();
    }
    
    /**
     * The current instance of the Minecraft client
     */
    public static Minecraft getClient() {
        return (Minecraft) BlazeLoaderCoreProvider.getGameEngine().getClient();
    }
    
	/**
	 * Opens server to lan.
	 * <p>
	 * Only has an effect on the Integrated Server
	 * 
	 * @param type			The gametype
	 * @param allowCheats	True to let players execute commands (cheats)
	 */
	public static void shareToLAN(GameType type, boolean allowCheats) {
		IntegratedServer server = getIntegratedServer();
		if (server != null) {
			server.shareToLAN(type, allowCheats);
		}
	}
    
    /**
     * The current IntegratedServer instance
     */
    public static IntegratedServer getIntegratedServer() {
        return (IntegratedServer)ApiServer.getServer();
    }
}
