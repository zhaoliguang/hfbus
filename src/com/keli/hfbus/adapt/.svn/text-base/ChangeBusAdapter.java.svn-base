package com.keli.hfbus.adapt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapView;
import com.keli.hfbus.R;
import com.keli.hfbus.ui.PopMapActivity;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Paint;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import android.widget.TextView;

public class ChangeBusAdapter extends BaseExpandableListAdapter implements OnClickListener {
	
	 private LayoutInflater inflater;
	 private Context context;
	 private Map  data;
	
	 private List yihclist;//依次换乘list
	 private List zdxllist;//直达线路list
	 private String shiJian,qiDian,xianLu1,xianLu2,lucheng2,qiDianZhongZuanZhan,zongDianzhongZhuanZhan,zongDian,lineLocation1,lineLocation2,zzztjl;
	 private String luChen1,luCheng2;
	 private String [] lineArray1,lineArray2;
	 private int huanChengCount,zhiDaCount,itemCount;
	 private int zhanDianJuLi1,zhanDianJuLi2;
	 private double qdJD,qdWD,zdJD,zdWD,zzJD,zzWD,qdzzWD,qdzzJD;
	 private TextView tvLinear1,tvLinear2,station1,station2;
	 private Handler mHandler;// 回发处理
	 public ChangeBusAdapter(Context context,Map data) {
		// TODO Auto-generated constructor stub
		 this.context=context;
		 inflater= (LayoutInflater)  context.
			     	getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 this.data=data;
		 if(data!=null){
			 yihclist=(ArrayList)data.get("yihclist");
			 zdxllist=(ArrayList)data.get("zdxllist");
			 if(yihclist!=null){
				 huanChengCount=yihclist.size();
				// sortByTime(yihclist);
			 }
			 else
				 huanChengCount=0;
			 if(zdxllist!=null){
				 zhiDaCount=zdxllist.size();
				// sortByTime(zdxllist);
			 }
			 else
				 zhiDaCount=0;
			 itemCount=huanChengCount+zhiDaCount;
		 }
	}
	 public void setHandler(Handler handler) {
			this.mHandler = handler;
		}
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		if(data==null){
			return 0;
		}
		else
			return itemCount;
	}


	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}


	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}


	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}


	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView=inflater.inflate(R.layout.change_bus_list_item,null);
			holder.tvLinear=(TextView) convertView.findViewById(R.id.tvLinear);
			holder.tvLinerInfo=(TextView) convertView.findViewById(R.id.tvLinerInfo);
			holder.tvMinutes=(TextView)convertView.findViewById(R.id.tvMinues);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		if(zhiDaCount!=0&&zhiDaCount>=groupPosition+1){
			
				Map map=(Map)zdxllist.get(groupPosition);
			
				xianLu1=(String)map.get("linename");
				double lc=(Double)map.get("lc");
				holder.tvLinear.setText("方案"+(groupPosition+1)+":   "+xianLu1);
				String minutes=getMinutes(lc);
				holder.tvMinutes.setText("全程约"+minutes+"分钟/");
				luChen1=getLuChen((Double)map.get("lc"));
				holder.tvLinerInfo.setText(String.valueOf(luChen1)+"公里");
				
			
			
		}else{
			Map map=(Map)yihclist.get(groupPosition-zhiDaCount);
			
			xianLu1=(String)map.get("qdlinename");
			xianLu2=(String)map.get("zdlinename");
			double lc=(Double)map.get("lc");
			holder.tvLinear.setText("方案"+(groupPosition+1)+":   "+xianLu1+"到"+xianLu2);
			String minutes=getMinutes(lc);
			holder.tvMinutes.setText("全程约"+minutes+"分钟/");
			luChen1=getLuChen((Double)map.get("lc"));
			holder.tvLinerInfo.setText(String.valueOf(luChen1)+"公里");
			
		}
			return convertView;
		
	}


	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChildViewHolder holder = null;
		LinearLayout llBuXing=null;
		LinearLayout llHuanChen1=null;
		LinearLayout llHuanCheng2=null;
		LinearLayout llTvXianLu1=null;
		LinearLayout llTvXianLu2=null;
		LinearLayout llZongDian=null;
		//LinearLayout llQiDian=null;
		LinearLayout llQDZZ=null;
		LinearLayout llZDXC=null;
		if (convertView == null) {
			holder = new ChildViewHolder();
			convertView=inflater.inflate(R.layout.change_bus_detail,null);
			
		 
			holder.tvQiDian=(TextView) convertView.findViewById(R.id.tvQiDian);
			holder.tvQiDian.setOnClickListener(this);
			holder.mapLocation=(TextView) convertView.findViewById(R.id.mapLocation);
			holder.mapLocation.setOnClickListener(this);
			station1=holder.tvQiDian;
			holder.tvXianLu1=(TextView) convertView.findViewById(R.id.tvXianLu1);
			tvLinear1=holder.tvXianLu1;
			
			holder.tvZhanShu1=(TextView) convertView.findViewById(R.id.tvZhanShu1);
			holder.tvZhongZhuanZhan=(TextView) convertView.findViewById(R.id.tvZhongZhuanZhan);
			station2=holder.tvZhongZhuanZhan;
			holder.tvMi=(TextView) convertView.findViewById(R.id.tvMi);
			holder.tvXianLu2=(TextView) convertView.findViewById(R.id.tvXianLu2);
			tvLinear2=holder.tvXianLu2;
			holder.tvZhanShu2=(TextView) convertView.findViewById(R.id.tvZhanShu2);
			holder.tvXiaChe2=(TextView) convertView.findViewById(R.id.tvXiaChe2);
			holder.tvXiaChe1=(TextView) convertView.findViewById(R.id.tvxiaChe1);
			holder.tvZongDian=(TextView) convertView.findViewById(R.id.tvZongDian);
			convertView.setTag(holder);
		}else{
			holder = (ChildViewHolder)convertView.getTag();
			station1=holder.tvQiDian;
			tvLinear1=holder.tvXianLu1;
			station2=holder.tvZhongZhuanZhan;
			tvLinear2=holder.tvXianLu2;
		}
		llHuanChen1=(LinearLayout) convertView.findViewById(R.id.llHuanChen1);
		llHuanCheng2=(LinearLayout) convertView.findViewById(R.id.llHuanCheng2);
		llBuXing=(LinearLayout) convertView.findViewById(R.id.llBuXing);
		llTvXianLu1=(LinearLayout) convertView.findViewById(R.id.llTvXianLu1);
		llTvXianLu2=(LinearLayout) convertView.findViewById(R.id.llTvXianLu2);
		llBuXing=(LinearLayout) convertView.findViewById(R.id.llBuXing);
		//llQiDian=(RelativeLayout) convertView.findViewById(R.id.llQiDian);
		llZongDian=(LinearLayout) convertView.findViewById(R.id.llZongDian);
		llQDZZ=(LinearLayout) convertView.findViewById(R.id.llQDZZ);
		llZDXC=(LinearLayout) convertView.findViewById(R.id.llZDXC);
		if(zhiDaCount!=0&&zhiDaCount>=groupPosition+1){
			
			Map map=(Map)zdxllist.get(groupPosition);
			//xianLu1=(String)map.get("linename");
			//System.out.println("直达"+map);
			qiDian=(String)map.get("qdztname");
			xianLu1=(String)map.get("linename");
			zongDian=(String)map.get("zdztname");
			lineLocation1=(String)map.get("line");
			lineArray1=lineLocation1.split(",");
			lineArray2=null;
			List ztlist =(ArrayList<Map>)(map.get("ztlist"));
			zhanDianJuLi1=ztlist.size();//站点距离
			qdJD=(Double) ((Map) ztlist.get(0)).get("JD");
			qdWD=(Double) ((Map) ztlist.get(0)).get("WD");
			zdJD=(Double) ((Map) ztlist.get(zhanDianJuLi1-1)).get("JD");
			zdWD=(Double) ((Map) ztlist.get(zhanDianJuLi1-1)).get("WD");
			qdzzWD=zdWD;
			qdzzJD=zdJD;
			
			llBuXing.setVisibility(View.GONE);
			llHuanCheng2.setVisibility(View.GONE);
			holder.tvQiDian.setText(qiDian);
			holder.tvQiDian.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			holder.tvXianLu1.setText(xianLu1);
			holder.tvXianLu1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			holder.tvZhanShu1.setText(zhanDianJuLi1+"站");
			holder.tvXiaChe1.setText(zongDian);
			holder.tvXianLu1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			holder.tvZongDian.setText(zongDian);
			holder.tvZongDian.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		
		}else{
			Map map=(Map)yihclist.get(groupPosition-zhiDaCount);
			//System.out.println("换乘"+map);
			xianLu1=(String)map.get("qdlinename");
			xianLu2=(String)map.get("zdlinename");
			qiDian=(String)map.get("qdztname");
			zongDian=(String)map.get("zdztname");
			qiDianZhongZuanZhan=(String)map.get("qdzzztname");
			zongDianzhongZhuanZhan=(String)map.get("zdzzztname");
			zzztjl=getDistanceMeter(String.valueOf((Double)map.get("zzztjl")));//中转站台距离
			
			
			List qdztlist =(ArrayList<Map>)(map.get("qdztlist"));
			zhanDianJuLi1=qdztlist.size();
			List zdztlist =(ArrayList<Map>)(map.get("zdztlist"));
			zhanDianJuLi2=zdztlist.size();
			lineLocation1=(String)map.get("qdline");
			lineLocation2=(String)map.get("zdline");
			lineArray1=lineLocation1.split(",");
			lineArray2=lineLocation2.split(",");
			qdJD=Double.valueOf((String)map.get("qdztjd"));
			qdWD=Double.valueOf((String)map.get("qdztwd"));
			qdzzWD=Double.valueOf((String)map.get("qdzzztwd"));
			qdzzJD=Double.valueOf((String)map.get("qdzzztjd"));
			zdJD=Double.valueOf((String)map.get("zdztjd"));
			zdWD=Double.valueOf((String)map.get("zdztwd"));
			zzJD=Double.valueOf((String)map.get("zdzzztjd"));
			zzWD=Double.valueOf((String)map.get("zdzzztwd"));
			
			llBuXing.setVisibility(View.VISIBLE);
			llHuanCheng2.setVisibility(View.VISIBLE);
			holder.tvQiDian.setText(qiDian);
			holder.tvQiDian.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			holder.tvXianLu1.setText(xianLu1);
			holder.tvXianLu1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			holder.tvZhanShu1.setText(zhanDianJuLi1+"站");
			holder.tvXiaChe1.setText(qiDianZhongZuanZhan);
			holder.tvXiaChe1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			holder.tvZhongZhuanZhan.setText(" "+zongDianzhongZhuanZhan);
			holder.tvZhongZhuanZhan.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			holder.tvMi.setText("约"+zzztjl+"米");
			holder.tvXianLu2.setText(xianLu2);
			holder.tvXianLu2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			holder.tvZhanShu2.setText(zhanDianJuLi2+"站");
			holder.tvXiaChe2.setText(zongDian);
			holder.tvXiaChe2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			holder.tvZongDian.setText(zongDian);
			holder.tvZongDian.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		}
		llTvXianLu1.setOnClickListener(this);
		llTvXianLu2.setOnClickListener(this);
		llBuXing.setOnClickListener(this);
		//llQiDian.setOnClickListener(this);
		
		llZongDian.setOnClickListener(this);
		llQDZZ.setOnClickListener(this);
		llZDXC.setOnClickListener(this);
	    return convertView;
		
	}


	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	private class ViewHolder{
	
		TextView tvLinear;//
		TextView tvLinerInfo;
		TextView tvMinutes;
	}
	private class ChildViewHolder{
		public TextView tvQiDian;
		public TextView tvXianLu1;
		public TextView tvZhanShu1;
		public TextView tvZhongZhuanZhan;
		public TextView tvMi;
		public TextView tvXianLu2;
		public TextView tvZhanShu2;
		public TextView tvXiaChe2;
		public TextView tvZongDian;
		public TextView tvXiaChe1;
		public TextView mapLocation;
		
	}
