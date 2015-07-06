package com.keli.hfbus;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.keli.hfbus.alarm.DeskClockMainActivity;
import com.keli.hfbus.hessianserver.GjcxService;
import com.keli.hfbus.hessianserver.JgywService;
import com.keli.hfbus.hessianserver.Request;
import com.keli.hfbus.hessianserver.RequestImpl;
import com.keli.hfbus.hessianserver.UserManagerService;
import com.keli.hfbus.ui.ChangeBusActivity;
import com.keli.hfbus.ui.LinerQueryAcitvity;
import com.keli.hfbus.ui.OrderPromptActivity;
import com.keli.hfbus.ui.OutPrompt;
import com.keli.hfbus.ui.StationQueryActivity;
import com.keli.hfbus.ui.SetOrderActivity;
import com.keli.hfbus.util.DialogPrompt;
import com.keli.hfbus.util.Downloader.OnDownloadListener;
import com.keli.hfbus.util.Utils;
import com.keli.hfbus.util.Downloader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

public class HFBusMain extends Activity implements OnClickListener{
     ImageButton ibtn_liner_query,ibtn_station_query
     ,ibtn_out_prompt,ibtn_arrive_prompt,ibtn_change_query,ibtn_system_info;
     private HFBusApp mApp;
     ProgressDialog progressBar;
 	 String apkPath;
 	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
				
        setContentView(R.layout.main);
        mApp = (HFBusApp) getApplication();
        mApp.getmActivityManager().add(this);
    	
