package com.movementinsome.app.pub.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.movementinsome.AppContext;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.database.vo.InsCheckFacRoad;
import com.movementinsome.kernel.util.JsonAnalysisUtil;
import com.j256.ormlite.dao.Dao;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

public class RegionAnalyzeTask2 extends AsyncTask<String, Void, String> {
	
	private ProgressDialog progressDialog;
	private boolean isAnalysis;
	private Context context;
	private String code;
	private List<InsCheckFacRoad> insCheckFacRoadList;
	private String ids;
	private Handler sHandler;
	private String taskNum;
	private String downloadType;
	
	public RegionAnalyzeTask2( Context context, Handler sHandler, String ids, String taskNum, String downloadType){
		this.context = context;
		this.ids = ids;
		this.sHandler = sHandler;
		this.taskNum = taskNum;
		this.downloadType = downloadType;
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
		progressDialog.setMessage("上传中,请耐心等候。。。");
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
			ob.put("type", "lock");
			ob.put("ids", ids);
			ob.put("facType", downloadType);
			ob.put("username", AppContext.getInstance().getCurUser().getUserName());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		result = SpringUtil.postData(url, ob.toString());
		insCheckFacRoadList=new ArrayList<InsCheckFacRoad>();
		try {
			JSONObject downloadObject = new JSONObject(result);
			String content0 = (String) downloadObject.get("content");
			if(!("[]").equals(content0)){
				JSONArray content = new JSONArray(content0);
				code = downloadObject.getString("code");
				
				Dao<InsCheckFacRoad, Long> insCheckFacRoadDao = AppContext
						.getInstance().getAppDbHelper()
						.getDao(InsCheckFacRoad.class);
				
				if("1".equals(code)){
					if (content != null && content.length() > 0) {
						for (int i = 0; i < content.length(); ++i) {
							InsCheckFacRoad insCheckFacRoad = new InsCheckFacRoad();
							JsonAnalysisUtil
							.setJsonObjectData(
									content.getJSONObject(i),
									insCheckFacRoad);
							insCheckFacRoad.setWorkTaskNum(taskNum);
							insCheckFacRoad.setState(Long.valueOf("3"));
							insCheckFacRoadDao.update(insCheckFacRoad);
						}
					}
				}
			}
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
				Toast.makeText(context, "提交设施成功", Toast.LENGTH_SHORT).show();
				sHandler.sendEmptyMessage(5);
			}else{
				Toast.makeText(context, "提交设施失败", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(context, "提交设施失败", Toast.LENGTH_SHORT).show();
		}
	}

}
