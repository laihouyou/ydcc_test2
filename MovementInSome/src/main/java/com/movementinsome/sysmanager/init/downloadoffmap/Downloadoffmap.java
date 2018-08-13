package com.movementinsome.sysmanager.init.downloadoffmap;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import android.os.Handler;
import android.os.Message;

public class Downloadoffmap {

	private boolean isfinished = false;
	private boolean isFail = true;
	private Handler mHandler;
	 /* 是否停止下载 */  
    public static boolean isPause = false;
	
	private int finishThreadNum=0;// 记录子线程完成数量
	private int threadNum ;// 开启的线程数
	public Downloadoffmap(Handler mHandler){
		this.mHandler=mHandler;
	}
	
	public void download(String downloadUrl, int threadNum, String filepath) {
		// TODO Auto-generated method stub
		isfinished = false;
		isFail = true;
		finishThreadNum=0;
		downloadTask task = new downloadTask(downloadUrl, threadNum, filepath);
		task.start();
	}
	/**
	 * 多线程文件下载
	 * 
	 */
	class downloadTask extends Thread {
		private String downloadUrl;// 下载链接地址
		private String filePath;// 保存文件路径地址
		private int blockSize;// 每一个线程的下载量

		public downloadTask(String downloadUrl, int threadNum, String fileptah) {
			this.downloadUrl = downloadUrl;
			Downloadoffmap.this.threadNum = threadNum;
			this.filePath = fileptah;
		}

		@Override
		public void run() {

			FileDownloadThread[] threads = new FileDownloadThread[threadNum];
			try {
				URL url = new URL(downloadUrl);
				// Log.d(TAG, "download file http path:" + downloadUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");  
		        conn.setRequestProperty("Accept-Encoding", "identity");
				// 读取下载文件总大小
				int fileSize = conn.getContentLength();
				if (fileSize <= 0) {
					System.out.println("读取文件失败");
					return;
				}
				if(conn!=null){
	            	conn.disconnect();
	            }
				Message message = new Message();
				message.what = 3;
				message.getData().putInt("fileSize", fileSize);
				mHandler.sendMessage(message);
				// 计算每条线程下载的数据长度
				blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum
						: fileSize / threadNum + 1;

				// Log.d(TAG, "fileSize:" + fileSize + "  blockSize:");

				File file = new File(filePath);
				for (int i = 0; i < threads.length; i++) {
					// 启动线程，分别下载每个线程需要下载的部分
					threads[i] = new FileDownloadThread(url, file, blockSize,
							(i + 1),Downloadoffmap.this);
					threads[i].setName("Thread:" + i);
					threads[i].start();
				}

				
				int downloadedAllSize = 0;
				while (!isfinished) {
					isfinished = true;
					// 当前所有线程下载总量
					downloadedAllSize = 0;
					for (int i = 0; i < threads.length; i++) {
						downloadedAllSize += threads[i].getDownloadLength();
						if (!threads[i].isCompleted()) {
							isfinished = false;
						}
					}
					if(!isFail){
						isfinished = false;
						break;
					}
					Message message1 = new Message();
					message1.what = 1;
					message1.getData().putInt("size", downloadedAllSize);
					mHandler.sendMessage(message1);
					// Log.d(TAG, "current downloadSize:" + downloadedAllSize);
					Thread.sleep(1000);// 休息1秒后再读取下载进度
				}
				// Log.d(TAG, " all of downloadSize:" + downloadedAllSize);

			} catch (MalformedURLException e) {
				isFail=false;
				e.printStackTrace();
			} catch (IOException e) {
				isFail=false;
				e.printStackTrace();
			} catch (InterruptedException e) {
				isFail=false;
				e.printStackTrace();
			}
		}
	}
	
	public boolean isIsfinished() {
		return isfinished;
	}
	public boolean isFail() {
		return isFail;
	}
	public void setFail(boolean isFail) {
		this.isFail = isFail;
	}
	public void addThreadNum(){
		++finishThreadNum;
	}
	public int getFinishThreadNum() {
		return finishThreadNum;
	}
	public void setFinishThreadNum(int finishThreadNum) {
		this.finishThreadNum = finishThreadNum;
	}
	
}
