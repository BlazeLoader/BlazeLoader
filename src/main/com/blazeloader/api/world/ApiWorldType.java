package com.blazeloader.api.world;

import java.util.Arrays;

import com.blazeloader.bl.main.BLMain;
import com.blazeloader.event.mixin.common.MWorldType;
import com.blazeloader.util.reflect.Constr;
import com.blazeloader.util.reflect.Reflect;

import net.minecraft.world.WorldType;

public class ApiWorldType {
		
	/**
	 * Creates a new world type with the given name.
	 * 
	 * Translation keys for world types made this way will have the following format:
	 * <p>
	 * {@code modName.generator.name}
	 * <p>
	 * Similarly they will also be identified by a combination of the modName and their name: 
	 * 
	 * @param modName	The mod creating the world type.
	 * @param name		The name of the world type.
	 * @return	A brand new WorldType for use.
	 */
	public static IWorldType registerWorldType(String modName, String name) {
		return registerWorldType(modName, name, 0);
	}
	
	/**
	 * Creates a new world type with the given name and generator version
	 * 
	 * Translation keys for world types made this way will have the following format:
	 * <p>
	 * {@code modName.generator.name}
	 * 
	 * @param modName	The mod creating the world type.
	 * @param name		The name of the world type.
	 * @param version	The generator version (default 0)
	 * @return	A brand new WorldType for use.
	 */
	private static final Constr<WorldType> worldTypeConstructor = Reflect.lookupConstructor(WorldType.class, int.class, String.class, int.class);
	public static IWorldType registerWorldType(String modName, String name, int version) {
		IWorldType result = null;
		try {
			result = ((IWorldType)worldTypeConstructor.call(getNextWorldTypeId(), name, version)).setModName(modName);
		} catch (Throwable e) {
			BLMain.LOGGER_FULL.fatal("Error constructing WorldType", e);
		}
		return result;
	}
	
	/**
	 * Gets the array of all known WorldTypes
	 */
	public static WorldType[] getWorldTypes() {
		return WorldType.WORLD_TYPES;
	}
	
	/**
	 * Gets a WorldType with the given name. Or null if none were found.
	 * @param name	The name of the world type
	 */
	public static WorldType getWorldType(String name) {
		return WorldType.parseWorldType(name);
	}
	
	/**
	 * Gets a world type for the given id or null if the given id is not valid.
	 * @param id	The id of the world type
	 */
	public static IWorldType getWorldType(int id) {
		if (id < 0 || id > getWorldTypes().length) {
			return null;
		}
		return (IWorldType)getWorldTypes()[id];
	}
	
	/**
	 * Gets the mod name a world type was registered with.
	 * @param type	The world type
	 * @return "vanilla" for non mod world types and "unknown" for unrecognised mods otherwise the mod name it was registered with.
	 */
	public static String getWorldTypeMod(WorldType type) {
		return ((IWorldType)type).getModName();
	}
	
	private static int getNextWorldTypeId() {
		WorldType[] types = getWorldTypes();
		for (int i = 0; i < types.length; i++) {
			if (types[i] == null) return i;
		}
		MWorldType.setWorldTypes(Arrays.copyOf(types, types.length + 16));
		return types.length;
	}
}
