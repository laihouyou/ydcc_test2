package com.movementinsome.app.remind.road;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.movementinsome.app.remind.FreqData;
import com.movementinsome.app.remind.FreqObject;
import com.movementinsome.app.remind.IFreqData;
import com.movementinsome.database.vo.HolidayManage;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class FreqDataWorkDay extends FreqData implements IFreqData {

	@Override
	public List<?> getData() {
		// TODO Auto-generated method stub
		List<InsPatrolDataVO> result = new ArrayList<InsPatrolDataVO>();
		// 获取当天日期
		int day = getCurday();
		List<InsPatrolDataVO> remove = new ArrayList();
		try {
			List<InsPatrolDataVO> lst = insPatrolDataDao.queryForEq(
					"workTaskType", "PLAN_PATROL_CYCLE");
			for (InsPatrolDataVO task : lst) {
				List<InsPatrolDataVO> temp = insPatrolDataDao
						.query(queryPre(task.getWorkTaskNum()));
				for (InsPatrolDataVO data : temp) {
					Integer choice = 0;
					choice = checkInThisFreq(changChkObj(data));
					if (choice < 1) {
						// result.remove(data);
						remove.add(data);
					}
				}

				if (temp.size() > 0) {
					result.addAll(temp);
				}
			}
			if (remove.size() > 0) {
				for (InsPatrolDataVO data : remove) {
					if (result.contains(data)) {
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

	@Override
	public List<?> getData(String workTaskNum) {
		// TODO Auto-generated method stub
		// 获取当天日期
		int day = getCurday();
		List<InsPatrolDataVO> remove = new ArrayList();
		try {
			List<InsPatrolDataVO> result = insPatrolDataDao
					.query(queryPre(workTaskNum));
			for (InsPatrolDataVO data : result) {
				Integer choice = 0;
				choice = checkInThisFreq(changChkObj(data));
				if (choice < 1) {
					// result.remove(data);
					remove.add(data);
				}
				if (choice == 2) {
					data.setInsCount((long) 0);
					insPatrolDataDao.update(data);
				}
			}
			if (remove.size() > 0) {
				for (InsPatrolDataVO data : remove) {
					if (result.contains(data)) {
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


	@Override
	public List<?> getWeekHadoData(String workTaskNum) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Integer checkInThisFreq(FreqObject data) {
		// TODO Auto-generated method stub
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("hmWorkState", 0);
		m.put("hmYear", getToYear());
		m.put("hmMonth", getToMonth());
		m.put("hmDay", getToDate());
		List<HolidayManage> hm=null;
		
		try {
			hm=holidayManageDao.queryForFieldValues(m);
			if(hm!=null&&hm.size()>0){
				// 今天非工作日
				return 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int day = getCurday();
		// 记录
		int count = 0;
		for(int i=1;i<=day;++i){
			m = new HashMap<String, Object>();
			String hmYear = getToYear();
			String hmMonth = getToMonth();
			String hmDay = i>=10 ? i+"" : 0+""+i;
			m.put("hmYear", hmYear);
			m.put("hmMonth", hmMonth);
			m.put("hmDay", hmDay);
			try {
				hm=holidayManageDao.queryForFieldValues(m);
				if(hm!=null && hm.size()>0){
					if(hm.get(0).getHmWorkState()==1){
						++count;
					}
				}else{
					if(getWeek(hmYear+"-"+hmMonth+"-"+hmDay)<6){
						++count;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Integer choice = 0;
		String[] freq = data.getFrequency().split("\\,");
		if (freq != null) {
			for (String s : freq) {
				if (s.equals(String.valueOf(count))) {
					if (null == data.getLastInsDateStr() 
							|| "".equals(data.getLastInsDateStr())) { // 没巡过
						choice = 1;
					} else if (date2String(data.getLastInsDateStr()).equals(
							getCurDate())) { // 在本周期内，则需要判断次数
						if (null == data.getInsCount()){ //次数记录异常，巡检次数复位为0
							choice = 2;
						} else if (data.getInsCount() == -1) { // 周期内巡查次数已够
							choice = -1;
						} else if (data.getInsCount() < data
								.getFrequencyNumber()) { // 周期内巡查次数未够
							choice = 1;
						} else {
							choice = -1;
						}
					} else {// 本周期没巡,巡查次数复为0
						choice = 2;
					}

					break;
				}
			}
		}
		return choice;
	}

	@Override
	public boolean checkDoInThisFreq(FreqObject data) {
		// TODO Auto-generated method stub
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("hmWorkState", 0);
		m.put("hmYear", getToYear());
		m.put("hmMonth", getToMonth());
		m.put("hmDay", getToDate());
		List<HolidayManage> hm=null;
		
		try {
			hm=holidayManageDao.queryForFieldValues(m);
			if(hm!=null&&hm.size()>0){
				// 今天非工作日
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int day = getCurday();
		// 记录
		int count = 0;
		for(int i=1;i<=day;++i){
			m = new HashMap<String, Object>();
			String hmYear = getToYear();
			String hmMonth = getToMonth();
			String hmDay = i>=10 ? i+"" : 0+""+i;
			m.put("hmYear", hmYear);
			m.put("hmMonth", hmMonth);
			m.put("hmDay", hmDay);
			try {
				hm=holidayManageDao.queryForFieldValues(m);
				if(hm!=null && hm.size()>0){
					if(hm.get(0).getHmWorkState()==1){
						++count;
					}
				}else{
					if(getWeek(hmYear+"-"+hmMonth+"-"+hmDay)<6){
						++count;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Integer choice = 0;
		String[] freq = data.getFrequency().split("\\,");
		if (freq != null) {
			for (String s : freq) {
				if (s.equals(String.valueOf(count))) {
					if (null == data.getLastInsDateStr() 
							|| "".equals(data.getLastInsDateStr())) { // 没巡过
						return false;
					} else if (date2String(data.getLastInsDateStr()).equals(
							getCurDate())) { // 在本周期内，则需要判断次数
						if (null == data.getInsCount()){ //次数记录异常，巡检次数复位为0
							return false;
						} else if (data.getInsCount() == -1) { // 周期内巡查次数已够
							return true;
						} else if (data.getInsCount() < data
								.getFrequencyNumber()) { // 周期内巡查次数未够
							return true;
						} else {
							return true;
						}
					} else {// 本周期没巡,巡查次数复为0
						return false;
					}
				}
			}
		}
		return false;
	}
	
	
	@Override
	public PreparedQuery<InsPatrolDataVO> queryPre(String workTaskNum)
			throws SQLException {
		QueryBuilder<InsPatrolDataVO, Long> queryBuilder = insPatrolDataDao
				.queryBuilder();
		Where<InsPatrolDataVO, Long> where = queryBuilder.where();
		where.eq("workTaskNum", workTaskNum);
		where.and();

		where.eq("frequencyType", "WORKDAY");
		/*
		 * where.and(); //where.not(); where.eq("lastInsDateStr", getToDate());
		 * // 当天没有巡过的
		 */// queryBuilder.orderBy("id", true);
		return queryBuilder.prepare();
	}

	@Override
	public String getClassFlag() {
		// TODO Auto-generated method stub
		return null;
	}


}
