package com.blazeloader.util.shape;

import java.util.Random;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * A rectangle.
 *
 */
public class Rect implements IShape {
	
	double width;
	double height;
	double depth;
	
	boolean hollow;
	
	private float yaw = 0;
	private float pitch = 0;
	
	/**
	 * Creates a square.
	 * 
	 * @param hollow	True if this shape must be hollow.
	 * @param side		Length of side.
	 */
	public Rect(boolean hollow, double side) {
		this(hollow, side, side, side);
	}
	
	/**
	 * Creates a rectangle of arbitrary dimension.
	 * 
	 * @param hollow	True if this shape must be hollow.
	 * @param height	Length along the Y-axis
	 * @param depth		Length along the Z-axis
	 * @param width		Length along the X-axis
	 */
	public Rect(boolean hollow, double height, double depth, double width) {
		this.hollow = hollow;
		this.height = height;
		this.width = width;
		this.depth = depth;
	}
	
	public double getVolumeOfSpawnableSpace() {
		return hollow ? (2*height*width) + (2*height*depth) + (2*width*depth) : height * width * depth;
	}
	
	public double getXOffset() {
		return -width/2;
	}
	
	public double getYOffset() {
		return -height/2;
	}
	
	public double getZOffset() {
		return -depth/2;
	}
	
	public Vec3d computePoint(Random rand) {
		double x = MathHelper.nextDouble(rand, 0, width);
		double y = MathHelper.nextDouble(rand, 0, depth);
		double z = MathHelper.nextDouble(rand, 0, height);
		return (new Vec3d(x, y, z)).rotateYaw(yaw).rotatePitch(pitch);
	}
	
	public Rect setRotation(float u, float v) {
		yaw = u;
		pitch = v;
		return this;
	}
	
	public boolean isPointInside(Vec3d point) {
		point = point.rotateYaw(-yaw).rotatePitch(-pitch);
		double x = Math.abs(point.x);
		double y = Math.abs(point.y);
		double z = Math.abs(point.z);
		if (hollow) return x == width/2 && y == height/2 && z == depth/2;
		return x <= width/2 && y <= height/2 && z <= depth/2;
	}
}
