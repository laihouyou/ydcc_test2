package com.movementinsome.map.facedit;


import com.movementinsome.app.server.SpringUtilFree;
import com.movementinsome.map.facedit.dialog.FacEditDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;

@SuppressWarnings("unused")
public class ObtainGid extends AsyncTask<String, Integer, String>{
	private Context context;
	private ProgressDialog progressDialog;
	private SpringUtilFree springUtilFree;
	private FacEditDialog facEditDialog;
	

	public ObtainGid(Context context,FacEditDialog FacEditDialog) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.facEditDialog=FacEditDialog;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog = new ProgressDialog(this.context);
		progressDialog.setMessage("正在获取GID,请等待……");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();

	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String result = "";
		springUtilFree= new SpringUtilFree<String>();
		result = springUtilFree.postData3(params[0],"");
		return result;
	}
	protected void onPostExecute(String result) {
		if (result!=null) {
			Message message = new Message();
			message.what = 1;
			Bundle mBundle = new Bundle();  
			mBundle.putString("gid", result);
			message.setData(mBundle);
			facEditDialog.gidHandler.sendMessage(message);
			
		}else{
			Message message = new Message();
			message.what = 2;
			facEditDialog.gidHandler.sendMessage(message);
		}
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
}
