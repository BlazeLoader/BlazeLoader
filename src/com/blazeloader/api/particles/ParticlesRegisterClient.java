package com.blazeloader.api.particles;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;

import com.blazeloader.api.client.ApiClient;
import com.blazeloader.bl.main.BLMain;
import com.blazeloader.bl.network.BLPacketParticles;

/**
 * 
 * Manages the registration, creation, and spawning of custom particles. 
 *
 * Client Side implementation
 *
 */
public class ParticlesRegisterClient extends ParticlesRegister {
	private static Map<Integer, IParticleFactory> vanillaRegistry;
	
	protected ParticlesRegisterClient() {
		super();
	}
	
	public void initialiseIds() {
		if (isRendererReady()) {
			syncroniseParticlesRegistry(getVanillaParticleRegistry());
		}
	}
	
	/**
	 * Initialises particle IDs and loads them into the vanilla registry for external API support.
	 * @param mapping	Mapping of pre-registered vanilla Particles
	 *
	 * @return A new, or previously cached, mapping with all custom particles added.
	 */
	//FIXME: This has to be linked up at the bottom of EffectRenderer.func_178930_c(). There might be a better place for this method though.
	public static Map<Integer, IParticleFactory> syncroniseParticlesRegistry(Map<Integer, IParticleFactory> mapping) {
		if (vanillaRegistry == null || !vanillaRegistry.equals(mapping)) {
			vanillaRegistry = mapping;
			int injected = 0;
			Iterator<IParticle> types = particlesRegistry.iterator();
			for (int i = 0; types.hasNext();) {
				if (mapping.containsKey(i)) {
					i++;
				} else {
					ParticleTypeClient type = (ParticleTypeClient)types.next();
					if (!particleIds.containsValue(type)) {
						mapping.put(i, type.getFactory());
						particleIds.put(i, type.setId(i));
						i++;
					} else {
						if (!mapping.containsKey(type.getId())) {
							mapping.put(type.getId(), type.getFactory());
						}
					}
				}
			}
		}
		return vanillaRegistry;
	}
	
	@Override
	protected IParticle createParticleType(String name, boolean ignoreDistance, int argumentCount) {
		return new ParticleTypeClient(name, ignoreDistance, argumentCount);
	}
	
	@Override
	public IParticle setFactory(IParticle particle, Object factory) {
		return ((ParticleTypeClient)particle).setFactory((IParticleFactory)factory);
	}
	
	@Override
	protected IParticle getParticle(EnumParticleTypes vanillaType) {
		return setFactory((new ParticleTypeClient(vanillaType.getParticleName(), vanillaType.func_179344_e(), vanillaType.getArgumentCount())).setId(vanillaType.getParticleID()), getVanillaParticleRegistry().get(vanillaType.getParticleID()));
	}
	
	@Override
	public void spawnParticleEmitter(Entity e, ParticleData particle) {
		addEffectToRenderer(new ParticleEmitter(e.worldObj, e, particle));
	}
	
