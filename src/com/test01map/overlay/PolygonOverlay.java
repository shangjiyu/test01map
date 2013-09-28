package com.test01map.overlay;

import java.util.List;

import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.Symbol;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class PolygonOverlay {

	MapView bMapView;
	GraphicsOverlay bGraphicsOverlay;
	
	public PolygonOverlay(MapView bMapView) {
		this.bMapView = bMapView;
		this.bGraphicsOverlay = new GraphicsOverlay(bMapView);
		this.bMapView.getOverlays().add(this.bGraphicsOverlay);
	}
	
	public long draw (List<GeoPoint> geopoints) {
		this.bGraphicsOverlay.removeAll();
		final Geometry polygonGeometry = new Geometry();
		polygonGeometry.setPolygon((GeoPoint[]) geopoints.toArray(new GeoPoint[0]));
		final Symbol polygonSymbol = new Symbol();
		final Symbol.Color polygonColor = polygonSymbol.new Color();
		polygonColor.red = 0;
		polygonColor.green = 255;
		polygonColor.blue = 0;
		polygonColor.alpha = 126;
		polygonSymbol.setSurface(polygonColor, 1, 4);
		final Graphic polygonGraphic = new Graphic(polygonGeometry, polygonSymbol);
		final long longPolygonId = this.bGraphicsOverlay.setData(polygonGraphic);
		this.bMapView.refresh();
		return longPolygonId;
	}
	
	public void delete () {
		this.bGraphicsOverlay.removeAll();
		this.bMapView.refresh();
	}
}
