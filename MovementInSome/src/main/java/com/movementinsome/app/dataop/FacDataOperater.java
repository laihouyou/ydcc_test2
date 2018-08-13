package com.movementinsome.app.dataop;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.esri.core.geometry.Polyline;
import com.movementinsome.AppContext;
import com.movementinsome.app.remind.InsPlanManage;
import com.movementinsome.app.remind.PlanOperate.DataSetSupervioer;
import com.movementinsome.app.remind.fac.FacPlanOperate;
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.app.remind.road.InsPatrolOnsiteRecordExtVO;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.initial.model.Module;
import com.j256.ormlite.dao.Dao;

public class FacDataOperater extends DataOperater implements IDataOperater {
	public FacDataOperater(Context context,
			InsTablePushTaskVo insTablePushTaskVo, String guid,
			List<Module> lstModule) {
		super(context, insTablePushTaskVo, guid, lstModule);
		// TODO Auto-generated constructor stub
	}

	private FacPlanOperate facPlanOperate;// 设施
	// 需要巡检列表
	private List<InsPatrolDataVO> valveList;
	// 已巡列表
	private List<InsPatrolDataVO> valveHadDoDataCache;
	// 未巡列表
	private List<InsPatrolDataVO> valveNotDoneDataCache;
	// 搜索列表
	private List<InsPatrolDataVO> valveSearchDataCache;
	private NearObject nearObject;
	// 当前查询的编号
	private String num;
	
	@Override
	public int initial(final Handler handler) {
		// TODO Auto-generated method stub
		// 设施
		facPlanOperate = (FacPlanOperate) InsPlanManage
				.getInstance(InsPlanManage.PLAN_MANAGE_TYPE.FAC);// 设施
		valveSearchDataCache = new ArrayList<InsPatrolDataVO>();
		refreshThisTimeData();
		/*valveList = facPlanOperate.getThisTimeData(insTablePushTaskVo
				.getTaskNum());
		valveHadDoDataCache = facPlanOperate.getHadDoData(insTablePushTaskVo
				.getTaskNum());*/
		/*nearObject = facPlanOperate.getNearSite();
		facPlanOperate
				.insBeginDo(context, guid, insTablePushTaskVo.getTaskNum());*/
		facPlanOperate.setDataSetChangeListener(new DataSetSupervioer() {
			public void onChange() {
				// 填写一些前置操作，如更新数据
				handler.sendEmptyMessage(1);
				// DisplayModel(); // 重新绘制
				// 填写一些后置操作，如更新状态
			}
		});
		return valveNotDoneDataCache==null?0:valveNotDoneDataCache.size();
	}
	
	@Override
	public void end() {
		// TODO Auto-generated method stub
		if (facPlanOperate != null) {
			facPlanOperate.insEndDo(guid);
		}
	}

	@Override
	public List<Map<String, Object>> getHadDoDataList() {
		// TODO Auto-generated method stub
		return getListData(valveHadDoDataCache, "InsPatrolDataVO");
	}

	@Override
	public List<Map<String, Object>> getWouldDoDataList() {
		// TODO Auto-generated method stub
		return getListData(valveNotDoneDataCache, "InsPatrolDataVO");
	}

	@Override
	public void refreshThisTimeData() {
		// TODO Auto-generated method stub
		valveList = facPlanOperate.getThisTimeData(insTablePushTaskVo
				.getTaskNum());
		if(valveHadDoDataCache == null){
			valveHadDoDataCache = new ArrayList<InsPatrolDataVO>();
		}else{
			valveHadDoDataCache.removeAll(valveHadDoDataCache);
		}
		if(valveNotDoneDataCache == null){
			valveNotDoneDataCache = new ArrayList<InsPatrolDataVO>();
		}else{
			valveNotDoneDataCache.removeAll(valveNotDoneDataCache);
		}
		valveHadDoDataCache = facPlanOperate.getHadDoData(insTablePushTaskVo.getTaskNum());
		for(InsPatrolDataVO insPatrolDataVO : valveList){
			if(insPatrolDataVO.getInsCount()!=null&&insPatrolDataVO.getInsCount()==-1){
				//valveHadDoDataCache.add(insPatrolDataVO);
			}else{
				valveNotDoneDataCache.add(insPatrolDataVO);
			}
			if(num!=null&&valveSearchDataCache.size()>0){
				searchData(num);
			}
		}
		// valveHadDoDataCache = facPlanOperate.getHadDoDataCache(guid);
	}

