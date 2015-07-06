package com.keli.hfbus;


import com.keli.hfbus.ui.LoginActivity;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WelcomActivity extends Activity {
	/** Called when the activity is first created. */
	private ProgressBar progressbar;
	private TextView tv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcom);
		progressbar = (ProgressBar) findViewById(R.id.progressBar_welcome);
		tv = (TextView)findViewById(R.id.textview);
		tv.setText("Æô¶¯ÖÐ");
	    HFBusApp.mApp.initApp(handler);
		Thread thread = new Thread(r);
		thread.start(); 
		
		
	}
	
	
	Runnable r = new Runnable() {
		Message mes;

		public void run() {
			for (int j = 0; j < 101; j = j + 10) {

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.err.println("welcome ---->sleep  erroe!");
				}
				mes = handler.obtainMessage();
				mes.what = 1;
				mes.arg1 = j;
				mes.sendToTarget();

			}

		}
	};
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			System.out.println(tv.getText().toString());
			progressbar.setProgress(msg.arg1);
			if (msg.arg1 % 20 == 0)
				tv.setText(tv.getText().toString() + ".");
			if (msg.arg1 >= 100) {
				Intent it = new Intent();
				it.setClass(WelcomActivity.this, LoginActivity.class);
				startActivity(it);
				finish();
			}

		}
	};
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}