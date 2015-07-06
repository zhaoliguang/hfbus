package com.keli.hfbus.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.keli.hfbus.HFBusApp;
import com.keli.hfbus.R;
import com.keli.hfbus.util.MyTimeList;
import com.keli.hfbus.util.Utils;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

public class DateSetActivity extends Activity implements OnClickListener{
	
	public static String timeSharePre="alart_time";
	private Editor editor;
	EditText edTime1,edTime2,edTime3,edTime4;
	ImageButton ibtnSet1,ibtnSet2,ibtnSet3,ibtnSet4,ibtnClear1,ibtnClear2,ibtnClear3,ibtnClear4,
		ibtnOk,ibtnCancel,ibtnTitleReturn;
	String times;
	Calendar calendar;
	Bundle bundle=new Bundle();
	private HFBusApp mApp;
	//ArrayList timeList=new ArrayList<String>();
	Calendar []calendarList;
	String timeStrings;
	MyTimeList timeList;
	SharedPreferences  mTimeShare;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
		setContentView(R.layout.date_set);
		ibtnTitleReturn=(ImageButton)findViewById(R.id.ibtnTitleReturn);
        ibtnTitleReturn.setOnClickListener(this);
		mApp = (HFBusApp) getApplication();
	    mApp.getmActivityManager().add(this);
	    mTimeShare=getSharedPreferences(timeSharePre,0);
	    editor=mTimeShare.edit();
	    
