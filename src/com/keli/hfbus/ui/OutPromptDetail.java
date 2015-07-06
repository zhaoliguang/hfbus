package com.keli.hfbus.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.keli.hfbus.HFBusApp;
import com.keli.hfbus.R;
import com.keli.hfbus.adapt.AdapterOutPromptList;
import com.keli.hfbus.hessianserver.Request;
import com.keli.hfbus.hessianserver.RequestImpl;
import com.keli.hfbus.hessianserver.WzlmServiceOpenAPI;
import com.keli.hfbus.ui.OutPrompt.RequestGetOutPrompt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OutPromptDetail extends Activity implements OnClickListener{
	 
	private ListView myListView;
	private AdapterOutPromptList adapter;
	private TextView textTitle, textDate,textContent;
	private String id, title;
	private String mData;
	private ImageButton ibtnTitleReturn;
	private HFBusApp mApp;
	protected static final int MENU_1= Menu.FIRST+1;
	protected static final int MENU_2= Menu.FIRST+2;
	protected static final int MENU_3 = Menu.FIRST+3;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.out_prompt_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
		
		ibtnTitleReturn=(ImageButton)findViewById(R.id.ibtnTitleReturn);
        ibtnTitleReturn.setOnClickListener(this);
		mApp = (HFBusApp) getApplication();
		mApp.getmActivityManager().add(this);
		Intent it = getIntent();
		id = it.getStringExtra("ID");
		title = it.getStringExtra("TITLE");
		System.out.println(OutPromptDetail.this.getClass() + "id" + id);
        textContent=(TextView) findViewById(R.id.textContent);
		textTitle=(TextView) findViewById(R.id.textTitle);
		textDate = (TextView) findViewById(R.id.textDate);
		textContent.setMovementMethod(ScrollingMovementMethod.getInstance());
	 
		textTitle.setText(title);

		Request request = new Request();
		request.query(new GetOutPromptDetail(this));

	}

	class GetOutPromptDetail extends RequestImpl {

		public GetOutPromptDetail(Context context) {
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

				Map map = mWzlmServiceOpenAPI.findWzxxById("wzlmgl", "wzlmgl",
						url_wzlm, id);
                return map;
				 
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
			System.out.println(OutPromptDetail.this.getClass() + ">>>" + m);
			List list = (List) m.get("wzxxList");
			if (list != null) {
				Map map;
				map = (Map) list.get(0);
				String date;
				if (map.get("CONTENT") != null) {
					mData = map.get("CONTENT").toString();
				} else {
					mData = "无数据";
				}
				SimpleDateFormat dateformatAll= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date temDate=null;
				if (map.get("ADD_DATE") != null) {
				
					date = map.get("ADD_DATE").toString();
					
					try {
						temDate=dateformatAll.parse(date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					date=dateformatAll.format(temDate);
				} else {
					date = "无数据";
				}
				
			
				System.out.println("date:"+date);
				textDate.setText(date);
				textContent.setText(Html.fromHtml(mData));
			}

		}
	}
	
	
	 @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		 
			super.onCreateOptionsMenu(menu);
			
			//menu.add(groupId, itemId, order, title)
			menu.add(0, MENU_1, 1, "GBK");
			menu.add(0, MENU_2, 2, "UTF-8");
			menu.add(0, MENU_3, 3, "GB2312");
		return true;
	}
	 
	 @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		  super.onOptionsItemSelected(item);
		switch(item.getItemId())
		{
		case MENU_1:
			System.out.println("GBK");
		 
			break;
		case MENU_2:
			System.out.println("UTF-8");
			 
			break;
		case MENU_3:
			System.out.println("GB2312");
			 
			break;
		
		}
		 
		 return true;
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
