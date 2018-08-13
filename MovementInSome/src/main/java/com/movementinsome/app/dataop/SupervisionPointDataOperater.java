package com.movementinsome.app.dataop;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.movementinsome.AppContext;
import com.movementinsome.app.remind.InsPlanManage;
import com.movementinsome.app.remind.PlanOperate.DataSetSupervioer;
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.app.remind.road.InsPatrolOnsiteRecordExtVO;
import com.movementinsome.app.remind.routineInsArea.RoutineInsAreaOperate;
import com.movementinsome.app.remind.supervisionPoint.SupervisionPointPlanOperate;
import com.movementinsome.database.vo.BsRoutineInsArea;
import com.movementinsome.database.vo.BsSupervisionPoint;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.initial.model.Module;
import com.movementinsome.map.utils.MapUtil;
import com.j256.ormlite.dao.Dao;

public class SupervisionPointDataOperater extends DataOperater implements IDataOperater{
	public SupervisionPointDataOperater(Context context,
			InsTablePushTaskVo insTablePushTaskVo, String guid,
			List<Module> lstModule) {
		super(context, insTablePushTaskVo, guid, lstModule);
		// TODO Auto-generated constructor stub
	}

	private SupervisionPointPlanOperate supervisionPointPlanOperate;// 签到点
	private RoutineInsAreaOperate routineInsAreaOperate;// 区域
	
	private List<BsSupervisionPoint> supervisionPointList;
	private List<BsSupervisionPoint> supervisionPointHadDoDataCache;
	private List<BsSupervisionPoint> supervisionPointNoHadDoDataCache;
	
	private NearObject nearObject;
	private NearObject nearObject2;
	private List<NearObject> nearObjectList;
	
