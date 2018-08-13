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
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.app.remind.road.InsPatrolOnsiteRecordExtVO;
import com.movementinsome.app.remind.site.SitePlanOperate;
import com.movementinsome.database.vo.InsSiteManage;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.initial.model.Module;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiteDataOperater  extends DataOperater  implements IDataOperater {

	public SiteDataOperater(Context context,
			InsTablePushTaskVo insTablePushTaskVo, String guid,
			List<Module> lstModule) {
		super(context, insTablePushTaskVo, guid, lstModule);
		// TODO Auto-generated constructor stub
	}

	private SitePlanOperate sitePlanOperate;// 工地
	private List<InsSiteManage> smList;
	private List<InsSiteManage> smHadDoDataCache;
	private NearObject nearObject;
	

	@Override
	public int initial(final Handler handler) {
		// TODO Auto-generated method stub
		// 获取数据
		sitePlanOperate = (SitePlanOperate) InsPlanManage
				.getInstance(InsPlanManage.PLAN_MANAGE_TYPE.SITE);// 工地
		sitePlanOperate.setDataSetChangeListener(new DataSetSupervioer() {
			public void onChange() {
				// 填写一些前置操作，如更新数据
				handler.sendEmptyMessage(1);
				// DisplayModel(); // 重新绘制
				// 填写一些后置操作，如更新状态
			}
		});
		sitePlanOperate.insBeginDo(context, guid, insTablePushTaskVo.getTaskNum());
		
		smList = sitePlanOperate.getThisTimeData(insTablePushTaskVo
				.getTaskNum());
		smHadDoDataCache = sitePlanOperate.getHadDoData(insTablePushTaskVo
				.getTaskNum());
		nearObject = sitePlanOperate.getNearSite();
		//updateWorkQdd();
		
		
		return smList==null?0:smList.size();
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		if (sitePlanOperate != null) {
			sitePlanOperate.insEndDo(guid);
		}
	}

	@Override
	public List<Map<String, Object>> getHadDoDataList() {
		// TODO Auto-generated method stub
		return getListData(smHadDoDataCache, "InsSiteManage");
	}

	@Override
	public List<Map<String, Object>> getWouldDoDataList() {
		// TODO Auto-generated method stub
		return getListData(smList, "InsSiteManage");
	}

	@Override
	public void refreshThisTimeData() {
		// TODO Auto-generated method stub
		smList = sitePlanOperate.getThisTimeData(insTablePushTaskVo
				.getTaskNum());
		smHadDoDataCache = sitePlanOperate.getHadDoDataCache2(insTablePushTaskVo.getTaskNum());
		nearObject = sitePlanOperate.getNearSite();
	}

	@Override
	public NearObject getCurentNearObj() {
		// TODO Auto-generated method stub
		return sitePlanOperate.getNearSite();
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
		List<InsSiteManage> insSiteManageList = null;
		
		try {
			Dao<InsSiteManage, Long> insSiteManageDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsSiteManage.class);
			insSiteManageList = insSiteManageDao.queryForEq("id",
					nearObject.getObjId());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (insSiteManageList != null && insSiteManageList.size() > 0) {
			if ("1".equals(insSiteManageList.get(0).getIsFirst())) {
				for (Module m : lstModule) {
					String mId = insSiteManageList.get(0).getAndroidForm();
					if (mId != null) {
						String[] mIds = mId.split(",");
						for (int i = 0; i < mIds.length; ++i) {
							if (mIds[i].equals(m.getId())) {
								mdList.add(m);
							}
						}
					}
				}
			} else {
				for (Module m : lstModule) {
					if (Constant.BIZ_CONSTRUCTION.equals(m.getId())) {
						mdList.add(m);
					}
				}
			}
		}
		
		return mdList;
	}

	@Override
	public HashMap<String, String> getTransParams() {
		// TODO Auto-generated method stub
		HashMap<String, String> params2 = new HashMap<String, String>();
		
		List<InsSiteManage> insSiteManageList = null;
		
		try {
			Dao<InsSiteManage, Long> insSiteManageDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsSiteManage.class);
			insSiteManageList = insSiteManageDao.queryForEq("id",
					nearObject.getObjId());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (insSiteManageList != null && insSiteManageList.size() > 0) {

			params2.put("constructionNum", insSiteManageList.get(0)
					.getConstructionNum());
			if (insSiteManageList.get(0).getSmId() != null) {
				params2.put("smId", insSiteManageList.get(0).getSmId() + "");
			}
		}
		return params2;
	}

	@Override
	public DataType dataType() {
		// TODO Auto-generated method stub
		return DataType.Pg;
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
				String constructionNum = oParams.get("constructionNum");
				Dao<InsSiteManage, Long> insSiteManageDao = AppContext
						.getInstance().getAppDbHelper()
						.getDao(InsSiteManage.class);
				List<InsSiteManage> insSiteManageList = insSiteManageDao
						.queryForEq("id", pid);
				if (insSiteManageList != null
						&& insSiteManageList.size() > 0) {
					InsSiteManage insSiteManage = insSiteManageList.get(0);
					insSiteManage.setIsFirst("1");
					Long insCount = insSiteManage.getInsCount();
					if(insCount==null){
						insCount=(long) 0;
					}
					Long frequencyNumber=insSiteManage.getFrequencyNumber();
					if(frequencyNumber==null){
						frequencyNumber=(long) 0;
					}
					if(insCount>=0&&insCount<frequencyNumber){
						++insCount;
						if(insCount==frequencyNumber){
							insCount=(long) -1;
						}
						insSiteManage.setInsCount(insCount);
						insSiteManage.setLastInsDateStr(getCurDate());
					}
					insSiteManageDao.update(insSiteManage);
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
			for(InsSiteManage dv :  sitePlanOperate.getThisTimeData(insTablePushTaskVo
					.getTaskNum())){
				geos.add(dv.getPrjBound());
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
			for(InsSiteManage dv :  sitePlanOperate.getHadDoData(insTablePushTaskVo.getTaskNum())){
				geos.add(dv.getPrjBound());
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
