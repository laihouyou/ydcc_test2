package com.movementinsome.app.remind.keypnt;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.movementinsome.app.remind.FreqData;
import com.movementinsome.app.remind.FreqObject;
import com.movementinsome.app.remind.IFreqData;
import com.movementinsome.database.vo.InsKeyPointPatrolData;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class FreqDataTem extends FreqData implements IFreqData {
	// 获取周期性巡检计划路段数据
	@Override
	public List<InsKeyPointPatrolData> getData() {
		// TODO Auto-generated method stub
		List<InsKeyPointPatrolData> result = new ArrayList<InsKeyPointPatrolData>();
		// 获取当天日期
		int day = getCurday();
		List<InsKeyPointPatrolData> remove = new ArrayList();
		try {
			List<InsKeyPointPatrolData> lst = insKeyPointPatrolDataDao.queryForEq(
					"workTaskType", "PLAN_PATROL_CYCLE");
			for (InsKeyPointPatrolData task : lst) {
				List<InsKeyPointPatrolData> temp = insKeyPointPatrolDataDao
						.query(queryPre(task.getWorkTaskNum()));
				for (InsKeyPointPatrolData data : temp) {
					Integer choice = 0;
					choice = checkInThisFreq(changChkObj(data));
					if (choice<1) {
						//result.remove(data);
						remove.add(data);
					}
				}

				if (temp.size() > 0) {
					result.addAll(temp);
				}
			}
			if (remove.size()>0){
				for(InsKeyPointPatrolData data : remove){
					if (result.contains(data)){
						result.remove(data);
					}
				}
			}
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return null;
		}
	}

	// 获取指定任务的巡检计划路段数据
	@Override
	public List<InsKeyPointPatrolData> getData(String workTaskNum) {
		// TODO Auto-generated method stub
		// 获取当天日期
		int day = getCurday();
		List<InsKeyPointPatrolData> remove = new ArrayList();
		try {
			List<InsKeyPointPatrolData> result = insKeyPointPatrolDataDao
					.query(queryPre(workTaskNum));
			for (InsKeyPointPatrolData data : result) {
				Integer choice = 0;
				choice = checkInThisFreq(changChkObj(data));
				if (choice<1) {
					//result.remove(data);
					remove.add(data);
				}
/*				if(choice==2){
					data.setInsCount((long) 0);
					insKeyPointPatrolDataDao.update(data);
				}*/
			}
			if (remove.size()>0){
				for(InsKeyPointPatrolData data : remove){
					if (result.contains(data)){
						result.remove(data);
					}
				}
			}
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return null;
		}

	}

	public PreparedQuery<InsKeyPointPatrolData> queryPre(String workTaskNum)
			throws SQLException {
		QueryBuilder<InsKeyPointPatrolData, Long> queryBuilder = insKeyPointPatrolDataDao
				.queryBuilder();
		Where<InsKeyPointPatrolData, Long> where = queryBuilder.where();
		where.eq("workTaskNum", workTaskNum);
		where.and();
		//where.eq("facType", "路段");
		//where.and();
		where.eq("frequencyType", "TEM");
/*		where.and();
		//where.not();
		where.eq("lastInsDateStr", getToDate()); // 当天没有巡过的
*/		// queryBuilder.orderBy("id", true);
		return queryBuilder.prepare();
	}

	@Override
	public Integer checkInThisFreq(FreqObject data) {
		// TODO Auto-generated method stub
		Integer choice = 0;
		if (data.getInsCount()==-1){ //周期内巡查次数已够
			choice = -1;
		}else if (data.getInsCount()<data.getFrequencyNumber()){ //周期内巡查次数未够
			choice = 1;
		}else{
			choice = -1;
		}
		return choice;
	}

	@Override
	public String getClassFlag() {
		// TODO Auto-generated method stub
		return "TEM";
	}

	@Override
	public List<?> getWeekHadoData(String workTaskNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkDoInThisFreq(FreqObject obj) {
		// TODO Auto-generated method stub
		return false;
	}
}
