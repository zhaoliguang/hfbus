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

/**��ͼ������ҵ�λ��ͼ��*/
public class GpsOverlay extends Overlay{

	private Context mContext;
	private double mLat = 31.864856; // γ��
	private double mLon = 117.287316;// ����
	private float bearing = 30;// ��ת��
	private int rid;// ͼƬid

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
		
		// ��Բ
//		Paint paint = new Paint();
//		paint.setColor(Color.RED);
//		arg0.drawCircle(point.x, point.y, 6, paint);
		
		// ����λͼ��
//		Matrix matrix=new Matrix();
//        matrix.setRotate(bearing);//ͼ����ת
//        Bitmap bm = BitmapFactory.decodeStream(mContext.getResources().openRawResource(rid)
//    			,null,null);
        Bitmap bm = BitmapFactory.decodeStream(mContext.getResources().openRawResource(R.drawable.map_background)
    			,null,null);
//        Bitmap resizeBitmap=Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        arg0.drawBitmap(bm, (float) (point.x-20), (float) (point.y-52), null);
        
        // ������
//        paint.setTextSize(22);
//        paint.setStrokeWidth(6);
//        paint.setAntiAlias(true);
//        paint.setColor(Color.RED);
//        
//        arg0.drawText("�ҵ�λ��", (float) (point.x+16), (float) (point.y), paint);
        
		return super.draw(arg0, arg1, arg2, arg3);
	}
}
