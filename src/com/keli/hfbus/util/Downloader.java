package com.keli.hfbus.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * ���߳��ļ����ع���
 * �ļ��ְ�����
 * ��������ٶ�
 * @see FileDownloadThread
 */
public class Downloader {
	
	/**���ؼ���*/
	public interface OnDownloadListener {
		/**
		 * ������ɻط��¼�
		 * @param data
		 */
	    public void onDownloadCompleted(byte[] data);
	    /**
	     * ���س���ط��¼�
	     * @param e
	     */
	    public void onDownloadError(Exception e);
	    /**
	     * ���ؽ��Ȼط��¼�
	     * @param size �ܴ�С
	     * @param completed �Ѿ�����
	     */
	    public void onDownloadProgress(int size,int completed);
	}
	
	/**������**/
	private int downloadedSize = 0;
	/**�ļ���С**/
	private int fileSize = 0;
	/***�����߳���**/
	private int downloadThreadNum = 1;//
	/**���ص�ַ*/
	private String downloadUrl = "";//
	/**�ļ�����·��*/
	private String downloadFilePath = "";//
	protected  Context mContext;
	
	private int DOWNLOAD_COMPLETED = 1;
	private int DOWNLOAD_ERROR = 2;
	private int DOWNLOAD_PROGRESS = 3;
	
	/**
	 * ��ȡ������ �ļ���������Ƿ񷵻��ֽ�����
	 * @since �����OnDownloadListener��onDownloadCompleted����Ҫʹ��
	 * @since Byte[]����������true,�������Ҫʹ��Byte[]������������false�Լ����ڴ��˷�
	 * @see OnDownloadListener
	 */
	public boolean isReturnByteData = true;
	
    /***���ؼ����¼�*/
	private OnDownloadListener onDownloadListener = null;
    
	/**
	 * ���������¼�����
	 * @param listener
	 */
	public void setOnDownloadListener(OnDownloadListener listener){
		onDownloadListener = listener;
	};
	
	/**
	 * Downloader ���캯��
	 * @param context 
	 * @param url ���ص�ַ
	 * @param filePath �ļ�����·��
	 * @param threadNum �������߳��� ����0,С�ļ�ֱ������1���
	 */
	public Downloader(Context context,String url,
			String filePath,int threadNum){
		mContext = context;
		downloadUrl = url;
		downloadFilePath = filePath;
		if(threadNum > 0)downloadThreadNum = threadNum;
	}

	/**��ʼ����*/
	public void download() {
		if(onDownloadListener != null){
			onDownloadListener.onDownloadProgress(fileSize, downloadedSize);
        }
		if(downloadThreadNum <1)downloadThreadNum = 1;
		try{
			// �ļ���·���������򴴽�
		    String dir = downloadFilePath.substring(0, downloadFilePath.lastIndexOf("/"));
		    File destDir = new File(dir);
		    if (!destDir.exists()) {
		    	System.out.println("����Ŀ¼");
		        destDir.mkdirs();
		    }
		}
		catch(Exception e)
		{
			
			if(onDownloadListener != null){
				onDownloadListener.onDownloadError(e);
	        }
			return;
		}

		downloadTask doWork = new downloadTask(downloadUrl, downloadThreadNum,downloadFilePath);
		doWork.setName("Thread(downloadTask)("+downloadUrl+")");
		doWork.start();
	}
	
