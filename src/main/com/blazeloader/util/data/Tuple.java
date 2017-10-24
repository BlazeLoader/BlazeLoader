package com.blazeloader.util.data;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Generic version of {@code net.minecraft.util.Tuple}
 * <p>
 * Wraps multiple objects.
 */
public interface Tuple {
	
	public boolean contentEquals(Tuple other);
	
	/**
	 * Factory method to create a one item Tuple.
	 */
	public static <T> Tuple1<T> create(T one) {
		return new Tuple1<T>(one);
	}
	
	/**
	 * Factory method to create a two item Tuple.
	 */
	public static <T, U> Tuple2<T, U> create(T one, U two) {
		return new Tuple2<T, U>(one, two);
	}
	
	/**
	 * Factory method to create a three item Tuple.
	 */
	public static <T, U, V> Tuple3<T, U, V> create(T one, U two, V three) {
		return new Tuple3<T, U, V>(one, two, three);
	}
	
	/**
	 * Factory method to create a four item Tuple.
	 */
	public static <T, U, V, W> Tuple4<T, U, V, W> create(T one, U two, V three, W four) {
		return new Tuple4<T, U, V, W>(one, two, three, four);
	}
	
	/**
	 * Factory method to create a five item Tuple.
	 */
	public static <T, U, V, W, X> Tuple5<T, U, V, W, X> create(T one, U two, V three, W four, X five) {
		return new Tuple5<T, U, V, W, X>(one, two, three, four, five);
	}
	
	/**
	 * Factory method to create a six item Tuple.
	 */
	public static <T, U, V, W, X, Y> Tuple6<T, U, V, W, X, Y> create(T one, U two, V three, W four, X five, Y six) {
		return new Tuple6<T, U, V, W, X, Y>(one, two, three, four, five, six);
	}
	
	/**
	 * Factory method to create a seven item Tuple.
	 */
	public static <T, U, V, W, X, Y, Z> Tuple7<T, U, V, W, X, Y, Z> create(T one, U two, V three, W four, X five, Y six, Z seven) {
		return new Tuple7<T, U, V, W, X, Y, Z>(one, two, three, four, five, six, seven);
	}
	
	/**
	 * Factory method to create a Tuple with more than seven items.
	 */
	public static <T, U, V, W, X, Y, Z, N extends Tuple> TupleN<T, U, V, W, X, Y, Z, N> create(T one, U two, V three, W four, X five, Y six, Z seven, Object... n) {
		if (n.length > 7) {
			return create(one, two, three, four, five, six, seven, create(n[0],n[1],n[2],n[3],n[4],n[5],n[6], ArrayUtils.subarray(n, 7, n.length)));
		} else {
			return TupleN.create(one, two, three, four, five, six, seven, n);
		}
	}
	
	public static class Tuple1<T> implements Tuple {
		/**
		 * The first item
		 */
		public final T itemOne;
		
		public Tuple1(T objectOne) {
			itemOne = objectOne;
		}
		
		protected String internalToString() {
			return itemOne.toString();
		}
		
		public String toString() {
			return "{" + internalToString() + "}";
		}
		
		public boolean contentEquals(Tuple other) {
			return getClass().isAssignableFrom(other.getClass()) && itemOne.equals(((Tuple1<?>)other).itemOne);
		}
	}
	
	public static class Tuple2<T, U> extends Tuple1<T> {
		/**
		 * The second item
		 */
		public final U itemTwo;
		
		
		public Tuple2(T one, U two) {
			super(one);
			itemTwo = two;
		}
		
		protected String internalToString() {
			return super.internalToString() + "," + itemTwo.toString();
		}
		
		public boolean contentEquals(Tuple other) {
			return super.contentEquals(other) && itemTwo.equals(((Tuple2<?, ?>)other).itemTwo);
		}
	}
	
	public static class Tuple3<T, U, V> extends Tuple2<T, U> {
		/**
		 * The third item
		 */
		public final V itemThree;
		
		public Tuple3(T one, U two, V three) {
			super(one, two);
			itemThree = three;
		}
		
		protected String internalToString() {
			return super.internalToString() + "," + itemThree.toString();
		}
		
