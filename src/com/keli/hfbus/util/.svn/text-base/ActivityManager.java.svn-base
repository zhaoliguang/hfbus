package com.keli.hfbus.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import android.app.Activity;

/**
 * 记录activity的类
 * 同时控制activity跳转
 * @author Administrator
 * 
 */
public class ActivityManager {
	List<Activity> data;

	public ActivityManager() {
		data = new ArrayList<Activity>();
	}

	public void add(Activity act) {
	 
		if (! contains(act)) {
			
			data.add (act);
			System.out.println("添加activity");
		} else {//已经存在，finish并删除原有的acitvity
			System.out.println("已存在activity");
			int index= indexOf(act);
			data.get(index).finish();
			data.remove(index);
			//删除原有的，添加新的
			data.add (act);
		}
	}
	
	/** 
	 * 删除act 
	 */
     public void remove (Activity act)
    {
    	int index=indexOf(act);
    	  data.remove(index);
    }
    /**
     * 结束所有act
     */
	public void finishAll()
	{
		Iterator<Activity> iter= data.iterator();
		while(iter.hasNext())
		{
			
			Activity act=  iter.next() ;
			System.out.println(act.getClass().getSimpleName());
			act.finish();
			
		}
	}
	
	
	/** 
	 * 删除 
	 */
	private  void remove(int index) {
		System.out.println("弹出activity");
		  data.remove(index);
	}

 
	/** 
	 * 判断act是否存在
	 */
	private boolean contains(Activity act)
	{
		String className=act.getClass().getSimpleName();
		                   
		 Iterator<Activity> iter=	data.iterator();
		 while(iter.hasNext())
		 {
			 Activity activity=iter.next();
			if( activity.getClass().getSimpleName().equals(className))
			{
				return true;
			}
		 }
	     return false;
	}
	/**
	 * 获得act的位置   
	 * @param act
	 * @return 位置，-1表示不存在
	 */
	 
	private int indexOf(Activity act)
	{
		String className=act.getClass().getSimpleName();
        
		 Iterator<Activity> iter=	data.iterator();
		 int i=-1;
		 while(iter.hasNext())
		 {
			 i++;
			 Activity activity=iter.next();
			if( activity.getClass().getSimpleName().equals(className))
			{
				return i;
			}
		 }
	     return i;
	 
	 	
	}
}
