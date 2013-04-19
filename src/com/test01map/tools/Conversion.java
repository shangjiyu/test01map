package com.test01map.tools;

import java.text.DecimalFormat;


public class Conversion {

	public static String ConversionD(int nub){
		DecimalFormat dt=(DecimalFormat) DecimalFormat.getInstance(); //获得格式化类对象
		dt.applyPattern("0");
		double i = nub;
		String mOrkm = dt.format(i) + "m";
		if (nub > 1000) {
			dt.applyPattern("0.00");
			i = i/1000;
			mOrkm = dt.format(i) + "km";; 
		}
		return mOrkm;
	}
	
	public static String ConversionA(double nub){
		DecimalFormat dt=(DecimalFormat) DecimalFormat.getInstance(); //获得格式化类对象
		dt.applyPattern("0");
		double i = nub;
		String mOrkm =dt.format(i) + "sq.m";
		if (nub > 1000000) {
			dt.applyPattern("0.00");
			i = i/1000000;
			mOrkm = dt.format(i) + "sq.km";
		}
		return mOrkm;
	}
}
