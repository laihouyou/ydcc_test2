package com.movementinsome.app.remind.insFacInfo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.movementinsome.app.remind.FreqData;
import com.movementinsome.app.remind.FreqObject;
import com.movementinsome.app.remind.IFreqData;
import com.movementinsome.database.vo.BsInsFacInfo;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class FreqDataDefaultDay extends FreqData implements IFreqData {

	@Override
	public List<BsInsFacInfo> getData() {
		// TODO Auto-generated method stub
		List<BsInsFacInfo> result = new ArrayList<BsInsFacInfo>();
		try {
			List<BsInsFacInfo> lst = bsInsFacInfoDao.queryForEq(
					"spType", "");
			for (BsInsFacInfo task : lst) {
				List<BsInsFacInfo> temp = bsInsFacInfoDao
						.query(queryPre(task.getWorkTaskNum()));
				for (BsInsFacInfo data : temp) {
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
	public List<BsInsFacInfo> getData(String workTaskNum) {
		// TODO Auto-generated method stub
		try {
			List<BsInsFacInfo> result = bsInsFacInfoDao
					.query(queryPre(workTaskNum));
			List<BsInsFacInfo> remove = new ArrayList();
			for (BsInsFacInfo data : result) {
				Integer choice = 0;
				choice = checkInThisFreq(changChkObj(data));
				if (choice<1) {
					//result.remove(data);
					remove.add(data);
				}
/*				if(choice==2){
					data.setInsCount((long) 0);
					bsInsFacInfoDao.update(data);
				}*/
			}
			if (remove.size()>0){
				for(BsInsFacInfo data : remove){
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

	public PreparedQuery<BsInsFacInfo> queryPre(String workTaskNum)
			throws SQLException {
		QueryBuilder<BsInsFacInfo, Long> queryBuilder = bsInsFacInfoDao
				.queryBuilder();
		Where<BsInsFacInfo, Long> where = queryBuilder.where();
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
