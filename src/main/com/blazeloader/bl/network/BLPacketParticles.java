package com.blazeloader.bl.network;

import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

import com.blazeloader.api.client.ApiClient;
import com.blazeloader.api.network.IMessage;
import com.blazeloader.api.network.IMessageHandler;
import com.blazeloader.api.particles.IParticle;
import com.blazeloader.api.particles.ParticleType;
import com.blazeloader.api.particles.ParticlesRegister;

import io.netty.buffer.ByteBuf;

public class BLPacketParticles implements IMessageHandler<BLPacketParticles.Message, IMessage, INetHandler> {
	
	public IMessage onMessage(Message message, INetHandler net) {
		ParticlesRegister.instance().handleParticleSpawn(ApiClient.getPlayer().getEntityWorld(), message);
		return null;
	}
	
	public static class Message implements IMessage {
		private IParticle particleType;
	    private float xCoord;
	    private float yCoord;
	    private float zCoord;
	    private float xOffset;
	    private float yOffset;
	    private float zOffset;
	    private float particleSpeed;
	    private int particleCount;
	    private boolean ignoreDistance;
	    private int[] arguments;

	    public Message() {}

	    public Message(IParticle type, boolean longDist, float x, float y, float z, float xOffsetIn, float yOffsetIn, float zOffsetIn, float speed, int count, int ... args) {
	        particleType = type;
	        ignoreDistance = longDist;
	        xCoord = x;
	        yCoord = y;
	        zCoord = z;
	        xOffset = xOffsetIn;
	        yOffset = yOffsetIn;
	        zOffset = zOffsetIn;
	        particleSpeed = speed;
	        particleCount = count;
	        arguments = args;
	    }
	    
	    public void fromBytes(ByteBuf bytes) {
	    	PacketBuffer buf = new PacketBuffer(bytes);
	    	
	        particleType = ParticlesRegister.getParticleFromName(buf.readString(32767));
	        
	        if (particleType == null) particleType = ParticleType.NONE;
	        
	        ignoreDistance = buf.readBoolean();
	        xCoord = buf.readFloat();
	        yCoord = buf.readFloat();
	        zCoord = buf.readFloat();
	        xOffset = buf.readFloat();
	        yOffset = buf.readFloat();
	        zOffset = buf.readFloat();
	        particleSpeed = buf.readFloat();
	        particleCount = buf.readInt();
	        arguments = new int[particleType.getArgumentCount()];
	        for (int i = 0; i < arguments.length; i++) {
	            arguments[i] = buf.readVarInt();
	        }
	    }
	    
		@Override
		public void toBytes(ByteBuf bytes) {
			PacketBuffer buf = new PacketBuffer(bytes);
			
	        buf.writeString(particleType.getName());
	        buf.writeBoolean(ignoreDistance);
	        buf.writeFloat(xCoord);
	        buf.writeFloat(yCoord);
	        buf.writeFloat(zCoord);
	        buf.writeFloat(xOffset);
	        buf.writeFloat(yOffset);
	        buf.writeFloat(zOffset);
	        buf.writeFloat(particleSpeed);
	        buf.writeInt(particleCount);
	        for (int i = 0; i < particleType.getArgumentCount(); ++i) {
	            buf.writeVarInt(arguments[i]);
	        }
	    }

	    public IParticle getType() {
	        return particleType;
	    }

	    public boolean isLongDistance() {
	        return ignoreDistance;
	    }
	    
	    public double getX() {
	        return xCoord;
	    }
	    
	    public double getY() {
	        return yCoord;
	    }
	    
	    public double getZ() {
	        return zCoord;
	    }
	    
	    public float getXOffset() {
	        return xOffset;
	    }
	    
	    public float getYOffset() {
	        return yOffset;
	    }
	    
	    public float getZOffset() {
	        return zOffset;
	    }
	    
	    public float getSpeed() {
	        return particleSpeed;
	    }
	    
	    public int getCount() {
	        return particleCount;
	    }
	    
	    public int[] getArguments() {
	        return arguments;
	    }
	}
}