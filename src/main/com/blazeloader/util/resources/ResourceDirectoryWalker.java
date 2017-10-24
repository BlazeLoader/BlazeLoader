package com.blazeloader.util.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public abstract class ResourceDirectoryWalker {
	public ResourceDirectoryWalker() {
		
	}
	
	public boolean tryWalk(String path) {
		try {
			return walk(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean walk(String path) throws IOException {
		Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
		
		Path p = Paths.get(URI.create(path));
		
		Iterator<Path> iterator = Files.walk(p).iterator();
		
		while (iterator.hasNext()) {
			Path entry = iterator.next();
			if ("json".equals(FilenameUtils.getExtension(entry.toString()))) {
                String s = FilenameUtils.removeExtension(entry.toString()).replaceAll("\\\\", "/");
                ResourceLocation resourcelocation = new ResourceLocation(s);
                BufferedReader bufferedreader = null;

                try {
                    try {
                        bufferedreader = Files.newBufferedReader(entry);
                        parseJSONEntry(resourcelocation, (JsonObject)JsonUtils.fromJson(gson, bufferedreader, JsonObject.class));
                    }
                    catch (JsonParseException jsonparseexception) {
                        return false;
                    } catch (IOException ioexception) {
                        return false;
                    }
                } finally {
                    IOUtils.closeQuietly((Reader)bufferedreader);
                }
            }
		}
		
		return true;
	}
	
	public abstract void parseJSONEntry(ResourceLocation name, JsonObject jsonObject);
}
