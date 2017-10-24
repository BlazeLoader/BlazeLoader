package com.blazeloader.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import java.util.Optional;
import java.util.function.Function;

import com.blazeloader.util.data.Tuple;
import com.blazeloader.util.data.Tuple.Tuple2;

/**
 * Javascript array methods re-implemented in Java. Any ones not found here are probably already in (@code ArrayUtils}.
 */
@SuppressWarnings("unchecked")
public class JSArrayUtils {
	
	/**
	 * Removes the first element from the end of an array and returns both it and the resulting array.
	 */
	public static <T> Tuple2<Optional<T>, T[]> pop(T... array) {
		if (array.length == 0) return Tuple.create(Optional.empty(), array);
		return Tuple.create(Optional.of(array[array.length - 1]), subarray(array, 0, array.length - 1));
	}
	
	private static <T> T[] subarray(T[] arr, int start, int end) {
		int size = end - start;
		if (size < 0) size = 0;
		T[] result = newArray(arr, size);
		if (size > 0) System.arraycopy(arr, start, result, 0, size);
		return result;
	}
	
	/**
	 * Adds one or more elements to the end of an array and returns the result.
	 */
    public static <T> T[] push(T[] array, T... added) {
    	T[] result = newArray(array, array.length + added.length);
    	System.arraycopy(array, 0, result, 0, array.length);
        System.arraycopy(added, 0, result, array.length, added.length);
    	return result;
    }
    
    /**
	 * Removes the first element from the start of an array and returns both it and the resulting array.
	 */
	public static <T> Tuple2<Optional<T>, T[]> shift(T... array) {
		if (array.length == 0) return Tuple.create(Optional.empty(), array);
		return Tuple.create(Optional.of(array[0]), subarray(array, 1, array.length));
	}
    
	/**
	 * Adds one or more elements to the beginning of an array and returns the result.
	 */
    public static <T> T[] unshift(T[] array, T... added) {
    	return push(added, array);
    }
    
    /**
     * Joins one or more arrays or array elements onto the given array and returns the result.
     */
	public static <T> T[] concat(T[] array, Object... concatted) {
    	T[] result = array;
    	for (int i = 0; i < concatted.length; i++) {
    		if (concatted[i].getClass() == array.getClass()) {
    			result = push(result, (T[])concatted[i]);
    		} else if (concatted[i].getClass() == array.getClass().getComponentType()) {
    			result = push(result, (T)concatted[i]);
    		}
    	}
    	return result;
    }
    
    /**
     * Removes a number of elements from an array beginning at the given start index and optionally inserts one or items in their place.
     */
    public static <T> T[] splice(T[] array, int start, int deleteCount, T ...inserted) {
    	if (start >= array.length) throw new IllegalArgumentException("Start index must be less than the size of the source array");
    	if (deleteCount < 0) deleteCount = 0; 
    	if (start < 0) start = 0;
    	
    	List<T> result = new ArrayList<T>();
    	int insertions = 0;
    	int i = 0;
    	while (i < array.length) {
    		if (i < start) {
    			result.add(array[i]);
    			 i++;
    		} else if (insertions < inserted.length) {
    			result.add(inserted[insertions++]);
    		} else if (deleteCount > 0) {
    			deleteCount--;
    		} else {
    			result.add(array[i]);
    			 i++;
    		}
    	}
    	return result.size() > 0 ? toArray(result) : newArray(array, 0);
    }
    
    /**
     * Returns a subset of an array.
     * 
     * @param start Start index of items to return. Negative values are taken as an offset from the end of the array and start is taken as 0 when not specified.
     * @param end	End index of items to return. Negative values are taken as an offset from the end of the array and end is taken as the end of the array when not specified.
     */
    public static <T> T[] slice(T[] array, int... args) {
    	int start = args.length > 0 ? args[0] : 0;
    	int end = args.length > 1 ? args[1] : array.length;
    	if (start < 0) start = array.length + start - 1;
    	if (end < 0) end = array.length + end - 1;
    	if (start > end) {
    		int tmp = start;
    		start = end;
    		end = tmp;
    	}
    	return subarray(array, start, end);
    }
    
    /**
     * Converts the given collection to an array using the given component type class.
     */
	public static <T> T[] toArray(Collection<T> source, Class<T> componentType) {
		return source.toArray((T[])Array.newInstance(componentType, source.size()));
	}
    
	/**
	 * Converts the given list to an array using the class of its first item.
	 * <p>
	 * List cannot be empty.
	 */
	public static <T> T[] toArray(List<T> newa) {
    	if (newa.size() == 0) throw new IllegalArgumentException("List cannot be empty");
    	return (T[])toArray(newa, getBaseClass(newa.get(0).getClass())); //Shut-up, gradle >.>
    }
    
    /**
     * Converts the given array to a list.
     */
    public static <T> List<T> toList(T... arr) {
    	return Arrays.asList(arr);
    }
    
    /**
     * Creates a new array with the same component type as that of the given array and with the given length.
     */
    public static <T> T[] newArray(T[] arr, int length) {
    	return (T[]) Array.newInstance(arr.getClass().getComponentType(), length);
    }
    
    private static <T> Class<? super T> getBaseClass(Class<? super T> c) {
    	while (c.getSuperclass() != Object.class && c.getSuperclass() != null) {
    		c = c.getSuperclass();
    	}
    	return c;
    }
    
    /**
     * Concatenates the items of an array to a string using the optional separator value. Default separator is a ",".
     */
    public static <T> String join(T[] arr) {
    	return join(arr, ",");
    }
    
