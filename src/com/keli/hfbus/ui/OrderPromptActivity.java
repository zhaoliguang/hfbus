package com.keli.hfbus.ui;


import java.util.ArrayList;
import java.util.HashMap;
import com.keli.hfbus.HFBusApp;
import com.keli.hfbus.R;
import com.keli.hfbus.alarm.Alarms;
import com.keli.hfbus.sqlite.BusDal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class OrderPromptActivity extends Activity implements OnClickListener{
	
	public static final String PREFERENCES = "AlarmClock";
	private Cursor mCursor;
	private SharedPreferences mPrefs;
	public int ALARM_COUNT=5;
	ImageButton ibtn_addOrder,ibtn_manageOrder,ibtnTitleReturn;
	 
	TextView remainOrder,orderCount;
	int remainCount,alarmCount;
	BusDal db;
	private HFBusApp mApp;
	//private ArrayList<AlarmOld> alarmList;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.order_prompt);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
		 ibtnTitleReturn=(ImageButton)findViewById(R.id.ibtnTitleReturn);
	        ibtnTitleReturn.setOnClickListener(this);
		mApp = (HFBusApp) getApplication();
		mApp.getmActivityManager().add(this);
		mPrefs = getSharedPreferences(PREFERENCES, 0);
		ibtn_addOrder=(ImageButton)findViewById(R.id.ibtn_addOrder);
		ibtn_manageOrder=(ImageButton)findViewById(R.id.ibtn_manageOrder);
		remainOrder=(TextView)findViewById(R.id.remainOrder);
		orderCount=(TextView)findViewById(R.id.orderCount);
		ibtn_addOrder.setOnClickListener(this);
		ibtn_manageOrder.setOnClickListener(this);
		db=new BusDal(OrderPromptActivity.this);
		HFBusApp busApp = ((HFBusApp)getApplicationContext());
		//alarmList = (ArrayList<AlarmOld>)busApp.getAlarmList();
		//initListItem();
		updateView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		 
		switch(v.getId())
		{
		case R.id.ibtnTitleReturn:
			super.onBackPressed();
			break;
		case R.id.ibtn_addOrder:
			if(alarmCount==5){
				Toast.makeText(OrderPromptActivity.this,"预约个数已满",Toast.LENGTH_SHORT).show();
			}else {
				intent.setClass(OrderPromptActivity.this, SetOrderActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.ibtn_manageOrder:
			if(alarmCount==0){
				Toast.makeText(OrderPromptActivity.this,"尚未设置预约，请添加",Toast.LENGTH_SHORT).show();
			}else {
	//			intent.setClass(OrderPromptActivity.this, ManageOrderActivity.class);
				startActivity(intent);
			}
			break;
			
		}
		updateView();
	}

	public void updateView(){
		mCursor = Alarms.getAlarmsCursor(getContentResolver());
		alarmCount=mCursor.getCount();
		if(alarmCount==5){
			
			//orderCount.setClickable(false);
			orderCount.setText("已设置了5个预约");
			remainOrder.setText("预约数已满");
		}else {
			orderCount.setText("已设置了"+alarmCount+"个预约");
			remainOrder.setText("还可设置"+(ALARM_COUNT-alarmCount)+"个预约");
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateView();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(db!=null)
		{
			db.close();
		}
		super.onDestroy();
	}
	
}
