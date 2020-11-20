package ru.fbtw.navigator.map_builder.utils;

public class StringUtils {
	public static String toolToString(Class toolClass) {
		String className = toolClass.getName();

		int beginIndex = className.lastIndexOf('.') + 1;
		int endIndex = className.length() - 4;

		return className.substring(beginIndex, endIndex);
	}
}
