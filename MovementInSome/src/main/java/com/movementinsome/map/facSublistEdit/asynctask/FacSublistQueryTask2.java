package com.movementinsome.map.facSublistEdit.asynctask;

import java.util.Map;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.ags.query.QueryTask;
import com.esri.core.tasks.ags.query.RelationshipQuery;
import com.movementinsome.kernel.initial.model.Ftlayer;
import com.movementinsome.map.facSublistEdit.dialog.FacSublistEditDialog;
import com.movementinsome.map.facedit.vo.FacAttribute;
import com.movementinsome.map.view.MyMapView;

public class FacSublistQueryTask2 extends
		AsyncTask<String, Void, Map<Integer, FeatureSet>> {

	private ProgressDialog progress;
	private Context context;
	private FacAttribute facAttribute;
	private Ftlayer ftlayer;
	private FacSublistEditDialog facSublistEditDialog;
	private Graphic graphicF;
	private MyMapView map;

	public FacSublistQueryTask2(Context context, MyMapView map,
			FacAttribute facAttribute, Ftlayer ftlayer, Graphic graphicF) {
		this.context = context;
		this.facAttribute = facAttribute;
		this.ftlayer = ftlayer;
		this.graphicF = graphicF;
		this.map = map;
	}

	@SuppressWarnings("static-access")
	@Override
	protected void onPreExecute() {
		if (progress == null) {
			progress = new ProgressDialog(context);
			progress.setIndeterminate(true);

		}
		progress.setMessage("正在查询子表,请稍后……");
		progress.show();
	}

	@Override
	protected Map<Integer, FeatureSet> doInBackground(String... params) {
		// TODO Auto-generated method stub
		Map<Integer, FeatureSet> fss = null;
		@SuppressWarnings("deprecation")
		QueryTask qTask = new QueryTask(facAttribute.getQueryUrl());
		try {
			int[] OBJECTID = new int[] { (Integer) graphicF.getAttributes()
					.get("OBJECTID") };
			RelationshipQuery relationshipQuery = new RelationshipQuery();
			relationshipQuery.setObjectIds(OBJECTID);
			relationshipQuery.setRelationshipId(ftlayer.getRelationshipId());
			relationshipQuery.setOutFields(new String[] { "*" });
			fss = qTask.executeRelationshipQuery(relationshipQuery);
			return fss;

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
		if (facSublistEditDialog == null) {
			facSublistEditDialog = new FacSublistEditDialog(context, map,
					result, graphicF, facAttribute, ftlayer);
			facSublistEditDialog.showAtLocation(map, Gravity.CENTER, 0, 0);
		} else {
			facSublistEditDialog.updateList(result);
			facSublistEditDialog.getFacSublistAdapter().notifyDataSetChanged();
			if (facSublistEditDialog.isShow()) {
				facSublistEditDialog.blak();
			}
		}
	}

	public FacSublistEditDialog getFacSublistEditDialog() {
		return facSublistEditDialog;
	}

	public void setFacSublistEditDialog(
			FacSublistEditDialog facSublistEditDialog) {
		this.facSublistEditDialog = facSublistEditDialog;
	}

}
