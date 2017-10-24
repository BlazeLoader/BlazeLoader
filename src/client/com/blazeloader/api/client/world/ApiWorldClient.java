package com.blazeloader.api.client.world;

import net.minecraft.client.Minecraft;

import com.blazeloader.api.ApiGeneral;
import com.blazeloader.api.ApiServer;

public class ApiWorldClient {
    /**
     * Gets the name of the current world, or connected server if on SMP.
     *
     * @return the name of the current world or null if no game is in progress.
     */
    public static String getCurrentWorldName() {
        if (ApiGeneral.isClient()) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.world != null) {
                if (mc.isIntegratedServerRunning()) {
                    return mc.getIntegratedServer().getWorldName(); //Client with a Singleplayer world
                } else {
                    String name = mc.getCurrentServerData().serverName;
                    if (name == null || name.trim().isEmpty() || "Minecraft Server".equals(name)) {
                        name = mc.getCurrentServerData().serverIP;
                    }
                    return name; //Client with a Multiplayer/LAN world
                }
            } else {
                return null; //Client with no game open
            }
        } else {
            return ApiServer.getServer().getWorldName(); //Server with open world
        }
    }
}
