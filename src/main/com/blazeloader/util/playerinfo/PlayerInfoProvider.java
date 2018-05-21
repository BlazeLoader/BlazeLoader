package com.blazeloader.util.playerinfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.blazeloader.util.config.JsonUtils;
import com.blazeloader.util.http.Downloader;
import com.blazeloader.util.http.IDownloadCallbackJson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

/**
 * PlayerInfo class for getting details about a player from mojang's servers.
 * <br>
 * eg. skin location, cape location, type of skin.
 *
 */
class PlayerInfoProvider {
	
	protected UUID uuid;
	
	protected Map<Type, String> skinUrls = new HashMap<Type, String>();
	
	private String skinType;
	
	private boolean hasLoaded = false;
	
	protected PlayerInfoProvider(UUID uuid) {
		this.uuid = uuid;
	}
	
	protected String popUrl(Type type) {
		if (!hasLoaded) loadPlayerInfo();
		return skinUrls.remove(type);
	}
	
	public String getSkinType() {
		if (!hasLoaded) loadPlayerInfo();
		return skinType;
	}
	
	public boolean hasSkin() {
		return hasTexture(Type.SKIN);
	}
	
	public boolean hasCape() {
		return hasTexture(Type.CAPE);
	}
	
	public boolean hasElytra() {
		return hasTexture(Type.ELYTRA);
	}
	
	private boolean hasTexture(Type type) {
		if (!hasLoaded) loadPlayerInfo();
		return skinUrls.containsKey(type);
	}
	
    private void loadPlayerInfo() {
    	if (hasLoaded) return;
    	hasLoaded = true;
    	(new Downloader("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", ""))).download(new IDownloadCallbackJson() {
			public void success(JsonElement json) {
				JsonObject obj = json.getAsJsonObject();
				if (obj.has("properties")) {
	            	obj = obj.getAsJsonArray("properties").get(0).getAsJsonObject();
	            	if (obj.has("value")) {
	            		String s = Downloader.decodeBas64(obj.get("value").getAsString());
	            		obj = JsonUtils.parseJSONObj(s);
	            		if (obj.has("textures")) {
	            			obj = obj.getAsJsonObject("textures");
	            			if (obj.has("SKIN")) {
	            				JsonObject skin = obj.getAsJsonObject("SKIN");
	            				skinUrls.put(Type.SKIN, skin.get("url").getAsString());
	            				if (skin.has("metadata")) {
	            					skin = skin.getAsJsonObject("metadata");
	            					skinType = skin.get("model").getAsString();
	            				} else {
	            					skinType = "default";
	            				}
	            			}
	            			if (obj.has("CAPE")) {
	            				skinUrls.put(Type.CAPE, obj.getAsJsonObject("CAPE").get("url").getAsString());
	            			}
	            			if (obj.has("ELYTRA")) {
	            				skinUrls.put(Type.ELYTRA, obj.getAsJsonObject("ELYTRA").get("url").getAsString());
	            			}
	            		}
	            	}
	            }
			}
    	});
    }
}
