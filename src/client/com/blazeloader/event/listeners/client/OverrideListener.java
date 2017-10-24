package com.blazeloader.event.listeners.client;

import com.blazeloader.bl.mod.BLMod;
import com.blazeloader.event.listeners.args.ContainerOpenedEventArgs;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.particle.Particle;

/**
 * Interface for mods that handle game events not handled by vanilla.  Override events are only called if the game is unable to handle the event on it's own.
 */
public interface OverrideListener extends BLMod {

    /**
     * Provides a hook to replace a particle before it gets spawned.
     *
     * @param particleId	Id of the particle being spawned
     * @param x            	The x-location to spawn at.
     * @param y            	The y-location to spawn at.
     * @param z            	The z-location to spawn at.
     * @param xOffset		The x velocity
     * @param yOffset		The y velocity
     * @param zOffset		The z velocity
     * @param currParticle The particle that the previous mod generated.  Set to null if no mod other has generated a particle yet
     * @return A generated particle
     */
    public Particle onSpawnParticle(int particleId, double x, double y, double z, double p1, double p2, double p3, Particle currParticle);


    /**
     * Called to allow a mod to display a gui for a custom container
     *
     * @param player         The player accessing the container
     * @return Return true if container has been handled
     */
    public boolean onContainerOpened(AbstractClientPlayer player, ContainerOpenedEventArgs e);
}
