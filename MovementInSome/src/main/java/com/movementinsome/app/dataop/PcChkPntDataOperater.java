package com.movementinsome.app.dataop;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.esri.core.geometry.Polyline;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.remind.InsPlanManage;
import com.movementinsome.app.remind.facPC.PcChkPntPlanOperate;
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.app.remind.road.InsPatrolOnsiteRecordExtVO;
import com.movementinsome.database.vo.InsCheckFacRoad;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.initial.model.Module;
import com.movementinsome.kernel.util.MyDateTools;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PcChkPntDataOperater extends DataOperater implements IDataOperater {

	private PcChkPntPlanOperate chkPntPlanOper;// 签到点
		
	private List<InsCheckFacRoad> checkInsPatrolDataList,hadDoInsPatrolDataList;
	private NearObject nearObject;
	
	
	public PcChkPntDataOperater(Context context,
			InsTablePushTaskVo insTablePushTaskVo, String guid,
			List<Module> lstModule) {
		super(context, insTablePushTaskVo, guid, lstModule);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int initial(final Handler handler) {
		// TODO Auto-generated method stub
		// 获取数据
		chkPntPlanOper = (PcChkPntPlanOperate) InsPlanManage
				.getInstance(InsPlanManage.PLAN_MANAGE_TYPE.PC_CHKPOINT);
		chkPntPlanOper.insBeginDo(context, guid, insTablePushTaskVo.getTaskNum());
		
		checkInsPatrolDataList = chkPntPlanOper
				.getThisTimeData(insTablePushTaskVo.getTaskNum());
		hadDoInsPatrolDataList = chkPntPlanOper.getHadDoDataCache(guid);
		nearObject = chkPntPlanOper.getCurTaskChkPnt();
		
		return checkInsPatrolDataList==null?0:checkInsPatrolDataList.size();
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		if (chkPntPlanOper != null) {
			chkPntPlanOper.insEndDo(guid);
		}
	}

	@Override
	public List<Map<String, Object>> getHadDoDataList() {
		// TODO Auto-generated method stub
		/*return getListData(hadDoInsPatrolDataList, "InsPatrolDataVO"),
				null, insTablePushTaskVo);*/
		return getListData(hadDoInsPatrolDataList, "InsCheckFacRoad");
	}

	@Override
	public List<Map<String, Object>> getWouldDoDataList() {
		// TODO Auto-generated method stub
		return getListData(checkInsPatrolDataList, "InsCheckFacRoad");
	}

	@Override
	public void refreshThisTimeData() {
		// TODO Auto-generated method stub
		checkInsPatrolDataList =chkPntPlanOper.getThisTimeData(insTablePushTaskVo.getTaskNum());
		hadDoInsPatrolDataList = chkPntPlanOper.getHadDoDataCache(guid);
		nearObject = chkPntPlanOper.getCurTaskChkPnt();
	}

	
	@Override
	public NearObject getCurentNearObj() {
		// TODO Auto-generated method stub
		nearObject =chkPntPlanOper.getCurTaskChkPnt();
		if (null != hadDoInsPatrolDataList){
			
			for(InsCheckFacRoad dv:hadDoInsPatrolDataList){
				if (nearObject.getObjNum().equals(dv.getFacNum())){
					return null;
				}
			}
		}
		return nearObject;		
	}

	@Override
	public InsPatrolOnsiteRecordExtVO getCurTaskLuDuan() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Module> getLinkModule() {
		// TODO Auto-generated method stub
		List<Module> mList = new ArrayList<Module>();
		for (Module m : lstModule) {
			if (Constant.BIZ_VALUECOVERT.equals(m.getId())) {
				mList.add(m);
			}
		}
		return mList;
	}

	@Override
	public HashMap<String, String> getTransParams() {
		// TODO Auto-generated method stub
		HashMap<String, String> params0 = new HashMap<String, String>();
		List<InsCheckFacRoad> insCheckFacRoadList = null;
		try {
			Dao<InsCheckFacRoad, Long> insCheckFacRoadDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsCheckFacRoad.class);
			insCheckFacRoadList = insCheckFacRoadDao.queryForEq("id",
					nearObject.getObjId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (insCheckFacRoadList != null && insCheckFacRoadList.size() > 0) {
			params0.put("sections", insCheckFacRoadList.get(0).getRoadName());
			params0.put("sectionsNum", insCheckFacRoadList.get(0)
					.getSerialNumber());
		}
		params0.put("irrDate", MyDateTools.date2String(new Date()));
		params0.put("irrName", AppContext.getInstance().getCurUser()
				.getUserAlias());
		if (AppContext.getInstance().getCurLocation() != null) {
			params0.put("irrPosition", AppContext.getInstance()
					.getCurLocation().getAddr());
			params0.put("reportedCoordinate", AppContext.getInstance()
					.getCurLocation().getCurGpsPosition());
		}
		return params0;
	}

	@Override
	public DataType dataType() {
		// TODO Auto-generated method stub
		return DataType.Pt;
	}

	@Override
	public void callback(Intent data) {
		// TODO Auto-generated method stub
		String taskNum = data.getStringExtra("taskNum");// 任务编号
		String pid= data.getStringExtra("pid");
		try {
			Dao<InsCheckFacRoad, Long> insCheckFacRoadDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsCheckFacRoad.class);
			List<InsCheckFacRoad> InsCheckFacRoadList = insCheckFacRoadDao.queryForEq("id", pid);
			if(InsCheckFacRoadList!=null&&InsCheckFacRoadList.size()>0){
				InsCheckFacRoadList.get(0).setState(Long.valueOf("3"));
				insCheckFacRoadDao.update(InsCheckFacRoadList.get(0));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<String> getThisTimeDataWtsGeo() {
		// TODO Auto-generated method stub
		List<String> geos = new ArrayList();
		try{
			for(InsCheckFacRoad dv : chkPntPlanOper.getThisTimeData(insTablePushTaskVo.getTaskNum())){
				geos.add(dv.getShapeStr());
			}
		}catch(NullPointerException ex){
			;
		}
		return geos;
	}

	@Override
	public List<String> getHadDoDataGeo() {
		// TODO Auto-generated method stub
		List<String> geos = new ArrayList();
		try{
			for(InsCheckFacRoad dv : chkPntPlanOper.getHadDoDataCache(guid)){
				geos.add(dv.getShapeStr());
			} 
		}catch(NullPointerException ex){
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

	@Override
	public void speObjOp(InsPatrolOnsiteRecordExtVO object,int state,String memo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insGoNo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Polyline> getTaskPreTrace() {
		// TODO Auto-generated method stub
		return null;
	}
}
