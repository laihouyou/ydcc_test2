package com.movementinsome.map.nearby;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.ags.query.QueryTask;
import com.movementinsome.R;
import com.movementinsome.map.view.MyMapView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AsyncQueryTask extends AsyncTask<String, Void, List<FeatureSet>> {

	private Context context;
	private MyMapView mapview;
	private GraphicsLayer grapLayer;
	private Point point;
	private ProgressDialog progress;
	private LinearLayout linearP;
	private TextView tv;
	private String crId,num;
	private String [] layerIds;
	private Envelope env;
	private NearByTools nearByTools;
	
	public AsyncQueryTask(Context context, MyMapView mapview,
			GraphicsLayer grapLayer) {
		this.context = context;
		this.mapview = mapview;
		this.grapLayer = grapLayer;
		
	}
	
	public AsyncQueryTask(Context context, MyMapView mapview,
			GraphicsLayer grapLayer, float x, float y,String [] layerIds
			,NearByTools nearByTools) {
		this.context = context;
		this.mapview = mapview;
		this.grapLayer = grapLayer;
		this.point = this.mapview.toMapPoint(x, y);
		/*Envelope env0=new Envelope();
		mapview.getExtent().queryEnvelope(env0);
		double mh=env0.getHeight();
		double mw=env0.getWidth();//地图宽度
		int ph=mapview.getHeight();
		int pw=mapview.getWidth();//地图容器宽度
*/
		if(point!=null){
			env=new Envelope(point, 60, 60);
		}
		this.layerIds=layerIds;
		this.nearByTools=nearByTools;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (progress == null) {
			progress = new ProgressDialog(context);
			progress.setIndeterminate(true);
			progress.setButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					cancel(true);
					ToastUtils.show("已取消查询");
				}
			});
		}
		progress.setMessage("正在查询施……");
		progress.show();
		//mapCalloutShow();
	}
	
	@Override
	protected List<FeatureSet> doInBackground(String... queryParams) {
		if (queryParams == null || queryParams.length <= 1||env==null)
			return null;
		//		context.ZZDB.fieldsSelect("", "query");
		List<FeatureSet> featureSetList=new ArrayList<FeatureSet>();
		for(int i=0;i<layerIds.length;++i){
			if (mapview.isLoaded()) {
				String url = queryParams[0]+"/"+layerIds[i];
				Query query = new Query();
				String whereClause = queryParams[1];
				query.setGeometry(env);
				if (mapview.getSpatialReference().getID()!=0)
					query.setOutSpatialReference(mapview.getSpatialReference());
				query.setReturnGeometry(true);
				query.setOutFields(new String[]{"*"});
				if (!"".equals(whereClause)){
					query.setWhere(whereClause);
				}
				QueryTask qTask = new QueryTask(url);
				FeatureSet fs = null;
				try {
					fs = qTask.execute(query);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				featureSetList.add(fs);
			}
		}
		
		return featureSetList;
	}
	
	@Override
	protected void onPostExecute(List<FeatureSet> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		progress.dismiss();
		if (result==null){
			ToastUtils.show("查询失败");
			return ;
		}
		if(result.size()<=0){
			ToastUtils.show("附近没有设施");
			return ;
		}
		try {
			List<Graphic> graphicList =new ArrayList<Graphic>();
			for(int i=0;i<result.size();++i){
				Graphic[] graphic = result.get(i).getGraphics();
				for(int j=0;j<graphic.length;++j){
					graphicList.add(graphic[j]);
				}
				/*if (graphic.length > 0) {
					SimpleFillSymbol symbol = new SimpleFillSymbol(Color.RED);
					grapLayer.setRenderer(new SimpleRenderer(symbol));
					grapLayer.removeAll();
					grapLayer.addGraphic(graphic[0]);
					
					mapview.zoomToScale(mapview.getCenter(), mapview.getScale());
					if (linearP != null) {
						
						mapview.getCallout().show(mapview.getCenter(), linearP);
					}
					ToastUtils.getInstance(context).show("显示查询结果");
					return;
				}*/
			}
			if(graphicList.size()>0){
				nearByTools.showFacList(result,graphicList);
			}else{
				ToastUtils.show("附近没有设施");
			}
		} catch (Exception e) {
			// TODO: handle exception
			ToastUtils.show("查询区域面异常");
			System.out.println(e);
		}

	}

	private void mapCalloutShow(){
		LinearLayout.LayoutParams btnparams = new LinearLayout.LayoutParams(80, -2);
		linearP = new LinearLayout(context);
		linearP.setOrientation(LinearLayout.VERTICAL);

		LinearLayout linearB = new LinearLayout(context);
		linearB.setOrientation(LinearLayout.HORIZONTAL);
		linearB.setGravity(Gravity.CENTER);

		Button btnTable = new Button(context);
		Button btnClear = new Button(context);
		Button btnFinish = new Button(context);
		btnTable.setBackgroundResource(R.drawable.btn_bg);
		btnClear.setBackgroundResource(R.drawable.btn_bg);
		btnFinish.setBackgroundResource(R.drawable.btn_bg);
		btnTable.setText("监  控");
		btnClear.setText("关  闭");
		btnFinish.setText("撤  销");
		/*if(context.pRes.getString(R.string.VersionsType).equals("HD")){
			btnTable.setLayoutParams(btnparams);
			btnClear.setLayoutParams(btnparams);
			btnFinish.setLayoutParams(btnparams);
		}*/
/*		if(context.dm.widthPixels>480&&context.dm.heightPixels>800)	{
			btnTable.setLayoutParams(btnparams);
			btnClear.setLayoutParams(btnparams);
			btnFinish.setLayoutParams(btnparams);
		}*/
		btnTable.setTextColor(Color.WHITE);
		btnClear.setTextColor(Color.WHITE);
		btnFinish.setTextColor(Color.WHITE);
		linearB.addView(btnClear);
		linearB.addView(btnTable);
		linearB.addView(btnFinish);

		tv = new TextView(context);
		linearP.addView(linearB);
		linearP.addView(tv);

		btnTable.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mapview.getCallout().hide();
				grapLayer.removeAll();
	/*			Intent intent = new Intent(context, XjsgsbActivity.class);
				intent.putExtra("crid", crId);//登记id
				intent.putExtra("num", num);// 施工编号
				context.startActivity(intent);*/
			}
		});
		btnClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				grapLayer.removeAll();
				mapview.getCallout().hide();
			}
		});
		btnFinish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				grapLayer.removeAll();
				mapview.getCallout().hide();
				String param = null;
				try {
					JSONObject json = new JSONObject();
					json.put("constructionNum", num);
					param = json.toString();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String[] params = new String[]{"InsTableConstructionRegisteredVO",param,"1"}; 
				uploadDataState uploadDataState = new uploadDataState();
				uploadDataState.execute(params);
			}
		});
	}
	
	private class uploadDataState extends AsyncTask<String, Void, String>{
		private ProgressDialog progressDialog;

		public uploadDataState() {
			progressDialog = new ProgressDialog(context);
			progressDialog.setIndeterminate(true);
			progressDialog.setButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					cancel(true);
				}
			});
			progressDialog.setMessage("正在提交,请稍后……");
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = null ;
/*			SpringUtil spring = new SpringUtil(context);
			result = spring.finishMinitor(params[0], params[1], params[2]);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(progressDialog != null)
				progressDialog.dismiss();
			if(result.equals("1")){
				//context.upDateFl();
				ToastUtils.show("提交成功");

			}else{
				ToastUtils.show("提交失败");

			}
		}
	}
}
