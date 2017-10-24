package com.blazeloader.api.network;

import java.util.HashMap;
import java.util.Map;

import com.blazeloader.api.client.ApiClient;
import com.blazeloader.util.version.Versions;
import com.mumfrey.liteloader.core.ClientPluginChannels;
import com.mumfrey.liteloader.core.ServerPluginChannels;
import com.mumfrey.liteloader.core.PluginChannels.ChannelPolicy;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;

/**
 * An interface for sending/recieving mod packets.
 * <p>
 * Intended for use together with LiteLoader's PluginChannels.
 *
 */
public class PacketChannel {
	
	private final Map<Integer, PacketEntry<?>> server_mapping = new HashMap<Integer, PacketEntry<?>>();
	private final Map<Integer, PacketEntry<?>> client_mapping = new HashMap<Integer, PacketEntry<?>>();
	
	private final Map<Class<? extends IMessage>, Integer> server_packetClasses = new HashMap<Class<? extends IMessage>, Integer>();
	private final Map<Class<? extends IMessage>, Integer> client_packetClasses = new HashMap<Class<? extends IMessage>, Integer>();
	
	private final String channelName;
	
	public PacketChannel(String identifier) {
		channelName = identifier;
	}
	
	/**
	 * Called to handle a packet received on the client at PluginChannelListener.onCustomPayload
	 * <p>
	 * 
	 * @param channel	The channel being received
	 * @param data		Packet data
	 */
	public void onPacketRecievedClient(String channel, PacketBuffer data) {
		try {
			if (channelName.contentEquals(channel)) {
				int id = data.readInt();
				if (client_mapping.containsKey(id)) {
					INetHandler net = ApiClient.getClient().getConnection();
					IMessage responce = client_mapping.get(id).onPacketRecieved(net, data);
					if (responce != null) {
						sendToServer(responce);
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Called to handle a packet received on the server at ServerPluginChannelListener.onCustomPayload
	 * 
	 * @param channel	The channel being received
	 * @param sender	Player entity we received this message from
	 * @param data		Packet data
	 */
	public void onPacketRecievedServer(String channel, EntityPlayerMP sender, PacketBuffer data) {
		try {
			if (channelName.contentEquals(channel)) {
				int id = data.readInt();
				if (server_mapping.containsKey(id)) {
					IMessage responce = server_mapping.get(id).onPacketRecieved(sender.connection, data);
					if (responce != null) {
						sendToClient(responce, sender);
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Registers a message handler on  this channel.
	 * 
	 * @param handlerSide	The side this handler works. Client/Server/Both
	 * @param handler		The handler object
	 * @param messageClass	Type of message it receives
	 * @param packetId		Id for the packets it receives
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends IMessage> void registerMessageHandler(Side handlerSide, IMessageHandler<T, ?, ?> handler, Class<T> messageClass, int packetId) {
		new PacketEntry(handlerSide, handler, messageClass, packetId);
	}
	
	/**
	 * Sends a message to the server.
	 * <p>
	 * Only has an effect on Clients.
	 * 
	 * @param message	The message to send
	 */
	public void sendToServer(IMessage message) {
		if (Versions.isClient()) {
			Integer id = server_packetClasses.get(message.getClass());
			if (id == null) {
				throw new UnrecognisedMessageException(channelName, message, "SERVER");
			}
			PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
			buf.writeInt(id);
			message.toBytes(buf);
			ClientPluginChannels.sendMessage(channelName, buf, ChannelPolicy.DISPATCH_ALWAYS);
		}
	}
	
	/**
	 * Sends a message to a client.
	 * 
	 * @param message	The message to send
	 * @param recipient	Player to recieve the message
	 */
	public void sendToClient(IMessage message, EntityPlayerMP recipient) {
		Integer id = client_packetClasses.get(message.getClass());
		if (id == null) {
			throw new UnrecognisedMessageException(channelName, message, "SERVER");
		}
		PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
		buf.writeInt(id);
		message.toBytes(buf);
		ServerPluginChannels.sendMessage(recipient, channelName, buf, ChannelPolicy.DISPATCH_ALWAYS);
	}
	
	/**
	 * Packs the given message to raw packet data.
	 * 
	 * @param message	Message to unpack
	 * 
	 * @return a PacketBuffer with the message's data and id
	 */
	public PacketBuffer getRawData(IMessage message) {
		Integer id = client_packetClasses.get(message.getClass());
		if (id == null) {
			id = server_packetClasses.get(message.getClass());
		}
		if (id == null) {
			throw new UnrecognisedMessageException(channelName, message, "ANY");
		}
		PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
		buf.writeInt(id);
		message.toBytes(buf);
		return buf;
	}
	
	/**
	 * Packages the given message into a Packet ready to be sent over the network.
	 * <p>
	 * This method serves as a bridge between Mojang code and LiteLoader/Blazeloader message handling.
	 * 
	 * @param message	Message to package
	 * 
	 * @return	A Packet with the given data that once, received on the opposite side, will be brought back to be handled by this PacketChannel.
	 */
	public Packet<?> getRawPacket(IMessage message) {
		PacketBuffer data = getRawData(message);
        return new SPacketCustomPayload(channelName, data);
	}
	
	private class PacketEntry<P extends IMessage> {
		public IMessageHandler<P, IMessage, INetHandler> messageHandler;
		public Class<P> packetClass;
		
		public PacketEntry(Side side, IMessageHandler<P, IMessage, INetHandler> handler, Class<P> packet, int identifier) {
			messageHandler = handler;
			packetClass = packet;
			Integer id = Integer.valueOf(identifier);
			if (side == Side.CLIENT || side == Side.BOTH) {
				client_mapping.put(id, this);
				client_packetClasses.put(packet, id);
			}
			if (side == Side.SERVER || side == Side.BOTH) {
				server_mapping.put(id, this);
				server_packetClasses.put(packet, id);
			}
		}
		
		public IMessage onPacketRecieved(INetHandler net, PacketBuffer data) throws InstantiationException, IllegalAccessException {
			P packet = packetClass.newInstance();
			packet.fromBytes(data);
			return messageHandler.onMessage(packet, net);
		}
	}
	
}