    /**
     * Concatenates the items of an array to a string using the optional separator value. Default separator is a ",".
     */
    public static <T> String join(T[] arr, Object separator) {
    	String sep = separator.toString();
    	String result = "";
    	for (T i : arr) {
    		if (result.length() > 0) result += sep;
    		if (isArray(i)) {
    			result += join(asArray(i), sep);
    		} else {
    			result += i.toString();
    		}
    	}
    	return result;
    }
    
    public static <T> String toSource(T... arr) {
    	String result = "";
    	for (T i : arr) {
    		if (result.length() > 0) result += ", ";
    		if (isArray(i)) {
    			result += toSource(asArray(i));
    		} else {
    			result += i.toString();
    		}
    	}
    	return "[" + result + "]";
    }
    
    /**
     * Check if the given object is an array.
     */
    public static boolean isArray(Object o) {
    	return o instanceof Object[] ||
    		o instanceof int[] ||
    		o instanceof boolean[] ||
    		o instanceof float[] ||
    		o instanceof double[] ||
    		o instanceof byte[] ||
    		o instanceof char[] ||
    		o instanceof long[] ||
    		o instanceof short[]; //This is faster.
    }
    
    /**
     * Converts a given primitive array or string to an object type array.
     */
    public static <T> T[] asArray(Object o) {
    	if (o == null) return null;
    	if (o instanceof Object[]) return (T[])o;
    	if (o instanceof String) o = ((String)o).toCharArray();
    	int n = Array.getLength(o);
    	T[] result = (T[])Array.newInstance(o.getClass().getComponentType(), n);
    	for (int i = 0; i < n; i++) result[n] = (T)Array.get(o, i);
    	return result;
    }
    
    /**
     * Checks if all items in the array pass the test defined by the given Predicate function.
     * 
     * @return true if all items pass, false otherwise.
     */
    public static <T> boolean every(T[] arr, Function<Args<T>, Boolean> callback) {
    	boolean result = true;
    	for (int i = 0; result && i < arr.length; i++) result &= callback.apply(new Args<T>(arr, i));
    	return result;
    }
    
    /**
     * Checks if any items in the array pass the test defined by the given Predicate function.
     * 
     * @return true if at least one item passes, false otherwise.
     */
    public static <T> boolean some(T[] arr, Function<Args<T>, Boolean> callback) {
    	for (int i = 0; i < arr.length; i++) {
    		if (callback.apply(new Args<T>(arr, i))) return false;
    	}
    	return true;
    }
    
    /**
     * Applies the given predicate function to each item in the array in sequence.
     * 
     * @return true if all items pass, false otherwise.
     */
    public static <T> void forEach(T[] arr, Function<Args<T>, Boolean> callback) {
    	for (int i = 0; i < arr.length; i++) callback.apply(new Args<T>(arr, i));
    }
    
    /**
     * Applies the given function to each item in the array accumulating a value through each call.
     */
    public static <T,V> V reduce(T[] arr, Function<Folds<T,V>, V> callback, V ...initial) {
    	if (arr.length < 2) {
    		if (initial.length == 0) {
    			throw new RuntimeException("no value to return");
    		} else if (arr.length == 0) {
    			return initial[0];
    		}
    	}
    	V ini = initial.length > 0 ? initial[0] :  (V)arr[1];
    	for (int i = 0; i < arr.length; i ++) {
    		ini = callback.apply(new Folds<T, V>(arr, i, ini));
    	}
    	return ini;
    }
    
    /**
     * Applies the given function to each item in the array in reverse order accumulating a value through each call.
     */
    public static <T,V> V reduceRight(T[] arr, Function<Folds<T,V>, V> callback, V ...initial) {
    	if (arr.length < 2) {
    		if (initial.length == 0) {
    			throw new RuntimeException("no value to return");
    		} else if (arr.length == 0) {
    			return initial[0];
    		}
    	}
    	V ini;
    	int i = arr.length - 1;
    	if (initial.length > 0) {
    		ini = initial[0];
    	} else {
    		i--;
    		ini = (V)arr[arr.length - 1];
    	}
    	for (; i <= 0; i--) {
    		ini = callback.apply(new Folds<T, V>(arr, i, ini));
    	}
    	return (V)ini;
    }
    
    /**
     * Returns an array of items that pass the test defined by the given Predicate function. 
     */
    public static <T> T[] filter(T[] arr, Predicate<T> test) {
    	if (arr == null || arr.length == 0) {
    		return arr;
    	}
    	List<T> result = new ArrayList<T>();
    	for (T i : arr) {
    		if (test.test(i)) result.add(i);
    	}
    	return result.size() > 0 ? toArray(result) : newArray(arr, 0);
    }
    
    public static class Folds<T, V> extends Args<T> {
    	/**
    	 * Result of the previous execution.
    	 * <p>
    	 * If there was a initialValue provided this will be set to that on the first execution, otherwise will be the previous item in the array for the respective direction.
    	 */
    	public final V previousValue;
    	
    	protected Folds(T[] arr, int i, V initial) {
    		super(arr, i);
    		previousValue = initial;
    	}
    }
    
    public static class Args<T> {
    	/**
    	 * The array being processed.
    	 */
    	public final T[] array;
    	/**
    	 * Current index.
    	 */
    	public final int index;
    	/**
    	 * Value at the current position in the array.
    	 */
    	public final T currentValue;
    	
    	protected Args(T[] arr, int i) {
    		array = arr;
    		index = i;
    		currentValue = array[index];
    	}
    }
}
