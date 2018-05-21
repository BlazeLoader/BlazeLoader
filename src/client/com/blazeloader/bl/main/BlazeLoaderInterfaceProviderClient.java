package com.blazeloader.bl.main;

import com.blazeloader.event.handlers.client.EventHandlerClient;
import com.blazeloader.event.handlers.client.ResourcesEventHandler;
import com.blazeloader.event.listeners.client.ClientPlayerListener;
import com.blazeloader.event.listeners.client.ClientWorldListener;
import com.blazeloader.event.listeners.client.GuiListener;
import com.blazeloader.event.listeners.client.OverrideListener;
import com.blazeloader.event.listeners.client.ResourcesListener;
import com.mumfrey.liteloader.core.InterfaceRegistrationDelegate;

/**
 * BlazeLoader InterfaceProvider
 */
public class BlazeLoaderInterfaceProviderClient extends BlazeLoaderInterfaceProvider {
    
    protected BlazeLoaderInterfaceProviderClient() {
    	super();
    }
    
    @Override
    public void registerInterfaces(InterfaceRegistrationDelegate delegate) {
    	super.registerInterfaces(delegate);
        delegate.registerInterface(GuiListener.class);
        delegate.registerInterface(OverrideListener.class);
        delegate.registerInterface(ClientPlayerListener.class);
        delegate.registerInterface(ClientWorldListener.class);
        delegate.registerInterface(ResourcesListener.class);
    }
    
    public void addClientEvent(GuiListener e) {
        EventHandlerClient.guiEventClients.add(e);
    }
    
    public void addOverrideEvent(OverrideListener e) {
        EventHandlerClient.overrideEventClients.add(e);
    }
    
    public void addPlayerEvent(ClientPlayerListener e) {
        EventHandlerClient.playerEventClients.add(e);
    }
    
    public void addWorldEventHandler(ClientWorldListener e) {
        EventHandlerClient.worldEventClients.add(e);
    }
    
    public void addResourceHandler(ResourcesListener e) {
    	ResourcesEventHandler.resourcesReloaders.add(e);
    }
}
