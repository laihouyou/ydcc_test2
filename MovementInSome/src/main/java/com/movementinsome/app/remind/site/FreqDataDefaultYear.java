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

public class FreqDataDefaultYear extends FreqData implements IFreqData {
	
	@Override
	public List<InsSiteManage> getData() {
		// TODO Auto-generated method stub
		List<InsSiteManage> result = new ArrayList<InsSiteManage>();

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
						temp.remove(data);
					}
				}
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

	@Override
	public List<InsSiteManage> getData(String workTaskNum) {
		// TODO Auto-generated method stub
		//int week = getWeek();

		try {
			List<InsSiteManage> result = insSiteManageDao
					.query(queryPre(workTaskNum));
			List<InsSiteManage> remove = new ArrayList();
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
		where.eq("frequencyType", "DEFAULT");
		where.and();
		where.like("frequency", "%年");
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
		
		if (data.getLastInsDateStr()==null || "".equals(data.getLastInsDateStr())){  //未巡，则可以巡
			choice = 1;
		}else{
			//N年/N次
			String[] freq = data.getFrequency().split("\\,");
			int ly = Integer.valueOf(data.getLastInsDateStr().split("\\-")[0]); //最后巡查年度
			int ny = Integer.valueOf(freq[0]); //n年单位值
			int dy = getCurYear()-ly +1; //年度差
			
			if (getToYear(data.getLastInsDateStr()).equals(String.valueOf(getCurYear()))){  //在周期内，则需要判断次数
				if(ny-dy>=0){
					if (data.getInsCount()==-1){ //周期内巡查次数已够
						choice = -1;
					}else{
						if ((data.getFrequencyNumber()-data.getInsCount())==0){  //如果巡查次已够不需要再巡查
							choice = -1;
						}else if (data.getFrequencyNumber()>data.getInsCount()){  //如果巡查次未够
							if (data.getInsCount()==0){  //如果还没有巡查过
								choice = 1;
								//break;
							} else{ 
								double avg = round((data.getFrequencyNumber()*1.00)/ny);  //求单位平均巡查次数
								if((int)(avg*dy-data.getInsCount())>0){  //如果平均巡查次磊累积次
									choice = 1;
								}else{
									choice = 0;
								}
							}
						}else{
							choice = -1;
						}
					}
				}else{ //本周期没巡,巡查次数复为0
					choice = 2;
				}
			}else{
				choice = 2;
			}
		}
		return choice;
	}

	@Override
	public String getClassFlag() {
		// TODO Auto-generated method stub
		return "DEFAULT_年";
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
