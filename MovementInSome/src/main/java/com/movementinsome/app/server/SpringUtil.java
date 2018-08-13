package com.movementinsome.app.server;

import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.movementinsome.AppContext;
import com.movementinsome.database.vo.ConstructionDataStateVO;
import com.movementinsome.database.vo.InitDataVO;
import com.movementinsome.database.vo.ReturnMessage;
import com.movementinsome.database.vo.UserBeanVO;
import com.movementinsome.kernel.location.LocationInfo;
import com.movementinsome.kernel.updateApk.UpdateApkInfo;
import com.movementinsome.kernel.util.MD5;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class SpringUtil {	
	
	/**** url常量设定 ****/
	public final static String _REST_HEARTBEAT = "/heartbeatResource/heartbeat"; //心跳访问
	public final static String _REST_TRACEUPLOAD = "/heartbeatResource/terminalLocations"; //轨迹上传

	
	public final static String _REST_USERLOGIN = "/appInitResource/appLogin?";// 用户登录
	public final static String _REST_USERLOGOUT = "/appInitResource/appLogout?";// 用户登录
	public final static String _REST_SYSTEMINITIA = "/heartbeatResource/initAppData?uuid=";// 系统初始化
	public final static String _REST_TASKDOWN = "/heartbeatResource/dynamicInfo";// 任务下载
	public final static String _REST_UPDATEAPK = "/appInitResource/getNewApkVersionInfo";// 系统升级
	public final static String _REST_MONITORING = "/locationResource/terminalLocation";// 终端时时监控
	public final static String _REST_EXTENDLEAKANALYSIS = "rest/dataManage/extendLeakAnalysis";// 爆管停水分析
	public final static String _REST_LEAKANALYSIS = "/dataManage/leakanalysis";// 爆管停水分析
	public final static String _REST_UPLOADPOINT = "/extanalysis/leakpos?";// 上传爆点并修正
	public final static String _REST_LOCATIONRESOURCE = "/locationResource/terminalLocation";// (位置监控上传)
	public final static String _REST_TASKLOCATIONMODELS = "/locationResource/locationResource";// 任务轨迹点和轨迹段
	public final static String _REST_UPLOADDATAMANAGE = "/dataManage/upLoadDataManage";// (任务对象上传)
	public final static String _REST_UPLOADTASKFEEDBACKINFO = "/dataManage/upLoadTaskFeedbackInfo";// (任务单反馈上传)
	public final static String _REST_UPLOADPLANTASKSTATE = "/heartbeatResource/dynamicStatus";// 上传状态接口
	public final static String _REST_FINISHMONITOR = "/dataManage/upLoadDataState";// 完成监控
	public final static String _REST_CONFIRMPUSHMESSAGE = "/dataManage/confirmPushMessage?";// 推送信息回访
	public final static String _REST_GETPUSHMESSAGE = "/heartbeatResource/message?";// 查询推送信息
	public final static String _REST_PUSHFEEDLEAKMESSAGE = "/dataManage/pushFeedLeakMessage?";// 申请定漏
	public final static String _REST_CONFIRPUSHFEEDLEAKMESSAGE = "/dataManage/confirmPushFeedLeakMessage?";// 确认定漏
	public final static String _REST_PASSWORD="/heartbeatResource/password";// 修改密码
	public final static String _HEARTBEATRESOURCE_SEQUENCE="/heartbeatResource/sequence";// 获取唯一编号
	
	public final static String _REST_DYMANICFROMUPLOAD = "/heartbeatResource/dynamicStore";//动态表单记录上传
	public final static String _REST_UPLOAD_IMG_STATUS="/heartbeatResource/imgStatus";
	public final static String _REST_USER_MANUAL="/AppUser/index_doc.htm";
	
	//文件服务器接口
	public final static String _REST_FILEATTACHUPLOAD = "/rest/fileKuManage/uploadFileMobile";//附件上传到文件服务器

	public final static String _REST_DOWNLOADFILESIMPLE = "/rest/fileKuManage/downloadByGuidAndPicName";//下载图片

	public final static String FILEATTACHBROWSE = "/img.jsp";//文件服务器附近浏览

	public final static int _REST_CONNTIMEOUT = 5000;// REST连接timeout时间设置，毫秒
	
	public final static String QUERY_SCADA_NET_DATA = "/scada/queryScadaNetData";// scada历史数据
	
	public final static String GET_NET_IN_FO = "/scada/getNetInfo";// scada基站数据
	
	public final static String GET_NET_IN_FO_GROUP= "/scada/getNetInfoGroup";// scada基站数据---树形结构
	
	public final static String QUERY_SCADA_NET_NEW_DATA = "/scada/queryScadaNetNewData";// scada实时数据
	
	public final static String BURST_MAIN_DATA = "/heartbeatResource/findUploadInfo";// 爆漏管理数据
	
	public static final String BITMAP_URL="/rest/fileKuManage/findPicFileInfoByLinkNum?linkNum=";//爆漏管理图片
	
	public static final String BURST_IMAGE_URL="/rest/fileKuManage/findPhotosBase64";//爆漏管理图片findPhotosBase64
	/*public SpringUtil(Context context ) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}*/

	public static final String SUCCESS="1";
	public static final String FAILURE="-1";
	
	//private Context context;
	private static final int TIME_OUT = 1000*15;   //超时时间
	private static final String CHARSET = "utf-8"; //设置编码
	//private static String serverUrl; //中间服务器上下文
	
	/**
	 * 初始化数据下载
	 * @return
	 */
	public static InitDataVO downloadFile(String serverUrl,String uuid,String phoneName,String versionName,Handler mHandler){
		try{
			String phoneVersionNum = AppContext.getInstance().getPhoneVersionNum();
			String phoneTypeNum = AppContext.getInstance().getPhoneTypeNum();
			String url = serverUrl+_REST_SYSTEMINITIA+uuid+"&itiName="+phoneName+"&versionName="+versionName;
//			final Map<String,String> map=new HashMap<>();
//			OkHttpUtils.get()
//					.url(url)
////				.addParams("uuid",map.get("phoneIMEI"))
////				.addParams("&userName=",map.get("userName"))
////				.addParams("&password=", MD5.getInstance().getMD5ofStr(map.get("userPwd")).toLowerCase())
//					.build()
//					.execute(new StringCallback() {
//						@Override
//						public void onError(Call call, Exception e, int id) {
//							e.printStackTrace();
//						}
//
//						@Override
//						public void onResponse(String response, int id) {
//							Log.i("tag",response);
//							map.put("response",response);
//						}
//					});
			String s = SpringUtil.postData(url, "");
			/*JSONObject jo=new JSONObject(s);
			JSONArray ja=jo.getJSONArray("mechanicals");
			for(int i=0;i<ja.length();++i){
				String date= ja.getJSONObject(i).get("bisBuyDate")+"";
			}*/
			Gson gson = new GsonBuilder()  
		      .setDateFormat("yyyy-MM-dd HH:mm:ss")  
		      .create(); 
			InitDataVO initDataVO =gson.fromJson(s, InitDataVO.class);
			/*HttpHeaders requestHeaders = headers(MediaType.APPLICATION_JSON,MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<String>(requestHeaders);
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setReadTimeout(TIME_OUT);
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			
			restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
			
			ResponseEntity<InitDataVO> responseEntity = restTemplate.exchange(
					url, HttpMethod.POST, requestEntity, InitDataVO.class);*/
			return initDataVO;
		}catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	/**
	 * 系统更新
	 * @return
	 */
	public static ResponseEntity<UpdateApkInfo[]> upDateApk(String serverUrl){
		try {
			String url = serverUrl + _REST_UPDATEAPK;
//			HttpHeaders requestUser = headers(MediaType.APPLICATION_JSON,
//					MediaType.APPLICATION_JSON);
//			HttpEntity<String> entity = new HttpEntity<String>(requestUser);
//			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//			requestFactory.setReadTimeout(TIME_OUT);
//			requestFactory.setConnectTimeout(_REST_CONNTIMEOUT);
//			RestTemplate restTemplate = new RestTemplate(requestFactory);
//
//			List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
//			list.add(new MappingJacksonHttpMessageConverter());
//			restTemplate.setMessageConverters(list);
//
//			ResponseEntity<UpdateApkInfo[]> responseEntity = restTemplate
//					.exchange(url, HttpMethod.POST, entity, UpdateApkInfo[].class);
//			return responseEntity;

			OkHttpUtils.post()
					.url(url)
					//.mediaType(MediaType.parse("application/json; charset=utf-8"))
					.build()
					.execute(new StringCallback() {
						public static final String TAG = "100";

						@Override
						public void onError(Call call, Exception e, int id) {
							Log.e("tag", "onError: ",e );
						}

						@Override
						public void onResponse(String response, int id) {
							Log.i(TAG, "onResponse: " + response);
						}
					});
			return null;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}



	/**
	 * 登录
	 * @param userName
	 */
	public static ResponseEntity<UserBeanVO> userLogin(String serverUrl,String userName,String password,String uuid){
		try {
			String url = serverUrl+ _REST_USERLOGIN + "uuid=" + uuid + "&userName=" + userName + "&password=" + MD5.getInstance().getMD5ofStr(password).toLowerCase();
			HttpHeaders requestUser = headers(MediaType.APPLICATION_JSON,MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<String>(requestUser);
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setReadTimeout(TIME_OUT);
			requestFactory.setConnectTimeout(_REST_CONNTIMEOUT);

			RestTemplate restTemplate = new RestTemplate(requestFactory);
			
			List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
			list.add(new MappingJacksonHttpMessageConverter());
			restTemplate.setMessageConverters(list);
			
			ResponseEntity<UserBeanVO> responseEntity = restTemplate.exchange(
					url, HttpMethod.POST, requestEntity, UserBeanVO.class);
			return responseEntity;
		} catch (Exception e) {
			// TODO: handle exception
			String s=e.toString();
			int i=0;
			return null;
		}
	}

	public static String userPwd(String serverUrl,String pss,String username,String pad,String imei) throws ParseException, IOException
	{
		try {
			HttpResponse httpResponse = null;
			HttpPost post = null;
			String result=null;
			post = new HttpPost(serverUrl+_REST_PASSWORD);
			post.setHeader("Content-Type",  "application/json");
		    String jsonObj = "{\"new_password\":\""+pss+"\",\"usernum\":\""+username+"\",\"old_password\":\""+pad+"\",\"imei\":\""+imei+"\"}";

			post.setEntity(new StringEntity(jsonObj,"UTF-8"));
			httpResponse = new DefaultHttpClient().execute(post);
			int x=httpResponse.getStatusLine().getStatusCode();
			if (x == 200) {				
				result= EntityUtils.toString(httpResponse.getEntity(), "UTF-8");				
			}else{
				result = null;
			}
			/*if (null != result){
				JSONObject rm = new JSONObject(result);
				return rm.get("code").equals("1")?"1":"0";
			}else*/
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}	
	}
	
	/**
	 * 退出
	 * @param userName
	 */
	public static void userLogOut(String serverUrl,String userName,String uuid){
		if(TextUtils.isEmpty(userName) && TextUtils.isEmpty(uuid)){
			return;
		}
		try {
			String url = serverUrl + _REST_USERLOGOUT + "uuid=" + uuid + "&userName=" + userName ;
			HttpHeaders requestUser = headers(MediaType.APPLICATION_JSON,MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<String>(requestUser);
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setReadTimeout(TIME_OUT);
//			requestFactory.setConnectTimeout(MyPublicData._REST_CONNTIMEOUT);

			RestTemplate restTemplate = new RestTemplate(requestFactory);
			
			List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
			list.add(new MappingJacksonHttpMessageConverter());
			restTemplate.setMessageConverters(list);
			
			ResponseEntity<UserBeanVO> responseEntity = restTemplate.exchange(
					url, HttpMethod.POST, requestEntity, UserBeanVO.class);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 上传爆点并修正
	 * @param x 
	 * @param y 
	 * @param edge 0为管线，1为设施
	 * @return 修正的爆点信息
	 */
	public static String uploadPoint(String serverUrl,String x  ,String y,String edge){
		String result = null;
		try {
			String url = serverUrl+"?x="+x+"&y="+y+"&edge="+edge;

            HttpPost request = new HttpPost(url);  
            request.setHeader(HTTP.CONTENT_TYPE, "application/json");  
             
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
            HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);
            HttpClient client = new DefaultHttpClient(httpParams);  
            HttpResponse response = client.execute(request);  
            result = EntityUtils.toString(response.getEntity());  
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 关阀停水分析
	 * @return
	 */
	public static String leakanalysis(String serverUrl,String out){
		String result = null; 
		try {   
			String url = serverUrl;
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> lpair = new ArrayList<NameValuePair>();
			lpair.add(new BasicNameValuePair("target", out));
			lpair.add(new BasicNameValuePair("f", "json"));
			UrlEncodedFormEntity httpEntity = new UrlEncodedFormEntity(lpair,"UTF-8");
			httpPost.setEntity(httpEntity);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(httpPost);
			if(response.getStatusLine().getStatusCode() == org.apache.http.HttpStatus.SC_OK){
				result = EntityUtils.toString(response.getEntity());  
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 扩大分析
	 * @return
	 */
	public static String extendLeakAnalysis(String serverUrl,String out){
		String result = null; 
		try {   
			HttpPost httpPost = new HttpPost(serverUrl);
			List<NameValuePair> lpair = new ArrayList<NameValuePair>();
			lpair.add(new BasicNameValuePair("target", out));
			UrlEncodedFormEntity httpEntity = new UrlEncodedFormEntity(lpair,"UTF-8");
			httpPost.setEntity(httpEntity);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(httpPost);
			if(response.getStatusLine().getStatusCode() == org.apache.http.HttpStatus.SC_OK){
				result = EntityUtils.toString(response.getEntity());  
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 地址搜索
	 * @param address
	 * @return
	 */
	public static String addressSearch(String serverUrl,String address){
		try {
			String url = serverUrl+"?search=" + address+ "&f=json";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setReadTimeout(TIME_OUT);

			RestTemplate restTemplate = new RestTemplate(requestFactory);
			List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
			list.add(new MappingJacksonHttpMessageConverter());
			list.add(new StringHttpMessageConverter());
			restTemplate.setMessageConverters(list);
			ResponseEntity<String> responseEntity = restTemplate.exchange(url,
					HttpMethod.GET, entity, String.class);
			return responseEntity.getBody();
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
		
	}
	
	/**
	 * 完成监控
	 * @param tableName
	 * @param params
	 * @param state
	 * @return
	 */
	public static String finishMinitor(String serverUrl,String tableName,String params,String state){
		String result = null; 
		try {   
			String url = serverUrl+_REST_FINISHMONITOR;
			JSONObject jsonparams = new JSONObject(params);
			JSONObject json = new JSONObject();
			json.put("tableName", tableName);
			json.put("params", jsonparams);
			json.put("state", state);
			ConstructionDataStateVO constructionDataState=new ConstructionDataStateVO ();
			constructionDataState.setParams(params);
			constructionDataState.setState(state);
			constructionDataState.setTableName(tableName);

			HttpResponse httpResponse = null;
			HttpPost post = null;
			
			post = new HttpPost(url);
			post.setHeader("Content-Type",  "application/json");
			post.setEntity(new StringEntity(json.toString()));
			httpResponse = new DefaultHttpClient().execute(post);
			int x=httpResponse.getStatusLine().getStatusCode();
			if (x == 200) {				
				result= EntityUtils.toString(httpResponse.getEntity());
			}else{
				result = "0";
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result = "0";
		}
		return result;
	}
	
	/**
	 * 设置httpheader
	 * @param contentType
	 * @param acceptType
	 * @return
	 */
	private static HttpHeaders headers(MediaType contentType,MediaType acceptType){
		HttpHeaders requestHeaders = new HttpHeaders();
		//设置传输媒体类型
		requestHeaders.setContentType(contentType);

		//设置接收媒体类型
		List<MediaType> listtype = new ArrayList<MediaType>();
		listtype.add(MediaType.TEXT_PLAIN);
		listtype.add(acceptType);
		requestHeaders.setAccept(listtype);

		//设置接收字符集
		List<Charset> charset = new ArrayList<Charset>();
		charset.add(Charset.forName("utf-8"));
		requestHeaders.setAcceptCharset(charset);

		return requestHeaders;
	}


	/**下载所有工单内容
	 * @param data	：参数
	 * @return
	 */
	public static String postData(String contextUrl,String data){
		try {
 			HttpResponse httpResponse = null;
			HttpPost post = null;
			String result=null;
			post = new HttpPost(contextUrl);
			post.setHeader("Content-Type",  "application/json");
			StringEntity stringEntity = new StringEntity(data,"UTF-8");
			stringEntity.setContentType("application/json");
			stringEntity.setContentEncoding("UTF-8");
			post.setEntity(stringEntity);
			HttpClient client = new DefaultHttpClient();
			 // 请求超时
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
            // 读取超时
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
			httpResponse=client.execute(post);
			int x=httpResponse.getStatusLine().getStatusCode();
			if (x == 200) {				
				result= EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			}else{
				result = null;
			}
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	/**下载任意文件到指定文件夹
	 * @return
	 */
	public static String VoidData(String contextUrl,String name){
		String dirName = "";
		dirName = Environment.getExternalStorageDirectory()+"/MyDownload/";
		File file2 = new File(dirName);
		if (!file2.exists()){
			file2.mkdir();
		}
		String newFilename = name;
		newFilename = dirName + newFilename;
		File file = new File(newFilename);
		try {
	         // 构造URL   
	         URL url = new URL(contextUrl);   
	         // 打开连接   
	         URLConnection con = url.openConnection();
	         //获得文件的长度
	         int contentLength = con.getContentLength();
	         System.out.println("长度 :"+contentLength);
	         // 输入流   
	         InputStream is = con.getInputStream();  
	         // 1K的数据缓冲   
	         byte[] bs = new byte[1024];   
	         // 读取到的数据长度   
	         int len;   
	         // 输出的文件流   
	         OutputStream os = new FileOutputStream(newFilename);   
	         // 开始读取   
	         while ((len = is.read(bs)) != -1) {   
	             os.write(bs, 0, len);   
	         }  
	         // 完毕，关闭所有链接   
	         os.close();  
	         is.close();
	            
	} catch (Exception e) {
	        e.printStackTrace();
	}
		return newFilename;
	}
	
	/**下载附件信息
	 * @param data	：参数
	 * @return
	 */
	public static String postData2(String contextUrl,String data){
		try {
			HttpResponse httpResponse = null;
			HttpPost post = null;
			String result=null;
			post = new HttpPost(contextUrl);
			post.setHeader("Content-Type",  "text/plain");
			StringEntity stringEntity = new StringEntity(data,"UTF-8");
			stringEntity.setContentType("application/json");
			stringEntity.setContentEncoding("UTF-8");
			post.setEntity(stringEntity);
			HttpClient client = new DefaultHttpClient();
			// 请求超时
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
			// 读取超时
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
			httpResponse=client.execute(post);
			int x=httpResponse.getStatusLine().getStatusCode();
			if (x == 200) {				
				result= EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			}else{
				result = null;
			}
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 任务表单上传对象
	 *  upLoadDataManage（任务提交）
	 *  upLoadTaskFeedbackInfo任务反馈提交的
	 *  
	 *  method ：
	 *   1.locationResource(位置监控上传)
	 *   2.upLoadDataManage(任务对象上传)
	 *   3.upLoadTaskFeedbackInfo(任务单反馈上传)
	 *   4.taskLocationModels (任务轨迹上传)
	 * @return
	 */
	public static String uploadSimpleFile(String serverUrl,Object upLoad ){
		try{
			String url = serverUrl;
			/*if("LOCATIONRESOURCE".equals(method.toUpperCase())){
				url += _REST_LOCATIONRESOURCE;
				
			}else if("UPLOADDATAMANAGE".equals(method.toUpperCase())){
				url += _REST_UPLOADDATAMANAGE;
				
			}else if("UPLOADTASKFEEDBACKINFO".equals(method.toUpperCase())){
				url += _REST_UPLOADTASKFEEDBACKINFO;
				
			}else if("UPLOADPLANTASKSTATE".equals(method.toUpperCase())){
				url += _REST_UPLOADPLANTASKSTATE;
				
			}else if("TASKLOCATIONMODELS".equals(method.toUpperCase())){
				url += _REST_TASKLOCATIONMODELS;
				
			}*/

		    HttpHeaders requestHeaders = headers(MediaType.APPLICATION_JSON,MediaType.APPLICATION_JSON);
			
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(upLoad,requestHeaders);
			
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setReadTimeout(TIME_OUT);
//			requestFactory.setConnectTimeout(MyPublicData._REST_CONNTIMEOUT);
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
			ResponseEntity<String> responseEntity =restTemplate.postForEntity(url, requestEntity, String.class);
			//ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class,upLoad);
			String restly = responseEntity.getBody().toString();
						
			return restly;
		}catch (Exception e) {
			return "-1";
		}
	}


	public static ReturnMessage traceUpload(String serverUrl,List<LocationInfo> locations){
		try{
			String url = serverUrl+_REST_TRACEUPLOAD;
		    HttpHeaders requestHeaders = headers(MediaType.APPLICATION_JSON,MediaType.APPLICATION_JSON);
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(locations,requestHeaders);
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setReadTimeout(TIME_OUT);
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
			//restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
			ResponseEntity<ReturnMessage> responseEntity =restTemplate.postForEntity(url, requestEntity, ReturnMessage.class);
			return responseEntity.getBody();					
		}catch (Exception e) {

			return null;
		}
	}
	
	public static String dymanicFormUpload(String serverUrl,String context){
		try {
			HttpResponse httpResponse = null;
			HttpPost post = null;
			String result=null;
			
			//创建HttpParams以用来设置HTTP参数  
            HttpParams params=new BasicHttpParams();  
			 //设置连接超时或响应超时  
            HttpConnectionParams.setConnectionTimeout(params, 3000);  
            HttpConnectionParams.setSoTimeout(params, 5000);  
            
            post = new HttpPost(serverUrl+_REST_DYMANICFROMUPLOAD);
			post.setHeader("Content-Type",  "application/json");
			post.setEntity(new StringEntity(context,"UTF-8"));
			httpResponse = new DefaultHttpClient(params).execute(post);
			int x=httpResponse.getStatusLine().getStatusCode();
			if (x == 200) {				
				result= EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			}else{
				result = null;
			}
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}	

	}
	public static String imgStatusUpload(String serverUrl,String context){
		try {
			HttpResponse httpResponse = null;
			HttpPost post = null;
			String result=null;
			
			//创建HttpParams以用来设置HTTP参数  
            HttpParams params=new BasicHttpParams();  
			 //设置连接超时或响应超时  
            HttpConnectionParams.setConnectionTimeout(params, 3000);  
            HttpConnectionParams.setSoTimeout(params, 5000);  
            
            post = new HttpPost(serverUrl+_REST_UPLOAD_IMG_STATUS);
			post.setHeader("Content-Type",  "application/json");
			post.setEntity(new StringEntity(context,"UTF-8"));
			httpResponse = new DefaultHttpClient(params).execute(post);
			int x=httpResponse.getStatusLine().getStatusCode();
			if (x == 200) {				
				result= EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			}else{
				result = null;
			}
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}	

	}
	
	/**
	 * 附件及附属信息上传
	 * @param context
	 * @return
	 */
	public static String attachFileUpload(String serverUrl,String context,File file){
		
		HttpClient httpclient = null;  
        httpclient = new DefaultHttpClient();  //实现这个接口的子类 

        final HttpPost httppost = new HttpPost(serverUrl+_REST_FILEATTACHUPLOAD);  
 
        MultipartEntity multipartEntity = new MultipartEntity(
        		HttpMultipartMode.BROWSER_COMPATIBLE,
				null,
				Charset.forName("utf-8"));//要想实现图片上传功能，必须将 MultipartEntity 的模式设置为 BROWSER_COMPATIBLE 。
        
        if (!"".equals(context)){
			try {
		        ContentBody contentBody;
				contentBody = new StringBody(context,Charset.forName("utf-8"));
				 multipartEntity.addPart("picFileInfoVO", contentBody); 
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
        }
		if (file != null){
			ContentBody contentBody = new FileBody(file);
			multipartEntity.addPart("file", contentBody);
		}

        httppost.setEntity(multipartEntity);  

        HttpResponse httpResponse;  
        try {  
            httpResponse = httpclient.execute(httppost);  

            final int statusCode = httpResponse.getStatusLine()  
                    .getStatusCode();  

            String response = EntityUtils.toString(  
                    httpResponse.getEntity(), HTTP.UTF_8);  

            if (statusCode == HttpStatus.SC_OK) {  
            	updataImgLogFile(context, file);
                return SUCCESS;  
            }  

        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (httpclient != null) {  
                httpclient.getConnectionManager().shutdown();  
                httpclient = null;  
            }  
        }  
        return FAILURE;  
	}
	
	private static void updataImgLogFile(String picFileInfoVO,File file){
		org.json.JSONObject logJson =null;
		try {
			logJson = new org.json.JSONObject(picFileInfoVO);
			logJson.put("isUpload", "true");
			FileWriter fw = null;
			BufferedWriter bw = null;
			String f =file.getAbsolutePath();
			if(f.endsWith(".jpg")){
				f = f.replace("jpg", "log");
			}else if(f.endsWith(".png")){
				f = f.replace("png", "log");
			}else if(f.endsWith(".JPG")){
				f = f.replace("JPG", "log");
			}else if(f.endsWith(".PNG")){
				f = f.replace("PNG", "log");
			}
			String datetime = "";
			try {
				fw = new FileWriter(f, false);//
				// 创建FileWriter对象，用来写入字符流
				bw = new BufferedWriter(fw); // 将缓冲对文件的输出
				bw.write(logJson.toString()+"\n"); // 写入文件
				bw.newLine();
				bw.flush(); // 刷新该流的缓冲
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					bw.close();
					fw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
				}
			}
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
	}
	
	public static String getOffLineTask(String serverUrl,String uuid,String userName){
		String url = serverUrl+_REST_GETPUSHMESSAGE;
		url +="uuid="+uuid+ "&userName="+userName;
		
		SpringUtilFree<String> getTask = new SpringUtilFree<String>();
		return getTask.getData(url, String.class);	
	}
	
	public static void configPushMessage (String serverUrl,String uuid,String userName,String workTaskNum,String pushState){
		String url = serverUrl + _REST_CONFIRMPUSHMESSAGE+"uuid="+uuid
		    +"&userName="+userName+"&workTaskNum="+workTaskNum+"&pushState="+pushState;
		SpringUtilFree<String> callBack = new SpringUtilFree<String>();
		callBack.getData(url, String.class);
	}
	/**
	 * 删除文件
	 * @param path 文件路径
	 */
/*	public void deleteImage(String path){
		File fileImage = new File(path);
		// 判断文件是否存在
		if (fileImage.exists()) { 
			// 判断是否是文件
			if (fileImage.isFile()) { 
				fileImage.delete(); 
			} else if (fileImage.isDirectory()) { 
				File files[] = fileImage.listFiles(); 
				if (files == null || files.length == 0) {  
					fileImage.delete();  
					return;  
				}  
				// 遍历目录下所有的文件
				for (int i = 0; i < files.length; i++) { 
					this.deleteImage(files[i].getAbsolutePath()); 
				}
				fileImage.delete();
			}
		} else {
		}
	}*/

}
