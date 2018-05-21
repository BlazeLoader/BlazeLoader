package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.blazeloader.api.privileged.IPluginChannels;
import com.mumfrey.liteloader.core.CommonPluginChannelListener;
import com.mumfrey.liteloader.core.PluginChannels;

@Mixin(PluginChannels.class)
public abstract class MPluginChannels<L extends CommonPluginChannelListener> implements IPluginChannels<L> {
	@Shadow(remap=false)
	abstract void addPluginChannelListener(L pluginChannelListener);
	
	@Override
	public void callAddPluginChannelListener(L pluginChannelListener) {
		addPluginChannelListener(pluginChannelListener);
	}
}
