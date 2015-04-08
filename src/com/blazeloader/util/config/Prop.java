package com.blazeloader.util.config;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Prop<T> implements IProperty<T> {
	private final IConfig cfg;
	
	private static final Map<Class, String> classToType = new HashMap<Class, String>();
	private static final Map<String, Class> typeToClass = new HashMap<String, Class>();
	
	private T currentValue;
	private T defaultValue;
	
	protected final String propertyName;
	
	private String description = "";
	
	protected boolean loaded = false;
	
	protected Prop(IConfig config, List<String> lines) {
		cfg = config;
		String first = lines.get(0);
		if (first.startsWith("\t#")) {
			description = first.substring(2, first.length());
			first = lines.get(0);
			lines.remove(0);
		}
		if (first.startsWith("\t@default: ")) {
			defaultValue = currentValue = (T)first.substring("\t@default: ".length(), first.length());
			first = lines.get(0);
			lines.remove(0);
		}
		propertyName = first.substring(1, first.length()).split("<")[0];
		String[] remain = first.substring(propertyName.length() + 1).split(">: ");
		String type = remain[0].substring(1, remain[0].length());
		String value = "";
		for (int i = 1; i < remain.length; i++) {
			value += remain[i];
		}
		currentValue = (T)parseValue(type, value);
		loaded = true;
	}
	
	protected Prop(IConfig config, String name, T def) {
		cfg = config;
		propertyName = cfg.applyNameRegexString(name);
		defaultValue = def;
		currentValue = def;
	}
	
	public String getName() {
		return propertyName;
	}
	
	public void setDefault(T newDef) {
		defaultValue = newDef;
	}
	
	public void reset() {
		set(defaultValue);
	}
	
	public T get() {
		return currentValue;
	}
	
	public void set(T val) {
		currentValue = val;
	}
	
	public void setDescription(String desc) {
		if (desc == null) {
			description = "";
		} else {
			description = cfg.applyDescriptionRegexString(desc);
		}
	}
	
	public String getType() {
		if (defaultValue instanceof String) return "S";
		if (defaultValue instanceof Integer) return "I";
		if (defaultValue instanceof Float) return "F";
		if (defaultValue instanceof Character) return "C";
		if (defaultValue instanceof Boolean) return "B";
		return getType(defaultValue.getClass());
	}
	
	protected void writeTo(StringBuilder builder) {
		if (!description.isEmpty()) {
			builder.append("\t# ");
			builder.append(description);
		}
		builder.append("\n\t@default: ");
		builder.append(defaultValue.toString());
		builder.append("\n\t");
		String type = getType();
		if (!"~null~".contentEquals(type)) {
			builder.append("<");
			builder.append(type);
			builder.append(">");
		}
		builder.append(propertyName);
		builder.append(": ");
		builder.append(currentValue.toString());
	}
	
	private static Object parseValue(String type, String value) {
		if (type.endsWith("[]")) {
			type = type.substring(0, type.length() - 2);
			String[] values = value.substring(1, value.length() -1).split(", ");
			Object[] arr = new Object[values.length];
			for (int i = 0; i < arr.length; i++) {
				arr[i] = parseValue(type, values[i]);
			}
			return arr;
		}
		if ("S".contentEquals(type)) return value;
		if ("I".contentEquals(type)) return Integer.valueOf(value);
		if ("F".contentEquals(type)) return Float.valueOf(value);
		if ("C".contentEquals(type)) return Character.valueOf(value.toCharArray()[0]);
		if ("B".contentEquals(type)) return Character.valueOf(value.toCharArray()[0]);
		Class typeClass = getTypeClass(type);
		if (typeClass != null) {
			try {
				if (IStringable.class.isAssignableFrom(typeClass)) {
					return ((IStringable)typeClass.newInstance()).valueOf(value);
				} else {
					Method m = typeClass.getMethod("valueOf", String.class);
					if (!m.isAccessible()) {
						m.setAccessible(true);
					}
					return m.invoke(typeClass.newInstance(), value);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static boolean hasType(Class type) {
		return classToType.containsKey(type);
	}
	
	public static boolean hasType(String typeString) {
		return typeToClass.containsKey(typeString);
	}
	
	public static String getType(Class type) {
		if (hasType(type)) {
			return classToType.get(type);
		} else {
			if (type.isArray()) {
				return getType(type.getComponentType()) + "[]";
			}
		}
		return "~null~";
	}
	
	public static Class getTypeClass(String typeString) {
		if (hasType(typeString)) {
			return typeToClass.get(typeString);
		}
		return null;
	}
	
	public static void registerType(Class<? extends IStringable> type, String typeString) {
		classToType.put(type, typeString);
		typeToClass.put(typeString, type);
	}
}