		edTime1=(EditText)findViewById(R.id.edTime1);
		edTime2=(EditText)findViewById(R.id.edTime2);
		edTime3=(EditText)findViewById(R.id.edTime3);
		edTime4=(EditText)findViewById(R.id.edTime4);
		ibtnSet1=(ImageButton)findViewById(R.id.ibtnSet1);
		ibtnSet2=(ImageButton)findViewById(R.id.ibtnSet2);
		ibtnSet3=(ImageButton)findViewById(R.id.ibtnSet3);
		ibtnSet4=(ImageButton)findViewById(R.id.ibtnSet4);
		ibtnClear1=(ImageButton)findViewById(R.id.ibtnClear1);
		ibtnClear2=(ImageButton)findViewById(R.id.ibtnClear2);
		ibtnClear3=(ImageButton)findViewById(R.id.ibtnClear3);
		ibtnClear4=(ImageButton)findViewById(R.id.ibtnClear4);
		ibtnOk=(ImageButton)findViewById(R.id.ibtnOk);
		ibtnCancel=(ImageButton)findViewById(R.id.ibtnCancel);
		ibtnSet1.setOnClickListener(this);
		ibtnSet2.setOnClickListener(this);
		ibtnSet3.setOnClickListener(this);
		ibtnSet4.setOnClickListener(this);
		ibtnClear1.setOnClickListener(this);
		ibtnClear2.setOnClickListener(this);
		ibtnClear3.setOnClickListener(this);
		ibtnClear4.setOnClickListener(this);
		ibtnOk.setOnClickListener(this);
		ibtnCancel.setOnClickListener(this);
		HFBusApp busApp = ((HFBusApp)getApplicationContext());
		calendarList=busApp.getCalendarList();
		timeList=busApp.getTimeList();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=getIntent();
		switch(v.getId()){
		case R.id.ibtnTitleReturn:
			super.onBackPressed();
			break;
		case R.id.edTime1:
			
			break;
		case R.id.edTime2:
			break;
		case R.id.edTime3:
			break;
		case R.id.edTime4:
			break;
		case R.id.ibtnSet1:
			showDatePicker(edTime1);
			calendarList[0]=calendar;
			break;
		case R.id.ibtnSet2:
			showDatePicker(edTime2);
			calendarList[1]=calendar;
			
			break;
		case R.id.ibtnSet3:
			showDatePicker(edTime3);
			calendarList[2]=calendar;
			
			break;
		case R.id.ibtnSet4:
			showDatePicker(edTime4);
			calendarList[3]=calendar;
			
			break;
		case R.id.ibtnClear1:
			clearTime(edTime1);
			calendarList[0]=null;
			editor.remove("hour1");
			editor.remove("minute1");
			break;
		case R.id.ibtnClear2:
			
			clearTime(edTime2);
			calendarList[1]=null;
			editor.remove("hour2");
			editor.remove("minute2");
			break;
		case R.id.ibtnClear3:
			clearTime(edTime3);
			calendarList[2]=null;
			editor.remove("hour3");
			editor.remove("minute3");
			break;
		case R.id.ibtnClear4:
			clearTime(edTime4);
			calendarList[3]=null;
			editor.remove("hour4");
			editor.remove("minute4");
			break;
		case R.id.ibtnOk:
			saveTime("hour1","minute1",edTime1.getText().toString().trim());
			saveTime("hour2","minute2",edTime2.getText().toString().trim());
			saveTime("hour3","minute3",edTime3.getText().toString().trim());
			saveTime("hour4","minute4",edTime4.getText().toString().trim());
			
			times=timeList.getTimeString();
			intent.putExtra("times", times);
			DateSetActivity.this.setResult(0,intent);
			DateSetActivity.this.finish();
			break;
		case R.id.ibtnCancel:
//			for(int i=0;i<timeString.length;i++)
//				timeString[i]="";
			times=timeList.getTimeString();
			intent.putExtra("times", times);
			DateSetActivity.this.setResult(0,intent);
			editor.clear();
			DateSetActivity.this.finish();
			break;
		}
		editor.commit();
	}
	public void saveTime(String hour,String minute,String time){
		String []temp;
		if(time!=null&&!time.equals(""))
		{
			temp=time.split(":");
			editor.putInt(hour, Integer.valueOf(temp[0]));
			editor.putInt(minute, Integer.valueOf(temp[1]));
		}
	}
	public void showDatePicker(final EditText editText){
		  calendar=Calendar.getInstance();
		  calendar.setTimeInMillis(System.currentTimeMillis());
		  
          int mHour=calendar.get(Calendar.HOUR_OF_DAY);
          int mMinute=calendar.get(Calendar.MINUTE);
          int mDay=calendar.get(Calendar.DAY_OF_WEEK);
          
          new TimePickerDialog(DateSetActivity.this,
            new TimePickerDialog.OnTimeSetListener()
            {                
              public void onTimeSet(TimePicker view,int hourOfDay,
                                    int minute)
              {
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
                Date date=calendar.getTime();
                
                if(!timeList.contains(date)){
                	timeList.add(date);
                	String time=Utils.formatTime(hourOfDay)+":"+Utils.formatTime(minute);
                	editText.setText(time);
                	 Toast.makeText(DateSetActivity.this,"设置的时间为:"+time,Toast.LENGTH_SHORT).show();
                }else{
                	Toast.makeText(DateSetActivity.this,"已经设置过相同的时间",Toast.LENGTH_SHORT).show();
                	editText.setText("");
                }
               
              }          
            },mHour,mMinute,true).show();
	}
	public void clearTime(EditText editText){
		
		String time=editText.getText().toString();
	
		if(time==null||time.equals(""))
			return;
		else{
			System.out.println("time"+time);
			boolean ss =timeList.remove(time);
			
			editText.setText("");
		}
		
	}
	private int getHour(Calendar c){
		int mHour=calendar.get(Calendar.HOUR_OF_DAY);
		return mHour;
	}
	private int getMinute(Calendar c){
		int mMinute=calendar.get(Calendar.MINUTE);
		return mMinute;
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		times="";
		timeList.clear();
		editor.clear();
		editor.commit();
		Intent intent=getIntent();
		intent.putExtra("times", times);
		DateSetActivity.this.setResult(0,intent);
		super.onBackPressed();
	}
	
}
