package com.movementinsome.app.server;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.app.mytask.handle.PublicHandle;

public class UploadDataTask extends AsyncTask<String, Void, String>{
	private Activity context;			//context对象应用
	private PublicHandle publicHandle;
	private ProgressDialog progre;		//进度条显示
	private String uploadData;// 上传内容
	public UploadDataTask(Activity context,PublicHandle publicHandle,String uploadData){
		this.context=context;
		this.uploadData=uploadData;
		this.publicHandle=publicHandle;
		progre = new ProgressDialog(context);
		progre.setCancelable(false);
		progre.setCanceledOnTouchOutside(false);
		progre.setMessage("正在上传数据,请勿退出,耐心等候……");
		progre.show();
	}
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String url = AppContext.getInstance().getServerUrl();//+MyPublicData.DYMANICFROMUPLOAD;
		//SpringUtil su = new SpringUtil(context);
		
		return SpringUtil.dymanicFormUpload(url, uploadData);
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(progre!=null){
			progre.dismiss();       
		}
		if(result!=null){
			try {
				org.json.JSONObject reltJson = new org.json.JSONObject(result);
				if ("1".equals(reltJson.getString("code"))){
					if(publicHandle!=null){
						publicHandle.updateData();
					}
					Toast.makeText(context, "提交成功", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(context, "提交失败，请重新提交", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(context, "提交失败，请重新提交", Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(context, "提交失败，请重新提交", Toast.LENGTH_LONG).show();
		}
	}
	
}
