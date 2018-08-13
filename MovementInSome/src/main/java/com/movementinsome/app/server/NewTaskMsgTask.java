package com.movementinsome.app.server;

import com.movementinsome.AppContext;
import com.movementinsome.app.pub.activity.WebSendOrdersActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

public class NewTaskMsgTask extends AsyncTask<String, Void, String>{

	private Context context;
	private final String NEW_ORDER_WARN ="/newOrderResource/newOrderWarn?uuid=";
	public NewTaskMsgTask (Context context){
		this.context = context;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		try {
			String imei=AppContext.getInstance().getPhoneIMEI();
			String url = AppContext.getInstance().getServerUrl().replace("fisds/rest", "gisApp/services");
			return HttpUtil.getRequest(url+NEW_ORDER_WARN+imei);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if("1".equals(result)){
			showMsg();
		}
	}
	private void showMsg(){
		new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题  
			.setMessage("是否直接进去工单派发页面")//设置显示的内容  
	    
		.setPositiveButton("是",new DialogInterface.OnClickListener() {//添加确定按钮  
	    	@Override  
	  
	         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
	    		// TODO Auto-generated method stub  
	    			Intent intent = new Intent();
	    			intent.setClass(context, WebSendOrdersActivity.class);
	    			context.startActivity(intent);
	    		}  
	  
	     }).setNegativeButton("否",new DialogInterface.OnClickListener() {//添加返回按钮  
	    	@Override  
	  
	         public void onClick(DialogInterface dialog, int which) {//响应事件  
	    		// TODO Auto-generated method stub  
	    			dialog.dismiss(); 
	            }  
	     }).show();
	}
}
