package com.test01map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

//import com.google.android.maps.GeoPoint;
//import com.google.android.maps.MapController;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.af.c;
import com.google.android.gms.internal.co;
import com.google.android.gms.internal.l;
//import com.google.android.maps.MapView;
import com.google.android.gms.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.plus.GooglePlusUtil;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;

import com.test01map.MyLocationManager.LocationCallBack;
import com.test01map.caculation.CaculationArea;
import com.test01map.caculation.CaculationDistance;
//import com.test01map.overlay.LineOverlay;
//import com.test01map.overlay.MarkerOverlay;
//import com.test01map.overlay.PolygonOverlay;
import com.test01map.tools.Conversion;

public class Main extends FragmentActivity implements LocationCallBack,
		OnClickListener, OnMapClickListener {

	private MyLocationManager mylocation;
//	GeoPoint p, pGps;
	LatLng p,pGps;
	Polyline polyline;
	Polygon polygon;
	List<Overlay> listOfOverlays = new ArrayList<Overlay>();
	List<PointF> listpoint = new ArrayList<PointF>();
//	List<GeoPoint> geopoints = new ArrayList<GeoPoint>();
	List<LatLng> geopoints = new ArrayList<LatLng>();
	List<Location> locations = new ArrayList<Location>();
	List<Marker> markers = new ArrayList<Marker>();
//	MapController mapController = null;
	boolean addpoint = false;
	GoogleMap map;
//	MapView mapView;
//	GoogleMap mapView
//	MapOverlay mapOverlay;
	PointF pointF, pointFGps;

	ImageButton delete;
	ImageButton clear;
	ToggleButton toggleButton0;
	ToggleButton toggleButton1;
	ToggleButton toggleButton2;
	
	TextView TextDistance;
	TextView TextArea;

	String dStr;

	Boolean Polygon = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		delete = (ImageButton) findViewById(R.id.delete);
		clear = (ImageButton) findViewById(R.id.clear);
		toggleButton0 = (ToggleButton) findViewById(R.id.toggleButton0);
		toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
		toggleButton2 = (ToggleButton) findViewById(R.id.toggleButton2);
		
		delete.setOnClickListener(this);
		clear.setOnClickListener(this);
		toggleButton0.setOnCheckedChangeListener(new toggleButton0Listener());
		toggleButton1.setOnCheckedChangeListener(new toggleButton1Listener());
		toggleButton2.setOnCheckedChangeListener(new toggleButton2Listener());
		

		TextDistance = (TextView) findViewById(R.id.Distance);
		TextArea = (TextView) findViewById(R.id.Area);
		
		System.out.println(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()));
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview))
	            .getMap();
//		GoogleMap map = (GoogleMap) mapView.getMap();
//		System.out.println(map);
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);
		polyline = map.addPolyline(new PolylineOptions().width(5));
//		polygon = map.addPolygon(new PolygonOptions());
		map.moveCamera(CameraUpdateFactory.zoomTo(3));
		/*mapController = mapView.getController();
		mapController.setZoom(3);*/
//		mapOverlay = new MapOverlay();
//		listOfOverlays = mapView.getOverlays();
//		listOfOverlays.clear();
//		listOfOverlays.add(mapOverlay);

		MyLocationManager.init(Main.this.getApplicationContext(), Main.this);
		mylocation = MyLocationManager.getInstance();

//		mapView.invalidate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);
		menu.add(1, 1, 3, "exit");
		menu.add(1, 2, 3, "set");

		return true;
	}

	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		switch (item.getItemId()) {
		case 1:
			AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(this);
			alertbBuilder
					.setTitle("Exit")
					.setMessage("Are you sure?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {

//									int nPid = android.os.Process.myPid();

									android.os.Process.killProcess(android.os.Process.myPid());
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();

								}
							}).create();
			alertbBuilder.show();
			break;
			
		case 2:
			
			Intent intent = new Intent(this, StartService.class);
			startActivity(intent);

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

