package com.blazeloader.api.particles;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;

/**
 * Used to store information about a particle.
 */
public final class ParticleData {
	private IParticle type;
	private boolean ignoreDist;
	private int[] args;
	private double maxRenderDistance = 256;
	
	protected double posX;
	protected double posY;
	protected double posZ;
	
	protected double velX;
	protected double velY;
	protected double velZ;
	
	/**
	 * Creates a ParticleData
	 *
	 * @param type        IParticle factory to be used when creating the spawned particle
	 * @param fulcrum		Override whether the particle follows the distance rule.
	 * @param arguments		Additional arguments given to the particle when creating.
	 * 
	 * @return ParticleData containing all the given information
	 */
	public static ParticleData get(IParticle type, boolean fulcrum, int ...arguments) {
		return new ParticleData(type, fulcrum, arguments);
	}
		
	/**
	 * Creates a ParticleData
	 * 
	 * @param type			Vanilla Particle type.
	 * @param fulcrum		Override whether the particle follows the distance rule.
	 * @param arguments		Additional arguments given to the particle when creating.
	 * 
	 * @return ParticleData containing all the given information
	 */
	public static ParticleData get(EnumParticleTypes type, boolean fulcrum, int ...arguments) {
		return new ParticleData(ApiParticles.getParticleFromEnum(type), fulcrum, arguments);
	}
	
	private ParticleData(IParticle particle, boolean fulcrum, int[] data) {
		type = particle;
		ignoreDist = fulcrum;
		args = data;
	}
	
	/**
	 * Gets the ParticleType used to identify this particle.
	 * 
	 * @return ParticleType
	 */
	public IParticle getType() {
		return type;
	}
	
	/**
	 * Get the int[] array of extra arguments given to a particle when created.
	 * 
	 * @return int[] array
	 */
	public int[] getArgs() {
		return args;
	}
	
	/**
	 * Returns true if this particle can be spawned outside of the particle render distance (256 blocks)
	 * 
	 * @return boolean
	 */
	public boolean getIgnoreDistance() {
		return ignoreDist;
	}
	
	/**
	 * Sets the maximum distance that the particle can be spawned.
	 * default 256 blocks.
	 * @param dist The maximum render distance
	 */
	public ParticleData setMaxRenderDistance(double dist) {
		maxRenderDistance = dist;
		return this;
	}
	
	/**
	 * Gets the maximum distance that the particle can be spawned.
	 * default 256 blocks.
	 */
	public double getMaxRenderDistance() {
		return maxRenderDistance;
	}
	
	/**
	 * Sets the position for particles spawned from the ParticleData.
	 * 
	 * @param pos	Vec3 position
	 * 
	 * @return this ParticleData
	 */
	public ParticleData setPos(Vec3d pos) {
		return setPos(pos.x, pos.y, pos.z);
	}
	
	/**
	 * Sets the position for particles spawned from the ParticleData.
	 * 
	 * @param x		X position
	 * @param y		Y position
	 * @param z		Z position
	 * @return	this ParticleData
	 */
	public ParticleData setPos(double x, double y, double z) {
		posX = x;
		posY = y;
		posZ = z;
		return this;
	}
	
	/**
	 * Gets the position of the particle represented by the ParticleData
	 * @return Vec3 position
	 */
	public Vec3d getPos() {
		return new Vec3d(posX, posY, posZ);
	}
	
	/**
	 * Sets the velocity for particles spawned from the ParticleData.
	 * 
	 * @param vel	Vec3 velocity
	 * @return	this ParticleData
	 */
	public ParticleData setVel(Vec3d vel) {
		return setVel(vel.x, vel.y, vel.z);
	}
	
	/**
	 * Sets the velocity for particles spawned from the ParticleData.
	 * 
	 * @param x		X velocity
	 * @param y		Y velocity
	 * @param z		Z velocity
	 * @return	this ParticleData
	 */
	public ParticleData setVel(double x, double y, double z) {
		velX = x;
		velY = y;
		velZ = z;
		return this;
	}
	
	/**
	 * Gets the velocity of the particle represented by the ParticleData
	 * @return Vec3 velocity
	 */
	public Vec3d getVel() {
		return new Vec3d(velX, velY, velZ);
	}
	
	/**
	 * Sets the position for particles spawned from the ParticleData.
	 * 
	 * @param pos	Vec3 position
	 * 
	 * @return this ParticleData
	 */
	public ParticleData withPos(Vec3d pos) {
		return setPos(pos.x, pos.y, pos.z);
	}
	
	/**
	 * Creates a new ParticleData with the given position.
	 * The original is left unmodified.
	 * 
	 * @param x		X position
	 * @param y		Y position
	 * @param z		Z position
	 * @return	this ParticleData
	 */
	public ParticleData withPos(double x, double y, double z) {
		return copy(x, y, z, velX, velY, velZ);
	}
	
	/**
	 * Creates a new ParticleData with the given position.
	 * The original is left unmodified.
	 * 
	 * @param vel	Vec3 velocity
	 * @return	this ParticleData
	 */
	public ParticleData withVel(Vec3d vel) {
		return withVel(vel.x, vel.y, vel.z);
	}
	
	/**
	 * Creates a new ParticleData with the given velocity.
	 * The original is left unmodified.
	 * 
	 * @param x		X velocity
	 * @param y		Y velocity
	 * @param z		Z velocity
	 * @return	this ParticleData
	 */
	public ParticleData withVel(double x, double y, double z) {
		return copy(posX, posY, posZ, x, y, z);
	}
	
	private ParticleData copy(double x, double y, double z, double vx, double vy, double vz) {
		ParticleData result = new ParticleData(type, ignoreDist, args);
		result.setPos(x, y, z);
		result.setVel(vx, vy, vz);
		return result;
	}
}











