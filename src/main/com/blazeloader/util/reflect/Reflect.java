package com.blazeloader.util.reflect;

import com.blazeloader.bl.obf.BLOBF;

/**
 * A collection of utility methods for getting fields and methods.
 */
public final class Reflect {
	/**
	 * Finds a hook into a field by a given BLOBF mapping.
	 */
	public static <T, V> Var<T, V> lookupField(BLOBF obf) {
		return new Var<T, V>(obf);
	}
	
	/**
	 * Finds a hook into a field by its declaring class, field type and field name.
	 * 
	 * @param declarer	The class in which this field is declared
	 * @param type		The type of values this field takes
	 * @param name		The name of this field
	 */
	public static <T, V> Var<T, V> lookupField(Class<T> declarer, Class<V> type, String name, String... aliases) {
		return new Var<T, V>(declarer, type, name, aliases);
	}
	
	/**
	 * Finds a hook into a field by its string descriptor.
	 */
	public static <T, V> Var<T, V> lookupField(String descriptor) {
		return new Var<T, V>(descriptor);
	}
	
	/**
	 * Finds a hook into a method by a given BLOBF mapping.
	 * <p>
	 * This version does not support lambda creation.
	 */
	public static <I, R> Func<I, R> lookupMethod(BLOBF obf) {
		return new Func<I, R>(obf);
	}
	
	/**
	 * Finds a hook into a method by its declaring class, return type, name, and parameter types.
	 * <p>
	 * This version does not support lambda creation.
	 *  
	 * @param declarer			The class in which this method is declared.
	 * @param returnType		The method return type
	 * @param name				The method name
	 * @param pars				The parameter types this method takes
	 */
	public static <I, R> Func<I, R> lookupMethod(Class<I> declarer, Class<R> returnType, String name, Class<?>... pars) {
		return new Func<I, R>(declarer, returnType, name, pars);
	}
	
	/**
	 * Finds a hook into a method by a given BLOBF mapping.
	 * 
	 * @param interfaceType		An interface for the generated lambda to implement. Must contain a method matching the one you wish to access.
	 */
	public static <I, T, R> Lamda<I, T, R> lookupMethod(Class<T> interfaceType, BLOBF obf) {
		return new Lamda<I, T, R>(interfaceType, obf);
	}
	
	/**
	 * Finds a hook into a method by its declaring class, return type, name, and parameter types.
	 * 
	 * @param interfaceType		An interface for the generated lambda to implement. Must contain a method matching the one you wish to access. 
	 * @param declarer			The class in which this method is declared.
	 * @param returnType		The method return type
	 * @param name				The method name
	 * @param pars				The parameter types this method takes
	 */
	public static <I, T, R> Lamda<I, T, R> lookupMethod(Class<T> interfaceType, Class<I> declarer, Class<R> returnType, String name, Class<?>... pars) {
		return new Lamda<I, T, R>(interfaceType, declarer, returnType, name, pars);
	}
	
	/**
	 * Finds a hook into a method by its string descriptor.
	 * 
	 * @param interfaceType		An interface for the generated lambda to implement. Must contain a method matching the one you wish to access.
	 */
	public static <I, T, R> Lamda<I, T, R> lookupMethod(Class<T> interfaceType, String descriptor) {
		return new Lamda<I, T, R>(interfaceType, descriptor);
	}
	
	/**
	 * Finds a hook into a method by its string descriptor.
	 * <p>
	 * This version does not support lambda creation.
	 */
	public static <I, R> Func<I, R> lookupMethod(String descriptor) {
		return new Func<I, R>(descriptor);
	}
	
	/**
	 * Finds a hook into a static field by a given BLOBF mapping.
	 */
	public static <T, V> StaticVar<T, V> lookupStaticField(BLOBF obf) {
		return new StaticVar<T, V>(obf);
	}
	
