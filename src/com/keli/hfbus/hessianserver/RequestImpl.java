package com.keli.hfbus.hessianserver;

import java.lang.ref.WeakReference;

import com.caucho.hessian.client.HessianProxyFactory;
import com.keli.hfbus.HFBusApp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * TCP请求回发处理
 * 
 * @since 预处理一些对象,如加载提示、超时处理、失败处理等。
 * 也可以直接继承IRequest接口，处理相关hessian请求操作
 * 
 * @author shaolong
 */
public abstract class RequestImpl implements IRequest{

	protected HessianProxyFactory factory;
	
	// 公交查询 
	protected String url_gjcx = HFBusApp.mApp.getHessianurls()[0];//"http://218.22.27.69:9080/Kljtxxfww_1_0_v/hessian/gjcxService";
	// 用户管理
	protected String url_yhxx = HFBusApp.mApp.getHessianurls()[1];//"http://218.22.27.75:80/Kljtxxfww_1_0_v/hessian/userManagerService";
	// 文章栏目 
	protected String url_wzlm = HFBusApp.mApp.getHessianurls()[2];//"http://218.22.27.75:80/Kljtxxfww_1_0_v/hessian/wzlmServiceOpenAPI";
	
	private boolean isQuery = false;//是否正在查询
	
	public boolean IsQuery(){
		return isQuery;
	}
	public void IsQuery(boolean isQuery){
		this.isQuery = isQuery;
	}
	
	@Override
	public Object doCall() {
		// TODO Auto-generated method stub
		return this.HessianVoid;
	}

	protected WeakReference<Context> mContext;
	protected String loadingMessage = "正在通讯获取数据，请稍候...";
	
	protected ProgressDialog pb;
	
	/**
	 * 构造函数
	 * @param context
	 */
	public RequestImpl(Context context){
		this.mContext = new WeakReference<Context>(context);
		factory = HFBusApp.mApp.getHessianFactory();
//		factory.setHessian2Reply(false);
//    	factory.setReadTimeout(30000);
	}
	
	/**
	 * 构造函数
	 * @param context
	 * @param loadingMessage Loading 内容
	 */
	public RequestImpl(Context context,String loadingMessage){
		this.mContext = new WeakReference<Context>(context);
		if(!loadingMessage.equals(""))
		    this.loadingMessage = loadingMessage;
	}
	
	@Override
	public void doLoading() {
		// TODO Auto-generated method stub
		pb = new ProgressDialog(mContext.get());
		pb.setMessage(this.loadingMessage);
//		pb.setOnKeyListener(new OnKeyListener(){
//
//			@Override
//			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
//				// TODO Auto-generated method stub
//				if(arg1==KeyEvent.KEYCODE_BACK){
//					Toast.makeText(mContext.get(), "请求中,请稍候...", Toast.LENGTH_SHORT).show();
//				}else if(arg1==KeyEvent.KEYCODE_HOME){
//					Toast.makeText(mContext.get(), "请求中不能返回桌面", Toast.LENGTH_SHORT).show();
//				}
//				return true;
//			}
//			
//		});
		pb.show();
	}


	@Override
	public void doComplete(Object o) {
		// TODO Auto-generated method stub
		try{
			if(pb != null){
				pb.dismiss();
				pb = null;
			}
		}catch(Exception e){}
		
	}

	@Override
	public void doTimeout() {
		// TODO Auto-generated method stub
        try{
        	if(pb != null){
    			pb.dismiss();
    			pb = null;
    		}
        	Toast.makeText(mContext.get(),"连接服务器超时,请重试!",Toast.LENGTH_LONG).show();
		}catch(Exception e){}
		
	}

	@Override
	public void doError(Exception e) {
		// TODO Auto-generated method stub
        try{
        	if(pb != null){
    			pb.dismiss();
    			pb = null;
    		}
        	if(e==null){
//        		Toast.makeText(mContext.get(),"请求出错:发送请求失败,请重试!",Toast.LENGTH_LONG).show();
        	}else{
        		Toast.makeText(mContext.get(),"发送请求失败,请重试!",Toast.LENGTH_SHORT).show();
        	}
		}catch(Exception ex){}
		
	}

}
