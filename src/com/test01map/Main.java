package com.test01map;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
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

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.test01map.MyLocationManager.LocationCallBack;
import com.test01map.caculation.CaculationArea;
import com.test01map.caculation.CaculationDistance;
import com.test01map.overlay.LineOverlay;
import com.test01map.overlay.MarkerOverlay;
import com.test01map.overlay.PolygonOverlay;
import com.test01map.tools.Conversion;

public class Main extends MapActivity implements LocationCallBack,
		OnClickListener {

	private MyLocationManager mylocation;
	GeoPoint p, pGps;
	List<Overlay> listOfOverlays = new ArrayList<Overlay>();
	List<PointF> listpoint = new ArrayList<PointF>();
	List<GeoPoint> geopoints = new ArrayList<GeoPoint>();
	List<Location> locations = new ArrayList<Location>();
	MapController mapController = null;
	boolean addpoint = false;

	MapView mapView;
	MapOverlay mapOverlay;
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

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setSatellite(false);
		mapView.setTraffic(true);
		mapView.setBuiltInZoomControls(true);
		mapView.setClickable(true);
		mapView.setEnabled(true);

		mapController = mapView.getController();
		mapController.setZoom(3);

		mapOverlay = new MapOverlay();

		listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		listOfOverlays.add(mapOverlay);

		MyLocationManager.init(Main.this.getApplicationContext(), Main.this);
		mylocation = MyLocationManager.getInstance();

		mapView.invalidate();
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

									int nPid = android.os.Process.myPid();

									android.os.Process.killProcess(nPid);
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

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	class MapOverlay extends Overlay {
		public boolean onTap(GeoPoint p, MapView mapView) {

			pointF = new PointF(p.getLatitudeE6(), p.getLongitudeE6());
			listpoint.add(pointF);
			geopoints.add(p);
			removeOverlay();
			overlayAndtextShow();
			addpoint = false;
			mapController.animateTo(p);
			return false;

		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.delete:
			if (!listpoint.isEmpty()) {
				listpoint.remove(listpoint.size() - 1);
				geopoints.remove(geopoints.size() - 1);
				removeOverlay();
				overlayAndtextShow();
			} else {
				Toast.makeText(MyApplication.getInstance(), "no point",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.clear:
			if (!listpoint.isEmpty()) {
				listpoint.clear();
				geopoints.clear();
				removeOverlay();
				overlayAndtextShow();
			} else {
				Toast.makeText(MyApplication.getInstance(), "no point",
						Toast.LENGTH_SHORT).show();
			}
			break;

		}
	}

	public void onCurrentLocation(Location locationGps) {

		mapController.setZoom(18);
		pGps = new GeoPoint((int) (locationGps.getLatitude() * 1E6),
				(int) (locationGps.getLongitude() * 1E6));
		mapController.animateTo(pGps);
		pointFGps = new PointF(pGps.getLatitudeE6(), pGps.getLongitudeE6());
		listpoint.add(pointFGps);
		geopoints.add(pGps);
		removeOverlay();
		overlayAndtextShow();

	}

	void removeOverlay() {
		if (4 == mapView.getOverlays().size()) {
			mapView.getOverlays().remove(3);
			mapView.getOverlays().remove(2);
			mapView.getOverlays().remove(1);
		} else if (3 == mapView.getOverlays().size()) {
			mapView.getOverlays().remove(2);
			mapView.getOverlays().remove(1);
		}
	}

	void overlayAndtextShow() {
		mapView.getOverlays().add(new LineOverlay(geopoints));
		mapView.getOverlays().add(new MarkerOverlay(geopoints));
		if (Polygon) {
			mapView.getOverlays().add(new PolygonOverlay(geopoints));
			dStr = Conversion.ConversionA(CaculationArea.calculateArea(listpoint));
			TextArea.setText("A:" + dStr);
		}	
		dStr = Conversion
				.ConversionD(CaculationDistance.getDistance(listpoint));
		TextDistance.setText("D:" + dStr);
		mapView.invalidate();
	}

	protected void onDestroy() {
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	class toggleButton0Listener implements OnCheckedChangeListener {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				mapView.setSatellite(false);
				mapView.setTraffic(true);
			} else {
				mapView.setSatellite(true);
				mapView.setTraffic(false);

			}

		}
	}

	class toggleButton1Listener implements OnCheckedChangeListener {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				mylocation.destoryLocationManager();
			} else {
				mylocation.addLocationManager();

			}

		}
	}
	
	class toggleButton2Listener implements OnCheckedChangeListener {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				Polygon = false;
				removeOverlay();
				overlayAndtextShow();
				TextArea.setText("A:0sq.m");
			} else {
				Polygon = true;
				mapView.getOverlays().add(new PolygonOverlay(geopoints));
				dStr = Conversion.ConversionA(CaculationArea.calculateArea(listpoint));
				TextArea.setText("A:" + dStr);
			}

		}
	}
}
