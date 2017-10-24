package com.blazeloader.api.entity;

import com.blazeloader.api.ApiServer;
import com.blazeloader.api.client.ApiClient;
import com.blazeloader.util.version.Versions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.world.WorldServer;

import java.util.List;

public class ApiPlayer {

    /**
     * Gets the player Entity corresponding to the current user.
     *
     * @return Minecraft.thePlayer if on a client, otherwise returns the server owner.
     */
    public static EntityPlayer getPlayer() {
        if (Versions.isClient()) {
            return ApiClient.getPlayer();
        }
        MinecraftServer server = ApiServer.getServer();
        if (server != null) {
            String owner = server.getServerOwner();
            for (WorldServer i : server.worlds) {
                EntityPlayer result = i.getPlayerEntityByName(owner);
                if (result != null) return result;
            }
        }
        return null;
    }

    /**
     * Returns true if the player has been opped or this is running in a singleplayer world.
     */
    public static boolean playerHasOPAbilities(EntityPlayer player) {
        MinecraftServer server = ApiServer.getServer();
        if (server != null) {
            return server.getPlayerList().canSendCommands(player.getGameProfile());
        }
        return true;
    }

    /**
     * Gets the IP for the current player.
     */
    public static String getPlayerIP(EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            return ((EntityPlayerMP) player).getPlayerIP();
        }
        return "LOCALHOST";
    }

    /**
     * Gets an array of all players currently present in the game.
     */
    public static EntityPlayer[] getAllPlayers() {
        MinecraftServer server = ApiServer.getServer();
        if (server != null) {
        	List<EntityPlayerMP> result = server.getPlayerList().getPlayers();
			return result.toArray(new EntityPlayer[result.size()]);
        }
        return new EntityPlayer[0];
    }

    public static StatisticsManagerServer getPlayerStatsFile(EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            return ((EntityPlayerMP) player).getStatFile();
        }
        MinecraftServer server = ApiServer.getServer();
        if (server != null) {
            return server.getPlayerList().getPlayerStatsFile(player);
        }
        return null;
    }
}
