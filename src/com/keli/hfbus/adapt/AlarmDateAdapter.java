package com.keli.hfbus.adapt;

import java.util.List;

import com.keli.hfbus.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlarmDateAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;  
    private Context context; 
//    private List<String> alarm_ids;
    private List<String> alarm_date;
//    private List<String> alarm_repeats;
//    private List<String> alarm_isopens;
//    private List<String> alarm_kinds;
    
    ZuJian zuJian;
    
    public AlarmDateAdapter(Context context,List<String> ids,List<String> dates,List<String> repeats,
    		List<String> isopens,List<String> kinds) {  
        this.context = context;  
//        this.alarm_ids = ids;
        this.alarm_date = dates;
//        this.alarm_repeats = repeats;
//        this.alarm_isopens = isopens;
//        this.alarm_kinds = kinds;
        this.layoutInflater = LayoutInflater.from(context);  
    }  
    
	public int getCount() {
		return 1; 
	}

	public Object getItem(int position) {
		return alarm_date.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		zuJian =  new ZuJian();
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.alarm_date_set_view, null);
			zuJian.alarmTitle = (TextView) convertView.findViewById(R.id.alarm_title);
			zuJian.alarmDateView= (TextView) convertView.findViewById(R.id.alarm_date);
			zuJian.imageView= (ImageView) convertView.findViewById(R.id.img_arrow);
			convertView.setTag(zuJian);
		} else {
			zuJian = (ZuJian) convertView.getTag();
		}
		zuJian.alarmTitle.setText("日程:");

		if (alarm_date.get(position) == null ) {
			zuJian.alarmDateView.setText("目前无设置");
		} else {
			zuJian.alarmDateView.setText(alarm_date.get(position));
		}
		
		return convertView;
	}

    final class ZuJian {
		public TextView alarmTitle;
		public TextView alarmDateView;
		public ImageView imageView;
		
	}

}
