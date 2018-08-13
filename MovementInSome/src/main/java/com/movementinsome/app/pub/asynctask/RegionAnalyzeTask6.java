package com.movementinsome.app.pub.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.movementinsome.AppContext;
import com.movementinsome.app.mytask.ShowPatrolDataExpActivity;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.database.vo.InsCheckFacRoad;
import com.movementinsome.database.vo.InsPatrolAreaData;
import com.movementinsome.kernel.util.JsonAnalysisUtil;
import com.j256.ormlite.dao.Dao;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

public class RegionAnalyzeTask6 extends AsyncTask<String, Void, String> {
	
	private ProgressDialog progressDialog;
	private boolean isAnalysis;
	private Context context;
	private List<InsPatrolAreaData> insPatrolAreaDataList;
	private String polygonStr;
	private String tableNum;
	private String code;
	private List<InsCheckFacRoad> insCheckFacRoadList;
	private ShowPatrolDataExpActivity showPatrolDataExpActivity;
	private String facType;
	
	public RegionAnalyzeTask6(ShowPatrolDataExpActivity showPatrolDataExpActivity, Context context, String polygonStr, String tableNum, String facType){
		this.context = context;
		this.polygonStr = polygonStr;
		this.tableNum = tableNum;
		this.facType = facType;
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
		url=AppContext.getInstance().getServerUrl()+"/checkItemResource/select/fac";
		try {
			Dao<InsPatrolAreaData, Long> insPatrolAreaDataDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsPatrolAreaData.class);
			 insPatrolAreaDataList = insPatrolAreaDataDao.queryForEq("workTaskNum", tableNum);
		} catch (Exception e) {
			// TODO: handle exception
		}
		JSONObject ob=new JSONObject();
		try {
			ob.put("areaId", insPatrolAreaDataList.get(0).getAreaId());
			ob.put("taskNum", tableNum);
			ob.put("shape", polygonStr);
			ob.put("radio", 3);
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
			JSONArray content = new JSONArray(content0);
			code = downloadObject.getString("code");
			Dao<InsCheckFacRoad, Long> insCheckFacRoadDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsCheckFacRoad.class);
			
			if("1".equals(code)){
				if (content != null && content.length() > 0) {
					for (int i = 0; i < content.length(); ++i) {
							if(content.getJSONObject(i).get("facType").toString().equals(facType)){
								if(content.getJSONObject(i).get("id")!=null){
									InsCheckFacRoad insCheckFacRoad = new InsCheckFacRoad();
									JsonAnalysisUtil
									.setJsonObjectData(
											content.getJSONObject(i),
											insCheckFacRoad);
									insCheckFacRoad.setWorkTaskNum(tableNum);
									if(facType.equals("阀门")){
										insCheckFacRoad.setAndroidForm("biz.ins_nearby_value_gs");
									}else{
										insCheckFacRoad.setAndroidForm("biz.ins_boosterimpact_form_gs");
									}
									insCheckFacRoadList.add(insCheckFacRoad);
									insCheckFacRoadDao.create(insCheckFacRoad);
								}else{
									return "2";
								}
							}
						
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			return "2";
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
				Toast.makeText(context, "获取数据成功", Toast.LENGTH_SHORT).show();
//				showPatrolDataExpActivity.setInsCheckFacRoadList(insCheckFacRoadList);
				showPatrolDataExpActivity.showList();
			}else if(result.equals("2")){
				Toast.makeText(context, "不在任务范围内", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(context, "获取数据失败", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(context, "获取数据失败", Toast.LENGTH_SHORT).show();
		}
	}

}
