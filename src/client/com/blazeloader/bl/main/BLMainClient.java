package com.blazeloader.bl.main;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.integrated.IntegratedServerCommandManager;

import com.blazeloader.api.client.ApiClient;
import com.mumfrey.liteloader.api.InterfaceProvider;
import com.mumfrey.liteloader.client.overlays.IMinecraft;
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
    protected boolean initiateShutdown() {
    	Minecraft minecraft = Minecraft.getMinecraft();
    	if (minecraft == null) return false;
    	LOGGER_FULL.fatal("Shutting down client...");
    	minecraft.shutdown();
    	return true;
    }
    
    @Override
    public List<InterfaceProvider> getInterfaceProviders() {
    	return Collections.singletonList(new BlazeLoaderInterfaceProviderClient());
    }
    
    @Override
    public void tick(boolean clock, float partial, boolean isInGame) {
    	IMinecraft client = (IMinecraft)ApiClient.getClient();
    	if (client != null) {
    		partialTicks = partial;
    		numTicks = client.getTimer().elapsedTicks;
    	} else {
    		partialTicks = numTicks = 0;
    	}
    }
    
    @Override
    public boolean supportsClient() {
        return true;
    }
    
    @Override
    protected CommandHandler createCommandHandler(MinecraftServer server) {
    	return new IntegratedServerCommandManager((IntegratedServer)server);
    }
}
