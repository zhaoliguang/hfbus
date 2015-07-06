package com.keli.hfbus.ui;

import java.io.File;
import java.util.Map;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.keli.hfbus.HFBusApp;
import com.keli.hfbus.R;
public class PopMapActivity extends MapActivity implements 
OnClickListener{
	
	private MapView mMapView;
	private MapController mMapController;
	private GeoPoint point;
	private LayoutInflater inflater;
	 HFBusApp mApp;
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mMapView= null;
		mMapController=null;

	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     // 获取屏幕密度  
        int screenWidth  = getWindowManager().getDefaultDisplay().getWidth(); 
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight(); 
        
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        wl.alpha=0.95f;//设置透明度,0.0为完全透明，1.0为完全不透明
        window.setAttributes(wl);
        mApp = (HFBusApp) getApplication();
        mApp.getmActivityManager().add(this);
        inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.map_view, null);
     // LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(screenWidth/4 * 3, screenHeight/2);
        LinearLayout llMapView=(LinearLayout)layout.findViewById(R.id.llMapLayout);
     //   llMapView.setLayoutParams(lp);
        setContentView(layout);
		mMapView = (MapView) findViewById(R.id.popMapView);
		mMapView.setBuiltInZoomControls(true); // 设置启用内置的缩放控件
		// 得到 mMapView 的控制权,可以用它控制和驱劢平移和缩放
		mMapController = mMapView.getController();
		

		// 用给定的经纬度构造一个 GeoPoint，单位是微度 (度 * 1E6)
		point = new GeoPoint((int) (31.864856 * 1E6), (int) (117.287316 * 1E6));
		mMapController.setCenter(point); // 设置地图中心点
		mMapController.setZoom(17); // 设置地图 zoom 级别
        mMapView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stu
				return false;
			}
        	
        });

    }

    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		System.out.println("==========Main:onPause");
		super.onPause();
	}
 
   
    @Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);

		super.onAttachedToWindow();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}