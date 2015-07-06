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
import com.keli.hfbus.util.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 线路查询界面
 * 
 * */
public class LinerQueryAcitvity extends Activity implements
		OnItemClickListener, OnClickListener ,OnItemLongClickListener{
	ListView myListView;
	SimpleAdapter adapter;
	ArrayList<HashMap<String, Object>> data;
	EditText editLiner;
	ImageButton ibtnLinerQuery,ibtnTitleReturn;
	BusDal dataBase;
	TextView textSearch;
	int index;
	 HFBusApp mApp;
	boolean hasClickButton;// 是否单击过按钮 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.liner_query);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
		ibtnTitleReturn=(ImageButton)findViewById(R.id.ibtnTitleReturn);
        ibtnTitleReturn.setOnClickListener(this);
		mApp = (HFBusApp) getApplication();
		mApp.getmActivityManager().add(this);
		hasClickButton=false;// 是否单击过按钮 
		editLiner = (EditText) findViewById(R.id.edit_liner);
		ibtnLinerQuery = (ImageButton) findViewById(R.id.ibtnLinerQuery);
		myListView = (ListView) findViewById(R.id.listView_InLinerQuery);
		dataBase=new BusDal(LinerQueryAcitvity.this);
		data = new ArrayList<HashMap<String, Object>>();
		textSearch=(TextView) findViewById(R.id.textSearch);
		List<Map<String, Object>> list= dataBase.getBuslineHistoryList();
		HashMap<String, Object> map;
	    for (int i=0 ;i<list.size();i++)
	    {
	    	map = new HashMap<String, Object>();
			map.put("img", R.drawable.ic_bus);
			map.put("str", list.get(i).get("name").toString());
			data.add(map);
	    }
		
		adapter = new SimpleAdapter(LinerQueryAcitvity.this, data,
				R.layout.liner_list_item, new String[] { "img", "str" },
				new int[] { R.id.img_bus, R.id.textview });
		myListView.setAdapter(adapter);
		ibtnLinerQuery.setOnClickListener(this);
		myListView.setOnItemClickListener(this);
		myListView.setOnItemLongClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent it = new Intent();
		String linerName=data.get(position).get("str").toString();
		it.putExtra("busName", linerName);
		//it.putExtra("from", "LinerQueryAcitvity");
		System.out.println("LinerQuery"+linerName);
		dataBase.insertBuslineHistory(linerName);
		it.setClass(LinerQueryAcitvity.this, BusLinearActivity.class);
		startActivity(it);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.ibtnTitleReturn:
			super.onBackPressed();
			break;
		case R.id.ibtnLinerQuery:
			hasClickButton=true;
			Request request = new Request();
			request.query(new RequestGetLiner(this));
			break;
		}
	}

	class RequestGetLiner extends RequestImpl {

		public RequestGetLiner(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Object doCall() {
			// TODO Auto-generated method stub
			//String url = "http://218.22.27.69:9080/Kljtxxfww_1_0_v/hessian/gjcxService";
			GjcxService mGjcxService;
			try {
				System.out.println("线路查询");
				mGjcxService = (GjcxService) factory.create(GjcxService.class,
						url_gjcx, this.mContext.get().getClassLoader());
			 
				Map map = mGjcxService.getBuslineListFuzzy(editLiner.getText()
						.toString().trim());
				System.out.println("map:"+map);
				return map;
				// return this.HessianVoid; 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				return e;
			}

		}

		@Override
		public void doComplete(Object o) {
			// TODO Auto-generated method stub
			super.doComplete(o);
			data.clear();
			Map m = (Map) o;
			textSearch.setText("搜索结果：");
			List list = (List) m.get("list");
		    String status = m.get("status").toString();//状态
		  //得到输入的线路
		    String busName=editLiner.getText().toString().trim();
		    if(!busName.contains("路")){
		    	busName=busName.concat("路");
		    }
		    
			if("1".equals(status)){
				if (list != null) {
					HashMap<String, Object> map;
					for (int i = 0; i < list.size(); i++) {
						map = new HashMap<String, Object>();
						map.put("img", R.drawable.ic_bus);
						String name= (String)((HashMap<String, Object>) list.get(i)).get("LINENAME");
						map.put("str", name);
						if(name.equals(busName))
						{
							data.add(0, map);
						}else{
							data.add(map);
						}
						
					}
					adapter.notifyDataSetChanged();
				}
			}else if("2".equals(status)){
				Utils.toastAlert(LinerQueryAcitvity.this, "没有满足条件的数据..."); 
				 
			}else if("3".equals(status)){
 
				Utils.toastAlert(LinerQueryAcitvity.this, "查询失败..."); 
			}
			System.out.println("getBuslineListFuzzy"+m);

		}

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position
			, long id)  {
		// TODO Auto-generated method stub
		
		if(hasClickButton)
		{
			return false;
		}
		index=position;
		new AlertDialog.Builder(this).setTitle("删除")
          .setMessage("确认删除吗？")
          .setPositiveButton("确认", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String carName=data.get(index).get("str").toString();
				dataBase.deleteSearchHistory(carName, "1");
				data.remove(index);
				adapter.notifyDataSetChanged();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).show();
		return true;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(dataBase!=null)
		{
			dataBase.close();
		}
		System.out.println("线路查询的onDestroy()");
		super.onDestroy();
	}
}
