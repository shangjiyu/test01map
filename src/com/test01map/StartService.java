package com.test01map;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartService extends Activity implements OnClickListener {

//	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.start_service);
//		button = (Button) findViewById(R.id.button1);
//		button.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.button1:
			
//			if(button.getText().equals(getResources().getString(R.string.start))){
//	 			stopService(new Intent("com.smstest.MyService"));
//	 			Intent service = new Intent("com.smstest.MyService");
//	 			startService(service);
//	 			button.setText(R.string.stop);
// 			}else{
// 				this.stopService(new Intent("com.smstest.MyService"));
// 				button.setText(R.string.start);
// 			}
//			finish();

//			break;

		default:
			break;
		}

	}

}
