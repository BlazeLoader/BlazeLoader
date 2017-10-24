package com.blazeloader.util.math;

public class MathUtils {
	/**
	 * Returns true only if the given string contains an integer value.
	 */
	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {}
		return false;
	}
}
