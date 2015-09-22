package com.blazeloader.bl.obf;

import com.mumfrey.liteloader.core.runtime.Obf;
import com.mumfrey.liteloader.transformers.event.MethodInfo;

import net.acomputerdog.core.java.Patterns;

/**
 * BL extension of MethodInfo that allows getting all data from a single obfuscation
 */
public class BLMethodInfo extends MethodInfo {
    private final String simpleName;

    private BLMethodInfo(Obf owner, String method, String desc, String name) {
        super(owner, method, desc);
        this.simpleName = getMethodName(name);
    }

    public String getSimpleName() {
        return simpleName;
    }

    //---------------------[Static stuff]----------------------------

    public static BLMethodInfo create(BLOBF method) {
        if (method == null || method.getValue() == null) {
            return null;
        }
        
        String[] methAndDesc = method.getValue().split(Patterns.SPACE);
        if (methAndDesc.length < 2) {
            throw new IllegalArgumentException("Method ID must contain method name and descriptor separated by a space!");
        }
        String desc = methAndDesc[1];
        String[] nameParts = methAndDesc[0].split(Patterns.PERIOD);
        if (nameParts.length < 2) {
            throw new IllegalArgumentException("Method name must contain class and method name!");
        }
        String name = nameParts[nameParts.length - 1];

        Obf owner = getObfType(getClassName(nameParts));
        return new BLMethodInfo(owner, name, desc, method.name);
    }

    private static Obf getObfType(String name) {
    	return BLOBF.getClass(name, OBFLevel.getCurrent());
    }

    private static String getClassName(String[] parts) {
        if (parts == null || parts.length == 0) {
            return null;
        }
        if (parts.length == 1) {
            return "";
        }
        StringBuilder builder = new StringBuilder(parts.length - 1);

        builder.append(parts[0]);
        for (int index = 1; index < parts.length - 1; index++) {
            builder.append(".");
            builder.append(parts[index]);
        }
        return builder.toString();
    }

    private static String getMethodName(String method) {
        if (method == null) {
            return null;
        }
        if ("".equals(method)) {
            return "";
        }
        String[] parts = method.split(" ")[0].split(Patterns.PERIOD);
        return parts[parts.length - 1];
    }
}
