package com.blazeloader.util.shape;

import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

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
	
	public Vec3 computePoint(Random rand) {
		double x = MathHelper.getRandomDoubleInRange(rand, 0, width);
		double y = MathHelper.getRandomDoubleInRange(rand, 0, depth);
		double z = MathHelper.getRandomDoubleInRange(rand, 0, height);
		return (new Vec3(x, y, z)).rotateYaw(yaw).rotatePitch(pitch);
	}
	
	public Rect setRotation(float u, float v) {
		yaw = u;
		pitch = v;
		return this;
	}
	
	public boolean isPointInside(Vec3 point) {
		point = point.rotateYaw(-yaw).rotatePitch(-pitch);
		double x = Math.abs(point.xCoord);
		double y = Math.abs(point.yCoord);
		double z = Math.abs(point.zCoord);
		if (hollow) return x == width/2 && y == height/2 && z == depth/2;
		return x <= width/2 && y <= height/2 && z <= depth/2;
	}
}
