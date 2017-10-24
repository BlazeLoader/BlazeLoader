package com.blazeloader.util.version;

import com.blazeloader.bl.main.BLMain;

import java.util.HashMap;
import java.util.Map;

/**
 * Version class that allows access to versions of various BL components
 */
public class Versions {
    private static final boolean isOBF = VersionUtils.isGameOBF();
    private static final boolean isForgeInstalled = VersionUtils.hasForge();
    private static final Map<String, Version<?>> versionMap = new HashMap<String, Version<?>>();
    

    /**
     * Detects if the game is obfuscated.
     *
     * @return Return true if the game is obfuscated.
     */
    public static boolean isGameObfuscated() {
        return isOBF;
    }

    /**
     * Returns true if running on the client, false for server.
     *
     * @return Return true if running on client, false for server.
     */
    public static boolean isClient() {
        return BLMain.isClient();
    }

    /**
     * Returns true if running on the server, false for client.
     *
     * @return Return true if running on server, false for client.
     */
    public static boolean isServer() {
        return !isClient();
    }
    
    /**
     * Detects if forge is installed.
     *
     * @return Return true if forge is installed
     */
    public static boolean isForgeInstalled() {
        return isForgeInstalled;
    }
    
    /**
     * Gets the version of blazeloader running on the current game
     */
    public static Version<?> getBLMainVersion() {
        return BLMain.getVersion();
    }
    
    /**
     * Attempts to get the version associated with the given id
     * 
     * @param id	Name of version to get
     * 
     * @return The associated version or null
     */
    public static Version<?> getVersionOf(String id) {
        return versionMap.get(id);
    }

    /**
     * Adds a version.
     * 
     * @param version	The version to add.
     */
    protected static void addVersion(Version<?> version) {
        versionMap.put(version.getID(), version);
    }
    
    /**
     * Checks if there is a version registered for the given key
     * 
     * @param id	Id of version to check for
     * 
     * @return true if one exists, false otherwise
     */
    public static boolean isVersionRegistered(String id) {
        return versionMap.containsKey(id);
    }
    
    /**
     * Checks if the given version is registered
     * 
     */
    public static boolean isVersionRegistered(Version<?> version) {
        return isVersionRegistered(version.getID());
    }

}
