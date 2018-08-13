package com.movementinsome.map.offvalve.asynctask;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.esri.core.geometry.Point;
import com.movementinsome.AppContext;
import com.movementinsome.kernel.util.HttpClientUtil;
import com.movementinsome.map.MapLoad;
import com.movementinsome.map.offvalve.PipeBroAnalysisTools;


public class PipeBrokenAnalysisUpload extends AsyncTask<List<String>, Void, JSONObject>{

	private final String CLOSET_FEATURE ="closetFeature";
	
	private ProgressDialog progressDialog;
	private Context context;
	private PipeBroAnalysisTools pipeBroAnalysisTools;
	
	/**
	 * 
	 * @param mapActivity
	 * @param output
	 */
	public PipeBrokenAnalysisUpload(PipeBroAnalysisTools pipeBroAnalysisTools) {
		// TODO Auto-generated constructor stub
		this.pipeBroAnalysisTools=pipeBroAnalysisTools;
		this.context = pipeBroAnalysisTools.getContext(); 
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
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
		progressDialog.setMessage("修正爆点中。。。");
		progressDialog.show();
	}
	
	@Override
	protected JSONObject doInBackground(List<String>... params) {
		// TODO Auto-generated method stub
		JSONObject json = null;
		String url = MapLoad.mapParam.findExtAnaServiceUrl(CLOSET_FEATURE,AppContext.getInstance().getAccNetState());//获取中间层url
/*		try{
			List<Module> moduels=AppContext.configurations.get(0).getModules().getModuels();
			for(Module module:moduels){
				if("爆管分析".equals(module.getName())){
					List<Relation>  relationList=module.getRelations().getRelationList();
					for(Relation relation:relationList){
						if("爆漏目标".equals(relation.getName())){
							url=relation.getForeign();
							break;
						}
					}
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			return null;
		}*/
		url += "?x="+params[0].get(0)+"&y="+params[0].get(1)+"&edge="+params[0].get(2)+"&f=pjson";//
		String result =HttpClientUtil.postData(url,null);
		if(result==null)
			return null;
		try {
			pipeBroAnalysisTools.setPipeBreakenData(result);
			json = new JSONObject(result);
			JSONArray jsonArray = json.getJSONArray("targetLeaks");
			if(jsonArray.getString(0).equals("null"))
				return null;
			JSONObject extendLeadData = json;
			JSONArray jsonList = new JSONArray();
			jsonList.put(jsonArray.getJSONObject(0).put("attribute", "null"));
			extendLeadData = extendLeadData.put("targetLeaks",jsonList);
			String jsonContent = extendLeadData.toString();
			//AppContext.pEditor.putString("extendLeadData",jsonContent).commit();
			json = jsonArray.getJSONObject(0);
			json = json.getJSONArray("location").getJSONObject(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		progressDialog.dismiss();
		if(result!= null){
			try {
				String x = result.getString("x");
				String y = result.getString("y");
				Point point = new Point(Float.parseFloat(x),
						Float.parseFloat(y));
			//	pipeBroAnalysisTools.analysisGraphicPoint(Float.parseFloat(x), Float.parseFloat(y), "1");
				PipeBroAnalysis analysisTask = new PipeBroAnalysis(pipeBroAnalysisTools, true,point);
				analysisTask.execute("ANALYSIS");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			Toast.makeText(context, "修正失败，请重新选择", 4).show();
		}
	}
}
