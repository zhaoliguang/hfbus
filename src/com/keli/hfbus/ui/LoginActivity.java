package com.keli.hfbus.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.keli.hfbus.HFBusApp;
import com.keli.hfbus.HFBusMain;
import com.keli.hfbus.R;
import com.keli.hfbus.hessianserver.GjcxService;
import com.keli.hfbus.hessianserver.Request;
import com.keli.hfbus.hessianserver.RequestImpl;
import com.keli.hfbus.hessianserver.UserManagerService;
import com.keli.hfbus.sqlite.BusDal;
import com.keli.hfbus.ui.RegisterActivity;
import com.keli.hfbus.util.DialogPrompt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class LoginActivity extends Activity implements OnClickListener {
	ImageButton iBtnLogin, iBtnRegister;
	EditText etAccount, etPassword;
	SharedPreferences settings;
	CheckBox cbSavepwd, cbAutologin;
	HFBusApp mApp;
	BusDal busDal;
	String userName,passWord;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mApp=(HFBusApp) getApplicationContext();
		mApp.getmActivityManager().add(this);
		busDal=new BusDal(this);
		iBtnLogin = (ImageButton) findViewById(R.id.ibtn_login);
		iBtnRegister = (ImageButton) findViewById(R.id.ibtn_register);
		settings =  getSharedPreferences("SETTING_SAVE_PASS_CHECK", 0);
		etAccount = (EditText) findViewById(R.id.etAccount);
		etPassword = (EditText) findViewById(R.id.etPassword);
		cbSavepwd = (CheckBox) findViewById(R.id.cbSavepwd);
		cbAutologin = (CheckBox) findViewById(R.id.cbAutologin);

		iBtnLogin.setOnClickListener(this);
		iBtnRegister.setOnClickListener(this);
		cbAutologin.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				 if (isChecked == true) {
					cbSavepwd.setChecked(true);

				}

			}

		});
		boolean isChecked = settings.getBoolean("SAVE_PASS_CHECK", false);
		boolean isAutologin = settings.getBoolean("SAVE_PASS_AUTOLOGIN", false);
		if (isChecked) {
			String jybh = settings.getString("SAVE_PASS_JYBH", "");
			String jymm = settings.getString("SAVE_PASS_JYMM", "");
			etAccount.setText(jybh);
			etPassword.setText(jymm);
			etAccount.setSelection(etAccount.length());
			cbSavepwd.setChecked(true);
            
//			if (isAutologin) {
//				cbAutologin.setChecked(true);
//				Toast.makeText(getApplicationContext(),
//						"自动登录！", 500).show();
//				login();
//			}
		}
	}
	private void login()
	{
		//记住用户名、密码、自动登录
		settings.edit().putBoolean("SAVE_PASS_CHECK", cbSavepwd.isChecked())
		.putBoolean("SAVE_PASS_AUTOLOGIN", cbAutologin.isChecked())
		.putString("SAVE_PASS_JYBH", etAccount.getText().toString())
		.putString("SAVE_PASS_JYMM", etPassword.getText().toString())
		.commit();
		userName=etAccount.getText().toString();
		passWord=etPassword.getText().toString();
		 Request request = new Request();
			request.query(new Login_callback(this));

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode==10086)
		{
			System.out.println("Result 登录");
			String jybh = settings.getString("SAVE_PASS_JYBH", "");
			String jymm = settings.getString("SAVE_PASS_JYMM", "");
			etAccount.setText(jybh);
			etPassword.setText(jymm);
			etAccount.setSelection(etAccount.length());
			cbSavepwd.setChecked(true);
			login();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.ibtn_login:// 登录
			 etAccount.getText().toString();
			  if("".equals( etAccount.getText().toString()) 
					  ||"".equals( etPassword.getText().toString()))
			  { Toast.makeText(getApplicationContext(), "用户名或密码不能为空！", 500).show();
			      break;
			  }
			  
			 login();
			break;
		case R.id.ibtn_register:// 注册
			Intent it=new Intent();
			it.setClass(LoginActivity.this, RegisterActivity.class);
			startActivityForResult(it, 1);

			break;
		}
	}
	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if(keyCode == KeyEvent.KEYCODE_BACK){
				exit();
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
		void exit(){
	    	Dialog dialog = null;
	    	DialogPrompt.Builder customBuilder = new DialogPrompt.Builder(
					this);
			
			customBuilder
					.setMessage("确定要退出系统吗?")
					.setNegativeButton("稍后再说",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
									dialog = null;
								}
							})
					.setPositiveButton("确定退出",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									
									dialog.cancel();
									dialog = null;
									LoginActivity.this.finish();
									HFBusApp.mApp.exit();
								}
							});
			dialog = customBuilder.create();
			dialog.show();
	    }
	
	class Login_callback extends RequestImpl{

		public Login_callback(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Object doCall() {
			// TODO Auto-generated method stub
			//String url = "http://218.22.27.75:80/Kljtxxfww_1_0_v/hessian/userManagerService";
			//String getStationUrl = "http://218.22.27.69:9080/Kljtxxfww_1_0_v/hessian/gjcxService";
			UserManagerService userManagerService;
			GjcxService mGjcxService;
			try {
				
				
				userManagerService = (UserManagerService) 
						factory.create(UserManagerService.class, url_yhxx, this.mContext.get()
								.getClassLoader());
				Map map=userManagerService
						.loginUser ("userManagerService", "userManagerService", url_yhxx,  userName, passWord);
				
				
//				mGjcxService = (GjcxService) factory.create(GjcxService.class,url_gjcx, this.mContext.get().getClassLoader());
//				Map stationMap = mGjcxService.getStationList();
//				Cursor cursor=busDal.getStation();
//				if(cursor.getCount()==0){
//					if(stationMap!=null){
//						List stationList=(ArrayList)stationMap.get("list");
//						if(stationList!=null){
//							busDal.updateStationName(stationList);
//						}
//					}
//				}
				
				
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
			Map map=(Map) o;
			System.out.println("map"+map);
			int status=Integer.parseInt( map.get("status").toString()) ;
			 
			if(status==100){
				
			}else if(status==101)
			{
				
				
				Toast.makeText(getApplicationContext(), "通讯异常！", 500).show();
				System.out.println("内部，用户名或密码错误");
				return ;
			}else if(status==102)
			{
				Toast.makeText(getApplicationContext(), "通讯异常！", 500).show();
				System.out.println("没有接口权限或接口不存在");
				return ;
			}else if(status==103)
			{
				Toast.makeText(getApplicationContext(), "通讯异常！", 500).show();
				System.out.println("用户对该接口的当日访问量用完");
				return ;
			}else
			{
				Toast.makeText(getApplicationContext(), "通讯异常！", 500).show();
				System.out.println("验证程序异常 稍后重试");
				return ;
			}
			String  operaStatus= map.get("operaStatus").toString();
			// 0 登录成功 1 登录出错 2 帐号或密码错误 3 帐号未激活
			 if("0".equals(operaStatus))
			 {
				 mApp.setUserName(userName);//记录全局变量 ，用户名
				 System.out.println("登录成功！");
					Intent it=new Intent();
					it.setClass(LoginActivity.this, HFBusMain.class);
					startActivity(it);
					
					finish();
			 }else if("1".equals(operaStatus))
			 {
				 Toast.makeText(getApplicationContext(), "登录出错！", 500).show();
			 }
			 else if("2".equals(operaStatus))
			 {
				 Toast.makeText(getApplicationContext(), "帐号或密码错误！", 500).show();
			 }
			 else if("3".equals(operaStatus))
			 {
				 Toast.makeText(getApplicationContext(), " 帐号未激活！", 500).show();
			 }
			super.doComplete(o);
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
	    
	}
}
