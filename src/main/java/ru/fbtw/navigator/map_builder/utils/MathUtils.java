package ru.fbtw.navigator.map_builder.utils;

public final class MathUtils {
	public static double sqr(double x){
		return x*x;
	}

	public static boolean doubleEquals(double a, double b, double e){
		return Math.abs(a - b) <= e;
	}

	public static int getDivisor(int n){
		int start = (int)Math.round(Math.sqrt(n));
		for(int i = start; i>0; i--){
			if(n % i == 0){
				return i;
			}
		}
		return 1;
	}
}
