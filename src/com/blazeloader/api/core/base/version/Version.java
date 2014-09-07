package com.blazeloader.api.core.base.version;

import com.blazeloader.api.core.base.main.BLMain;

import java.util.HashMap;
import java.util.Map;

/**
 * Version class that allows access to versions of various BL components
 */
public class Version {
    private static final boolean isOBF = VersionUtils.isGameOBF();
    private static final boolean isForgeInstalled = VersionUtils.hasForge();
    private static final Map<String, Versioned> versionMap = new HashMap<String, Versioned>();
    public static final Versioned BL_VERSION = new BasicVersion(new int[]{1, 0, 0, 0}, "BlazeLoader.main", "BlazeLoader", BuildType.DEVELOPMENT);

    /**
     * Detects if the game is obfuscated.
     *
     * @return Return true if the game is obfuscated.
     */
    public static boolean isGameObfuscated() {
        return isOBF;
    }

    /**
     * Returns true if running on the client, false for server.  Only works AFTER BlazeLoader is initialized.
     * @return Return true is running on client, false for server.
     */
    public static boolean isClient() {
        return !isServer();
    }

    /**
     * Returns true if running on the server, false for client.  Only works AFTER BlazeLoader is initialized.
     *
     * @return Return true is running on server, false for client.
     */
    public static boolean isServer() {
        return BLMain.instance().supportsServer();
    }

    /**
     * Detects if forge is installed.
     *
     * @return Return true if forge is installed
     */
    public static boolean isForgeInstalled() {
        return isForgeInstalled;
    }

    public static Versioned getBLMainVersion() {
        return BL_VERSION;
    }

    public static Versioned getVersionOf(String id) {
        return versionMap.get(id);
    }

    public static void addVersion(Versioned version) {
        versionMap.put(version.getID(), version);
    }

    public static boolean isVersionRegistered(String id) {
        return versionMap.containsKey(id);
    }

}
