package com.blazeloader.bl.obf;

import com.blazeloader.util.version.Versions;

/**
 * Type of obfuscation state for the game.
 */
public enum OBFLevel {
	/**
	 * Production build, all references are obfuscated.
	 */
	OBF,
	/**
	 * Forge build, references all use automatically generated names.
	 */
	SRG,
	/**
	 * Development build, all references have human-readable names.
	 */
	MCP;
	
	/**
	 * Gets the obfuscation level corresponding to the current game state.
	 */
	public static OBFLevel getCurrent() {
		return Versions.isGameObfuscated() ? OBF : Versions.isForgeInstalled() ? SRG : MCP;
	}
}
