package com.movementinsome.app.remind.supervisionPoint;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.movementinsome.app.remind.FreqData;
import com.movementinsome.app.remind.FreqObject;
import com.movementinsome.app.remind.IFreqData;
import com.movementinsome.database.vo.BsSupervisionPoint;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class FreqDataDefaultWeek  extends FreqData implements IFreqData {
	@Override
	public List<BsSupervisionPoint> getData() {
		// TODO Auto-generated method stub
		List<BsSupervisionPoint> result = new ArrayList<BsSupervisionPoint>();
		try {
			List<BsSupervisionPoint> lst = bsSupervisionPointDao.queryForEq(
					"spType", "");
			for (BsSupervisionPoint task : lst) {
				List<BsSupervisionPoint> temp = bsSupervisionPointDao
						.query(queryPre(task.getWorkTaskNum()));
				for (BsSupervisionPoint data : temp) {
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
	public List<BsSupervisionPoint> getData(String workTaskNum) {
		// TODO Auto-generated method stub
		try {
			List<BsSupervisionPoint> result = bsSupervisionPointDao
					.query(queryPre(workTaskNum));
			List<BsSupervisionPoint> remove = new ArrayList();

			for (BsSupervisionPoint data : result) {
				Integer choice = 0;
				choice = checkInThisFreq(changChkObj(data));
				if (choice<1) {
					//result.remove(data);
					remove.add(data);
				}
/*				if(choice==2){
					data.setInsCount((long) 0);
					bsSupervisionPointDao.update(data);
				}*/
			}
			if (remove.size()>0){
				for(BsSupervisionPoint data : remove){
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

	public PreparedQuery<BsSupervisionPoint> queryPre(String workTaskNum)
			throws SQLException {
		QueryBuilder<BsSupervisionPoint, Long> queryBuilder = bsSupervisionPointDao
				.queryBuilder();
		Where<BsSupervisionPoint, Long> where = queryBuilder.where();
		where.eq("workTaskNum", workTaskNum);
		where.and();
/*		where.eq("facType", "路段");
		where.and();*/
		where.eq("frequencyType", "DEFAULT");
		where.and();
		where.like("frequency", "%周");
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
		if (data.getLastInsDateStr()==null || "".equals(data.getLastInsDateStr())){ //未巡过
			choice = 1;
		}else{
			//N月/N次
			String[] freq = data.getFrequency().split("\\,");
			String[] lastDate = data.getLastInsDateStr().split("\\-");
			if (lastDate[1].length()<2){
				lastDate[1] = "0"+lastDate[1];
			}
			//int lym = Integer.valueOf(lastDate[0]+lastDate[1]); //最后巡查年月
			
			int nym = Integer.valueOf(freq[0]); //n年月单位值
			int dym = (getCurYear()-Integer.valueOf(lastDate[0]))*12 +getCurMonth()-Integer.valueOf(lastDate[1])+1; //年月差

			if (getToYWeek(data.getLastInsDateStr()).equals(getCurYWeek())){ //在周期内
				if(nym-dym>=0){
					if (data.getInsCount()==-1){ //巡查次数已够
						choice = -1;
					}else{
						
						if (data.getFrequencyNumber()-data.getInsCount()==0){  //如果巡查次已够不需要再巡查
							choice = -1;
						}else if (data.getFrequencyNumber()>data.getInsCount()){  //如果巡查次未够
							if (data.getInsCount()==0){  //如果还没有巡查过
								choice = 1;
							} else{ 
								double avg = round((data.getFrequencyNumber()*1.00)/nym);  //求单位平均巡查次数
								if((int)(avg*dym-data.getInsCount())>0){  //如果平均巡查次磊累积次
									choice = 1;
								}else{
									choice = 0;
								}
							}
						}else{
							choice = -1;
						}
					}
				}else{//本周没有巡,巡查次数复为0
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
		return "DEFAULT_周";
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
