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

public class MarkerOverlay {
	// private GeoPoint p;
//	List<GeoPoint> geopoints;
	MapView bMapView;
	Geometry markerGeometry;
	Symbol markerSymbol;
	Graphic markerGraphic;
	GraphicsOverlay bGraphicsOverlay;
	GeoPoint markerPositionGeoPoint;
	long longMarkerId;
	
	public MarkerOverlay (MapView bMapView, GraphicsOverlay bGraphicsOverlay, GeoPoint markerPosition){
		this.markerPositionGeoPoint = markerPosition;
		this.markerGeometry = new Geometry();
		markerGeometry.setPoint(this.markerPositionGeoPoint, 3);
		this.markerSymbol = new Symbol();
		Symbol.Color markerColor = markerSymbol.new Color();
		markerColor.red = 0;
		markerColor.green = 0;
		markerColor.blue = 0;
		markerColor.alpha =255;
		markerSymbol.setPointSymbol(markerColor);
		this.markerGraphic = new Graphic(markerGeometry, markerSymbol);
		this.bMapView = bMapView;
		this.bGraphicsOverlay = bGraphicsOverlay;
		this.longMarkerId = this.bGraphicsOverlay.setData(markerGraphic);
	}
	
	public void draw () {
		this.bMapView.getOverlays().add(this.bGraphicsOverlay);
		this.bMapView.refresh();
//		return this.longMarkerId;
	}
	
	public void delete () {
		this.bGraphicsOverlay.removeGraphic(this.longMarkerId);
		this.bMapView.refresh();
	}
}