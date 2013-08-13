package com.test01map.tools;

import java.text.DecimalFormat;


/**
 * @ClassName: Conversion
 * @Description: TODO(此类用以转换计算地图覆盖物所展示的距离和面积)
 * @author jiyu
 * @date 2013-8-12 下午9:01:20
 *
 */
public class Conversion {

	public static String ConversionD(int nub){
		//转换距离
		DecimalFormat dt=(DecimalFormat) DecimalFormat.getInstance();
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
		//转换面积
		DecimalFormat dt=(DecimalFormat) DecimalFormat.getInstance(); 
		dt.applyPattern("0");
		double i = nub;
		String mOrkm =dt.format(i) + "㎡";
		if (nub > 1000000) {
			dt.applyPattern("0.00");
			i = i/1000000;
//			mOrkm = dt.format(i) + "sq.km";
			mOrkm = dt.format(i) + "k㎡";
		}
		return mOrkm;
	}
}
