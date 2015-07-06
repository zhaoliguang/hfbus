package com.keli.hfbus.hessianserver;

import android.os.Handler;
import android.os.Message;

/**
 * 处理客户端发送请求的对象
 * @author shaolong
 * @category 2013-01-15 
 */
public class Request {
//    WeakReference<Context> mContext;
    IRequest iRequest = null;
    String url;
    Object response;
    
    public Request(//Context context
    		){
//    	this.mContext = new WeakReference<Context>(context);
    }
    
    
    /**
     * 查询,连接服务器请求
     * @param iRequest 回调处理接口
     */
    public void query(IRequest iRequest){
    	this.iRequest = iRequest;
    	
    	if(iRequest != null){
    		if(iRequest instanceof RequestImpl){
    			((RequestImpl)iRequest).IsQuery(true);
    		}
    		iRequest.doLoading();
    		final queryThread qt = new queryThread();
        	qt.start();
        	
        	/// 检测查询超时的线程 shaolong 2013-04-07 update
        	new Thread(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					int count = 0;
					while(!qt.isFinished()){
						++count ;
						if(count > 300){
							// 连接超时 30秒 此处需要跟hessian超时时间一致
							Message msg = mHandler.obtainMessage();
							msg.what = REQUEST_TIME_OUT;
							mHandler.sendMessage(msg);
							qt.setDonot(true);
							break;
						}
						try{
							sleep(150);
						}catch(Exception e){}
					}
//					System.out.println("timeout thread exit");
				}
        		
        		
        	}.start();
    	}
    	
    }
    
    class queryThread extends Thread{

    	private boolean finished = false;//本线程是否完成
    	private boolean donot = false;//本线程是否超时,超时后不需要作后续操作
    	/**请求是否结束,主要检查hessian比较耗时的操作*/
    	public boolean isFinished(){
    		return finished;
    	}
    	/**设置不需要作处理,在超时后*/
    	public void setDonot(boolean donot){
    		this.donot =  donot;
    	}
    	
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Message msg = mHandler.obtainMessage();
			try{
				
				finished = false;//请求前
				// ht.call(null, envelope);
				Object result = iRequest.doCall();
				
                finished = true;//请求后
				
				if(donot)return;
				
				if(result == null){// 返回null表示结果是空,但是查询已成功
//					msg.what = REQUEST_TIME_OUT;
//					mHandler.sendMessage(msg);
					
					msg.what = REQUEST_ERROR;
					msg.obj = null;
					mHandler.sendMessage(msg);
					
				}else{// 有返回值表示请求成功
					if(result instanceof Exception){
						msg.what = REQUEST_ERROR;
						msg.obj = result;
						mHandler.sendMessage(msg);
					}else{
						msg.what = REQUEST_COMPLETED;
						msg.obj = result;
						mHandler.sendMessage(msg);
					}
					
				}
				if(iRequest instanceof RequestImpl){
	    			((RequestImpl)iRequest).IsQuery(false);
	    		}
			}catch(Exception e){// 报错--本地操作报错
				finished = true;//请求后
				if(donot)return;
				System.out.println("Request Error :" +e.getMessage());
				msg.what = REQUEST_ERROR;
				msg.obj = e;
				mHandler.sendMessage(msg);
				if(iRequest instanceof RequestImpl){
	    			((RequestImpl)iRequest).IsQuery(false);
	    		}
			}
			
		}
    	
    }
    
    public final int REQUEST_COMPLETED = 2;
	public final int REQUEST_ERROR = -1;
	public final int REQUEST_TIME_OUT = 1;
//	public final int REQUEST_OTHER_TASK = 3;
    
    /**
     * 消息处理
     */
    public Handler mHandler = new Handler() {
    	public void handleMessage(Message msg) {
    		
    		switch(msg.what) {
    			case REQUEST_ERROR://请求出错
    				if(iRequest!=null)
    				    iRequest.doError((Exception)msg.obj);
    				break;
    			case REQUEST_TIME_OUT://请求超时
    				if(iRequest!=null)
    					iRequest.doTimeout();
    				break;
    			case REQUEST_COMPLETED://请求完成
    				if(iRequest!=null){
					   iRequest.doComplete(msg.obj);
    				}
    				break;
    		}
    	}
    };
}
