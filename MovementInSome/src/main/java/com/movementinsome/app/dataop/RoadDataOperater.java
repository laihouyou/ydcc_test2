package com.movementinsome.app.dataop;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.esri.core.geometry.Polyline;
import com.movementinsome.app.remind.InsPlanManage;
import com.movementinsome.app.remind.PlanOperate.DataSetSupervioer;
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.app.remind.road.InsPatrolOnsiteRecordExtVO;
import com.movementinsome.app.remind.road.RoadPlanOperate;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.initial.model.Module;

public class RoadDataOperater  extends DataOperater implements IDataOperater {

	public RoadDataOperater(Context context,
			InsTablePushTaskVo insTablePushTaskVo, String guid,
			List<Module> lstModule) {
		super(context, insTablePushTaskVo, guid, lstModule);
		// TODO Auto-generated constructor stub
	}

	//private List<Module> lstModule;// 所有表单配置
	//private List<Module> taskModule;// 当前任务表单配置
	
	private RoadPlanOperate roadPlanOper;// 路段
	//private ChkPntPlanOperate chkPntPlanOper;// 签到点
	// 未巡数据
	private List<InsPatrolDataVO> pdList;
	// 已巡数据
	private List<InsPatrolDataVO> hadDoInsPatrolDataList;
	// 签到点
	//private List<InsPatrolDataVO> checkInsPatrolDataList;
		
	//private InsPatrolOnsiteRecordExtVO insPatrolOnsiteRecordVO = null; // 当前路段
	//private InsPatrolOnsiteRecordExtVO preInsPatrolOnsiteRecordVO = null;// 上一巡查路段
		
	//private NearObject nearObject;
	
	
	@Override
	public int initial(final Handler handler) {
		// TODO Auto-generated method stub
		/*show_work_im.setVisibility(View.GONE);
		show_work_arrive.setVisibility(View.VISIBLE);*/
		// 路段
		roadPlanOper = (RoadPlanOperate) InsPlanManage
				.getInstance(InsPlanManage.PLAN_MANAGE_TYPE.ROAD);
		
		roadPlanOper.insBeginDo(context, guid, insTablePushTaskVo.getTaskNum());
		
		pdList = roadPlanOper.getThisTimeData(insTablePushTaskVo.getTaskNum());
		hadDoInsPatrolDataList = roadPlanOper.getHadDoDataCache(guid);
		//insPatrolOnsiteRecordVO = roadPlanOper.getCurTaskLuDuan();

		

		roadPlanOper.setDataSetChangeListener(new DataSetSupervioer() {
			public void onChange() {
				// 填写一些前置操作，如更新数据
				handler.sendEmptyMessage(1);
				// DisplayModel(); // 重新绘制
				// 填写一些后置操作，如更新状态
			}
		});
		return pdList==null?0:pdList.size();
	}
	
	@Override
	public void end() {
		// TODO Auto-generated method stub
		if (roadPlanOper != null) {
			roadPlanOper.insEndDo(guid);
		}
	}

	@Override
	public List<Map<String, Object>> getHadDoDataList() {
		// TODO Auto-generated method stub
		return getListData(hadDoInsPatrolDataList, "InsPatrolDataVO");
	}

	@Override
	public List<Map<String, Object>> getWouldDoDataList() {
		// TODO Auto-generated method stub
		return getListData(pdList, "InsPatrolDataVO");
	}

	@Override
	public void refreshThisTimeData() {
		// TODO Auto-generated method stub
		pdList = roadPlanOper.getThisTimeData(insTablePushTaskVo
				.getTaskNum());
		hadDoInsPatrolDataList = roadPlanOper.getHadDoDataCache(guid);
	}

	@Override
	public NearObject getCurentNearObj() {
		// TODO Auto-generated method stub
		if (null != roadPlanOper.getCurTaskLuDuan()){
			return roadPlanOper.getCurTaskLuDuan().getCurNearObj();
		}else{
			return null;
		}
	}

	@Override
	public InsPatrolOnsiteRecordExtVO getCurTaskLuDuan() {
		// TODO Auto-generated method stub
		return roadPlanOper.getCurTaskLuDuan();
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
		
	}

	@Override
	public List<String> getThisTimeDataWtsGeo() {
		// TODO Auto-generated method stub
		List<String> geos = new ArrayList();
		try{
			for(InsPatrolDataVO dv :  roadPlanOper.getThisTimeData(insTablePushTaskVo.getTaskNum())){
				geos.add(dv.getGeometryStr());
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
			for(InsPatrolDataVO dv :  roadPlanOper.getHadDoDataCache(guid)){
				geos.add(dv.getGeometryStr());
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
			for(InsPatrolDataVO dv :  roadPlanOper.getWaitForDoDataCache(guid)){
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
		roadPlanOper.pause(pauseRoad);
	}

	@Override
	public void goNo(InsPatrolOnsiteRecordExtVO pauseRoad) {
		// TODO Auto-generated method stub
		pauseRoad.setWarnState("1");
		roadPlanOper.goNo(pauseRoad);
	}*/
	
	@Override
	public void speObjOp(InsPatrolOnsiteRecordExtVO object,int state,	String memo) {
		// TODO Auto-generated method stub
		roadPlanOper.curObjOp(object,state,memo);
	}

	@Override
	public void insPause() {
		// TODO Auto-generated method stub
		roadPlanOper.insPause();
	}

	@Override
	public void insGoNo() {
		// TODO Auto-generated method stub
		roadPlanOper.insGoNo();
	}

}
