package com.keli.hfbus.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.Overlay;
import com.amap.mapapi.map.Projection;
import com.keli.hfbus.R;
import com.keli.hfbus.util.Utils;

/**地图上添加我的位置图层*/
public class GpsOverlay extends Overlay{

	private Context mContext;
	private double mLat = 31.864856; // 纬度
	private double mLon = 117.287316;// 经度
	private float bearing = 30;// 旋转度
	private int rid;// 图片id

	public GpsOverlay(){}
	
	public GpsOverlay(Context context,double lon,double lat,float bearing,int rid){
		this.mLon = lon;
		this.mLat = lat;
		this.bearing = bearing;
		this.rid = rid;
		this.mContext = context;
	}

	@Override
	public boolean draw(Canvas arg0, MapView arg1, boolean arg2, long arg3) {
		// TODO Auto-generated method stub
		
		//Location location = TrafficNaviApp.mApp.getCurrentLocation();
	//	if(location==null)return super.draw(arg0, arg1, arg2, arg3);
	//	double mLon = location.getLongitude();
		//double mLat = location.getLatitude();
//		float bearing = location.getBearing();
		GeoPoint gp = new GeoPoint((int) ((mLat+Utils.Y_OFFSET) * 1E6), 
				(int) ((mLon+Utils.X_OFFSET) * 1E6));
		
		Projection projection = arg1.getProjection();
		
		Point point = projection.toPixels(gp, null);
		
		// 画圆
//		Paint paint = new Paint();
//		paint.setColor(Color.RED);
//		arg0.drawCircle(point.x, point.y, 6, paint);
		
		// 画定位图标
//		Matrix matrix=new Matrix();
//        matrix.setRotate(bearing);//图像旋转
//        Bitmap bm = BitmapFactory.decodeStream(mContext.getResources().openRawResource(rid)
//    			,null,null);
        Bitmap bm = BitmapFactory.decodeStream(mContext.getResources().openRawResource(R.drawable.map_background)
    			,null,null);
//        Bitmap resizeBitmap=Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        arg0.drawBitmap(bm, (float) (point.x-20), (float) (point.y-52), null);
        
        // 画文字
//        paint.setTextSize(22);
//        paint.setStrokeWidth(6);
//        paint.setAntiAlias(true);
//        paint.setColor(Color.RED);
//        
//        arg0.drawText("我的位置", (float) (point.x+16), (float) (point.y), paint);
        
		return super.draw(arg0, arg1, arg2, arg3);
	}
}
