package com.blazeloader.api.block;

import java.lang.reflect.InvocationTargetException;

import com.blazeloader.api.gui.ApiGui;
import com.blazeloader.api.item.ApiItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class JSONBlockRegister {
	
	public <T extends JsonCreatedBlock> T instantiateBlock(Class<T> blockClass) throws InstantiationException {
		T result;
		try {
			result = blockClass.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new InstantiationException("Block type does not implement a default constructor.");
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends JsonCreatedBlock> T createBlockInstance(ResourceLocation resourceLoc, JsonObject json) throws InstantiationException, ClassNotFoundException {
		Class<T> clazz = (Class<T>) JSONBlockRegister.class.getClassLoader().loadClass(json.get("class").getAsString());
		T result = instantiateBlock(clazz);
		
		result.loadBlockAttributes(json);
		
		ApiBlock.registerBlock(ApiBlock.getFreeBlockId(), resourceLoc, result);
		ApiItem.registerItemBlock(result, result.loadItemAttributes(json.get("item").getAsJsonObject()));
		
		
		return result;
	}
	
	private static class JsonSoundType extends SoundType {

		public JsonSoundType(JsonObject sounds) {
			super(sounds.get("volume").getAsFloat(), sounds.get("pitch").getAsFloat(),
					getEvent(sounds, "break"), getEvent(sounds, "step"), getEvent(sounds, "place"), getEvent(sounds, "hit"), getEvent(sounds, "fall"));
		}
		
		private static SoundEvent getEvent(JsonObject json, String type) {
			return SoundEvent.REGISTRY.getObject(new ResourceLocation(json.get(type).getAsString()));
		}
	}
	
	public static class JsonCreatedBlock extends Block {

		protected JsonCreatedBlock(JsonObject json) {
			super(getMaterial(json));
		}
		
		protected static Material getMaterial(JsonObject material) {
			String mat = material.get("material").getAsString();
			return BlockMaterials.getMaterialByName(mat);
		}
		
		protected void loadBlockAttributes(JsonObject json) {
			setUnlocalizedName(json.get("unlocalizedName").getAsString());
			
			if (json.has("hardness")) setHardness(json.get("hardness").getAsFloat());
			if (json.has("resistance")) setResistance(json.get("resistance").getAsFloat());
			if (json.has("sounds")) setSoundType(new JsonSoundType(json.get("sounds").getAsJsonObject()));
			if (json.has("lightopacity")) setLightOpacity(json.get("lightopacity").getAsInt());
			if (json.has("lightlevel")) setLightLevel(json.get("lightlevel").getAsInt());
			if (json.has("creativetab")) setCreativeTab(ApiGui.getCreativeTabByName(json.get("creativetab").getAsString()));
			if (json.has("unbreakable")) setBlockUnbreakable();
			if (json.has("disablestats")) disableStats();
			if (json.has("variants")) {
				JsonArray variants = json.get("variants").getAsJsonArray();
				String[] vars = new String[variants.size()];
				for (int i = 0; i < vars.length; i++) vars[i] = variants.get(i).getAsString();
				ApiBlock.registerBlockVarientNames(this, vars);
			}
		}
		
		protected ItemBlock loadItemAttributes(JsonObject json) {
			return new ItemBlock(this);
		}
	}
}
