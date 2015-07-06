package com.keli.hfbus.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.keli.hfbus.HFBusApp;
import com.keli.hfbus.R;
import com.keli.hfbus.adapt.ChangeBusAdapter;
import com.keli.hfbus.hessianserver.GjcxService;
import com.keli.hfbus.hessianserver.Request;
import com.keli.hfbus.hessianserver.RequestImpl;
import com.keli.hfbus.map.GpsOverlay;
import com.keli.hfbus.map.NaviStartOverlay;
import com.keli.hfbus.util.Utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class ChangeBusItem extends MapActivity implements OnItemClickListener,OnGroupClickListener,OnClickListener{
	
	private ExpandableListView busList;
	private ChangeBusAdapter changeBusAdapter;
	private String startStation,endStation;
	private ImageButton ibtnRerutn1,ibtnExit1,ibtnTitleReturn;
	private Intent intent;
	private Map data;
	private HFBusApp mApp;
	 private PopupWindow mapWindow;
	private MapView mMapView;
	private MapController mMapController;
	private GeoPoint point;
	private GpsOverlay gpsOverlay;
	/**实时导航图层*/
	NaviStartOverlay nsol;
	int screenWidth;
	int screenHeight;
	private View llMapView;
	private LayoutInflater inflater;
	private int lastClick=-1;//记录上一次点击grouption的位置
	private int queryType=0;//换乘查询类型
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.change_bus_item);
		screenWidth  = getWindowManager().getDefaultDisplay().getWidth(); 
	    screenHeight = getWindowManager().getDefaultDisplay().getHeight(); 
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
		ibtnTitleReturn=(ImageButton)findViewById(R.id.ibtnTitleReturn);
        ibtnTitleReturn.setOnClickListener(this);
		intent=getIntent();
		mApp = (HFBusApp) getApplication();
	    mApp.getmActivityManager().add(this);
	    inflater = LayoutInflater.from(this);
	    llMapView = inflater.inflate(R.layout.map_view, null);
	    mMapView = (MapView)llMapView.findViewById(R.id.popMapView);
		mMapView.setBuiltInZoomControls(true); // 设置启用内置的缩放控件
		// 得到 mMapView 的控制权,可以用它控制和驱劢平移和缩放
		mMapController = mMapView.getController();
        
		startStation=intent.getStringExtra("startStation");
		endStation=intent.getStringExtra("endStation");
		queryType=intent.getIntExtra("changeType", 0);
		busList=(ExpandableListView)findViewById(R.id.exlvChangeBus);
		ibtnRerutn1=(ImageButton)findViewById(R.id.ibtnRerutn1);
		ibtnExit1=(ImageButton)findViewById(R.id.ibtnExit1);
		ibtnRerutn1.setOnClickListener(this);
		ibtnExit1.setOnClickListener(this);
		busList.setGroupIndicator(null);  //去掉前面箭头图标
		busList.setOnItemClickListener(this);
		busList.setOnGroupClickListener(this);
		inflater= (LayoutInflater)  ChangeBusItem.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Request request = new Request();
		request.query(new RequestChangeBusQuery(this));
		LinearLayout ll=new LinearLayout(this);
		
		
	}
	class RequestChangeBusQuery extends RequestImpl {

		public RequestChangeBusQuery(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Object doCall() {
			// TODO Auto-generated method stub
			//String url = "http://218.22.27.69:9080/Kljtxxfww_1_0_v/hessian/gjcxService";
			GjcxService mGjcxService;
			try {
				mGjcxService = (GjcxService) factory.create(GjcxService.class,
						url_gjcx, this.mContext.get().getClassLoader());
				System.out.println(queryType);
				Map map = mGjcxService.getGjhcMap(startStation,endStation,queryType);
				return map;
				// return this.HessianVoid;
			} catch (Exception e) { 
				// TODO Auto-generated catch block
				System.out.println("e:"+e.toString());
				return e;
			}

		}

		@Override
		public void doComplete(Object o) {
			// TODO Auto-generated method stub
			super.doComplete(o);
		//	data.clear();
			
			data = (Map) o;
			
			
			List yihclist=(ArrayList)data.get("yihclist");
			List zdxllist=(ArrayList)data.get("zdxllist");
			
			if(yihclist==null&&zdxllist==null||(yihclist.size()+zdxllist.size())==0){
				Toast.makeText(ChangeBusItem.this, "无查询数据,请重新查询", Toast.LENGTH_SHORT).show();
				finish();
				
			}
			changeBusAdapter=new ChangeBusAdapter(ChangeBusItem.this,data);
			changeBusAdapter.setHandler(mAdpBtnClickHandler);
			busList.setAdapter(changeBusAdapter);

		}

	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
	}
	public Handler mAdpBtnClickHandler = new Handler() {
		String linearName,stationName,qiDian,zongDian,qiDianZhongZuanZhan,zongDianzhongZhuanZhan;
		 private double qdJD,qdWD,zdJD,zdWD,zzJD,zzWD,qdzzWD,qdzzJD;
		public void handleMessage(final Message msg) {
			if(mapWindow != null && mapWindow.isShowing()) 
				return;
			switch(msg.what){
			
			case 1:
				linearName=(String)msg.getData().get("busName");
				stationName=(String)msg.getData().get("stationName");
				Intent intent=new Intent();
				intent.setClass(ChangeBusItem.this, BusLinearActivity.class);
				intent.putExtra("busName", linearName);
				intent.putExtra("stationName", stationName);
				startActivity(intent);
			break;
			case 2:
				double jd=(Double)msg.getData().get("JD");
				double wd=(Double)msg.getData().get("WD");
				
				point = new GeoPoint((int) ((wd+Utils.Y_OFFSET) * 1E6), (int) ((jd+Utils.X_OFFSET) * 1E6));
				mMapController.setCenter(point); // 设置地图中心点
				mMapController.setZoom(17); // 设置地图 zoom 级别
				if(mMapView.getOverlays().contains(nsol))mMapView.getOverlays().remove(nsol);
				if(gpsOverlay != null)
					mMapView.getOverlays().remove(gpsOverlay);
				gpsOverlay = new GpsOverlay(ChangeBusItem.this,jd,wd,30,R.drawable.map_background);
				mMapView.getOverlays().add(gpsOverlay);
				mMapView.invalidate();
				mapWindow = new PopupWindow(llMapView,screenWidth, screenHeight*2/3);
				mapWindow.setAnimationStyle(R.style.popWindowgStyle);
				mapWindow.setFocusable(true);
				mapWindow.setOutsideTouchable(true);
				mapWindow.setBackgroundDrawable(new BitmapDrawable());
				mapWindow.showAtLocation(findViewById(R.id.llChangeBus),Gravity.CENTER, 0, 0);//在屏幕居中，无偏移
				break;
			case 3:
				qdJD=(Double)msg.getData().get("qdJD");
				qdWD=(Double)msg.getData().get("qdWD");
				zdJD=(Double)msg.getData().get("zdJD");
				zdWD=(Double)msg.getData().get("zdWD");
				zzJD=(Double)msg.getData().get("zzJD");
				zzWD=(Double)msg.getData().get("zzWD");
				qdzzWD=(Double)msg.getData().get("qdzzWD");
				qdzzJD=(Double)msg.getData().get("qdJD");
				qiDian=(String)msg.getData().get("qiDian");
				zongDian=(String)msg.getData().get("zongDian");
				qiDianZhongZuanZhan=(String)msg.getData().get("qiDianZhongZuanZhan");
				zongDianzhongZhuanZhan=(String)msg.getData().get("zongDianzhongZhuanZhan");
				 
				String [] lineArray1=(String[]) msg.getData().get("lineArray1");
				String [] lineArray2=(String[]) msg.getData().get("lineArray2");
				point = new GeoPoint((int) (((qdWD+zdWD)/2+Utils.Y_OFFSET) * 1E6), (int) (((qdJD+zdJD)/2+Utils.X_OFFSET) * 1E6));
				//point = new GeoPoint((int) ((qdWD+Utils.Y_OFFSET) * 1E6), (int) ((qdJD+Utils.X_OFFSET) * 1E6));
				mMapController.setCenter(point); // 设置地图中心点
				mMapController.setZoom(12); // 设置地图 zoom 级别
				if(mMapView.getOverlays().contains(gpsOverlay))mMapView.getOverlays().remove(gpsOverlay);
				if(nsol != null)
					mMapView.getOverlays().remove(nsol);
				nsol = new NaviStartOverlay(ChangeBusItem.this);
				nsol.setList1(lineArray1);
				nsol.setList2(lineArray2);
				nsol.setStart1(qiDian);
				nsol.setStart2(zongDianzhongZhuanZhan);
				nsol.setEnd1(qiDianZhongZuanZhan);
				nsol.setEnd2(zongDian);
				mMapView.getOverlays().add(nsol);
				mMapView.invalidate();
				mapWindow = new PopupWindow(llMapView,screenWidth, screenHeight*2/3);
				mapWindow.setAnimationStyle(R.style.popWindowgStyle);
				mapWindow.setFocusable(true);
				mapWindow.setOutsideTouchable(true);
				mapWindow.setBackgroundDrawable(new BitmapDrawable());
				mapWindow.showAtLocation(findViewById(R.id.llChangeBus),Gravity.CENTER, 0, 0);//在屏幕居中，无偏移
				break;
			}
		}
	};
	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		// TODO Auto-generated method stub
		if(mapWindow != null && mapWindow.isShowing()) {
				mapWindow.dismiss();
				mapWindow = null;
				return true;
		}
		if(lastClick == -1)  
        {  
			busList.expandGroup(groupPosition);  
        }  
          
        if(lastClick != -1 && lastClick != groupPosition)  
        {  
        	busList.collapseGroup(lastClick);  
        	busList.expandGroup(groupPosition);  
        }  
        else if(lastClick == groupPosition)   
        {  
            if(busList.isGroupExpanded(groupPosition))  
            	busList.collapseGroup(groupPosition);  
            else if(!busList.isGroupExpanded(groupPosition))  
            	busList.expandGroup(groupPosition);  
        }  
          
        lastClick = groupPosition;  
        changeBusAdapter.notifyDataSetChanged();
        return true;  
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.ibtnTitleReturn:
			super.onBackPressed();
			break;
		case R.id.ibtnRerutn1:
			super.onBackPressed();
			break;
		case R.id.ibtnExit1:
			mApp.getmActivityManager().finishAll();
			break;
		}
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK ){ 
			if (mapWindow != null && mapWindow.isShowing()) {

				mapWindow.dismiss();
				mapWindow = null;

				}
			return true;
		} 
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		System.out.println("点击");
		// TODO Auto-generated method stub
		if(mapWindow != null && mapWindow.isShowing()) {
			mapWindow.dismiss();
			mapWindow = null;
			}
		return super.onTouchEvent(event);
	}
}
