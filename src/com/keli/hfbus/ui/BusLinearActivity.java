package com.keli.hfbus.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.keli.hfbus.HFBusApp;
import com.keli.hfbus.R;
import com.keli.hfbus.adapt.MyListViewAdapter;
import com.keli.hfbus.hessianserver.GjcxService;
import com.keli.hfbus.hessianserver.Request;
import com.keli.hfbus.hessianserver.RequestImpl;
import com.keli.hfbus.ui.LinerQueryAcitvity.RequestGetLiner;
import com.keli.hfbus.view.AutoScrollTextView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
/** 
 * 线路查询结果界面
 * 站点查询最终界面 
 * （线路经过哪些站）
 *  
 * */
public class BusLinearActivity extends Activity implements OnItemClickListener,OnClickListener{
     
    ListView myListView;
	// SimpleAdapter adapter;
    MyListViewAdapter adapter;
	 List<HashMap<String,Object>> data;
	 TextView textEarlyTime,textLateTime,textUpDown,textLinerName ;
	 String linerName,linerId,stationId,stationName;
	 Map busLinerDatail;
	 ImageButton ibtOrientation,ibtnRerutn2,ibtnExit2,ibtnTitleReturn;
	 Map lastResult;
	 GjcxService mGjcxService;
	// TextView tvNearTitle;
	 HFBusApp mApp;
	 LinearLayout detail_tail,showNextBusError,showNextBus,arriveStation,hasNStation,startStation;
	 TextView tvNearMsgStationName,tvNearMsgLinerName,
	 tvNearMsgNearStationName,tvNearMsgLength,tvNearMsgError;
	 String [] noGpsLines={"601路","602路","603路","604路","605路","606路","650路","651路","652路","655路"};
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.bus_liner);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
        ibtnTitleReturn=(ImageButton)findViewById(R.id.ibtnTitleReturn);
        ibtnTitleReturn.setOnClickListener(this);
        Intent it=getIntent();
        mApp = (HFBusApp) getApplication();
        mApp.getmActivityManager().add(this);
        showNextBusError=(LinearLayout) findViewById(R.id.showNextBusError);
        
        showNextBus=(LinearLayout) findViewById(R.id.showNextBus);
        linerName=it.getStringExtra("busName");//得到线路名
        textEarlyTime=(TextView) findViewById(R.id.textEarlyTime);
		textLateTime=(TextView) findViewById(R.id.textLateTime);
		textUpDown=(TextView) findViewById(R.id.textUpDown);
		textLinerName=(TextView) findViewById(R.id.textLinerName);
		ibtOrientation=(ImageButton) findViewById(R.id.ibtOrientation);
		ibtOrientation.setOnClickListener(this);
		ibtnRerutn2=(ImageButton)findViewById(R.id.ibtnRerutn2);
		ibtnRerutn2.setOnClickListener(this);
		ibtnExit2=(ImageButton)findViewById(R.id.ibtnExit2);
		ibtnExit2.setOnClickListener(this);
		textUpDown.setText( "上行");
		textLinerName.setText(linerName);
		data=new  ArrayList<HashMap<String,Object>>();
    	
		detail_tail = (LinearLayout)findViewById(R.id.detail_tail);
		//tvNearTitle = (TextView) findViewById(R.id.tvNearTitle);
		
		//目的站点名称
		tvNearMsgStationName   = (TextView) findViewById(R.id.tvNearMsgStationName );
		//距离目的站点最近的路线名称
		tvNearMsgLinerName   = (TextView) findViewById(R.id.tvNearMsgLinerName );
		//当前所在站点名称
		tvNearMsgNearStationName   = (TextView) findViewById(R.id.tvNearMsgNearStationName );
		//当前站到到目的站点距离
		tvNearMsgLength   = (TextView) findViewById(R.id.tvNearMsgLength );
		//
		tvNearMsgError  = (TextView) findViewById(R.id.tvNearMsgError );
		arriveStation=(LinearLayout) findViewById(R.id.arrive_station);
		hasNStation=(LinearLayout) findViewById(R.id.has_n_station);
		startStation=(LinearLayout) findViewById(R.id.start_station);
    	myListView=(ListView) findViewById(R.id.listView_InBusLiner);
    	adapter=new MyListViewAdapter(getApplicationContext(), data);
		myListView.setAdapter(adapter);
		myListView.setOnItemClickListener(this);
		
		
			 Request request = new Request();
			 request.query(new RequestGetLinerDtatil(this)); 
			 stationName= it.getStringExtra("stationName");//得到站点名称
			 System.out.println("打印"+stationName);
			
    }
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		List<String> lineList=Arrays.asList(noGpsLines);
		if(lineList.contains(linerName)){
			Toast.makeText(BusLinearActivity.this, "本线路暂无GPS信息", 30000).show();
			return;
		}

		selectStation(position);
	}
	
	
	class RequestGetNearestBus extends RequestImpl{

		public RequestGetNearestBus(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Object doCall() {
			// TODO Auto-generated method stub
		//	String url = "http://218.22.27.69:9080/Kljtxxfww_1_0_v/hessian/gjcxService";
			
		
			try {
				System.out.println("获取最近站点：linerId"+linerId+"stationId:"+stationId);
				mGjcxService = (GjcxService) factory.create(GjcxService.class, url_gjcx, this.mContext.get().getClassLoader());
				Map map=mGjcxService.getNearestBusList(linerId, stationId) ;
				//Map map=mGjcxService.getNearestBusList("97", "1898") ;
				return map;
//				return this.HessianVoid;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return e;
			}
		    
		}

		@Override
		public void doComplete(Object o) {
			// TODO Auto-generated method stub
			super.doComplete(o);
			showBottom(o);
//			Map m = (Map)o;
//			
//		    String status = m.get("status").toString();//状态
//			if("1".equals(status)){
//				showNextBusError.setVisibility(View.GONE);
//				showNextBus.setVisibility(View.VISIBLE);
//				Map list = (Map)m.get("list");
//				String time= list.get("time").toString();//到达时间分钟
//				String begname = list.get("begname").toString();//到达站点
//				String betlength = list.get("betlength").toString();//间隔站点数
//				//tvNearMsg.setText("车辆于 "+time+" 分钟后到达,所在站点："+begname+",距离当前站点 "+betlength+" 站 ");
//				
//				/*tvNearMsg.setText("距离"+stationName+"最近的"+
//						linerName+"目前所在站点为"+begname
//						+"距离您选择的站点还有"+betlength+"站");*/
//			 
//				tvNearMsgStationName.setText(stationName);
//				tvNearMsgLinerName.setText(linerName);
//				tvNearMsgNearStationName.setText(begname);
//				tvNearMsgLength.setText(betlength);
//
//				System.out.println(stationName);
//				System.out.println(linerName);
//				System.out.println(begname);
//				System.out.println(betlength);
//				if(betlength.equals("0"))
//				{
//				 
//					arriveStation.setVisibility(View.VISIBLE);
//					hasNStation.setVisibility(View.GONE);
//					
//				} else
//				{
//					arriveStation.setVisibility(View.GONE);
//					hasNStation.setVisibility(View.VISIBLE);
//				}
//				adapter.setbusStation(begname);
//			}else if("2".equals(status)){
//				showNextBusError.setVisibility(View.VISIBLE);
//				showNextBus.setVisibility(View.GONE);
//				tvNearMsgError.setText("没有满足条件的车辆...");
//				adapter.setbusStation("");
//			}else if("3".equals(status)){
//				showNextBusError.setVisibility(View.VISIBLE);
//				showNextBus.setVisibility(View.GONE);
//				tvNearMsgError.setText("查询失败...");
//				adapter.setbusStation("");
//			}
		}
		
		
	}
	class RequestGetLinerDtatil extends RequestImpl{

		public RequestGetLinerDtatil(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Object doCall() {
			// TODO Auto-generated method stub
			//String url = "http://218.22.27.69:9080/Kljtxxfww_1_0_v/hessian/gjcxService";
			GjcxService mGjcxService;
		
			try {
				mGjcxService = (GjcxService) factory.create(GjcxService.class, url_gjcx, this.mContext.get().getClassLoader());
				//Log.i("RequestGetLinerDtatil", linerName);
				Map map=mGjcxService.getBuslineDetail(linerName);
				System.out.println("RequestGetLinerDtatil  "+map  );
				return map;
//				return this.HessianVoid;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return e;
			}
		    
		}

		@Override
		public void doComplete(Object o) {
			// TODO Auto-generated method stub
			super.doComplete(o);
			 data.clear();
			 busLinerDatail = (Map)o;
			 Map m =(Map) busLinerDatail.get("upstream");
			  if(m==null) return;
			 
			textEarlyTime.setText("首班："+	m .get("earlytime") );
			textLateTime.setText("末班："+	m .get("latetime"));
		 
			List list=(List) m.get("list");
			
			HashMap<String,Object> map;
			for(int i=0;i<list.size();i++)
			{
				 map=new HashMap<String,Object>();
				 map.put("img", R.drawable.ic_station);
				 map.put("str",((HashMap<String,Object> ) list.get(i)) .get("STATIONNAME") );
				 map.put("ico", null);
				 map.put("linerId",((HashMap<String,Object> ) list.get(i)) .get("LINEID") );
				 map.put("stationId",((HashMap<String,Object> ) list.get(i)) .get("STATIONID") );
				 data.add(map);
			}
		    adapter.notifyDataSetChanged();
		    
			if( stationName !=null)
			{
				 for(int i=0;i<data.size();i++)
				 {
					 if(data.get(i).get("str").toString().
							 equals(stationName) )
					 {
						 System.out.println("从选站点进去，自动选择站点。");
						 selectStation(i);
						 break;
					 }
				 }
				
			}
			System.out.println(busLinerDatail);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.ibtOrientation:
		if(busLinerDatail!=null)
		{
			detail_tail.setVisibility(View.GONE);
			showNextBusError.setVisibility(View.GONE);
			showNextBus.setVisibility(View.GONE);
			Map m;
			if(textUpDown.getText().toString().equals("上行"))
			{
				textUpDown.setText("下行");
			   m =(Map) busLinerDatail.get("downstream");
			}else
			{
				textUpDown.setText("上行");
				  m =(Map) busLinerDatail.get("upstream");
			}
		     if(m==null) return;
			 
				textEarlyTime.setText("首班："+	m .get("earlytime") );
				textLateTime.setText("末班："+	m .get("latetime"));
			 
				List list=(List) m.get("list");
				data.clear();
				HashMap<String,Object> map;
				for(int i=0;i<list.size();i++)
				{
					 map=new HashMap<String,Object>();
					 map.put("img", R.drawable.ic_station);
					 map.put("str",((HashMap<String,Object> ) list.get(i)) .get("STATIONNAME") );
					 map.put("ico", null);
					 map.put("linerId",((HashMap<String,Object> ) list.get(i)) .get("LINEID") );
					 map.put("stationId",((HashMap<String,Object> ) list.get(i)) .get("STATIONID") );
					 data.add(map);
				}
				detail_tail.setVisibility(View.GONE);
			    adapter.setClickPos(-1);
			    adapter.setbusStation("");
			    adapter.notifyDataSetChanged();
			    if( stationName !=null)
				{
					 for(int i=0;i<data.size();i++)
					 {
						 if(data.get(i).get("str").toString().
								 equals(stationName) )
						 {
							 System.out.println("切换上下行，自动选择站点。");
							 selectStation(i);
							 break;
						 }
					 }
					
				}
		}
		break;
		case R.id.ibtnTitleReturn:
			super.onBackPressed();
			break;
		case R.id.ibtnRerutn2:
			super.onBackPressed();
			break;
		case R.id.ibtnExit2:
			mApp.getmActivityManager().finishAll();
			break;
		}
	}
	
	private  Timer timer; 
	private TimerTask task;
	int clickPosition;//记录点击位置
	long currentTime=System.currentTimeMillis();
	private void selectStation(int position)
	{
		clickPosition=position;
		 System.out.println("selectStation>>>>>>>>>>>>>>>>>>>>>>>>>>  ");
		linerId=data.get(position).get("linerId").toString() ;
		stationId=data.get(position).get("stationId").toString() ;
		stationName = data.get(position).get("str").toString() ;
		if(task!=null)
		{
			task.cancel();
			task=null;
			System.out.println("取消task");
		}
		if(timer!=null){
			timer.cancel();
			timer=null;
			System.out.println("取消timer");
		}
		
		timer=new Timer();
		task=new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 Message message = new Message();
			     handler.sendMessage(message);
			}
		};
		detail_tail.setVisibility(View.VISIBLE);
		 adapter.setClickPos(position);
		 myListView.requestFocusFromTouch();
		 myListView.setSelectionFromTop(position,150) ;
		 tvNearMsgStationName.requestFocusFromTouch();
		 System.out.println("pos  "+position+"station"+stationName+"stationId "+stationId+"linerId "+linerId);
		 Request request = new Request();
		 request.query(new RequestGetNearestBus(this));
		 currentTime=System.currentTimeMillis();
		 timer.schedule(task, 0, 30000);
		 
		 
		 
	}
	Handler handler =new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			new GetThread().execute();
		};
	};
	long pastTime=System.currentTimeMillis();
	public void showBottom(Object o){
		Map m = (Map)o;
	    String status = m.get("status").toString();//状态
		if("1".equals(status)){
			showNextBusError.setVisibility(View.GONE);
			showNextBus.setVisibility(View.VISIBLE);
			Map list = (Map)m.get("list");
			String time= list.get("time").toString();//到达时间分钟
			String begname = list.get("begname").toString();//到达站点
			String betlength = list.get("betlength").toString();//间隔站点数
			//String busId=list.get("hphm").toString();
			//System.out.println("list:"+list);
			//tvNearMsg.setText("车辆于 "+time+" 分钟后到达,所在站点："+begname+",距离当前站点 "+betlength+" 站 ");
			
			/*tvNearMsg.setText("距离"+stationName+"最近的"+
					linerName+"目前所在站点为"+begname
					+"距离您选择的站点还有"+betlength+"站");*/
			
			adapter.setbusStation(begname);
			adapter.notifyDataSetChanged();
			tvNearMsgStationName.setText(stationName);
			tvNearMsgLinerName.setText(linerName);
			tvNearMsgNearStationName.setText(begname);
			tvNearMsgLength.setText(betlength);
			pastTime=System.currentTimeMillis();

			if(clickPosition==0){
				startStation.setVisibility(View.VISIBLE);
				hasNStation.setVisibility(View.GONE);
				arriveStation.setVisibility(View.GONE);
			}
			else if(betlength.equals("0"))
			{
				arriveStation.setVisibility(View.VISIBLE);
				startStation.setVisibility(View.GONE);
				hasNStation.setVisibility(View.GONE);
				
			} else
			{
				arriveStation.setVisibility(View.GONE);
				hasNStation.setVisibility(View.VISIBLE);
				startStation.setVisibility(View.GONE);
			}
			
			
		}else if("2".equals(status)){
			showNextBusError.setVisibility(View.VISIBLE);
			showNextBus.setVisibility(View.GONE);
			tvNearMsgError.setText("没有满足条件的车辆...");
			adapter.setbusStation("");
		}else if("3".equals(status)){
			showNextBusError.setVisibility(View.VISIBLE);
			showNextBus.setVisibility(View.GONE);
			tvNearMsgError.setText("查询失败...");
			adapter.setbusStation("");
		}
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(task!=null)
		{
			task.cancel();
			task=null;
			System.out.println("onStop+取消task");
		}
		if(timer!=null){
			timer.cancel();
			timer=null;
			System.out.println("onStop取消timer");
		}
		
	}
	public class GetThread extends AsyncTask<Map,String,Map>{
		
		@Override
		protected Map doInBackground(Map... params) {
			// TODO Auto-generated method stub
			 try{
				 lastResult=mGjcxService.getNearestBusList(linerId, stationId) ;
				 
			 }catch(Exception e){
				 
			 }
			return lastResult;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Map result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(lastResult!=null){
				  showBottom(lastResult);
			 }
		}
	}

}



