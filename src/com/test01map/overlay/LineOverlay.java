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
	Geometry lineGeometry;
	Symbol lineSymbol;
	Graphic lineGraphic;
	GraphicsOverlay bGraphicsOverlay;
	MapView bMapView;
	long longPolylineId;

	public LineOverlay(MapView bMapView) {
		this.bMapView = bMapView;
		this.bGraphicsOverlay = new GraphicsOverlay(this.bMapView);
		this.bMapView.getOverlays().add(this.bGraphicsOverlay);
	}
	
	public long draw (List<GeoPoint> geopoints) {
		this.bGraphicsOverlay.removeAll();
		final Geometry lineGeometry = new Geometry();
		lineGeometry.setPolyLine((GeoPoint[]) geopoints.toArray(new GeoPoint[0]));
		final Symbol lineSymbol = new Symbol();
		final Symbol.Color lineColor = lineSymbol.new Color();
		lineColor.red =0;
		lineColor.green = 0;
		lineColor.blue = 0;
		lineColor.alpha = 255;
		lineSymbol.setLineSymbol(lineColor, 4);
		final Graphic lineGraphic = new Graphic(lineGeometry, lineSymbol);
		this.bGraphicsOverlay.setData(lineGraphic);
		this.bMapView.refresh();
		return this.longPolylineId;
	}
	
	public void delete () {
		this.bGraphicsOverlay.removeAll();
		this.bMapView.refresh();
	}
}
