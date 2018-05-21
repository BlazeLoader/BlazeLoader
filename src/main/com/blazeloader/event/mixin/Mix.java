package com.blazeloader.event.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class Mix {
	/**
	 * Cancels an event if the passed result is true (indicates method overriding).
	 */
	public static void intercept(boolean val, CallbackInfo info) {
		if (val) info.cancel();
	}
	
	/**
	 * Cancels the event and returns the passed result if it is non-null.
	 */
	public static <T> void intercept(T val, CallbackInfoReturnable<T> info) {
		if (val != null) info.setReturnValue(val);
	}
}
