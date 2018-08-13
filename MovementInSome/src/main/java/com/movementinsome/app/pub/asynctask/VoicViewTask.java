package com.movementinsome.app.pub.asynctask;

import com.movementinsome.app.mytask.ShowTaskMsgActivity;
import com.movementinsome.app.server.SpringUtil;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;

public class VoicViewTask extends AsyncTask<String, Void, String> {
	
	private ProgressDialog progressDialog;
	private ShowTaskMsgActivity context;
	private String code;
	private Handler sHandler;
	private String name;
	private String guid;
	
	public VoicViewTask(ShowTaskMsgActivity context, String guid, Handler sHandler, String name){
		this.context = context;
		this.sHandler = sHandler;
		this.name = name;
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
		String result = SpringUtil.VoidData(arg0[0],name);
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		progressDialog.dismiss();
		if(result!=null){
//			context.setmFileName(result);
			sHandler.sendEmptyMessage(1);
		}
		
	}

}
