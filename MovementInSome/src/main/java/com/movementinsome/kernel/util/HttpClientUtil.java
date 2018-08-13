package com.movementinsome.kernel.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {

	public static String postData(String url,Map<String,String> valuePair) {//提交一个List<NameValuePair>到服务器
		try {
			String t=null;
			HttpResponse httpResponse = null;
			HttpPost post = null;
			post = new HttpPost(url);
			//post.setHeader("Content-Type",  "application/json");
			List<NameValuePair> lpair = new ArrayList<NameValuePair>();
			if(valuePair!=null){
				Set<String> keys=valuePair.keySet();//获取Map<String,String>valuePair中的三个键值对保存
				Iterator<String> it= keys.iterator();
				while(it.hasNext()){
					String key=it.next();
					lpair.add(new BasicNameValuePair(key, valuePair.get(key)));
				}
			}
			UrlEncodedFormEntity httpEntity = new UrlEncodedFormEntity(lpair,"UTF-8");
			//httpEntity.setContentType("application/json");
			post.setEntity(httpEntity);
			HttpClient client = new DefaultHttpClient();
            // 请求超时
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 300000);
            // 读取超时
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 300000);
			httpResponse =client.execute(post);
			int x = httpResponse.getStatusLine().getStatusCode();
			if(x==200){
				t=EntityUtils.toString(httpResponse.getEntity(),HTTP.UTF_8);
			}
			return t;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}		
	}
	private static final String APPLICATION_JSON = "application/json";
    
    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

    public static void httpPostWithJSON(String url, String json) throws Exception {
        // 将JSON进行UTF-8编码,以便传输中文
        String encoderJson = URLEncoder.encode(json, HTTP.UTF_8);
        
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
        
        StringEntity se = new StringEntity(encoderJson);
        se.setContentType(CONTENT_TYPE_TEXT_JSON);
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
        httpPost.setEntity(se);
        httpClient.execute(httpPost);
    } 
}
