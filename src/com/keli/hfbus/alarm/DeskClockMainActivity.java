package com.keli.hfbus.alarm;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.keli.hfbus.HFBusApp;
import com.keli.hfbus.HFBusMain;
import com.keli.hfbus.R;
import com.keli.hfbus.hessianserver.Request;
import com.keli.hfbus.hessianserver.RequestImpl;
import com.keli.hfbus.hessianserver.UserManagerService;
import com.keli.hfbus.ui.SetOrderActivity;
import com.keli.hfbus.util.DialogPrompt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DeskClockMainActivity extends Activity implements OnItemClickListener,OnClickListener{
	
    static final String PREFERENCES = "AlarmClock";

    /** This must be false for production.  If true, turns on logging,
        test code, etc. */
    static final boolean DEBUG = false;

    private SharedPreferences mPrefs;
    private LayoutInflater mFactory;
    private ListView mAlarmsList;
    private Cursor mCursor;
    private HFBusApp mApp;
    private String userName;
	private ImageButton ibtnTitleReturn;
	private int id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = (HFBusApp) getApplication();
		mApp.getmActivityManager().add(this);
		userName=mApp.getUserName();
        mFactory = LayoutInflater.from(this);
        
        mCursor = Alarms.getAlarmsCursor(getContentResolver());
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
    	setContentView(R.layout.alarm_clock);
    	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);

    }
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	 Request request = new Request();
    	 request.query(new getOrder(this));
         updateLayout();
    }
    private void updateLayout() {
    	
    	ibtnTitleReturn=(ImageButton)findViewById(R.id.ibtnTitleReturn);
        ibtnTitleReturn.setOnClickListener(this);
        mAlarmsList = (ListView) findViewById(R.id.alarms_list);
        AlarmTimeAdapter adapter = new AlarmTimeAdapter(this, mCursor);
        mAlarmsList.setAdapter(adapter);
        mAlarmsList.setVerticalScrollBarEnabled(true);
        mAlarmsList.setOnItemClickListener(this);
        mAlarmsList.setOnCreateContextMenuListener(this);
        
        View addAlarm = findViewById(R.id.add_alarm);
        addAlarm.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    addNewAlarm();
                }
            });
        // Make the entire view selected when focused.
        addAlarm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    v.setSelected(hasFocus);
                }
        });

        ImageButton deskClock =
                (ImageButton) findViewById(R.id.desk_clock_button);
        deskClock.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    
                }
        });
    }
    
    private void addNewAlarm() {
        startActivity(new Intent(this, SetOrderActivity.class));
    }
    
 
    private class AlarmTimeAdapter extends CursorAdapter {
        public AlarmTimeAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View ret = mFactory.inflate(R.layout.alarm_time, parent, false);

            DigitalClock digitalClock =
                    (DigitalClock) ret.findViewById(R.id.digitalClock);
            digitalClock.setLive(false);
            return ret;
        }
        public void bindView(View view, Context context, Cursor cursor) {
            final Alarm alarm = new Alarm(cursor);

            View indicator = view.findViewById(R.id.indicator);

            // Set the initial resource for the bar image.
            final ImageView barOnOff =
                    (ImageView) indicator.findViewById(R.id.bar_onoff);
            barOnOff.setImageResource(alarm.enabled ?
                    R.drawable.ic_indicator_on : R.drawable.ic_indicator_off);
          
            // Set the initial state of the clock "checkbox"
            final CheckBox clockOnOff =
                    (CheckBox) indicator.findViewById(R.id.clock_onoff);
            clockOnOff.setChecked(alarm.enabled);

            // Clicking outside the "checkbox" should also change the state.
            indicator.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        clockOnOff.toggle();
                        updateIndicatorAndAlarm(clockOnOff.isChecked(),
                                barOnOff, alarm);
                    }
            });

            DigitalClock digitalClock =
                    (DigitalClock) view.findViewById(R.id.digitalClock);

            // set the alarm text
            final Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, alarm.hour);
            c.set(Calendar.MINUTE, alarm.minutes);
            digitalClock.updateTime(c);
            digitalClock.setTypeface(Typeface.DEFAULT);
            
            final TextView linearName=(TextView)view.findViewById(R.id.bus);
            linearName.setText(alarm.linear);
            final TextView start=(TextView)view.findViewById(R.id.start);
            start.setText(alarm.startSation);
            final TextView end=(TextView)view.findViewById(R.id.end);
            end.setText(alarm.endStation);
            final TextView arrive=(TextView)view.findViewById(R.id.arrive);
            arrive.setText(alarm.arriveStation);
            final TextView distance=(TextView)view.findViewById(R.id.distance);
            distance.setText(alarm.stationDistance);
            // Set the repeat text or leave it blank if it does not repeat.
            TextView daysOfWeekView =
                    (TextView) digitalClock.findViewById(R.id.daysOfWeek);
            final String daysOfWeekStr =
                    alarm.daysOfWeek.toString(DeskClockMainActivity.this, false);
            if (daysOfWeekStr != null && daysOfWeekStr.length() != 0) {
                daysOfWeekView.setText(daysOfWeekStr);
                daysOfWeekView.setVisibility(View.VISIBLE);
            } else {
                daysOfWeekView.setText("今日");
            }
        }
    };
    //更新checkbox
    private void updateIndicatorAndAlarm(boolean enabled, ImageView bar,
            Alarm alarm) {
        bar.setImageResource(enabled ? R.drawable.ic_indicator_on
                : R.drawable.ic_indicator_off);
        ContentResolver resolver = DeskClockMainActivity.this.getContentResolver();
        ContentValues values = new ContentValues(1);
        if(!enabled){
        	Alarms.disableAlert(DeskClockMainActivity.this,alarm.id);
    	//	Alarms.setNextAlert(DeskClockMainActivity.this);
    		values.put(Alarm.Columns.flag, 0);
    		resolver.update(ContentUris.withAppendedId(
	                Alarm.Columns.CONTENT_URI, alarm.id), values, null, null);
        }
      //  Alarm alarmCurrent=Alarms.getAlarm(getContentResolver(), alarm.id);
        if (enabled) {
            SetOrderActivity.popAlarmSetToast(this, alarm.hour, alarm.minutes,
                    alarm.daysOfWeek);
        }
        Alarms.enableAlarm(this, alarm.id, enabled);
    }
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
         */
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final AdapterContextMenuInfo info =
                (AdapterContextMenuInfo) item.getMenuInfo();
         id = (int) info.id;
        // Error check just in case.
        if (id == -1) {
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.delete_alarm:
                // Confirm that the alarm will be deleted.
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.delete_alarm))
                        .setMessage(getString(R.string.delete_alarm_confirm))
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface d,
                                            int w) {
                                    	 Request request = new Request();
                                    	 request.query(new deleteOrder(DeskClockMainActivity.this));  
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
                return true;

            case R.id.enable_alarm:
                final Cursor c = (Cursor) mAlarmsList.getAdapter()
                        .getItem(info.position);
                final Alarm alarm = new Alarm(c);
                ContentResolver resolver = DeskClockMainActivity.this.getContentResolver();
                ContentValues values = new ContentValues(1);
                
                              
                if (!alarm.enabled) {
                	
                   SetOrderActivity.popAlarmSetToast(this, alarm.hour, alarm.minutes,
                            alarm.daysOfWeek);
                }
                else{
                	Alarms.disableAlert(DeskClockMainActivity.this,alarm.id);
                	values.put(Alarm.Columns.flag, 0);
            		resolver.update(ContentUris.withAppendedId(
        	                Alarm.Columns.CONTENT_URI, alarm.id), values, null, null);
                }
                Alarms.enableAlarm(this, alarm.id, !alarm.enabled);
                return true;

            case R.id.edit_alarm:
                Intent intent = new Intent(this, SetOrderActivity.class);
                intent.putExtra(Alarms.ALARM_ID, id);
                startActivity(intent);
                return true;

            default:
                break;
        }
        return super.onContextItemSelected(item);
    }
    
   
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
            ContextMenuInfo menuInfo) {
        // Inflate the menu from xml.
        getMenuInflater().inflate(R.menu.context_menu, menu);

        // Use the current item to create a custom view for the header.
        final AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        final Cursor c =
                (Cursor) mAlarmsList.getAdapter().getItem((int) info.position);
        final Alarm alarm = new Alarm(c);

        // Construct the Calendar to compute the time.
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, alarm.hour);
        cal.set(Calendar.MINUTE, alarm.minutes);
        final String time = Alarms.formatTime(this, cal);

        // Inflate the custom view and set each TextView's text.
        final View v = mFactory.inflate(R.layout.context_menu_header, null);
        TextView textView = (TextView) v.findViewById(R.id.header_time);
        textView.setText(time);
