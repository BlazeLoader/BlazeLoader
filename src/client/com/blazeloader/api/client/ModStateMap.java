package com.blazeloader.api.client;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import com.blazeloader.api.block.ApiBlock;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public class ModStateMap extends StateMapperBase {
	private final String modid;
    private final IProperty<?> mappedProperty;
    private final String suffix;
    private final List<IProperty<?>> ignoredProperties;
    
    private ModStateMap(String modid, IProperty<?> mappedProperty, String suffix, List<IProperty<?>> ignoredProperties) {
    	this.modid = modid;
        this.mappedProperty = mappedProperty;
        this.suffix = suffix;
        this.ignoredProperties = ignoredProperties;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        LinkedHashMap<IProperty<?>, Comparable<?>> stateProperties = Maps.newLinkedHashMap(state.getProperties());
        String modelLocation;

        if (mappedProperty == null) {
            modelLocation = ApiBlock.getStringBlockName(state.getBlock());
        } else {
            modelLocation = modid + ":" + ((IProperty)mappedProperty).getName(stateProperties.remove(mappedProperty));
        }

        if (suffix != null) modelLocation += suffix;
        
        for (IProperty<?> i : ignoredProperties) {
            stateProperties.remove(i);
        }

        return new ModelResourceLocation(modelLocation, getPropertyString(stateProperties));
    }
    
    /**
     * Builder class for creating a state mapper for block models.
     */
    public static class Builder {
        private IProperty<?> property;
        private String suffix;
        private String modId = "minecraft";
        private final List<IProperty<?>> excludedProperties = Lists.newArrayList();
        
        /**
         * Sets the property used when mapping states to resource locations.
         */
        public Builder setProperty(IProperty<?> mappedProperty) {
            property = mappedProperty;
            return this;
        }
        
        /**
         * Set a suffex to be appended at the end of any resource location names generated.
         */
        public Builder setSuffix(String suffex) {
            suffix = suffex;
            return this;
        }
        
        /**
         * Sets the modid/domain for resource locations generated.
         */
        public Builder setModId(String mod) {
        	modId = mod;
        	return this;
        }
        
        /**
         * Adds properties that must be ignored when generating a resource location
         */
        public Builder addPropertiesToIgnore(IProperty<?> ... ignored) {
            Collections.addAll(excludedProperties, ignored);
            return this;
        }
        
        /**
         * Builds a mapper to be registered for the block.
         */
        public ModStateMap build() {
            return new ModStateMap(modId, property, suffix, excludedProperties);
        }
    }
}
