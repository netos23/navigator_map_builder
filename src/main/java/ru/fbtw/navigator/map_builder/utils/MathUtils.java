package ru.fbtw.navigator.map_builder.utils;

public class MathUtils {
	public static double sqr(double x){
		return x*x;
	}

	public static boolean doubleEquals(double a, double b, double e){
		return Math.abs(a - b) <= e;
	}
}
