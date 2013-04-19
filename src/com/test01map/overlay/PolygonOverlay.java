package com.test01map.overlay;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class PolygonOverlay extends Overlay {

	List<GeoPoint> geopoints;
	int size;

	public PolygonOverlay(List<GeoPoint> geopoints) {

		this.geopoints = geopoints;
	}

	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {
		super.draw(canvas, mapView, shadow, when);

		Projection projection = mapView.getProjection();
		Paint wallpaint = new Paint();
		Path wallpath = new Path();
		
		wallpaint.setStyle(Style.FILL);
		size = geopoints.size();
		if (size >= 3) {
			wallpaint.setStyle(Paint.Style.FILL_AND_STROKE);
			wallpaint.setStrokeJoin(Paint.Join.ROUND);
			wallpaint.setStrokeCap(Paint.Cap.ROUND);
			wallpaint.setAntiAlias(true);
			wallpaint.setStrokeWidth(5);
			wallpaint.setColor(Color.GREEN);
			Point first = projection.toPixels(geopoints.get(0), null);
			Point last = projection.toPixels(geopoints.get(size - 1),
					null);
			canvas.drawLine(first.x, first.y, last.x, last.y, wallpaint);
		}
		wallpaint.setColor(Color.argb(50, 255, 0, 0));
		for (int i = 0; i < size; ++i) {
			Point point = projection.toPixels(geopoints.get(i), null);
			if (0 == i) {
				wallpath.moveTo(point.x,point.y);
			}
			else{
				 wallpath.lineTo(point.x,point.y);
			}
		}
		canvas.drawPath(wallpath, wallpaint);
	   return super.draw(canvas, mapView, shadow, when);
	}

}
