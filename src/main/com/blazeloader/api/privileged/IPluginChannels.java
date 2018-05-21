package com.blazeloader.api.privileged;

import com.mumfrey.liteloader.core.CommonPluginChannelListener;

public interface IPluginChannels<L extends CommonPluginChannelListener> {
	void callAddPluginChannelListener(L pluginChannelListener);
}
