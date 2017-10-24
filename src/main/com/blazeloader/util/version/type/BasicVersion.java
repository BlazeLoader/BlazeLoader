package com.blazeloader.util.version.type;

import com.blazeloader.util.version.AbstractVersion;
import com.blazeloader.util.version.BuildType;
import com.blazeloader.util.version.SaveableVersion;

/**
 * Basic implementation of a version. Supports saving and loading from a config file and can have any number of components.
 */
public class BasicVersion extends SaveableVersion<BasicVersion> {
	
	public BasicVersion(String id, String name, BuildType buildType, int... versionParts) {
		super(id, name, buildType, versionParts);
	}
	
	@SuppressWarnings("unchecked")
	protected BasicVersion(@SuppressWarnings("rawtypes") AbstractVersion other) {
		super(other);
	}
	
	protected BasicVersion createVersion(String id, String name, BuildType buildType, int... parts) {
		return new BasicVersion(id, name, buildType, parts);
	}
}
