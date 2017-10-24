package com.blazeloader.api.entity.tracker;

import java.util.HashMap;

import com.blazeloader.bl.main.BLPacketChannels;
import com.blazeloader.bl.network.BLPacketSpawnObject;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.network.Packet;

public class EntityTrackerRegistry {
	private static EntityTrackerRegistry instance = new EntityTrackerRegistry();
	
	private final HashMap<Class<? extends Entity>, ITrack<Entity>> mappings = new HashMap<Class<? extends Entity>, ITrack<Entity>>();
	
	public static EntityTrackerRegistry instance() {
		return instance;
	}
	
	public <T extends Entity> void addTracker(Class<T> entityClass, int range, int updateFrequency, boolean includeVelocity) {
		addTracker(entityClass, new BasicTrack<T>(range, updateFrequency, includeVelocity));
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Entity> void addTracker(Class<T> entityClass, ITrack<T> tracker) {
		mappings.put(entityClass, (ITrack<Entity>)tracker);
	}
	
	public boolean addEntityToTracker(EntityTracker tracker, Entity entity) {
		if (entity != null) {
			if (entity instanceof ITrackable) {
				ITrackable trackable = (ITrackable)entity;
				tracker.track(entity, trackable.getMaxRange(), trackable.getUpdateFrequency(), trackable.mustSendVelocity());
				return true;
			} else {
				ITrack<Entity> entry = mappings.get(entity.getClass());
				if (entry != null) {
					entry.addEntityToTracker(tracker, entity);
					return true;
				}
			}
		}
		return false;
	}
	
	public Packet<?> getSpawnPacket(EntityTrackerEntry trackerEntry) {
		if (trackerEntry != null) {
			Entity tracked = trackerEntry.getTrackedEntity();
			if (tracked instanceof ITrackable) {
				return BLPacketChannels.instance().getRawPacket(new BLPacketSpawnObject.Message(tracked, 1));
			}
			ITrack<Entity> entry = mappings.get(tracked.getClass());
			if (entry != null) {
				return entry.getEntitySpawnPacket(trackerEntry);
			}
		}
		return null;
	}
}
