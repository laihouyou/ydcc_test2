package com.movementinsome.app.remind.fireplug;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.movementinsome.app.remind.FreqData;
import com.movementinsome.app.remind.FreqObject;
import com.movementinsome.app.remind.IFreqData;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class FreqDataDefaultDay extends FreqData implements IFreqData {

	@Override
	public List<InsPatrolDataVO> getData() {
		// TODO Auto-generated method stub
		List<InsPatrolDataVO> result = new ArrayList<InsPatrolDataVO>();
		try {
			List<InsPatrolDataVO> lst = insPatrolDataDao.queryForEq(
					"workTaskType", "PLAN_HYDRANT_CYCLE");
			for (InsPatrolDataVO task : lst) {
				List<InsPatrolDataVO> temp = insPatrolDataDao
						.query(queryPre(task.getWorkTaskNum()));
				for (InsPatrolDataVO data : temp) {
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
	public List<InsPatrolDataVO> getData(String workTaskNum) {
		// TODO Auto-generated method stub
		try {
			List<InsPatrolDataVO> result = insPatrolDataDao
					.query(queryPre(workTaskNum));
			List<InsPatrolDataVO> remove = new ArrayList();
			for (InsPatrolDataVO data : result) {
				Integer choice = 0;
				choice = checkInThisFreq(changChkObj(data));
				if (choice<1) {
					//result.remove(data);
					remove.add(data);
				}
				if(choice==2){
					data.setInsCount((long) 0);
					insPatrolDataDao.update(data);
				}
			}
			if (remove.size()>0){
				for(InsPatrolDataVO data : remove){
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

	public PreparedQuery<InsPatrolDataVO> queryPre(String workTaskNum)
			throws SQLException {
		QueryBuilder<InsPatrolDataVO, Long> queryBuilder = insPatrolDataDao
				.queryBuilder();
		Where<InsPatrolDataVO, Long> where = queryBuilder.where();
		where.eq("workTaskNum", workTaskNum);
		where.and();
/*		where.eq("facType", "路段");
		where.and();*/
		where.eq("frequencyType", "DEFAULT");
		where.and();
		where.like("frequency", "%日");
		//where.and();
		//where.not();
		//where.eq("lastInsDateStr", getToDate()); // 当天没有巡过的
		// queryBuilder.orderBy("id", true);
		return queryBuilder.prepare();
	}

	@Override
	public Integer checkInThisFreq(FreqObject data) {
		// TODO Auto-generated method stub
		Integer choice = 0;

		
		if (data.getLastInsDateStr()==null || "".equals(data.getLastInsDateStr())){//未巡过
			choice = 1;
		}else{
			//N日/N次
			String[] freq = data.getFrequency().split("\\,");
			long nymd = Integer.valueOf(freq[0]); //n年月单位值
			long dymd = getDaySub(data.getLastInsDateStr(),getCurDate()) +1; //年月差
			
			if (date2String(data.getLastInsDateStr()).equals(getCurDate())){//在周期内
				if(nymd-dymd>=0){
					if (data.getInsCount()==-1){ //已巡够次数
						choice = -1;
					}else{
						
						if ((data.getFrequencyNumber()-data.getInsCount())==0){  //如果巡查次已够不需要再巡查
							choice = -1;
						}else if (data.getFrequencyNumber()>data.getInsCount()){  //如果巡查次未够
							if (data.getInsCount()==0){  //如果还没有巡查过
								choice = 1;
							} else{ 
								double avg = round((data.getFrequencyNumber()*1.00)/nymd);  //求单位平均巡查次数
								if((int)(avg*dymd-data.getInsCount())>0){  //如果平均巡查次磊累积次
									choice = 1;
								}else{
									choice = 0;
								}
							}
						}else{
							choice = -1;
						}
					}
				}else{//本周期没巡过,巡查次数复为0
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
		return 	"DEFAULT_日";
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
