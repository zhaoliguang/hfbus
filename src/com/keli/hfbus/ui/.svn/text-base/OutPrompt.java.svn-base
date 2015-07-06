package com.keli.hfbus.ui;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.keli.hfbus.HFBusApp;
import com.keli.hfbus.R;
import com.keli.hfbus.adapt.AdapterOutPromptList;
import com.keli.hfbus.hessianserver.GjcxService;
import com.keli.hfbus.hessianserver.Request;
import com.keli.hfbus.hessianserver.RequestImpl;
import com.keli.hfbus.hessianserver.WzlmServiceOpenAPI;
import com.keli.hfbus.ui.LinerQueryAcitvity.RequestGetLiner;
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
import android.widget.Toast;

public class OutPrompt extends Activity implements OnItemClickListener,OnClickListener {
	ArrayList<HashMap<String, Object>> data;
	ListView myListView;
	AdapterOutPromptList adapter;
	int page, pageSize;
	boolean hasNext;
	private ImageButton ibtnTitleReturn;
	private HFBusApp mApp;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.out_prompt);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
		
		ibtnTitleReturn=(ImageButton)findViewById(R.id.ibtnTitleReturn);
	    ibtnTitleReturn.setOnClickListener(this);
		mApp = (HFBusApp) getApplication();
		mApp.getmActivityManager().add(this);
		hasNext = true;
		page = 0;
		pageSize = 3;

		data = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("str", "获取数据.....");
		data.add(map);
		myListView = (ListView) findViewById(R.id.list_out_prompt);
		adapter = new AdapterOutPromptList(getApplicationContext(), data);
		myListView.setAdapter(adapter);
		myListView.setOnItemClickListener(this);

		Request request = new Request();
		request.query(new RequestGetOutPrompt(this));

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (position == data.size() - 1) {
			System.out.println(this.getClass() + ">>>" + hasNext);
			if (hasNext) {
				
				Request request = new Request();
				request.query(new RequestGetOutPrompt(this));
			} else {
				Toast.makeText(getApplicationContext(), "无更多数据！", 500).show();
			}

		} else {
			Intent it = new Intent();
			Map map = (Map) myListView.getAdapter().getItem(position);
			String id2 = (String) map.get("ID");

			if ("-1".equals(id2)) {
				Toast.makeText(getApplicationContext(), "数据出错，无法获取内容！", 400)
						.show();

				return;
			}
			System.out.println(this.getClass() + "Map" + map);
			System.out.println(this.getClass() + "id2" + id2);

			it.putExtra("TITLE", map.get("str").toString());
			it.putExtra("ID", id2);
			it.setClass(OutPrompt.this, OutPromptDetail.class);
			startActivity(it);

		}
	}

	class RequestGetOutPrompt extends RequestImpl {

		public RequestGetOutPrompt(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Object doCall() {
			// TODO Auto-generated method stub
			//String url = "http://218.22.27.75:80/Kljtxxfww_1_0_v/hessian/wzlmServiceOpenAPI";
			WzlmServiceOpenAPI mWzlmServiceOpenAPI;
			try {
				mWzlmServiceOpenAPI = (WzlmServiceOpenAPI) factory.create(
						WzlmServiceOpenAPI.class, url_wzlm, this.mContext.get()
								.getClassLoader());
				/*
				 * mWzlmServiceOpenAPI = (WzlmServiceOpenAPI)
				 * factory.create(WzlmServiceOpenAPI.class, url );
				 */
				page++;
				Map map = mWzlmServiceOpenAPI.findGjgxtzWzxxList("wzlmgl",
						"wzlmgl", url_wzlm, page, pageSize);
				System.out.println(this.getClass() + ">>>>>>>page" + page);
				System.out.println(map);
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

			Map m = (Map) o;
			System.out.println(m);

			List list = (List) m.get("wzxxList");
			if (m.get("selectPage") != null) {
				int selectPage = (Integer) m.get("selectPage");
				if (selectPage < page) {
					Toast.makeText(getApplicationContext(), "无更多数据！", 500)
							.show();
					hasNext = false;
				 
					return;
				}
			}
			System.out.println(this.getClass()+"标记");
			if (list != null) {
				data.remove(data.size()-1);
				HashMap<String, Object> map;
				String title, id;
				for (int i = 0; i < list.size(); i++) {
					map = new HashMap<String, Object>();
					map.put("ico", R.id.img_station);
					Map tempMap = (Map) list.get(i);

					if (tempMap.get("TITLE") != null) {
						title = tempMap.get("TITLE").toString();
					} else {
						title = "无数据";
					}

					map.put("str", title);

					if (tempMap.get("ID") != null) {
						id = tempMap.get("ID").toString();
					} else {
						id = "-1";
					}
					map.put("ID", id);
					data.add(map);
				}
				map = new HashMap<String, Object>();

				map.put("str", "更多.....");
				data.add(map);
				adapter.notifyDataSetChanged();
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
