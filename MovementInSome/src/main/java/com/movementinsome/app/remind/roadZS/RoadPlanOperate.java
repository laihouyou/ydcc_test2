package com.movementinsome.app.remind.roadZS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;

import com.movementinsome.app.remind.FreqData;
import com.movementinsome.app.remind.IFreqData;
import com.movementinsome.app.remind.IPlanOperate;
import com.movementinsome.app.remind.PlanOperate;
import com.movementinsome.app.remind.road.InsPatrolOnsiteRecordExtVO;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsPlanTaskVO;

public class RoadPlanOperate extends PlanOperate implements IPlanOperate {
	public static final Class<FreqData>[] classes = new Class[] {
			FreqDataDefaultDay.class, FreqDataDay.class, FreqDataWeek.class,
			FreqDataDefaultMonth.class, FreqDataDefaultYear.class ,FreqDataWorkDay.class};

	static Map<String, RoadTranceTaskExp> inTranTaskCache = new ConcurrentHashMap<String, RoadTranceTaskExp>(); // 启动执行路段判断线程
	// 待巡路段缓存
	static Map<String, Vector<InsPatrolDataVO>> waitForDoDataCache = new ConcurrentHashMap<String, Vector<InsPatrolDataVO>>();// 本次等执行数据
	// 已巡路段缓存
	static Map<String, Vector<InsPatrolDataVO>> hadDoDataCache = new ConcurrentHashMap<String, Vector<InsPatrolDataVO>>();// 本次等执行数据

	// 待签到缓存
	//static Map<String, Vector<InsPatrolDataVO>> waitForDoChkPntCache = new HashMap<String, Vector<InsPatrolDataVO>>();// 本次等执行数据

