package com.keli.hfbus.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.keli.hfbus.HFBusApp;
import com.keli.hfbus.R;
import com.keli.hfbus.hessianserver.GjcxService;
import com.keli.hfbus.hessianserver.Request;
import com.keli.hfbus.hessianserver.RequestImpl;
import com.keli.hfbus.util.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
/**
 *站点查询界面界面
 *（经过某站点有哪些线路） 
 * */
public class LinerInStationActivity extends Activity implements OnItemClickListener,OnClickListener{
	
	 ListView myListView;
	 SimpleAdapter adapter;
	 ArrayList<HashMap<String,Object>> data;
	TextView textSize,textStation;
	HFBusApp mApp;
	String stationName;
	ImageButton ibtnTitleReturn;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
    	setContentView(R.layout.liner_in_station);
    	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
    	
    	 ibtnTitleReturn=(ImageButton)findViewById(R.id.ibtnTitleReturn);
         ibtnTitleReturn.setOnClickListener(this);
    	Intent it=getIntent();
    	mApp = (HFBusApp) getApplication();
		mApp.getmActivityManager().add(this);
    	stationName=it.getStringExtra("stationName");
    	textSize=(TextView) findViewById(R.id.textSize);
    	textStation=(TextView) findViewById(R.id.textStation);
    	data=new  ArrayList<HashMap<String,Object>>();
    	myListView=(ListView) findViewById(R.id.listView_InLinerInStation);
		adapter=new SimpleAdapter(LinerInStationActivity.this
				, data,R.layout.liner_list_item
				, new String[]{"img","str"}
		  ,new int[]{R.id.img_bus,R.id.textview});
		myListView.setAdapter(adapter);
		myListView.setOnItemClickListener(this);
    	
    	
		textStation.setText(stationName);
		Request request = new Request();
		request.query(new RequestStationQuery(this));
	
    }


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Intent it=new Intent();
		it.putExtra("busName", data.get(position).get("str").toString());
		//it.putExtra("from", "LinerInStationActivity");
		it.putExtra("stationName", stationName);
		it.setClass(LinerInStationActivity.this,BusLinearActivity.class );
	    startActivity(it);
		
	}
	class RequestStationQuery extends RequestImpl {

		public RequestStationQuery(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Object doCall() {
			// TODO Auto-generated method stub
		//	String url = "http://218.22.27.69:9080/Kljtxxfww_1_0_v/hessian/gjcxService";
			GjcxService mGjcxService;
			try {
				mGjcxService = (GjcxService) factory.create(GjcxService.class,
						url_gjcx, this.mContext.get().getClassLoader());
				Map map = mGjcxService.getBuslineByStation(stationName
						  );
				return map;
				// return this.HessianVoid;
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
			Map m = (Map) o;
			List list = (List) m.get("list");
			sortByTime(list);
		    String status = m.get("status").toString();//状态
			if("1".equals(status)){
				if (list != null) {
					HashMap<String, Object> map;
					for (int i = 0; i < list.size(); i++) {
						map = new HashMap<String, Object>();
						map.put("img", R.drawable.ic_bus);
						map.put("str", ((HashMap<String, Object>) list.get(i))
								.get("LINENAME"));
	
						data.add(map);
					}
					adapter.notifyDataSetChanged();
					textSize.setText(""+list.size());
				}
			}else if("2".equals(status)){
				Utils.toastAlert(LinerInStationActivity.this, "没有满足条件的数据..."); 
				 
			}else if("3".equals(status)){
 
				Utils.toastAlert(LinerInStationActivity.this, "查询失败..."); 
			}
            
			System.out.println(m);

		}

	}
	//list排序算法
		public void sortByTime(List list) {
			//ArrayList<HashMap<String, String>>
		    boolean needNextPass = true;
			if(list != null && list.size() != 0){
				for(int k = 1; k< list.size() && needNextPass ;k++) {
					needNextPass = false;
					for(int i = 0; i<list.size() - k; i++){
						String name1=(String) ((HashMap<String, Object>) list.get(i)).get("LINENAME");
						String name2=(String) ((HashMap<String, Object>) list.get(i+1)).get("LINENAME");
						name1=name1.substring(0, name1.length()-1);
						name2=name2.substring(0, name2.length()-1);
						int linear1;
						int linear2;
						if(name1.matches("[0-9]+")&&name2.matches("[0-9]+")){
							linear1=Integer.valueOf(name1);
							linear2=Integer.valueOf(name2);
							if( linear1>linear2) {
								Map temp = (Map)list.get(i);
								list.set(i, list.get(i+1));
								list.set(i+1, temp);
								needNextPass = true;
							}
						}
						
					}
				}
			}
		}


		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.ibtnTitleReturn:
				super.onBackPressed();
				break;
			}
		
		}	

}
