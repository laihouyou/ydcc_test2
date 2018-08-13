package com.movementinsome.app.pub.asynctask;

import org.json.JSONException;
import org.json.JSONObject;

import com.movementinsome.AppContext;
import com.movementinsome.app.mytask.ShowPatrolDataExpActivity;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.database.vo.InsCheckFacRoad;
import com.j256.ormlite.dao.Dao;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

public class RegionAnalyzeTask5 extends AsyncTask<String, Void, String> {
	
	private ProgressDialog progressDialog;
	private boolean isAnalysis;
	private Context context;
	private String code;
	private InsCheckFacRoad insCheckFacRoad;
	private ShowPatrolDataExpActivity showPatrolDataExpActivity;
	
	public RegionAnalyzeTask5( Context context, InsCheckFacRoad insCheckFacRoad){
		this.context = context;
		this.insCheckFacRoad = insCheckFacRoad;
		this.showPatrolDataExpActivity = showPatrolDataExpActivity;
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
		progressDialog.setMessage("分析中,请耐心等候。。。");
		progressDialog.show();
		
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		String url = "";// 获取中间层url
		String result = null;
		JSONObject json = null;
		url=AppContext.getInstance().getServerUrl()+"/checkItemResource/lock/fac";
		JSONObject ob=new JSONObject();
		try {
			ob.put("type", "unlock");
			ob.put("ids", insCheckFacRoad.getId());
			ob.put("username", AppContext.getInstance().getCurUser().getUserName());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		result = SpringUtil.postData(url, ob.toString());
		try {
				
				Dao<InsCheckFacRoad, Long> insCheckFacRoadDao = AppContext
						.getInstance().getAppDbHelper()
						.getDao(InsCheckFacRoad.class);
				
				int fanhui=insCheckFacRoadDao.delete(insCheckFacRoad);
				code=String.valueOf(fanhui);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return code;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		progressDialog.dismiss();
		if(result!= null){
			if(result.equals("1")){
				Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
		}
	}

}
