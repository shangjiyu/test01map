package com.test01map;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
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

	GeoPoint p, pGps;
	LineOverlay polyline = null;
	PolygonOverlay polygon = null;
	
	List<PointF> listpoint = new ArrayList<PointF>();
	List<GeoPoint> geopoints = new ArrayList<GeoPoint>();
	List<Location> locations = new ArrayList<Location>();
	List<MarkerOverlay> markers = new ArrayList<MarkerOverlay>();
	
	boolean addpoint = false;
	BMapManager bMapManager;
	MapView bMapView;
	MapController bMapController;
	MKOfflineMap bOfflineMap = null;
	GraphicsOverlay bGraphicsOverlay;
	LocationClient bLocationClient;
	LocationClientOption bLocationClientOption = new LocationClientOption();
	TestLocationListener testLocationListener = new TestLocationListener();
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
//		bMapManager.init("31EB3BBC63C599FB0D5C6F9E2E2BE3FFE4E726FB", null);
		bMapManager.init("3164354232f7313b61bd70dfc5b58145", null);
		setContentView(R.layout.main);
		
		TextDistance = (TextView)findViewById(R.id.Distance);
		TextArea = (TextView)findViewById(R.id.Area);
		
		View customNav = LayoutInflater.from(this).inflate(R.layout.custom_abs_view, null);
		((ImageView) customNav.findViewById(R.id.menu_share)).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				System.out.println("share action");
				Intent shareIntent = new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.putExtra(Intent.EXTRA_TEXT, "面积有："+TextArea.getText()+"距离有："+TextDistance.getText());
				shareIntent.setType("text/plain");
				startActivity(Intent.createChooser(shareIntent, "分享到"));
			}
		});
		/*ShareActionProvider mShareActionProvider = (ShareActionProvider)((MenuItem) customNav.findViewById(R.id.menu_share)).getActionProvider();
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_TEXT, "面积有："+TextArea.getText()+"距离有："+TextDistance.getText());
		shareIntent.setType("text/plain");
		mShareActionProvider.setShareIntent(shareIntent);*/
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setCustomView(customNav);
		actionBar.setDisplayShowCustomEnabled(true);
//		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_CUSTOM);
		
		bMapView = (MapView) findViewById(R.id.bmapsView);
		bMapView.setBuiltInZoomControls(true);
		bMapView.setSatellite(false);
//		设置启用内置的缩放控件
		bMapController = bMapView.getController();
//		取得地图控制权
		bMapController.setZoom(15);
//		bMapController.animateTo(new GeoPoint(120047134,30231811));
		bGraphicsOverlay = new GraphicsOverlay(bMapView);
		
//		offline Map
		bOfflineMap = new MKOfflineMap();
		bOfflineMap.init(bMapController, new MKOfflineMapListener() {
			
			public void onGetOfflineMapState(int type, int state) {
				AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(Main.this);
				// TODO Auto-generated method stub
				switch (type) {
				case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
					{
						// Log.d("OfflineDemo", String.format("cityid:%d update", state));
						MKOLUpdateElement update = bOfflineMap.getUpdateInfo(state);
						if ( update != null ){
							alertbBuilder.setTitle("地图离线包更新")
							.setMessage(update.cityID+","+update.ratio)
							.setPositiveButton("更新", new DialogInterface.OnClickListener() {
								
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
	//								android.os.Process.killProcess(android.os.Process.myPid());
								}
							})
							.setNegativeButton("取消", new DialogInterface.OnClickListener() {
								
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							}).create();
						alertbBuilder.show();
						}
	//					    mText.setText(String.format("%s : %d%%", update.cityName, update.ratio));
					}
					break;
					case MKOfflineMap.TYPE_NEW_OFFLINE:
					{
						alertbBuilder.setTitle("地图更新")
						.setMessage("新安装离线地图包"+state+"个")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
//								android.os.Process.killProcess(android.os.Process.myPid());
							}
						})
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						}).create();
					alertbBuilder.show();
					}
					case MKOfflineMap.TYPE_VER_UPDATE:
					{
						MKOLUpdateElement element = bOfflineMap.getUpdateInfo(state);
						if (element != null) {
							alertbBuilder.setTitle("离线地图更新")
							.setMessage(element.cityName+"有新的离线地图版本")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
//									android.os.Process.killProcess(android.os.Process.myPid());
								}
							})
							.setNegativeButton("取消", new DialogInterface.OnClickListener() {
								
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							}).create();
							alertbBuilder.show();
						}
					}
					break;
				}
			}
		});
		int number = bOfflineMap.scan();
		if (number != 0) {
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Main.this);
			alertBuilder.setTitle("离线包导入")
			.setMessage("已导入"+number+"个城市")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
