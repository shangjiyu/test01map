package com.test01map;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.slidingmenu.lib.SlidingMenu;
import com.test01map.caculation.CaculationArea;
import com.test01map.caculation.CaculationDistance;
import com.test01map.overlay.LineOverlay;
import com.test01map.overlay.MarkerOverlay;
import com.test01map.overlay.PolygonOverlay;
import com.test01map.tools.Conversion;

/**
 * @ClassName: Main
 * @Description: TODO(展示一张地图，并跟踪设备位置实时的添加到地图上)
 * @author jiyu
 * @date 2013-8-12 下午9:04:10
 *
 */
public class Main extends SherlockActivity {

	GeoPoint p, pGps;
	Drawable marker;
	LineOverlay polyline = null;
	PolygonOverlay polygon = null;
	MarkerOverlay markers = null;
	
	List<PointF> listpoint = new ArrayList<PointF>();
	List<GeoPoint> geopoints = new ArrayList<GeoPoint>();
	List<Location> locations = new ArrayList<Location>();
	
	BMapManager bMapManager;
	MapView bMapView;
	MapController bMapController;
	MKOfflineMap bOfflineMap = null;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		bMapManager = new BMapManager(getApplication());
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
				shareIntent.putExtra(Intent.EXTRA_TEXT, "面积有："+TextArea.getText()+"\r\n距离有："+TextDistance.getText());
				shareIntent.setType("text/plain");
				startActivity(Intent.createChooser(shareIntent, "分享到"));
			}
		});
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setCustomView(customNav);
		actionBar.setDisplayShowCustomEnabled(true);
//		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_CUSTOM);
		
//		slidingmenu
		SlidingMenu sm = new SlidingMenu(this.getApplicationContext());
		sm.setMode(SlidingMenu.LEFT);
		sm.setFadeDegree(0.35f); //颜色渐变比例
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); //拉动事件区域  --全屏
		
		bMapView = (MapView) findViewById(R.id.bmapsView);
		bMapView.setBuiltInZoomControls(true);
		bMapView.setSatellite(false);
//		初始化 overlay	
		this.polyline = new LineOverlay(bMapView);
		this.polygon = new PolygonOverlay(bMapView);
		this.marker = getResources().getDrawable(R.drawable.icon_marka);
		this.markers = new MarkerOverlay(bMapView, getApplicationContext(), marker);
//		设置启用内置的缩放控件
		bMapController = bMapView.getController();
//		取得地图控制权
		bMapController.setZoom(15);
		
