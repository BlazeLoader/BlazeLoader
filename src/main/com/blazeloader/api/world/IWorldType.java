package com.blazeloader.api.world;

public interface IWorldType {
	/**
     * Gets the name of the mod this world type belongs to or "vanilla" if it does not belong to any.
     */
	String getModName();
	
	/**
     * Gets the name of this world type.
     */
	String getName();
	
	/**
     * Sets canBeCreated to the provided value, and returns this.
     */
	IWorldType setCanBeCreated(boolean val);
	
	/**
     * Flags this world type as having an associated version.
     */
	IWorldType setVersioned(boolean val);
	
	/**
     * enables the display of generator.[worldtype].info message on the customise world menu
     */
	IWorldType setEnableInfoNotice(boolean val);
	
	IWorldType setModName(String mod);
}
