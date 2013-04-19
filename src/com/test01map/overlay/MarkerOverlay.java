package com.test01map.overlay;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MarkerOverlay extends Overlay {
	// private GeoPoint p;
	List<GeoPoint> geopoints;
	int size;
	public MarkerOverlay(List<GeoPoint> geopoints) {
		this.geopoints = geopoints;
	}

	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {
		super.draw(canvas, mapView, shadow);
		
		Paint paint = new Paint();
		paint.setARGB(100, 0, 0, 255);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(4);

		Paint paintText = new Paint();
		paintText.setTextSize(18);
		paintText.setColor(Color.RED);
		
		size = geopoints.size();
		// ---translate the GeoPoint to screen pixels---
		for (int i = 0; i < size; i++) {
			Point screenPts = new Point();
			mapView.getProjection().toPixels(geopoints.get(i), screenPts);
			canvas.drawCircle(screenPts.x, screenPts.y, 3, paint);
		}
		
		return true;
	}
}