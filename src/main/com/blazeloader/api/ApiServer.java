package com.blazeloader.api;

import com.blazeloader.bl.main.BlazeLoaderCoreProvider;

import net.minecraft.server.MinecraftServer;

public class ApiServer {
	
	/**
	 * Enabled or disables server profiling
	 * 
	 * @param enabled
	 */
	public static void setProfiling(boolean enabled) {
		MinecraftServer server = getServer();
		if (server != null) {
			if (enabled) {
				server.enableProfiling();
			} else {
				server.profiler.profilingEnabled = false;
			}
		}
	}
	
	/**
	 * Checks if the server is running in online mode
	 * 
	 * @return True if the server exists and is in online mode, otherwise false
	 */
	public static boolean isInOnlineMode() {
		MinecraftServer server = getServer();
		return server != null && server.isServerInOnlineMode();
	}
	
	/**
	 * Checks if th current game is a singleplayer one
	 * @return true if the server is not running or is singleplayer, false otherwise
	 */
	public static boolean isSinglePlayer() {
		MinecraftServer server = getServer();
		return server == null || server.isSinglePlayer();
	}
	
	/**
	 * Checks if a server is running.
	 * @return true if the server exists and is running, otherwise false
	 */
	public static boolean isServerRunning() {
		MinecraftServer server = getServer();
		return server != null && server.isServerRunning();
	}
	
    /**
     * The current MinecraftServer instance
     */
	public static MinecraftServer getServer() {
		return BlazeLoaderCoreProvider.getGameEngine().getServer();
	}
}
