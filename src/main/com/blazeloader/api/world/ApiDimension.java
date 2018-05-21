package com.blazeloader.api.world;

import com.blazeloader.bl.interop.ForgeDimensionType;
import com.blazeloader.util.reflect.EnumFactory;
import com.blazeloader.util.registry.Registry;
import com.blazeloader.util.registry.RegistryIdManager;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;

public class ApiDimension {
	private static final RegistryNamespaced<ResourceLocation, DimensionType> REGISTRY = Registry.createNamespacedRegistry();
	private static final RegistryIdManager IDMANAGER = new RegistryIdManager(() -> Registry.of(REGISTRY));
	
	private static final EnumFactory<DimensionType> dimensionFactory = EnumFactory.create(DimensionType.class);
	
	static {
		init();
	}
	
	public static <T extends WorldProvider> DimensionType registerDimension(int code, ResourceLocation name, String suffex, boolean keepSpawnLoaded, Class<T> dim) {
		init();
		if (IDMANAGER.hasId(code)) {
			code = IDMANAGER.getNextFreeId();
		}
		String n = name.toString();
		DimensionType result = dimensionFactory.createValue(n.toUpperCase(), code, n, suffex, dim);
		
		((ForgeDimensionType)(Object)result).setLoadSpawn(keepSpawnLoaded);
		
		REGISTRY.register(code, name, result);
		return result;
	}
	
	public static DimensionType getDimensionForId(int id) {
		return REGISTRY.getObjectById(id);
	}
	
	public static DimensionType getDimensionForName(ResourceLocation name) {
		return REGISTRY.getObject(name);
	}
	
	private static void init() {
		for (DimensionType i : DimensionType.values()) {
			if (!IDMANAGER.hasId(i.getId())) {
				REGISTRY.register(i.getId(), new ResourceLocation(i.getName()), i);
			}
		}
	}
}
