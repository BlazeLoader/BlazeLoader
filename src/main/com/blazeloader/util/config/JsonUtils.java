package com.blazeloader.util.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Json utilities class
 */
public class JsonUtils {
	private static Gson json;
	private static JsonParser parser;
	
	private static JsonParser getParser() {
		if (parser == null) {
			parser = new JsonParser();
		}
		return parser;
	}
	
	private static Gson getJson() {
		if (json == null) {
			json = new Gson();
		}
		return json;
	}
	
	/**
	 * Converts a json string to a json element.
	 */
	public static JsonElement parseJSON(String json) {
		return getParser().parse(json);
	}
	
	/**
	 * Converts a json string to a json object.
	 */
	public static JsonObject parseJSONObj(String json) {
		JsonElement element = parseJSON(json);
		if (element.isJsonObject()) {
			return (JsonObject)element;
		}
		return null;
	}
	
	/**
	 * Converts a string of json data to the given object type.
	 */
	public static <T> T parseJson(String json, Class<T> objectType) {
		return getJson().fromJson(json, objectType);
	}
}
