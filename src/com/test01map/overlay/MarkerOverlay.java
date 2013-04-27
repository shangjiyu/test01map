package com.test01map.overlay;

//import java.util.List;
//
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Point;

import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.Symbol;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.mapapi.map.MapView;

public class MarkerOverlay extends GraphicsOverlay {
	// private GeoPoint p;
//	List<GeoPoint> geopoints;
	MapView bMapView;
	Geometry markerGeometry;
	Symbol markerSymbol;
	Graphic markerGraphic;
	GeoPoint markerPositionGeoPoint;
	long longMarkerId;
	
	public MarkerOverlay (MapView bMapView, GeoPoint markerPosition){
		super(bMapView);
		this.markerPositionGeoPoint = markerPosition;
		this.markerGeometry = new Geometry();
		markerGeometry.setPoint(this.markerPositionGeoPoint, 3);
		this.markerSymbol = new Symbol();
		Symbol.Color markerColor = markerSymbol.new Color();
		markerColor.red = 0;
		markerColor.green = 255;
		markerColor.blue = 0;
		markerColor.alpha =126;
		markerSymbol.setPointSymbol(markerColor);
		this.markerGraphic = new Graphic(markerGeometry, markerSymbol);
		
	}
	
	public long draw () {
		this.longMarkerId = this.setData(markerGraphic);
		bMapView.getOverlays().add(this);
		bMapView.refresh();
		return this.longMarkerId;
	}
	
	public void delete () {
		this.removeGraphic(this.longMarkerId);
	}
	
	/*public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {
//		super.draw(canvas, mapView, shadow);
		
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
	}*/
}