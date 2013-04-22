package com.test01map;

//import android.app.Activity;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MapFragment extends FragmentActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //printOut wheather google Play Services is available.
        System.out.println(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()));
        //setUp the View
        setContentView(R.layout.main);
       System.out.println("woxx");
    }
}
