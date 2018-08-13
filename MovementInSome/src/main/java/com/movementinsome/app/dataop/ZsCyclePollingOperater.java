package com.movementinsome.app.dataop;

import java.sql.SQLException;
import java.util.ArrayList;
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
import com.movementinsome.app.remind.valve.ValvePlanOperate;
import com.movementinsome.database.vo.InsPatrolAreaData;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.initial.model.Module;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;

public class ZsCyclePollingOperater extends DataOperater implements
		IDataOperater {

	private String guid2;
	
	public ZsCyclePollingOperater(Context context,
			InsTablePushTaskVo insTablePushTaskVo, String guid,
			List<Module> lstModule) {
		super(context, insTablePushTaskVo, guid, lstModule);
		// TODO Auto-generated constructor stub
		guid2 = UUID.randomUUID().toString();
	}

	private ValvePlanOperate valvePlanOperate;
	private PatrolAreaOperate patrolAreaOperate;

	private List<InsPatrolDataVO> valveList;
	private List<InsPatrolDataVO> valveHadDoDataCache;

	private NearObject nearObject;
	private List<InsPatrolDataVO> valveNearDataCache;

	private List<InsPatrolAreaData> insPatrolAreaDataList;

	@Override
	public int initial(final Handler handler) {
		// TODO Auto-generated method stub
		// 设施
		valvePlanOperate = (ValvePlanOperate) InsPlanManage
				.getInstance(InsPlanManage.PLAN_MANAGE_TYPE.VALVE);
		patrolAreaOperate = (PatrolAreaOperate) InsPlanManage
				.getInstance(InsPlanManage.PLAN_MANAGE_TYPE.ZS_PATROL_AREA);

		valvePlanOperate.setDataSetChangeListener(new DataSetSupervioer() {
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
		valvePlanOperate.insBeginDo(context, guid,
				insTablePushTaskVo.getTaskNum());
		patrolAreaOperate.insBeginDo(context, guid2,
				insTablePushTaskVo.getTaskNum());

		valveList = valvePlanOperate.getThisTimeData(insTablePushTaskVo
				.getTaskNum());
		valveHadDoDataCache = valvePlanOperate.getHadDoData(insTablePushTaskVo
				.getTaskNum());
		nearObject = valvePlanOperate.getNearSite();
		valveNearDataCache = new ArrayList<InsPatrolDataVO>();

		insPatrolAreaDataList = patrolAreaOperate
				.getThisTimeData(insTablePushTaskVo.getTaskNum());
		return valveList == null ? 0 : valveList.size();
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		if (valvePlanOperate != null) {
			valvePlanOperate.insEndDo(guid);
		}
		if (patrolAreaOperate != null) {
			patrolAreaOperate.insEndDo(guid2);
		}
	}

	@Override
	public List<Map<String, Object>> getHadDoDataList() {
		// TODO Auto-generated method stub
		valveHadDoDataCache = valvePlanOperate.getHadDoDataCache(guid);
		return getListData(valveHadDoDataCache, "InsPatrolDataVO");
	}

	@Override
	public List<Map<String, Object>> getWouldDoDataList() {
		// TODO Auto-generated method stub
		valveList = valvePlanOperate.getThisTimeData(insTablePushTaskVo
				.getTaskNum());
		List<Map<String, Object>> dataList;
		dataList = new ArrayList<Map<String, Object>>();
		if (insPatrolAreaDataList != null) {
			for (InsPatrolAreaData insPatrolAreaData : insPatrolAreaDataList) {
				Map<String, Object> d = new HashMap<String, Object>();
				d.put("InsPatrolAreaData", insPatrolAreaData);
				dataList.add(d);
				if (valveList != null) {
					for (InsPatrolDataVO insPatrolDataVO : valveList) {
						if (insPatrolDataVO.getAreaId() != null) {
							if (insPatrolDataVO.getAreaId().longValue() == insPatrolAreaData
									.getAreaId().longValue()) {
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
		valveList = valvePlanOperate.getThisTimeData(insTablePushTaskVo
				.getTaskNum());
		insPatrolAreaDataList = patrolAreaOperate
				.getThisTimeData(insTablePushTaskVo.getTaskNum());
		valveHadDoDataCache = valvePlanOperate.getHadDoDataCache(guid);
	}

	@Override
	public NearObject getCurentNearObj() {
		// TODO Auto-generated method stub
		nearObject = valvePlanOperate.getNearSite();
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
			vList = insPatrolDataVODao
					.queryForEq("guid", nearObject.getObjId());

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
		String pid = data.getStringExtra("pid");
		String uploadTable = data.getStringExtra("uploadTable");
		int opState = data.getIntExtra("opState", 0);
		try {
			HashMap<String, String> oParams = (HashMap<String, String>) data
					.getSerializableExtra("oParams");

			if (opState == 1 && uploadTable != null && "1".equals(uploadTable)) {
				try {
					Dao<InsPatrolDataVO, Long> patrolDataDao = AppContext
							.getInstance().getAppDbHelper()
							.getDao(InsPatrolDataVO.class);
					List<InsPatrolDataVO> insPatrolDataVOList = patrolDataDao
							.queryForEq("guid", pid);
					if (insPatrolDataVOList != null
							&& insPatrolDataVOList.size() > 0) {
						Long frequencyNumber = insPatrolDataVOList.get(0)
								.getFrequencyNumber();
						if (frequencyNumber == null) {
							frequencyNumber = (long) 0;
						}
						Long insCount = insPatrolDataVOList.get(0)
								.getInsCount();
						if (insCount == null) {
							insCount = (long) 0;
						}
						if (insCount >= 0 && insCount < frequencyNumber) {
							++insCount;
							if (insCount == frequencyNumber) {
								insCount = (long) -1;
							}
							insPatrolDataVOList.get(0).setInsCount(insCount);
							insPatrolDataVOList.get(0).setLastInsDateStr(
									getCurDate());
							patrolDataDao.update(insPatrolDataVOList.get(0));
						}
						Long areaId = insPatrolDataVOList.get(0).getAreaId();
						GenericRawResults<String[]> genericRawResults = patrolDataDao
								.queryRaw(
										"SELECT * FROM InsPatrolDataVO WHERE insCount >= 0 and areaId = ?",
										new String[] { areaId + "" });
						List<String[]> ls = genericRawResults.getResults();
						if (ls != null && ls.size() == 0) {
							Dao<InsPatrolAreaData, Long> insPatrolAreaDataDao = AppContext
									.getInstance().getAppDbHelper()
									.getDao(InsPatrolAreaData.class);
							Map<String, Object> m = new HashMap<String, Object>();
							m.put("areaId", areaId);
							m.put("workTaskNum", taskNum);
							List<InsPatrolAreaData> insPatrolAreaDataList = insPatrolAreaDataDao
									.queryForFieldValues(m);
							if (insPatrolAreaDataList != null
									&& insPatrolAreaDataList.size() > 0) {
								InsPatrolAreaData insPatrolAreaData = insPatrolAreaDataList
										.get(0);
								Long fn = insPatrolAreaData
										.getFrequencyNumber();
								if (fn == null) {
									fn = (long) 0;
								}
								Long areaCount = insPatrolAreaData
										.getInsCount();
								if (areaCount == null) {
									areaCount = (long) 0;
								}
								if (areaCount >= 0 && areaCount < fn) {
									++areaCount;
									if (areaCount == fn) {
										areaCount = (long) -1;
									}
									insPatrolAreaData.setInsCount(insCount);
									insPatrolAreaData
											.setLastInsDateStr(getCurDate());
									insPatrolAreaDataDao
											.update(insPatrolAreaData);
								}
							}
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
		try {
			for (InsPatrolDataVO dv : valvePlanOperate
					.getThisTimeData(insTablePushTaskVo.getTaskNum())) {
				geos.add(dv.getGeometryStr());
			}
		} catch (NullPointerException ex) {
			;
		}
		return geos;
	}

	@Override
	public List<String> getHadDoDataGeo() {
		// TODO Auto-generated method stub
		List<String> geos = new ArrayList();
		try {
			for (InsPatrolDataVO dv : valvePlanOperate
					.getHadDoData(insTablePushTaskVo.getTaskNum())) {
				geos.add(dv.getGeometryStr());
			}
		} catch (NullPointerException ex) {
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
		List<InsPatrolAreaData> insPatrolAreaDatas = new ArrayList<InsPatrolAreaData>();
		if (nearObject != null && valveList != null) {
			for (int i = 0; i < valveList.size(); ++i) {
				if (nearObject.getObjId().equals(valveList.get(i).getGuid())) {
					valveNearDataCache.add(valveList.get(i));
				}
			}
			if (insPatrolAreaDataList != null) {
				for (int i = 0; i < insPatrolAreaDataList.size(); ++i) {
					if (nearObject.getObjId().equals(
							insPatrolAreaDataList.get(i).getId() + "")) {
						insPatrolAreaDatas.add(insPatrolAreaDataList.get(i));
					}
				}
			}
		}
		List<Map<String, Object>> data = getListData(valveNearDataCache,
				"InsPatrolDataVO");
		data.addAll(getListData(insPatrolAreaDatas, "InsPatrolAreaData"));
		return data;
	}

	@Override
	public List<NearObject> getCurentNearObjs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void speObjOp(InsPatrolOnsiteRecordExtVO object, int state,
			String memo) {
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
