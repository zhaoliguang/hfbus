package com.keli.hfbus.sqlite;

import java.lang.ref.WeakReference;

import android.content.Context;

/**
 * ���ݷ��ʶ������
 *
 */
public abstract class DalBase {
	protected dbhelp dbHelper;
	protected String sdPath;
	protected WeakReference<Context> context;
	
	public DalBase(Context context,dbEnum dbtype){
		this.context = new WeakReference<Context>(context);
		
		// ���ݿ�ŵ�sdcard��
	    // sdPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
		String dbname = dbtype == dbEnum.BUSINESS ? "data.db3" :
			"data.db3";
		// 
		// dbHelper = new dbhelp(this.context,sdPath+dbConst.SAVE_PATH_IN_SDCARD,dbname);
		// ���ݴ洢��Ӧ�ó���˽��Ŀ¼  /data/data/package/
		dbHelper = new dbhelp(this.context.get(),"",dbname);
	}
	
	/**
	 * �ر����ݿ�
	 */
	public void close(){
		if(dbHelper != null){
			dbHelper.close();
		}
	} 
}
