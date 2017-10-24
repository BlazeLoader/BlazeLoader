package com.blazeloader.bl.obf;

import java.net.URL;

import com.blazeloader.util.regex.Patterns;
import com.blazeloader.util.transformers.ONFTransformer;
import com.blazeloader.util.version.Versions;
import com.mumfrey.liteloader.core.runtime.Obf;

import net.acomputerdog.OBFUtil.map.ObfMapSrg;
import net.acomputerdog.OBFUtil.map.TargetType;
import net.acomputerdog.OBFUtil.parse.types.ONFParser;

/**
 * BlazeLoader extension of LL's Obf class
 */
public class BLOBF extends Obf implements ObfMapSrg.Entry {
	
    /**
     * The simple (mcp, no package) name of this BLOBF
     */
    public final String simpleMcp;
    public final String simpleObf;
    public final String simpleSrg;
    
    /**
     * Creates a new BLOBF.
     *
     * @param obfName    The obfuscated name of the class
     * @param seargeName The searge name of the class
     * @param mcpName    The deobfuscated (mcp) name of the class
     */
    protected BLOBF(String obfName, String seargeName, String mcpName) {
        super(seargeName, obfName, mcpName);
        simpleMcp = splitPackageOff(mcpName);
        simpleObf = splitPackageOff(obfName);
        simpleSrg = splitPackageOff(seargeName);
    }
    
    private String splitPackageOff(String string) {
    	String[] nameParts = string.split(Patterns.PERIOD);
    	return nameParts.length > 0 ? nameParts[nameParts.length - 1] : string;
    }
    
    /**
     * Gets the obf/srg/mcp name of this class based on the current obfuscation mode.
     *
     * @return Return the mcp name if the game is deobfuscated, return the srg name if forge is installed, return the obf name if the game is obfuscated.
     */
    public String getValue() {
        if (!Versions.isGameObfuscated()) {
            return name;
        }
        if (Versions.isForgeInstalled()) {
            return srg;
        }
        
        return obf;
    }
    
    /**
     * Fix for BLOBF not matching against method names.
     */
    public boolean matches(String name) {
    	return super.matches(name) ||
    			simpleObf.equals(name) || simpleObf.split(" ")[0].equals(name) ||
    			simpleSrg.equals(name) || simpleSrg.split(" ")[0].equals(name) ||
    			simpleMcp.equals(name) || simpleMcp.split(" ")[0].equals(name);
    }

    //-----------------------------[Static Stuff]------------------------------------

    /**
     * BL's central obfuscation table, contains all raw package, class, method, and field obfuscation mappings.
     */
    public static final BLOBFTable<?> OBF = loadOBF();
    
    private static BLOBFTable<?> loadOBF() {
        @SuppressWarnings("rawtypes")
		BLOBFTable<?> obf = new BLOBFTable();
        ONFParser parser = new ONFParser();
        loadEntries(parser, "blazeloader", obf, true);
        ONFTransformer.setONFS(parser.getDetectedTransformations());
        return obf;
    }
    
    private static void loadEntries(ONFParser parser, String filename, BLOBFTable<?> obf, boolean mustThrow) {
    	try {
    		URL stream = BLOBF.class.getResource("/conf/" + filename + ".onf");
    		parser.loadEntries(stream, obf, true);
    	} catch (Exception e) {
    		if (mustThrow) {
    			throw new RuntimeException("Unable to load obfuscation table; BlazeLoader cannot start!", e);
    		} else {
    			e.printStackTrace();
    		}
    	}
    }
    
    /**
     * Gets a BLOBF from an obfuscated name.
     *
     * @param obfName The obfuscated name.
     * @param type    The type of object to get.
     * @param level	  The obfuscation level MCP/SRG/OBF
     * @return Return a BLOBF created from an obfuscated name.
     */
    public static BLOBF getOBF(String obfName, TargetType type, OBFLevel level) {
        return OBF.getBLOBF(obfName, type, level);
    }
    
	public String obf() {
		return obf;
	}
	
	public String deObf() {
		return name;
	}
	
	public String srg() {
		return srg;
	}
}
