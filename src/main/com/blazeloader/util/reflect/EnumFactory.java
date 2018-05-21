package com.blazeloader.util.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.blazeloader.util.registry.Registry;

import net.minecraft.util.registry.RegistrySimple;
import sun.reflect.ConstructorAccessor;
import sun.reflect.ReflectionFactory;

/**
 * Factory class for dynamically generating Enum values.
 * <br>
 * Modified from the code at the site below.
 * <br>
 * Found at: http://niceideas.ch/roller2/badtrash/entry/java_create_enum_instances_dynamically
 * 
 * @param <E>	The type of enum to produce.
 */
public final class EnumFactory<E extends Enum<E>> {
	private static final RegistrySimple<Class<?>, EnumFactory<?>> REGISTRY = Registry.createSimpleRegistry();
	
	private static final ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
	
	
	/**
	 * Creates or returns a new factory for the given type of enum.
	 * 
	 * @param type	The Enum to generate
	 * @return a corresponding factory instance.
	 */
	public static <E extends Enum<E>> EnumFactory<E> create(Class<E> type) {
		@SuppressWarnings("unchecked")
		EnumFactory<E> result = (EnumFactory<E>) REGISTRY.getObject(type);
		if (result == null) {
			try {
				return new EnumFactory<E>(type);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		return result;
	}
	
	private final Class<E> type;
	
	//private final Field $VALUES;
	private List<E> values;
	
	private final StaticVar<E, E[]> $VALUES;
	
	private final E[] ARRAY;
	
	@SuppressWarnings("unchecked")
	private EnumFactory(Class<E> type) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		this.type = type;
		
		ARRAY = (E[])Array.newInstance(type, 0);
		
		$VALUES = Reflect.lookupStaticField(type, (Class<E[]>)ARRAY.getClass(), "$VALUES", "ENUM$VALUES");
		
		REGISTRY.putObject(type, this);
	}
	
	/**
	 * Gets the internal array of all values associated with this Enum type.
	 */
	public E[] getEnumValues() {
		return $VALUES.get(ARRAY);
	}
	
	/**
	 * Create a new enum value with the given name and constructor parameters.
	 * 
	 * @param name		Name for the enum constant.
	 * @param params	Optional parameters to pass to its constructor.
	 */
	public E createValue(String name, Object... params) {
		E result = makeEnum(name, params);
		
		values = new ArrayList<E>(Arrays.asList(getEnumValues()));
		values.add(result);
		$VALUES.set(values.toArray(ARRAY));
		
		cleanEnumCache();
        
        return result;
	}
	
	@SuppressWarnings("unchecked")
	private E makeEnum(String value, Object... additionalValues) {
		Object[] parms = new Object[additionalValues.length + 2];
		parms[0] = value;
		parms[1] = Integer.valueOf(values.size());
		System.arraycopy(additionalValues, 0, parms, 2, additionalValues.length);
		
		try {
			ConstructorAccessor constructor = reflectionFactory.newConstructorAccessor(type.getDeclaredConstructor(typeParams(additionalValues)));
			return (E)constructor.newInstance(parms);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	private Class<?>[] typeParams(Object... params) {
		Class<?>[] result = new Class<?>[params.length + 2];
		result[0] = String.class;
		result[1] = Integer.class;
		for (int i = 0; i < params.length; i++) result[i + 2] = params[i].getClass();
		return result;
	}
	
	private static void setFailsafeFieldValue(Field field, Object target, Object value) throws NoSuchFieldException, IllegalAccessException {
		field.setAccessible(true);
		
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		
		int modifiers = modifiersField.getInt(field);
		modifiersField.setInt(field, modifiers & ~Modifier.FINAL);
		
		reflectionFactory.newFieldAccessor(field, false).set(target, value);
   }

   private static void clearField(Class<?> enumClass, String fieldName) {
       for (Field field : Class.class.getDeclaredFields()) {
           if (field.getName().contains(fieldName)) {
        	   try {
        		   setFailsafeFieldValue(field, enumClass, null);
        	   } catch (NoSuchFieldException | IllegalAccessException e) {
        		   e.printStackTrace();
        	   }
               break;
           }
       }
   }

   private void cleanEnumCache() {
       clearField(type, "enumConstantDirectory");
       clearField(type, "enumConstants");
   }
}
