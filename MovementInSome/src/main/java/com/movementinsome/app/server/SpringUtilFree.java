package com.movementinsome.app.server;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class SpringUtilFree<T> {
	public T getData(String url,Class<T> c){
		T t = null;
		try {
			HttpHeaders requestHeaders = new HttpHeaders(); 
			
			requestHeaders.setContentType(MediaType.APPLICATION_JSON);
			
			List<MediaType> listtype = new ArrayList<MediaType>();
			listtype.add(MediaType.TEXT_PLAIN);
			listtype.add(new MediaType("application","json"));
			requestHeaders.setAccept(listtype);
			
			//设置接收字符集
			List<Charset> charset = new ArrayList<Charset>();
			charset.add(Charset.forName("utf-8"));
			requestHeaders.setAcceptCharset(charset);
			HttpEntity<String> requestEntity = new HttpEntity<String>(requestHeaders);
			
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//			requestFactory.setConnectTimeout(MyPublicData.REST_CONNTIMEOUT);

			RestTemplate restTemplate = new RestTemplate(requestFactory);
			//restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

			
			ResponseEntity<T> responseEntity =  restTemplate.exchange(url, HttpMethod.POST, requestEntity, c);
			t = responseEntity.getBody(); 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return t;
	}

	public ResponseEntity<T> getData2(String url,Class<T> c){
		try {
			HttpHeaders requestHeaders = new HttpHeaders(); 
			requestHeaders.setContentType(MediaType.APPLICATION_JSON);
			
			List<MediaType> listtype = new ArrayList<MediaType>();
			listtype.add(MediaType.TEXT_PLAIN);
			listtype.add(new MediaType("application","json"));
			requestHeaders.setAccept(listtype);
			
			//设置接收字符集
			List<Charset> charset = new ArrayList<Charset>();
			charset.add(Charset.forName("utf-8"));
			requestHeaders.setAcceptCharset(charset);
			HttpEntity<String> requestEntity = new HttpEntity<String>(requestHeaders);
			
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setReadTimeout(SpringUtil._REST_CONNTIMEOUT);

			RestTemplate restTemplate = new RestTemplate(requestFactory);
			restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

			ResponseEntity<T> responseEntity =  restTemplate.exchange(
					url, HttpMethod.GET, requestEntity, c);
			return responseEntity;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public T PostData(String url ,Object upload,Class<T> c){
		T t = null;
		try {
			HttpHeaders requestHeaders = new HttpHeaders(); 
			requestHeaders.setContentType(MediaType.APPLICATION_JSON);
//			requestHeaders.setAccept(Collections.singletonList(new MediaType("application","json")));
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(upload,requestHeaders);
			
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setReadTimeout(SpringUtil._REST_CONNTIMEOUT);

			RestTemplate restTemplate = new RestTemplate(requestFactory);
			restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

			ResponseEntity<T> responseEntity =  restTemplate.exchange(
					url, HttpMethod.POST, requestEntity, c);
			t = responseEntity.getBody(); 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return t;
	}
	public String postData2(String url, String upload,String key) {
		try {
			String t=null;
			HttpResponse httpResponse = null;
			HttpPost post = null;
			post = new HttpPost(url);
			List<NameValuePair> lpair = new ArrayList<NameValuePair>();
			lpair.add(new BasicNameValuePair(key, upload));
			UrlEncodedFormEntity httpEntity = new UrlEncodedFormEntity(lpair,"UTF-8");
			post.setEntity(httpEntity);
			httpResponse = new DefaultHttpClient().execute(post);
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
	public String postData3(String url,String upload){
		String result=null;
		try {
			
			HttpResponse httpResponse = null;
			HttpPost post = null;
			
			post = new HttpPost(url);
			/*List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("Content-Type", "application/json"));
			params.add(new BasicNameValuePair("target", st));*/
			post.setHeader("Content-Type",  "application/json");
		//	post.addHeader("Content-Type", "application/json");
			post.setEntity(new StringEntity(upload, "UTF-8"));
			HttpClient client = new DefaultHttpClient();
            // 请求超时
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
            // 读取超时
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 15000);

			httpResponse = client.execute(post);
			int x=httpResponse.getStatusLine().getStatusCode();
			if (x == 200) {				
				result= EntityUtils.toString(httpResponse.getEntity());
			}else{
				result = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}	
		return result;
	}
	public String getData4(String url) {
		String result = null;
		HttpGet hp = new HttpGet(url);
		HttpClient hc = new DefaultHttpClient();
		HttpResponse hr = null;
		try {
			hr = hc.execute(hp);
			if (hr.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(hr.getEntity(), HTTP.UTF_8);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
	}
}
