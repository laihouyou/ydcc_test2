package com.movementinsome.app.pub.asynctask;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.movementinsome.AppContext;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.server.SpringUtil;

public class GetOffLineAsyncTask extends AsyncTask<String, Void, String>{

	private Context context;
	String url ;
	String phoneIMEI;
	String curUserName;
	
	public GetOffLineAsyncTask(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		url = AppContext.getInstance().getServerUrl();
		phoneIMEI = AppContext.getInstance().getPhoneIMEI();
		curUserName = AppContext.getInstance().getCurUser().getUserName();
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		
		//url+=MyPublicData.GETPUSHMESSAGE
		//+"uuid="+AppContext.getInstance().getPhoneIMEI()
		//+ "&userName="+AppContext.getInstance().getCurUser().getUserName();
		
		//SpringUtilFree<String> getTask = new SpringUtilFree<String>();
		
		String tasks = SpringUtil.getOffLineTask(url,phoneIMEI,curUserName);
		//String tasks = getTask.getData(url, String.class);	
		try {		
			tasks = URLDecoder.decode(tasks, "utf-8");	
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return tasks;
	}

	/*private void writeFile(String tr,String ts){
		try {
			FileOutputStream fo=new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/llcx.txt",true);
			fo.write(tr.getBytes());
			fo.write(ts.getBytes());
			fo.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result!= null){
			try {
				JSONArray jsonArray = new JSONArray(result);
				if(!jsonArray.isNull(0)){
					Intent intent = new Intent(Constant.ACTION_SHOW_NOTIFICATION);
					for(int i=0;i<jsonArray.length();++i){
						String pushNum=jsonArray.getJSONObject(i).getString("PUSH_NUM");// 用户+imei
						
						String pushMessage=jsonArray.getJSONObject(i).getString("PUSH_MESSAGE");
						String title=jsonArray.getJSONObject(i).getString("PUSH_TITLE");
						String pushState=jsonArray.getJSONObject(i).getString("PUSH_STATE");// 推送状态0：离线，1：在线
						String createDate=jsonArray.getJSONObject(i).getString("CREATE_DATE");// 日期
						
						intent .putExtra(Constant.NOTIFICATION_TITLE,title);
						intent.putExtra(Constant.NOTIFICATION_MESSAGE,pushMessage );
						intent.putExtra(Constant.NOTIFICATION_PUSH_STATE,pushState);
						context.sendBroadcast(intent);
					}	
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				
			}
		}
 	}
	
	
}
