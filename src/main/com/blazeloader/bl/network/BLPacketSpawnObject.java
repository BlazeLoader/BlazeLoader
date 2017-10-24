package com.blazeloader.bl.network;

import java.io.IOException;

import com.blazeloader.api.entity.ApiEntity;
import com.blazeloader.api.entity.ApiProjectile;
import com.blazeloader.api.network.IMessage;
import com.blazeloader.api.network.IMessageHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.world.World;

public class BLPacketSpawnObject implements IMessageHandler<BLPacketSpawnObject.Message, IMessage, INetHandler> {
	
	public IMessage onMessage(Message message, INetHandler net) {
		double x = (double)message.wrapped.getX() / 32.0D;
        double y = (double)message.wrapped.getY() / 32.0D;
        double z = (double)message.wrapped.getZ() / 32.0D;
        World world = Minecraft.getMinecraft().world;
        Entity entity = EntityList.createEntityByID(message.wrapped.getType(), world);
        
        if (entity != null) {
        	entity.setPosition(x, y, z);
        	EntityTracker.updateServerPosition(entity, message.wrapped.getX(), message.wrapped.getY(), message.wrapped.getZ());
            entity.rotationPitch = (float)(message.wrapped.getPitch() * 360) / 256f;
            entity.rotationYaw = (float)(message.wrapped.getYaw() * 360) / 256f;
            Entity[] parts = entity.getParts();

            if (parts != null) {
                int off = message.wrapped.getEntityID() - entity.getEntityId();

                for (int i = 0; i < parts.length; ++i) {
                    parts[i].setEntityId(parts[i].getEntityId() + off);
                }
            }

            entity.setEntityId(message.wrapped.getEntityID());
            entity.setUniqueId(message.wrapped.getUniqueId());
            ((WorldClient)world).addEntityToWorld(message.wrapped.getEntityID(), entity);

            if (message.wrapped.getData() > 0) {
            	if (ApiProjectile.isProjectile(entity)) {
            		Entity thrower = ((WorldClient)world).getEntityByID(message.wrapped.getData() - 1);
            		if (thrower instanceof EntityLivingBase) {
            			ApiProjectile.setThrowingEntity(entity, thrower);
            		}
            	}
                entity.setVelocity((double)message.wrapped.getSpeedX() / 8000.0D, (double)message.wrapped.getSpeedY() / 8000.0D, (double)message.wrapped.getSpeedZ() / 8000.0D);
            }
        }
		return null;
	}
	
	public static class Message implements IMessage {
		
		private SPacketSpawnObject wrapped;
		
		public Message() {
			
		}
		
		public Message(Entity e, int unknown) {
			wrapped = new SPacketSpawnObject(e, ApiEntity.getEntityID(e));
		}
		
		public void fromBytes(ByteBuf buf) {
			wrapped = new SPacketSpawnObject();
			try {
				wrapped.readPacketData(new PacketBuffer(buf));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void toBytes(ByteBuf buf) {
			try {
				wrapped.writePacketData(new PacketBuffer(buf));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
