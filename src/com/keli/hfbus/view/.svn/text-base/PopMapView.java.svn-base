package com.keli.hfbus.view;
import com.amap.mapapi.map.MapView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 重写地图对象
 * @since 主要用来重载onTouchEvent事件
 * @since 避免地图拖动时路况层重复渲染
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
	
	
	/**是否需要绘制*/
	public boolean getNeedDraw(){
		return needDraw;
	}
}
