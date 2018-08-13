package com.movementinsome.app.remind.facPC;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.content.Context;

import com.movementinsome.AppContext;
import com.movementinsome.app.remind.FreqData;
import com.movementinsome.app.remind.IPlanOperate;
import com.movementinsome.app.remind.PlanOperate;
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.database.vo.InsCheckFacRoad;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsPlanTaskVO;

public class PcChkPntPlanOperate extends PlanOperate implements IPlanOperate {
	public static final Class<FreqData>[] classes = new Class[] {};

	//static InsPlanManage insPlanManage;
	static Map<String, PcChkPntTranceTask> inTranTaskCache = new HashMap<String, PcChkPntTranceTask>(); // 启动执行路段判断线程
	// 待巡路段缓存
	static Map<String, Vector<InsCheckFacRoad>> waitForDoDataCache = new HashMap<String, Vector<InsCheckFacRoad>>();// 本次等执行数据
	// 已巡路段缓存
	static Map<String, Vector<InsCheckFacRoad>> hadDoDataCache = new HashMap<String, Vector<InsCheckFacRoad>>();// 本次等执行数据

	// 待签到缓存
	//static Map<String, Vector<InsPatrolDataVO>> waitForDoChkPntCache = new HashMap<String, Vector<InsPatrolDataVO>>();// 本次等执行数据

	// 当前所在路段
	//static InsPatrolOnsiteRecordVO curTaskLuDuan;
	// 当前最近签到点
	static NearObject curTaskChkPnt;



/*	public static InsPlanManage getInstance() {
		if (insPlanManage == null) {
			insPlanManage = new InsPlanManage();
		}
		return insPlanManage;
	}*/

