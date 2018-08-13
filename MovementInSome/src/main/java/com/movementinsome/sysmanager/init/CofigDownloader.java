package com.movementinsome.sysmanager.init;

import com.movementinsome.kernel.util.FileUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CofigDownloader {
	//private URL url = null;
	FileUtils fileUtils = new FileUtils();
	
	public int downfile(String urlStr, String path, String fileName) {
		if (fileUtils.isFileExist(path + fileName)) {
			return 1;
		} else {

			try {
				InputStream input = null;
				input = getInputStream(urlStr);
				File resultFile = fileUtils.write2SDFromInput(path, fileName,
						input);
				if (resultFile == null) {
					return -1;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -2;
			}

		}
		return 0;
	}

	public int downfileOver(String urlStr, String path, String fileName) {
		try {
			InputStream input = null;
			input = getInputStream(urlStr);
			File resultFile = fileUtils.write2SDFromInput(path, fileName,
					input);
			if (resultFile == null) {
				return -1;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -2;
		}

		return 0;
	}
	
	// 由于得到一个InputStream对象是所有文件处理前必须的操作，所以将这个操作封装成了一个方法
	public synchronized InputStream getInputStream(String urlStr) throws IOException {
		HttpFileDown checkConn = new HttpFileDown(urlStr);
    	checkConn.start();
    	boolean isfinished = false;
		while (!isfinished) {
			// isfinished = checkConn.isSucessed();
			if (checkConn.isSucessed()){
				return checkConn.getInputStream();
			}else if(checkConn.getInputStream()!=null){
				return checkConn.getInputStream();
			}
		}
		return null;
	}
	
	
    private class HttpFileDown extends Thread{
    	String downUrl;
    	ByteArrayOutputStream baos;
    	boolean isPd = false;
    	boolean sucessed = false;
    	
    	public HttpFileDown(String url){
    		this.downUrl = url;
    	}
    	public boolean isSucessed() {
    		// TODO Auto-generated method stub
    		return sucessed;
    	}
    	
    	public InputStream getInputStream(){
    		if (baos != null && isPd){
    		  return new ByteArrayInputStream(baos.toByteArray());
    		}
    		return null;
    	}
    	
    	@Override
    	public void run() {
    		
    		URL url;
			try {
				url = new URL(downUrl);
				HttpURLConnection urlConn = null;
				try {
					urlConn = (HttpURLConnection) url.openConnection();
					urlConn.setRequestProperty("Accept-Encoding", "UTF-8");
					//input = urlConn.getInputStream();
					BufferedInputStream reader = null;
					reader = new BufferedInputStream(urlConn.getInputStream());
					baos = new ByteArrayOutputStream();

					byte[] buffer = new byte[1024];  
					int len;  
					while ((len = reader.read(buffer)) > -1 ) {  
					    baos.write(buffer, 0, len);  
					}  
					baos.flush();
					isPd = true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {  
                //释放网络连接资源  
                sucessed = true;
            }  
    	}
    }
}
