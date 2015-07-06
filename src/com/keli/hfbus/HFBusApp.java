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
 * 应用程序全局对象,需要在应用程序运行期间始终存在的对象都放在这里处理
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
	private Calendar []calendarList={null,null,null,null};//保存设置的时间
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
	private HessianProxyFactory factory = null;//hessian工厂
	public HessianProxyFactory getHessianFactory(){
		if(factory==null){
			factory = new HessianProxyFactory();
			factory.setHessian2Reply(false);
	    	factory.setReadTimeout(50000);
		}
		return factory;
	}
	
	/**
	 * 初始化应用程序,此任务耗时,尽量不要在onCreate方法里使用
	 */
	public void initApp(final Handler handler){
		
		 new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					// 1、加载配置文件=================== ==========
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
						
				     // 2、初始化参数=============================
				     SDCARD_ROOT = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
				     DEBUG = props.getProperty("IS_DEBUG").trim().toLowerCase().equals("true")?true:false;
					    
					 String timeout = props.getProperty("ConnectTimeoutInMillis");
					 ConnectTimeoutInMillis = Integer.valueOf(timeout);
					 SAVE_ROOT = props.getProperty("SAVE_ROOT");
					 PHOTO_PATH = props.getProperty("PHOTO_PATH");
					 UPLOAD_PATH = props.getProperty("UPLOAD_PATH");
					 // 配置hessian url地址集
					 hessianurls = new String[3];
					 hessianurls[0] = props.getProperty("HessianUrl2");
					 hessianurls[1] = props.getProperty("HessianUrl3");
					 hessianurls[2] = props.getProperty("HessianUrl4");
//					 handler.sendEmptyMessage(2);
//						sleep(500);
						
					// 3、创建系统缓存=============================
//						handler.sendEmptyMessage(3);
//						sleep(450);
						
				    // 4、初始化数据库============================= 
//					    new getRttThread().start();
//						new getRoadThread().start();
//						new initRttBasicDataThread().start();
//						sleep(400);
						
					// 5、获取手机配置信息=============================
						genMEID();
						
//						handler.sendEmptyMessage(5);
//						sleep(200);
						
					// 6、创建系统文件目录=============================
						if(Utils.isHasSdcard()){
							
							System.out.println("create directory...");
							// 创建根目录
						    File destDir = new File(SDCARD_ROOT+SAVE_ROOT);
						    if (!destDir.exists()) {
						        destDir.mkdirs();
						    }
						    // 创建.nomedia文件夹
						    File nomedia = new File(SDCARD_ROOT+SAVE_ROOT+".nomedia/");
						    if (!nomedia.exists()) {
						    	nomedia.mkdirs();
						    }
						    // 创建照片文件夹
						    File portraitDir = new File(SDCARD_ROOT+SAVE_ROOT+PHOTO_PATH);
						    if (!portraitDir.exists()) {
						    	portraitDir.mkdirs();
						    }
						    // 创建上传文件夹
						    File uploadDir = new File(SDCARD_ROOT+SAVE_ROOT+UPLOAD_PATH);
						    if (!uploadDir.exists()) {
						    	uploadDir.mkdirs();
						    }
						}
						
//						handler.sendEmptyMessage(6);
//						sleep(500);
						
					// 7、检测网络连接配置=============================
//						handler.sendEmptyMessage(7);
//						sleep(320);
//						startMinaClient();
					
					// 8、完成
//						handler.sendEmptyMessage(8);
//						sleep(200);
					// 9、进入登录界面
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
		// 结束程序
        android.os.Process.killProcess(android.os.Process.myPid());
	}
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}
}
