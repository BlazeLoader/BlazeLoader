package com.blazeloader.api.entity.profession;

public enum MatingCondition {
	/**
	 * Does this villager have enough food to breed?
	 */
	BREED(1),
	/**
	 * Does this villager have enough food to give away?
	 */
	GIVE(2),
	/**
	 * Does this villager need more feed?
	 */
	FEED(5);
	
	private final int multiplier;
	
	private MatingCondition(int multiplier) {
		this.multiplier = multiplier;
	}
	
	/**
	 * Gets the stack multiplier for this condition as used in vanilla minecraft
	 */
	public int multiplier() {
		return this.multiplier;
	}
}