	@Override
	public NearObject getCurentNearObj() {
		// TODO Auto-generated method stub
		return facPlanOperate.getNearSite();
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
		List<InsPatrolDataVO> vList = null;
		try {
			Dao<InsPatrolDataVO, Long> insPatrolDataVODao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsPatrolDataVO.class);
			vList = insPatrolDataVODao.queryForEq("guid",
					nearObject.getObjId());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (vList != null && vList.size() > 0) {
			for (Module m : lstModule) {
				String mId = vList.get(0).getAndroidForm();
				if (mId != null) {
					String[] mIds = mId.split(",");
					for (int i = 0; i < mIds.length; ++i) {
						if (mIds[i].equals(m.getId())) {
							mdList.add(m);
						}
					}
				}
			}

		}
		return mdList;
	}

	@Override
	public HashMap<String, String> getTransParams() {
		// TODO Auto-generated method stub
		HashMap<String, String> params = new HashMap<String, String>();
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
		String taskNum = data.getStringExtra("taskNum");// 任务编号
		String taskCategory = data.getStringExtra("taskCategory");// 表单类型
		String tableName = data.getStringExtra("tableName");// 表单在数据库中表名
		String pid= data.getStringExtra("pid");
		String uploadTable= data.getStringExtra("uploadTable");
		int opState = data.getIntExtra("opState", 0);
		try {
			HashMap<String, String> oParams = (HashMap<String, String>) data
					.getSerializableExtra("oParams");

			if (opState == 1&&uploadTable!=null&&"1".equals(uploadTable)) {
				try {
					Dao<InsPatrolDataVO, Long> patrolDataDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsPatrolDataVO.class);
					List<InsPatrolDataVO> insPatrolDataVOList = patrolDataDao.queryForEq("guid", pid);
					if(insPatrolDataVOList!=null&&insPatrolDataVOList.size()>0){
						Long frequencyNumber=insPatrolDataVOList.get(0).getFrequencyNumber();
						if(frequencyNumber==null){
							frequencyNumber=(long) 0;
						}
						Long insCount =insPatrolDataVOList.get(0).getInsCount();
						if(insCount==null){
							insCount=(long) 0;
						}
						if(insCount>=0&&insCount<frequencyNumber){
							++insCount;
							if(insCount==frequencyNumber){
								insCount=(long) -1;
							}
							insPatrolDataVOList.get(0).setInsCount(insCount);
							insPatrolDataVOList.get(0).setLastInsDateStr(getCurDate());
							patrolDataDao.update(insPatrolDataVOList.get(0));
						}
					}					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
			for(InsPatrolDataVO dv :  facPlanOperate.getThisTimeData(insTablePushTaskVo
					.getTaskNum())){
				geos.add(dv.getGeometryStr());
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
			for(InsPatrolDataVO dv :  facPlanOperate.getHadDoData(insTablePushTaskVo
					.getTaskNum())){
				geos.add(dv.getGeometryStr());
			} 
		}catch(NullPointerException ex){
			;
		}
		return geos;
	}

	@Override
	public void searchData(String num) {
		// TODO Auto-generated method stub
		this.num = num;
		valveSearchDataCache.removeAll(valveSearchDataCache);
		for(InsPatrolDataVO insPatrolDataVO : valveList){
			if(num.equals(insPatrolDataVO.getNum())){
				valveSearchDataCache.add(insPatrolDataVO);
			}
		}
		if(valveSearchDataCache.size()==0){
			Toast.makeText(context, "搜索设施为空", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public List<Map<String, Object>> getSearchDataList() {
		// TODO Auto-generated method stub
		return getListData(valveSearchDataCache, "InsPatrolDataVO");
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
	public void speObjOp(InsPatrolOnsiteRecordExtVO object, int state,String memo) {
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
