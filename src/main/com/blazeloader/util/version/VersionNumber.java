package com.blazeloader.util.version;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.blazeloader.util.JSArrayUtils;
import com.google.common.collect.Lists;

/**
 * Class containing the implementation details for a version's numberic component
 *
 */
public class VersionNumber<T extends Version<T>> {
	private final int versionDepth;
	
	protected final Integer[] versionParts;
	
	private String componentString = null;
    private String versionString = null;
    
	private final T owner;
	
    protected Function<VersionNumber<T>, String> formattingFunc;
    protected Function<VersionNumber<T>, String> componentFormattingFunc;
	
	public VersionNumber(T owner, int ... parts) {
		this.owner = owner;
		if (parts.length == 0) {
            throw new IllegalArgumentException("versionParts cannot be null and must have at least one element!");
        }
		
		versionDepth = parts.length;
		boolean started = false;
        List<Integer> trimmedVersionParts = new ArrayList<Integer>();
        for (int i = parts.length - 1; i >= 0; i--) {
        	if (parts[i] < 0) {
        		parts[i] = 0;
        	}
        	if (parts[i] != 0) started = true;
        	if (started) {
        		trimmedVersionParts.add(parts[i]);
        	}
        }
        
        versionParts = JSArrayUtils.toArray(Lists.reverse(trimmedVersionParts), Integer.class);
	}
	
	public int getDepth() {
        return versionDepth;
    }
	
	public int get(int num) {
		if (num < versionDepth) {
        	if (num < 0) num = 0;
        	if (num > versionParts.length) return 0;
            return versionParts[num];
        }
        throw new IllegalArgumentException("num must be less than getVersionDepth!");
	}
	
	public T getVersion() {
		return owner;
	}
	
    public String getVersionString() {
    	if (versionString == null) {
    		if (formattingFunc != null) {
    			versionString = formattingFunc.apply(this);
    		} else {
    			versionString = (owner.getBuildType().humanReadable() + " " + componentString).trim();
    		}
    	}
        return versionString;
    }
    
    public void setFormat(Function<VersionNumber<T>, String> func) {
    	formattingFunc = func;
    }
    
    public void setComponentFormat(Function<VersionNumber<T>, String> func) {
    	componentFormattingFunc = func;
    }
    
    @Override
    public int hashCode() {
    	return versionParts.hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
    	return other == this || (other instanceof VersionNumber && ((VersionNumber<?>)other).versionParts == versionParts);
    }
    
    @Override
    public String toString() {
    	if (componentString == null) {
    		if (componentFormattingFunc != null) {
    			componentString = componentFormattingFunc.apply(this);
    		} else {
    			componentString = versionParts.length > 0 ? JSArrayUtils.join(versionParts, ".") : "null";
    		}
    	}
    	return componentString;
    }
}
