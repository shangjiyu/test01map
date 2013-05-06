package com.test01map;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import android.app.Application;

public class Location extends Application {

	public LocationClient locationClient = null;
	public BDLocationListener bdLocationListener = null;
	
	public Location() {
		// TODO Auto-generated constructor stub
	}
	
	public class MyLocationListener implements BDLocationListener {

		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			
		}

		public void onReceivePoi(BDLocation poiLocation) {
			// TODO Auto-generated method stub
			
		}

	}
}
