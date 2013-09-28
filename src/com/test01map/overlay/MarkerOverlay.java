package com.test01map.overlay;

//import java.util.List;
//
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * @ClassName: MarkerOverlay
 * @Description: TODO(点标记)
 * @author shangjiyu
 * @date 2013-8-12 下午9:15:05
 *
 */
public class MarkerOverlay {
	ArrayList<GeoPoint> geoPoints;
	MapView bMapView;
	static Context context;
	ItemizedOverlay<OverlayItem> itemizedOverlay;
	
	public MarkerOverlay (MapView bMapView, Context context, Drawable marker){
		this.bMapView = bMapView;
		MarkerOverlay.context = context;
		this.itemizedOverlay = new ItemOverlay(marker, bMapView);
		this.bMapView.getOverlays().add(this.itemizedOverlay);
	}
	
	/**
	 * @Title: draw
	 * @Description: TODO(画到地图上去)
	 * @param     设定文件
	 * @return void    返回类型
	 * @throws
	 */
	public void draw (List<GeoPoint> geoPoints) {
		this.itemizedOverlay.removeAll();
		List<OverlayItem> overlayItems = new ArrayList<OverlayItem>();
		for (int i = 0; i < geoPoints.size(); i++) {
			overlayItems.add(new OverlayItem(geoPoints.get(i), "point"+i, "point"+i));
		}
		this.itemizedOverlay.addItem(overlayItems);
		this.bMapView.refresh();
//		return this.longMarkerId;
	}
	
	/**
	 * @Title: delete
	 * @Description: TODO(从地图上删掉)
	 * @param     设定文件
	 * @return void    返回类型
	 * @throws
	 */
	public void delete () {
		this.itemizedOverlay.removeAll();
		this.bMapView.refresh();
	}
	
	/**
	 * @ClassName: ItemOverlay
	 * @Description: TODO(itemoverlay class)
	 * @author shangjiyu
	 * @date 2013-9-28 下午2:15:10
	 *
	 */
	public class ItemOverlay extends ItemizedOverlay<OverlayItem> {
		
		public ItemOverlay (Drawable marker,MapView mapView) {
			super(marker, mapView);
		}
		
		protected boolean onTap(int index) {
			//在此处理item点击事件
			System.out.println("item onTap: "+index);
			return true; 
		}
		
		public boolean onTap (GeoPoint point,MapView mapView) {
			//在此处理MapView的点击事件，当返回 true时  
			super.onTap(point,mapView);
			 return false;
		}
	}
}