package com.movementinsome.app.pub.asynctask;

import org.json.JSONArray;
import org.json.JSONException;

import com.movementinsome.app.mytask.ShowTaskMsgActivity;
import com.movementinsome.app.server.SpringUtil;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

public class AttachmentTask extends AsyncTask<String, Void, String> {
	
	private ProgressDialog progressDialog;
	private ShowTaskMsgActivity context;
	private Handler sHandler;
	private String guid;
	private String url;
	
	public AttachmentTask(ShowTaskMsgActivity context, String guid, Handler sHandler){
		this.context = context;
		this.sHandler = sHandler;
		this.guid = guid;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog = new ProgressDialog(context);
		progressDialog.setIndeterminate(true);
		progressDialog.setButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						cancel(true);
					}
				});
		progressDialog.setCancelable(false);
		progressDialog.setMessage("下载语音,请耐心等候。。。");
		progressDialog.show();
		
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		String result = SpringUtil.postData2(arg0[0], "");
		if(result!=null){
			try {
				JSONArray content = new JSONArray(result);
				for (int i = 0; i < content.length(); i++) {
					if(content.getJSONObject(i).get("businessType").equals("录音")){
						result=content.getJSONObject(i).get("pfiName").toString();
						url = content.getJSONObject(i).get("photoURL").toString();
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		progressDialog.dismiss();
		if(result.contains(".ogg")){
//			context.setAttachment(result+","+url);
			sHandler.sendEmptyMessage(2);
		}else{
			Toast.makeText(context, "没有录音附件", Toast.LENGTH_SHORT).show();
		}
		
	}

}
