package com.test01map.overlay;

import java.util.List;

import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Symbol;
import com.baidu.platform.comapi.basestruct.GeoPoint;


/**
 * @ClassName: LineOverlay
 * @Description: TODO(折线覆盖物)
 * @author jiyu
 * @date 2013-8-12 下午9:14:27
 *
 */
public class LineOverlay {

//	List<GeoPoint> geopoints;
	GeoPoint[] geoPoints;
	int size;
	Geometry lineGeometry;
	Symbol lineSymbol;
	Graphic lineGraphic;
	GraphicsOverlay bGraphicsOverlay;
	MapView bMapView;
	long longPolylineId;

	public LineOverlay(List<GeoPoint> geopoints, MapView bMapView, GraphicsOverlay bGraphicsOverlay) {
		this.geoPoints = new GeoPoint[geopoints.size()];
		for (int i = 0; i < geopoints.size(); i++) {
			geoPoints[i] = geopoints.get(i);
		}
		this.lineGeometry = new Geometry();
		lineGeometry.setPolyLine(geoPoints);
		this.lineSymbol = new Symbol();
		Symbol.Color lineColor = lineSymbol.new Color();
		lineColor.red =0;
		lineColor.green = 0;
		lineColor.blue = 0;
		lineColor.alpha = 255;
		lineSymbol.setLineSymbol(lineColor, 4);
		this.lineGraphic = new Graphic(lineGeometry, lineSymbol);
		this.bGraphicsOverlay = bGraphicsOverlay;
		this.bMapView = bMapView;
		this.longPolylineId = this.bGraphicsOverlay.setData(lineGraphic);
	}
	
	public long draw () {
		this.bMapView.getOverlays().add(this.bGraphicsOverlay);
		this.bMapView.refresh();
		return this.longPolylineId;
	}
	
	public void delete () {
		this.bGraphicsOverlay.removeGraphic(this.longPolylineId);
		this.bMapView.refresh();
	}
}
