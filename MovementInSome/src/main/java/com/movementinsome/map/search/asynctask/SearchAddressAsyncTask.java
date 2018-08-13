package com.movementinsome.map.search.asynctask;

import com.movementinsome.AppContext;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.map.search.SearchDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;


public class SearchAddressAsyncTask extends AsyncTask<String, String, String> {

	private Context context;
	private SearchDialog searchDialog;
	private ProgressDialog progressDialog;
	private String ROAD_TASK="roadTask";

	public SearchAddressAsyncTask(Context context,SearchDialog searchDialog,String address){
		this.context = context;
		this.searchDialog=searchDialog;
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setIndeterminate(true);
			progressDialog.setButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					cancel(true);
				}
			});
		}
		progressDialog.setMessage("正在查询,请稍后……");
		progressDialog.show();
		String url = AppContext.getInstance().getTask("roadTask").getForeign();
		if (null != url)
			execute(url,address);
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		//SpringUtil spring = new SpringUtil(context);
		String value = SpringUtil.addressSearch(params[0],params[1]);
		return value;
	}


	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(progressDialog != null){
			progressDialog.dismiss();
		}
		searchDialog.analysisJson(result);
	}

}
