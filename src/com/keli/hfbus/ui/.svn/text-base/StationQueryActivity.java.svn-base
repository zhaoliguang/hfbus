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
import com.keli.hfbus.sqlite.BusDal;
import com.keli.hfbus.ui.ChangeBusActivity.startTextWatcher;
import com.keli.hfbus.ui.LinerQueryAcitvity.RequestGetLiner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * 站点查询界面
 * 
 * */
public class StationQueryActivity extends Activity implements
		OnItemClickListener, OnClickListener, OnItemLongClickListener {
	ListView myListView,stationList;
	SimpleAdapter adapter;
	ArrayList<HashMap<String, Object>> data;
	EditText editStation;
	ImageButton ibtnStationQuery,ibtnTitleReturn;
	BusDal db;
	private HFBusApp mApp;
	int index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.station_query);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
		 ibtnTitleReturn=(ImageButton)findViewById(R.id.ibtnTitleReturn);
	        ibtnTitleReturn.setOnClickListener(this);
		mApp = (HFBusApp) getApplication();
		mApp.getmActivityManager().add(this);
		editStation = (EditText) findViewById(R.id.editStation);
		editStation.addTextChangedListener(new startTextWatcher());
		ibtnStationQuery = (ImageButton) findViewById(R.id.ibtnStationQuery);
		ibtnStationQuery.setOnClickListener(this);
		data = new ArrayList<HashMap<String, Object>>();
		db = new BusDal(StationQueryActivity.this);
		/*
		 * List<Map<String, Object>> list = db.getBusstationHistoryList();
		 * HashMap<String, Object> map; for (int i = 0; i < list.size(); i++) {
		 * map = new HashMap<String, Object>(); map.put("img",
		 * R.drawable.ic_bus); map.put("str",
		 * list.get(i).get("name").toString()); data.add(map); }
		 */
		myListView = (ListView) findViewById(R.id.listView_InStationQuery);
		adapter = new SimpleAdapter(StationQueryActivity.this, data,
				R.layout.liner_list_item, new String[] { "img", "str" },
				new int[] { R.id.img_bus, R.id.textview });
		myListView.setAdapter(adapter);
		myListView.setOnItemClickListener(this);
		myListView.setOnItemLongClickListener(this);
		
		stationList=(ListView)findViewById(R.id.stationList);
		stationList.setOnItemClickListener(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (db != null) {
			db.close();
		}
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
		switch(parent.getId()){
		case R.id.listView_InStationQuery:
			Intent it = new Intent();
			String tmp = data.get(position).get("str").toString();
			it.putExtra("stationName", tmp);

			it.setClass(StationQueryActivity.this, LinerInStationActivity.class);
			startActivity(it);
			break;
		case R.id.stationList:
			LinearLayout llStation=(LinearLayout)view;
			TextView text=(TextView)llStation.findViewById(R.id.stationName);
			editStation.setText(text.getText().toString().trim());
			editStation.setSelection(text.getText().length());
			stationList.setAdapter(null);
			break;
		}
		
	}
	class startTextWatcher implements TextWatcher{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stu
				
			String key=editStation.getText().toString().trim();
			  if(key!=null && !"".equals(key.trim())){  
                  List<Map<String, ?>> stations = db.selectStation(key);
                  SimpleAdapter stationAdapter=new SimpleAdapter(StationQueryActivity.this, stations, R.layout.station_name_list, new String [] {"stationName"}, new int []{R.id.stationName});
                  stationList.setAdapter(stationAdapter);  
              }else{  
            	  stationList.setAdapter(null);  
              }  
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		/*
		 * Request request = new Request(); request.query(new
		 * RequestStationQuery(this));
		 */
		switch(v.getId()){
		case R.id.ibtnTitleReturn:
			super.onBackPressed();
			break;
		case R.id.ibtnStationQuery:
			String tmp = editStation.getText().toString().trim();
			if(tmp==null||tmp.equals("")){
				Toast.makeText(StationQueryActivity.this, "请输入站点名称", 30000).show();
				return;
			}
			Intent it = new Intent();
	
			it.putExtra("stationName", tmp);
			db.insertBusstationHistory(tmp);
	
			it.setClass(StationQueryActivity.this, LinerInStationActivity.class);
			startActivity(it);
			break;
		}
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub

		index = position;
		new AlertDialog.Builder(this).setTitle("删除").setMessage("确认删除吗？")
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String carName = data.get(index).get("str").toString();
						db.deleteSearchHistory(carName, "2");
						data.remove(index);
						adapter.notifyDataSetChanged();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).show();
		return true;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub

		if (data != null) {
			data.clear();
			List<Map<String, Object>> list = db.getBusstationHistoryList();
			HashMap<String, Object> map;
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("img", R.drawable.ic_bus);
				map.put("str", list.get(i).get("name").toString());
				data.add(map);
			}
		}
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		super.onStart();
	}

}
