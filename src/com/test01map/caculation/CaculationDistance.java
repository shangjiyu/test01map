package com.test01map.caculation;

import java.util.List;

import android.graphics.PointF;

public class CaculationDistance {

	
	public static int getDistance(List<PointF> listPointFs) {
		int size = listPointFs.size();
		int distance = 0;

		for (int i = 0; i < size - 1; i++) {
			double lat_a = (listPointFs.get(i).x)/1000000;
			double lng_a = (listPointFs.get(i).y)/1000000;
			double lat_b = (listPointFs.get(i + 1).x)/1000000;
			double lng_b = (listPointFs.get(i + 1).y)/1000000;
			double radLat1 = (lat_a * Math.PI / 180.0);
			double radLat2 = (lat_b * Math.PI / 180.0);
			double a = radLat1 - radLat2;
			double b = (lng_a - lng_b) * Math.PI / 180.0;
			double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
			+ Math.cos(radLat1) * Math.cos(radLat2)
			* Math.pow(Math.sin(b / 2), 2)));
			s = s * 6378137.0;
			s = Math.round(s * 10000) / 10000;
			distance = (int)(s + distance);
		}
		return distance;
	}
}