	/**���ػط�����*/
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == DOWNLOAD_COMPLETED){//���
				if(onDownloadListener != null){
				   byte[]bData = null;
				   if(isReturnByteData && msg.obj!=null)bData=(byte[])msg.obj;
				   onDownloadListener.onDownloadCompleted(bData);
                }
			}
			if(msg.what == DOWNLOAD_ERROR){//����
				if(onDownloadListener != null){
					if(msg.obj != null){
						onDownloadListener.onDownloadError((Exception)msg.obj);
					}
                }
			}
			if(msg.what == DOWNLOAD_PROGRESS){//������
				if(onDownloadListener != null){
					onDownloadListener.onDownloadProgress(fileSize, downloadedSize);
                }
			}
		}
	};
 
	/**������ҵ,һ���µ��߳�**/
     class downloadTask extends Thread {
		private int blockSize, downloadSizeMore;
		private int threadNum = 5;
		String urlStr, threadNo, fileName;
        
		public downloadTask(String urlStr, int threadNum, String fileName) {
			this.urlStr = urlStr;
			this.threadNum = threadNum;
			this.fileName = fileName;
		}
 
		@Override
		public void run() {
			try {
				URL url = new URL(urlStr);
				URLConnection conn = url.openConnection();
				//��ȡ�����ļ����ܴ�С
				fileSize = conn.getContentLength();
				File file = new File(fileName);
				
				//-----------------����û���ļ���������·������ʱ�޷�ȷ���ļ���С���޷��ְ���ֻ�ܵ��߳�����----------------
				if(fileSize < 0){
					InputStream is = conn.getInputStream();
					fileSize = is.available();
					Message message = handler.obtainMessage(DOWNLOAD_PROGRESS, null);
                    handler.sendMessage(message);
					RandomAccessFile fos = new RandomAccessFile(file, "rw");
					byte[] bts = new byte[1024];
					
					ByteArrayOutputStream baos = null;
					if(isReturnByteData)baos= new ByteArrayOutputStream();
					
					int len = 0;
					while((len = is.read(bts)) != -1){
						fos.write(bts, 0, len);
						
						if(isReturnByteData)baos.write(bts, 0, len);
						
						downloadedSize += len;
						Message message1 = handler.obtainMessage(DOWNLOAD_PROGRESS, null);
	                    handler.sendMessage(message1);
					}
					
					byte[]bData = null;
					if(isReturnByteData)bData=baos.toByteArray();
					
					Message message1 = handler.obtainMessage(DOWNLOAD_COMPLETED, bData);
                    handler.sendMessage(message1);
                    
					is.close();
					fos.close();
					return;
				}//---------------------------------------------------------------------------------------------------
				
				// ���̷ְ߳����� 
				FileDownloadThread[] fds = new FileDownloadThread[threadNum];
				// ����ÿ���߳�Ҫ���ص�������
				blockSize = fileSize / threadNum;
				// ���������ٷֱȼ������
				downloadSizeMore = (fileSize % threadNum);
				
				for (int i = 0; i < threadNum; i++) {
					// �����̣߳��ֱ������Լ���Ҫ���صĲ���
					int start = i * blockSize;
					int end = (i + 1) * blockSize - 1;
					// ���һ���߳� �������ӽ�ȥ
					if(i==threadNum-1)end = end + downloadSizeMore;
					FileDownloadThread fdt = new FileDownloadThread(url, file,
							start, end);
					fdt.setName("Thread(downloadTask)("+downloadUrl+"):" + i);
					fdt.start();
					fds[i] = fdt;
				}
				
				// ѭ����������Ƿ����
				boolean finished = false;
				while (!finished) {
					finished = true;
					downloadedSize = 0;
					for (int i = 0; i < fds.length; i++) {
						downloadedSize += fds[i].getDownloadSize();
						if (!fds[i].isFinished()) {
							finished = false;
						}
					}
                    
					if(finished){// ������ɷ���
						// �ӳ�1�����
						sleep(1000);
						if(isReturnByteData){
							RandomAccessFile raf = new RandomAccessFile(new File(fileName), "rw");
							byte[] bts1 = new byte[1024];
							ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
							int len1 = 0;
							while((len1 = raf.read(bts1)) != -1){
								baos1.write(bts1, 0, len1);
							}
							Message message1 = handler.obtainMessage(DOWNLOAD_COMPLETED,baos1.toByteArray());
		                    handler.sendMessage(message1);
						}else{
							Message message1 = handler.obtainMessage(DOWNLOAD_COMPLETED,null);
		                    handler.sendMessage(message1);
						}
						break;
						
					}else// û��������ɸ��½�����
					{
						Message message1 = handler.obtainMessage(DOWNLOAD_PROGRESS, null);
	                    handler.sendMessage(message1);
					}
					sleep(130);
				}
				
			} catch (Exception e) {// ���ر���
				Message message = handler.obtainMessage(DOWNLOAD_ERROR, e);
                handler.sendMessage(message);
			}
		}
		
	}

     /**
      * �ļ����ص����߳�   Downloader����ʱ������������߳�
      * @since �������ط�����Լ��ķְ�����
      * @see Downloader
      */
    class FileDownloadThread extends Thread{
     	private static final int BUFFER_SIZE = 1024;//��������С
     	private URL url;//���ص�ַ
     	private File file;//������ļ�
     	private int startPosition;//��ʼλ��
     	private int endPosition;//����λ��
     	private int curPosition;//��ǰλ��
     	private boolean finished = false;//�߳��Ƿ��������
     	private int downloadSize = 0;//�������ݴ�С
     	
     	/**
     	 * ���캯��
     	 * @param url ����url
     	 * @param file �����ļ�����
     	 * @param startPosition �ֽڿ�ʼλ��
     	 * @param endPosition �ֽڽ���λ��
     	 */
     	public FileDownloadThread(URL url,File file,int startPosition,int endPosition){
     		this.url = url;
     		this.file = file;
     		this.startPosition = startPosition;
     		this.curPosition = startPosition;
     		this.endPosition = endPosition;
     	}
         
     	@Override
     	public void run() {
     		 BufferedInputStream bis = null;
     		 
             RandomAccessFile fos = null;
             
             byte[] buf = new byte[BUFFER_SIZE];
             
             URLConnection con = null;
             
             try {
                 con = url.openConnection();
                 
                 con.setAllowUserInteraction(true);
                 
                 // ���õ�ǰ�߳����ص���㣬�յ�
                 con.setRequestProperty("Range", "bytes=" + startPosition + "-" + endPosition);
                 
                 // ʹ��java�е�RandomAccessFile ���ļ����������д����
                 fos = new RandomAccessFile(file, "rw");
                 
                 // ���ÿ�ʼд�ļ���λ��
                 fos.seek(startPosition);
                 
                 bis = new BufferedInputStream(con.getInputStream());
                 
                 // ��ʼѭ����������ʽ��д�ļ�
                 while (curPosition < endPosition) {
                     int len = bis.read(buf, 0, BUFFER_SIZE);                
                     if (len == -1) {
                         break;
                     }
                     fos.write(buf, 0, len);
                     curPosition = curPosition + len;
                     if (curPosition > endPosition) {
                     	downloadSize += len - (curPosition - endPosition) + 1;
                     } else {
                     	downloadSize += len;
                     }
                 }
                 
                 bis.close();
                 
                 fos.close();
                 
                 // ���������Ϊtrue
                 this.finished = true;
                 
             } catch (Exception e) {
            	 
                 this.finished = true;
             }
     	}
     	
     	/**
     	 * �Ƿ��������
     	 * @return
     	 */
     	public boolean isFinished(){
     		return finished;
     	}
         /**
          * �������ݴ�С
          * @return
          */
     	public int getDownloadSize() {
     		return downloadSize;
     	}
     }
    
    /**
     * ��һ��URl ��ȡͼƬ
     * @param url
     * @return
     */
	public static BitmapDrawable getImageFromURL(URL url)
	{  
		BitmapDrawable bd = null;
		try {
			HttpURLConnection hc = (HttpURLConnection) url.openConnection();
			Log.d("pic", "----------------" + hc.getResponseCode());
			if (hc.getResponseCode() == 200) { // ʹ������������BitmapDrawable
				bd = new BitmapDrawable(hc.getInputStream());
			}
		} catch (Exception e) {
			return null;
		}
		return bd;
	}
	
	/**
	 * �ļ��ߴ絥λת��
	 * @param lsize
	 * @return
	 */
	public static String GetRealSize(int lsize)
    {
        double size = (double)lsize;
        double kb = 1024;        // Kilobyte
        double mb = 1024 * kb;   // Megabyte
        double gb = 1024 * mb;   // Gigabyte
        double tb = 1024 * gb;   // Terabyte
        
        DecimalFormat dcf = new DecimalFormat("0.00");
        double c = 0.00;
        String s = "";
        if(size < kb)
        {
            s = size + " B";
        }
        else if(size < mb)
        {
            c = size/kb;
            s = dcf.format(c) + " KB";
        }
        else if (size < gb)
        {
            c = size / mb;
            s = dcf.format(c) + " MB";
        }
        else if (size < tb)
        {
            c = size / gb;
            s = dcf.format(c) + " GB";
        }
        else
        {
            c = size / tb;
            s = dcf.format(c) + " TB";
        }
        return s;
    }
}
