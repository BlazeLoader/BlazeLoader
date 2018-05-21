package com.blazeloader.bl.interop;

import java.util.List;

import org.apache.logging.log4j.core.Logger;

/**
 * Indirect access to minecraft forge. All usages should be prefixed with a check for forge existing {@code IFML.isForge()}.
 */
public interface IFML {
	public static boolean isForge() {
		return FML.instance != null;
	}
	
	/**
	 * Returns an instance of FMLCommonHandler.
	 */
	public static IFML instance() {
		return FML.instance;
	}
	
	/**
	 * Gets all the usual junk forge puts on the main menu. (credits, fml, Minecraft Forge, MCP, Serge, E621's house cat, etc.)
	 * @param includeMC Include the game version
	 */
	List<String> getBrandings(boolean includeMC);
	
	Logger getFMLLogger();
	
	/**
	 * Exits the game. Forge likes having control...
	 */
	void exitJava(int exitCode, boolean hardExit);
}
