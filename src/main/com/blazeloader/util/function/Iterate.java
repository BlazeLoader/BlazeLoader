package com.blazeloader.util.function;

import java.util.function.Consumer;

public class Iterate {
	public static <T> void on(Iterable<T> iterable, Consumer<? super T> consumer) {
		iterable.forEach(consumer);
	}
	
	public static <T> void on(T[] iterable, Consumer<? super T> consumer) {
		for (int i = 0; i < iterable.length; i++) consumer.accept(iterable[i]);
	}
}
