package com.blazeloader.util.reflect;

import com.blazeloader.bl.obf.BLOBF;

/**
 * A Function, of course.
 * <p>
 * Uses a combination of {@code MethodHandle} and {@code LamdaMetaFactory} to gain quicker access to methods out of reach.
 * 
 * @param <I> The class host for this method
 * @param <R> The return type for this method
 */
public class Func<I, R> extends Function<I, Object, R> {
	public Func(Class<I> context, Class<R> returnType, String name, Class<?>... pars) {
		this(context, returnType, name, false, pars);
	}
	
	public Func(Class<I> context, Class<R> returnType, String name, boolean isStatic, Class<?>... pars) {
		super(null, context, returnType, name, isStatic, pars);
	}
	
	public Func(BLOBF obf) {
		this(false, obf);
	}
	
	public Func(boolean isStatic, BLOBF obf) {
		super(null, isStatic, obf);
	}
	
	public Func(String descriptor) {
		this(false, descriptor);
	}
	
	public Func(boolean isStatic, String descriptor) {
		super(null, isStatic, descriptor);
	}
	
	/**
	 * Invokes the underlying method with the given arguments and null context.
	 * @param args			Object array of arguments.
	 * <p>
	 * Note: Calling a method through its lambda can be considerably faster than using call or apply depending on usage.
	 * 
	 * @return	The returned result of the method
	 * @throws Throwable if there is any error.
	 */
	@SuppressWarnings("unchecked")
	public R call(Object... args) throws Throwable {
		return (R)handle.target.invokeWithArguments(args);
	}
	
	/**
	 * Invokes the underlying method with the given arguments and instance context.
	 * 
	 * @param instance		The instance to bind to
	 * @param args			Object array of arguments.
	 * <p>
	 * Note: Calling a method through its lambda can be considerably faster than using call or apply depending on usage.
	 * 
	 * @return	The returned result of the method
	 * @throws Throwable if there is any error.
	 */
	@SuppressWarnings("unchecked")
	public R apply(I instance, Object... args) throws Throwable {
		if (handle.staticMethod) {
			return call(args);
		}
		return (R)handle.target.bindTo(instance).invokeWithArguments(args);
	}
}
