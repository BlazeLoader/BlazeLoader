package com.blazeloader.util;

public class Strings {
	public static String toProperCase(String s) {
		char[] chars = s.toCharArray();
		chars[0] = ((Character)chars[0]).toString().toUpperCase().charAt(0);
		return new String(chars);
	}
}
