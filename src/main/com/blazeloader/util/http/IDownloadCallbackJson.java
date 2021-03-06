package com.blazeloader.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.blazeloader.bl.main.BLMain;
import com.blazeloader.util.config.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

/**
 * Download callback for json responses. The data will be automatically unpacked to a json element.
 */
@FunctionalInterface
public interface IDownloadCallbackJson extends IDownloadCallback {

	@Override
	default void success(InputStream stream) {
		BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(stream));
            String json = "";
            String line;
            while ((line = in.readLine()) != null) {
            	json += line;
            }
            success(JsonUtils.parseJSON(json));
        } catch (IOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			BLMain.LOGGER_FULL.error("Exception whilst reading json response", e);
		}
	}
	
	/**
	 * Called when a response is received from the server.
	 * 
	 * @param json	Response, preparsed into a json element.
	 */
	void success(JsonElement json);
}
