package com.keli.hfbus.ui;

import java.util.Map;

import com.keli.hfbus.HFBusApp;
import com.keli.hfbus.R;
import com.keli.hfbus.alarm.Alarm;
import com.keli.hfbus.alarm.Alarms;
import com.keli.hfbus.hessianserver.Request;
import com.keli.hfbus.hessianserver.RequestImpl;
import com.keli.hfbus.hessianserver.UserManagerService;
import com.keli.hfbus.util.Utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener {
	private ImageButton register_ok, register_cancel;
	private EditText reg_username, reg_phonenumber, reg_password,
			reg_repassword;
	private LinearLayout reg_layOutOffset;
	SharedPreferences settings;

	String un,ph,pw ;
	HFBusApp mApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		mApp = (HFBusApp) getApplication();
		mApp.getmActivityManager().add(this);
		register_ok = (ImageButton) findViewById(R.id.register_ok);
		register_cancel = (ImageButton) findViewById(R.id.register_cancel);
		reg_username = (EditText) findViewById(R.id.reg_username);
		reg_phonenumber = (EditText) findViewById(R.id.reg_phonenumber);
		reg_password = (EditText) findViewById(R.id.reg_password);
		reg_repassword = (EditText) findViewById(R.id.reg_repassword);
		settings =  getSharedPreferences("SETTING_SAVE_PASS_CHECK", 0);

		register_ok.setOnClickListener(this);
		register_cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.register_ok:

			  un = reg_username.getText().toString().trim();
			  ph = reg_phonenumber.getText().toString().trim();
			  pw = reg_password.getText().toString().trim();
			String rp = reg_repassword.getText().toString().trim();

			if (un.equals("") || ph.equals("") || pw.equals("")
					|| rp.equals("")) {
				Toast.makeText(this, "帐号、手机号、密码、确认密码不能为空", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if (!pw.equals(rp)) {
				Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
				return;
			}
			if (Utils.isChinese(un)) {
				Toast.makeText(this, "帐号不能包含汉字", Toast.LENGTH_SHORT).show();
				return;
			}
			if (un.length() < 4) {
				Toast.makeText(this, "帐号长度不低于4个字符", Toast.LENGTH_SHORT).show();
				return;
			}
			if (pw.length() < 6) {
				Toast.makeText(this, "密码长度不低于6个字符", Toast.LENGTH_SHORT).show();
				return;
			}
			if (ph.length()!=11) {
				Toast.makeText(this, "手机号必须是11位", Toast.LENGTH_SHORT).show();
				return;
			}
			
			 Request request = new Request();
				request.query(new Register_callback(this));
			break;

		case R.id.register_cancel:
			  reg_username.setText("");
			  reg_phonenumber.setText("");
			  reg_password.setText("");
			  reg_repassword.setText("");
			break;
		}

	}
	
	class Register_callback extends RequestImpl{

		public Register_callback(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Object doCall() {
			// TODO Auto-generated method stub
			//String url = "http://218.22.27.75:80/Kljtxxfww_1_0_v/hessian/userManagerService";
			UserManagerService userManagerService;
			try {
	 
				userManagerService = (UserManagerService)factory.create(UserManagerService.class, url_yhxx, this.mContext.get().getClassLoader());
				Map map=userManagerService.registerUser("userManagerService", "userManagerService", url_yhxx, un, ph, pw);
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
				
				
				Toast.makeText(getApplicationContext(), "通讯异常！", 

500).show();
				System.out.println("内部，用户名或密码错误");
				return ;
			}else if(status==102)
			{
				Toast.makeText(getApplicationContext(), "通讯异常！", 

500).show();
				System.out.println("没有接口权限或接口不存在");
				return ;
			}else if(status==103)
			{
				Toast.makeText(getApplicationContext(), "通讯异常！", 

500).show();
				System.out.println("用户对该接口的当日访问量用完");
				return ;
			}else
			{
				Toast.makeText(getApplicationContext(), "通讯异常！", 

500).show();
				System.out.println("验证程序异常 稍后重试");
				return ;
			}
			String  operaStatus= map.get("operaStatus").toString();
			 if("0".equals(operaStatus))
			 {
				 Toast.makeText(getApplicationContext(), "注册成功！", 500).show();
				 settings.edit().putBoolean("SAVE_PASS_CHECK", true)
					.putBoolean("SAVE_PASS_AUTOLOGIN", false)
					.putString("SAVE_PASS_JYBH", reg_username.getText().toString())
					.putString("SAVE_PASS_JYMM", reg_password.getText().toString())
					.commit();
					 
				    Cursor cursor = Alarms.getFilteredAlarmsCursor(RegisterActivity.this.getContentResolver());
				    if (cursor != null) {
				    	if (cursor.moveToFirst()){
				    		do{
				    			Alarm alarm = new Alarm(cursor);
					    		 Uri uri = ContentUris.withAppendedId(Alarm.Columns.CONTENT_URI, alarm.id);
					    		 RegisterActivity.this.getContentResolver().delete(uri, "", null);
				    		}while(cursor.moveToNext());
				    	}
				    	cursor.close();
				    }
					setResult(10086);
					RegisterActivity.this.finish();

				 
			 }else if("1".equals(operaStatus))
			 {
				 Toast.makeText(getApplicationContext(), "注册出错！", 500).show();
			 }
			 else if("2".equals(operaStatus))
			 {
				 Toast.makeText(getApplicationContext(), "用户名重复！", 500).show();
			 }
			 else if("3".equals(operaStatus))
			 {
				 Toast.makeText(getApplicationContext(), "手机号重复！", 500).show();
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
