package com.test01map.overlay;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class LineOverlay extends Overlay {

	List<GeoPoint> geopoints;
	int size;

	public LineOverlay() {

	}

	public LineOverlay(List<GeoPoint> geopoints) {
		this.geopoints = geopoints;		
	}

	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {
		Projection projection = mapView.getProjection();
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setDither(true);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(5);
		size = geopoints.size();

		if (geopoints != null && size >= 2) {
			Point start = projection.toPixels(geopoints.get(0), null);
			for (int i = 1; i < size; ++i) {
				Point end = projection.toPixels(geopoints.get(i), null);
				canvas.drawLine(start.x, start.y, end.x, end.y, paint);
				start = end;
			}
		}
		return super.draw(canvas, mapView, shadow, when);
	}
}
