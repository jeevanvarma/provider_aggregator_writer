package com.blogspot.denizstij.paw.common.math;

/**
* @author Deniz Turan , http://denizstij.blogspot.co.uk/, denizstij AT gmail DOT com, 30/05/2015
*/
public class MathUtil {

	public static final float DOUBLE_COMPARE_RESOLUTION=0.00001f;	
	
	public static boolean isEqual(double val1, double val2) {
		
		if (Math.abs(val1-val2)<DOUBLE_COMPARE_RESOLUTION){
			return true;
		}				
		return false;
	}
	
}
