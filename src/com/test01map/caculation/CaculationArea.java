package com.test01map.caculation;

import java.util.List;

import android.graphics.PointF;

public class CaculationArea {

	final static double earthRadiusMeters = 6371000.0;
	final static double metersPerDegree = 2.0 * Math.PI * earthRadiusMeters
			/ 360.0;
	final static double radiansPerDegree = Math.PI / 180.0;
	final static double degreesPerRadian = 180.0 / Math.PI;
	

	public static double calculateArea(List<PointF> listGps) {

		int size =listGps.size();
		double areaMeters2 = 0;
		if (size > 2) {
			areaMeters2 = PlanarPolygonAreaMeters2(listGps,size);
			if (areaMeters2 > 1000000.0)
				areaMeters2 = SphericalPolygonAreaMeters2(listGps,size);
		}
		return areaMeters2;
	}

	public static double PlanarPolygonAreaMeters2(List<PointF> listGps,int size) {
		double a = 0.0;
		for (int i = 0; i < size; ++i) {
			int j = (i + 1) % size;
			double xi = (double) listGps.get(i).y
					/ 1000000
					* metersPerDegree
					* Math.cos(((double) listGps.get(i).x) / 1000000
							* radiansPerDegree);
			double yi = ((double) listGps.get(i).x) / 1000000 * metersPerDegree;
			double xj = ((double) listGps.get(j).y)
					/ 1000000
					* metersPerDegree
					* Math.cos(((double) listGps.get(j).x) / 1000000
							* radiansPerDegree);
			double yj = ((double) listGps.get(j).x) / 1000000 * metersPerDegree;
			a += xi * yj - xj * yi;
		}
		return Math.abs(a / 2.0);
	}

	public static  double SphericalPolygonAreaMeters2(List<PointF> listGps,int size) {

		double totalAngle = 0.0;
		for (int i = 0; i < size; ++i) {
			int j = (i + 1) % size;
			int k = (i + 2) % size;
			totalAngle += Angle((double) listGps.get(i).y / 1000000,
					(double) listGps.get(i).x / 1000000,
					(double) listGps.get(j).y / 1000000,
					(double) listGps.get(j).x / 1000000,
					(double) listGps.get(k).y / 1000000,
					(double) listGps.get(k).x / 1000000);
		}
		double planarTotalAngle = (size - 2) * 180.0;
		double sphericalExcess = totalAngle - planarTotalAngle;
		if (sphericalExcess > 420.0) {
			totalAngle = size * 360.0 - totalAngle;
			sphericalExcess = totalAngle - planarTotalAngle;
		} else if (sphericalExcess > 300.0 && sphericalExcess < 420.0) {
			sphericalExcess = Math.abs(360.0 - sphericalExcess);
		}
		return sphericalExcess * radiansPerDegree * earthRadiusMeters
				* earthRadiusMeters;
	}

	public static  double Angle(double iX, double iY, double jX, double jY,
			double kX, double kY) {
		double bearing21 = Bearing(jY, jX, iY, iX);
		double bearing23 = Bearing(jY, jX, kY, kX);
		double angle = bearing21 - bearing23;
		if (angle < 0.0)
			angle += 360.0;
		return angle;
	}

	public static  double Bearing(double fromX, double fromY, double toX,
			double toY) {
		double lat1 = fromX * radiansPerDegree;
		double lon1 = fromY * radiansPerDegree;
		double lat2 = toX * radiansPerDegree;
		double lon2 = toY * radiansPerDegree;
		double angle0 = -Math.atan2(
				Math.sin(lon1 - lon2) * Math.cos(lat2),
				Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
						* Math.cos(lat2) * Math.cos(lon1 - lon2));
		if (angle0 < 0.0)
			angle0 += Math.PI * 2.0;
		angle0 = angle0 * degreesPerRadian;
		return angle0;
	}
}
