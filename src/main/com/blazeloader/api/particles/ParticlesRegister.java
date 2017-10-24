package com.blazeloader.api.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.blazeloader.bl.main.BLPacketChannels;
import com.blazeloader.bl.network.BLPacketParticles;
import com.blazeloader.util.shape.IShape;
import com.blazeloader.util.version.Versions;

/**
 * 
 * Manages the registration, creation, and spawning of custom particles. 
 *
 * Server side.
 * 
 * Warning: Not yet fully implemented. It may be best to keep spawning your stuff on the client for now.
 *
 */
public class ParticlesRegister<T> {
	protected static ParticlesRegister<?> instance;
	
	protected static final HashMap<String, IParticle> particleNames = new HashMap<String, IParticle>();
	protected static final HashMap<Integer, IParticle> particleIds = new HashMap<Integer, IParticle>();
	protected static final ArrayList<IParticle> particlesRegistry = new ArrayList<IParticle>();
	
	protected ParticlesRegister() {
		if (instance != null) {
			throw new IllegalStateException("ParticlesRegister has already been initialized. Cannot initialize again.");
		}
		instance = this;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <F> ParticlesRegister<F> instance() {
		if (instance == null) {
			if (Versions.isClient()) {
				return (ParticlesRegister<F>)new ParticlesRegisterClient();
			}
			return (ParticlesRegister<F>)new ParticlesRegister();
		}
		return (ParticlesRegister<F>)instance;
	}
	
	public static String[] getParticleNames() {
		return particleNames.keySet().toArray(new String[particleNames.size()]);
	}
	
	public static IParticle getParticleFromName(String name) {
		if (particleNames.containsKey(name)) {
			return particleNames.get(name);
		}
		return ParticleType.NONE;
	}
	
	public static IParticle getParticleFromEnum(EnumParticleTypes vanillaType) {
		if (particleIds.containsKey(vanillaType.getParticleID())) {
			return particleIds.get(vanillaType.getParticleID());
		}
		
		IParticle result = instance().getParticle(vanillaType);
		particleIds.put(vanillaType.getParticleID(), result);
		return result;
	}
	
	/**
	 * Initialises particle IDs and loads them into the vanilla registry for external API support.
	 * @param mapping	Mapping of pre-registered vanilla Particles
	 *
	 * @return A new, or previously cached, mapping with all custom particles added.
	 */
	public void preInit() {
		for (EnumParticleTypes i : EnumParticleTypes.values()) {
			if (!particleNames.containsKey(i.getParticleName())) particleNames.put(i.getParticleName(), getParticle(i));
		}
		Iterator<IParticle> types = particlesRegistry.iterator();
		for (int i = 0; types.hasNext();) {
			if (EnumParticleTypes.getParticleFromId(i) != null) {
				i++;
			} else {
				IParticle type = types.next();
				if (!particleIds.containsValue(type)) {
					particleIds.put(i, ((ParticleType)type).setId(i));
					i++;
				}
			}
		}
	}
	
	public void init(Map<Integer, T> mappings) {
	}
		
	public IParticle registerParticle(String name, boolean ignoreDistance, int argumentCount) {
		IParticle result = createParticleType(name, ignoreDistance, argumentCount);
		particlesRegistry.add(result);
		if (!particleNames.containsKey(name) && !name.endsWith("_")) {
			particleNames.put(name, result);
		}
		return result;
	}
	
	protected IParticle createParticleType(String name, boolean ignoreDistance, int argumentCount) {
		return new ParticleType(name, ignoreDistance, argumentCount);
	}
	
	public IParticle setFactory(IParticle particle, T factory) {
		return particle;
	}
	
	protected IParticle getParticle(EnumParticleTypes vanillaType) {
		return (new ParticleType(vanillaType.getParticleName(), vanillaType.getShouldIgnoreRange(), vanillaType.getArgumentCount())).setId(vanillaType.getParticleID());
	}
	
    public void addBlockHitEffectsToEntity(Entity e, IBlockState blockState) {
    	for (int side = 0; side < 6; side++) {
    		addBlockHitEffectsToEntity(e, blockState, side);
    	}
    }
    
    public void addBlockHitEffectsToEntity(Entity e, IBlockState blockState, int side) {
    	side = side % 6;
    	float f = 0.25f;
    	double x = MathHelper.nextDouble(e.getEntityWorld().rand, e.posX - e.width/2 - f, e.posX + e.width/2 + f);
    	double y = MathHelper.nextDouble(e.getEntityWorld().rand, e.posY - f, e.posY + e.height + f);
    	double z = MathHelper.nextDouble(e.getEntityWorld().rand, e.posZ - e.width/2 - f, e.posZ + e.width/2 + f);
    	
    	double vX = 0;
    	double vY = 0;
    	double vZ = 0;
    	
    	if (side == 0) y = e.posY - f;
        if (side == 1) {
        	y = e.posY + e.height + f;
        	vY += 0.5;
        }
        if (side == 2) {
        	z = e.posZ - e.width/2 - f;
        	vZ -= 0.5;
        }
        if (side == 3) {
        	z = e.posZ + e.width/2 + f;
        	vZ += 0.5;
        }
        if (side == 4) {
        	x = e.posX - e.width/2 - f;
        	vX -= 0.5;
        }
        if (side == 5) {
        	x = e.posX + e.width/2 + f;
        	vX += 0.5;
        }
        spawnDigginFX(e.getEntityWorld(), x, y, z, vX, vY, vZ, blockState, 0.2F, 0.6F);
    }
    
    public void addBlockDestroyEffectsToEntity(Entity e, IBlockState blockState) {
    	float f = 0.1f;
    	int total = 64 * (int)(e.width * e.height * e.width);
    	for (int i = 0; i < total; i++) {
	    	double x = MathHelper.nextDouble(e.getEntityWorld().rand, e.posX - e.width/2 - f, e.posX + e.width/2 + f);
	    	double y = MathHelper.nextDouble(e.getEntityWorld().rand, e.posY - f, e.posY + e.height + f);
	    	double z = MathHelper.nextDouble(e.getEntityWorld().rand, e.posZ - e.width/2 - f, e.posZ + e.width/2 + f);
	    	spawnDigginFX(e.getEntityWorld(), x, y, z, x - (int)x - 0.5, y - (int)y - 0.5, z - (int)z - 0.5, blockState, 1, 1);
    	}
    }
	
	public void spawnParticleShape(ParticleData particle, World world, double x, double y, double z, IShape shape, int total) {
		total *= shape.getVolumeOfSpawnableSpace();
		for (int i = 0; i < total; i++) {
			Vec3d point = shape.computePoint(world.rand);
			spawnParticle(particle.setPos(x + shape.getXOffset() + point.x, y + shape.getYOffset() + point.y, z + shape.getZOffset() + point.z), world);
		}
	}
	
	public void spawnParticleEmitter(Entity e, ParticleData particle) {
		if (particle.getType() == ParticleType.NONE) return;
		
	}
	
    public void spawnParticle(ParticleData particle, World world) {
    	if (particle.getType() == ParticleType.NONE) return;
    	EnumParticleTypes vanillaP = EnumParticleTypes.getParticleFromId(particle.getType().getId());
    	if (vanillaP != null) {
    		Packet<?> packet = new SPacketParticles(vanillaP, particle.getIgnoreDistance(), (float)particle.posX, (float)particle.posY, (float)particle.posZ, 0, 0, 0, (float)particle.getVel().lengthVector(), 1, particle.getArgs());
	        for (EntityPlayer player : (ArrayList<EntityPlayer>)(((WorldServer)world).playerEntities)) {
	            BlockPos pos = player.getPosition();
	            double dist = pos.distanceSq(particle.posX, particle.posY, particle.posZ);
	            if (dist <= particle.getMaxRenderDistance() || particle.getIgnoreDistance() && dist <= 65536.0D) {
	                ((EntityPlayerMP)player).connection.sendPacket(packet);
	            }
	        }
    	} else {
    		BLPacketParticles.Message message = new BLPacketParticles.Message(particle.getType(), particle.getIgnoreDistance(), (float)particle.posX, (float)particle.posY, (float)particle.posZ, 0, 0, 0, (float)particle.getVel().lengthVector(), 1, particle.getArgs());
    		for (EntityPlayer player : (ArrayList<EntityPlayer>)(((WorldServer)world).playerEntities)) {
	            BlockPos pos = player.getPosition();
	            double dist = pos.distanceSq(particle.posX, particle.posY, particle.posZ);
	            if (dist <= particle.getMaxRenderDistance() || particle.getIgnoreDistance() && dist <= 65536.0D) {
	            	BLPacketChannels.instance().sendToClient(message, (EntityPlayerMP)player);
	            }
	        }
    	}
    }
    
    //Do nothing. Since we're on the server.
    public void handleParticleSpawn(World w, BLPacketParticles.Message p) { }
    
    public void addEffectToRenderer(Object fx) { }
    //
    
    protected void spawnDigginFX(World w, double x, double y, double z, double vX, double vY, double vZ, IBlockState blockState, float multScale, float multVel) {
    	((WorldServer)w).spawnParticle(EnumParticleTypes.BLOCK_CRACK, false, x, y, z, 1, 0, 0, 0, Math.sqrt(vX * vX + vY * vY + vZ * vZ) * multVel, Block.getStateId(blockState));
    }
}
