package com.movementinsome.map.facSublistEdit.asynctask;

import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

import com.esri.core.geometry.Envelope;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.ags.query.QueryTask;
import com.esri.core.tasks.ags.query.RelationshipQuery;
import com.movementinsome.kernel.initial.model.Ftlayer;
import com.movementinsome.map.facSublistEdit.dialog.FacSublistEditDialog;
import com.movementinsome.map.facedit.vo.FacAttribute;
import com.movementinsome.map.view.MyMapView;

public class FacSublistQueryTask extends AsyncTask<String, Void, Map<Integer, FeatureSet>>{

	private Envelope env;// 要查询的面
	private ProgressDialog progress;
	private Context context;
	private MyMapView map;
	private FacAttribute facAttribute; 
	private Ftlayer ftlayer;
	private FacSublistEditDialog facSublistEditDialog;
	public FacSublistQueryTask(Context context,MyMapView map,FacAttribute facAttribute,Ftlayer ftlayer){
		this.context=context;
		this.map=map;
		this.facAttribute=facAttribute;
		this.ftlayer = ftlayer;
		
		Envelope env0=new Envelope();
		map.getExtent().queryEnvelope(env0);
		double mh=env0.getHeight();
		double mw=env0.getWidth();//地图宽度
		int ph=map.getHeight();
		int pw=map.getWidth();//地图容器宽度	
		env=new Envelope(facAttribute.getEditPoint(), (mw/(pw*1.0))*50, (mh/(ph*1.0))*50);
	}
	@SuppressWarnings("static-access")
	@Override
	protected void onPreExecute() {
		if (progress == null) {
			progress = new ProgressDialog(context);
			progress.setIndeterminate(true);
			progress.setButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					cancel(true);
					Toast.makeText(context, "已取消查询", Toast.LENGTH_LONG).show();
				}
			});
		}
		progress.setMessage("正在查询水表,请稍后……");
		progress.show();
	}
	@Override
	protected Map<Integer, FeatureSet> doInBackground(String... params) {
		// TODO Auto-generated method stub
		Map<Integer, FeatureSet> fss=null;
		Query query = new Query();
		query.setGeometry(this.env);
		query.setOutSpatialReference(map.getSpatialReference());
		query.setReturnGeometry(true);
		query.setOutFields(new String[]{params[0]});
		query.setWhere("1=1");
		@SuppressWarnings("deprecation")
		QueryTask qTask = new QueryTask(facAttribute.getQueryUrl());
		try {
			FeatureSet fs = qTask.execute(query);
			if(fs!=null){
				Graphic[] graphic = fs.getGraphics();
				if(graphic!=null&&graphic.length>0){
					int OBJECTID[]=new int[graphic.length];
					for(int i=0;i<graphic.length;++i){		
						OBJECTID[i]=(Integer) graphic[i].getAttributes().get("OBJECTID");
					}
					RelationshipQuery relationshipQuery=new RelationshipQuery();
					relationshipQuery.setObjectIds(OBJECTID);
					relationshipQuery.setRelationshipId(ftlayer.getRelationshipId());
					relationshipQuery.setOutFields(new String[]{"*"});
					fss=qTask.executeRelationshipQuery(relationshipQuery);
					return fss;
				}else{
					return null;
				}
			}else{
				return null;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(Map<Integer, FeatureSet> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		progress.dismiss();
		if(result!=null){
			if(facSublistEditDialog == null){
				facSublistEditDialog = new FacSublistEditDialog(context,map, result, facAttribute, ftlayer);
				facSublistEditDialog.showAtLocation(map, Gravity.CENTER, 0, 0);
			}else{
				facSublistEditDialog.updateList(result);
				facSublistEditDialog.getFacSublistAdapter().notifyDataSetChanged();
				if(facSublistEditDialog.isShow()){
					facSublistEditDialog.blak();
				}
			}
		}else{
			Toast.makeText(context, "查询不到水表", Toast.LENGTH_LONG).show();
		}
	}
	public FacSublistEditDialog getFacSublistEditDialog() {
		return facSublistEditDialog;
	}
	public void setFacSublistEditDialog(FacSublistEditDialog facSublistEditDialog) {
		this.facSublistEditDialog = facSublistEditDialog;
	}
}
