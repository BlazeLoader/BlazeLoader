package com.blazeloader.util.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that this method requires an access transformation on some other member in order to function.
 * This an informational annotation intended purely for developer assistance to allow
 * easier documentation and tracking of access transformation as well as the effect their removal might have.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface AccessTransform {
	/**
	 * The class(es) owning the affected member(s).
	 */
	public Class<?>[] value() default { };
	
	/**
	 * Members affected.
	 */
	public String[] target() default { };
	
	/**
	 * The type of transformation (can be anything).
	 */
	public String[] action() default { };
}
