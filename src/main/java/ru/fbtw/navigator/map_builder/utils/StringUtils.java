package ru.fbtw.navigator.map_builder.utils;

import java.util.UUID;

public final class StringUtils {

	public static String toolToString(Class toolClass) {
		String className = toolClass.getName();

		return classNameToString(className, 4, '.');
	}

	private static String classNameToString(String className, int endOffset, char start) {
		int beginIndex = className.lastIndexOf(start) + 1;
		int endIndex = className.length() - endOffset;

		return className.substring(beginIndex, endIndex);
	}

	public static String holderToString(Class holderClass) {
		String className = holderClass.getName();

		return classNameToString(className, 6, '.');
	}

	public static String getOrDefault(String s, String defaultVal) {
		return s == null ? defaultVal : s;
	}

	public static String nextHashName() {
		return UUID.randomUUID()
				.toString()
				.replaceAll("-","")
				.substring(0,16);
	}
}
