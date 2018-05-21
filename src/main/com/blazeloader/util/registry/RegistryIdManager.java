package com.blazeloader.util.registry;

import java.util.function.Supplier;

public class RegistryIdManager {
	private int nextId = 1;
	
	private final Supplier<Registry<?, ?>> supplier;
	
	public RegistryIdManager(Supplier<Registry<?, ?>> supplier) {
		this.supplier = supplier;
	}
	
	public boolean hasId(int id) {
    	return supplier.get().getUnderlyingMap().get(id) != null;
    }
    
	public int getNextFreeId() {
		while (hasId(nextId)) nextId++;
		return nextId;
	}
}
