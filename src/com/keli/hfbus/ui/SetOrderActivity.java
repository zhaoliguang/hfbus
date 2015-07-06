package com.keli.hfbus.ui;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.keli.hfbus.HFBusApp;
import com.keli.hfbus.R;
import com.keli.hfbus.adapt.AlarmDateAdapter;
import com.keli.hfbus.adapt.StationDistanceAdapter;
import com.keli.hfbus.alarm.Alarm;
import com.keli.hfbus.alarm.AlarmPreference;
import com.keli.hfbus.alarm.Alarms;
import com.keli.hfbus.alarm.DeskClockMainActivity;
import com.keli.hfbus.alarm.RepeatPreference;
import com.keli.hfbus.alarm.ToastMaster;
import com.keli.hfbus.hessianserver.GjcxService;
import com.keli.hfbus.hessianserver.Request;
import com.keli.hfbus.hessianserver.RequestImpl;
import com.keli.hfbus.hessianserver.UserManagerService;
import com.keli.hfbus.sqlite.BusDal;
import com.keli.hfbus.util.Contants;
import com.keli.hfbus.util.MyTimeList;
import com.keli.hfbus.util.Utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class SetOrderActivity extends PreferenceActivity
implements TimePickerDialog.OnTimeSetListener,OnItemSelectedListener, 
Preference.OnPreferenceChangeListener,OnClickListener{
	
//	private EditTextPreference mLabel;//标签
//    private CheckBoxPreference mEnabledPref;//启用
	private CheckBox isOpenCheckBox;//开启
    private AlarmPreference mAlarmPref;//铃声
    private Preference mTimePref;//时间
    private CheckBoxPreference mVibratePref;//震动
    private RepeatPreference mRepeatPref;//重复
    private MenuItem mTestAlarmItem;//菜单
    
    private int     mId;//闹钟id
    private int     mHour;//闹钟小时
    private int     mMinutes;//闹钟
    private boolean mTimePickerCancelled;
    private Alarm   mOriginalAlarm;
	
	
	// Initial value that can be set with the values saved in the database.
    private Alarm.DaysOfWeek mDaysOfWeek = new Alarm.DaysOfWeek(0);
    // New value that will be set if a positive result comes back from the
    // dialog.
    private Alarm.DaysOfWeek mNewDaysOfWeek = new Alarm.DaysOfWeek(0);
	private StationDistanceAdapter selectDistanceAdapter;
	private TextView tvOriention;
	private static TextView tvStartStation,tvEndStation,tvOrientation;
	private EditText editLinear;
	private ImageButton ibtnLinearQuery,ibtnSwitchOriention,ibtnSave,ibtnCancel,ibtnTitleReturn;
	public String stationDistance,linerName,orientation,arriveStation;
	public static String startStation;
	public static String endStation;
	private String linearId;
	private String stationId;
	public boolean isSave=false;//判断是是否执行过保存操作
	private ArrayList<HashMap<String, Object>> linearData;
	private Map busLinerDatail;
	private HFBusApp mApp;
	private Spinner spSelectDistance,spStationList;
	public static String [] stationList;
	private BusDal db;
	 private String userName;
	private List<Map<String, Object>> list_distance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.alarm_prefs);
		setContentView(R.layout.set_order);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
		 ibtnTitleReturn=(ImageButton)findViewById(R.id.ibtnTitleReturn);
         ibtnTitleReturn.setOnClickListener(this);
		this.getListView().setBackgroundResource(R.drawable.bg_list_item);
		mApp = (HFBusApp) getApplication();
		mApp.getmActivityManager().add(this);
		db=new BusDal(SetOrderActivity.this);
		userName=mApp.getUserName();
		isOpenCheckBox=(CheckBox)findViewById(R.id.cb_isOpen);
		spSelectDistance=(Spinner)findViewById(R.id.sp_select_distance);
		spStationList=(Spinner)findViewById(R.id.spStationList);
		ibtnLinearQuery=(ImageButton)findViewById(R.id.ibtnLinerarQuery);
		ibtnLinearQuery.setOnClickListener(this);
		ibtnSwitchOriention=(ImageButton)findViewById(R.id.ibtnSwitchOriention);
		ibtnSwitchOriention.setOnClickListener(this);
		editLinear=(EditText)findViewById(R.id.editLinear);
		tvOriention=(TextView)findViewById(R.id.tvOriention);
		tvStartStation=(TextView)findViewById(R.id.tvStartStation);
		tvEndStation=(TextView)findViewById(R.id.tvEndStation);
		tvOrientation=(TextView)findViewById(R.id.tvOriention);
		linearData = new ArrayList<HashMap<String, Object>>();
		 spSelectDistance.setOnItemSelectedListener(this);
			spStationList.setOnItemSelectedListener(this);
			HFBusApp busApp = ((HFBusApp)getApplicationContext());
			//alarmIntent=busApp.getAlarmIntent();
			ibtnSave=(ImageButton)findViewById(R.id.ibtnSave);
			ibtnSave.setOnClickListener(this);
			ibtnCancel=(ImageButton)findViewById(R.id.ibtnCancel1);
			ibtnCancel.setOnClickListener(this);
			Utils.setListSpinner(SetOrderActivity.this, list_distance, spSelectDistance, "distance",
					"distance");
	        //时间配置
	        mTimePref = findPreference("time");
	        //铃声配置
	        mAlarmPref = (AlarmPreference) findPreference("alarm");
	        mAlarmPref.setOnPreferenceChangeListener(this);
	        //震动配置
	        mVibratePref = (CheckBoxPreference) findPreference("vibrate");
	        mVibratePref.setOnPreferenceChangeListener(this);
	        //重复配置
	        mRepeatPref = (RepeatPreference) findPreference("setRepeat");
	        mRepeatPref.setOnPreferenceChangeListener(this);
	        isOpenCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					 if (isOpenCheckBox.isChecked()) {
                      //   popAlarmSetToast(SetOrderActivity.this, mHour, mMinutes,mRepeatPref.getDaysOfWeek());
                     }
					// Alarms.enableAlarm(this, alarm.id, enabled);
				}
			});
	      
	
		//selectDistanceAdapter=new StationDistanceAdapter(SetOrderActivity.this, distances);
		//spSelectDistance.setAdapter(selectDistanceAdapter);
	       
		  Intent i = getIntent();//从设置中跳转过来
	        mId = i.getIntExtra(Alarms.ALARM_ID, -1);//获得闹钟id
	        if (true) {
	            Log.v("wangxianming", "In SetAlarm, alarm id = " + mId);
	        }

	        Alarm alarm = null;
	        if (mId == -1) {
	            // No alarm id means create a new alarm.新建闹钟
	            alarm = new Alarm();//
	        } else {
	            /* load alarm details from database */
	            alarm = Alarms.getAlarm(getContentResolver(), mId);//从数据库中加载闹钟
	           
	          
	            // Bad alarm, bail to avoid a NPE若没有闹钟，则关闭
	            if (alarm == null) {
	                finish();
	                return;
	            }
	        }
	        mOriginalAlarm = alarm;//把传过来的闹钟赋值

	        updatePrefs(mOriginalAlarm);//用传过来的闹钟更新配置选项
	        // The last thing we do is pop the time picker if this is a new alarm.
	        //若是新建闹钟，弹出时间选择框
