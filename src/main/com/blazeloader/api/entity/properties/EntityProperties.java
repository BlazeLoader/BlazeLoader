package com.blazeloader.api.entity.properties;

import java.util.ArrayList;
import java.util.List;

import com.blazeloader.bl.main.BLMain;
import com.blazeloader.util.data.INBTWritable;

import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityProperties implements INBTWritable {
	private final List<IEntityProperties> handlers = new ArrayList<IEntityProperties>();
	
	public EntityProperties() {}
	
	public void registerHandler(IEntityProperties handler) {
		for (IEntityProperties i : handlers) {
			if (i.getClass().equals(handler.getClass())) {
				BLMain.LOGGER_FULL.warn("Attempted to register duplicate Properties object (" + handler.getClass().getCanonicalName() + "). Only one instance allowed per entity. Handler was not registered.");
				return;
			}
		}
		handlers.add(handler);
	}
	
	public <E extends IEntityProperties> E getHandler(Entity e, Class<E> c) {
		try {
			if (handlers != null) {
				for (IEntityProperties i : handlers) {
					if (i.getClass() == c) {
						@SuppressWarnings("unchecked")
						E result = (E)i;
						result.setOwningEntity(e);
						return result;
					}
				}
			}
		} catch (Throwable t) {
			BLMain.LOGGER_FULL.error("Unexpected error whilst retrieving entity properties: ", t);
		}
		return null;
	}
	
	public void unRegisterHandler(IEntityProperties handler) {
		if (handlers.contains(handler)) handlers.remove(handler);
	}
	
	public void onEntityUpdate(Entity entity) {
		for (IEntityProperties i : handlers) i.onEntityUpdate(entity);
	}
	
	public void entityInit(Entity entity, World world) {
		for (IEntityProperties i : handlers) i.entityInit(entity, world);
	}
	
	public EntityProperties readFromNBT(NBTTagCompound t) {
		NBTTagCompound modsTag;
		if (t.hasKey("BlazeLoader")) {
			modsTag = t.getCompoundTag("BlazeLoader");
		} else {
			modsTag = new NBTTagCompound();
		}
		try {
			for (IEntityProperties i : handlers) i.readFromNBT(modsTag);
		} catch (Throwable er) {
			BLMain.LOGGER_FULL.fatal("Failed in reading entity NBT into (" + this.getClass().getCanonicalName() + ").", er);
		}
		
		return this;
	}
	
	public void writeToNBT(NBTTagCompound t) {
		NBTTagCompound modsTag = new NBTTagCompound();
		t.setTag("BlazeLoader", modsTag);
		try {
			for (IEntityProperties i : handlers) i.writeToNBT(modsTag);
		} catch (Throwable er) {
			BLMain.LOGGER_FULL.fatal("Failed in writing entity NBT from (" + this.getClass().getCanonicalName() + ").", er);
		}
		
	}
	
	public void addCrashInfo(Entity e, CrashReportCategory section) {
		EntityProperties p = ((IEntityPropertyOwner)e).getExtendedProperties();
		try {
			section.addCrashSection("BlazeLoader Extended Entity Properties", p.getCrashInfo());
		} catch (Throwable er) {
			section.addCrashSectionThrowable("BLMod error", er);
		}
	}
	
	public String getCrashInfo() {
		StringBuilder result = new StringBuilder();
		result.append("Extended Properties Registered: " + handlers.size());
		for (IEntityProperties i : handlers) {
			CrashReportCategory category = new CrashReportCategory(null, "Mod:" + i.getClass().toString());
			i.addEntityCrashInfo(category);
			category.appendToStringBuilder(result);
		}
		return result.toString();
	}
	
	public String toString() {
		return "Properties(" + handlers.size() + ")#" + handlers.toString();
	}
}