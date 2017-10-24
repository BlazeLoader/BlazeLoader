package com.blazeloader.api.client.tick;

import com.blazeloader.api.client.ApiClient;
import com.mumfrey.liteloader.client.overlays.IMinecraft;

/**
 * Client side tick functions
 *
 */
public class ApiTickClient {
    /**
     * Gets the game's tick rate.
     *
     * @return Returns the game's current tick rate.
     */
    public static float getTPS() {
        return getGameTimer().getTicksPerSecond();
    }
    
    /**
     * Sets the game tick rate.
     *
     * @param tps The new tick rate.
     */
    public static void setTPS(float tps) {
    	getGameTimer().setTicksPerSecond(tps);;
    }
    
    /**
     * Gets the timer currently used by the game.
     * 
     * @return ITimer instance
     */
    public static ITimer getGameTimer() {
    	return (ITimer)((IMinecraft)ApiClient.getClient()).getTimer();
    }
}
