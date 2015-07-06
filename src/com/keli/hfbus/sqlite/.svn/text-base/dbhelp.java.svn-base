package com.keli.hfbus.sqlite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 数据库操作对象
 * @author shaolong
 * @date 2012-08-31 edit
 */
public class dbhelp {
	
	/** 路径,存储在sdcard时使用,否则为空*/
	private String DATABASE_PATH = "";
	
	/** 文件名全称如"db.db3",不包括路径  */
	private String DATABASE_FILENAME = "";
	
	/** 路径+文件名 */
	private String DATABASE_CONNSTRING = "";
	
	/** 文件名 不包括路径和扩展名 如"db" */
	String fileName = "";
	
	WeakReference<Context> context;
	SQLiteDatabase database;

	/**
	 * 设置数据库文件路径
	 * 
	 * @param context 
	 * 
	 * @param path
	 *            路径 为空表示数据库安装在应用程序私有目录  /data/data/package/ 否则安装在sdcrad 
	 * @param filename
	 *            文件名 数据库文件全名如taxi.db3
	 */
	public dbhelp(Context context,String path, String filename) {
		this.context = new WeakReference<Context>(context);
		this.DATABASE_PATH = path;
		this.DATABASE_FILENAME = filename;
		this.DATABASE_CONNSTRING = DATABASE_PATH+DATABASE_FILENAME;
		
		
		int pos = this.DATABASE_FILENAME.lastIndexOf(".");
		if(pos != -1){
			this.fileName = this.DATABASE_FILENAME.substring(0, pos);
		}else{
			this.fileName = this.DATABASE_FILENAME;
		}
	}

	/**
	 * 关闭数据库释放内存
	 */
	public void close(){
		if(database != null && database.isOpen()){
		    database.close();
		    database.releaseMemory();
	    }
	}
	

	/**
	 * 打开数据库 存储在sdcard
	 * @return
	 */
	public SQLiteDatabase getDatabaseSDCard() {
		try {
			if(database != null && database.isOpen())return database;
			String databaseFilename = DATABASE_CONNSTRING;
			// 没有这个数据库文件
			if (!(new File(databaseFilename)).exists()) {
				try{
					//获得文件夹路径
				    File destDir = new File(DATABASE_PATH);
				    if (!destDir.exists()) {
				        destDir.mkdirs();
				    }
				}
				catch(Exception e)
				{
					Log.e("dbhelp.getDatabase", "Error：" + e.getMessage());
				}
				int dbid = context.get().getResources().
				getIdentifier(fileName,"raw",context.get().getPackageName());
                //复制到指定目录
				int res = dbid;
				
				InputStream is = context.get().getResources().openRawResource(res);   
                FileOutputStream fos = new FileOutputStream(databaseFilename);   
                byte[] buffer = new byte[7168];   
                int count = 0;   
                // 开始复制dictionary.db文件   
                while ((count = is.read(buffer)) > 0)   
                {   
                    fos.write(buffer, 0, count);   
                }   
                fos.close();   
                is.close();
				
			}

		    database = SQLiteDatabase.openOrCreateDatabase(
					databaseFilename, null);
			return database;
		} catch (Exception e) {
			Log.e("dbhelp.getDatabase", "Error：" + e.getMessage());
		}
		return null;
	}
	

