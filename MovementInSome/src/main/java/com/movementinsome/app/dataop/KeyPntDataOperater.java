package com.movementinsome.app.dataop;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.esri.core.geometry.Polyline;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.remind.InsPlanManage;
import com.movementinsome.app.remind.PlanOperate.DataSetSupervioer;
import com.movementinsome.app.remind.keypnt.KeypntPlanOperate;
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.app.remind.road.InsPatrolOnsiteRecordExtVO;
import com.movementinsome.database.vo.InsKeyPointPatrolData;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.initial.model.Module;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyPntDataOperater  extends DataOperater implements IDataOperater {

	public KeyPntDataOperater(Context context,
			InsTablePushTaskVo insTablePushTaskVo, String guid,
			List<Module> lstModule) {
		super(context, insTablePushTaskVo, guid, lstModule);
		// TODO Auto-generated constructor stub
	}

	private KeypntPlanOperate keypntPlanOperate;// 关键点
	private List<InsKeyPointPatrolData> kpList;
	private List<InsKeyPointPatrolData> kpHadDoDataCache;
	private NearObject nearObject;
	

	@Override
	public int initial(final Handler handler) {
		// TODO Auto-generated method stub
		// 获取数据
		keypntPlanOperate = (KeypntPlanOperate) InsPlanManage
				.getInstance(InsPlanManage.PLAN_MANAGE_TYPE.KEYPOINT);
		keypntPlanOperate.insBeginDo(context, guid,
				insTablePushTaskVo.getTaskNum());
		
		kpList = keypntPlanOperate.getThisTimeData(insTablePushTaskVo
				.getTaskNum());
		kpHadDoDataCache = keypntPlanOperate.getHadDoDataCache(guid);
		nearObject = keypntPlanOperate.getNearSite();

		
		keypntPlanOperate.setDataSetChangeListener(new DataSetSupervioer() {
			public void onChange() {
				// 填写一些前置操作，如更新数据
				handler.sendEmptyMessage(1);
				// DisplayModel(); // 重新绘制
				// 填写一些后置操作，如更新状态
			}
		});
		return kpList==null?0:kpList.size();
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		if (keypntPlanOperate != null) {
			keypntPlanOperate.insEndDo(guid);
		}
	}

	@Override
	public List<Map<String, Object>> getHadDoDataList() {
		// TODO Auto-generated method stub
		return getListData(kpHadDoDataCache, "InsKeyPointPatrolData");
	}

	@Override
	public List<Map<String, Object>> getWouldDoDataList() {
		// TODO Auto-generated method stub
		return getListData(kpList, "InsKeyPointPatrolData");
	}

	@Override
	public void refreshThisTimeData() {
		// TODO Auto-generated method stub
		kpList = keypntPlanOperate.getThisTimeData(insTablePushTaskVo
				.getTaskNum());
		kpHadDoDataCache = keypntPlanOperate.getHadDoDataCache(guid);
		nearObject = keypntPlanOperate.getNearSite();
	}

	@Override
	public NearObject getCurentNearObj() {
		// TODO Auto-generated method stub
		return keypntPlanOperate.getNearSite();
	}

	@Override
	public InsPatrolOnsiteRecordExtVO getCurTaskLuDuan() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Module> getLinkModule() {
		// TODO Auto-generated method stub
		List<Module> mdList = new ArrayList<Module>();
		for (Module m : lstModule) {
			if (Constant.BIZ_KEYPOINT.equals(m.getId())) {
				mdList.add(m);
			}
		}
		return mdList;
	}

	@Override
	public HashMap<String, String> getTransParams() {
		// TODO Auto-generated method stub
		List<InsKeyPointPatrolData> kpList = null;
		try {
			Dao<InsKeyPointPatrolData, Long> insKeyPointPatrolDataDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsKeyPointPatrolData.class);
			kpList = insKeyPointPatrolDataDao.queryForEq("id",
					nearObject.getObjId());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String, String> params = new HashMap<String, String>();
		if (kpList != null && kpList.size() > 0) {
			params.put("facName", kpList.get(0).getFacName());
			params.put("facNum", kpList.get(0).getFacNum());
			params.put("coordinate", kpList.get(0).getKpaPosition());
			params.put("manageUnit", kpList.get(0).getManageUnit());
			if (kpList.get(0).getKppdId() != null) {
				params.put("kppdId", kpList.get(0).getKppdId() + "");
			}
		}
		return params;
	}

	@Override
	public DataType dataType() {
		// TODO Auto-generated method stub
		return DataType.Pt;
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
			for(InsKeyPointPatrolData dv :  keypntPlanOperate.getThisTimeData(insTablePushTaskVo.getTaskNum())){
				geos.add(dv.getKpaPosition());
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
			for(InsKeyPointPatrolData dv : keypntPlanOperate.getHadDoDataCache(guid)){
				geos.add(dv.getKpaPosition());
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
	public void speObjOp(InsPatrolOnsiteRecordExtVO object,int state,	String memo) {
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
