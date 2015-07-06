package com.keli.hfbus.sqlite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.keli.hfbus.util.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BusDal extends DalBase{
	
	

	public BusDal(Context context) {
		super(context,dbEnum.BUSINESS);
		// TODO Auto-generated constructor stub
	}
	
	//删除搜索历史记录
	private int deleteSearchHistory(String type){
		String cmdText = "delete from bus_search_history  where ID<(select min(ID) from " +
		"(select id From bus_search_hostory where type=? ORDER BY ID DESC LIMIT 19)) ";
        String[] Parms = {type};
        return dbHelper.ExecuteNonQuery(cmdText, Parms);
	}
	//删除搜索历史记录
	public int deleteSearchHistory(String name,String type){
		String cmdText = "delete from bus_search_history  where name=? and type=? ";
        String[] Parms = {name,type};
        return dbHelper.ExecuteNonQuery(cmdText, Parms);
	}
	//获得历史搜索记录
	private List<Map<String, Object>> getSearchHistoryList(String type)
	{
		String cmdText = "Select id id,name name,type type From bus_search_history "+
		" where type=? order by id desc ";
		String[] Parms = { type };
        List<Map<String, Object>> listata = dbHelper.ExecuteList(cmdText,Parms);
        return listata;
	}
	
	// 获得线路查询历史记录
	public List<Map<String, Object>> getBuslineHistoryList()
	{
        return getSearchHistoryList("1");
	}
	//获得站点查询历史记录
	public List<Map<String, Object>> getBusstationHistoryList()
	{
        return getSearchHistoryList("2");
	}
	//插入线路查询历史记录
	public int insertBuslineHistory(String busline){
		deleteSearchHistory("1");
		deleteSearchHistory(busline,"1");
		String cmdText = "insert into bus_search_history(name,type) " +
				"values(?,?) ";
		String[] Parms = {busline,"1"};
        return dbHelper.ExecuteNonQuery(cmdText,Parms);
	}
	//插入站点查询历史记录
	public int insertBusstationHistory(String busstation){
		deleteSearchHistory("2");
		deleteSearchHistory(busstation,"2");
		String cmdText = "insert into bus_search_history(name,type) " +
				"values(?,?) ";
		String[] Parms = {busstation,"2"};
        return dbHelper.ExecuteNonQuery(cmdText,Parms);
	}
	
	
	/**
	 * 获得代码列表
	 * @param dmlb
	 * @return
	 */
	public List<Map<String, Object>> getCode(String dmlb)
	{
		String cmdText = "Select dmlb dmlb, dmz dmz,dmsm1 dmsm1,dmsm2 dmsm2,dmsm3 dmsm3,dmsm4 dmsm4,dmlbmc dmlbmc " +
				" From bus_b_code where dmlb=?  order by dmz";
		String[] Parms = { dmlb };
        List<Map<String, Object>> listata = dbHelper.ExecuteList(cmdText,Parms);
        return listata;
	}
	
	public List<Map<String, Object>> getCode(String dmlb,String where)
	{
		String cmdText = "Select dmlb dmlb, dmz dmz,dmsm1 dmsm1,dmsm2 dmsm2,dmsm3 dmsm3,dmsm4 dmsm4,dmlbmc dmlbmc " +
				" From bus_b_code where dmlb=? "+where+" order by dmz";
		String[] Parms = { dmlb };
        List<Map<String, Object>> listata = dbHelper.ExecuteList(cmdText,Parms);
        return listata;
	}

	
	
	/**
	 * 获得查询缓存
	 * @return
	 */
	public String getQueryCache(String queryText)
	{
		String cmdText = "select _id _id, fsml fsml,jsml jsml,cxsj csxj " +
		" from BUS_QUERY_CACHE where fsml=?";
		String[] Parms = { queryText };
        List<Map<String, Object>> listata = dbHelper.ExecuteList(cmdText,Parms);
        if(listata != null){
        	if(listata.size()>0){
        		return listata.get(0).get("jsml").toString();
        	}
        }
        return "";
	}
	
	/**
	 * 插入查询缓存
	 * @param fsml
	 * @param jsml
	 * @return
	 */
	public int insertQueryCache(String fsml,String jsml){
		if(getQueryCache(fsml).equals("")){//缓存不存在
			int i = deleteQueryCache();
	        String cmdText = "insert into BUS_QUERY_CACHE(fsml,jsml,cxsj)" +
			" values(?,?,?) ";
	        String[] Parms = {fsml,jsml,Utils.formatTime()};
	        int k = dbHelper.ExecuteNonQuery(cmdText, Parms);
	        return k;
		}
		return 0;
	}
	
	/**
	 * 删除查询缓存只保留最后500条
	 * @return
	 */
	public int deleteQueryCache(){
		String cmdText = 
		"delete from BUS_QUERY_CACHE  where _ID<(select min(_ID) from (select _ID From BUS_QUERY_CACHE ORDER BY _ID DESC LIMIT 500)) ";
        return dbHelper.ExecuteNonQuery(cmdText);
	}
	
	/**
	 * 删除所有查询缓存
	 * @return
	 */
	public int deleteAllQueryCache(){
		String cmdText = 
			"delete from BUS_QUERY_CACHE  where _ID>0 ";
	        return dbHelper.ExecuteNonQuery(cmdText);
	}
	
	/**
	 * 获得保存的密码数据
	 * @return
	 */
	public List<Map<String, Object>> getSavePass()
	{
		String cmdText = "Select _id id,account account,pass pass,createtime createtime,imagepath imagepath " +
		" From BUS_SAVE_PASS order by createtime desc";
        List<Map<String, Object>> listata = dbHelper.ExecuteList(cmdText);
        
        return listata;
	}
	
	/**
	 * 获得保存的密码数据
	 * @param account
	 * @return
	 */
	public Map<String, Object> getSavePass(String account)
	{
		String cmdText = "Select _id id,account account,pass pass,createtime createtime,imagepath imagepath " +
		" From BUS_SAVE_PASS where account=? ";
		String[] Parms = {account};
        List<Map<String, Object>> listata = dbHelper.ExecuteList(cmdText,Parms);
        if(listata != null && listata.size() > 0)
        return listata.get(0);
        else return null;
	}
	
	/**
	 * 插入保存密码信息
	 * @return
	 */
	public int insertSavePass(String account,String pass,String time,String url)
	{
		if(account==null)return -1;
		String cmdText = "insert into BUS_SAVE_PASS(account,pass,createtime,imagepath)" +
				" values(?,?,?,?) ";
		String[] Parms = {account,pass,time,url};
		return dbHelper.ExecuteNonQuery(cmdText, Parms);
	}
	/**
	 * 更新保存密码信息
	 * @return
	 */
	public int updateSavePass(String account,String pass,String time,String url)
	{
		if(account==null)return -1;
		String cmdText = "update BUS_SAVE_PASS set pass=?,createtime=?,imagepath=? " +
				" where account=? ";
		String[] Parms = {pass,time,url,account};
		return dbHelper.ExecuteNonQuery(cmdText, Parms);
	}
	/**
	 * 删除保存密码信息
	 * @return
	 */
	public int deleteSavePass(String account)
	{
		String[] Parms = {account};
		return dbHelper.ExecuteNonQuery("delete from BUS_SAVE_PASS where account=? ",Parms);
	}
	public Cursor getStation(){
		String cmdText = "select * from bus_station_name "; 
		Cursor cursor=dbHelper.ExecuteCursor(cmdText);
		return cursor;
	}
	/**
	 * 更新站点名称
	 * @return
	 */
    public void updateStationName(List<HashMap<String, String>> stationName){
    	
    	String cmdText = "delete from bus_station_name "; 
    	dbHelper.ExcuteSql(cmdText);
    	//System.out.println(stationName);
    	for(HashMap<String, String> name:stationName){
    		String station=name.get("STATIONNAME");
    		cmdText = "insert into bus_station_name(stationName) values(?) ";
    		String[] Parms = {station};
    		dbHelper.ExcuteSql(cmdText,Parms);
    	}
    	
    }
 
    public List<Map<String,?>> selectStation(String key){  
    	List<Map<String,?>> stationList = new ArrayList<Map<String,?>>(); 
    	Cursor cur = null;  
    	 if(null!=key && !"".equals(key)){  
    		  
             //查询条件  
             //String cmdText="select stationName from bus_station_name where stationName like "+ key+"%";
             String cmdText="select * from bus_station_name where stationName like "+"'"+key+"%"+"'";
             cur = dbHelper.ExecuteCursor(cmdText);  
             cur.moveToFirst();  
             //循环读取数据  
             while(!cur.isAfterLast()){  
                 String name =  cur.getString(0);  
                 Map<String,String> map=new HashMap<String, String>();
                 map.put("stationName", name);
                 stationList.add(map);  
                 cur.moveToNext();  
             }  
             cur.close();  
             return stationList;  
         }  
    	 return null;
    }
		
}
