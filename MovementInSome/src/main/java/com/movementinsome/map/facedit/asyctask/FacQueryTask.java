package com.movementinsome.map.facedit.asyctask;

import java.util.List;
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
import com.esri.core.tasks.identify.IdentifyResult;
import com.movementinsome.kernel.initial.model.Ftlayer;
import com.movementinsome.kernel.initial.model.MapParam;
import com.movementinsome.map.facedit.MapEditFac;
import com.movementinsome.map.facedit.dialog.FacEditDialog;
import com.movementinsome.map.facedit.vo.FacAttribute;
import com.movementinsome.map.view.MyMapView;

public class FacQueryTask extends AsyncTask<String, Void, FeatureSet>{

	private Envelope env;// 要查询的面
	private ProgressDialog progress;
	private Context context;
	private MyMapView map;
	//地图设置参数
	private MapParam mapParam;
	private Map<String , List<IdentifyResult>> identifyResultLists;
	private FacAttribute facAttribute; 
	private String type ;
	private MapEditFac mapEditFac;
	private Ftlayer ftlayer;
	
	public FacQueryTask(Context context,MyMapView map,FacAttribute facAttribute
			,String type, MapEditFac mapEditFac,Ftlayer ftlayer){
		this.context=context;
		this.map=map;
		this.facAttribute=facAttribute;
		this.type = type;
		this.mapEditFac=mapEditFac;
		this.ftlayer = ftlayer;
		mapParam = map.getMapParam();
		
		Envelope env0=new Envelope();
		map.getExtent().queryEnvelope(env0);
		double mh=env0.getHeight();
		double mw=env0.getWidth();//地图宽度
		int ph=map.getHeight();
		int pw=map.getWidth();//地图容器宽度	
		env=new Envelope(facAttribute.getEditPoint(), (mw/(pw*1.0))*40, (mh/(ph*1.0))*40);
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
		progress.setMessage("正在查询附近设施,请稍后……");
		progress.show();
	}
	@Override
	protected FeatureSet doInBackground(String... params) {
		// TODO Auto-generated method stub

		/*Cursor cursor = GddstApplication.ZZDB.mapLayerSelect("identify");
		
		int[] layerId = new int[cursor.getCount()];
		int i = 0;
		while(cursor.moveToNext()){
			layerId[i++] = Integer.parseInt(cursor.getString(
					cursor.getColumnIndex("layerid")));
		}
		IdentifyParameters param = new IdentifyParameters();
		if(layerId.length > 0)
			param.setLayers(layerId);
		
		param.setDPI(98);
		param.setTolerance(15);
		param.setGeometry(point); 
		param.setMapWidth(mapview.getWidth());
		param.setMapHeight(mapview.getHeight());
		param.setLayerMode(context.pmapPrefere.getInt("identifyLayerMode", 0));
		param.setSpatialReference(mapview.getSpatialReference());	
		Envelope env = new Envelope();
		mapview.getExtent().queryEnvelope(env);
		param.setMapExtent(env);
		
		IdentifyResult[] mResult = null;
	    String url;
		if(MyPublicData.INTRANET.equals(context.pPrefere.getString("Net", MyPublicData.OUTERNET))){
			url = context.pmapPrefere.getString("identifyLocal", "");        	
		}else{
			url = context.pmapPrefere.getString("identifyForeign", "");
		}
		IdentifyTask mIdentifyTask = new IdentifyTask(url);
		try {
			mResult = mIdentifyTask.execute(param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		if(mResult!=null&&mResult.length>0){
			identifyResultLists=new HashMap<String, List<IdentifyResult>>();
			for(int j=0;j<mResult.length;j++){
				if(identifyResultLists.containsKey(mResult[i].getLayerName())){
					identifyResultLists.get(mResult[i].getLayerName()).add(mResult[i]);
				}else{
					 List<IdentifyResult> identifyResultList=new ArrayList<IdentifyResult>();
					 identifyResultList.add(mResult[i]);
					 identifyResultLists.put(mResult[i].getLayerName(), identifyResultList);
				}
			}
		}else{
			return null;
		}*/
		/*List<FeatureSet> fsList = new ArrayList<FeatureSet>();
		//循环查询
		for (Mapservice mapservice : mapParam.getBizMap()) {
			int[] layerIds = mapParam.getLayerIds(mapservice);
			if(layerIds!=null){
				for(int i=0;i<layerIds.length;++i){
					Query query = new Query();
					query.setGeometry(this.env);
					query.setOutSpatialReference(map.getSpatialReference());
					query.setReturnGeometry(true);
					query.setOutFields(new String[]{params[0]});
					query.setWhere("1=1");
					QueryTask qTask = new QueryTask(mapservice.getForeign()+"/"+layerIds[i]);
					try {
						FeatureSet fs = qTask.execute(query);
						Graphic[] graphic = fs.getGraphics();
						if(graphic!=null&&graphic.length>0){
							fsList.add(fs);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}*/
		Query query = new Query();
		query.setGeometry(this.env);
		query.setOutSpatialReference(map.getSpatialReference());
		query.setReturnGeometry(true);
		query.setOutFields(new String[]{params[0]});
		query.setWhere("1=1");
		QueryTask qTask = new QueryTask(facAttribute.getQueryUrl());
		try {
			FeatureSet fs = qTask.execute(query);
			Graphic[] graphic = fs.getGraphics();
			if(graphic!=null&&graphic.length>0){
				return fs;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(FeatureSet results) {
		if(results==null){
			Toast.makeText(context, "查询不到设施", 5).show();
		}else{
			new FacEditDialog(context, facAttribute, type,ftlayer,mapEditFac,results).showAtLocation(map, Gravity.CENTER, 0, 0);
		}
		if(progress!=null){
			progress.dismiss();
		}
	}
}
