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
 * 多线程文件下载工具
 * 文件分包下载
 * 提高下载速度
 * @see FileDownloadThread
 */
public class Downloader {
	
	/**下载监听*/
	public interface OnDownloadListener {
		/**
		 * 下载完成回发事件
		 * @param data
		 */
	    public void onDownloadCompleted(byte[] data);
	    /**
	     * 下载出错回发事件
	     * @param e
	     */
	    public void onDownloadError(Exception e);
	    /**
	     * 下载进度回发事件
	     * @param size 总大小
	     * @param completed 已经下载
	     */
	    public void onDownloadProgress(int size,int completed);
	}
	
	/**已下载**/
	private int downloadedSize = 0;
	/**文件大小**/
	private int fileSize = 0;
	/***分配线程数**/
	private int downloadThreadNum = 1;//
	/**下载地址*/
	private String downloadUrl = "";//
	/**文件保存路径*/
	private String downloadFilePath = "";//
	protected  Context mContext;
	
	private int DOWNLOAD_COMPLETED = 1;
	private int DOWNLOAD_ERROR = 2;
	private int DOWNLOAD_PROGRESS = 3;
	
	/**
	 * 获取或设置 文件下载完毕是否返回字节数据
	 * @since 如果在OnDownloadListener的onDownloadCompleted里需要使用
	 * @since Byte[]参数则设置true,如果不需要使用Byte[]参数可以设置false以减少内存浪费
	 * @see OnDownloadListener
	 */
	public boolean isReturnByteData = true;
	
    /***下载监听事件*/
	private OnDownloadListener onDownloadListener = null;
    
	/**
	 * 设置下载事件监听
	 * @param listener
	 */
	public void setOnDownloadListener(OnDownloadListener listener){
		onDownloadListener = listener;
	};
	
	/**
	 * Downloader 构造函数
	 * @param context 
	 * @param url 下载地址
	 * @param filePath 文件保存路径
	 * @param threadNum 开启的线程数 大于0,小文件直接设置1则可
	 */
	public Downloader(Context context,String url,
			String filePath,int threadNum){
		mContext = context;
		downloadUrl = url;
		downloadFilePath = filePath;
		if(threadNum > 0)downloadThreadNum = threadNum;
	}

	/**开始下载*/
	public void download() {
		if(onDownloadListener != null){
			onDownloadListener.onDownloadProgress(fileSize, downloadedSize);
        }
		if(downloadThreadNum <1)downloadThreadNum = 1;
		try{
			// 文件夹路径不存在则创建
		    String dir = downloadFilePath.substring(0, downloadFilePath.lastIndexOf("/"));
		    File destDir = new File(dir);
		    if (!destDir.exists()) {
		    	System.out.println("创建目录");
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
	
	/**下载回发处理*/
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == DOWNLOAD_COMPLETED){//完成
				if(onDownloadListener != null){
				   byte[]bData = null;
				   if(isReturnByteData && msg.obj!=null)bData=(byte[])msg.obj;
				   onDownloadListener.onDownloadCompleted(bData);
                }
			}
			if(msg.what == DOWNLOAD_ERROR){//出错
				if(onDownloadListener != null){
					if(msg.obj != null){
						onDownloadListener.onDownloadError((Exception)msg.obj);
					}
                }
			}
			if(msg.what == DOWNLOAD_PROGRESS){//下载中
				if(onDownloadListener != null){
					onDownloadListener.onDownloadProgress(fileSize, downloadedSize);
                }
			}
		}
	};
 
	/**下载作业,一个新的线程**/
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
				//获取下载文件的总大小
				fileSize = conn.getContentLength();
				File file = new File(fileName);
				
				//-----------------处理没有文件名的下载路径，此时无法确认文件大小，无法分包，只能单线程下载----------------
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
				
				// 多线程分包下载 
				FileDownloadThread[] fds = new FileDownloadThread[threadNum];
				// 计算每个线程要下载的数据量
				blockSize = fileSize / threadNum;
				// 解决整除后百分比计算误差
				downloadSizeMore = (fileSize % threadNum);
				
				for (int i = 0; i < threadNum; i++) {
					// 启动线程，分别下载自己需要下载的部分
					int start = i * blockSize;
					int end = (i + 1) * blockSize - 1;
					// 最后一个线程 将余数加进去
					if(i==threadNum-1)end = end + downloadSizeMore;
					FileDownloadThread fdt = new FileDownloadThread(url, file,
							start, end);
					fdt.setName("Thread(downloadTask)("+downloadUrl+"):" + i);
					fdt.start();
					fds[i] = fdt;
				}
				
				// 循环检测下载是否完成
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
                    
					if(finished){// 下载完成返回
						// 延迟1秒加载
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
						
					}else// 没有下载完成更新进度条
					{
						Message message1 = handler.obtainMessage(DOWNLOAD_PROGRESS, null);
	                    handler.sendMessage(message1);
					}
					sleep(130);
				}
				
			} catch (Exception e) {// 下载报错
				Message message = handler.obtainMessage(DOWNLOAD_ERROR, e);
                handler.sendMessage(message);
			}
		}
		
	}

     /**
      * 文件下载单个线程   Downloader下载时将启动多个该线程
      * @since 负责下载分配给自己的分包数据
      * @see Downloader
      */
    class FileDownloadThread extends Thread{
     	private static final int BUFFER_SIZE = 1024;//缓冲区大小
     	private URL url;//下载地址
     	private File file;//保存的文件
     	private int startPosition;//开始位置
     	private int endPosition;//结束位置
     	private int curPosition;//当前位置
     	private boolean finished = false;//线程是否下载完成
     	private int downloadSize = 0;//下载数据大小
     	
     	/**
     	 * 构造函数
     	 * @param url 下载url
     	 * @param file 保存文件对象
     	 * @param startPosition 字节开始位置
     	 * @param endPosition 字节结束位置
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
                 
                 // 设置当前线程下载的起点，终点
                 con.setRequestProperty("Range", "bytes=" + startPosition + "-" + endPosition);
                 
                 // 使用java中的RandomAccessFile 对文件进行随机读写操作
                 fos = new RandomAccessFile(file, "rw");
                 
                 // 设置开始写文件的位置
                 fos.seek(startPosition);
                 
                 bis = new BufferedInputStream(con.getInputStream());
                 
                 // 开始循环以流的形式读写文件
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
                 
                 // 下载完成设为true
                 this.finished = true;
                 
             } catch (Exception e) {
            	 
                 this.finished = true;
             }
     	}
     	
     	/**
     	 * 是否下载完成
     	 * @return
     	 */
     	public boolean isFinished(){
     		return finished;
     	}
         /**
          * 下载数据大小
          * @return
          */
     	public int getDownloadSize() {
     		return downloadSize;
     	}
     }
    
    /**
     * 从一个URl 获取图片
     * @param url
     * @return
     */
	public static BitmapDrawable getImageFromURL(URL url)
	{  
		BitmapDrawable bd = null;
		try {
			HttpURLConnection hc = (HttpURLConnection) url.openConnection();
			Log.d("pic", "----------------" + hc.getResponseCode());
			if (hc.getResponseCode() == 200) { // 使用输入流构建BitmapDrawable
				bd = new BitmapDrawable(hc.getInputStream());
			}
		} catch (Exception e) {
			return null;
		}
		return bd;
	}
	
	/**
	 * 文件尺寸单位转换
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
