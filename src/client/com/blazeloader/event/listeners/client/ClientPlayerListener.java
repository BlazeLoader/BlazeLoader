package com.blazeloader.event.listeners.client;

import com.blazeloader.bl.mod.BLMod;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.server.SPacketJoinGame;

/**
 * Interface for mods that handle player events
 */
public interface ClientPlayerListener extends BLMod {

    /**
     * Called when the client player dies.
     */
    public void onClientPlayerDeath();

    /**
     * Called when the client connects to a server or singleplayer game
     *
     * @param netHandler  The network handler processing loginPacket
     * @param loginPacket The login packet for this login
     */
    public void onClientJoinGame(INetHandler netHandler, SPacketJoinGame loginPacket);
}