		public boolean contentEquals(Tuple other) {
			return super.contentEquals(other) && itemThree.equals(((Tuple3<?, ?, ?>)other).itemThree);
		}
	}
	
	public static class Tuple4<T, U, V, W> extends Tuple3<T, U, V> {
		/**
		 * The fourth item
		 */
		public final W itemFour;
		
		public Tuple4(T one, U two, V three, W four) {
			super(one, two, three);
			itemFour = four;
		}
		
		protected String internalToString() {
			return super.internalToString() + "," + itemFour.toString();
		}
		
		public boolean contentEquals(Tuple other) {
			return super.contentEquals(other) && itemFour.equals(((Tuple4<?, ?, ?, ?>)other).itemFour);
		}
	}
	
	public static class Tuple5<T, U, V, W, X> extends Tuple4<T, U, V, W> {
		/**
		 * The fifth item
		 */
		public final X itemFive;
		
		public Tuple5(T one, U two, V three, W four, X five) {
			super(one, two, three, four);
			itemFive = five;
		}
		
		protected String internalToString() {
			return super.internalToString() + "," + itemFive.toString();
		}
		
		public boolean contentEquals(Tuple other) {
			return super.contentEquals(other) && itemFive.equals(((Tuple5<?, ?, ?, ?, ?>)other).itemFive);
		}
	}
	
	public static class Tuple6<T, U, V, W, X, Y> extends Tuple5<T, U, V, W, X> {
		/**
		 * The sixth item
		 */
		public final Y itemSix;
		
		public Tuple6(T one, U two, V three, W four, X five, Y six) {
			super(one, two, three, four, five);
			itemSix = six;
		}
		
		public String internalToString() {
			return super.internalToString() + "," + itemSix.toString();
		}
		
		public boolean contentEquals(Tuple other) {
			return super.contentEquals(other) && itemSix.equals(((Tuple6<?, ?, ?, ?, ?, ?>)other).itemSix);
		}
	}
	
	public static class Tuple7<T, U, V, W, X, Y, Z> extends Tuple6<T, U, V, W, X, Y> {
		/**
		 * The seventh item
		 */
		public final Z itemSeven;
		
		public Tuple7(T one, U two, V three, W four, X five, Y six, Z seven) {
			super(one, two, three, four, five, six);
			itemSeven = seven;
		}
		
		public String internalToString() {
			return super.internalToString() + "," + itemSeven.toString();
		}
		
		public boolean contentEquals(Tuple other) {
			return super.contentEquals(other) && itemSix.equals(((Tuple7<?, ?, ?, ?, ?, ?, ?>)other).itemSeven);
		}
	}
	
	public static class TupleN<T, U, V, W, X, Y, Z, N extends Tuple> extends Tuple7<T, U, V, W, X, Y, Z> {
		/**
		 * A Tuple of additional items.
		 */
		public final N extended;
		
		public TupleN(T one, U two, V three, W four, X five, Y six, Z seven, N n) {
			super(one, two, three, four, five, six, seven);
			extended = n;
		}
		
		public String internalToString() {
			return super.internalToString() + "," + extended.toString();
		}
		
		public boolean contentEquals(Tuple other) {
			return super.contentEquals(other) && extended.contentEquals(((TupleN<?, ?, ?, ?, ?, ?, ?, ?>)other).extended);
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		private static Tuple createNthTuple(Object[] n) {
			switch (n.length) {
				case 1: return new Tuple1(n[0]);
				case 2: return new Tuple2(n[0],n[1]);
				case 3: return new Tuple3(n[0],n[1],n[2]);
				case 4: return new Tuple4(n[0],n[1],n[2],n[3]);
				case 5: return new Tuple5(n[0],n[1],n[2],n[3],n[4]);
				case 6: return new Tuple6(n[0],n[1],n[2],n[3],n[4],n[5]);
				case 7: return new Tuple7(n[0],n[1],n[2],n[3],n[4],n[5],n[6]);
			}
			return null;
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		protected static <T, U, V, W, X, Y, Z, N extends Tuple> TupleN<T, U, V, W, X, Y, Z, N> create(T one, U two, V three, W four, X five, Y six, Z seven, Object[] n) {
			return new TupleN(one, two, three, four, five, six, seven, createNthTuple(n));
		}
	}
}
