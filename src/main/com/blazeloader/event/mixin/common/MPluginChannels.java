package com.blazeloader.event.mixin.common;

import org.spongepowered.asm.mixin.gen.Invoker;

import com.mumfrey.liteloader.core.CommonPluginChannelListener;

public interface MPluginChannels<L extends CommonPluginChannelListener> {
	@Invoker(remap=false)
	void addPluginChannelListener(L pluginChannelListener);
}
