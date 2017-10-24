package com.blazeloader.util.shape;

import java.util.Random;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * A lonely Line. The simplest form of shape.
 *
 */
public class Line implements IShape {
	
	double len;
	
	double dX;
	double dY;
	double dZ;
	
	double sX;
	double sY;
	double sZ;
	
	private float yaw = 0;
	private float pitch = 0;
	
	/**
	 * Creates a line with a given length, starting point, and gradient represented
	 * by another point.
	 * 
	 * @param length	Length of this line
	 * @param startX	Offset X from origin
	 * @param startY	Offset Y from origin
	 * @param startZ	Offset Z from origin
	 * @param deltaX	Change in X
	 * @param deltaY	Change in Y
	 * @param deltaZ	Change in Z
	 */
	public Line(double length, double startX, double startY, double startZ, double deltaX, double deltaY, double deltaZ) {
		len = length;
		dX = deltaX;
		dY = deltaY;
		dZ = deltaZ;
		sX = startX;
		sY = startY;
		sZ = startZ;
	}
	
	public double getVolumeOfSpawnableSpace() {
		return len;
	}
	
	public double getXOffset() {
		return sX;
	}
	
	public double getYOffset() {
		return sY;
	}
	
	public double getZOffset() {
		return sZ;
	}
	
	public Vec3d computePoint(Random rand) {
		double distance = MathHelper.nextDouble(rand, 0, len);
		return (new Vec3d(distance * dX, distance * dY, distance * dZ)).rotateYaw(yaw).rotatePitch(pitch);
	}
	
	public Line setRotation(float u, float v) {
		yaw = u;
		pitch = v;
		return this;
	}
	
	public boolean isPointInside(Vec3d point) {
		point = point.rotateYaw(-yaw).rotatePitch(-pitch);
		return point.x/dX == point.y/dY && point.x/dX == point.z/dZ;
	}
}
