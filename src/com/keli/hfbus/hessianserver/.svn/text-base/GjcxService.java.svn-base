package com.keli.hfbus.hessianserver;

import java.util.Map;

public interface GjcxService {
	/**
	 * 线路列表模糊查询
	 * @param line
	 * @return
	 */
	public Map getBuslineListFuzzy(String line);
	/**
	 * 线路详细信息查询
	 * @param line
	 * @return
	 */
	public Map getBuslineDetail(String line);
	/**
	 * 站点线路查询
	 * @param station
	 * @return
	 */
	public Map getBuslineByStation(String station);
	/**
	 * 站点最近公交查询
	 * @param lineid
	 * @param stationid
	 * @return
	 */
	public Map getNearestBusList(String lineid,String stationid);
	
	/**
	 * 公交换乘查询
	 * @param qdzt 起点站台名称
	 * @param zdzt 终点站台名称
	 * @return
	 */
	public Map<String,Object>getGjhc(String qdzt,String zdzt);
	
	/**
	 * 获得所有公交站点名称
	 * @return 
	 */
	public Map getStationList();
	
	/**
	 * @param qdzt
	 * @param zdzt
	 * @param type 0:较快捷；1:少换乘；2：少步行；
	 * @return
	 */
	public Map<String,Object>getGjhc(String qdzt,String zdzt,int type);

	public Map<String,Object>getGjhcMap(String qdzt,String zdzt,int type);

}
