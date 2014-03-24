package net.acomputerdog.BlazeLoader.event;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.world.World;

/**
 * Interface for mods that handle game events not handled by vanilla.  Override events are only called if the game is unable to handle the event on it's own.
 */
public interface OverrideEventHandler {

    /**
     * Creates a spawn packet for the given entity.
     *
     * @param entity    The entity to create the spawn packet for.
     * @param isHandled True if another mod has already created a packet for this entity.
     * @return Return a spawn packet for the given entity, or null if none exists.
     */
    public S0EPacketSpawnObject overrideCreateSpawnPacket(Entity entity, boolean isHandled);

    /**
     * Adds an entity to an entity tracker.
     *
     * @param tracker   The tracker to add the entity to.
     * @param entity    The entity to add.
     * @param isHandled True if another mod has already handled the event.
     * @return Return true if the entity was added, false otherwise.
     */
    public boolean overrideAddEntityToTracker(EntityTracker tracker, Entity entity, boolean isHandled);

    /**
     * Spawns a particle into thw world.
     *
     * @param name         The name of the particle to spawn.
     * @param world        The world to spawn in.
     * @param x            The x-location to spawn at.
     * @param y            The y-location to spawn at.
     * @param z            The z-location to spawn at.
     * @param p1           Parameter 1
     * @param p2           Parameter 1
     * @param p3           Parameter 1
     * @param currParticle The particle that the previous mod generated.  Set to null if no mod has generated an particle
     * @return A generated particle, or param currParticle to disable behavior
     */
    public EntityFX overrideSpawnParticle(String name, World world, double x, double y, double z, double p1, double p2, double p3, EntityFX currParticle);
}
