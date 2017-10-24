package com.blazeloader.util.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;

import com.blazeloader.api.ApiServer;
import com.blazeloader.api.client.ApiClient;
import com.blazeloader.bl.main.BLMain;
import com.blazeloader.util.version.Versions;

/**
 * A basic http downloader.
 * Will attempt to fetch a resource from the given url and then delegate to a provided callback for how to handle the result. 
 *
 */
public class Downloader {
	private static final AtomicInteger threadDownloadCounter = new AtomicInteger(0);
	
	private final File cached;
	private final String url;
	
	private Thread downloadThread;
	
	/**
	 * @param httpUrl			Location of resource to download.
	 */
	public Downloader(String httpUrl) {
		this(null, httpUrl);
	}
	
	/**
	 * @param cacheLocation		File to save responses. Repeat attempts will then yield the cached result.
	 * @param httpUrl			Location of resource to download.
	 */
	public Downloader(File cacheLocation, String httpUrl) {
		cached = cacheLocation;
		url = httpUrl;
	}
	
	/**
	 * Launches a download operation.
	 * <br>
	 * Will return results from a cached location if one exists.
	 * 
	 * @param callback	Callback to be notified on completion.
	 */
	public void download(IDownloadCallback callback) {
		if (cached != null && cached.isFile()) {
			try {
				returnCachedData(callback);
			} catch (IOException e) {
				BLMain.LOGGER_FULL.error("Couldn\'t open cached resource file", e);
			}
			return;
		}
		launchHttpThread(callback);
	}
	
	private void returnCachedData(IDownloadCallback callback) throws IOException {
		InputStream stream = null;
		try {
			stream = FileUtils.openInputStream(cached);
			callback.success(stream);
		} finally {
			if (stream != null) stream.close();
		}
	}
	
	private void launchHttpThread(IDownloadCallback callback) {
		downloadThread = new Thread("Http download thread #" + threadDownloadCounter.incrementAndGet()) {
			public void run() {
				HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection)(new URL(url)).openConnection(Downloader.getProxy());
                    connection.setDoInput(true);
                    connection.connect();
                    
                    if (connection.getResponseCode() / 100 != 2) {
                    	callback.error(connection.getResponseCode());
                        return;
                    }
                    InputStream input = connection.getInputStream();
                    try {
	                    if (cached != null) {
	                    	if (!cached.exists()) cached.createNewFile();
	                        FileUtils.copyInputStreamToFile(input, cached);
	                    }
	                    callback.success(input);
                    } finally {
                    	input.close();
                    }
                } catch (Exception e) {
                    BLMain.LOGGER_FULL.error("Couldn\'t download http resource", e);
                } finally {
                    if (connection != null) connection.disconnect();
                }
            }
		};
		downloadThread.setDaemon(true);
		downloadThread.start();
	}
	
	/**
	 * Gets the game's current proxy setting
	 */
	public static Proxy getProxy() {
		if (Versions.isClient()) {
			return ApiClient.getClient().getProxy();
		}
		return ApiServer.getServer().getServerProxy();
	}
	
	/**
	 * Decodes a Base64 encoded string.
	 */
	public static String decodeBas64(String encoded) {
		return new String(Base64.getDecoder().decode(encoded));
	}
	
	/**
	 * Encodes a string using base64.
	 */
	public static String encodeBas64(String decoded) {
		return Base64.getEncoder().encodeToString(decoded.getBytes());
	}
}
