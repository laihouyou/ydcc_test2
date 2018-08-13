package com.movementinsome.app.dataop;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.esri.core.geometry.Polyline;
import com.movementinsome.AppContext;
import com.movementinsome.app.remind.InsPlanManage;
import com.movementinsome.app.remind.PlanOperate.DataSetSupervioer;
import com.movementinsome.app.remind.fireplug.FireplugPlanOperate;
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.app.remind.road.InsPatrolOnsiteRecordExtVO;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.initial.model.Module;
import com.j256.ormlite.dao.Dao;

public class FireplugDataOperater  extends DataOperater  implements IDataOperater {

	public FireplugDataOperater(Context context,
			InsTablePushTaskVo insTablePushTaskVo, String guid,
			List<Module> lstModule) {
		super(context, insTablePushTaskVo, guid, lstModule);
		// TODO Auto-generated constructor stub
	}

	private FireplugPlanOperate fireplugPlanOperate;// 消防栓
	// 消防栓
	private List<InsPatrolDataVO> fireplugList;
	private List<InsPatrolDataVO> fireplugHadDoDataCache;
	private List<InsPatrolDataVO> valveNearDataCache;
	
	private NearObject nearObject;
	
	
	@Override
	public int initial(final Handler handler) {
		// TODO Auto-generated method stub
		// 设施
		fireplugPlanOperate = (FireplugPlanOperate) InsPlanManage
				.getInstance(InsPlanManage.PLAN_MANAGE_TYPE.FIREPLUG);// 阀门
		fireplugPlanOperate.setDataSetChangeListener(new DataSetSupervioer() {
			public void onChange() {
				// 填写一些前置操作，如更新数据
				handler.sendEmptyMessage(1);
				// DisplayModel(); // 重新绘制
				// 填写一些后置操作，如更新状态
			}
		});
		fireplugPlanOperate.insBeginDo(context, guid,
				insTablePushTaskVo.getTaskNum());
		
		fireplugList = fireplugPlanOperate.getThisTimeData(insTablePushTaskVo
				.getTaskNum());
		fireplugHadDoDataCache = fireplugPlanOperate.getHadDoData(insTablePushTaskVo
				.getTaskNum());
		nearObject = fireplugPlanOperate.getNearSite();
		valveNearDataCache = new ArrayList<InsPatrolDataVO>();
		//updateWorkQdd();
		
		
		return fireplugList==null?0:fireplugList.size();
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		if (fireplugPlanOperate != null) {
			fireplugPlanOperate.insEndDo(guid);
		}
	}

	@Override
	public List<Map<String, Object>> getHadDoDataList() {
		// TODO Auto-generated method stub
		return getListData(fireplugHadDoDataCache, "InsPatrolDataVO");
	}

	@Override
	public List<Map<String, Object>> getWouldDoDataList() {
		// TODO Auto-generated method stub
		return getListData(fireplugList, "InsPatrolDataVO");
	}

	@Override
	public void refreshThisTimeData() {
		// TODO Auto-generated method stub
		fireplugList = fireplugPlanOperate.getThisTimeData(insTablePushTaskVo
				.getTaskNum());
		fireplugHadDoDataCache = fireplugPlanOperate.getHadDoDataCache(guid);
		nearObject = fireplugPlanOperate.getNearSite();
	}

	@Override
	public NearObject getCurentNearObj() {
		// TODO Auto-generated method stub
		nearObject = fireplugPlanOperate.getNearSite();
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

		};
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
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public List<String> getThisTimeDataWtsGeo() {
		// TODO Auto-generated method stub
		List<String> geos = new ArrayList();
		try{
			for(InsPatrolDataVO dv : fireplugPlanOperate.getThisTimeData(insTablePushTaskVo.getTaskNum())){
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
			for(InsPatrolDataVO dv : fireplugPlanOperate.getHadDoData(insTablePushTaskVo.getTaskNum())){
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
		
	}

	@Override
	public List<Map<String, Object>> getSearchDataList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getNearbyDataList() {
		// TODO Auto-generated method stub
		valveNearDataCache.removeAll(valveNearDataCache);
		if(nearObject!=null&&fireplugList!=null){
			for(int i =0 ;i<fireplugList.size();++i){
				if(nearObject.getObjId().equals(fireplugList.get(i).getGuid())){
					valveNearDataCache.add(fireplugList.get(i));
				}
			}
		}
		return getListData(valveNearDataCache, "InsPatrolDataVO");
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
