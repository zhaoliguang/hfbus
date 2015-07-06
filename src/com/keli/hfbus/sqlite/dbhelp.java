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
 * ���ݿ��������
 * @author shaolong
 * @date 2012-08-31 edit
 */
public class dbhelp {
	
	/** ·��,�洢��sdcardʱʹ��,����Ϊ��*/
	private String DATABASE_PATH = "";
	
	/** �ļ���ȫ����"db.db3",������·��  */
	private String DATABASE_FILENAME = "";
	
	/** ·��+�ļ��� */
	private String DATABASE_CONNSTRING = "";
	
	/** �ļ��� ������·������չ�� ��"db" */
	String fileName = "";
	
	WeakReference<Context> context;
	SQLiteDatabase database;

	/**
	 * �������ݿ��ļ�·��
	 * 
	 * @param context 
	 * 
	 * @param path
	 *            ·�� Ϊ�ձ�ʾ���ݿⰲװ��Ӧ�ó���˽��Ŀ¼  /data/data/package/ ����װ��sdcrad 
	 * @param filename
	 *            �ļ��� ���ݿ��ļ�ȫ����taxi.db3
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
	 * �ر����ݿ��ͷ��ڴ�
	 */
	public void close(){
		if(database != null && database.isOpen()){
		    database.close();
		    database.releaseMemory();
	    }
	}
	

	/**
	 * �����ݿ� �洢��sdcard
	 * @return
	 */
	public SQLiteDatabase getDatabaseSDCard() {
		try {
			if(database != null && database.isOpen())return database;
			String databaseFilename = DATABASE_CONNSTRING;
			// û��������ݿ��ļ�
			if (!(new File(databaseFilename)).exists()) {
				try{
					//����ļ���·��
				    File destDir = new File(DATABASE_PATH);
				    if (!destDir.exists()) {
				        destDir.mkdirs();
				    }
				}
				catch(Exception e)
				{
					Log.e("dbhelp.getDatabase", "Error��" + e.getMessage());
				}
				int dbid = context.get().getResources().
				getIdentifier(fileName,"raw",context.get().getPackageName());
                //���Ƶ�ָ��Ŀ¼
				int res = dbid;
				
				InputStream is = context.get().getResources().openRawResource(res);   
                FileOutputStream fos = new FileOutputStream(databaseFilename);   
                byte[] buffer = new byte[7168];   
                int count = 0;   
                // ��ʼ����dictionary.db�ļ�   
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
			Log.e("dbhelp.getDatabase", "Error��" + e.getMessage());
		}
		return null;
	}
	

	/**
	 * �����ݿ� �洢��˽������ /data/data/package/
	 * @return
	 */
	public SQLiteDatabase getDatabase() {
		try {
			if(database != null && database.isOpen())return database;
			
			// �ļ�·������ ��ʾ�洢��sd����
			if(!this.DATABASE_PATH.equals(""))return getDatabaseSDCard();
			
			// û��������ݿ��ļ�
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
                //���Ƶ�ָ��Ŀ¼
				int res = dbid;
				
				InputStream is = context.get().getResources().openRawResource(res);   
                FileOutputStream fos = context.get().openFileOutput(DATABASE_FILENAME, Context.MODE_PRIVATE); 
                byte[] buffer = new byte[7168];   
                int count = 0;   
                // ��ʼ����dictionary.db�ļ�   
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
			Log.e("dbhelp.getDatabase", "Error��" + e.getMessage());
		}
		return null;
	}

	/**
	 * ִ��SQL���,����Ӱ������
	 * @param cmdText
	 * @return
	 */
	public int ExecuteNonQuery(String cmdText) {

		return ExecuteNonQuery(getDatabase(), cmdText, null);
	}
    
	/**
	 * ִ��SQL���,����Ӱ������
	 * @param cmdText
	 * @param selectionArgs
	 * @return
	 */
	public int ExecuteNonQuery(String cmdText,String[] selectionArgs) {

		return ExecuteNonQuery(getDatabase(), cmdText, selectionArgs);
	}
    /**
     * ִ��SQL���,����Ӱ������
     * @param database
     * @param cmdText
     * @return
     */
	public int ExecuteNonQuery(SQLiteDatabase database, String cmdText) {

		return ExecuteNonQuery(database, cmdText, null);

	}
    /**
     * ִ��SQL���,����Ӱ������
     * @param database
     * @param cmdText SQL��ѯ���
     * @param selectionArgs SQL��������
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
	 * ִ��SQL���,�����α�
	 * @param cmdText
	 * @param selectionArgs
	 * @return
	 */
	public Cursor ExecuteCursor(String cmdText,
			String[] selectionArgs) {
		return ExecuteCursor(getDatabase(), cmdText, selectionArgs);
	}
    /**
     * ִ��SQL���,�����α�
     * @param database
     * @param cmdText
     * @return
     */
	public Cursor ExecuteCursor(SQLiteDatabase database, String cmdText) {
		return ExecuteCursor(database, cmdText, null);
	}
	/**
	 * ִ��SQL���,�����α�
	 * @param cmdText
	 * @return
	 */
	public Cursor ExecuteCursor(String cmdText) {
		return ExecuteCursor(getDatabase(), cmdText, null);
	}
    /**
     * ִ��SQL���,�����α�
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
	 * ִ��SQL���,����List<Map>
	 * @param cmdText
	 * @param selectionArgs
	 * @return
	 */
	public List<Map<String, Object>> ExecuteList(
			String cmdText,String[] selectionArgs) {
		return ExecuteList(getDatabase(),cmdText,selectionArgs);
	}
	/**
	 * ִ��SQL���,����List<Map>
	 * @param database
	 * @param cmdText
	 * @return
	 */
	public List<Map<String, Object>> ExecuteList(
			SQLiteDatabase database, String cmdText) {
		return ExecuteList(database,cmdText,null);
	}
	/**
	 * ִ��SQL���,����List<Map>
	 * @param cmdText
	 * @return
	 */
	public List<Map<String, Object>> ExecuteList(String cmdText) {
		 
		return ExecuteList(getDatabase(),cmdText,null);
	}
	/**
	 * ִ��SQL���,����List<Map>  ��ѯ��� �������ݼ�
	 * @param database
	 * @param cmdText SQL��ѯ���
	 * @param selectionArgs SQL��������
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