        ibtn_liner_query=(ImageButton) findViewById(R.id.ibtn_liner_query);
        ibtn_liner_query.setOnClickListener(this );
        ibtn_station_query=(ImageButton) findViewById(R.id.ibtn_station_query);
        ibtn_station_query.setOnClickListener(this );
        ibtn_out_prompt=(ImageButton) findViewById(R.id.ibtn_out_prompt);
        ibtn_out_prompt.setOnClickListener(this );
        ibtn_arrive_prompt=(ImageButton) findViewById(R.id.ibtn_arrive_prompt);
        ibtn_arrive_prompt.setOnClickListener(this );
        ibtn_change_query=(ImageButton) findViewById(R.id.ibtn_change_query);
        ibtn_change_query.setOnClickListener(this );
        
        
        progressBar = new ProgressDialog(this);
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 水平进度条
		progressBar.setMax(100);
     // 检查更新
        new Request().query(new r_update(this));
         
    }

	@Override
	public void onClick(View v) {
		Intent it=new Intent();
	 
		switch(v.getId())
		{
		
		case R.id.ibtn_liner_query://线路查询
			it.setClass(HFBusMain.this, LinerQueryAcitvity.class);
			startActivity(it);
			break;
		case R.id.ibtn_station_query://站点查询
		 
			it.setClass(HFBusMain.this, StationQueryActivity.class);
			startActivity(it);
			break;
		
		case R.id.ibtn_out_prompt://出行提示
			it.setClass(HFBusMain.this, OutPrompt.class);
			startActivity(it);
			break;
		case R.id.ibtn_arrive_prompt://预约提醒
			 it.setClass(HFBusMain.this,DeskClockMainActivity.class);
			 startActivity(it);
			break;
		case R.id.ibtn_change_query://公交换乘
			 it.setClass(HFBusMain.this, ChangeBusActivity.class);
			 startActivity(it);
			break;
		
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		exit();
	}
	
	void exit(){
		new AlertDialog.Builder(this).setTitle("提示").setMessage("确认退出吗？")
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}

		})
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				HFBusMain.this.finish();
				android.os.Process.killProcess(android.os.Process.myPid());
			}

		}).show();
	}
	/***自动更新*/
	class r_update extends RequestImpl{

		String oldversion = "";
		String newversion = "";
		String surl = "";
		
		public r_update(Context context) {
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
 				oldversion = mApp.getVersionCode();
				userManagerService = (UserManagerService) factory.create(UserManagerService.class, url_yhxx, this.mContext.get().getClassLoader());
				Map map=userManagerService.getAutoUpdateInfo ("userManagerService", "userManagerService", url_yhxx,  "1", 
						oldversion);
				 
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
			
			Map map=(Map) o;
			System.out.println("map"+map);
			int status=Integer.parseInt( map.get("status").toString()) ;
			 
			if(status==100){
				
			}else if(status==101)
			{
				Toast.makeText(getApplicationContext(), "通讯异常！", Toast.LENGTH_LONG).show();
				System.out.println("内部，用户名或密码错误");
				return ;
			}else if(status==102)
			{
				Toast.makeText(getApplicationContext(), "通讯异常！", Toast.LENGTH_LONG).show();
				System.out.println("没有接口权限或接口不存在");
				return ;
			}else if(status==103)
			{
				Toast.makeText(getApplicationContext(), "通讯异常！", Toast.LENGTH_LONG).show();
				System.out.println("用户对该接口的当日访问量用完");
				return ;
			}else
			{
				Toast.makeText(getApplicationContext(), "通讯异常！", Toast.LENGTH_LONG).show();
				System.out.println("验证程序异常 稍后重试");
				return ;
			}
			
			newversion = map.get("bbh")==null? "":map.get("bbh").toString();
			surl = map.get("addr")==null? "":map.get("addr").toString();
			
			if("".equals(newversion) || "".equals(surl)){
				return;
			}
            
			newversion = newversion.replace(".", "");
			oldversion = oldversion.replace(".", "");
			System.out.println("newversion:"+newversion);
			System.out.println("oldversion:"+oldversion);
			// 注意 这里保存路径写你的项目的根目录
			apkPath = mApp.SDCARD_ROOT+mApp.SAVE_ROOT+"update.apk";
			
			if (Integer.valueOf(newversion)>Integer.valueOf(oldversion)) {
				// 提示更新---------------------------------------------------
				DialogPrompt dialog;
				DialogPrompt.Builder customBuilder = new DialogPrompt.Builder(
						HFBusMain.this);
				
				customBuilder
						.setMessage("系统检测到有可用的更新,\n是否马上更新程序?")
						.setNegativeButton("以后再说",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
										dialog = null;
										
									}
								})
						.setPositiveButton("立即更新",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										
										dialog.cancel();
										dialog = null;
										
										download(surl);
									}
								});
				dialog = customBuilder.create();
				dialog.show();
				//-----------------------------------------------------------------
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
	
	/**下载文件*/
	void download(String url){
		if(!Utils.isHasSdcard()){
			
			Toast.makeText(getApplicationContext(), "存储设备没有正确加载,无法下载更新!", Toast.LENGTH_SHORT).show();
			return;
		}
		// 从网络下载安装包
		Downloader download = new Downloader(this, url, apkPath, 1);
		download.isReturnByteData = false;
		download.setOnDownloadListener(new OnDownloadListener() {

			@Override
			public void onDownloadCompleted(byte[] data) {
				// TODO Auto-generated method stub
				progressBar.dismiss();
				// Toast.makeText(mContext, "下载完成", Toast.LENGTH_SHORT).show();
				install();
			}

			@Override
			public void onDownloadError(Exception e) {
				// TODO Auto-generated method stub
				progressBar.dismiss();
				Toast.makeText(getApplicationContext(), "抱歉,下载更新出错!", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onDownloadProgress(int size, int completed) {
				// TODO Auto-generated method stub

				// 当收到更新视图消息时，计算已完成下载百分比，同时更新进度条信息
				int progress = 0;
				if (size > 0) {
					progress = (Double.valueOf((completed * 1.0 / size * 100))).intValue();
					progressBar.setProgress(progress);
				}
				progressBar.setMessage("文件大小: " + Downloader.GetRealSize(size) + "\n已下载: " 
						+ Downloader.GetRealSize(completed));
				if (!progressBar.isShowing())progressBar.show();
			}

		});
		
		download.download();
	}
	
	/**安装更新*/
	void install() {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(apkPath)),
				"application/vnd.android.package-archive");
		startActivity(intent);
		
    	
    	// 删除数据库 以更新   注意：这里写你的项目的数据库名
    	File f1 = getFileStreamPath("data.db3");
        if(f1.exists())f1.delete();
        
        this.finish();
        
        // 退出所有界面
//        app.getActivityManager().popAllActivityExceptOne(mContext.getClass());
        
        // 结束程序
        android.os.Process.killProcess(android.os.Process.myPid());

	}
}