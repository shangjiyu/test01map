package com.test01map;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import com.test01map.MyLocationManager.LocationCallBack;
import com.test01map.caculation.CaculationArea;
import com.test01map.caculation.CaculationDistance;
import com.test01map.overlay.LineOverlay;
import com.test01map.overlay.MarkerOverlay;
import com.test01map.overlay.PolygonOverlay;
import com.test01map.tools.Conversion;

public class Main extends SherlockActivity implements LocationCallBack,
		OnClickListener {

	private MyLocationManager mylocation;
	GeoPoint p, pGps;
//	LatLng p,pGps;
	LineOverlay polyline = null;
	PolygonOverlay polygon = null;
//	List<Overlay> listOfOverlays = new ArrayList<Overlay>();
	List<PointF> listpoint = new ArrayList<PointF>();
	List<GeoPoint> geopoints = new ArrayList<GeoPoint>();
//	List<LatLng> geopoints = new ArrayList<LatLng>();
	List<Location> locations = new ArrayList<Location>();
	List<MarkerOverlay> markers = new ArrayList<MarkerOverlay>();
	boolean addpoint = false;
//	GoogleMap map;
	BMapManager bMapManager;
	MapView bMapView;
	MapController bMapController;
//	MapView mapView;
//	GoogleMap mapView
//	MapOverlay mapOverlay;
	PointF pointF, pointFGps;

	Button redoButton;
	Button cleanButton;
	ToggleButton areaToggleButton;
	ToggleButton manulyToggleButton;
	ToggleButton mapTypeToggleButton;
	
	TextView TextDistance;
	TextView TextArea;

	String dStr;

	Boolean Polygon = false;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		bMapManager = new BMapManager(getApplication());
		bMapManager.init("31EB3BBC63C599FB0D5C6F9E2E2BE3FFE4E726FB", null);
		setContentView(R.layout.main);
		
		TextDistance = (TextView) findViewById(R.id.Distance);
		TextArea = (TextView) findViewById(R.id.Area);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
//		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		
		bMapView = (MapView) findViewById(R.id.bmapsView);
		bMapView.setBuiltInZoomControls(true);
		bMapView.setSatellite(true);
//		设置启用内置的缩放控件
		bMapController = bMapView.getController();
//		取得地图控制权
		bMapController.setZoom(15);
		
		MyLocationManager.init(Main.this.getApplicationContext(), Main.this);
		mylocation = MyLocationManager.getInstance();
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
//		MenuCompat.setShowAsAction(menu.findItem(R.id.mapTypeToggleButton), 1);
		mapTypeToggleButton = (ToggleButton) menu.findItem(R.id.mapTypeToggleButton).getActionView();
		mapTypeToggleButton.setTextOn("卫星");
		mapTypeToggleButton.setTextOff("普通");
		mapTypeToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (!isChecked) {
//					map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					bMapView.setSatellite(false);
					if (!geopoints.isEmpty()) {
						listpoint.removeAll(listpoint);
						geopoints.removeAll(geopoints);
						for (int i = 0; i < markers.size(); i++) {
							markers.get(i).delete();
						}
						markers.removeAll(markers);
						overlayAndtextShow();
					}
				} else {
//					map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
					bMapView.setSatellite(true);
					if (!geopoints.isEmpty()) {
						listpoint.removeAll(listpoint);
						geopoints.removeAll(geopoints);
						for (int i = 0; i < markers.size(); i++) {
							markers.get(i).delete();
						}
						markers.removeAll(markers);
						overlayAndtextShow();
					}
				}
//				bMapView.refresh();
			}
		});
		mapTypeToggleButton.setChecked(true);
		areaToggleButton = (ToggleButton) menu.findItem(R.id.areaToggleButton).getActionView();
		areaToggleButton.setTextOff("距离");
		areaToggleButton.setTextOn("面积");
		areaToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (!isChecked) {
//					buttonView.setText("不算面积");
					Polygon = false;
					if (polygon != null) {
						polygon.delete();
						polygon = null;
					}
					TextArea.setText("A:0㎡");
				} else {
//					buttonView.setText("计算面积");
					Polygon = true;
					if (geopoints.size() > 2) {
						if (polygon != null) {
							polygon.delete();
							polygon = new PolygonOverlay(geopoints, bMapView);
							polygon.draw();
						}
					}
					dStr = Conversion.ConversionA(CaculationArea.calculateArea(listpoint));
//					dStr = Conversion.ConversionA(CaculationArea.calculateArea(geopoints));
					TextArea.setText("A:" + dStr);
				}
