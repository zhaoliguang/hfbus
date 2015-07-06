package com.keli.hfbus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import com.caucho.hessian.client.HessianProxyFactory;
import com.keli.hfbus.util.ActivityManager;
import com.keli.hfbus.util.MyTimeList;
import com.keli.hfbus.util.Utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.telephony.TelephonyManager;

/**
 * Ӧ�ó���ȫ�ֶ���,��Ҫ��Ӧ�ó��������ڼ�ʼ�մ��ڵĶ��󶼷������ﴦ��
 * 
 * 
 */
public class HFBusApp extends Application {
	public static HFBusApp mApp;
	public static String SDCARD_ROOT;
	public static String SAVE_ROOT;
	public static String PHOTO_PATH;
	public static String UPLOAD_PATH;
	
	public static boolean DEBUG;
	private String meid;
	private Properties props;
	public static int ConnectTimeoutInMillis;
	
	ActivityManager mActivityManager =new ActivityManager();

	private static volatile AtomicBoolean isConnected = new AtomicBoolean(false);
	private Calendar []calendarList={null,null,null,null};//�������õ�ʱ��
	private Intent alarmIntent =new Intent();
	private String userName;
	private MyTimeList timeList=new MyTimeList();
	private String[] hessianurls;
	
	public String[] getHessianurls() {
		return hessianurls;
	}

	public void setHessianurls(String[] hessianurls) {
		this.hessianurls = hessianurls;
	}

	public String getVersionCode(){
		try {
			return getPackageManager().getPackageInfo("com.keli.hfbus", 0).versionName+"";
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			return "";
		}
	}
	private HessianProxyFactory factory = null;//hessian����
	public HessianProxyFactory getHessianFactory(){
		if(factory==null){
			factory = new HessianProxyFactory();
			factory.setHessian2Reply(false);
	    	factory.setReadTimeout(50000);
		}
		return factory;
	}
	
	/**
	 * ��ʼ��Ӧ�ó���,�������ʱ,������Ҫ��onCreate������ʹ��
	 */
	public void initApp(final Handler handler){
		
		 new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					// 1�����������ļ�=================== ==========
					props = new Properties();  
				    InputStream in = HFBusApp.class.getResourceAsStream("config.properties");  
				    try {  
				        props.load(in);  
				        in.close();
				     } catch (IOException e) {  
				        e.printStackTrace();  
				    }
//				     handler.sendEmptyMessage(1);
//						sleep(300);
						
				     // 2����ʼ������=============================
				     SDCARD_ROOT = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
				     DEBUG = props.getProperty("IS_DEBUG").trim().toLowerCase().equals("true")?true:false;
					    
					 String timeout = props.getProperty("ConnectTimeoutInMillis");
					 ConnectTimeoutInMillis = Integer.valueOf(timeout);
					 SAVE_ROOT = props.getProperty("SAVE_ROOT");
					 PHOTO_PATH = props.getProperty("PHOTO_PATH");
					 UPLOAD_PATH = props.getProperty("UPLOAD_PATH");
					 // ����hessian url��ַ��
					 hessianurls = new String[3];
					 hessianurls[0] = props.getProperty("HessianUrl2");
					 hessianurls[1] = props.getProperty("HessianUrl3");
					 hessianurls[2] = props.getProperty("HessianUrl4");
//					 handler.sendEmptyMessage(2);
//						sleep(500);
						
					// 3������ϵͳ����=============================
//						handler.sendEmptyMessage(3);
//						sleep(450);
						
				    // 4����ʼ�����ݿ�============================= 
//					    new getRttThread().start();
//						new getRoadThread().start();
//						new initRttBasicDataThread().start();
//						sleep(400);
						
					// 5����ȡ�ֻ�������Ϣ=============================
						genMEID();
						
//						handler.sendEmptyMessage(5);
//						sleep(200);
						
					// 6������ϵͳ�ļ�Ŀ¼=============================
						if(Utils.isHasSdcard()){
							
							System.out.println("create directory...");
							// ������Ŀ¼
						    File destDir = new File(SDCARD_ROOT+SAVE_ROOT);
						    if (!destDir.exists()) {
						        destDir.mkdirs();
						    }
						    // ����.nomedia�ļ���
						    File nomedia = new File(SDCARD_ROOT+SAVE_ROOT+".nomedia/");
						    if (!nomedia.exists()) {
						    	nomedia.mkdirs();
						    }
						    // ������Ƭ�ļ���
						    File portraitDir = new File(SDCARD_ROOT+SAVE_ROOT+PHOTO_PATH);
						    if (!portraitDir.exists()) {
						    	portraitDir.mkdirs();
						    }
						    // �����ϴ��ļ���
						    File uploadDir = new File(SDCARD_ROOT+SAVE_ROOT+UPLOAD_PATH);
						    if (!uploadDir.exists()) {
						    	uploadDir.mkdirs();
						    }
						}
						
//						handler.sendEmptyMessage(6);
//						sleep(500);
						
					// 7�����������������=============================
//						handler.sendEmptyMessage(7);
//						sleep(320);
//						startMinaClient();
					
					// 8�����
//						handler.sendEmptyMessage(8);
//						sleep(200);
					// 9�������¼����
//						handler.sendEmptyMessage(9);
						
						//new getRttThread().start();
					 
				}catch(Exception e){
					System.out.println("initApp Error:"+e.getMessage());
				}
			}
			 
		 }.start();
	    
	}
	private void genMEID(){
		TelephonyManager tm = (TelephonyManager)this.
		getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		meid = tm.getSubscriberId();
		System.out.println("this serialNumber is "+tm.getSubscriberId());
	}
	public MyTimeList getTimeList() {
		return timeList;
	}

	public void setTimeList(MyTimeList timeList) {
		this.timeList = timeList;
	}

	public ActivityManager getmActivityManager() {
		return mActivityManager;
	}

	public void setmActivityManager(ActivityManager mActivityManager) {
		this.mActivityManager = mActivityManager;
	}
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Intent getAlarmIntent() {
		return alarmIntent;
	}

	public void setAlarmIntent(Intent alarmIntent) {
		this.alarmIntent = alarmIntent;
	}


	public Calendar[] getCalendarList() {
		return calendarList;
	}

	public void setCalendarList(Calendar[] calendarList) {
		this.calendarList = calendarList;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		mApp = this;
	 
		super.onCreate();
	}
	public void exit(){
		// ��������
        android.os.Process.killProcess(android.os.Process.myPid());
	}
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}
}
