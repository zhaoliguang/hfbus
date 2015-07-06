package com.keli.hfbus.adapt;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.keli.hfbus.R;

public class AdapterOutPromptList extends BaseAdapter{
     List<HashMap<String,Object>>  data;
     
     LayoutInflater inflater;
     private int clickPos = -1;//设置当前点击的行
     private String busStation = "";
     
     // 设置当前点击位置
     public void setClickPos(int pos){
    	 this.clickPos = pos;
    	 this.notifyDataSetChanged();
     }
     
     // 设置当前站点的位置
     public void setbusStation(String busStation){
    	 this.busStation = busStation;
    	 this.notifyDataSetChanged();
     }
     
     public AdapterOutPromptList(Context context,List<HashMap<String,Object>> data )
     {
    	 this.data=data;
    	 
    	 inflater= (LayoutInflater)  context.
     	getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
	
		return 	data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}
    
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView=inflater.inflate(R.layout.liner_detail_list_item,null);
			
			holder.img_station=(ImageView) convertView.findViewById(R.id.img_station);
			holder.textview_info=(TextView) convertView.findViewById(R.id.textview_info);
			holder.img_state=(ImageView) convertView.findViewById(R.id.img_state);
		 
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		  holder.img_station.setImageResource(R.drawable.ic_station);
		  if(position==data.size()-1)
		  { 
			 
			  holder.img_station.setVisibility(View.INVISIBLE);
		   
		  }else
			  holder.img_station.setVisibility(View.VISIBLE);
		
		String station = data.get(position).get("str").toString();
		holder.textview_info.setText(station);
      	
      	if(this.clickPos == position){
      		holder.textview_info.setTextColor(Color.RED);
      	}else{
      		holder.textview_info.setTextColor(Color.parseColor("#18415f"));
      	}
      	
      	 if("".equals(this.busStation))
         {
      		holder.img_state.setVisibility(View.INVISIBLE);
         }else{
        	 holder.img_state.setVisibility(View.VISIBLE);
        	 
        	 
        	 
        	 if(station.trim().equals(busStation.trim()) && this.clickPos == position){//显示bus图标
         
        		 holder.img_state.setImageResource(R.drawable.bus_and_me);
        	 }else  if(station.trim().equals(busStation.trim())){//显示bus图标
        		 holder.textview_info.setTextColor(Color.BLUE);//修改
        		 holder.img_state.setImageResource(R.drawable.bus);
        	 }else if(this.clickPos == position){
        		 holder.img_state.setImageResource(R.drawable.me);
           	 }else{
           		holder.img_state.setVisibility(View.INVISIBLE);
           	 }
         }
      	 
		 
		return convertView;
	}

	private class ViewHolder{
		ImageView img_station;//
		TextView textview_info;//
		ImageView img_state;//
		 
	}
}
