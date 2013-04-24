package com.test01map;

//import android.app.Activity;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MapFragment extends FragmentActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //printOut wheather google Play Services is available.
        System.out.println(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()));
        //setUp the View
        setContentView(R.layout.mapfragment);
        GoogleMap map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment))
	               .getMap();
        if (map != null) {
			map.setMyLocationEnabled(true);
			map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		}else {
			AlertDialog.Builder mapAlert = new AlertDialog.Builder(this);
			mapAlert
				.setTitle("map exception")
				.setMessage("cann't get the map objection,map is null")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {
								int nPid = android.os.Process.myPid();
								android.os.Process.killProcess(nPid);
							}
						})
				.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int which) {
								dialog.cancel();
							}
						}).create();
			mapAlert.show();
		}
    }
}
