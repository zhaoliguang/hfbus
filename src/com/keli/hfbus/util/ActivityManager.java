package com.keli.hfbus.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import android.app.Activity;

/**
 * ��¼activity����
 * ͬʱ����activity��ת
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
			System.out.println("���activity");
		} else {//�Ѿ����ڣ�finish��ɾ��ԭ�е�acitvity
			System.out.println("�Ѵ���activity");
			int index= indexOf(act);
			data.get(index).finish();
			data.remove(index);
			//ɾ��ԭ�еģ�����µ�
			data.add (act);
		}
	}
	
	/** 
	 * ɾ��act 
	 */
     public void remove (Activity act)
    {
    	int index=indexOf(act);
    	  data.remove(index);
    }
    /**
     * ��������act
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
	 * ɾ�� 
	 */
	private  void remove(int index) {
		System.out.println("����activity");
		  data.remove(index);
	}

 
	/** 
	 * �ж�act�Ƿ����
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
	 * ���act��λ��   
	 * @param act
	 * @return λ�ã�-1��ʾ������
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
