package com.blazeloader.api.client.render;

import java.util.HashMap;

import com.blazeloader.util.JSArrayUtils;
import com.blazeloader.util.config.IStringable;

public final class SkinType implements IStringable<SkinType> {
	private static final HashMap<String, SkinType> skinTypes = new HashMap<String, SkinType>();
	
	public static final SkinType STEVE = new SkinType("STEVE", "default");
	public static final SkinType ALEX = new SkinType("ALEX", "slim");
	
	private static int nextID = 0;
	
	private final int id;
	private final String name;
	protected final String key;
	
	private SkinType(String name, String identifier) {
		this.name = name;
		this.key = identifier;
		this.id = nextID++;
		skinTypes.put(identifier, this);
	}
	
	public String toString() {
		return name;
	}
	
	public SkinType fromString(String string) {
		return valueOf(string);
	}
	
	public int ordinal() {
		return id;
	}
	
	public static SkinType valueOf(String name) {
		SkinType result = skinTypes.get(name.toLowerCase());
		if (result == null) {
			for (SkinType i : skinTypes.values()) {
				if (i.name.equals(name)) return i;
			}
			return new SkinType(name, name.toLowerCase());
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T valueOf(Class<T> enumType, String name) {
		if (enumType == SkinType.class) return (T)valueOf(name);
		return (T)Enum.valueOf((Class<? extends Enum>)enumType, name);
	}
	
	public static SkinType[] values() {
		return JSArrayUtils.toArray(skinTypes.values(), SkinType.class);
	}
	
	protected static SkinType getOrCreate(String identifier) {
		String id = identifier.toLowerCase();
		SkinType result = skinTypes.get(id);
		if (result == null) return new SkinType(identifier, id);
		return result;
	}
}