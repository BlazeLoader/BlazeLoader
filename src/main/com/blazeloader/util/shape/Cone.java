package com.blazeloader.util.shape;

import java.util.Random;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Pointy
 */
public class Cone implements IShape {
	
	private final Vec3d stretch;
	
	private final boolean hollow;
	private final double height;
	private final double rad;
	
	private float yaw = 0;
	private float pitch = 0;
	
	/**
	 * Creates a uniform cone
	 * 
	 * @param height		The maximum distance from the base to point of this cone
	 * @param baseRadius	Radius measure at the base
	 */
	public Cone(boolean hollow, double height, double baseRadius) {
		this(hollow, height, baseRadius, 1, 1, 1);
	}
	
	/**
	 * Creates a cone of arbitrary dimensions
	 * 
	 * @param height		The maximum distance from the base to point of this cone
	 * @param baseRadius	Radius measure at the base
	 * @param stretchX	Warp this shape's X-axis
	 * @param stretchY	Warp this shape's Y-axis
	 * @param stretchZ	Warp this shape's Z-axis
	 */
	public Cone(boolean hollow, double height, double baseRadius, float stretchX, float stretchY, float stretchZ) {
		this.hollow = hollow;
		this.height = height;
		rad = baseRadius;
		stretch = new Vec3d(stretchX, stretchY, stretchZ);
	}
	
	public double getVolumeOfSpawnableSpace() {
		if (hollow) return Math.PI * rad * (rad + Math.sqrt((height * height) + (rad * rad)));
		return Math.PI * (rad * rad) * height/3;
	}
	
	public double getXOffset() {
		return 0;
	}
	
	public double getYOffset() {
		return 0;
	}
	
	public double getZOffset() {
		return 0;
	}
	
	public Vec3d computePoint(Random rand) {
		double pheta = MathHelper.nextDouble(rand, 0, Math.PI * 2);
		double phi = Math.abs(Math.atan(rad / height));
		
		if (!hollow) {
			phi = MathHelper.nextDouble(rand, 0, phi);
		}
		
		double rho = MathHelper.nextDouble(rand, 0, height);
		
		double x = rho * Math.sin(phi) * Math.cos(pheta);
		double y = rho * Math.sin(phi) * Math.sin(pheta);
		double z = rho * Math.cos(phi);
		
		return (new Vec3d(x * stretch.x, y * stretch.y, z * stretch.z)).rotateYaw(yaw).rotatePitch(pitch);
	}
	
	public Cone setRotation(float u, float v) {
		yaw = u;
		pitch = v;
		return this;
	}

	@Override
	public boolean isPointInside(Vec3d point) {
		point = point.rotateYaw(-yaw).rotatePitch(-pitch);
		point = new Vec3d(point.x / stretch.x, point.y / stretch.y, point.z / stretch.z);
		if (Math.abs(point.y) < height/2) {
			double phi = Math.acos(point.z / point.lengthVector());
			double maxphi = Math.atan(rad / height);
			return hollow ? phi <= maxphi : phi == maxphi;
		}
		return Math.abs(point.y) == height/2;
	}
}