//        textView = (TextView) v.findViewById(R.id.header_label);
//        textView.setText(alarm.label);

        // Set the custom view on the menu.
        menu.setHeaderView(v);
        // Change the text based on the state of the alarm.
        if (alarm.enabled) {
            menu.findItem(R.id.enable_alarm).setTitle(R.string.disable_alarm);
        }
    }
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            //case R.id.menu_item_desk_clock:
            	//modify by wangxianming in 2012-4-14
//                startActivity(new Intent(this, DeskClock.class));
              //  return true;
            case R.id.menu_item_add_alarm:
                addNewAlarm();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
   
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
  
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long id) {
		Intent intent = new Intent(this, SetOrderActivity.class);
        intent.putExtra(Alarms.ALARM_ID, (int) id);
        startActivity(intent);
		
	}
	/***从网站获取预约数据*/
	class getOrder extends RequestImpl{

		public getOrder(Context context) {
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
				Map map=userManagerService.getGjcgz("userManagerService","userManagerService", url_yhxx,userName,null,null,null,null,null);
				 
				return map;
//			 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return e;
				
			}
		}

		@Override
		public void doComplete(Object o) {
			// TODO Auto-generated method stub
			super.doComplete(o);
			
			Map m=(Map) o;
			int status=Integer.parseInt( m.get("status").toString()) ;
			 
			if(status==100){
				Alarms.deleteAllAlarm(DeskClockMainActivity.this);
				
				List list = (List) m.get("gjcList");
				HashMap<String, Object> map;
				Alarm alarm = new Alarm();
				for (int i = 0; i < list.size(); i++){
					map = (HashMap<String, Object>)list.get(i);
					String orderId=(String)map.get("ID");
			        String [] SJ=((String)map.get("SJ")).split(";");
			        String [] XQ=((String)map.get("XQ")).split(";");
			        String [] XXFX=((String)map.get("XXFX")).split("[-,>]");
			        int FX=new BigDecimal(map.get("FX").toString()).intValue();
			        int TQTX=new BigDecimal(map.get("TQTX").toString()).intValue();
			        int linearId=new BigDecimal(map.get("LINEID").toString()).intValue();
			        String CZMC=(String)map.get("CZMC");
			        String CZ=(String)map.get("CZ");
			        String XLMC=(String)map.get("XLMC");
			        Alarm.DaysOfWeek mDaysOfWeek = new Alarm.DaysOfWeek(0);
			        for(int n=0;n<XQ.length;n++){
			        	int day=Integer.valueOf(XQ[n]);
			        	mDaysOfWeek.set(day-1, true);
			        }
			        for(int k=0;k<SJ.length;k++){
			        	//alarm.id = mId;
			        	alarm.enabled = (new BigDecimal(map.get("QY").toString()).intValue())==1;
			        	String [] time=SJ[k].split(":");
			        	int hour1=Integer.parseInt(time[0]);
			        	int minute1=Integer.parseInt(time[1]);
			        	alarm.hour = hour1;
			        	alarm.minutes = minute1;
			        	alarm.daysOfWeek=mDaysOfWeek;
			        	alarm.label = "";
			        	alarm.startSation=XXFX[0];
			        	alarm.endStation=XXFX[3];
			        	alarm.orientation=FX==0?"上行":"下行";
			        	alarm.arriveStation=CZMC;
			        	alarm.stationDistance=String.valueOf(TQTX);
			        	alarm.linear=XLMC;
			        	alarm.orderId=orderId;
			        	alarm.stationId=CZ;
			        	alarm.linearId=String.valueOf(linearId);
			        	Alarms.addAlarm(DeskClockMainActivity.this, alarm);
			        }

				}
				
			}else if(status==101)
			{
				Toast.makeText(getApplicationContext(), "通讯异常，获取数据失败！", Toast.LENGTH_LONG).show();
				System.out.println("内部，用户名或密码错误");
				return ;
			}else if(status==102)
			{
				Toast.makeText(getApplicationContext(), "通讯异常，获取数据失败！", Toast.LENGTH_LONG).show();
				System.out.println("没有接口权限或接口不存在");
				return ;
			}else if(status==103)
			{
				Toast.makeText(getApplicationContext(), "通讯异常，获取数据失败！", Toast.LENGTH_LONG).show();
				System.out.println("用户对该接口的当日访问量用完");
				return ;
			}else
			{
				Toast.makeText(getApplicationContext(), "通讯异常，获取数据失败！", Toast.LENGTH_LONG).show();
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
	/***从网站获取预约数据*/
	class deleteOrder extends RequestImpl{

		public deleteOrder(Context context) {
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
				//Map map=userManagerService.getGjcgz("userManagerService","userManagerService", url_yhxx,userName,null,null,null,null,null);
				Alarm alarm=Alarms.getAlarm(getContentResolver(), id);
				Map map=userManagerService.delGjcgz("userManagerService", "userManagerService", url_yhxx, alarm.orderId);
				return map;
//			 
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
				if(operaStatus==1){
					Alarm alarm=Alarms.getAlarm(getContentResolver(), id);
	                Cursor cursor=Alarms.getSaveAlarm(getContentResolver(), alarm.orderId);
	                if (cursor != null) {
			            if (cursor.moveToFirst()) {
			            	do{
			            		Alarm nextAlarm=Alarms.calculateNextAlert(DeskClockMainActivity.this);
			            		int alarmId=cursor.getInt(Alarm.Columns.ALARM_ID_INDEX);
			            		  if(nextAlarm!=null&&alarmId==nextAlarm.id){
	                              	Alarms.disableAlert(DeskClockMainActivity.this,alarmId);
	                              	Alarms.setNextAlert(DeskClockMainActivity.this);
	                              }
			            		  Alarms.deleteAlarm(DeskClockMainActivity.this, alarmId);
			            	}while(cursor.moveToNext());
			            }
			            cursor.close();
			        }
	                Toast.makeText(getApplicationContext(), "删除成功！", Toast.LENGTH_LONG).show();
				}
				else{
					Toast.makeText(getApplicationContext(), "删除失败！", Toast.LENGTH_LONG).show();
				}
				
			}else if(status==101)
			{
				Toast.makeText(getApplicationContext(), "通讯异常，获取数据失败！", Toast.LENGTH_LONG).show();
				System.out.println("内部，用户名或密码错误");
				return ;
			}else if(status==102)
			{
				Toast.makeText(getApplicationContext(), "通讯异常，获取数据失败！", Toast.LENGTH_LONG).show();
				System.out.println("没有接口权限或接口不存在");
				return ;
			}else if(status==103)
			{
				Toast.makeText(getApplicationContext(), "通讯异常，获取数据失败！", Toast.LENGTH_LONG).show();
				System.out.println("用户对该接口的当日访问量用完");
				return ;
			}else
			{
				Toast.makeText(getApplicationContext(), "通讯异常，获取数据失败！", Toast.LENGTH_LONG).show();
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
        super.onDestroy();
        ToastMaster.cancelToast();
        mCursor.close();
    }

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	switch(v.getId()){
	case R.id.ibtnTitleReturn:
		super.onBackPressed();
		break;
	}
	
}
}