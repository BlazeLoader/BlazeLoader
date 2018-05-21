package com.blazeloader.bl.main;

import java.io.File;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;

import com.blazeloader.api.network.PacketChannel;
import com.blazeloader.api.network.Side;
import com.blazeloader.api.privileged.IPluginChannels;
import com.blazeloader.bl.network.BLPacketParticles;
import com.blazeloader.bl.network.BLPacketSpawnObject;
import com.blazeloader.event.listeners.PacketChannelListener;
import com.blazeloader.util.version.Versions;
import com.google.common.collect.ImmutableList;
import com.mumfrey.liteloader.core.LiteLoader;

public class BLPacketChannels extends PacketChannel implements PacketChannelListener {
	private static final BLPacketChannels instance = new BLPacketChannels();
	
	public BLPacketChannels() {
		super(BLMain.instance().getPluginChannelName());
		registerMessageHandler(Side.CLIENT, new BLPacketParticles(), BLPacketParticles.Message.class, 0);
		registerMessageHandler(Side.CLIENT, new BLPacketSpawnObject(), BLPacketSpawnObject.Message.class, 1);
	}
	
	public static BLPacketChannels instance() {
		return instance;
	}
	
	@Override
	public String getName() {
		return BLMain.instance().getPluginChannelName();
	}

	@Override
	public List<String> getChannels() {
		return ImmutableList.of(getName());
	}
	
	@Override
	public String getVersion() {
		return null;
	}

	@Override
	public void init(File configPath) {}

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {}

	@Override
	public void onCustomPayload(EntityPlayerMP sender, String channel, PacketBuffer data) {
		onPacketRecievedServer(channel, sender, data);
	}
	
	@Override
	public void onCustomPayload(String channel, PacketBuffer data) {
		if (Versions.isClient()) {
			onPacketRecievedClient(channel, data);
		}
	}
	
	/**
	 * Hack to get Blazeloader PluginChannels registered without having to make a Litemod.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void register() {
		BLMain.LOGGER_FULL.info("Registering Blazeloader packet channel...");
		((IPluginChannels)LiteLoader.getServerPluginChannels()).callAddPluginChannelListener(this);
		if (Versions.isClient()) {
			((IPluginChannels)LiteLoader.getClientPluginChannels()).callAddPluginChannelListener(this);
		}
		BLMain.LOGGER_FULL.debug("Packet channel registration complete.");
	}
}
