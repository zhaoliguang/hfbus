package com.keli.hfbus.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.RadialGradient;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.keli.hfbus.HFBusApp;
import com.keli.hfbus.R;
import com.keli.hfbus.adapt.ChangeBusAdapter;
import com.keli.hfbus.hessianserver.GjcxService;
import com.keli.hfbus.hessianserver.Request;
import com.keli.hfbus.hessianserver.RequestImpl;
import com.keli.hfbus.sqlite.BusDal;
import com.keli.hfbus.ui.ChangeBusItem.RequestChangeBusQuery;

public class ChangeBusActivity extends Activity implements OnClickListener,OnItemClickListener,OnFocusChangeListener,OnCheckedChangeListener{
	
	ImageButton ibtnQueryLinear,ibtnTitleReturn;
	EditText edStart,edEnd,currentEdit;
	String startStation,endStation;
	private Map data;
	ListView startList,endList,currentList;
	private RadioButton rdFast,rdLessChange,rdLessFoot;
	private RadioGroup rgChange;
	private int checked;
	HFBusApp mApp;
	BusDal busDal;
	String key;
	//Map<String,Object> data=new Map<String,Object>();;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.change_bus);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
		 ibtnTitleReturn=(ImageButton)findViewById(R.id.ibtnTitleReturn);
	        ibtnTitleReturn.setOnClickListener(this);
		 mApp = (HFBusApp) getApplication();
	     mApp.getmActivityManager().add(this);
	     busDal=new BusDal(this);
		ibtnQueryLinear=(ImageButton)findViewById(R.id.ibtnQueryLinear);
		ibtnQueryLinear.setOnClickListener(this);
		edStart=(EditText)findViewById(R.id.edStart);
		edStart.setOnFocusChangeListener(this);
		edStart.addTextChangedListener(new startTextWatcher());
		edEnd=(EditText)findViewById(R.id.edEnd);
		edEnd.setOnFocusChangeListener(this);
		edEnd.addTextChangedListener(new startTextWatcher());
		startList=(ListView)findViewById(R.id.startList);
		startList.setOnItemClickListener(this);
		endList=(ListView)findViewById(R.id.endList);
		endList.setOnItemClickListener(this);
		rdFast=(RadioButton)findViewById(R.id.rdFast);
		rdLessChange=(RadioButton)findViewById(R.id.rdLessChange);
		rdLessFoot=(RadioButton)findViewById(R.id.rdLessFoot);
		rgChange=(RadioGroup)findViewById(R.id.rgChange);
		rgChange.setOnCheckedChangeListener(this);
		checked=0;
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.ibtnTitleReturn:
			super.onBackPressed();
			break;
		case R.id.ibtnQueryLinear:
			
			startStation=edStart.getText().toString().trim();
			endStation=edEnd.getText().toString().trim();
			if(startStation==null||startStation.equals("")){
				Toast.makeText(ChangeBusActivity.this,"请输入起始站点",Toast.LENGTH_SHORT).show();
				return;
			}
			if(endStation==null||endStation.equals("")){
				Toast.makeText(ChangeBusActivity.this,"请输入终点站点",Toast.LENGTH_SHORT).show();
				return;
			}
			if(startStation.equals("火车站")){
				startStation="中绿广场";
			}
			if(endStation.equals("火车站")){
				endStation="中绿广场";
			}
			if(startStation.equals(endStation))
			{
				Toast.makeText(ChangeBusActivity.this, "起始站点和终点站相同，请重新输入！", 30000).show();
				return;
			}
				Intent intent=new Intent();
				intent.putExtra("startStation", startStation);
				intent.putExtra("endStation", endStation);
				intent.putExtra("changeType", checked);
				intent.setClass(ChangeBusActivity.this, ChangeBusItem.class);
				startActivity(intent);
			
//			
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
			// TODO Auto-generated method stub
			if(edStart.isFocused()){
				currentEdit=edStart;
				currentList=startList;
			}else{
				currentEdit=edEnd;
				currentList=endList;
			}
				
			
			String key=currentEdit.getText().toString().trim();
			  if(key!=null && !"".equals(key.trim())){  
                  List<Map<String,?>> stationList = busDal.selectStation(key);
                  SimpleAdapter stationAdapter=new SimpleAdapter(ChangeBusActivity.this, stationList, R.layout.station_name_list, new String [] {"stationName"}, new int []{R.id.stationName});
                //  ArrayAdapter stationAdapter = new ArrayAdapter<String>(ChangeBusActivity.this, android.R.layout.simple_spinner_item, stationList);  
                  currentList.setAdapter(stationAdapter);  
              }else{  
            	  currentList.setAdapter(null);  
              }  
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
		LinearLayout llStation=(LinearLayout)view;
		TextView text=(TextView)llStation.findViewById(R.id.stationName);
		switch(parent.getId()){
		case R.id.startList:
			edStart.setText(text.getText().toString().trim());
			edStart.setSelection(edStart.getText().length());
			startList.setAdapter(null);
			break;
		case R.id.endList:
			edEnd.setText(text.getText().toString().trim());
			edEnd.setSelection(edEnd.getText().length());
			endList.setAdapter(null);
			break;
		}
	}
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		edEnd.setSelection(edEnd.getText().length());
		edStart.setSelection(edStart.getText().length());
		switch(v.getId()){
		case R.id.edStart:
			if(edStart.hasFocus()){
				endList.setAdapter(null);
			}
			break;
		case R.id.edEnd:
			if(edEnd.hasFocus()){
				startList.setAdapter(null);
			}
			break;
		}
		
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if(checkedId==R.id.rdFast){
			checked=0;
		}else if(checkedId==R.id.rdLessChange){
			checked=1;
		}else if(checkedId==R.id.rdLessFoot){
			checked=2;
		}
	}
	


	
}
