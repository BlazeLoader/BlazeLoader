package com.blazeloader.bl.main;

import com.blazeloader.api.ApiGeneral;

import java.io.File;

/**
 * Global settings.
 */
public class Settings {

    /**
     * The folder where config files are stored.  Mods do not have to obey this, but should if possible.
     */
    public static String configDir = new File(ApiGeneral.mainDir.getPath(), "/BL/config/").getPath();
}