//									android.os.Process.killProcess(android.os.Process.myPid());
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			}).create();
			alertBuilder.show();
		}
		
		bLocationClientOption.setOpenGps(true);
		bLocationClientOption.setCoorType("bd09ll");
		bLocationClientOption.setProdName("com.test01map");
		bLocationClientOption.setScanSpan(5000);
		bLocationClientOption.setPriority(LocationClientOption.GpsFirst);
		bLocationClientOption.disableCache(true);
		bLocationClient = new LocationClient(getApplicationContext());
		bLocationClient.setLocOption(bLocationClientOption);
		bLocationClient.registerLocationListener(testLocationListener);
		bLocationClient.start();
//		bLocationClient.requestLocation();
	}

	public boolean onCreateOptionsMenu(Menu menu) {

		
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
//		ShareActionProvider shareActionProvider = (ShareActionProvider) menu.findItem(R.id.menu_share).getActionProvider();
//		shareActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
//		Intent shareIntent = new Intent();
//		shareIntent.setAction(Intent.ACTION_SEND);
//		shareIntent.setType("image/*");
////        Uri uri = Uri.fromFile(getFileStreamPath("shared.png"));
////        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//		shareActionProvider.setShareIntent(shareIntent);
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
		mapTypeToggleButton.setChecked(false);
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
							polygon = new PolygonOverlay(geopoints, bMapView, bGraphicsOverlay);
							polygon.draw();
						}
					}
					dStr = Conversion.ConversionA(CaculationArea.calculateArea(listpoint));
//					dStr = Conversion.ConversionA(CaculationArea.calculateArea(areaPointFs));
					TextArea.setText("A:" + dStr);
				}
				overlayAndtextShow();
