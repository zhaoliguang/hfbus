package com.keli.hfbus.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class MyTimeList extends ArrayList<Date> implements Serializable,Parcelable{
	
	@Override
	public boolean contains(Object object) {
		// TODO Auto-generated method stub
		Date date=(Date)object;
		Date dateOld;
		for(int i=0;i<this.size();i++){
			dateOld=this.get(i);
			if(dateOld.getHours()==date.getHours()&&dateOld.getMinutes()==date.getMinutes()){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean add(Date object) {
		// TODO Auto-generated method stub
		Date date=(Date)object;
		Date dateOld;
		int i=0;
		for(i=0;i<this.size();i++){
			dateOld=this.get(i);
			if(date.before(dateOld)){
				
			    this.add(i, date);
				return true;
			}
		}
		this.add(i, date);
		return true;
		
	}
	
	@Override
	public boolean remove(Object oldTime) {
		// TODO Auto-generated method stub
		Date date;
		String tmpTime=(String)oldTime;
		for(int i=0;i<this.size();i++){
			date=this.get(i);
			int hour=date.getHours();
			int minute=date.getMinutes();
			String time=Utils.formatTime(hour)+":"+Utils.formatTime(minute);
			System.out.println("timeList"+time);
			if(time.equals(tmpTime)){
				
				this.remove(i);
				return true;
			}
			
		}
		return false;
	}
	public String getTimeString(){
		String times="";
		Date date;
		for(int i=0;i<this.size();i++){
			date=(Date)this.get(i);
			int hour=date.getHours();
			int minutes=date.getMinutes();
			times+=Utils.formatTime(hour)+":"+Utils.formatTime(minutes)+",";
		}
		if(times!=null&&times.length()>1){
			times=times.substring(0, times.length()-1);
		}
		return times;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}