	/**
	 * Finds a hook into a static field by its declaring class, field type and field name.
	 * 
	 * @param declarer	The class in which this field is declared
	 * @param type		The type of values this field takes
	 * @param name		The name of this field
	 */
	public static <T, V> StaticVar<T, V> lookupStaticField(Class<T> declarer, Class<V> type, String name, String... aliases) {
		return new StaticVar<T, V>(declarer, type, name);
	}
	
	/**
	 * Finds a hook into a static field by its string descriptor.
	 */
	public static <T, V> StaticVar<T, V> lookupStaticField(String descriptor) {
		return new StaticVar<T, V>(descriptor);
	}
	
	/**
	 * Finds a hook into a static method by a given BLOBF mapping.
	 * <p>
	 * This version does not support lambda creation.
	 */
	public static <I, R> Func<I, R> lookupStaticMethod(BLOBF obf) {
		return new Func<I, R>(true, obf);
	}
	
	/**
	 * Finds a hook into a static method by its declaring class, return type, name, and parameter types.
	 * <p>
	 * This version does not support lambda creation.
	 *  
	 * @param declarer			The class in which this method is declared.
	 * @param returnType		The method return type
	 * @param name				The method name
	 * @param pars				The parameter types this method takes
	 */
	public static <I, R> Func<I, R> lookupStaticMethod(Class<I> declarer, Class<R> returnType, String name, Class<?>... pars) {
		return new Func<I, R>(declarer, returnType, name, true, pars);
	}
	
	/**
	 * Finds a hook into a static method by a given BLOBF mapping.
	 * 
	 * @param interfaceType		An interface for the generated lambda to implement. Must contain a method matching the one you wish to access.
	 */
	public static <I, T, R> Lamda<I, T, R> lookupStaticMethod(Class<T> interfaceType, BLOBF obf) {
		return new Lamda<I, T, R>(interfaceType, true, obf);
	}
	
	/**
	 * Finds a hook into a static method by its declaring class, return type, name, and parameter types.
	 * 
	 * @param interfaceType		An interface for the generated lambda to implement. Must contain a method matching the one you wish to access. 
	 * @param declarer			The class in which this method is declared.
	 * @param returnType		The method return type
	 * @param name				The method name
	 * @param pars				The parameter types this method takes
	 */
	public static <I, T, R> Lamda<I, T, R> lookupStaticMethod(Class<T> interfaceType, Class<I> declarer, Class<R> returnType, String name, Class<?>... pars) {
		return new Lamda<I, T, R>(interfaceType, declarer, returnType, name, true, pars);
	}
	
	/**
	 * Finds a hook into a static method by its string descriptor.
	 * 
	 * @param interfaceType		An interface for the generated lambda to implement. Must contain a method matching the one you wish to access.
	 */
	public static <I, T, R> Lamda<I, T, R> lookupStaticMethod(Class<T> interfaceType, String descriptor) {
		return new Lamda<I, T, R>(interfaceType, true, descriptor);
	}
	
	/**
	 * Finds a hook into a static method by its string descriptor.
	 * <p>
	 * This version does not support lambda creation.
	 */
	public static <I, R> Func<I, R> lookupStaticMethod(String descriptor) {
		return new Func<I, R>(true, descriptor);
	}
	
	/**
	 * Finds a hook into a class contructor by its declaring class and parameter types
	 * 
	 * @param declarer	Class owner
	 * @param pars		Parameters
	 */
	public static <I> Constr<I> lookupConstructor(Class<I> declarer, Class<?>... pars) {
		return new Constr<I>(declarer, pars);
	}
	
	/**
	 * Finds a hook into a constructor by the BLOBF class description
	 */
	public static <I> Constr<I> lookupConstructor(BLOBF obf) {
		return new Constr<I>(obf);
	}
	
	/**
	 * Finds a hook into a constructor by its string descriptor
	 */
	public static <I> Constr<I> lookupConstructor(String descriptor) {
		return new Constr<I>(descriptor);
	}
}
