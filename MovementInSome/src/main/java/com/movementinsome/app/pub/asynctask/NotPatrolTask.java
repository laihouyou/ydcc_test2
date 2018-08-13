package com.movementinsome.app.pub.asynctask;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.movementinsome.AppContext;
import com.movementinsome.app.mytask.ShowPatrolDataExpActivity;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.database.vo.InsCheckFacRoad;
import com.movementinsome.database.vo.InsPatrolAreaData;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.util.JsonAnalysisUtil;
import com.j256.ormlite.dao.Dao;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

public class NotPatrolTask extends AsyncTask<String, Void, String> {
	
	private ProgressDialog progressDialog;
	private boolean isAnalysis;
	private ShowPatrolDataExpActivity showPatrolDataExpActivity;
	private InsTablePushTaskVo insTablePushTaskVo;
	private List<InsPatrolAreaData> insPatrolAreaDataList;
	private Handler sHandler;
	private List<InsCheckFacRoad> insCheckFacRoadList;
	
	public NotPatrolTask(ShowPatrolDataExpActivity showPatrolDataExpActivity, InsTablePushTaskVo insTablePushTaskVo, Handler sHandler, List<InsCheckFacRoad> insCheckFacRoadList){
		this.showPatrolDataExpActivity = showPatrolDataExpActivity;
		this.insTablePushTaskVo = insTablePushTaskVo;
		this.sHandler = sHandler;
		this.insCheckFacRoadList = insCheckFacRoadList;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog = new ProgressDialog(showPatrolDataExpActivity);
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
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String url = "";// 获取中间层url
		String result = null;
		JSONObject json = null;
		url=AppContext.getInstance().getServerUrl()+"/checkItemResource/download/fac";
		try {
			Dao<InsPatrolAreaData, Long> insPatrolAreaDataDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsPatrolAreaData.class);
			 insPatrolAreaDataList = insPatrolAreaDataDao.queryForEq("workTaskNum", insTablePushTaskVo.getTaskNum());
		} catch (Exception e) {
			// TODO: handle exception
		}
		JSONObject ob=new JSONObject();
		try {
			ob.put("areaId", insPatrolAreaDataList.get(0).getAreaId());
			ob.put("taskNum", insTablePushTaskVo.getTaskNum());
			ob.put("startNum", params[0]);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		result = SpringUtil.postData(url, ob.toString());
		try {
			JSONObject downloadObject = new JSONObject(result);
			String content0 = (String) downloadObject.get("content");
			JSONArray content = new JSONArray(content0);
			String code = downloadObject.getString("code");
			result =code+","+params[0];
			
			if("1".equals(code)){
				if (content != null && content.length() > 0) {
					for (int i = 0; i < content.length(); ++i) {
						InsCheckFacRoad insCheckFacRoad = new InsCheckFacRoad();
						JsonAnalysisUtil
						.setJsonObjectData(
								content.getJSONObject(i),
								insCheckFacRoad);
						insCheckFacRoadList.add(insCheckFacRoad);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		progressDialog.dismiss();
		String[] pd=result.split(",");
		if(result!= null){
			if(pd[0].equals("1")){
				if(pd[1].equals("0")){
					Toast.makeText(showPatrolDataExpActivity, "分析成功", Toast.LENGTH_SHORT).show();
					showPatrolDataExpActivity.setInsCheckFacRoadList(insCheckFacRoadList);
					sHandler.sendEmptyMessage(1);
				}else{
					Toast.makeText(showPatrolDataExpActivity, "分析成功", Toast.LENGTH_SHORT).show();
					showPatrolDataExpActivity.setInsCheckFacRoadList(insCheckFacRoadList);
					sHandler.sendEmptyMessage(2);
				}
			}else {
				Toast.makeText(showPatrolDataExpActivity, "分析失败", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
