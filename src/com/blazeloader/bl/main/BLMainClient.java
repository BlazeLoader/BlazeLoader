package com.blazeloader.bl.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandHandler;
import net.minecraft.server.integrated.IntegratedServerCommandManager;

import com.blazeloader.api.client.ApiClient;
import com.google.common.collect.Lists;
import com.mumfrey.liteloader.api.InterfaceProvider;
import com.mumfrey.liteloader.launch.LoaderEnvironment;
import com.mumfrey.liteloader.launch.LoaderProperties;

/**
 * Client BLMain.
 */
public class BLMainClient extends BLMain {
	
    BLMainClient(LoaderEnvironment environment, LoaderProperties properties) {
        super(environment, properties);
    }
    
    @Override
    public String[] getRequiredTransformers() {
        String[] superRequiredArr = super.getRequiredTransformers();
        ArrayList<String> list = null;
        if (superRequiredArr != null) {
        	list = Lists.newArrayList(superRequiredArr);
        } else {
        	list = new ArrayList(1);
        }
        list.add("com.blazeloader.event.transformers.BLEventInjectionTransformerClient");
        return list.toArray(new String[list.size()]);
    }
    
    @Override
    protected boolean initiateShutdown() {
    	Minecraft minecraft = Minecraft.getMinecraft();
    	if (minecraft == null) return false;
    	LOGGER_FULL.logFatal("Shutting down client...");
    	minecraft.shutdown();
    	return true;
    }
    
    public List<InterfaceProvider> getInterfaceProviders() {
    	return Collections.singletonList(new BlazeLoaderInterfaceProviderClient());
    }
    
    @Override
    public void tick(boolean clock, float partial, boolean isInGame) {
    	Minecraft client = ApiClient.getClient();
    	if (client != null) {
    		partialTicks = partial;
    		numTicks = client.timer.elapsedTicks;
    	} else {
    		partialTicks = numTicks = 0;
    	}
    }
    
    @Override
    public boolean supportsClient() {
        return true;
    }
    
    @Override
    protected CommandHandler createCommandHandler() {
    	return new IntegratedServerCommandManager();
    }
}
