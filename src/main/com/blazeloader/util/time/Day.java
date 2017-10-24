package com.blazeloader.util.time;

import com.blazeloader.util.Strings;

public enum Day {
	MONDAY,
	TUESDAY,
	WEDNESDAY,
	THURSDAY,
	FRIDAY,
	SATURDAY,
	SUNDAY;
	
	private final String displayName = Strings.toProperCase(name());
	private final String shortName = displayName.substring(0, Math.min(3, displayName.length()));
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getShortName() {
		return shortName;
	}
}
