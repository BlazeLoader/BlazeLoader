package com.blazeloader.util.version;

import com.blazeloader.util.config.IStringable;

/**
 * Abstract Specialisation of MultiPartVersion with an IStringable implementation.  
 * 
 * @param <T> This version to be returned from/given to factory functions.
 */
public abstract class SaveableVersion<T extends SaveableVersion<T>> extends AbstractVersion<T> implements IStringable<T> {
	public SaveableVersion(String id, String name, BuildType buildType, int... versionParts) {
		super(id, name, buildType, versionParts);
	}
	
	protected SaveableVersion(AbstractVersion<T> other) {
		super(other);
	}
	
    @Override
	public T fromString(String string) {
		string = string.trim();
		if (string.startsWith("{")) string = string.substring(1);
		if (string.endsWith("}")) string = string.substring(0, string.length() - 1);
		string = string.trim();
		String[] parts = string.split(", ");
		if (parts.length == 3) {
			String[] versionString = parts[0].split(".");
			int[] versionParts = new int[versionString.length - 1];
			for (int i = 1; i < versionString.length; i++) {
				try {
					versionParts[i - 1] = Integer.valueOf(versionString[i]);
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("Invalid string", e);
				}
			}
			BuildType buildType = BuildType.STABLE;
			try {
				buildType = BuildType.valueOf(parts[2].toUpperCase());
			} catch (Throwable e) {
				throw new IllegalArgumentException("Invalid string", e);
			}
			return createVersion(versionString[0], parts[1], buildType, versionParts);
		}
		return null;
	}
    
    protected abstract T createVersion(String id, String name, BuildType buildType, int... parts);
}