//				bMapView.refresh();
			}
		});
		areaToggleButton.setChecked(true);
		manulyToggleButton = (ToggleButton) menu.findItem(R.id.manulyToggleButton).getActionView();
		manulyToggleButton.setTextOff("手动");
		manulyToggleButton.setTextOn("定位");
		manulyToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (!isChecked) {
//					buttonView.setText("手动取点");
					mylocation.destoryLocationManager();
					setOnMapClickListener(true);
				} else {
//					buttonView.setText("自动定位");
//					buttonView.setTextColor();
					mylocation.addLocationManager();
					setOnMapClickListener(false);
				}
			}
		});
		manulyToggleButton.setChecked(false);
		redoButton = (Button) menu.findItem(R.id.redoButton).getActionView();
		redoButton.setText(R.string.redoButton);
		redoButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!geopoints.isEmpty()) {
					listpoint.remove(listpoint.size() - 1);
					geopoints.remove(geopoints.size() - 1);
					markers.get(markers.size()-1).delete();
					markers.remove(markers.size()-1);
					overlayAndtextShow();
				} else {
					Toast.makeText(MyApplication.getInstance(), "已经删完...",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		cleanButton = (Button) menu.findItem(R.id.clearButton).getActionView();
		cleanButton.setText(R.string.clearButton);
		cleanButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!geopoints.isEmpty()) {
					listpoint.removeAll(listpoint);
					geopoints.removeAll(geopoints);
					for (int i = 0; i < markers.size(); i++) {
						markers.get(i).delete();
					}
					markers.removeAll(markers);
					overlayAndtextShow();
				} else {
					Toast.makeText(MyApplication.getInstance(), "已经删空...",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		return true;
	}
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(this);
		switch (item.getItemId()) {
		case R.id.exit:
			alertbBuilder.setTitle("退出程序")
				.setMessage("确定退出？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						android.os.Process.killProcess(android.os.Process.myPid());
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				}).create();
			alertbBuilder.show();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	protected boolean isRouteDisplayed() {
		return false;
	}

	public void setOnMapClickListener(boolean flag) {
		if (flag) {
//			map.setOnMapClickListener(this);
			
			bMapView.setOnTouchListener(new OnTouchListener() {
				
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					int x = (int)event.getX();  
			        int y = (int)event.getY();  
			        GeoPoint geoPoint = bMapView.getProjection().fromPixels(x, y);  
			        int xx = geoPoint.getLongitudeE6();  
			        int yy = geoPoint.getLatitudeE6();
			        pointFGps = new PointF(xx, yy);
					listpoint.add(pointFGps);
					geopoints.add(geoPoint);
					markers.add(new MarkerOverlay(bMapView, geoPoint));
					overlayAndtextShow();
//			        Log.d("xxxxxxxxxxx", Integer.toString(xx));  
//			        Log.d("yyyyyyyyyyy", Integer.toString(yy));  
//			        return super.onTouchEvent(arg0);  
					return false;
				}
			});
		}else {
			 bMapView.setOnTouchListener(null);
		}
		
	}
	public void onClick(View v) {
	}

	public void onCurrentLocation(Location locationGps) {

		pGps = new GeoPoint((int) (locationGps.getLatitude() * 1E6),
				(int) (locationGps.getLongitude() * 1E6));
//		pGps = new LatLng((locationGps.getLatitude()),(locationGps.getLongitude()));
//		mapController.animateTo(pGps);
		pointFGps = new PointF(pGps.getLatitudeE6(), pGps.getLongitudeE6());
//		pointFGps = new PointF((float)(pGps.latitude*1E6),(float)(pGps.longitude*1E6));
		listpoint.add(pointFGps);
		geopoints.add(pGps);
		markers.add(new MarkerOverlay(bMapView, pGps));
		bMapView.getController().animateTo(pGps);
		overlayAndtextShow();
	}

	void overlayAndtextShow() {
//		mapView.getOverlays().add(new LineOverlay(geopoints));
//		mapView.getOverlays().add(new MarkerOverlay(geopoints));
//		System.out.println(geopoints);
		if (geopoints.isEmpty()) {
//			polyline.remove();
			if (polyline != null) {
				polyline.delete();
				polyline = null;
			}
			if (!markers.isEmpty()) {
				for (int i = 0; i < markers.size(); i++) {
					markers.get(i).delete();
				}
				markers.remove(markers);
			}
//			polyline.setPoints(geopoints);
			if (Polygon && polygon != null) {
				polygon.delete();
				polygon = null;
//				System.out.println(polygon);
			}
			TextDistance.setText("D:0m");
			TextArea.setText("A:0㎡");
		}else {
			markers.get(markers.size()-1).draw();
			if (polyline != null) {
				polyline.delete();
				polyline = new LineOverlay(geopoints, bMapView);
				polyline.draw();
			}else {
				polyline = new LineOverlay(geopoints, bMapView);
				polyline.draw();
			}
			if (Polygon) {
//				mapView.getOverlays().add(new PolygonOverlay(geopoints));
				if (geopoints.size() > 2) {
					if (polygon != null) {
						polygon.delete();
						polygon = null;
						polygon = new PolygonOverlay(geopoints, bMapView);
						polygon.draw();
					}else {
						polygon = new PolygonOverlay(geopoints, bMapView);
						polygon.draw();
					}
				}else {
					if (polygon != null) {
						polygon.delete();
						polygon = null;
					}
				}
				dStr = Conversion.ConversionA(CaculationArea.calculateArea(listpoint));
				TextArea.setText("A:" + dStr);
			}else {
				TextArea.setText("A:0㎡");
			}	
			dStr = Conversion
					.ConversionD(CaculationDistance.getDistance(listpoint));
			TextDistance.setText("D:" + dStr);
		}

		bMapView.refresh();
	}

	protected void onDestroy() {
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}