package com.blazeloader.api.version;

import com.blazeloader.api.main.BLMain;
import com.blazeloader.api.version.type.Version;
import com.blazeloader.api.version.type.values.QuadrupleVersion;

import java.util.HashMap;
import java.util.Map;

/**
 * Version class that allows access to versions of various BL components
 */
public class Versions {
    private static final boolean isOBF = VersionUtils.isGameOBF();
    private static final boolean isForgeInstalled = VersionUtils.hasForge();
    private static final Map<String, Version> versionMap = new HashMap<String, Version>();
    public static final QuadrupleVersion BL_VERSION = new QuadrupleVersion("BlazeLoader.main", "BlazeLoader", BuildType.DEVELOPMENT, 1, 0, 0, 0);

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
     *
     * @return Return true is running on client, false for server.
     */
    public static boolean isClient() {
        return BLMain.instance().supportsClient();
    }

    /**
     * Detects if forge is installed.
     *
     * @return Return true if forge is installed
     */
    public static boolean isForgeInstalled() {
        return isForgeInstalled;
    }

    public static QuadrupleVersion getBLMainVersion() {
        return BL_VERSION;
    }

    public static Version getVersionOf(String id) {
        return versionMap.get(id);
    }

    public static void addVersion(Version version) {
        versionMap.put(version.getID(), version);
    }

    public static boolean isVersionRegistered(String id) {
        return versionMap.containsKey(id);
    }

}
