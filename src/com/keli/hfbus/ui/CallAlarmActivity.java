package com.keli.hfbus.ui;

/* import class */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

import com.keli.hfbus.R;
import com.keli.hfbus.hessianserver.GjcxService;
import com.keli.hfbus.hessianserver.RequestImpl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/*AlarmReceiver */
public class CallAlarmActivity extends BroadcastReceiver
{
	 GjcxService mGjcxService;
	 String getDistance;
	 Map busLinerDatail;
	 List<HashMap<String,Object>> data=new  ArrayList<HashMap<String,Object>>();
  @Override
  public void onReceive(final Context context, Intent intent)
  {	  
	//AlarmOld currentAlarm=(AlarmOld)intent.getExtras().get("alarm");
	//Intent alaramIntent = new Intent(context, AlarmAgainSetting.class);

//	alaramIntent.putExtra("currentAlarm",currentAlarm);
	//alaramIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	
//	String stationName=currentAlarm.getArriveStation();
//	String linearName=currentAlarm.getLinear();
//	String orientation=currentAlarm.getOrientation();
//	String stationDistance=currentAlarm.getStationDistance();
//	RequestGetDistance getDistanceRequest=new RequestGetDistance(context,stationName,orientation,linearName);
	//String betweenDistance=getDistanceRequest.getBetweenDistance(stationName,linearName,orientation);
//	Thread getThread=new Thread(getDistanceRequest);
//	getThread.start();
	
	System.out.println("getDistance:"+getDistance);
////	if(getDistance!=null&&stationDistance!=null)
//		if((Integer.parseInt(getDistance)<=Integer.parseInt(stationDistance))){
//			alaramIntent.putExtra("remainDistance", getDistance);
//			context.startActivity(alaramIntent);
//			
//		}
			
	System.out.println("成功接收广播");
	//Toast.makeText(context, "成功接收广播", Toast.LENGTH_LONG).show();
	
}
class RequestGetDistance extends RequestImpl implements Runnable{
	
	public String stationName,orientation,linearName;
	
	public RequestGetDistance(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public RequestGetDistance(Context context,String stationName,String orientation,String linearName){
		super(context);
		this.stationName=stationName;
		this.orientation=orientation;
		this.linearName=linearName;
	}
	@SuppressWarnings("unchecked")
	String station,linerId ,stationId;
	public String getBetweenDistance() {
	//	String url = "http://218.22.27.69:9080/Kljtxxfww_1_0_v/hessian/gjcxService";
		GjcxService mGjcxService;
		Map m = null  ;
		HashMap<String,Object> map;
		List list;
		
		try {
			mGjcxService = (GjcxService) factory.create(GjcxService.class, url_gjcx, this.mContext.get().getClassLoader());
			busLinerDatail=(Map)mGjcxService.getBuslineDetail(linearName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		if(busLinerDatail==null)
			return null;
		 if(orientation.equals("上行")){
			 m =(Map) busLinerDatail.get("upstream");
		 }
		 else
			 m =(Map) busLinerDatail.get("downstream");
		 if(m==null) 
			 return null;
		 list=(List) m.get("list");
		for(int i=0;i<list.size();i++)
		{
		 map=new HashMap<String,Object>();
			station=((HashMap<String,Object> ) list.get(i)) .get("STATIONNAME").toString();
			linerId=((HashMap<String,Object> ) list.get(i)) .get("LINEID").toString();
			stationId=((HashMap<String,Object> ) list.get(i)) .get("STATIONID").toString();
			data.add(map);
			 if(station.equals(stationName))
				 break;
		}
		System.out.println(busLinerDatail);
		return getDistance(linerId,stationId);
	}
	public String getDistance(String linerid,String stationid) {
		// TODO Auto-generated method stub
		//super.doComplete(o);
		Map map=null;
		//String url = "http://218.22.27.69:9080/Kljtxxfww_1_0_v/hessian/gjcxService";
		try {
			//System.out.println("获取最近站点：linerId"+linerId+"stationId:"+stationId);
			mGjcxService = (GjcxService) factory.create(GjcxService.class, url_gjcx, this.mContext.get().getClassLoader());
			map=mGjcxService.getNearestBusList(linerid, stationid) ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		if(map==null)return null;
	    String status = map.get("status").toString();//状态
	    System.out.println("status:"+status);
		if("1".equals(status)){
			Map list = (Map)map.get("list");
			String distance = list.get("betlength").toString();//间隔站点数
				System.out.println(distance);
				return distance;
		}else
			return null;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		getDistance=getBetweenDistance();
	}
	}

}
