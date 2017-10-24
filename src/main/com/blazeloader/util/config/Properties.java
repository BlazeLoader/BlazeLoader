package com.blazeloader.util.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.blazeloader.api.block.ApiBlock;
import com.blazeloader.api.entity.ApiEntity;
import com.blazeloader.api.item.ApiItem;

/**
 * 
 * Overly complicated implementation of a properties file.
 * <p>
 * Supports grouping property entries in groups, comments, type coercion.
 *
 */
public class Properties implements IConfig {
	private final HashMap<String, Section> sections = new HashMap<String, Section>();
	
	private File file;
	
	private boolean writeDefaults = true;
	
	public Properties(File file) {
		if (file != null) load(file);
	}
	
	@Override
	public void load(File file) {
		if (file.exists() && !(file.canRead() && file.isFile())) {
			throw new IllegalArgumentException("Given file is not a file or is not accessible.");
		}
		this.file = file;
		try {
			if (file.exists()) {
				List<String> lines = FileUtils.readLines(file, Charset.defaultCharset());
				readFrom(lines);
			} else {
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void save() {
		try {
			FileWriter writer = new FileWriter(file);
			writeTo(writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Properties setWriteDefaults(boolean val) {
		writeDefaults = val;
		return this;
	}
	
	@Override
	public boolean getWriteDefaults() {
		return writeDefaults;
	}
	
	protected boolean hasSection(String section) {
		return sections.containsKey(section);
	}
	
	@Override
	public boolean has(String section, String name) {
		if (hasSection(section)) {
			return sections.get(section).has(name);
		}
		return false;
	}
	
	@Override
	public <T> Prop<T> getProperty(String section, String name, T defaultValue) {
		return getSection(section).get(name, defaultValue);
	}
	
	/**
	 * Gets an entity ID property from this property file.
	 * <p>
	 * Contains additional checks to ensure the id is available.
	 * 
	 * @param section		Name of the section
	 * @param name			Name of the property
	 * @param defaultValue	The default value of the property
	 * 
	 * @return	A property object for the given keys.
	 */
	public Prop<Integer> getEntityId(String section, String name) {
		Section sec = getSection(section);
		if (!sec.has(name)) {
			return sec.get(name, ApiEntity.getFreeEntityId());
		}
		Prop<Integer> result = sec.get(name, 0);
		if (!ApiEntity.isIdFree(result.get())) {
			result.set(ApiEntity.getFreeEntityId());
		}
		return result;
	}
	
	/**
	 * Gets a block ID property from this property file.
	 * <p>
	 * Contains additional checks to ensure the id is available.
	 * 
	 * @param section		Name of the section
	 * @param name			Name of the property
	 * @param defaultValue	The default value of the property
	 * 
	 * @return	A property object for the given keys.
	 */
	public Prop<Integer> getBlockId(String section, String name) {
		Section sec = getSection(section);
		if (!sec.has(name)) {
			return sec.get(name, ApiBlock.getFreeBlockId());
		}
		Prop<Integer> result = sec.get(name, 0);
		if (!ApiBlock.isIdFree(result.get())) {
			result.set(ApiBlock.getFreeBlockId());
		}
		return result;
	}
	
	/**
	 * Gets an item ID property from this property file.
	 * <p>
	 * Contains additional checks to ensure the id is available.
	 * 
	 * @param section		Name of the section
	 * @param name			Name of the property
	 * @param defaultValue	The default value of the property
	 * 
	 * @return	A property object for the given keys.
	 */
	public Prop<Integer> getItemId(String section, String name) {
		Section sec = getSection(section);
		if (!sec.has(name)) {
			return sec.get(name, ApiItem.getFreeItemId());
		}
		Prop<Integer> result = sec.get(name, 0);
		if (!ApiItem.isIdFree(result.get())) {
			result.set(ApiItem.getFreeItemId());
		}
		return result;
	}
	
	@Override
	public Section getSection(String section) {
		if (hasSection(section)) {
			return sections.get(section);
		}
		Section result = new Section(this, section);
		sections.put(section, result);
		return result;
	}
	
	protected void writeTo(FileWriter writer) throws IOException {
		StringBuilder builder = new StringBuilder();
		for (Section i : sections.values()) {
			i.writeTo(builder);
			builder.append("\r\n");
		}
		writer.append(builder.toString());
	}
	
	protected void readFrom(List<String> lines) {
		while (lines.size() > 0) {
			try {
				Section section = new Section(this, lines);
				if (section.loaded) {
					sections.put(section.getName(), section);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public String applyNameRegexString(String name) {
		return name.replaceAll("<|>|\t(\t)*|\n(\n)*|\r(\r)*| ", "_");
	}
	
	@Override
	public String applyDescriptionRegexString(String description) {
		return description.replaceAll("\t(\t)*", " ");
	}
	
	@Override
	public String popNextLine(List<String> lines) {
		String next = "";
 		do {
			next = lines.remove(0).trim();
		} while (next.isEmpty());
 		return next;
	}
	
	@Override
	public String popNextValue(List<String> lines) {
		int quote_type = -1;
		int arrays_open = 0;
		int objects_open = 0;
		String next = "";
		String result = "";
		
		do {
			next = lines.remove(0);
			for (int i = 0; i < next.length(); i++) {
				if (i > 0 && next.charAt(i - 1) == '\\') continue;
				char c = next.charAt(i);
				if (quote_type < 0) {
					if (c == '"') quote_type = 0;
					if (c == '\'') quote_type = 1;
					if (c == '[') arrays_open++;
					if (c == ']' && arrays_open > 0) arrays_open--;
					if (c == '{') objects_open++;
					if (c == '}' && objects_open > 0) objects_open--;
				} else {
					if (quote_type == 0 && c == '"') quote_type = -1;
					if (quote_type == 1 && c == '\'') quote_type = -1;
				}
			}
			result += (result.isEmpty() ? "" : "\n") + next;
		} while (lines.size() > 0 && (result.trim().isEmpty() || quote_type > -1 || arrays_open > 0 || objects_open > 0));
		return result.trim();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<Section> values() {
		return sections.values();
	}
}
