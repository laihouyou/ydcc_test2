package com.movementinsome.app.remind.leakInsArea;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.movementinsome.app.remind.FreqData;
import com.movementinsome.app.remind.FreqObject;
import com.movementinsome.app.remind.IFreqData;
import com.movementinsome.database.vo.HolidayManage;
import com.movementinsome.database.vo.BsLeakInsArea;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class FreqDataWorkDay extends FreqData implements IFreqData {

	@Override
	public List<?> getData() {
		// TODO Auto-generated method stub
		List<BsLeakInsArea> result = new ArrayList<BsLeakInsArea>();
		// 获取当天日期
		int day = getCurday();
		List<BsLeakInsArea> remove = new ArrayList();
		try {
			List<BsLeakInsArea> lst = bsLeakInsAreaDao.queryForEq(
					"spType", "");
			for (BsLeakInsArea task : lst) {
				List<BsLeakInsArea> temp = bsLeakInsAreaDao
						.query(queryPre(task.getWorkTaskNum()));
				for (BsLeakInsArea data : temp) {
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
				for (BsLeakInsArea data : remove) {
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
		List<BsLeakInsArea> remove = new ArrayList();
		try {
			List<BsLeakInsArea> result = bsLeakInsAreaDao
					.query(queryPre(workTaskNum));
			for (BsLeakInsArea data : result) {
				Integer choice = 0;
				choice = checkInThisFreq(changChkObj(data));
				if (choice < 1) {
					// result.remove(data);
					remove.add(data);
				}
				if (choice == 2) {
					data.setInsCount((long) 0);
					bsLeakInsAreaDao.update(data);
				}
			}
			if (remove.size() > 0) {
				for (BsLeakInsArea data : remove) {
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
			String hmDay = i>10 ? i+"" : 0+""+i;
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
					if (data.getLastInsDateStr() == null
							|| "".equals(data.getLastInsDateStr())) { // 没巡过
						choice = 1;
					} else if (date2String(data.getLastInsDateStr()).equals(
							getCurDate())) { // 在本周期内，则需要判断次数
						if (data.getInsCount() == -1) { // 周期内巡查次数已够
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
	public PreparedQuery<BsLeakInsArea> queryPre(String workTaskNum)
			throws SQLException {
		QueryBuilder<BsLeakInsArea, Long> queryBuilder = bsLeakInsAreaDao
				.queryBuilder();
		Where<BsLeakInsArea, Long> where = queryBuilder.where();
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
