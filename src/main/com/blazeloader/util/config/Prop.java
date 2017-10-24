package com.blazeloader.util.config;

import java.util.List;

import com.blazeloader.api.compatibility.ISubscription;
import com.blazeloader.api.compatibility.IWatchable;

public class Prop<T> implements IProperty<T>, IWatchable<T> {
	private final IConfig cfg;
	
	private Class<T> typeClass;
	private IWrapObject<T> currentValue;
	private IWrapObject<T> defaultValue;
	
	private ISubscription<T> subscriber = null;
	
	private final String propertyName;
	
	private String description = "";
	
	protected boolean loaded = false;
	
	protected Prop(IConfig config, List<String> lines) {
		cfg = config;
		defaultValue = new StringableObject<T>(null);
		currentValue = new StringableObject<T>(null);
		checkForComment(lines);
		String first = cfg.popNextValue(lines);
		String def = null;
		if (first.startsWith("@default: ")) {
			def = first.substring("@default:".length(), first.length()).split("\\(")[0].trim();
			checkForComment(lines);
			first = cfg.popNextValue(lines);
		}
		String[] remain = first.split(":");
		propertyName = remain[0].trim();
		String value = "";
		for (int i = 1; i < remain.length; i++) {
			value += remain[i];
		}
		if (def != null) {
			defaultValue.set(unescapeValue(def));
			currentValue.set(defaultValue.get());
		}
		if (!value.trim().isEmpty()) {
			currentValue.set(unescapeValue(value));
		}
		if (defaultValue.get() != null) {
			typeClass = defaultValue.getObjectClass();
		} else {
			typeClass = currentValue.getObjectClass();
		}
		loaded = true;
	}
		
	@SuppressWarnings("unchecked")
	protected Prop(IConfig config, String name, T def) {
		cfg = config;
		typeClass = (Class<T>)def.getClass();
		propertyName = cfg.applyNameRegexString(name);
		
		if (typeClass.isArray()) {
			defaultValue = (IWrapObject<T>) new StringableArray<T>((T[])def); 
			currentValue = (IWrapObject<T>) new StringableArray<T>((T[])defaultValue.get());
		} else {
			defaultValue = new StringableObject<T>(def);
			currentValue = new StringableObject<T>(def);
		}
	}
	
	public String getName() {
		return propertyName;
	}
	
	public Prop<T> setDefault(T newDef) {
		defaultValue.set(newDef);
		return this;
	}
	
	public T getDefault() {
		return defaultValue.get();
	}
	
	public void reset() {
		set(defaultValue.get());
	}
	
	public T get() {
		return currentValue.get();
	}
	
	public void set(T val) {
		T old = currentValue.get();
		if (change(old, val)) currentValue.set(val);
	}
	
	public Class<T> getTypeClass() {
		return typeClass;
	}
	
	public String getType() {
		return typeClass.getSimpleName();
	}
	
	@SuppressWarnings("unchecked")
	public T[] getPossibleValues() {
		if (defaultValue.get() instanceof Boolean) {
			return (T[])new Boolean[] {true,false};
		} else if (typeClass.isEnum()) {
			return (T[])typeClass.getEnumConstants();
		}
		return null;
	}
	
	public Prop<T> watch(ISubscription<T> subscriber) {
		this.subscriber = subscriber;
		return this;
	}
	
	public boolean change(T old, T neu) {
		if (subscriber != null) {
			return subscriber.valueChanged(this.propertyName, old, neu);
		}
		return true;
	}
	
	public Prop<T> setDescription(String... desc) {
		StringBuilder full = new StringBuilder();
		for (String i : desc) {
			full.append(i);
			full.append("\r\n");
		}
		if (desc.length == 0 || full.toString().isEmpty()) {
			description = "";
		} else {
			description = cfg.applyDescriptionRegexString(full.toString().trim());
		}
		return this;
	}
	
	@SuppressWarnings("unchecked")
	protected void updateType(T def) {
		if (typeClass != def.getClass()) {
			typeClass = (Class<T>)def.getClass();
			if (currentValue.get() instanceof String) {
				if (typeClass.isArray()) {
					currentValue = (IWrapObject<T>) new StringableArray<T>((T[])def, (String)currentValue.get());
					defaultValue = (IWrapObject<T>) new StringableArray<T>((T[])def);
				} else {
					currentValue.fromString(def, ((String)currentValue.get()));
					defaultValue.set(def);
				}
			}
		}
	}
	
	protected void writeTo(StringBuilder builder) {
		if (!description.isEmpty()) {
			String[] descriptions = description.split("\n");
			for (int i = 0; i < descriptions.length; i++) {
				builder.append("   #");
				builder.append(descriptions[i].trim());
				builder.append("\r\n");
			}
		}
		
		if (cfg.getWriteDefaults()) {
			builder.append("   @default: ");
			
			if (typeClass == String.class) {
				builder.append("\"" + defaultValue.toString() + "\"");
			} else {
				builder.append(defaultValue.toString());
			}
			T[] possibles = getPossibleValues();
			if (possibles != null) {
				builder.append(" (");
				for (int i = 0; i < possibles.length; i++) {
					if (i > 0) builder.append(", ");
					builder.append(possibles[i].toString());
				}
				builder.append(")");
			}
			builder.append("\r\n");
		}
		builder.append("   ");
		builder.append(propertyName);
		builder.append(": ");
		if (typeClass == String.class) {
			builder.append("\"" + currentValue.toString() + "\"");
		} else {
			builder.append(currentValue.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	private T unescapeValue(String value) {
		value = value.trim();
		if (value.startsWith("\"")) value = value.substring(1, value.length());
		if (value.endsWith("\"")) value = value.substring(0, value.length() - 1);
		return (T)value;
	}
	
	private void checkForComment(List<String> lines) {
		while (lines.get(0).trim().startsWith("#")) {
			String first = cfg.popNextLine(lines);
			if (!description.isEmpty()) {
				description += "\r\n";
			}
			description += first.substring(1, first.length());
		}
	}
}
