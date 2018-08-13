package com.movementinsome.app.remind.site;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.movementinsome.app.remind.FreqData;
import com.movementinsome.app.remind.FreqObject;
import com.movementinsome.app.remind.IFreqData;
import com.movementinsome.database.vo.InsSiteManage;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class FreqDataWeek extends FreqData implements IFreqData {

	@Override
	public List<InsSiteManage> getData() {
		// TODO Auto-generated method stub
		List<InsSiteManage> result = new ArrayList<InsSiteManage>();
		List<InsSiteManage> remove = new ArrayList();
		// 获取当天日期
		int week = getWeek();

		try {
			List<InsSiteManage> lst = insSiteManageDao.queryForEq(
					"workTaskType", "PLAN_PATROL_CYCLE");
			for (InsSiteManage task : lst) {
				List<InsSiteManage> temp = insSiteManageDao
						.query(queryPre(task.getWorkTaskNum()));
				for (InsSiteManage data : temp) {
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
				for(InsSiteManage data : remove){
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

	@Override
	public List<InsSiteManage> getData(String workTaskNum) {
		// TODO Auto-generated method stub
		// 获取当天日期
		int week = getWeek();

		List<InsSiteManage> remove = new ArrayList();
		try {
			List<InsSiteManage> result = insSiteManageDao
					.query(queryPre(workTaskNum));
			for (InsSiteManage data : result) {
				Integer choice = 0;
				choice = checkInThisFreq(changChkObj(data));
				if (choice<1) {
					//result.remove(data);
					remove.add(data);
				}
				if(choice==2){
					data.setInsCount((long) 0);
					insSiteManageDao.update(data);
				}
			}
			if (remove.size()>0){
				for(InsSiteManage data : remove){
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

	public PreparedQuery<InsSiteManage> queryPre(String workTaskNum)
			throws SQLException {
		QueryBuilder<InsSiteManage, Long> queryBuilder = insSiteManageDao
				.queryBuilder();
		Where<InsSiteManage, Long> where = queryBuilder.where();
		where.eq("workTaskNum", workTaskNum);
		where.and();
/*		where.eq("facType", "路段");
		where.and();*/
		where.eq("frequencyType", "WEEK");
/*		where.and();
		//where.not();
		where.eq("lastInsDateStr", getToDate()); // 当天没有巡过的
*/		// queryBuilder.orderBy("id", true);
		return queryBuilder.prepare();
	}

	@Override
	public Integer checkInThisFreq(FreqObject data) {
		// TODO Auto-generated method stub
		// 获取当天日期
		int week = getWeek();
		
		Integer choice = 0;
		String[] freq = data.getFrequency().split("\\,");
		if (freq != null) {

			for (String s : freq) {
				if (s.equals(String.valueOf(week))) {
					if (data.getLastInsDateStr()==null || "".equals(data.getLastInsDateStr())){ //没巡过
						choice = 1;
					}else if (date2String(data.getLastInsDateStr()).equals(getCurDate())){ //在本周期内，则需要判断次数
						if (data.getInsCount()==-1){ //周期内巡查次数已够
							choice = -1;
						}else if (data.getInsCount()<data.getFrequencyNumber()){ //周期内巡查次数未够
							choice = 1;
						}else{
							choice = -1;
						}
					}else{//本周期没巡,复位巡查次数为0
						choice = 2;
					}
					
					break;
				}
			}
		}
		return choice;
	}

	@Override
	public String getClassFlag() {
		// TODO Auto-generated method stub
		return 	"WEEK";
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
