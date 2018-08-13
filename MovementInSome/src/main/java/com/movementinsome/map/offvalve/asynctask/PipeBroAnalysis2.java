package com.movementinsome.map.offvalve.asynctask;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.movementinsome.map.offvalve.PipeBroAnalysisTools2;
import com.movementinsome.map.view.MyMapView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PipeBroAnalysis2 extends AsyncTask<String, Void, String> {

	private final String OFFVALVA_ANALYSIS = "offValveAna";

	private ProgressDialog progressDialog;
	private Context context;
	private Gson gson;
	private Type type;
	private Point point;
	private boolean isAnalysis; // 分析：true；展现地图：false
	private MyMapView map;
	private PipeBroAnalysisTools2 pipeBroAnalysisTools2;
	List<String> bound;
	List<Map<String, String>> data;
	private String value;

	public PipeBroAnalysis2(PipeBroAnalysisTools2 pipeBroAnalysisTools2,
			boolean isAnalysis, Point point, String value) {
		// TODO Auto-generated constructor stub
		this.pipeBroAnalysisTools2 = pipeBroAnalysisTools2;
		this.context = pipeBroAnalysisTools2.getContext();
		this.isAnalysis = isAnalysis;
		this.map = pipeBroAnalysisTools2.getMap();
		this.point = point;
		this.value = value;
		gson = new Gson();
		type = new TypeToken<List<String>>() {
		}.getType();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog = new ProgressDialog(context);
		if (isAnalysis) {
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
	}

	@SuppressWarnings("static-access")
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
			
		JSONObject json = null;
		String result = null;
		String url = "";// 获取中间层url
		Map<String, String> valuePair = new HashMap<String, String>();// 上传参数
		String target =  pipeBroAnalysisTools2.getPipeBreakenData();
		JSONObject jo=null;
		try {
			jo = new JSONObject(target);
			String geometricnetwork = jo.getString("geometricnetwork");
			valuePair.put("geometricnetwork", geometricnetwork);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (params[0].equals("ANALYSIS")) {
			url = getPipeBroAnalysisUrl("爆漏分析")+"?f=pjson";
			valuePair.put("target", jo.toString());// 转格式去掉换行
			valuePair.put("geometricnetwork", "SDE.WATER_NETWORK_Net");
			valuePair.put("f", "json");
			result = HttpClientUtil.postData(url, valuePair);
		} else if (params[0].equals("EXTEND")) {
			url = getPipeBroAnalysisUrl("扩大关阀分析")+"?f=pjson";
			valuePair.put("target", jo.toString());
			valuePair.put("geometricnetwork", "SDE.WATER_NETWORK_Net");
			result = HttpClientUtil.postData(url, valuePair);
			if (result != null) {
				// AppContext.DB.delTable(MyPublicData.PIPE_TABLE_NAME+"userName");
			}
		}else if (params[0].equals("SHOW")){
			result = value;
		}
		if (result == null){
			return null;
		}else{
			result = result.replaceAll("\n", "");
			pipeBroAnalysisTools2.setResult(result);
		}
		Map<String, String> mapKey = new HashMap<String, String>();
		bound = new ArrayList<String>();
		JSONObject jsonBound = null;
		try {
			json = new JSONObject(result);
			jsonBound = json.getJSONObject("bound");
			bound.add(jsonBound.getString("maxx"));
			bound.add(jsonBound.getString("maxy"));
			bound.add(jsonBound.getString("minx"));
			bound.add(jsonBound.getString("miny"));
			// AppContext.pEditor.putString("bound", gson.toJson(bound, type));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// AppContext.pEditor.putString("bound", null);
		}
		// AppContext.pEditor.commit();

		try {
			JSONArray jsondstGeometry = json.getJSONArray("dstGeometry");
			// AppContext.pEditor.putString("dstGeometry",
			// jsondstGeometry.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// AppContext.pEditor.putString("dstGeometry", null);
		}
		// AppContext.pEditor.commit();

		try {
			JSONObject jsonEffectRegion = json.getJSONObject("effectRegion");
			JSONObject jsonGeometry = jsonEffectRegion
					.getJSONObject("geometry");
			JSONArray jsonRings = jsonGeometry.getJSONArray("rings");
			// AppContext.pEditor.putString("rings", jsonRings.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// AppContext.pEditor.putString("rings", null);
		}
		// AppContext.pEditor.commit();

		try {
			JSONObject jsonSnkfp = json.getJSONObject("snkfp");
			// AppContext.pEditor.putString("snkfp", jsonSnkfp.toString());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			// AppContext.pEditor.putString("snkfp", null);
		}
		// AppContext.pEditor.commit();

		try {
			JSONObject jsonSchfp = json.getJSONObject("schfp");
			jsonSchfp = jsonSchfp.getJSONObject("fields");
			JSONArray jsonArraySchfp = jsonSchfp.getJSONArray("fields");

			int size = jsonArraySchfp.length();
			for (int i = 0; i < size; i++) {
				jsonSchfp = jsonArraySchfp.getJSONObject(i);
				mapKey.put(jsonSchfp.getString("idx"),
						jsonSchfp.getString("name"));
			}
			// 创建表
			// AppContext.DB.pipeBroTable(mapKey,"userName");
			JSONArray jsondstRow = json.getJSONObject("schfp").getJSONArray(
					"rows");
			try {
				List<String> point;
				data = new ArrayList<Map<String, String>>();
				for (int i = 0; i < jsondstRow.length(); i++) {
					Map<String, String> mp = new HashMap<String, String>();

					JSONObject jsondstrow = jsondstRow.getJSONObject(i);
					JSONObject jsondstGeometry = jsondstrow
							.getJSONObject("dstGeometry");
					JSONArray jsonPoint = jsondstGeometry
							.getJSONArray("points");
					point = new ArrayList<String>();
					point.add(jsonPoint.getJSONObject(0).getString("x"));
					point.add(jsonPoint.getJSONObject(0).getString("y"));
					mp.put("points", gson.toJson(point, type));
					JSONObject jsonValues = jsondstrow.getJSONObject("values");
					Iterator<String> it = jsonValues.keys();
					while (it.hasNext()) {
						String key = it.next();
						mp.put(mapKey.get(key), jsonValues.getString(key));
					}
					JSONObject jsonNetElement = jsondstrow
							.getJSONObject("netElement");
					mp.put("netElement", jsonNetElement.toString());
					mp.put("state", "0");
					data.add(mp);
				}
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return "SUCCEED";
	}

	private String getPipeBroAnalysisUrl(String type) {
		String url = MapLoad.mapParam.findExtAnaServiceUrl(OFFVALVA_ANALYSIS,
				AppContext.getInstance().getAccNetState());
		/*
		 * try{ List<Module>
		 * moduels=AppContext.configurations.get(0).getModules().getModuels();
		 * for(Module module:moduels){ if("爆管分析".equals(module.getName())){
		 * List<Relation> relationList=module.getRelations().getRelationList();
		 * for(Relation relation:relationList){
		 * if(type.equals(relation.getName())){ url=relation.getForeign();
		 * break; } } } } }catch (Exception e) { // TODO: handle exception }
		 */
		return url;
	}

	@SuppressWarnings("static-access")
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		progressDialog.dismiss();

		if (result == null) {
			Toast.makeText(context, "分析失败，请检查网络后重试", 4).show();
			return;
		}
		if (result.equals("SUCCEED") || result.equals("SHOW")) {
			if (result.equals("SUCCEED")){
				Toast.makeText(context, "分析完毕", 4).show();
			}
			pipeBroAnalysisTools2.setData(data);
			pipeBroAnalysisTools2.setBound(bound);
			pipeBroAnalysisTools2.getBadSwitchs().removeAll(pipeBroAnalysisTools2.getBadSwitchs());
			pipeBroAnalysisTools2.showList();
		}
	}
}
