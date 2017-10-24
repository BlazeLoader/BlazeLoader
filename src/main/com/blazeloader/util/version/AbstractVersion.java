package com.blazeloader.util.version;

import java.util.function.Function;

/**
 * Basic, abstract implementation of Version.
 * Automatically registers itself with Versions.
 * 
 * @param <T> This version to be returned from/given to factory functions.
 */
public abstract class AbstractVersion<T extends AbstractVersion<T>> implements Version<T> {
    private final String id;
    private final String name;
    private final BuildType buildType;
        
    protected final VersionNumber<T> versionNumber;
    
    @SuppressWarnings("unchecked")
	public AbstractVersion(String id, String name, BuildType buildType, int ... parts) {
        this.id = id;
        this.name = name;
        this.buildType = buildType;
        Versions.addVersion(this);
        versionNumber = new VersionNumber<T>((T)this, parts);
    }
    
    protected AbstractVersion(AbstractVersion<T> other) {
    	this.id = other.getID();
    	this.name = other.getName();
    	this.buildType = other.getBuildType();
    	this.versionNumber = other.versionNumber;
    }

    public AbstractVersion(String id, BuildType buildType) {
        this(id, id, buildType);
    }
    
    @Override
    public String getID() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public BuildType getBuildType() {
        return buildType;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public T setFormat(Function<VersionNumber<T>, String> func) {
    	versionNumber.setFormat(func);
    	return (T)this;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public T setComponentFormat(Function<VersionNumber<T>, String> func) {
    	versionNumber.setComponentFormat(func);
    	return (T)this;
    }
    
    @Override
    public int getVersionDepth() {
        return versionNumber.getDepth();
    }
    
    @Override
    public int getNthComponent(int num) {
        return versionNumber.get(num);
    }
    
    @Override
    public String getVersionString() {
    	return versionNumber.getVersionString();
    }
    
    @Override
    public String getComponentsString() {
    	return versionNumber.toString();
    }
    
    @Override
    public int hashCode() {
    	return id.hashCode() + versionNumber.hashCode() ^ id.length() + 1; 
    }
    
    @Override
    public boolean equals(Object other) {
    	return other == this || ((other instanceof AbstractVersion) && ((AbstractVersion<?>)other).versionNumber.equals(versionNumber));
    }
    
    @Override
    public String toString() {
    	return "{" + getID() + ", " + versionNumber.toString() + ", " + getName() + ", " + getBuildType().name() + "}";
    }
}
