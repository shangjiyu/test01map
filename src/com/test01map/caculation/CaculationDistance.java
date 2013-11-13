package com.test01map.caculation;

import java.util.List;

import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.graphics.PointF;

public class CaculationDistance {

	
	/********************************************************
	 * @Title: getDistance
	 * @Description: TODO(显示屏坐标计算距离)
	 * @param @param listPointFs
	 * @param @return    设定文件
	 * @return int    返回类型
	 * @throws
	 */
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
	
	/********************************************************
	 * @Title: getBaiduDistance
	 * @Description: TODO(百度给点计算距离接口)
	 * @param @param geoPoints
	 * @param @return    设定文件
	 * @return double    返回类型
	 * @throws
	 */
	public static double getBaiduDistance(List<GeoPoint> geoPoints) {
		double dis = 0.0;
		for (int i = 0; i < geoPoints.size()-1; i++) {
			dis = dis+DistanceUtil.getDistance(geoPoints.get(i), geoPoints.get(i+1));
		}
		return dis;
	}
}