	@Override
    public void spawnParticle(ParticleData particle, World world) {
    	if (particle.getType() == ParticleType.NONE) return;
    	
    	Minecraft mc = Minecraft.getMinecraft();
		if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null) {
            int particleSetting = mc.gameSettings.particleSetting;

            if (particleSetting == 1 && mc.theWorld.rand.nextInt(3) == 0) {
            	particleSetting = 2;
            }
            
            IParticleFactory factory = ((ParticleTypeClient)particle.getType()).getFactory();
            try {
	            if (particle.getIgnoreDistance()) {
	            	spawnCustomParticle(particle, factory, world);
	            } else {
    	            double disX = mc.getRenderViewEntity().posX - particle.posX;
    	            double disY = mc.getRenderViewEntity().posY - particle.posY;
    	            double disZ = mc.getRenderViewEntity().posZ - particle.posZ;
	                if (disX * disX + disY * disY + disZ * disZ <= particle.getMaxRenderDistance() && particleSetting <= 1) {
	                	spawnCustomParticle(particle, factory, world);
	                }
	            }
            } catch (Throwable e) {
            	reportParticleError(e, factory, particle.getType(), particle.posX, particle.posY, particle.posZ, particle.getArgs());
            }
        }
    }
    
    private void spawnCustomParticle(ParticleData particle, IParticleFactory factory, World world) {
    	addEffectToRenderer(factory.getEntityFX(particle.getType().getId(), world, particle.posX, particle.posY, particle.posZ, particle.velX, particle.velY, particle.velZ, particle.getArgs()));
    }
    
    private void reportParticleError(Throwable e, IParticleFactory factory, IParticle particle, final double x, final double y, final double z, int[] args) {
    	CrashReport report = CrashReport.makeCrashReport(e, "Exception while adding custom particle");
        CrashReportCategory category = report.makeCategory("Particle being added");
    	category.addCrashSection("ID", particle.getId());
    	category.addCrashSection("Particle Factory Class", factory == null ? "Null" : factory.getClass().toString());
    	
        if (args != null && args.length > 0) category.addCrashSection("Parameters", args);
        category.addCrashSectionCallable("Position", new Callable() {
            public String call() {
                return CrashReportCategory.getCoordinateInfo(x, y, z);
            }
        });
        throw new ReportedException(report);
    }
    
    @Override
    public void addEffectToRenderer(Entity fx) {
    	if (fx != null && fx instanceof EntityFX) {
    		Minecraft.getMinecraft().effectRenderer.addEffect((EntityFX)fx);
    	}
    }
    
    private boolean isRendererReady() {
    	return ApiClient.getEffectRenderer() != null;
    }
    
    private Map<Integer, IParticleFactory> getVanillaParticleRegistry() {
		return ApiClient.getEffectRenderer().particleTypes;
	}
    
    @Override
	protected void spawnDigginFX(World w, double x, double y, double z, double vX, double vY, double vZ, IBlockState blockState, float multScale, float multVel) {
    	addEffectToRenderer(buildDiggingEffect(w, x, y, z, vX, vY, vZ, blockState).func_174846_a(new BlockPos((int)x, (int)y, (int)z)).multiplyVelocity(multScale).multipleParticleScaleBy(multVel));
    }
    
    protected EntityDiggingFX buildDiggingEffect(World w, double x, double y, double z, double vX, double vY, double vZ, IBlockState blockState) {
    	return (EntityDiggingFX)(new EntityDiggingFX.Factory()).getEntityFX(EnumParticleTypes.BLOCK_CRACK.getParticleID(), w, x, y, z, vX, vY, vZ, Block.getStateId(blockState));
    }
    
    @Override
    public void handleParticleSpawn(World w, BLPacketParticles.Message p) {
    	ParticleData particle = ParticleData.get(p.getType(), p.isLongDistance(), p.getArguments());
    	
    	if (p.getCount() == 0) {
    		particle.setPos(p.getX(), p.getY(), p.getZ());
    		particle.setVel(p.getSpeed() * p.getXOffset(), p.getSpeed() * p.getYOffset(), p.getSpeed() * p.getZOffset());
    		try {
    			spawnParticle(particle, w);
    		} catch (Throwable e) {
    			BLMain.LOGGER_MAIN.logWarning("Could not spawn particle effect " + p.getType().getName());
    		}
    	} else {
    		for (int i = 0; i < p.getCount(); i++) {
                double xOffset = p.getX() + (w.rand.nextGaussian() * p.getXOffset());
                double yOffset = p.getY() + (w.rand.nextGaussian() * p.getYOffset());
                double zOffset = p.getZ() + (w.rand.nextGaussian() * p.getZOffset());
                double velX = w.rand.nextGaussian() * p.getSpeed();
                double velY = w.rand.nextGaussian() * p.getSpeed();
                double velZ = w.rand.nextGaussian() * p.getSpeed();
                
                particle.setPos(xOffset, yOffset, zOffset);
                particle.setVel(velX, velY, velZ);
                
                try {
                	spawnParticle(particle, w);
                } catch (Throwable e) {
                	BLMain.LOGGER_MAIN.logWarning("Could not spawn particle effect " + p.getType().getName());
                    return;
                }
            }
    	}
    	
    }
}
