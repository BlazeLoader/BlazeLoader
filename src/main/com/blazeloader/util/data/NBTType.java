package com.blazeloader.util.data;

import net.minecraft.nbt.NBTBase;

/**
 * All the NBT value types for easy access.
 */
public enum NBTType {
	/**
	 * No tag or end of stream
	 */
	NONE(0),
	
	BYTE(1),
	SHORT(2),
	INT(3),
	LONG(4),
	FLOAT(5),
	DOUBLE(6),
	STRING(8),
	LIST(9),
	COMPOUND(10),
	
	BYTE_ARRAY(7),
	INT_ARRAY(11),
	LONG_ARRAY(12),
	/**
	 * Alias for {@link STRING}
	 */
	CHAR_ARRAY(8);
	
	private final byte id;
	
	private NBTType(int id) {
		this.id = (byte)id;
	}
	
	/**
	 * Gets then name for this type as used in {@link NBTBase}
	 */
	public String getName() {
		return NBTBase.getTagTypeName(id);
	}
	
	/**
	 * Gets the type id corresponding to this nbt tag type
	 */
	public byte value() {
		return id;
	}
	
	/**
	 * Creates a new NBTTag with the current type
	 */
	public NBTBase createTag() {
		return NBT.createByType(this.id);
	}
	
	private static abstract class NBT extends NBTBase {
		public static NBTBase createByType(byte id) {
			return NBTBase.createNewByType(id);
		}
	}
}
