package com.keli.hfbus.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.keli.hfbus.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Utils {
	
	/**MapABC 地图x方向偏移量 越大越靠右*/
	public static final double X_OFFSET = 0.00541;//0.00541
	/**MapABC 地图Y方向偏移量 越小越靠下*/
	public static final double Y_OFFSET = -0.0019;//0.0019

	public static boolean isWifiActive(Context icontext) {
		Context context = icontext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info;
		if (connectivity != null) {
			info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI")
							&& info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static String getWifiMac(Context icontext){
		WifiManager wifi = (WifiManager) icontext.getSystemService(Context.WIFI_SERVICE); 
		WifiInfo info = wifi.getConnectionInfo(); 
		return info.getMacAddress();
	}
	
	public static String dateFormat(String datetime){
		try{
    		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Date d = f.parse(datetime);
			return f.format(d);
    	}catch(Exception e){
    		return datetime;
    	}
	}
	
	public static void toastAlert(Context context,String msg){
    	Toast.makeText(context, msg,Toast.LENGTH_SHORT).show();
    }
	
	public static String formatTime() {
		java.util.Date d = new java.util.Date();
		java.text.SimpleDateFormat f = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String s = f.format(d);
		return s;
	}
	
	public static String formatTime(int x)
    {
	    String s=""+x;
	    if(s.length()==1) s="0"+s;
	    return s;
	}
	public static void setListSpinner(Context context,
			List<Map<String, Object>> list, Spinner sp, String textColumn,
			String valueColumn) {
		if (list == null)
			return;
		try {
			String[] text = new String[list.size()];
			String[] value = new String[list.size()];
			Iterator<Map<String, Object>> iterator = list.listIterator();
			int i = 0;
			while (iterator.hasNext()) {
				Map<String, Object> obj = iterator.next();
				value[i] = obj.get(valueColumn).toString();
				text[i] = obj.get(textColumn).toString();
				i++;
			}
			ArrayAdapter<CharSequence> adapter = new ArrayAdapter(context,
					android.R.layout.simple_spinner_item, text);
			adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
			sp.setAdapter(adapter);
		} catch (Exception ex) {
		}
	}
	/**
	 * 检查存储卡是否插入
	 * 
	 * @return
	 */
	public static boolean isHasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串是否包含汉字
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str){
		return str.getBytes().length == str.length() ? false:true;
	}
}
