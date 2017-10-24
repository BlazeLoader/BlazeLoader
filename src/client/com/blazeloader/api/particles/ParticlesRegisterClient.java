package com.blazeloader.api.particles;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
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
public class ParticlesRegisterClient extends ParticlesRegister<IParticleFactory> {
	private static Map<Integer, IParticleFactory> vanillaRegistry;
	
	protected ParticlesRegisterClient() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	public static ParticlesRegister<IParticleFactory> instance() {
		return ParticlesRegister.instance();
	}
	
	@Override
	public void preInit() {
		//Do nothing, initilialising is done after RenderManager init
	}
	
	@Override
	public void init(Map<Integer, IParticleFactory> mapping) {
		if (vanillaRegistry == null || !vanillaRegistry.equals(mapping)) {
			vanillaRegistry = mapping;
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
	}
	
	@Override
	protected IParticle createParticleType(String name, boolean ignoreDistance, int argumentCount) {
		return new ParticleTypeClient(name, ignoreDistance, argumentCount);
	}
	
	@Override
	public IParticle setFactory(IParticle particle, IParticleFactory factory) {
		return ((ParticleTypeClient)particle).setFactory(factory);
	}
	
	@Override
	protected IParticle getParticle(EnumParticleTypes vanillaType) {
		return setFactory((new ParticleTypeClient(vanillaType.getParticleName(), vanillaType.getShouldIgnoreRange(), vanillaType.getArgumentCount())).setId(vanillaType.getParticleID()), getVanillaParticleRegistry().get(vanillaType.getParticleID()));
	}
	
	@Override
	public void spawnParticleEmitter(Entity e, ParticleData particle) {
		addEffectToRenderer(new ParticleEmitter(e.getEntityWorld(), e, particle));
	}
	
	@Override
    public void spawnParticle(ParticleData particle, World world) {
    	if (particle.getType() == ParticleType.NONE) return;
    	
    	Minecraft mc = ApiClient.getClient();
		if (mc != null && mc.effectRenderer != null) {
			Entity view = mc.getRenderViewEntity();
			if (view != null) {
	            int particleSetting = mc.gameSettings.particleSetting;
	
	            if (particleSetting == 1 && mc.world.rand.nextInt(3) == 0) {
	            	particleSetting = 2;
	            }
	            
	            IParticleFactory factory = ((ParticleTypeClient)particle.getType()).getFactory();
	            try {
		            if (particle.getIgnoreDistance()) {
		            	spawnCustomParticle(particle, factory, world);
		            } else {
	    	            double disX = view.posX - particle.posX;
	    	            double disY = view.posY - particle.posY;
	    	            double disZ = view.posZ - particle.posZ;
		                if (disX * disX + disY * disY + disZ * disZ <= particle.getMaxRenderDistance() && particleSetting <= 1) {
		                	spawnCustomParticle(particle, factory, world);
		                }
		            }
	            } catch (Throwable e) {
	            	reportParticleError(e, factory, particle.getType(), particle.posX, particle.posY, particle.posZ, particle.getArgs());
	            }
			}
        }
    }
    
    private void spawnCustomParticle(ParticleData particle, IParticleFactory factory, World world) {
    	addEffectToRenderer(factory.createParticle(particle.getType().getId(), world, particle.posX, particle.posY, particle.posZ, particle.velX, particle.velY, particle.velZ, particle.getArgs()));
    }
    
    private void reportParticleError(Throwable e, IParticleFactory factory, IParticle particle, final double x, final double y, final double z, int[] args) {
    	CrashReport report = CrashReport.makeCrashReport(e, "Exception while adding custom particle");
        CrashReportCategory category = report.makeCategory("Particle being added");
    	category.addCrashSection("ID", particle.getId());
    	category.addCrashSection("Particle Factory Class", factory == null ? "Null" : factory.getClass().toString());
    	
        if (args != null && args.length > 0) category.addCrashSection("Parameters", args);
        category.addDetail("Position", new ICrashReportDetail<String>() {
            public String call() {
                return CrashReportCategory.getCoordinateInfo(x, y, z);
            }
        });
        throw new ReportedException(report);
    }
    
    @Override
    public void addEffectToRenderer(Object fx) {
    	if (fx != null && fx instanceof Particle) {
    		ApiClient.getEffectRenderer().addEffect((Particle)fx);
    	}
    }
    
    private Map<Integer, IParticleFactory> getVanillaParticleRegistry() {
		return vanillaRegistry;
	}
    
    @Override
	protected void spawnDigginFX(World w, double x, double y, double z, double vX, double vY, double vZ, IBlockState blockState, float multScale, float multVel) {
    	addEffectToRenderer(buildDiggingEffect(w, x, y, z, vX, vY, vZ, blockState).setBlockPos(new BlockPos((int)x, (int)y, (int)z)).multiplyVelocity(multScale).multipleParticleScaleBy(multVel));
    }
    
    protected ParticleDigging buildDiggingEffect(World w, double x, double y, double z, double vX, double vY, double vZ, IBlockState blockState) {
    	return (ParticleDigging)(new ParticleDigging.Factory()).createParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), w, x, y, z, vX, vY, vZ, Block.getStateId(blockState));
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
    			BLMain.LOGGER_FULL.warn("Could not spawn particle effect " + p.getType().getName());
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
                	BLMain.LOGGER_FULL.warn("Could not spawn particle effect " + p.getType().getName());
                    return;
                }
            }
    	}
    	
    }
}