	private List<BsSupervisionPoint> supervisionPointNearDataCache;
	private List<BsRoutineInsArea> bsRoutineInsAreaList;
	private SpatialReference sr;
	
	
	@Override
	public int initial(final Handler handler) {
		// TODO Auto-generated method stub
		
		this.sr = SpatialReference.create(Integer.valueOf(AppContext.getInstance().getSrid()));
		nearObjectList = new ArrayList<NearObject>();
		
		supervisionPointPlanOperate = (SupervisionPointPlanOperate) InsPlanManage
				.getInstance(InsPlanManage.PLAN_MANAGE_TYPE.SUPERVISION_POINT);// 签到点
		supervisionPointPlanOperate
		.insBeginDo(context, guid, insTablePushTaskVo.getTaskNum());
		
		routineInsAreaOperate = (RoutineInsAreaOperate) InsPlanManage.getInstance(InsPlanManage.PLAN_MANAGE_TYPE.ROUTINE_INS_AREA);
		
		supervisionPointList = supervisionPointPlanOperate.getThisTimeData(insTablePushTaskVo
				.getTaskNum());
		refreshThisTimeData();
		nearObject = supervisionPointPlanOperate.getNearSite();
		nearObject2 = routineInsAreaOperate.getNearSite();
		
		supervisionPointNearDataCache = new ArrayList<BsSupervisionPoint>();
		//updateWorkQdd();

		supervisionPointPlanOperate.setDataSetChangeListener(new DataSetSupervioer() {
			public void onChange() {
				// 填写一些前置操作，如更新数据
				handler.sendEmptyMessage(1);
				// DisplayModel(); // 重新绘制
				// 填写一些后置操作，如更新状态
			}
		});
		routineInsAreaOperate.insBeginDo(context, guid, insTablePushTaskVo.getTaskNum());
		routineInsAreaOperate.setDataSetChangeListener(new DataSetSupervioer() {
		public void onChange() {
				// 填写一些前置操作，如更新数据
				handler.sendEmptyMessage(1);
				// DisplayModel(); // 重新绘制
				// 填写一些后置操作，如更新状态
			}
		});
		Dao<BsRoutineInsArea, Long> bsRoutineInsAreaDao;
		try {
			bsRoutineInsAreaDao = AppContext.getInstance().getAppDbHelper()
			.getDao(BsRoutineInsArea.class);
			bsRoutineInsAreaList = bsRoutineInsAreaDao.queryForEq(
					"workTaskNum", insTablePushTaskVo.getTaskNum());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return supervisionPointNoHadDoDataCache==null?0:supervisionPointNoHadDoDataCache.size();
	}
	
	@Override
	public void end() {
		// TODO Auto-generated method stub
		if (supervisionPointPlanOperate != null) {
			supervisionPointPlanOperate.insEndDo(guid);
		}
		if (routineInsAreaOperate != null) {
			routineInsAreaOperate.insEndDo(guid);
		}
	}

	@Override
	public List<Map<String, Object>> getHadDoDataList() {
		// TODO Auto-generated method stub
		
		return getListData(supervisionPointHadDoDataCache, "BsSupervisionPoint");
	}

	@Override
	public List<Map<String, Object>> getWouldDoDataList() {
		// TODO Auto-generated method stub
		List<Map<String, Object>> dataList;
		dataList = new ArrayList<Map<String, Object>>();
		/*if(bsRoutineInsAreaList!=null){
			dataList.addAll(getListData(bsRoutineInsAreaList, "BsRoutineInsArea"));
		}*/
		if (bsRoutineInsAreaList != null) {
			for (BsRoutineInsArea bsRoutineInsArea : bsRoutineInsAreaList) {
				Map<String, Object> d = new HashMap<String, Object>();
				d.put("BsRoutineInsArea", bsRoutineInsArea);
				dataList.add(d);
				String riaCoordinate = bsRoutineInsArea.getRiaCoordinate();
				Geometry geo1 = MapUtil.wkt2Geometry(riaCoordinate);
				if (supervisionPointList != null) {
					for (BsSupervisionPoint bsSupervisionPoint : supervisionPointList) {
						String spCoordinate =bsSupervisionPoint.getSpCoordinate();
						Geometry geo2 = MapUtil.wkt2Geometry(spCoordinate);
						if(GeometryEngine.contains(geo1, geo2, sr)){
							d = new HashMap<String, Object>();
							bsSupervisionPoint.setAreaNum(bsRoutineInsArea.getAreaNum());
							d.put("BsSupervisionPoint", bsSupervisionPoint);
							dataList.add(d);
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
		supervisionPointList = supervisionPointPlanOperate.getThisTimeData(insTablePushTaskVo
				.getTaskNum());
		if(supervisionPointHadDoDataCache == null){
			supervisionPointHadDoDataCache = new ArrayList<BsSupervisionPoint>();
		}else{
			supervisionPointHadDoDataCache.removeAll(supervisionPointHadDoDataCache);
		}
		if(supervisionPointNoHadDoDataCache == null){
			supervisionPointNoHadDoDataCache = new ArrayList<BsSupervisionPoint>();
		}else{
			supervisionPointNoHadDoDataCache.removeAll(supervisionPointNoHadDoDataCache);
		}
		for(BsSupervisionPoint bsSupervisionPoint : supervisionPointList){
			if(bsSupervisionPoint.getInsCount()==null||bsSupervisionPoint.getInsCount()==0){
				supervisionPointNoHadDoDataCache.add(bsSupervisionPoint);//未巡查过
			}else{
				supervisionPointHadDoDataCache.add(bsSupervisionPoint);//开始巡查
			}
		}
	}

	@Override
	public NearObject getCurentNearObj() {
		// TODO Auto-generated method stub
		nearObject = supervisionPointPlanOperate.getNearSite();
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
		List<BsSupervisionPoint> vList = null;
		try {
			Dao<BsSupervisionPoint, Long> bsSupervisionPointDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(BsSupervisionPoint.class);
			vList = bsSupervisionPointDao.queryForEq("spId",
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
		return DataType.Pts;
	}

	@Override
	public void callback(Intent data) {
		// TODO Auto-generated method stub
		String taskNum = data.getStringExtra("taskNum");// 任务编号
		String taskCategory = data.getStringExtra("taskCategory");// 表单类型
		String tableName = data.getStringExtra("tableName");// 表单在数据库中表名
		String pid= data.getStringExtra("pid");
		String uploadTable = data.getStringExtra("uploadTable");
		boolean isUpdate = data.getBooleanExtra("isUpdate", true);
		int opState = data.getIntExtra("opState", 0);
		try {
			HashMap<String, String> oParams = (HashMap<String, String>) data
					.getSerializableExtra("oParams");

			if (opState == 1&&uploadTable!=null&&"1".equals(uploadTable)&&isUpdate) {
				try {
					Dao<BsSupervisionPoint, Long> bsSupervisionPointDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(BsSupervisionPoint.class);
					List<BsSupervisionPoint> bsSupervisionPointList = bsSupervisionPointDao.queryForEq("spId", pid);
					if(bsSupervisionPointList!=null&&bsSupervisionPointList.size()>0){
						Long frequencyNumber=bsSupervisionPointList.get(0).getFrequencyNumber();
						if(frequencyNumber==null){
							frequencyNumber=(long) 0;
						}
						Long insCount = bsSupervisionPointList.get(0).getInsCount();
						if(insCount==null){
							insCount=(long) 0;
						}
						if(insCount>=0&&insCount<frequencyNumber){
							++insCount;
							if(insCount==frequencyNumber){
								insCount=(long) -1;
							}
							bsSupervisionPointList.get(0).setInsCount(insCount);
							bsSupervisionPointList.get(0).setLastInsDateStr(getCurDate());
							bsSupervisionPointDao.update(bsSupervisionPointList.get(0));
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
			for(BsSupervisionPoint dv :  supervisionPointPlanOperate.getThisTimeData(insTablePushTaskVo
					.getTaskNum())){
				geos.add(dv.getSpCoordinate());
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
			for(BsSupervisionPoint dv :  supervisionPointPlanOperate.getHadDoData(insTablePushTaskVo
					.getTaskNum())){
				geos.add(dv.getSpCoordinate());
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
		supervisionPointNearDataCache.removeAll(supervisionPointNearDataCache);
		List<BsRoutineInsArea> bsRoutineInsAreas = new ArrayList<BsRoutineInsArea>();
		if(nearObjectList!=null){
			for(NearObject nb : nearObjectList){
				if(supervisionPointList!=null){
					for(int i =0 ;i<supervisionPointList.size();++i){
						if(nb.getObjId().equals(supervisionPointList.get(i).getSpId()+"")){
							supervisionPointNearDataCache.add(supervisionPointList.get(i));
						}
					}
				}
				if(bsRoutineInsAreaList!=null){
					for(int i =0 ;i<bsRoutineInsAreaList.size();++i){
						if(nb.getObjId().equals(bsRoutineInsAreaList.get(i).getRiaId()+"")){
							bsRoutineInsAreas.add(bsRoutineInsAreaList.get(i));
						}
					}
				}
			}
		}
		List<Map<String, Object>> data = getListData(supervisionPointNearDataCache, "BsSupervisionPoint");
		data.addAll(getListData(bsRoutineInsAreas, "BsRoutineInsArea"));
		return data;
	}

	@Override
	public List<NearObject> getCurentNearObjs() {
		// TODO Auto-generated method stub
		nearObject = supervisionPointPlanOperate.getNearSite();
		nearObject2 = routineInsAreaOperate.getNearSite();
		nearObjectList.removeAll(nearObjectList);
		if(nearObject!=null){
			nearObjectList.add(nearObject);
		}
		/*if(nearObject2!=null){
			nearObjectList.add(nearObject2);
		}*/
		return nearObjectList;
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
