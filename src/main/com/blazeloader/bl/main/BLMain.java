package com.blazeloader.bl.main;

import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.command.CommandHandler;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;

import com.blazeloader.api.ApiServer;
import com.blazeloader.bl.interop.IFML;
import com.blazeloader.util.version.BuildType;
import com.blazeloader.util.version.Version;
import com.blazeloader.util.version.type.BasicVersion;
import com.mumfrey.liteloader.api.CoreProvider;
import com.mumfrey.liteloader.api.CustomisationProvider;
import com.mumfrey.liteloader.api.InterfaceProvider;
import com.mumfrey.liteloader.launch.LoaderEnvironment;
import com.mumfrey.liteloader.launch.LoaderProperties;

/**
 * BL main class
 */
public class BLMain {
    private static BLMain instance;

    /**
     * Logger that logs date and time
     */
    public static final Logger LOGGER_FULL = LogManager.getLogger("BlazeLoader");
    
    private static final Version<?> BL_VERSION = new BasicVersion("BlazeLoader.main", "BlazeLoader", BuildType.BETA, 1);
    
    /**
     * Gets the version of blazeloader running on the current game
     */
    public static Version<?> getVersion() {
        return BL_VERSION;
    }
    
    /**
     * the partial render tick for the client
     */
    protected static float partialTicks = 0;
    
    /**
     * number of ticks that the game has been running
     */
    protected static int numTicks = 0;

    public final LoaderEnvironment environment;
    public final LoaderProperties properties;

    private CommandHandler commandHandler;
    
    public static BLMain instance() {
        return instance;
    }
    
    public static boolean isClient() {
    	return instance == null || instance.supportsClient();
    }
    
    public static float getPartialTicks() {
    	return partialTicks;
    }
    
    public static int getTicks() {
    	return numTicks;
    }
    
    BLMain(LoaderEnvironment environment, LoaderProperties properties) {
        if (instance != null) {
            throw new IllegalStateException("BLMain cannot be created twice!");
        }
        instance = this;
        this.environment = environment;
        this.properties = properties;
        
        LOGGER_FULL.info("BlazeLoader initialized.");
    }

    public String[] getRequiredTransformers() {
    	return new String[]{ "com.blazeloader.util.transformers.ONFTransformer" };
    }
    
    public List<CoreProvider> getCoreProviders() {
        return Collections.singletonList(BlazeLoaderCoreProvider.instance());
    }
    
    public List<InterfaceProvider> getInterfaceProviders() {
    	return Collections.singletonList(BlazeLoaderInterfaceProvider.instance());
    }
    
    public List<CustomisationProvider> getCustomisationProviders() {
        return Collections.singletonList(BlazeLoaderBrandingProvider.instance());
    }
    
    public final void shutdown(String message, int code) {
        try {
            LOGGER_FULL.fatal("Unexpected shutdown detected!");
            LOGGER_FULL.fatal(message);
            if (!initiateShutdown()) {
                LOGGER_FULL.fatal("Game is not running, closing immediately with code %s!", code);
                if (IFML.isForge()) {
                	IFML.instance().exitJava(code, false);
                } else {
                	System.exit(code);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            if (IFML.isForge()) {
            	IFML.instance().exitJava(code, false);
            } else {
            	System.exit(code);
            }
        }
    }
    
    protected boolean initiateShutdown() {
    	MinecraftServer server = ApiServer.getServer();
    	if (server == null) return false;
    	LOGGER_FULL.info("Shutting down server...");
    	server.initiateShutdown();
    	return true;
    }
    
    public void tick(boolean clock, float partialTicks, boolean isInGame) {
    	MinecraftServer server = ApiServer.getServer();
    	if (server != null) {
    		numTicks = server.getTickCounter();
    	} else {
    		numTicks = 0;
    	}
    }
    
    public boolean supportsClient() {
        return false;
    }

    public CommandHandler getCommandHandler(MinecraftServer server) {
        return commandHandler == null ? commandHandler = createCommandHandler(server) : commandHandler;
    }

    protected CommandHandler createCommandHandler(MinecraftServer server) {
    	return new ServerCommandManager(server);
    }

    public String getPluginChannelName() {
        return "BLAZELOADER";
    }
    
    public String[] getMixinConfigs() {
		return new String[] {
			"mixins.blazeloader.json",
			"mixins.blazeloader.client.json"
		};
	}
}
