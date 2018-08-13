package com.movementinsome.kernel.util;

import android.content.Context;
import android.os.SystemClock;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class WebAccessTools {
	
	// 网络访问状态
	public enum NET_STATE {
		NULL, LOCAL, FOEIGN
	}
	
    /** 
     * 当前的Context上下文对象 
     */  
    private Context context;  
    /** 
     * 构造一个网站访问工具类 
     * @param context 记录当前Activity中的Context上下文对象 
     */  
    public WebAccessTools(Context context) {  
        this.context = context;  
    }  
      
    /** 
     * 根据给定的url地址访问网络，得到响应内容(这里为GET方式访问) 
     * @param url 指定的url地址 
     * @return web服务器响应的内容，为<code>String</code>类型，当访问失败时，返回为null 
     */  
    public synchronized String getWebContent(String url) {  
    	CheckConn checkConn = new CheckConn(this.context,url);
    	checkConn.start(); 
    	boolean isfinished = false;
    	int count = 0;
    	for (int i = 0; i < 10; i++) {
    		SystemClock.sleep(500);
    		if(checkConn.isSucessed()){
    			return checkConn.getConnect();
    		}
		}
//		while (!isfinished) {
//			isfinished = checkConn.isSucessed();
//			System.out.println(count);
//			if (isfinished){
//				return checkConn.getConnect();
//			}
//		}
		return null;
    }  
    
    private class CheckConn extends Thread{
    	Context context;
    	String url;
    	String connect = null;
    	boolean sucessed = false;
    	
    	public CheckConn(Context context,String url){
    		this.context = context;
    		this.url = url;
    	}
    	
    	public boolean isSucessed() {
    		// TODO Auto-generated method stub
    		return sucessed;
    	}
    	
    	public String getConnect(){
    		return connect;
    	}
    	
    	@Override
    	public void run() {
            //创建一个http请求对象  
            HttpGet request = new HttpGet(url);  
            //创建HttpParams以用来设置HTTP参数  
            HttpParams params=new BasicHttpParams();  
            //设置连接超时或响应超时  
            HttpConnectionParams.setConnectionTimeout(params, 3000);  
            HttpConnectionParams.setSoTimeout(params, 5000);  
            //创建一个网络访问处理对象  
            HttpClient httpClient = new DefaultHttpClient(params);  
            try{  
                //执行请求参数项  
                HttpResponse response = httpClient.execute(request);  
                //判断是否请求成功  
                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
                    //获得响应信息  
                	connect = EntityUtils.toString(response.getEntity());  
                } else {  
                    //网连接失败，使用Toast显示提示信息  
                    Toast.makeText(context, "网络访问失败，请检查您机器的联网设备!", Toast.LENGTH_LONG).show();  
                }  
                  
            }catch(Exception e) {  
            	connect = null;
                e.printStackTrace();  
            } finally {  
                //释放网络连接资源  
                httpClient.getConnectionManager().shutdown();  
                sucessed = true;
            }  
    	}
    }
}  