//	        if (mId == -1) {
//	            // Assume the user hit cancel
//	            mTimePickerCancelled = true;
//	            showTimePicker();
//	        }
        
}
	// Used to post runnables asynchronously.
    private static final Handler sHandler = new Handler();
	 public boolean onPreferenceChange(final Preference p, Object newValue) {
	        // Asynchronously save the alarm since this method is called _before_
	        // the value of the preference has changed.
	        sHandler.post(new Runnable() {
	            public void run() {
	                // Editing any preference (except enable) enables the alarm.
	                    isOpenCheckBox.setChecked(true);
	               // saveAlarmAndEnableRevert();
	            }
	        });
	        return true;
	    }
		 private void updatePrefs(Alarm alarm) {
		      // mId = alarm.id;
			 	mId = alarm.id;
			 	isOpenCheckBox.setChecked(alarm.enabled);
			 	mHour = alarm.hour;
			    mMinutes = alarm.minutes;
			    mRepeatPref.setDaysOfWeek(alarm.daysOfWeek);
			    mVibratePref.setChecked(alarm.vibrate);
			   mAlarmPref.setAlert(alarm.alert);
		       stationDistance=alarm.stationDistance;
		       arriveStation=alarm.arriveStation;
		       linerName=alarm.linear;
		       
		       if(mId!=-1){
		    	   endStation=alarm.endStation;
			       startStation=alarm.startSation;
			       orientation=alarm.orientation;
			       tvOriention.setText(orientation);
				   tvStartStation.setText(startStation);
				   tvEndStation.setText(endStation);
				   int distance;
				   if(stationDistance!=null){
					   distance=Integer.valueOf(stationDistance);
					   spSelectDistance.setSelection(distance-1);
				   }
				   String [] arrive={arriveStation};
				   ArrayAdapter<CharSequence> adapter = new ArrayAdapter(SetOrderActivity.this,
							android.R.layout.simple_spinner_item, arrive);
					adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
					spStationList.setAdapter(adapter);
		       }
		       editLinear.setText(linerName);
		       updateTime();
		    }
	

	@Override
	public void onClick(View v) { 
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.ibtnTitleReturn:
			onBackPressed();
			break;
		case R.id.ibtnLinerarQuery:
			linerName=editLinear.getText().toString().trim();
			if(linerName!=null&&!linerName.equals("")){
				if(linerName.contains("路")){
					
				}else{
					linerName=editLinear.getText().toString().trim()+ "路";
				}
			}else{
				Toast.makeText(SetOrderActivity.this,"请输入查询路线",Toast.LENGTH_SHORT).show();
				return;
			}
			System.out.println(linerName);
			
			Request request = new Request();
			request.query(new RequestGetLinerDtatil(this));
			
			break;
		case R.id.ibtnSwitchOriention:
			if(busLinerDatail!=null)
			{
				Map m;
				if(tvOriention.getText().toString().trim().equals("上行"))
				{
					tvOriention.setText("下行");
				   m =(Map) busLinerDatail.get("downstream");
				}else
				{
					tvOriention.setText("上行");
					  m =(Map) busLinerDatail.get("upstream");
				}
			     if(m==null) 
			    	 return;
				List list=(List) m.get("list");
				linearData.clear();
				HashMap<String,Object> map;
				
				for(int i=0;i<list.size();i++)
				{
					 map=new HashMap<String,Object>();
					 map.put("str",((HashMap<String,Object> ) list.get(i)) .get("STATIONNAME") );
					 linearData.add(map);
				}
				setArrayListSpinner(SetOrderActivity.this, linearData, spStationList, "str");
			}
			else{
				String end=tvEndStation.getText().toString();
				String start=tvStartStation.getText().toString();
				if(tvOriention.getText().toString().equals("上行"))
				{
					tvOriention.setText("下行");
					
				}else
				{
					tvOriention.setText("上行");
				}
				tvStartStation.setText(end);
				tvEndStation.setText(start);
			}
			
			break;
		case R.id.ibtnSave:
			if(linerName==null||linerName.equals("")||startStation==null||endStation==null||arriveStation==null){
		   		Toast.makeText(SetOrderActivity.this,"请查询路线",Toast.LENGTH_SHORT).show();
		   		return ;
		   	 }
			if(busLinerDatail!=null){
	    		Map m;
				if(tvOriention.getText().toString().trim().equals("上行"))
				{
				   m =(Map) busLinerDatail.get("downstream");
				}else
				{
					  m =(Map) busLinerDatail.get("upstream");
				}
			     if(m==null) 
			    	 return ;
				List list=(List) m.get("list");
				linearData.clear();
				HashMap<String,Object> map;
				if(list.size()>0)
					linearId=((HashMap<String,Object> )list.get(0)).get("LINEID").toString();
				String station;
				for(int i=0;i<list.size();i++)
				{
					station=(String)((HashMap<String,Object> ) list.get(i)) .get("STATIONNAME");
					if(station.equals(arriveStation)){
						stationId=((HashMap<String,Object> )list.get(i)).get("STATIONID").toString();
						break;
					}
				}
	    	}
			if(mId==-1){
				Request saveRequest = new Request();
				saveRequest.query(new saveOrder(this));
			}
			else{
				Request updateRequest = new Request();
				updateRequest.query(new updateOrder(this));
			}
			
			break;
		case R.id.ibtnCancel1:
			  int newId = mId;
              updatePrefs(mOriginalAlarm);//用原来闹钟配置
              // "Revert" on a newly created alarm should delete it.
//              if (mOriginalAlarm.id == -1) {
//                  Alarms.deleteAlarm(SetOrderActivity.this, newId);
//              } else {
//                  saveAlarm();
//              }
              //ibtnCancel.setEnabled(false);
			break;
		}
	}
	
	class RequestGetLinerDtatil extends RequestImpl{

		public RequestGetLinerDtatil(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Object doCall() {
			// TODO Auto-generated method stub
			//String url = "http://218.22.27.69:9080/Kljtxxfww_1_0_v/hessian/gjcxService";
			GjcxService mGjcxService;
		
			try {
				mGjcxService = (GjcxService) factory.create(GjcxService.class, url_gjcx, this.mContext.get().getClassLoader());
				//Log.i("RequestGetLinerDtatil", linerName);
				Map map=mGjcxService.getBuslineDetail(linerName);
				System.out.println("RequestGetLinerDtatil  "+map  );
				return map;
//				return this.HessianVoid;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return e;
			}
		    
		}

		@Override
		public void doComplete(Object o) {
			// TODO Auto-generated method stub
			super.doComplete(o);
			linearData.clear();
			 busLinerDatail = (Map)o;
			 Map m =(Map) busLinerDatail.get("upstream");
			 if(m==null) return;
			List list=(List) m.get("list");
			
			HashMap<String,Object> map;
			for(int i=0;i<list.size();i++)
			{
				 map=new HashMap<String,Object>();
				 map.put("str",((HashMap<String,Object> ) list.get(i)) .get("STATIONNAME") );
				 linearData.add(map);
			}
			//System.out.println(busLinerDatail);
			//System.out.println("linearData"+linearData);
			setArrayListSpinner(SetOrderActivity.this, linearData, spStationList, "str");
			
		}
	}
	public static void setArrayListSpinner(Context context,
			ArrayList<HashMap<String, Object>> list, Spinner sp, String textColumn ) {
		if (list == null)
			return;
		try {
			stationList = new String[list.size()];
			Iterator<HashMap<String, Object>> iterator = list.listIterator();
			int i = 0;
			while (iterator.hasNext()) {
				HashMap<String, Object> obj = iterator.next();
				stationList[i] = obj.get(textColumn).toString();
				i++;
			}
			startStation=stationList[0];
			System.out.println("startStation"+startStation);
			endStation=stationList[i-1];
			tvStartStation.setText(startStation);
			tvEndStation.setText(endStation);
			ArrayAdapter<CharSequence> adapter = new ArrayAdapter(context,
					android.R.layout.simple_spinner_item, stationList);
			adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
			sp.setAdapter(adapter);
		} catch (Exception ex) {
		}
	}
    //保存闹钟函数
    private long saveAlarm() {
        Alarm alarm = new Alarm();
        alarm.id = mId;
        alarm.enabled = isOpenCheckBox.isChecked();
        alarm.hour = mHour;
        alarm.minutes = mMinutes;
        alarm.daysOfWeek =mRepeatPref.getDaysOfWeek();
        System.out.println("alarm.daysOfWeek:"+alarm.daysOfWeek.isRepeatSet() );
        alarm.vibrate = mVibratePref.isChecked();
        alarm.label = "";
        alarm.alert =mAlarmPref.getAlert();;
        alarm.endStation=endStation;
        alarm.startSation=startStation;
        alarm.orientation=tvOrientation.getText().toString().trim();
        alarm.arriveStation=arriveStation;
        alarm.stationDistance=stationDistance;
        alarm.linear=linerName;

        long time;
        if (alarm.id == -1) {
        	
            time = Alarms.addAlarm(this, alarm);
            // addAlarm populates the alarm with the new id. Update mId so that
            // changes to other preferences update the new alarm.
            mId = alarm.id;
        } else {
            time = Alarms.setAlarm(this, alarm);
            
        }
        popAlarmSetToast(this, time);
        return time;
    }
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String [] distances={"1","2","3","4","5"};
		switch(parent.getId())
		{
		case R.id.sp_select_distance:
			stationDistance=distances[position];
			System.out.println(view.getId());
			System.out.println("stationDistance"+stationDistance);
			break;
		case R.id.spStationList:
			if(stationList!=null)
				arriveStation=stationList[position];
			System.out.println("arriveStation"+arriveStation);
				break;
		}
	}
	 @Override
	    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
	            Preference preference) {
	        if (preference == mTimePref) {
	            showTimePicker();
	        }

	        return super.onPreferenceTreeClick(preferenceScreen, preference);
	    }
	 private void showTimePicker() {
	        new TimePickerDialog(this, this, mHour, mMinutes,
	                DateFormat.is24HourFormat(this)).show();
	    }
	  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	        // onTimeSet is called when the user clicks "Set"
	        mTimePickerCancelled = false;
	        mHour = hourOfDay;
	        mMinutes = minute;
	        updateTime();
	        // If the time has been changed, enable the alarm.
	        isOpenCheckBox.setChecked(true);
	        // Save the alarm and pop a toast.
	      //  popAlarmSetToast(this, saveAlarmAndEnableRevert());
	    }

	    private void updateTime() {
	        Log.v("wangxianming", "updateTime " + mId);
	        mTimePref.setSummary(Alarms.formatTime(this, mHour, mMinutes,
	                mRepeatPref.getDaysOfWeek()));
	    }
	    
	    private long saveAlarmAndEnableRevert() {
	        // Enable "Revert" to go back to the original Alarm.
//	        final Button revert = (Button) findViewById(R.id.alarm_revert);
//	        revert.setEnabled(true);
	        return saveAlarm();
	    }
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	/***更新数据到网站*/
	class updateOrder extends RequestImpl{
		

		public updateOrder(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public Object doCall() {
			// TODO Auto-generated method stub
			super.doCall();
			//String url = "http://218.22.27.75:80/Kljtxxfww_1_0_v/hessian/userManagerService";
			UserManagerService userManagerService;
			try {
				userManagerService = (UserManagerService) factory.create(UserManagerService.class, url_yhxx, this.mContext.get().getClassLoader());
				String fx;
				if(tvOrientation.getText().toString().trim().equals("上行"))
					fx="0";
				else
					fx="1";
				boolean [] xq=mRepeatPref.getDaysOfWeek().getBooleanArray();
				List<String> xqList=new ArrayList<String>();  
				for(int i=0;i<xq.length;i++){
					if(xq[i]){
						xqList.add(String.valueOf(i+1));
							//System.out.println("周几"+i);
					}
				}
				
				if(xqList.size()==0){
					int weekDay = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK);
					if(weekDay==1)
					{
						xqList.add("7");
					}
					else
						xqList.add(String.valueOf((weekDay-1)));
				}
				String [] XQ = (String [])xqList.toArray(new String[xqList.size()]);
				List<String> sjList=new ArrayList<String>();  
				String hour=String.valueOf(mHour);
				String minute=String.valueOf(mMinutes);
				if(hour.length()==1){
        			hour="0".concat(hour);
        		}
        		if(minute.length()==1){
        			minute="0".concat(minute);
        		}
				String sj=String.valueOf(hour).concat(":").concat(String.valueOf(minute));
				sjList.add(sj);
				Alarm alarm = Alarms.getAlarm(getContentResolver(), mId);//从数据库中加载闹钟
				stationId=alarm.stationId;
				linearId=alarm.linearId;
				Cursor cursor=Alarms.getSaveAlarm(getContentResolver(), alarm.orderId);
				 if (cursor != null) {
			            if (cursor.moveToFirst()) {
			            	do{
			            		System.out.println("id"+cursor.getInt(Alarm.Columns.ALARM_ID_INDEX));
			            		System.out.println("minutes"+cursor.getString(Alarm.Columns.ALARM_MINUTES_INDEX));
			            		hour=cursor.getString(Alarm.Columns.ALARM_HOUR_INDEX);
			            		minute=cursor.getString(Alarm.Columns.ALARM_MINUTES_INDEX);
			            		if(hour.length()==1){
			            			hour="0".concat(hour);
			            		}
			            		if(minute.length()==1){
			            			minute="0".concat(minute);
			            		}
				            	if(cursor.getInt(Alarm.Columns.ALARM_ID_INDEX)!=mId){
				            		sjList.add(hour.concat(":").concat(minute));
				            	}
			            		
			            	}while(cursor.moveToNext());
			            }
			            cursor.close();
			        }
				 String [] SJ= (String [])sjList.toArray(new String[sjList.size()]);
			//	Map map=userManagerService.saveGjcgz("userManagerService", "userManagerService", url_yhxx,userName,linerName,fx,stationId,stationDistance,XQ,SJ,linearId,arriveStation,isOpenCheckBox.isChecked()?"1":"0");
//				 System.out.println("orderId"+alarm.orderId);
//				 System.out.println("userName"+userName);
//				 System.out.println("linerName"+linerName);
//				 System.out.println("stationId"+stationId);
//				 System.out.println("stationDistance"+stationDistance);
//				 System.out.println("stationDistance"+stationDistance);
//				 System.out.println("SJ"+SJ.toString());
//				 System.out.println("linearId"+linearId);
//				 System.out.println("fx"+fx);
				Map map=userManagerService.updateGjcgz("userManagerService", "userManagerService", url_yhxx,alarm.orderId,userName, linerName,fx, stationId, stationDistance, XQ, SJ,linearId, arriveStation,isOpenCheckBox.isChecked()?"1":"0");
				return map;
//			 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				return e;
				
			}
		}

		@Override
		public void doComplete(Object o) {
			// TODO Auto-generated method stub
			super.doComplete(o);
			
			Map m=(Map) o;
			System.out.println("map："+m);
			int status=Integer.parseInt( m.get("status").toString()) ;
			int operaStatus=Integer.parseInt( m.get("operaStatus").toString()) ;
			if(status==100){
				if(operaStatus==0){
					Alarm alarm = new Alarm();
			        alarm.id = mId;
			        alarm.enabled = isOpenCheckBox.isChecked();
			        alarm.hour = mHour;
			        alarm.minutes = mMinutes;
			        alarm.daysOfWeek =mRepeatPref.getDaysOfWeek();
			        System.out.println("alarm.daysOfWeek:"+alarm.daysOfWeek.isRepeatSet() );
			        alarm.vibrate = mVibratePref.isChecked();
			        alarm.label = "";
			        alarm.alert =mAlarmPref.getAlert();;
			        alarm.endStation=endStation;
			        alarm.startSation=startStation;
			        alarm.orientation=tvOrientation.getText().toString().trim();
			        alarm.arriveStation=arriveStation;
			        alarm.stationDistance=stationDistance;
			        alarm.linear=linerName;
			        Alarms.setAlarm(SetOrderActivity.this, alarm);
			    	Cursor cursor=Alarms.getSaveAlarm(getContentResolver(), alarm.orderId);
					 if (cursor != null) {
				            if (cursor.moveToFirst()) {
				            	if(cursor.getInt(Alarm.Columns.ALARM_ALERT_INDEX)!=mId){
				            		 alarm.hour=cursor.getInt(Alarm.Columns.ALARM_HOUR_INDEX);
						            	alarm.hour=cursor.getInt(Alarm.Columns.ALARM_MINUTES_INDEX);
						            	 Alarms.setAlarm(SetOrderActivity.this, alarm);//设置闹钟
				            	}
				              
				            }
				            cursor.close();
				        }
					 Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_LONG).show();
						finish();//关闭
				}
				 else if(operaStatus==1)
					 Toast.makeText(getApplicationContext(), "修改出错！", Toast.LENGTH_LONG).show();
				 else
					 Toast.makeText(getApplicationContext(), "原密码错误", Toast.LENGTH_LONG).show();
				
			}else if(status==101)
			{
				Toast.makeText(getApplicationContext(), "通讯异常，保存失败！", Toast.LENGTH_LONG).show();
				System.out.println("内部，用户名或密码错误");
				return ;
			}else if(status==102)
			{
				Toast.makeText(getApplicationContext(), "通讯异常，保存失败！", Toast.LENGTH_LONG).show();
				System.out.println("没有接口权限或接口不存在");
				return ;
			}else if(status==103)
			{
				Toast.makeText(getApplicationContext(), "通讯异常，保存失败！", Toast.LENGTH_LONG).show();
				System.out.println("用户对该接口的当日访问量用完");
				return ;
			}else
			{
				Toast.makeText(getApplicationContext(), "通讯异常，保存失败！", Toast.LENGTH_LONG).show();
				System.out.println("验证程序异常 稍后重试");
				return ;
			}
		}

		@Override
		public void doTimeout() {
			// TODO Auto-generated method stub
			super.doTimeout();
		}

		@Override
		public void doError(Exception e) {
			// TODO Auto-generated method stub
			super.doError(e);
		}

		@Override
		public void doLoading() {
			// TODO Auto-generated method stub
//			super.doLoading();
		}
		
		
	}
	/***保存数据到网站*/
	class saveOrder extends RequestImpl{

		public saveOrder(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public Object doCall() {
			// TODO Auto-generated method stub
			super.doCall();
			//String url = "http://218.22.27.75:80/Kljtxxfww_1_0_v/hessian/userManagerService";
			UserManagerService userManagerService;
			try {
				userManagerService = (UserManagerService) factory.create(UserManagerService.class, url_yhxx, this.mContext.get().getClassLoader());
				String fx;
				if(tvOrientation.getText().toString().trim().equals("上行"))
					fx="0";
				else
					fx="1";
				boolean [] xq=mRepeatPref.getDaysOfWeek().getBooleanArray();
				List<String> list=new ArrayList<String>();  
				for(int i=0;i<xq.length;i++){
					if(xq[i]){
							list.add(String.valueOf(i+1));
							System.out.println("周几"+i);
					}
				}
				
				if(list.size()==0){
					int weekDay = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK);
					if(weekDay==1)
					{
						list.add("7");
					}
					else
						list.add(String.valueOf((weekDay-1)));
				}
				String [] XQ = (String [])list.toArray(new String[list.size()]);
				String sj=String.valueOf(mHour).concat(":").concat(String.valueOf(mMinutes));
				String [] SJ= {sj};
				Map map=userManagerService.saveGjcgz("userManagerService", "userManagerService", url_yhxx,userName,linerName,fx,stationId,stationDistance,XQ,SJ,linearId,arriveStation,isOpenCheckBox.isChecked()?"1":"0");
				 
				return map;
//			 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				return e;
				
			}
		}

		@Override
		public void doComplete(Object o) {
			// TODO Auto-generated method stub
			super.doComplete(o);
			
			Map m=(Map) o;
			System.out.println("map："+m);
			int status=Integer.parseInt( m.get("status").toString()) ;
			int operaStatus=Integer.parseInt( m.get("operaStatus").toString()) ;
			if(status==100){
				if(operaStatus==0){
					 long isSave=saveAlarm();//保存闹钟
					 if(isSave!=0)
						 finish();//关闭
				}
				 else if(operaStatus==1)
					 Toast.makeText(getApplicationContext(), "保存到服务器失败！", Toast.LENGTH_LONG).show();
				 else
					 Toast.makeText(getApplicationContext(), "保存出错，设置公交线路预约重复！", Toast.LENGTH_LONG).show();
				
			}else if(status==101)
			{
				Toast.makeText(getApplicationContext(), "通讯异常，保存失败！", Toast.LENGTH_LONG).show();
				System.out.println("内部，用户名或密码错误");
				return ;
			}else if(status==102)
			{
				Toast.makeText(getApplicationContext(), "通讯异常，保存失败！", Toast.LENGTH_LONG).show();
				System.out.println("没有接口权限或接口不存在");
				return ;
			}else if(status==103)
			{
				Toast.makeText(getApplicationContext(), "通讯异常，保存失败！", Toast.LENGTH_LONG).show();
				System.out.println("用户对该接口的当日访问量用完");
				return ;
			}else
			{
				Toast.makeText(getApplicationContext(), "通讯异常，保存失败！", Toast.LENGTH_LONG).show();
				System.out.println("验证程序异常 稍后重试");
				return ;
			}
		}

		@Override
		public void doTimeout() {
			// TODO Auto-generated method stub
			super.doTimeout();
		}

		@Override
		public void doError(Exception e) {
			// TODO Auto-generated method stub
			super.doError(e);
		}

		@Override
		public void doLoading() {
			// TODO Auto-generated method stub
//			super.doLoading();
		}
		
		
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

	public static void popAlarmSetToast(Context context, int hour, int minute,
            Alarm.DaysOfWeek daysOfWeek) {
		popAlarmSetToast(context,
		Alarms.calculateAlarm(hour, minute, daysOfWeek)
		.getTimeInMillis());
	}

	public static void popAlarmSetToast(Context context, long timeInMillis) {
		String toastText = formatToast(context, timeInMillis);
		Toast toast = Toast.makeText(context, toastText, Toast.LENGTH_SHORT);
		ToastMaster.setToast(toast);
		toast.show();
	}
/**
 * format "Alarm set for 2 days 7 hours and 53 minutes from
 * now"
 */
	static String formatToast(Context context, long timeInMillis) {
    long delta = timeInMillis - System.currentTimeMillis();
    long hours = delta / (1000 * 60 * 60);
    long minutes = delta / (1000 * 60) % 60;
    long days = hours / 24;
    hours = hours % 24;

    String daySeq = (days == 0) ? "" :
            (days == 1) ? context.getString(R.string.day) :
            context.getString(R.string.days, Long.toString(days));

    String minSeq = (minutes == 0) ? "" :
            (minutes == 1) ? context.getString(R.string.minute) :
            context.getString(R.string.minutes, Long.toString(minutes));

    String hourSeq = (hours == 0) ? "" :
            (hours == 1) ? context.getString(R.string.hour) :
            context.getString(R.string.hours, Long.toString(hours));

    boolean dispDays = days > 0;
    boolean dispHour = hours > 0;
    boolean dispMinute = minutes > 0;

    int index = (dispDays ? 1 : 0) |
                (dispHour ? 2 : 0) |
                (dispMinute ? 4 : 0);

    String[] formats = context.getResources().getStringArray(R.array.alarm_set);
    return String.format(formats[index], daySeq, hourSeq, minSeq);
}
@Override
public void onBackPressed() {
    // In the usual case of viewing an alarm, mTimePickerCancelled is
    // initialized to false. When creating a new alarm, this value is
    // assumed true until the user changes the time.
    if (!mTimePickerCancelled) {
        //saveAlarm();
    }
    finish();
}

}
