package com.test01map.overlay;

import java.util.List;

//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Paint.Style;
//import android.graphics.Path;
//import android.graphics.Point;

import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.Symbol;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class PolygonOverlay {

//	List<GeoPoint> geopoints;
	GeoPoint[] geoPoints;
	int size;
	Geometry polygonGeometry;
	Symbol polygonSymbol;
	Graphic polygonGraphic;
	MapView bMapView;
	long longPolygonId;
	GraphicsOverlay bGraphicsOverlay;
	
	public PolygonOverlay(List<GeoPoint> geopoints, MapView bMapView, GraphicsOverlay bGraphicsOverlay) {
		this.geoPoints = new GeoPoint[(geopoints.size()+1)];
		for (int i = 0; i < geopoints.size(); i++) {
			geoPoints[i] = geopoints.get(i);
		}
		this.geoPoints[geopoints.size()] = geopoints.get(0);
		this.polygonGeometry = new Geometry();
		this.polygonGeometry.setPolyLine(geoPoints);
		this.polygonSymbol = new Symbol();
		Symbol.Color polygonColor = polygonSymbol.new Color();
		polygonColor.red = 0;
		polygonColor.green = 255;
		polygonColor.blue = 0;
		polygonColor.alpha = 126;
		polygonSymbol.setSurface(polygonColor, 1, 4);
		this.polygonGraphic = new Graphic(polygonGeometry, polygonSymbol);
		this.bGraphicsOverlay = bGraphicsOverlay;
		this.bMapView = bMapView;
		this.longPolygonId = this.bGraphicsOverlay.setData(polygonGraphic);
	}
	
	public long draw () {
		this.bMapView.getOverlays().add(this.bGraphicsOverlay);
		this.bMapView.refresh();
		return this.longPolygonId;
	}
	
	public void delete () {
		this.bGraphicsOverlay.removeGraphic(this.longPolygonId);
		this.bMapView.refresh();
	}
	
	/*@Override
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
	}*/

}