/*	class MapOverlay extends Overlay {
		public boolean onTap(GeoPoint p, MapView mapView) {

			pointF = new PointF(p.getLatitudeE6(), p.getLongitudeE6());
			listpoint.add(pointF);
			geopoints.add(p);
//			removeOverlay();
			overlayAndtextShow();
			addpoint = false;
//			mapController.animateTo(p);
			return false;

		}
	}*/
	public void setOnMapClickListener(boolean flag) {
		if (flag) {
			map.setOnMapClickListener(this);
		}else {
			map.setOnMapClickListener(null);
		}
		
	}
	public void onMapClick (LatLng latLng) {
		geopoints.add(latLng);
		pointFGps = new PointF((float)(latLng.latitude*1E6),(float)(latLng.longitude*1E6));
		listpoint.add(pointFGps);
		markers.add(map.addMarker(new MarkerOptions().position(latLng)));
//		map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//		map.animateCamera(CameraUpdateFactory.zoomTo(17));
		overlayAndtextShow();
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.delete:
			if (!geopoints.isEmpty()) {
				listpoint.remove(listpoint.size() - 1);
				geopoints.remove(geopoints.size() - 1);
				polyline.setPoints(geopoints);
//				polygon.setPoints(geopoints);
				markers.get(markers.size()-1).remove();
				markers.remove(markers.size()-1);
//				removeOverlay();
				overlayAndtextShow();
			} else {
				Toast.makeText(MyApplication.getInstance(), "no point",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.clear:
			if (!geopoints.isEmpty()) {
				listpoint.removeAll(listpoint);
				geopoints.removeAll(geopoints);
				polyline.remove();
				if (polygon != null) {
					polygon.remove();
				}
				for (int i = 0; i < markers.size(); i++) {
					markers.get(i).remove();
				}
				markers.removeAll(markers);
//				removeOverlay();
//				map.clear();
				polyline = map.addPolyline(new PolylineOptions().width(5));
//				polygon = map.addPolygon(new PolygonOptions().strokeWidth(5));
//				overlayAndtextShow();
			} else {
				Toast.makeText(MyApplication.getInstance(), "no point",
						Toast.LENGTH_SHORT).show();
			}
			break;

		}
	}

	public void onCurrentLocation(Location locationGps) {

		/*pGps = new GeoPoint((int) (locationGps.getLatitude() * 1E6),
				(int) (locationGps.getLongitude() * 1E6));*/
		pGps = new LatLng((locationGps.getLatitude()),(locationGps.getLongitude()));
//		mapController.animateTo(pGps);
//		pointFGps = new PointF(pGps.getLatitudeE6(), pGps.getLongitudeE6());
		pointFGps = new PointF((float)(pGps.latitude*1E6),(float)(pGps.longitude*1E6));
		listpoint.add(pointFGps);
		geopoints.add(pGps);
		markers.add(map.addMarker(new MarkerOptions().position(pGps).title("marker")));
		map.moveCamera(CameraUpdateFactory.newLatLng(pGps));
		overlayAndtextShow();
	}

	void overlayAndtextShow() {
//		mapView.getOverlays().add(new LineOverlay(geopoints));
//		mapView.getOverlays().add(new MarkerOverlay(geopoints));
//		System.out.println(geopoints);
		polyline.setPoints(geopoints);
		polyline.setVisible(true);
//		System.out.println(polyline.getPoints());
		if (Polygon) {
//			mapView.getOverlays().add(new PolygonOverlay(geopoints));
			if (!geopoints.isEmpty()) {
				polygon.setPoints(geopoints);
				polygon.setVisible(true);
			}
			dStr = Conversion.ConversionA(CaculationArea.calculateArea(listpoint));
			TextArea.setText("A:" + dStr);
		}	
		dStr = Conversion
				.ConversionD(CaculationDistance.getDistance(listpoint));
		TextDistance.setText("D:" + dStr);
//		mapView.invalidate();
	}

	protected void onDestroy() {
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	class toggleButton0Listener implements OnCheckedChangeListener {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//			GoogleMap map = (GoogleMap)mapView.getMap();
			if (isChecked) {
//				mapView.setSatellite(false);
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//				mapView.setTraffic(true);
			} else {
//				mapView.setSatellite(true);
//				mapView.setTraffic(false);
				map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

			}

		}
	}

	class toggleButton1Listener implements OnCheckedChangeListener {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				mylocation.destoryLocationManager();
				setOnMapClickListener(true);
			} else {
				mylocation.addLocationManager();
				setOnMapClickListener(false);
			}

		}
	}
	
	class toggleButton2Listener implements OnCheckedChangeListener {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				Polygon = false;
//				removeOverlay();
//				map.clear();
				geopoints.removeAll(geopoints);
				listpoint.removeAll(listpoint);
				polyline.remove();
				polygon.remove();
				for (int i = 0; i < markers.size(); i++) {
					markers.get(i).remove();
				}
				markers.removeAll(markers);
//				overlayAndtextShow();
				polyline = map.addPolyline(new PolylineOptions().width(5));
//				polygon = map.addPolygon(new PolygonOptions().strokeWidth(5));
				TextArea.setText("A:0sq.m");
				TextDistance.setText("D:0m");
			} else {
				Polygon = true;
//				mapView.getOverlays().add(new PolygonOverlay(geopoints));
				polygon = map.addPolygon(new PolygonOptions().addAll(geopoints).strokeWidth(5));
				dStr = Conversion.ConversionA(CaculationArea.calculateArea(listpoint));
//				dStr = Conversion.ConversionA(CaculationArea.calculateArea(geopoints));
				TextArea.setText("A:" + dStr);
			}

		}
	}
}
