package ru.fbtw.navigator.map_builder.utils;

public class StringUtils {


	public static String toolToString(Class toolClass) {
		String className = toolClass.getName();

		return classNameToString(className,4,'.' );
	}

	private static String classNameToString(String className, int endOffset, char start) {
		int beginIndex = className.lastIndexOf(start) + 1;
		int endIndex = className.length() - endOffset;

		return className.substring(beginIndex, endIndex);
	}

	public static String holderToString(Class holderClass){
		String className = holderClass.getName();

		return classNameToString(className,6,'.');
	}
}
