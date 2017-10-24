package com.blazeloader.bl.main;

import com.mumfrey.liteloader.api.*;
import com.mumfrey.liteloader.launch.LoaderEnvironment;
import com.mumfrey.liteloader.launch.LoaderProperties;

import java.util.List;

/**
 * BlazeLoader LiteAPI
 */
public class BlazeLoaderAPI implements LiteAPI, MixinConfigProvider {
    @Override
    public void init(LoaderEnvironment environment, LoaderProperties properties) {
        if (environment.getType() == LoaderEnvironment.EnvironmentType.CLIENT) {
            new BLMainClient(environment, properties);
        } else {
            new BLMain(environment, properties);
        }
    }
    
    @Override
    public String getIdentifier() {
        return getName();
    }
    
    @Override
    public String getName() {
        return "BlazeLoader";
    }
    
    @Override
    public String getVersion() {
    	return BLMain.getVersion().getVersionString();
    }
    
    @Override
    public int getRevision() {
        return BLMain.getVersion().getNthComponent(2);
    }
    
    @Override
    public String[] getRequiredTransformers() {
        return BLMain.instance().getRequiredTransformers();
    }
    
    public MixinConfigProvider getMixins() {
    	return this;
    }
    
    @Override
    public String[] getRequiredDownstreamTransformers() {
        return null;
    }
    
    @Override
    public String getModClassPrefix() {
        return "BlazeMod";
    }
    
    @Override
    public List<EnumeratorModule> getEnumeratorModules() {
        return null;
    }
    
    @Override
    public List<CoreProvider> getCoreProviders() {
        return BLMain.instance().getCoreProviders();
    }
    
    @Override
    public List<InterfaceProvider> getInterfaceProviders() {
        return BLMain.instance().getInterfaceProviders();
    }

    @Override
    public List<Observer> getPreInitObservers() {
        return null;
    }
    
    @Override
    public List<Observer> getObservers() {
        return null;
    }
    
    @Override
    public List<CustomisationProvider> getCustomisationProviders() {
        return BLMain.instance().getCustomisationProviders();
    }
    
	@Override
	public String[] getMixinConfigs() {
		return BLMain.instance().getMixinConfigs();
	}

	@Override
	public String[] getErrorHandlers() {
		return null;
	}
}
