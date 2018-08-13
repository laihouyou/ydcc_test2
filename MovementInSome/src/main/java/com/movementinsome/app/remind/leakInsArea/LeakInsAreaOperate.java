package com.movementinsome.app.remind.leakInsArea;

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
import com.movementinsome.database.vo.BsLeakInsArea;

public class LeakInsAreaOperate extends PlanOperate implements IPlanOperate {
	public static final Class<FreqData>[] classes = new Class[] {
			FreqDataDefaultDay.class, FreqDataDay.class, FreqDataWeek.class,
			FreqDataDefaultMonth.class, FreqDataDefaultYear.class,
			FreqDataWorkDay.class,FreqDataDefaultWeek.class };

	// static InsPlanManage insPlanManage;
	static Map<String, LeakInsAreaTranceTask> inTranTaskCache = new HashMap<String, LeakInsAreaTranceTask>(); // 启动执行路段判断线程
	// 待巡路段缓存
	static Map<String, Vector<BsLeakInsArea>> waitForDoDataCache = new HashMap<String, Vector<BsLeakInsArea>>();// 本次等执行数据
	// 已巡路段缓存
	static Map<String, Vector<BsLeakInsArea>> hadDoDataCache = new HashMap<String, Vector<BsLeakInsArea>>();// 本次等执行数据

	// 当前最近签到点
	static NearObject nearSite;

	/**
	 * 获取所有周期性数据
	 * 
	 * @return
	 */
	public List<BsLeakInsArea> getAllFreqData() {
		List<BsLeakInsArea> result = new ArrayList<BsLeakInsArea>();
		try {
			List<BsLeakInsArea> lst = bsLeakInsAreaDao.queryForAll();
			for (BsLeakInsArea task : lst) {
				List<BsLeakInsArea> temp = bsLeakInsAreaDao.queryForEq(
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
	public List<BsLeakInsArea> getAllData(String workTaskNum) {
		try {
			return bsLeakInsAreaDao.queryForEq("workTaskNum", "workTaskNum");
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
	public List<BsLeakInsArea> getThisTimeFreqData() {
		List<BsLeakInsArea> result = new ArrayList<BsLeakInsArea>();

		for (Class<FreqData> clazz : classes) {
			try {
				List<BsLeakInsArea> lstTask;

				lstTask = bsLeakInsAreaDao.queryForAll();

				for (BsLeakInsArea task : lstTask) {
					IFreqData freqData = (IFreqData) clazz.newInstance();
					List<BsLeakInsArea> lst = (List<BsLeakInsArea>) freqData
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
	public List<BsLeakInsArea> getThisTimeData(String workTaskNum) {
		List<BsLeakInsArea> result = new ArrayList<BsLeakInsArea>();

		for (Class<FreqData> clazz : classes) {
			try {
				IFreqData freqData = (IFreqData) clazz.newInstance();
				List<BsLeakInsArea> lst = (List<BsLeakInsArea>) freqData
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
	public Integer checkIsThisFreqEnd(BsLeakInsArea obj) {
		// List<BsLeakInsArea> result = new
		// ArrayList<BsLeakInsArea>();
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
	public Vector<BsLeakInsArea> getWaitForDoDataCache(String id) {
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
	public Vector<BsLeakInsArea> getHadDoDataCache(String id) {
		if (hadDoDataCache.containsKey(id)) {
			return hadDoDataCache.get(id);
		} else {
			return null;
		}
	}

	public List<BsLeakInsArea> getHadDoData(String workTaskNum) {
		List<BsLeakInsArea> BsLeakInsAreaList = null;
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("workTaskNum", workTaskNum);
		m.put("insCount", -1);
		try {
			BsLeakInsAreaList = bsLeakInsAreaDao.queryForFieldValues(m);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return BsLeakInsAreaList;
	}

	public void addHadDoDataCache(String id, BsLeakInsArea data) {
		if (hadDoDataCache.containsKey(id)) {
			hadDoDataCache.get(id).add(data);
		} else {
			Vector<BsLeakInsArea> v = new Vector<BsLeakInsArea>();
			v.add(data);
			hadDoDataCache.put(id, v);
		}
	}

	public void removeWaitForDoDataCache(String id, BsLeakInsArea data) {
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
				Vector<BsLeakInsArea> v = new Vector<BsLeakInsArea>();
				for (BsLeakInsArea d : this.getThisTimeData(workTaskNum)) {
					v.add(d);
				}
				waitForDoDataCache.put(doId, v);
			} else {
				Vector<BsLeakInsArea> v = new Vector<BsLeakInsArea>();
				for (BsLeakInsArea d : this.getThisTimeFreqData()) {
					v.add(d);
				}
				waitForDoDataCache.put(doId, v);
			}
			LeakInsAreaTranceTask workTran = new LeakInsAreaTranceTask(
					this, context, doId);
			inTranTaskCache.put(doId, workTran);
			workTran.start(0, 5000);
		} else {
			LeakInsAreaTranceTask workTran = inTranTaskCache.get(doId);
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
			LeakInsAreaTranceTask workTran = inTranTaskCache.get(doId);
			workTran.end();

			inTranTaskCache.remove(doId);
			waitForDoDataCache.remove(doId);

			nearSite = null;
		}
	}
}
