package com.test01map.overlay;

import java.util.List;

import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Symbol;
import com.baidu.platform.comapi.basestruct.GeoPoint;

//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Point;

public class LineOverlay extends GraphicsOverlay {

//	List<GeoPoint> geopoints;
	GeoPoint[] geoPoints;
	int size;
	Geometry lineGeometry;
	Symbol lineSymbol;
	Graphic lineGraphic;
	MapView bMapView;
	long longPolylineId;

	public LineOverlay(List<GeoPoint> geopoints, MapView bMapView) {
		super(bMapView);
		this.geoPoints = new GeoPoint[geopoints.size()];
		for (int i = 0; i < geopoints.size(); i++) {
			geoPoints[i] = geopoints.get(i);
		}
		this.lineGeometry = new Geometry();
		lineGeometry.setPolyLine(geoPoints);
		this.lineSymbol = new Symbol();
		Symbol.Color lineColor = lineSymbol.new Color();
		lineColor.red =255;
		lineColor.green = 255;
		lineColor.blue = 255;
		lineColor.alpha = 255;
		lineSymbol.setLineSymbol(lineColor, 4);
		this.lineGraphic = new Graphic(lineGeometry, lineSymbol);
		this.bMapView = bMapView;
		
	}
	
	public long draw () {
		this.longPolylineId = this.setData(lineGraphic);
		bMapView.getOverlays().add(this);
		bMapView.refresh();
		return this.longPolylineId;
	}
	
	public void delete () {
		this.removeGraphic(this.longPolylineId);
	}
	
//	@Override
//	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
//			long when) {
//		Projection projection = mapView.getProjection();
//		Paint paint = new Paint();
//		paint.setColor(Color.RED);
//		paint.setDither(true);
//		paint.setStyle(Paint.Style.FILL_AND_STROKE);
//		paint.setStrokeJoin(Paint.Join.ROUND);
//		paint.setStrokeCap(Paint.Cap.ROUND);
//		paint.setAntiAlias(true);
//		paint.setStrokeWidth(5);
//		size = geopoints.size();
//
//		if (geopoints != null && size >= 2) {
//			Point start = projection.toPixels(geopoints.get(0), null);
//			for (int i = 1; i < size; ++i) {
//				Point end = projection.toPixels(geopoints.get(i), null);
//				canvas.drawLine(start.x, start.y, end.x, end.y, paint);
//				start = end;
//			}
//		}
//		return super.draw(canvas, mapView, shadow, when);
//	}
}