//		offLine Map Initialize
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
						if (state != 0) {
							alertbBuilder.setTitle("地图更新")
							.setMessage("新安装离线地图包"+state+"个")
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
		bLocationClient.setAK("064161ed83119b3e1d1700678e3a6e3f");
		bLocationClient.setLocOption(bLocationClientOption);
		bLocationClient.registerLocationListener(testLocationListener);
		bLocationClient.start();
//		bLocationClient.requestLocation();
	}

	public boolean onCreateOptionsMenu(Menu menu) {

		
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		mapTypeToggleButton = (ToggleButton) menu.findItem(R.id.mapTypeToggleButton).getActionView();
		mapTypeToggleButton.setTextOn("卫星");
		mapTypeToggleButton.setTextOff("普通");
		mapTypeToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (!isChecked) {
					bMapView.setSatellite(false);
				} else {
					bMapView.setSatellite(true);
				}
				overlayAndtextShow();
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
				} else {
//					buttonView.setText("计算面积");
					Polygon = true;
				}
				overlayAndtextShow();
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
//					markers.remove(markers.size()-1);
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
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * @Title: setOnMapClickListener
	 * @Description: TODO(设置手动去点还是GPS定位，有aciton bar的button调用)
	 * @param @param flag    设定文件
	 * @return void    返回类型
	 * @throws
	 */
	public void setOnMapClickListener(boolean flag) {
		if (flag) {
			MKMapTouchListener mapTouchListener = new MKMapTouchListener() {
				
				@Override
				public void onMapLongClick(GeoPoint arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onMapDoubleClick(GeoPoint arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onMapClick(GeoPoint geoPoint) {
					// TODO Auto-generated method stub
//					System.out.println(geoPoint);
			        int xx = geoPoint.getLongitudeE6();  
			        int yy = geoPoint.getLatitudeE6();
			        pointFGps = new PointF(yy, xx);
					listpoint.add(pointFGps);
					geopoints.add(geoPoint);
//					markers.add(new MarkerOverlay(bMapView, bGraphicsOverlay, geoPoint));
					overlayAndtextShow();
			        Log.d("xxxxxxxxxxx", Integer.toString(xx));  
			        Log.d("yyyyyyyyyyy", Integer.toString(yy)); 
				}
			};
			bMapView.regMapTouchListner(mapTouchListener);
		}else {
			 bMapView.setOnTouchListener(null);
		}
		
	}

	/**
	 * @Title: overlayAndtextShow
	 * @Description: TODO(update the map overlays)
	 * @param     设定文件
	 * @return void    返回类型
	 * @throws
	 */
	public void overlayAndtextShow() {
		if (geopoints.isEmpty()) {
			if (polyline != null) {
				polyline.delete();
//				System.out.println("delete polyline");
			}
			if (polygon != null) {
				polygon.delete();
//				System.out.println("delete polygon");
			}
			if (markers != null) {
				markers.delete();
			}
			TextDistance.setText("D:0m");
			TextArea.setText("A:0㎡");
		}else {
			if (markers != null) {
				markers.draw(geopoints);
//				System.out.println("update marker");
			}else {
				markers = new MarkerOverlay(bMapView, getApplicationContext(), marker);
				markers.draw(geopoints);
//				System.out.println("update marker");
			}
			if (polyline != null) {
				polyline.draw(geopoints);
//				System.out.println("update polyLine");
			}else {
				polyline = new LineOverlay(bMapView);
				polyline.draw(geopoints);
//				System.out.println("update polyLine");
			}
			if (Polygon) {
				if (geopoints.size() > 2) {
					if (polygon != null) {
						polygon.draw(geopoints);
//						System.out.println("update polygon");
					}else {
						polygon = new PolygonOverlay(bMapView);
						polygon.draw(geopoints);
//						System.out.println("update polygon");
					}
				}else {
					if (polygon != null) {
						polygon.delete();
//						System.out.println("delete polygon");
					}
				}
				dStr = Conversion.ConversionA(CaculationArea.calculateArea(listpoint));
				TextArea.setText("A:" + dStr);
			}else {
				TextArea.setText("A:0㎡");
				if (polygon != null) {
					polygon.delete();
				}
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
	
	@Override
	protected void onDestroy() {
		bMapView.destroy();
		if (bMapManager != null) {
			bMapManager.destroy();
			bMapManager = null;
		}
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	@Override
	protected void onPause(){
		bMapView.onPause();
		if (bMapManager!=null) {
			bMapManager.stop();
		}
		super.onPause();
	}
	
	@Override
	protected void onResume(){
		bMapView.onResume();
		if (bMapManager!=null) {
			bMapManager.start();
		}
		super.onResume();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		switch (this.getResources().getConfiguration().orientation) {
		case Configuration.ORIENTATION_LANDSCAPE:
			getSupportActionBar().getCustomView().findViewById(R.id.menu_share).setVisibility(View.GONE);
			break;
		case Configuration.ORIENTATION_PORTRAIT:
			getSupportActionBar().getCustomView().findViewById(R.id.menu_share).setVisibility(View.VISIBLE);
			break;
		case Configuration.ORIENTATION_UNDEFINED:break;
		default:
			break;
		}
	}
	/**
	 * @ClassName: TestLocationListener
	 * @Description: TODO(百度定位sdk的定位结果事件监听类)
	 * @author jiyu
	 * @date 2013-9-29 下午7:55:58
	 *
	 */
	public class TestLocationListener implements BDLocationListener {

		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
//			System.out.println(location.getLocType());
			if (location == null) {
				return;
			}
			pGps = new GeoPoint((int) (location.getLatitude() * 1E6),
					(int) (location.getLongitude() * 1E6));
			pointFGps = new PointF(pGps.getLatitudeE6(), pGps.getLongitudeE6());
			listpoint.add(pointFGps);
			geopoints.add(pGps);
			bMapView.getController().animateTo(pGps);
			overlayAndtextShow();
		}

		public void onReceivePoi(BDLocation poiLocation) {
			// TODO Auto-generated method stub
			
		}
		
	}	
}