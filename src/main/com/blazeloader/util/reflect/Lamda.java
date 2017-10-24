package com.blazeloader.util.reflect;

import com.blazeloader.bl.obf.BLOBF;

/**
 * A Function, of course.
 * <p>
 * Uses a combination of {@code MethodHandle} and {@code LamdaMetaFactory} to gain quicker access to methods out of reach.
 * 
 * @param <I> The class host for this method
 * @param <T> The interface type containing the method signature for lamda generation.
 * @param <R> The return type for this method
 */
public class Lamda<I, T, R> extends Function<I, T, R> {
	public Lamda(Class<T> interfaceType, Class<I> context, Class<R> returnType, String name, Class<?>... pars) {
		this(interfaceType, context, returnType, name, false, pars);
	}
	
	public Lamda(Class<T> interfaceType, Class<I> context, Class<R> returnType, String name, boolean isStatic, Class<?>... pars) {
		super(interfaceType, context, returnType, name, isStatic, pars);
	}
	
	public Lamda(Class<T> interfaceType, BLOBF obf) {
		this(interfaceType, false, obf);
	}
	
	public Lamda(Class<T> interfaceType, boolean isStatic, BLOBF obf) {
		super(interfaceType, isStatic, obf);
	}
	
	public Lamda(Class<T> interfaceType, String descriptor) {
		this(interfaceType, false, descriptor);
	}
	
	public Lamda(Class<T> interfaceType, boolean isStatic, String descriptor) {
		super(interfaceType, isStatic, descriptor);
	}
	
	protected Lamda(Lamda<I, T,R> other) {
		super(other);
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
	public R apply(Object instance, Object... args) throws Throwable {
		if (handle.staticMethod) {
			return call(args);
		}
		return (R)handle.target.bindTo(instance).invokeWithArguments(args);
	}
	
	/**
	 * Gets a lambda object built from the given interface class with the underlying method as its implementation.
	 * <p>
	 * Note: Calling a method through its lambda can be considerably faster than using call or apply depending on usage.
	 * 
	 * @param instance		An instance to bind to.
	 * 
	 * @return lambda T
	 * @throws Throwable if there is any error.
	 */
	public T getLambda(I instance) throws Throwable {
		return (T)handle.factory.bindTo(instance).invoke();
	}
	
	/**
	 * Attempts to get a copy of this method bound to the given instance.
	 * <br>
	 * Will simply return {@code this} if it is a static method.
	 * 
	 * @param instance	The object instance to bind to
	 * 
	 * @return The bound method or {@code this} if static.
	 */
	public Function<I, T, R> bindTo(I instance) {
		if (handle.staticMethod) {
			return this;
		}
		return new BoundFunc<I, T, R>(instance, this);
	}
}