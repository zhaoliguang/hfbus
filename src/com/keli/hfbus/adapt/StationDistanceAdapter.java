package com.keli.hfbus.adapt;

import java.util.List;

import com.keli.hfbus.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StationDistanceAdapter extends BaseAdapter {
	
	private LayoutInflater layoutInflater;  
    private Context context; 
    private String [] distance;
	public StationDistanceAdapter(Context context,String [] distance) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.distance=distance;
		this.layoutInflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return distance.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		
		return distance[position];
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
			convertView = layoutInflater.inflate(R.layout.selec_distance, null);
			holder = new ViewHolder();
			holder.tv1=(TextView)convertView.findViewById(R.id.tv1);
			holder.tv2=(TextView)convertView.findViewById(R.id.tv2);
			holder.textview_info=(TextView) convertView.findViewById(R.id.distance);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		String station = distance[position];
		holder.textview_info.setText(station);
		return convertView;
	}

	private class ViewHolder{
		TextView tv1;
		TextView textview_info;//
		TextView tv2;
		 
	}

}
