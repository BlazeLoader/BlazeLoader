package com.blazeloader.util.http;

import java.io.InputStream;

/**
 * Callback object for downloads.
 * 
 */
@FunctionalInterface
public interface IDownloadCallback {
	/**
	 * Called when a response is received from the server.
	 * 
	 * @param stream	Raw stream of data being delivered.
	 */
	void success(InputStream stream);
	
	/**
	 * Called when a negative response is received.
	 */
	default void error(int responseCode) {
		
	}
}