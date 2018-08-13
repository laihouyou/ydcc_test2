package com.movementinsome.app.dataop;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureEditResult;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.remind.InsPlanManage;
import com.movementinsome.app.remind.PlanOperate.DataSetSupervioer;
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.app.remind.road.InsPatrolOnsiteRecordExtVO;
import com.movementinsome.app.remind.roadPC.PcRoadPlanOperate;
import com.movementinsome.database.vo.InsCheckFacRoad;
import com.movementinsome.database.vo.InsPatrolAreaData;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.initial.model.Module;
import com.movementinsome.map.facedit.vo.FacAttribute;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PcRoadDataOperater  extends DataOperater implements IDataOperater {

	public PcRoadDataOperater(Context context,
			InsTablePushTaskVo insTablePushTaskVo, String guid,
			List<Module> lstModule) {
		super(context, insTablePushTaskVo, guid, lstModule);
		// TODO Auto-generated constructor stub
	}
	private FacAttribute facAttribute;
	private ArcGISFeatureLayer fl;
	private PcRoadPlanOperate pcroadPlanOper;// 路段
	// 未巡数据
	private List<InsCheckFacRoad> pdList;
	// 已巡数据
	private List<InsCheckFacRoad> hadDoInsPatrolDataList;
	
	private List<InsPatrolAreaData> insPatrolAreaDataList;
	
	
	@Override
	public int initial(final Handler handler) {
		// TODO Auto-generated method stub
		// 路段
		pcroadPlanOper = (PcRoadPlanOperate) InsPlanManage
				.getInstance(InsPlanManage.PLAN_MANAGE_TYPE.PC_ROAD);
		
		pcroadPlanOper.insBeginDo(context, guid, insTablePushTaskVo.getTaskNum());
		
		pdList = pcroadPlanOper.getThisTimeData(insTablePushTaskVo.getTaskNum());
		hadDoInsPatrolDataList = pcroadPlanOper.getHadDoDataCache(guid);
		
		pcroadPlanOper.setDataSetChangeListener(new DataSetSupervioer() {
			public void onChange() {
				// 填写一些前置操作，如更新数据
				handler.sendEmptyMessage(1);
				// DisplayModel(); // 重新绘制
				// 填写一些后置操作，如更新状态
			}
		});
		Dao<InsPatrolAreaData, Long> insPatrolAreaDataDao;
		try {
			insPatrolAreaDataDao = AppContext.getInstance().getAppDbHelper()
			.getDao(InsPatrolAreaData.class);
			insPatrolAreaDataList = insPatrolAreaDataDao.queryForEq(
					"workTaskNum", insTablePushTaskVo.getTaskNum());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pdList==null?0:pdList.size();
	}
	
	@Override
	public void end() {
		// TODO Auto-generated method stub
		if (pcroadPlanOper != null) {
			pcroadPlanOper.insEndDo(guid);
		}
	}

	@Override
	public List<Map<String, Object>> getHadDoDataList() {
		// TODO Auto-generated method stub
		return getListData(hadDoInsPatrolDataList, "InsCheckFacRoad");
	}

	@Override
	public List<Map<String, Object>> getWouldDoDataList() {
		// TODO Auto-generated method stub
		List<Map<String, Object>> dataList;
		dataList = new ArrayList<Map<String, Object>>();
//		if(insPatrolAreaDataList!=null){
//			for(InsPatrolAreaData insPatrolAreaData : insPatrolAreaDataList){
//				Map<String, Object> d = new HashMap<String, Object>();
////				d.put("InsPatrolAreaData", insPatrolAreaData);
////				dataList.add(d);
//				if(pdList!=null){
//					for(InsCheckFacRoad InsCheckFacRoad : pdList){
//						if(InsCheckFacRoad.getAreaId()!=null){
//							if(InsCheckFacRoad.getAreaId().longValue() == insPatrolAreaData.getAreaId().longValue()){
//								d = new HashMap<String, Object>();
//								d.put("InsCheckFacRoad", InsCheckFacRoad);
//								dataList.add(d);
//							}
//						}
//					}
//				}
//			}
//		}
		if(pdList!=null){
			for(InsCheckFacRoad InsCheckFacRoad : pdList){
				Map<String, Object> d = new HashMap<String, Object>();
				d = new HashMap<String, Object>();
				d.put("InsCheckFacRoad", InsCheckFacRoad);
				dataList.add(d);
			}
		}
		return dataList;
	}

	@Override
	public void refreshThisTimeData() {
		// TODO Auto-generated method stub
		pdList = pcroadPlanOper.getThisTimeData(insTablePushTaskVo
				.getTaskNum());
		hadDoInsPatrolDataList = pcroadPlanOper.getHadDoDataCache(guid);
	}

	@Override
	public NearObject getCurentNearObj() {
		// TODO Auto-generated method stub
		if (null != pcroadPlanOper.getCurTaskLuDuan()){
			return pcroadPlanOper.getCurTaskLuDuan().getCurNearObj();
		}else{
			return null;
		}
	}

	@Override
	public InsPatrolOnsiteRecordExtVO getCurTaskLuDuan() {
		// TODO Auto-generated method stub
		return pcroadPlanOper.getCurTaskLuDuan();
	}

	@Override
	public List<Module> getLinkModule() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, String> getTransParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataType dataType() {
		// TODO Auto-generated method stub
		return DataType.Pl;
	}

	@Override
	public void callback(Intent data) {
		// TODO Auto-generated method stub
		String taskNum = data.getStringExtra("taskNum");// 任务编号
		String pid = data.getStringExtra("pid");
		String temp = data.getStringExtra("template");
		String template = temp.replace(".xml", "");
		String[] prid = pid.split(",");
		String pgDate = data.getStringExtra("pgDate");
//		try {
//			Dao<InsCheckFacRoad, Long> insCheckFacRoadDao = AppContext
//					.getInstance().getAppDbHelper()
//					.getDao(InsCheckFacRoad.class);
//			for (int i = 0; i < prid.length; i++) {
//				List<InsCheckFacRoad> InsCheckFacRoadList = insCheckFacRoadDao.queryForEq("id", prid[i]);
//					if(InsCheckFacRoadList!=null&&InsCheckFacRoadList.size()>0){
//						InsCheckFacRoadList.get(0).setState(Long.valueOf("3"));
//						insCheckFacRoadDao.update(InsCheckFacRoadList.get(0));
//					}
//			}
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		
		if(temp.equals("ins_boosterimpact_form_gs.xml")||temp.equals("ins_nearby_value_gs.xml")){
			try {
			Dao<InsCheckFacRoad, Long> insCheckFacRoadDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsCheckFacRoad.class);
			for (int i = 0; i < prid.length; i++) {
				List<InsCheckFacRoad> InsCheckFacRoadList = insCheckFacRoadDao.queryForEq("facNum", prid[i]);
					if(InsCheckFacRoadList!=null&&InsCheckFacRoadList.size()>0){
						InsCheckFacRoadList.get(0).setAndroidForm("提交");
						insCheckFacRoadDao.update(InsCheckFacRoadList.get(0));
					}
			}
			
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else{
			facAttribute = new FacAttribute();
			fl= new ArcGISFeatureLayer("http://123.160.220.35:6080/arcgis/rest/services/GZYHPC/YHPC_NETWORK/FeatureServer/1", ArcGISFeatureLayer.MODE.ONDEMAND);
			facAttribute.setFeaturelayer(fl);
	//		facAttribute.setQueryUrl("http://123.160.220.35:6080/arcgis/rest/services/GZYHPC/YHPC_NETWORK/FeatureServer/1");
			facAttribute.setEditPoint(AppContext.getInstance().getmMapPoint());
			Map<String, Object> attributes= new HashMap<String, Object>();
			int pgJb = 1;
			if(pgDate.equals("地势差")){
				pgJb = 1;
			}else if(pgDate.equals("密集生活区")){
				pgJb = 2;
			}else if(pgDate.equals("地下停车场")){
				pgJb = 3;
			}else if(pgDate.equals("商场与商铺面积")){
				pgJb = 4;
			}else if(pgDate.equals("受骑压情况")){
				pgJb = 5;
			}else if(pgDate.equals("受扰动次级及油气电情况")){
				pgJb = 6;
			}else if(pgDate.equals("处于立交桥附近距离")){
				pgJb = 7;
			}
			attributes.put("POINTTYPE", pgJb);
	//		attributes.put("POINTNUM", null);
			attributes.put("PIPEID", pid);
	//		attributes.put("REMARK", null);
			
			Graphic upDataGraphic=null;
			PictureMarkerSymbol symbol = new PictureMarkerSymbol(
					context.getResources().getDrawable(R.drawable.dot3));
			upDataGraphic=new Graphic(facAttribute.getEditPoint(), symbol, attributes);
	
			Graphic[]upData=new Graphic[]{
					upDataGraphic
			};
			
			wtEdit(upData, null, null); 
		}
	}

	private void wtEdit(final Graphic[] adds,final Graphic[]deletes,final Graphic[]updates) {
		// TODO Auto-generated method stub

		 facAttribute.getFeaturelayer().applyEdits(adds, deletes, updates, new callback());
	}
	
	 //属性编辑后回调
	 class callback implements CallbackListener<FeatureEditResult[][]>{

		@Override
		public void onCallback(FeatureEditResult[][] arg0) {
			// TODO Auto-generated method stub
			for(int i=0;i<arg0.length;++i){
				if(arg0[i]!=null&&arg0[i].length>0){
					if(arg0[i][0]!=null&&arg0[i][0].isSuccess()){
						Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
		
		@Override
		public void onError(Throwable arg0) {
			// TODO Auto-generated method stub
			Toast.makeText(context, "出错", Toast.LENGTH_SHORT).show();
		}
	 }

	@Override
	public List<String> getThisTimeDataWtsGeo() {
		// TODO Auto-generated method stub
		List<String> geos = new ArrayList();
		try{
			for(InsCheckFacRoad dv :  pcroadPlanOper.getThisTimeData(insTablePushTaskVo.getTaskNum())){
				geos.add(dv.getShapeStr());
			} 
		}catch(NullPointerException ex){
			;
		}catch(ConcurrentModificationException ex){
			;
		}
		return geos;
	}

	@Override
	public List<String> getHadDoDataGeo() {
		// TODO Auto-generated method stub
		List<String> geos = new ArrayList();
		try{
			for(InsCheckFacRoad dv :  pcroadPlanOper.getHadDoDataCache(guid)){
				geos.add(dv.getShapeStr());
			} 
		}catch(NullPointerException ex){
			;
		}catch(ConcurrentModificationException ex){
			;
		}
		return geos;
	}
	
	@Override
	public List<Polyline> getTaskPreTrace(){
		List<Polyline> geos = new ArrayList();
		try{
			for(InsCheckFacRoad dv :  pcroadPlanOper.getWaitForDoDataCache(guid)){
				if (null != dv.getRoadPreTrace()&& !dv.getRoadPreTrace().isEmpty()){
					geos.add(dv.getRoadPreTrace());
				}
			} 
		}catch(NullPointerException ex){
			;
		}catch(ConcurrentModificationException ex){
			;
		}
		return geos;
	}

	@Override
	public void searchData(String num) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Map<String, Object>> getSearchDataList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getNearbyDataList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NearObject> getCurentNearObjs() {
		// TODO Auto-generated method stub
		return null;
	}

/*	@Override
	public void pause(InsPatrolOnsiteRecordExtVO pauseRoad) {
		// TODO Auto-generated method stub
		pauseRoad.setWarnState("3");
		pcroadPlanOper.pause(pauseRoad);
	}

	@Override
	public void goNo(InsPatrolOnsiteRecordExtVO pauseRoad) {
		// TODO Auto-generated method stub
		pauseRoad.setWarnState("1");
		pcroadPlanOper.goNo(pauseRoad);
	}*/
	
	@Override
	public void speObjOp(InsPatrolOnsiteRecordExtVO object,int state,	String memo) {
		// TODO Auto-generated method stub
		pcroadPlanOper.curObjOp(object,state,memo);
	}

	@Override
	public void insPause() {
		// TODO Auto-generated method stub
		pcroadPlanOper.insPause();
	}

	@Override
	public void insGoNo() {
		// TODO Auto-generated method stub
		pcroadPlanOper.insGoNo();
	}
}
