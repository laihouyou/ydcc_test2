package com.movementinsome.caice.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.caice.okhttp.ProjectRequest;
import com.movementinsome.caice.vo.DetelePoiVo;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

public class ShareProjectAsyn extends AsyncTask<String, Void, String> {
	private Activity context;
	private ProgressDialog progressDialog;
	private JSONObject jsonObject;
	private DetelePoiVo detelePoiVo;

	public ShareProjectAsyn(Activity context, JSONObject jsonObject) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.jsonObject=jsonObject;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog = new ProgressDialog(context);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(context.getString(R.string.load_msg));
		progressDialog.show();
		
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String result = "Ok";
		try {
			detelePoiVo = ProjectRequest.ProjectShate(jsonObject, context);
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}

		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
		if(result!=null&&result.equals("Ok")){
			if (detelePoiVo!=null){
				if (detelePoiVo.getCode()==0){
					Toast.makeText(context, detelePoiVo.getMsg(), Toast.LENGTH_LONG).show();
					EventBus.getDefault().post(Constant.FAC_LIST_UPDATA);
				}else {
					Toast.makeText(context, detelePoiVo.getMsg(), Toast.LENGTH_LONG).show();
				}
			}
		}else{
			Toast.makeText(context,"", Toast.LENGTH_LONG).show();
		}
	}
}
