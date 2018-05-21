package com.blazeloader.bl.interop;

public final class FML {
	
	protected static IFML instance = null;
	
	public static void instantiated(IFML instance) {
		FML.instance = instance;
	}
}
