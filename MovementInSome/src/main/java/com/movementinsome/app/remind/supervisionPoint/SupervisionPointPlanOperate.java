package com.movementinsome.app.remind.supervisionPoint;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import android.content.Context;
import com.movementinsome.app.remind.FreqData;
import com.movementinsome.app.remind.IFreqData;
import com.movementinsome.app.remind.IPlanOperate;
import com.movementinsome.app.remind.PlanOperate;
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.database.vo.BsSupervisionPoint;

public class SupervisionPointPlanOperate extends PlanOperate implements
		IPlanOperate {
	public static final Class<FreqData>[] classes = new Class[] {
			FreqDataDefaultDay.class, FreqDataDay.class, FreqDataWeek.class,
			FreqDataDefaultMonth.class, FreqDataDefaultYear.class,
			FreqDataWorkDay.class ,FreqDataDefaultWeek.class};

	// static InsPlanManage insPlanManage;
	static Map<String, SupervisionPointTranceTask> inTranTaskCache = new HashMap<String, SupervisionPointTranceTask>(); // 启动执行路段判断线程
	// 待巡路段缓存
	static Map<String, Vector<BsSupervisionPoint>> waitForDoDataCache = new HashMap<String, Vector<BsSupervisionPoint>>();// 本次等执行数据
	// 已巡路段缓存
	static Map<String, Vector<BsSupervisionPoint>> hadDoDataCache = new HashMap<String, Vector<BsSupervisionPoint>>();// 本次等执行数据

	// 当前最近签到点
	static NearObject nearSite;

	/**
	 * 获取所有周期性数据
	 * 
	 * @return
	 */
	public List<BsSupervisionPoint> getAllFreqData() {
		List<BsSupervisionPoint> result = new ArrayList<BsSupervisionPoint>();
		try {
			List<BsSupervisionPoint> lst = bsSupervisionPointDao.queryForAll();
			for (BsSupervisionPoint task : lst) {
				List<BsSupervisionPoint> temp = bsSupervisionPointDao
						.queryForEq("workTaskNum", task.getWorkTaskNum());
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
	public List<BsSupervisionPoint> getAllData(String workTaskNum) {
		try {
			return bsSupervisionPointDao.queryForEq("workTaskNum",
					"workTaskNum");
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
	public List<BsSupervisionPoint> getThisTimeFreqData() {
		List<BsSupervisionPoint> result = new ArrayList<BsSupervisionPoint>();

		for (Class<FreqData> clazz : classes) {
			try {
				List<BsSupervisionPoint> lstTask;

				lstTask = bsSupervisionPointDao.queryForAll();

				for (BsSupervisionPoint task : lstTask) {
					IFreqData freqData = (IFreqData) clazz.newInstance();
					List<BsSupervisionPoint> lst = (List<BsSupervisionPoint>) freqData
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
	public List<BsSupervisionPoint> getThisTimeData(String workTaskNum) {
		List<BsSupervisionPoint> result = new ArrayList<BsSupervisionPoint>();

		for (Class<FreqData> clazz : classes) {
			try {
				IFreqData freqData = (IFreqData) clazz.newInstance();
				List<BsSupervisionPoint> lst = (List<BsSupervisionPoint>) freqData
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
	 * 检查该对象巡查对象本次巡查完毕之后是否本周期巡查已经结束
	 * 
	 * @param obj
	 * @return
	 */
	public Integer checkIsThisFreqEnd(BsSupervisionPoint obj) {
		// List<BsSupervisionPoint> result = new
		// ArrayList<BsSupervisionPoint>();
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
	public Vector<BsSupervisionPoint> getWaitForDoDataCache(String id) {
		if (waitForDoDataCache.containsKey(id)) {
			return waitForDoDataCache.get(id);
		} else {
			return null;
		}
	}

	/**
	 * 获取本次已巡查的路段缓存
	 * 
	 * @param id
	 * @return
	 */
	public Vector<BsSupervisionPoint> getHadDoDataCache(String id) {
		if (hadDoDataCache.containsKey(id)) {
			return hadDoDataCache.get(id);
		} else {
			return null;
		}
	}

	public List<BsSupervisionPoint> getHadDoData(String workTaskNum) {
		List<BsSupervisionPoint> BsSupervisionPointList = null;
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("workTaskNum", workTaskNum);
		m.put("insCount", -1);
		try {
			BsSupervisionPointList = bsSupervisionPointDao
					.queryForFieldValues(m);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return BsSupervisionPointList;
	}

	public void addHadDoDataCache(String id, BsSupervisionPoint data) {
		if (hadDoDataCache.containsKey(id)) {
			hadDoDataCache.get(id).add(data);
		} else {
			Vector<BsSupervisionPoint> v = new Vector<BsSupervisionPoint>();
			v.add(data);
			hadDoDataCache.put(id, v);
		}
	}

	public void removeWaitForDoDataCache(String id, BsSupervisionPoint data) {
		if (waitForDoDataCache.containsKey(id)) {
			waitForDoDataCache.get(id).remove(data);
		}
	}

	/**
	 * 距离最近签到点
	 * 
	 * @return
	 */
	public NearObject getNearSite() {
		return nearSite;
	}

	public void setNearSite(NearObject obj) {
		this.nearSite = obj;
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
	public void insBeginDo(Context context, String doId, String workTaskNum) {

		if (!inTranTaskCache.containsKey(doId)) {
			if (!"".equals(workTaskNum)) {
				Vector<BsSupervisionPoint> v = new Vector<BsSupervisionPoint>();
				for (BsSupervisionPoint d : this.getThisTimeData(workTaskNum)) {
					v.add(d);
				}
				waitForDoDataCache.put(doId, v);
			} else {
				Vector<BsSupervisionPoint> v = new Vector<BsSupervisionPoint>();
				for (BsSupervisionPoint d : this.getThisTimeFreqData()) {
					v.add(d);
				}
				waitForDoDataCache.put(doId, v);
			}
			SupervisionPointTranceTask workTran = new SupervisionPointTranceTask(
					this, context, doId);
			inTranTaskCache.put(doId, workTran);
			workTran.start(0, 5000);
		} else {
			SupervisionPointTranceTask workTran = inTranTaskCache.get(doId);
			workTran.cancel();
			workTran.start(0, 5000);
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
			SupervisionPointTranceTask workTran = inTranTaskCache.get(doId);
			workTran.end();

			inTranTaskCache.remove(doId);
			waitForDoDataCache.remove(doId);

			nearSite = null;
		}
	}
}
