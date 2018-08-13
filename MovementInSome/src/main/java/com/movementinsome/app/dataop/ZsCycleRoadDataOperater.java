package com.movementinsome.app.dataop;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.esri.core.geometry.Polyline;
import com.movementinsome.AppContext;
import com.movementinsome.app.remind.InsPlanManage;
import com.movementinsome.app.remind.PlanOperate.DataSetSupervioer;
import com.movementinsome.app.remind.area.PatrolAreaOperate;
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.app.remind.road.InsPatrolOnsiteRecordExtVO;
import com.movementinsome.app.remind.roadZS.RoadPlanOperate;
import com.movementinsome.database.vo.InsPatrolAreaData;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.initial.model.Module;
import com.j256.ormlite.dao.Dao;

public class ZsCycleRoadDataOperater extends DataOperater implements IDataOperater{

	private String guid2;
	public ZsCycleRoadDataOperater(Context context,
			InsTablePushTaskVo insTablePushTaskVo, String guid,
			List<Module> lstModule) {
		super(context, insTablePushTaskVo, guid, lstModule);
		// TODO Auto-generated constructor stub
		guid2 = UUID.randomUUID().toString();
	}
	private RoadPlanOperate roadPlanOper;// 路段
	// 未巡数据
	private List<InsPatrolDataVO> pdList;
	// 已巡数据
	private List<InsPatrolDataVO> hadDoInsPatrolDataList;
	
	private List<InsPatrolAreaData> insPatrolAreaDataList;
	private PatrolAreaOperate patrolAreaOperate;
	
	
	@Override
	public int initial(final Handler handler) {
		// TODO Auto-generated method stub
		// 路段
		roadPlanOper = (RoadPlanOperate) InsPlanManage
				.getInstance(InsPlanManage.PLAN_MANAGE_TYPE.ZS_ROAD);
		patrolAreaOperate = (PatrolAreaOperate) InsPlanManage
				.getInstance(InsPlanManage.PLAN_MANAGE_TYPE.ZS_PATROL_AREA);
		
		roadPlanOper.insBeginDo(context, guid, insTablePushTaskVo.getTaskNum());
		
		pdList = roadPlanOper.getThisTimeData(insTablePushTaskVo.getTaskNum());
		hadDoInsPatrolDataList = roadPlanOper.getHadDoDataCache(guid);
		
		roadPlanOper.setDataSetChangeListener(new DataSetSupervioer() {
			public void onChange() {
				// 填写一些前置操作，如更新数据
				handler.sendEmptyMessage(1);
				// DisplayModel(); // 重新绘制
				// 填写一些后置操作，如更新状态
			}
		});
		patrolAreaOperate.setDataSetChangeListener(new DataSetSupervioer() {
			public void onChange() {
				// 填写一些前置操作，如更新数据
				handler.sendEmptyMessage(1);
				// DisplayModel(); // 重新绘制
				// 填写一些后置操作，如更新状态
			}
		});
		patrolAreaOperate.insBeginDo(context, guid2,
				insTablePushTaskVo.getTaskNum());
		insPatrolAreaDataList = patrolAreaOperate
				.getThisTimeData(insTablePushTaskVo.getTaskNum());
		if(insPatrolAreaDataList!=null){
			for(int i=0;i<insPatrolAreaDataList.size();++i){
				try {
					InsPatrolAreaData insPatrolAreaData = insPatrolAreaDataList.get(i);
					Dao<InsPatrolDataVO, Long> patrolDataDao = AppContext
							.getInstance().getAppDbHelper()
							.getDao(InsPatrolDataVO.class);
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("areaId",insPatrolAreaData.getAreaId());
					m.put("insCount", -1);
					List<InsPatrolDataVO> pdList =patrolDataDao.queryForFieldValues(m);
					if(pdList!=null&&pdList.size()>0){
						Dao<InsPatrolAreaData, Long> insPatrolAreaDataDao = AppContext
								.getInstance().getAppDbHelper()
								.getDao(InsPatrolAreaData.class);
						insPatrolAreaData.setLastInsDateStr(getCurDate());
						insPatrolAreaDataDao.update(insPatrolAreaData);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return pdList==null?0:pdList.size();
	}
	
	@Override
	public void end() {
		// TODO Auto-generated method stub
		if (roadPlanOper != null) {
			roadPlanOper.insEndDo(guid);
		}
		if (patrolAreaOperate != null) {
			patrolAreaOperate.insEndDo(guid2);
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
		List<Map<String, Object>> dataList;
		dataList = new ArrayList<Map<String, Object>>();
		if(insPatrolAreaDataList!=null){
			for(InsPatrolAreaData insPatrolAreaData : insPatrolAreaDataList){
				Map<String, Object> d = new HashMap<String, Object>();
				d.put("InsPatrolAreaData", insPatrolAreaData);
				dataList.add(d);
				if(pdList!=null){
					for(InsPatrolDataVO insPatrolDataVO : pdList){
						if(insPatrolDataVO.getAreaId()!=null){
							if(insPatrolDataVO.getAreaId().longValue() == insPatrolAreaData.getAreaId().longValue()){
								d = new HashMap<String, Object>();
								d.put("InsPatrolDataVO", insPatrolDataVO);
								dataList.add(d);
							}
						}
					}
				}
			}
		}
		return dataList;
	}

	@Override
	public void refreshThisTimeData() {
		// TODO Auto-generated method stub
		pdList = roadPlanOper.getThisTimeData(insTablePushTaskVo
				.getTaskNum());
		insPatrolAreaDataList = patrolAreaOperate
				.getThisTimeData(insTablePushTaskVo.getTaskNum());
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