	/**
	 * 获取所有周期性数据
	 * 
	 * @return
	 */
	public List<InsPatrolDataVO> getAllFreqData() {
		List<InsPatrolDataVO> result = new ArrayList<InsPatrolDataVO>();
		try {
			List<InsPlanTaskVO> lst = insPatrolTaskDao.queryForEq(
					"workTaskType", "PLAN_PATROL_CYCLE");
			for (InsPlanTaskVO task : lst) {
				List<InsPatrolDataVO> temp = insPatrolDataDao.queryForEq(
						"workTaskNum", task.getWorkTaskNum());
				if (temp.size() > 0) {
					result.addAll(temp);
				}
			}
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取指定任务路段数据
	 * 
	 * @return
	 */
	public List<InsPatrolDataVO> getAllData(String workTaskNum) {
		try {
			return insPatrolDataDao.queryForEq("workTaskNum", workTaskNum);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取本次等执行周期性巡查数据
	 * 
	 * @return
	 */
	public List<InsCheckFacRoad> getThisTimeFreqData() {
		List<InsCheckFacRoad> insCheckFacRoadLists = new ArrayList<InsCheckFacRoad>();
		try {
			InsCheckFacRoadDao = AppContext.getInstance().getAppDbHelper()
					.getDao(InsCheckFacRoad.class);
			List<InsCheckFacRoad> insCheckFacRoadList = InsCheckFacRoadDao.queryForEq(
							"workTaskNum", "");
				for (int i = 0; i < insCheckFacRoadList.size(); i++) {
					if(insCheckFacRoadList.get(i).getFacType().equals("阀门")){
						insCheckFacRoadLists.add(insCheckFacRoadList.get(i));
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		return insCheckFacRoadLists;
	}

	/**
	 * 获取本次等执行巡查数据
	 * 
	 * @return
	 */
	public List<InsCheckFacRoad> getThisTimeData(String workTaskNum) {
		List<InsCheckFacRoad> insCheckFacRoadLists = new ArrayList<InsCheckFacRoad>();
		try {
			InsCheckFacRoadDao = AppContext.getInstance().getAppDbHelper()
					.getDao(InsCheckFacRoad.class);
			
			List<InsCheckFacRoad> insCheckFacRoadList = InsCheckFacRoadDao.queryForEq(
							"workTaskNum", workTaskNum);
				for (int i = 0; i < insCheckFacRoadList.size(); i++) {
					if(insCheckFacRoadList.get(i).getFacType().equals("阀门")){
						insCheckFacRoadLists.add(insCheckFacRoadList.get(i));
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
		}
		return insCheckFacRoadLists;
	}


	/**
	 * 检查该对象巡查对象本次巡查完毕之后是否本周期巡查已经结束
	 * 
	 * @param obj
	 * @return
	 */
/*	public Integer checkIsThisFreqEnd(InsPatrolDataVO obj) {
		// List<InsPatrolDataVO> result = new ArrayList<InsPatrolDataVO>();
		String flag = obj.getFrequencyType();
		if (obj.getFrequency().split("\\,").length > 1) {
			if (obj.getFrequency().split("\\,")[1].equals("日")
					|| obj.getFrequency().split("\\,")[1].equals("月")
					|| obj.getFrequency().split("\\,")[1].equals("年"))
				flag += "_" + obj.getFrequency().split("\\,")[1];
		}

		for (Class<FreqData> clazz : classes) {
			try {
				IFreqData freqData = (IFreqData) clazz.newInstance();
				if (flag.equals(freqData.getClassFlag())) {
					return freqData.checkInThisFreq(FreqData.changChkObj(obj));
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}*/

	/**
	 * 获取等待巡查的路段缓存
	 * 
	 * @param id
	 * @return
	 */
	public Vector<InsCheckFacRoad> getWaitForDoDataCache(String id) {
		if (waitForDoDataCache.containsKey(id)) {
			return waitForDoDataCache.get(id);
		} else {
			return null;
		}
	}

	/**
	 * 获取等待巡查的路段缓存
	 * 
	 * @param id
	 * @return
	 */
/*	public Vector<InsPatrolDataVO> getWaitForDoChkPntCache(String id) {
		if (waitForDoChkPntCache.containsKey(id)) {
			return waitForDoChkPntCache.get(id);
		} else {
			return null;
		}
	}*/

	/**
	 * 获取本次已巡查的路段缓存
	 * 
	 * @param id
	 * @return
	 */
	public Vector<InsCheckFacRoad> getHadDoDataCache(String id) {
		if (hadDoDataCache.containsKey(id)) {
			return hadDoDataCache.get(id);
		} else {
			return null;
		}
	}

	public void addHadDoDataCache(String id, InsCheckFacRoad data) {
		if (hadDoDataCache.containsKey(id)) {
			hadDoDataCache.get(id).add(data);
		} else {
			Vector<InsCheckFacRoad> v = new Vector<InsCheckFacRoad>();
			v.add(data);
			hadDoDataCache.put(id, v);
		}
	}

	public void removeWaitForDoDataCache(String id, InsCheckFacRoad data) {
		if (waitForDoDataCache.containsKey(id)) {
			waitForDoDataCache.get(id).remove(data);
		}
	}

	/**
	 * 获取当前所在路段
	 * 
	 * @return
	 */
/*	public InsPatrolOnsiteRecordVO getCurTaskLuDuan() {
		return curTaskLuDuan;
	}

	public void setCurTaskLuDuan(InsPatrolOnsiteRecordVO curTaskLuDuan) {
		this.curTaskLuDuan = curTaskLuDuan;
		notifyDataSetChange();
	}*/

	/**
	 * 距离最近签到点
	 * 
	 * @return
	 */
	public NearObject getCurTaskChkPnt() {
		return curTaskChkPnt;
	}

	public void setCurTaskChkPnt(NearObject curTaskChkPnt) {
		this.curTaskChkPnt = curTaskChkPnt;
		notifyDataSetChange();
	}

	/**
	 * 发出开始巡查动作
	 * 
	 * @param doId
	 *            每次执行产生的ID识别码,用UUID表达
	 * @param obj
	 *            本次本巡内容
	 */
	public void insBeginDo(Context context, String doId,String workTaskNum) {

		if (!inTranTaskCache.containsKey(doId)) {
			if (!"".equals(workTaskNum)){
				Vector<InsCheckFacRoad> v = new Vector<InsCheckFacRoad>();
				for (InsCheckFacRoad d : this.getThisTimeData(workTaskNum)) {
					v.add(d);
				}
				waitForDoDataCache.put(doId, v);
			}
//			else{
//				Vector<InsCheckFacRoad> v = new Vector<InsCheckFacRoad>();
//				for (InsCheckFacRoad d : this.getThisTimeFreqData()) {
//					v.add(d);
//				}
//				waitForDoDataCache.put(doId, v);
//			}
			PcChkPntTranceTask workTran = new PcChkPntTranceTask(this,context, doId);
			inTranTaskCache.put(doId, workTran);
			workTran.start(0, 25000);
		} else {
			PcChkPntTranceTask workTran = inTranTaskCache.get(doId);
			workTran.cancel();
			workTran.start(0, 25000);
		}
	}


	/**
	 * 发出停止执行动作
	 * 
	 * @param doId
	 *            开始执行时产生的识别码
	 */
	public void insEndDo(String doId) {
		if (inTranTaskCache.containsKey(doId)) {
			PcChkPntTranceTask workTran = inTranTaskCache.get(doId);
			workTran.end();

			inTranTaskCache.remove(doId);
			waitForDoDataCache.remove(doId);

			curTaskChkPnt = null;
		}
	}

}
