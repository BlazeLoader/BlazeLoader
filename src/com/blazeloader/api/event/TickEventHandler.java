package com.blazeloader.api.event;

import com.blazeloader.api.mod.BLMod;

/**
 * Interface for mods that handle tick events
 */
public interface TickEventHandler extends BLMod {
    /**
     * Called when the game is ticked.
     */
    public void eventTick();

}