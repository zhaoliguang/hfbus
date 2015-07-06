package com.keli.hfbus.view;
import com.amap.mapapi.map.MapView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ��д��ͼ����
 * @since ��Ҫ��������onTouchEvent�¼�
 * @since �����ͼ�϶�ʱ·�����ظ���Ⱦ
 */
public class PopMapView extends MapView {
	boolean needDraw = true;
	public PopMapView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if(e.getAction()==MotionEvent.ACTION_DOWN||e.getAction() == MotionEvent.ACTION_MOVE){
			needDraw = false;
		}else if(e.getAction() == MotionEvent.ACTION_UP){
			needDraw = true;
//			System.out.println("========RttMapView.needDraw:"+needDraw+"========");
		}
		return super.onTouchEvent(e);
	}
	
	
	/**�Ƿ���Ҫ����*/
	public boolean getNeedDraw(){
		return needDraw;
	}
}