//	//list排序算法
//	public void sortByTime(List list) {
//		//ArrayList<HashMap<String, String>>
//	    boolean needNextPass = true;
//		if(list != null && list.size() != 0){
//			for(int k = 1; k< list.size() && needNextPass ;k++) {
//				needNextPass = false;
//				for(int i = 0; i<list.size() - k; i++){
////					Map map=(Map)list.get(i);
////					double lc=(Double)map.get("lc");
//					if( (Double)((Map)list.get(i)).get("lc")> (Double)((Map)list.get(i+1)).get("lc")) {
//						Map temp = (Map)list.get(i);
//						list.set(i, list.get(i+1));
//						list.set(i+1, temp);
//						needNextPass = true;
//					}
//				}
//			}
//		}
//	
//	}

	//把路程转换为字符串
	public String getLuChen(Double luchen){
		 double LuChen=luchen/1000;
		 String strLuChen=Double.toString(LuChen);
		 int index=strLuChen.indexOf(".");
		 strLuChen=strLuChen.substring(0,index+2);
		 return strLuChen;
		
	}
	//计算换乘站点还有多少米
	public String getDistanceMeter(String distance){
		int index=distance.indexOf(".");
		distance=distance.substring(0,index );
		return distance;
	}
	//计算时长
	public String getMinutes(double distance){
		int minutes=(int)distance/200;
		return String.valueOf(minutes);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		Message msg=new Message();
		Bundle bundle = new Bundle();    
		if (mHandler != null) {
					
			 msg = mHandler.obtainMessage();
			 
		} 
		switch(v.getId()){
		case R.id.llQDZZ:
			 msg.what =2 ;
			  bundle.putDouble("JD", qdzzJD);  
	          bundle.putDouble("WD",qdzzWD);  
			break;
		case R.id.llZongDian:
			 msg.what =2 ;
			  bundle.putDouble("JD", zdJD);  
	          bundle.putDouble("WD",zdWD);  
			break;
		case R.id.llZDXC:
			 msg.what =2 ;
			  bundle.putDouble("JD", zdJD);  
	          bundle.putDouble("WD",zdWD);  
			break;
			
		case R.id.llBuXing:
			 msg.what =2 ;
			 bundle.putDouble("JD", zzJD);  
	          bundle.putDouble("WD",zzWD);  
			break;
		case R.id.tvQiDian:
			msg.what=2;
			 bundle.putDouble("JD", qdJD);  
	          bundle.putDouble("WD",qdWD);  
			break;
		case R.id.mapLocation:
			
			msg.what=3;
			 bundle.putDouble("qdJD", qdJD);  
			 bundle.putDouble("qdWD", qdWD);  
			 bundle.putDouble("qdzzWD", qdzzWD);  
			 bundle.putDouble("qdzzJD", qdzzJD);  
			 bundle.putDouble("zdJD", zdJD);  
			 bundle.putDouble("zdWD", zdWD);  
			 bundle.putDouble("zzJD", zzJD);  
			 bundle.putDouble("zzWD", zzWD);  
			 bundle.putString("qiDian", qiDian);
			 bundle.putString("zongDian", zongDian);
			 bundle.putString("qiDianZhongZuanZhan", "在"+qiDianZhongZuanZhan+"下车");
			 bundle.putString("zongDianzhongZhuanZhan", "步行至"+zongDianzhongZhuanZhan+"约"+zzztjl+"米乘坐"+xianLu2);
			bundle.putStringArray("lineArray1", lineArray1);
			bundle.putStringArray("lineArray2", lineArray2);
			break;
		case R.id.llTvXianLu1:
			 msg.what =1 ;
            bundle.putString("busName",tvLinear1.getText().toString().trim());  //往Bundle中存放数据   
            bundle.putString("stationName",station1.getText().toString().trim());  //往Bundle中put数据   
			break;
		case R.id.llTvXianLu2:
			 msg.what =1 ;
			bundle.putString("busName",tvLinear2.getText().toString().trim());  //往Bundle中存放数据   
		    bundle.putString("stationName",station2.getText().toString().trim());  //往Bundle中put数据   
			break;
		}
		msg.setData(bundle);
		mHandler.sendMessage(msg);
		
	}
    
}
