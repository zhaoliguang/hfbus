package com.keli.hfbus.hessianserver;

import android.os.Handler;
import android.os.Message;

/**
 * ����ͻ��˷�������Ķ���
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
     * ��ѯ,���ӷ���������
     * @param iRequest �ص�����ӿ�
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
        	
        	/// ����ѯ��ʱ���߳� shaolong 2013-04-07 update
        	new Thread(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					int count = 0;
					while(!qt.isFinished()){
						++count ;
						if(count > 300){
							// ���ӳ�ʱ 30�� �˴���Ҫ��hessian��ʱʱ��һ��
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

    	private boolean finished = false;//���߳��Ƿ����
    	private boolean donot = false;//���߳��Ƿ�ʱ,��ʱ����Ҫ����������
    	/**�����Ƿ����,��Ҫ���hessian�ȽϺ�ʱ�Ĳ���*/
    	public boolean isFinished(){
    		return finished;
    	}
    	/**���ò���Ҫ������,�ڳ�ʱ��*/
    	public void setDonot(boolean donot){
    		this.donot =  donot;
    	}
    	
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Message msg = mHandler.obtainMessage();
			try{
				
				finished = false;//����ǰ
				// ht.call(null, envelope);
				Object result = iRequest.doCall();
				
                finished = true;//�����
				
				if(donot)return;
				
				if(result == null){// ����null��ʾ����ǿ�,���ǲ�ѯ�ѳɹ�
//					msg.what = REQUEST_TIME_OUT;
//					mHandler.sendMessage(msg);
					
					msg.what = REQUEST_ERROR;
					msg.obj = null;
					mHandler.sendMessage(msg);
					
				}else{// �з���ֵ��ʾ����ɹ�
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
			}catch(Exception e){// ����--���ز�������
				finished = true;//�����
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
     * ��Ϣ����
     */
    public Handler mHandler = new Handler() {
    	public void handleMessage(Message msg) {
    		
    		switch(msg.what) {
    			case REQUEST_ERROR://�������
    				if(iRequest!=null)
    				    iRequest.doError((Exception)msg.obj);
    				break;
    			case REQUEST_TIME_OUT://����ʱ
    				if(iRequest!=null)
    					iRequest.doTimeout();
    				break;
    			case REQUEST_COMPLETED://�������
    				if(iRequest!=null){
					   iRequest.doComplete(msg.obj);
    				}
    				break;
    		}
    	}
    };
}