	// 当前所在路段
	static InsPatrolOnsiteRecordExtVO curTaskLuDuan;
	// 当前最近签到点
	//static NearObject curTaskChkPnt;
	Context context;
	String doId;
	String workTaskNum;


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
			return insPatrolDataDao.queryForEq("workTaskNum", "workTaskNum");
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
	public List<InsPatrolDataVO> getThisTimeFreqData() {
		List<InsPatrolDataVO> result = new ArrayList<InsPatrolDataVO>();

		for (Class<FreqData> clazz : classes) {
			try {
				List<InsPlanTaskVO> lstTask;

				lstTask = insPatrolTaskDao.queryForEq("workTaskType",
						"PLAN_PATROL_CYCLE");

				for (InsPlanTaskVO task : lstTask) {
					IFreqData freqData = (IFreqData) clazz.newInstance();
					List<InsPatrolDataVO> lst = (List<InsPatrolDataVO>) freqData
							.getData(task.getWorkTaskNum());

					if (lst.size() > 0) {
						result.addAll(lst);
					}
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 获取本次等执行巡查数据
	 * 
	 * @return
	 */
	public List<InsPatrolDataVO> getThisTimeData(String workTaskNum) {
		List<InsPatrolDataVO> result = new ArrayList<InsPatrolDataVO>();

		for (Class<FreqData> clazz : classes) {
			try {
				IFreqData freqData = (IFreqData) clazz.newInstance();
				List<InsPatrolDataVO> lst = (List<InsPatrolDataVO>) freqData
						.getData(workTaskNum);

				if (lst.size() > 0) {
					result.addAll(lst);
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}

	
	/**
	 * 获取获取周期内已巡检数据
	 * 
	 * @return
	 */
	public List<InsPatrolDataVO> getWeekHadoData(String workTaskNum) {
		List<InsPatrolDataVO> result = new ArrayList<InsPatrolDataVO>();

		for (Class<FreqData> clazz : classes) {
			try {
				IFreqData freqData = (IFreqData) clazz.newInstance();
				List<InsPatrolDataVO> lst = (List<InsPatrolDataVO>) freqData.
						getWeekHadoData(workTaskNum);

				if (null != lst && lst.size() > 0) {
					result.addAll(lst);
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}
/*	*//**
	 * 获取本次等执行巡查数据
	 * 
	 * @return
	 *//*
	public List<InsPatrolDataVO> getThisTimeFreqCheckPoint(String workTaskNum) {	
		IFreqData freqData = new FreqCheckPoint();
		return (List<InsPatrolDataVO>) freqData.getData();
	}
	*//**
	 * 获取本次等执行巡查数据
	 * 
	 * @return
	 *//*
	public List<InsPatrolDataVO> getThisTimeCheckPoint(String workTaskNum) {
		IFreqData freqData = new FreqCheckPoint();
		return (List<InsPatrolDataVO>) freqData.getData(workTaskNum);
	}*/

	/**
	 * 检查该对象巡查对象本次巡查完毕之后是否本周期巡查已经结束
	 * 
	 * @param obj
	 * @return
	 */
	public Integer checkIsThisFreqEnd(InsPatrolDataVO obj) {
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
	}

	/**
	 * 获取等待巡查的路段缓存
	 * 
	 * @param id
	 * @return
	 */
	public Vector<InsPatrolDataVO> getWaitForDoDataCache(String id) {
		if (waitForDoDataCache.containsKey(id)) {
			return waitForDoDataCache.get(id);
		} else {
			return new Vector<InsPatrolDataVO>();
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
	public Vector<InsPatrolDataVO> getHadDoDataCache(String id) {
		if (hadDoDataCache.containsKey(id)) {
			return hadDoDataCache.get(id);
		} else {
			//return null;
			return new Vector<InsPatrolDataVO>();
		}
	}

	public void addHadDoDataCache(String id, InsPatrolDataVO data) {
		if (hadDoDataCache.containsKey(id)) {
			hadDoDataCache.get(id).add(data);
		} else {
			Vector<InsPatrolDataVO> v = new Vector<InsPatrolDataVO>();
			v.add(data);
			hadDoDataCache.put(id, v);
		}
	}

	public void removeWaitForDoDataCache(String id, InsPatrolDataVO data) {
		if (waitForDoDataCache.containsKey(id)) {
			waitForDoDataCache.get(id).remove(data);
		}
	}
	
	public void replaceWaitForDoDataCache(String id, InsPatrolDataVO data) {
		if (waitForDoDataCache.containsKey(id)) {
			waitForDoDataCache.get(id).remove(data);
			waitForDoDataCache.get(id).add(data);
		}
	}
	/**
	 * 获取当前所在路段
	 * 
	 * @return
	 */
	public InsPatrolOnsiteRecordExtVO getCurTaskLuDuan() {
		return curTaskLuDuan;
	}

	public void setCurTaskLuDuan(InsPatrolOnsiteRecordExtVO curTaskLuDuan) {
		this.curTaskLuDuan = curTaskLuDuan;
		notifyDataSetChange();
	}

	/**
	 * 距离最近签到点
	 * 
	 * @return
	 */
/*	public NearObject getCurTaskChkPnt() {
		return curTaskChkPnt;
	}

	public void setCurTaskChkPnt(NearObject curTaskChkPnt) {
		this.curTaskChkPnt = curTaskChkPnt;
		notifyDataSetChange();
	}*/

	/**
	 * 发出开始巡查动作
	 * 
	 * @param doId
	 *            每次执行产生的ID识别码,用UUID表达
	 * @param obj
	 *            本次本巡内容
	 */
	public void insBeginDo(Context context, String doId,String workTaskNum) {
		this.doId = doId;
		this.workTaskNum = workTaskNum;
		this.context = context;
		if (!inTranTaskCache.containsKey(doId)) {
			if (!"".equals(workTaskNum)){
				Vector<InsPatrolDataVO> v = new Vector<InsPatrolDataVO>();
				for (InsPatrolDataVO d : this.getThisTimeData(workTaskNum)) {
					v.add(d);
				}
				waitForDoDataCache.put(doId, v);
				
				//周期内已经巡查的
				Vector<InsPatrolDataVO> v1 = new Vector<InsPatrolDataVO>();
				for (InsPatrolDataVO d : this.getWeekHadoData(workTaskNum)) {
					v1.add(d);
				}
				hadDoDataCache.put(doId,v1);
				
	
/*				v = new Vector<InsPatrolDataVO>();
				for (InsPatrolDataVO d : this.getThisTimeCheckPoint(workTaskNum)) {
					v.add(d);
				}
				waitForDoChkPntCache.put(doId, v);*/
			}else{
				Vector<InsPatrolDataVO> v = new Vector<InsPatrolDataVO>();
				for (InsPatrolDataVO d : this.getThisTimeFreqData()) {
					v.add(d);
				}
				waitForDoDataCache.put(doId, v);
					
/*				v = new Vector<InsPatrolDataVO>();
				for (InsPatrolDataVO d : this.getThisTimeFreqData()) {
					v.add(d);
				}
				waitForDoChkPntCache.put(doId, v);*/
			}
			RoadTranceTaskExp workTran = new RoadTranceTaskExp(this,context, doId);
			inTranTaskCache.put(doId, workTran);
			workTran.start(0, 25000);
		} else {
			RoadTranceTaskExp workTran = inTranTaskCache.get(doId);
			workTran.cancel();
			workTran.start(0, 25000);
		}
	}

	//复位
	public void rest(){
		if (inTranTaskCache.containsKey(doId)) {
			if (!"".equals(workTaskNum)){
				Vector<InsPatrolDataVO> v = new Vector<InsPatrolDataVO>();
				for (InsPatrolDataVO d : this.getThisTimeData(workTaskNum)) {
					v.add(d);
				}
				waitForDoDataCache.remove(doId);
				waitForDoDataCache.put(doId, v);
	
/*				v = new Vector<InsPatrolDataVO>();
				for (InsPatrolDataVO d : this.getThisTimeCheckPoint(workTaskNum)) {
					v.add(d);
				}
				waitForDoChkPntCache.put(doId, v);*/
			}else{
				Vector<InsPatrolDataVO> v = new Vector<InsPatrolDataVO>();
				for (InsPatrolDataVO d : this.getThisTimeFreqData()) {
					v.add(d);
				}
				waitForDoDataCache.remove(doId);
				waitForDoDataCache.put(doId, v);
	
/*				v = new Vector<InsPatrolDataVO>();
				for (InsPatrolDataVO d : this.getThisTimeFreqData()) {
					v.add(d);
				}
				waitForDoChkPntCache.put(doId, v);*/
			}
/*			RoadTranceTask workTran = new RoadTranceTask(this,context, doId);
			inTranTaskCache.put(doId, workTran);
			workTran.start(0, 5000);*/
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
			RoadTranceTaskExp workTran = inTranTaskCache.get(doId);
			workTran.end();

			inTranTaskCache.remove(doId);
			waitForDoDataCache.remove(doId);
			//waitForDoChkPntCache.remove(doId);

			curTaskLuDuan = null;
			//curTaskChkPnt = null;
		}
	}

	
/*	public void pause(InsPatrolOnsiteRecordExtVO pauseRoad){
		RoadTranceTaskExp workTran = inTranTaskCache.get(doId);
		workTran.pause(pauseRoad);
	}
	public void goNo(InsPatrolOnsiteRecordExtVO pauseRoad){
		RoadTranceTaskExp workTran = inTranTaskCache.get(doId);
		workTran.goNo(pauseRoad);
	}*/
	
	public void insPause(){
		RoadTranceTaskExp workTran = inTranTaskCache.get(doId);
		workTran.setPaused(true);
	}
	public void insGoNo(){
		RoadTranceTaskExp workTran = inTranTaskCache.get(doId);
		workTran.setPaused(false);
	}
	public void curObjOp(InsPatrolOnsiteRecordExtVO object,int state,	String memo){
		RoadTranceTaskExp workTran = inTranTaskCache.get(doId);
		workTran.nowDoRoadOp(object,state, memo);
	}
}