	/**
	 * 打开数据库 存储在私有区域 /data/data/package/
	 * @return
	 */
	public SQLiteDatabase getDatabase() {
		try {
			if(database != null && database.isOpen())return database;
			
			// 文件路径不空 表示存储在sd卡中
			if(!this.DATABASE_PATH.equals(""))return getDatabaseSDCard();
			
			// 没有这个数据库文件
			String [] files = context.get().fileList();
			boolean isexsit = false;
			for(int i=0;i<files.length;i++){
				if(files[i].trim().equals(DATABASE_FILENAME.trim())){
					isexsit = true;
					break;
				}
			}
			if (!isexsit) {

				int dbid = context.get().getResources().
				getIdentifier(fileName,"raw",context.get().getPackageName());
                //复制到指定目录
				int res = dbid;
				
				InputStream is = context.get().getResources().openRawResource(res);   
                FileOutputStream fos = context.get().openFileOutput(DATABASE_FILENAME, Context.MODE_PRIVATE); 
                byte[] buffer = new byte[7168];   
                int count = 0;   
                // 开始复制dictionary.db文件   
                while ((count = is.read(buffer)) > 0)   
                {   
                    fos.write(buffer, 0, count);   
                }   
                fos.close();   
                is.close();
				
			}
		    database = SQLiteDatabase.openOrCreateDatabase(
		    		context.get().getFileStreamPath(DATABASE_FILENAME).getAbsolutePath(), null);
			return database;
		} catch (Exception e) {
			Log.e("dbhelp.getDatabase", "Error：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 执行SQL语句,返回影响行数
	 * @param cmdText
	 * @return
	 */
	public int ExecuteNonQuery(String cmdText) {

		return ExecuteNonQuery(getDatabase(), cmdText, null);
	}
    
	/**
	 * 执行SQL语句,返回影响行数
	 * @param cmdText
	 * @param selectionArgs
	 * @return
	 */
	public int ExecuteNonQuery(String cmdText,String[] selectionArgs) {

		return ExecuteNonQuery(getDatabase(), cmdText, selectionArgs);
	}
    /**
     * 执行SQL语句,返回影响行数
     * @param database
     * @param cmdText
     * @return
     */
	public int ExecuteNonQuery(SQLiteDatabase database, String cmdText) {

		return ExecuteNonQuery(database, cmdText, null);

	}
    /**
     * 执行SQL语句,返回影响行数
     * @param database
     * @param cmdText SQL查询语句
     * @param selectionArgs SQL参数集合
     * @return
     */
	public int ExecuteNonQuery(SQLiteDatabase database, String cmdText,
			String[] selectionArgs) {
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(cmdText, selectionArgs);
			return cursor.getCount();
		} catch (Exception e) {
			return -1;
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
	}

	/**
	 * 执行SQL语句,返回游标
	 * @param cmdText
	 * @param selectionArgs
	 * @return
	 */
	public Cursor ExecuteCursor(String cmdText,
			String[] selectionArgs) {
		return ExecuteCursor(getDatabase(), cmdText, selectionArgs);
	}
    /**
     * 执行SQL语句,返回游标
     * @param database
     * @param cmdText
     * @return
     */
	public Cursor ExecuteCursor(SQLiteDatabase database, String cmdText) {
		return ExecuteCursor(database, cmdText, null);
	}
	/**
	 * 执行SQL语句,返回游标
	 * @param cmdText
	 * @return
	 */
	public Cursor ExecuteCursor(String cmdText) {
		return ExecuteCursor(getDatabase(), cmdText, null);
	}
    /**
     * 执行SQL语句,返回游标
     * @param database
     * @param cmdText
     * @param selectionArgs
     * @return
     */
	public Cursor ExecuteCursor(SQLiteDatabase database, String cmdText,
			String[] selectionArgs) {
		Cursor cursor = database.rawQuery(cmdText, selectionArgs);
		return cursor;
	}

	/**
	 * 执行SQL语句,返回List<Map>
	 * @param cmdText
	 * @param selectionArgs
	 * @return
	 */
	public List<Map<String, Object>> ExecuteList(
			String cmdText,String[] selectionArgs) {
		return ExecuteList(getDatabase(),cmdText,selectionArgs);
	}
	/**
	 * 执行SQL语句,返回List<Map>
	 * @param database
	 * @param cmdText
	 * @return
	 */
	public List<Map<String, Object>> ExecuteList(
			SQLiteDatabase database, String cmdText) {
		return ExecuteList(database,cmdText,null);
	}
	/**
	 * 执行SQL语句,返回List<Map>
	 * @param cmdText
	 * @return
	 */
	public List<Map<String, Object>> ExecuteList(String cmdText) {
		 
		return ExecuteList(getDatabase(),cmdText,null);
	}
	/**
	 * 执行SQL语句,返回List<Map>  查询语句 返回数据集
	 * @param database
	 * @param cmdText SQL查询语句
	 * @param selectionArgs SQL参数数组
	 * @return
	 */
	public List<Map<String, Object>> ExecuteList(
			SQLiteDatabase database, String cmdText, String[] selectionArgs) {
//		Cursor cursor = database.rawQuery(cmdText, selectionArgs);
//		return arraylist.loadAllList(cursor);
		Cursor cursor = null;
		try {
			cursor = database.rawQuery(cmdText, selectionArgs);
			return loadAllList(cursor);
		} catch (Exception e) {
			return null;
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
	}
	public List<Map<String, Object>> loadAllList(Cursor cursor) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

		Map<String, Object> item;

		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				item = new HashMap<String, Object>();
				for (int j = 0; j < cursor.getColumnCount(); j++) {
					item.put(cursor.getColumnName(j), cursor.getString(j)==null?"":cursor.getString(j));
				}
				data.add(item);
				cursor.moveToNext();
			}
		}
		cursor.deactivate();
		cursor.close();
		cursor= null;
		
		return data;
	}
	public void ExcuteSql(String cmdText,String []Params){
		if(cmdText!=null){
			database=getDatabase();
			database.execSQL(cmdText, Params);
		}
	}
	public void ExcuteSql(String cmdText){
		if(cmdText!=null){
			database=getDatabase();
			database.execSQL(cmdText);
		}
	}
	}

