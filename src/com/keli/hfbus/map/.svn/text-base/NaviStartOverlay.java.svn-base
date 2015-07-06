
package com.keli.hfbus.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.Overlay;
import com.amap.mapapi.map.Projection;
import com.keli.hfbus.R;
import com.keli.hfbus.util.Utils;


/**路径导航图层,只画路径*/
public class NaviStartOverlay extends Overlay{

	private Context mContext;
	private String [] list1,list2;
	/**起点所在位置矩形区域*/
	private Rect rStart1 = null;
	/**终点所在位置矩形区域*/
	private Rect rEnd1 = null;
	private Rect rEnd2=null;
	private Rect rStart2=null;
	private String start1,end1,start2,end2;
	private Point point1 = null, point2 = null;
	   private View popUpView;
	   private GeoPoint pioPoint = null;//选择的点位信息
	   private LayoutInflater inflater;
	  private TextView tv_mapseltip,tv_PoiName,tv_PoiAddress,tv_Poilatlon;
	public NaviStartOverlay(String []list1,String []list2){
		this.list1=list1;
		this.list2=list2;
	}
	
	
	public NaviStartOverlay(Context context){
		this.mContext = context;
		inflater = (LayoutInflater)context.getSystemService(
    	        Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public boolean draw(Canvas arg0, MapView arg1, boolean arg2, long arg3) {
		// TODO Auto-generated method stub
		Projection projection = arg1.getProjection();
		//if(TrafficNaviApp.mApp.getNaviRouteData() == null)return false;
		//List list = (ArrayList)TrafficNaviApp.mApp.getNaviRouteData().get("list_2");
		if(list1 == null && list2==null)return false;
		if(list1!=null&&list2!=null&&list1.length<2&&list2.length<2)return false;
		
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setStrokeWidth(6);
        paint.setAntiAlias(true);
		//arg0.drawCircle(point.x, point.y, 5, paint);
        
       
		//画路径1
        if(list1!=null&&list1.length>2){
        	GeoPoint gStart,gEnd,gTmp;
    		Point pStart,pEnd = null,pTmp;
    		try{
    			gStart = new GeoPoint((int) ((Double.valueOf(list1[1].toString())+Utils.Y_OFFSET) * 1E6), 
    					(int) ((Double.valueOf(list1[0].toString())+Utils.X_OFFSET) * 1E6));
    			
    			pStart = projection.toPixels(gStart, null);
    			
    			pTmp = pStart;//从起点画
    			//System.out.println("地图list:"+list);
    			for(int i = 2;i<list1.length;i+=2){
    				
    				//mapTmp = (HashMap<String,Object>)list.get(i);
    				gEnd = new GeoPoint((int) ((Double.valueOf(list1[i+1].toString())+Utils.Y_OFFSET) * 1E6), 
    						(int) ((Double.valueOf(list1[i].toString())+Utils.X_OFFSET) * 1E6));
    				pEnd = projection.toPixels(gEnd, null);
    				arg0.drawLine(pTmp.x, pTmp.y, pEnd.x, pEnd.y, paint);
    				
    				pTmp = pEnd;//下一个
    			}
    			 // 画起点图标
    	        Bitmap bm = BitmapFactory.decodeStream(mContext.getResources().openRawResource(R.drawable.map_ic_start)
    	    			,null,null);
    	    	arg0.drawBitmap(bm, (float) (pStart.x-20), (float) (pStart.y-48), null);
    	        rStart1 = new Rect(pStart.x-20,pStart.y-48,pStart.x-20+bm.getWidth(),pStart.y-48+bm.getHeight());//设置起点矩阵
    			// 画终点图标
    			if(list2!=null&&list2.length>2){
    				bm = BitmapFactory.decodeStream(mContext.getResources().openRawResource(R.drawable.bus),null,null);
    				arg0.drawBitmap(bm, (float) (pEnd.x-20), (float) (pEnd.y-20), null);
    				rEnd1 = new Rect(pEnd.x-20,pEnd.y-48,pEnd.x-20+bm.getWidth(),pEnd.y-20+bm.getHeight());
    			}
    			else{
    				bm = BitmapFactory.decodeStream(mContext.getResources().openRawResource(R.drawable.map_ic_end),null,null);
    				arg0.drawBitmap(bm, (float) (pEnd.x-20), (float) (pEnd.y-48), null);
    				rEnd1 = new Rect(pEnd.x-20,pEnd.y-48,pEnd.x-20+bm.getWidth(),pEnd.y-48+bm.getHeight());//设置终点矩阵
    				end1=end2;
    			}
    			
    			
    		}catch(Exception e){
    			
    		}
        }
        //画路径2
        if(list2!=null&&list2.length>2){
        	GeoPoint gStart,gEnd,gTmp;
    		Point pStart,pEnd = null,pTmp;
    		try{
    			gStart = new GeoPoint((int) ((Double.valueOf(list2[1].toString())+Utils.Y_OFFSET) * 1E6), 
    					(int) ((Double.valueOf(list2[0].toString())+Utils.X_OFFSET) * 1E6));
    			
    			pStart = projection.toPixels(gStart, null);
    			pTmp = pStart;//从起点画
    			//System.out.println("地图list:"+list);
    			for(int i = 2;i<list2.length;i+=2){
    				
    				//mapTmp = (HashMap<String,Object>)list.get(i);
    				gEnd = new GeoPoint((int) ((Double.valueOf(list2[i+1].toString())+Utils.Y_OFFSET) * 1E6), 
    						(int) ((Double.valueOf(list2[i].toString())+Utils.X_OFFSET) * 1E6));
    				pEnd = projection.toPixels(gEnd, null);
    				arg0.drawLine(pTmp.x, pTmp.y, pEnd.x, pEnd.y, paint);
    				
    				pTmp = pEnd;//下一个
    			}
    			// 画起点图标
    	        Bitmap bm = BitmapFactory.decodeStream(mContext.getResources().openRawResource(R.drawable.bus)
    	    			,null,null);
    	    	arg0.drawBitmap(bm, (float) (pStart.x-20), (float) (pStart.y-20), null);
    	    	 rStart2 = new Rect(pStart.x-20,pStart.y-20,pStart.x-20+bm.getWidth(),pStart.y-20+bm.getHeight());//设置起点矩阵
    			// 画终点图标
    	        bm = BitmapFactory.decodeStream(mContext.getResources().openRawResource(R.drawable.map_ic_end),null,null);
    	        arg0.drawBitmap(bm, (float) (pEnd.x-20), (float) (pEnd.y-48), null);
    			rEnd2 = new Rect(pEnd.x-20,pEnd.y-48,pEnd.x-20+bm.getWidth(),pEnd.y-48+bm.getHeight());//设置终点矩阵
    			
    		}catch(Exception e){
    			
    		}
        }
		return super.draw(arg0, arg1, arg2, arg3);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event, MapView map) {
		// TODO Auto-generated method stub
		
		
		float fx = event.getX();
		float fy = event.getY();
		if(popUpView!=null){
			map.removeView(popUpView);
		}
		switch (event.getAction()) {
//		
//		case MotionEvent.ACTION_MOVE:// 单点移动--------------------------------------------
//			
//			return super.onTouchEvent(event, map);
			
		case MotionEvent.ACTION_UP:// 单点按下-------------------------------------------
			point1 = new Point((int) fx, (int) fy);
			
			// 转换成经纬度
			GeoPoint point = map.getProjection().fromPixels(point1.x, point1.y);
		    double lon = point.getLongitudeE6()/1000000.0 - Utils.X_OFFSET;//117.202148;
		    double lat = point.getLatitudeE6()/1000000.0 - Utils.Y_OFFSET;//31.921279;
			
			 // Projection接口用于屏幕像素点坐标系统和地球表面经纬度点坐标系统之间的变换
		    popUpView=inflater.inflate(R.layout.map_select_point_pop, null);
		   // tv_PoiName = (TextView) popUpView.findViewById(R.id.PoiName);
		    tv_PoiAddress = (TextView) popUpView.findViewById(R.id.PoiAddress);
			if(rStart1!=null&&rStart1.contains(point1.x,point1.y)){//按下起点
				tv_PoiAddress.setText(start1);
			}else if(rEnd1!=null&&rEnd1.contains(point1.x,point1.y)){//按下终点
				tv_PoiAddress.setText(end1);
			}else if(rStart2!=null&&rStart2.contains(point1.x,point1.y)){
				tv_PoiAddress.setText(start2);
			}else if(rEnd2!=null&&rEnd2.contains(point1.x,point1.y)){
				tv_PoiAddress.setText(end2);
			}
			else{
				return super.onTouchEvent(event, map);
			}
			MapView.LayoutParams lp;
			lp = new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
					MapView.LayoutParams.WRAP_CONTENT,
					point,0,0,
					MapView.LayoutParams.BOTTOM_CENTER);
			map.addView(popUpView,lp);
			return super.onTouchEvent(event, map);
		}
		
		return super.onTouchEvent(event, map);
	}
	public String[] getList1() {
		return list1;
	}


	public void setList1(String[] list1) {
		this.list1 = list1;
	}


	public String[] getList2() {
		return list2;
	}


	public void setList2(String[] list2) {
		this.list2 = list2;
	}


	public Rect getrEnd1() {
		return rEnd1;
	}


	public void setrEnd1(Rect rEnd1) {
		this.rEnd1 = rEnd1;
	}


	public Rect getrEnd2() {
		return rEnd2;
	}


	public void setrEnd2(Rect rEnd2) {
		this.rEnd2 = rEnd2;
	}


	public String getEnd1() {
		return end1;
	}


	public void setEnd1(String end1) {
		this.end1 = end1;
	}


	public String getEnd2() {
		return end2;
	}


	public void setEnd2(String end2) {
		this.end2 = end2;
	}


	public String getStart1() {
		return start1;
	}


	public void setStart1(String start1) {
		this.start1 = start1;
	}


	public String getStart2() {
		return start2;
	}


	public void setStart2(String start2) {
		this.start2 = start2;
	}
} 