//				bMapView.refresh();
			}
		});
		areaToggleButton.setChecked(false);
		manulyToggleButton = (ToggleButton) menu.findItem(R.id.manulyToggleButton).getActionView();
		manulyToggleButton.setTextOff("手动");
		manulyToggleButton.setTextOn("定位");
		manulyToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (!isChecked) {
					bLocationClient.stop();
					setOnMapClickListener(true);
				} else {
					bLocationClient.start();
					setOnMapClickListener(false);
				}
				overlayAndtextShow();
			}
		});
		manulyToggleButton.setChecked(true);
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
//		System.out.println("menu created");
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
//		case R.id.redoButton:
//		{
//			redoButton = (Button) item.getActionView();
//			redoButton.setText(R.string.redoButton);
//			redoButton.setOnClickListener(new OnClickListener() {
//				
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (!geopoints.isEmpty()) {
//						listpoint.remove(listpoint.size() - 1);
//						geopoints.remove(geopoints.size() - 1);
//						markers.get(markers.size()-1).delete();
//						markers.remove(markers.size()-1);
//						overlayAndtextShow();
//					} else {
//						Toast.makeText(MyApplication.getInstance(), "已经删完...",
//								Toast.LENGTH_SHORT).show();
//					}
//				}
//			});
//		}
//		break;
//		case R.id.clearButton:
//		{
//			cleanButton = (Button) item.getActionView();
//			cleanButton.setText(R.string.clearButton);
//			cleanButton.setOnClickListener(new OnClickListener() {
//				
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (!geopoints.isEmpty()) {
//						listpoint.removeAll(listpoint);
//						geopoints.removeAll(geopoints);
//						for (int i = 0; i < markers.size(); i++) {
//							markers.get(i).delete();
//						}
//						markers.removeAll(markers);
//						overlayAndtextShow();
//					} else {
//						Toast.makeText(MyApplication.getInstance(), "已经删空...",
//								Toast.LENGTH_SHORT).show();
//					}
//				}
//			});
//		}
//		break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("resumed");
	}

	protected boolean isRouteDisplayed() {
		return false;
	}

	public void setOnMapClickListener(boolean flag) {
		if (flag) {
//			map.setOnMapClickListener(this);
			bMapView.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});			
			bMapView.setOnTouchListener(new OnTouchListener() {
				
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
					{
						int x = (int)event.getX();  
				        int y = (int)event.getY();  
				        GeoPoint geoPoint = bMapView.getProjection().fromPixels(x, y);
				        System.out.println(geoPoint);
				        int xx = geoPoint.getLongitudeE6();  
				        int yy = geoPoint.getLatitudeE6();
//				        pointFGps = new PointF(xx, yy);
				        pointFGps = new PointF(yy, xx);
						listpoint.add(pointFGps);
						geopoints.add(geoPoint);
						markers.add(new MarkerOverlay(bMapView, bGraphicsOverlay, geoPoint));
						overlayAndtextShow();
				        Log.d("xxxxxxxxxxx", Integer.toString(xx));  
				        Log.d("yyyyyyyyyyy", Integer.toString(yy));  
//				        return super.onTouchEvent(arg0);  
//						return true;
					}	
					break;
//					case MotionEvent.ACTION_MOVE:
//						return false;
					}
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
		markers.add(new MarkerOverlay(bMapView,bGraphicsOverlay, pGps));
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
				System.out.println("-polyline");
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
				System.out.println("-polygon");
				polygon = null;
//				System.out.println(polygon);
			}
			TextDistance.setText("D:0m");
			TextArea.setText("A:0㎡");
		}else {
			markers.get(markers.size()-1).draw();
			System.out.println("+marker");
			if (polyline != null) {
				polyline.delete();
				System.out.println("-polyline");
				polyline = new LineOverlay(geopoints, bMapView, bGraphicsOverlay);
				polyline.draw();
				System.out.println("+polyline");
			}else {
				polyline = new LineOverlay(geopoints, bMapView, bGraphicsOverlay);
				polyline.draw();
				
			}
			if (Polygon) {
//				mapView.getOverlays().add(new PolygonOverlay(geopoints));
				if (geopoints.size() > 2) {
					if (polygon != null) {
						polygon.delete();
						System.out.println("-polygon");
						polygon = null;
						polygon = new PolygonOverlay(geopoints, bMapView, bGraphicsOverlay);
						polygon.draw();
						System.out.println("+polygon");
					}else {
						polygon = new PolygonOverlay(geopoints, bMapView, bGraphicsOverlay);
						polygon.draw();
						System.out.println("+polygon");
					}
				}else {
					if (polygon != null) {
						polygon.delete();
						System.out.println("-polygon");
						polygon = null;
					}
				}
				dStr = Conversion.ConversionA(CaculationArea.calculateArea(listpoint));
				TextArea.setText("A:" + dStr);
			}else {
				TextArea.setText("A:0㎡");
			}
			if (Polygon) {
				List<PointF> areaPointFs = new ArrayList<PointF>();
				areaPointFs.addAll(listpoint);
				if (listpoint.size() != 0) {
					areaPointFs.add(listpoint.get(0));
				}
				dStr = Conversion
						.ConversionD(CaculationDistance.getDistance(areaPointFs));
			}else {
				dStr = Conversion
						.ConversionD(CaculationDistance.getDistance(listpoint));
			}
			TextDistance.setText("D:" + dStr);
		}

		bMapView.refresh();
	}

	protected void onDestroy() {
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	public class TestLocationListener implements BDLocationListener {

		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			System.out.println(location.getLocType());
//			if (location == null) {
//				return;
//			}
			pGps = new GeoPoint((int) (location.getLatitude() * 1E6),
					(int) (location.getLongitude() * 1E6));
//			pGps = new LatLng((locationGps.getLatitude()),(locationGps.getLongitude()));
//			mapController.animateTo(pGps);
			pointFGps = new PointF(pGps.getLatitudeE6(), pGps.getLongitudeE6());
//			pointFGps = new PointF((float)(pGps.latitude*1E6),(float)(pGps.longitude*1E6));
			listpoint.add(pointFGps);
			geopoints.add(pGps);
			markers.add(new MarkerOverlay(bMapView,bGraphicsOverlay, pGps));
			bMapView.getController().animateTo(pGps);
			overlayAndtextShow();
		}

		public void onReceivePoi(BDLocation poiLocation) {
			// TODO Auto-generated method stub
			
		}
		
		
	}	
}