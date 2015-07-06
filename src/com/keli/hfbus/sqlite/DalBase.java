package com.keli.hfbus.sqlite;

import java.lang.ref.WeakReference;

import android.content.Context;

/**
 * 数据访问对象基类
 *
 */
public abstract class DalBase {
	protected dbhelp dbHelper;
	protected String sdPath;
	protected WeakReference<Context> context;
	
	public DalBase(Context context,dbEnum dbtype){
		this.context = new WeakReference<Context>(context);
		
		// 数据库放到sdcard中
	    // sdPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
		String dbname = dbtype == dbEnum.BUSINESS ? "data.db3" :
			"data.db3";
		// 
		// dbHelper = new dbhelp(this.context,sdPath+dbConst.SAVE_PATH_IN_SDCARD,dbname);
		// 数据存储在应用程序私有目录  /data/data/package/
		dbHelper = new dbhelp(this.context.get(),"",dbname);
	}
	
	/**
	 * 关闭数据库
	 */
	public void close(){
		if(dbHelper != null){
			dbHelper.close();
		}
	} 
}
