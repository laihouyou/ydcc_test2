package com.movementinsome.app.pub.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.movementinsome.AppContext;
import com.movementinsome.app.server.SpringUtil;


public class CallbackTask extends AsyncTask<String, Void, String>{
	
	public CallbackTask(Context context) {
		// TODO Auto-generated constructor stub
		
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String url;
		url = AppContext.getInstance().getServerUrl();
/*		url += MyPublicData.CONFIRMPUSHMESSAGE+"uuid="+params[0]
		    +"&userName="+params[1]+"&workTaskNum="+params[2]+"&pushState="+params[3];
		SpringUtilFree<String> callBack = new SpringUtilFree<String>();
		callBack.getData(url, String.class);*/
		SpringUtil.configPushMessage(url, params[0], params[1], params[2], params[3]);
		return null;
	}

}
