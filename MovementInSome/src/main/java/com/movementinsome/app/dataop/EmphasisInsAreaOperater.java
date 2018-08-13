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
import com.movementinsome.app.remind.emphasisInsArea.EmphasisInsAreaOperate;
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.app.remind.road.InsPatrolOnsiteRecordExtVO;
import com.movementinsome.database.vo.BsEmphasisInsArea;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.initial.model.Module;
import com.j256.ormlite.dao.Dao;

public class EmphasisInsAreaOperater extends DataOperater implements IDataOperater{
	public EmphasisInsAreaOperater(Context context,
			InsTablePushTaskVo insTablePushTaskVo, String guid,
			List<Module> lstModule) {
		super(context, insTablePushTaskVo, guid, lstModule);
		// TODO Auto-generated constructor stub
	}

	private EmphasisInsAreaOperate emphasisInsAreaOperate;// 
	
	private List<BsEmphasisInsArea> list;
	private List<BsEmphasisInsArea> hadDoDataCache;
	// 未巡列表
	private List<BsEmphasisInsArea> notDoneDataCache;
	
	private NearObject nearObject;
	
	private List<BsEmphasisInsArea> nearDataCache;
	
	
	@Override
	public int initial(final Handler handler) {
		// TODO Auto-generated method stub
		emphasisInsAreaOperate = (EmphasisInsAreaOperate) InsPlanManage
				.getInstance(InsPlanManage.PLAN_MANAGE_TYPE.EMPHASIS_INS_AREA);// 
		emphasisInsAreaOperate.setDataSetChangeListener(new DataSetSupervioer() {
			public void onChange() {
				// 填写一些前置操作，如更新数据
				handler.sendEmptyMessage(1);
				// DisplayModel(); // 重新绘制
				// 填写一些后置操作，如更新状态
			}
		});
		emphasisInsAreaOperate
		.insBeginDo(context, guid, insTablePushTaskVo.getTaskNum());
		
		list = emphasisInsAreaOperate.getThisTimeData(insTablePushTaskVo
				.getTaskNum());
		refreshThisTimeData();
		nearObject = emphasisInsAreaOperate.getNearSite();
		nearDataCache = new ArrayList<BsEmphasisInsArea>();
		//updateWorkQdd();
		
		return list==null?0:list.size();
	}
	
	@Override
	public void end() {
		// TODO Auto-generated method stub
		if (emphasisInsAreaOperate != null) {
			emphasisInsAreaOperate.insEndDo(guid);
		}
	}

	@Override
	public List<Map<String, Object>> getHadDoDataList() {
		// TODO Auto-generated method stub
		
		return getListData(hadDoDataCache, "BsEmphasisInsArea");
	}

	@Override
	public List<Map<String, Object>> getWouldDoDataList() {
		// TODO Auto-generated method stub
		
		return getListData(list, "BsEmphasisInsArea");
	}

	@Override
	public void refreshThisTimeData() {
		// TODO Auto-generated method stub
		list = emphasisInsAreaOperate.getThisTimeData(insTablePushTaskVo
				.getTaskNum());
		if(hadDoDataCache == null){
			hadDoDataCache = new ArrayList<BsEmphasisInsArea>();
		}else{
			hadDoDataCache.removeAll(hadDoDataCache);
		}
		if(notDoneDataCache == null){
			notDoneDataCache = new ArrayList<BsEmphasisInsArea>();
		}else{
			notDoneDataCache.removeAll(notDoneDataCache);
		}
		for(BsEmphasisInsArea bsEmphasisInsArea : list){
			if(bsEmphasisInsArea.getInsCount()==null||bsEmphasisInsArea.getInsCount()==0){
				notDoneDataCache.add(bsEmphasisInsArea);
			}else{
				hadDoDataCache.add(bsEmphasisInsArea);
			}
		}
	}

	@Override
	public NearObject getCurentNearObj() {
		// TODO Auto-generated method stub
		nearObject = emphasisInsAreaOperate.getNearSite();
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
		List<BsEmphasisInsArea> vList = null;
		try {
			Dao<BsEmphasisInsArea, Long> BsEmphasisInsAreaDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(BsEmphasisInsArea.class);
			vList = BsEmphasisInsAreaDao.queryForEq("guid",
					nearObject.getObjId());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (vList != null && vList.size() > 0) {
			for (Module m : lstModule) {
				String mId = vList.get(0).getReportTableNames();
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
					Dao<BsEmphasisInsArea, Long> patrolDataDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(BsEmphasisInsArea.class);
					List<BsEmphasisInsArea> BsEmphasisInsAreaList = patrolDataDao.queryForEq("eiaId", pid);
					if(BsEmphasisInsAreaList!=null&&BsEmphasisInsAreaList.size()>0){
						Long frequencyNumber=BsEmphasisInsAreaList.get(0).getFrequencyNumber();
						if(frequencyNumber==null){
							frequencyNumber=(long) 0;
						}
						Long insCount = BsEmphasisInsAreaList.get(0).getInsCount();
						if(insCount==null){
							insCount=(long) 0;
						}
						if(insCount>=0&&insCount<frequencyNumber){
							++insCount;
							if(insCount==frequencyNumber){
								insCount=(long) -1;
							}
							BsEmphasisInsAreaList.get(0).setInsCount(insCount);
							BsEmphasisInsAreaList.get(0).setLastInsDateStr(getCurDate());
							patrolDataDao.update(BsEmphasisInsAreaList.get(0));
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
			for(BsEmphasisInsArea dv :  emphasisInsAreaOperate.getThisTimeData(insTablePushTaskVo
					.getTaskNum())){
				geos.add(dv.getEiaCoordinate());
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
			for(BsEmphasisInsArea dv :  emphasisInsAreaOperate.getHadDoData(insTablePushTaskVo
					.getTaskNum())){
				geos.add(dv.getEiaCoordinate());
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
		nearDataCache.removeAll(nearDataCache);
		if(nearObject!=null&&list!=null){
			for(int i =0 ;i<list.size();++i){
				if(nearObject.getObjId().equals(list.get(i).getEiaId()+"")){
					nearDataCache.add(list.get(i));
				}
			}
		}
		return getListData(nearDataCache, "BsEmphasisInsArea");
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
