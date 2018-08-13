package com.movementinsome.app.remind.area;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.movementinsome.app.remind.FreqData;
import com.movementinsome.app.remind.FreqObject;
import com.movementinsome.app.remind.IFreqData;
import com.movementinsome.database.vo.InsPlanTaskVO;
import com.movementinsome.database.vo.InsPatrolAreaData;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class FreqDataCycleDay  extends FreqData implements IFreqData {

	@Override
	public List<InsPatrolAreaData> getData() {
		// TODO Auto-generated method stub
		List<InsPatrolAreaData> result = new ArrayList<InsPatrolAreaData>();
		try {
			Long workOrder = null;
			List<InsPatrolAreaData> lst = insPatrolAreaDataDao.queryForEq(
					"workTaskType", "PLAN_PATROL_CYCLE");
			for (InsPatrolAreaData task : lst) {
				List<InsPatrolAreaData> temp = insPatrolAreaDataDao
						.query(queryPre(task.getWorkTaskNum()));
				for (InsPatrolAreaData data : temp) {
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
	public List<InsPatrolAreaData> getData(String workTaskNum) {
		// TODO Auto-generated method stub
		try {
			List<InsPatrolAreaData> result = insPatrolAreaDataDao
					.query(queryPre(workTaskNum));
			List<InsPatrolAreaData> remove = new ArrayList();
			for (InsPatrolAreaData data : result) {
				Integer choice = 0;
				choice = checkInThisFreq(changChkObj(data));
				if (choice<1) {
					//result.remove(data);
					remove.add(data);
				}
				if(choice==2){
					data.setInsCount((long) 0);
					insPatrolAreaDataDao.update(data);
				}
			}
			if (remove.size()>0){
				for(InsPatrolAreaData data : remove){
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

	public PreparedQuery<InsPatrolAreaData> queryPre(String workTaskNum)
			throws SQLException {
		Long workOrder = null;
		List<InsPlanTaskVO> insPlanTaskVOList = insPatrolTaskDao.queryForEq("workTaskNum", workTaskNum);
		if(insPlanTaskVOList!=null&&insPlanTaskVOList.size()>0){
			InsPlanTaskVO insPlanTaskVO = insPlanTaskVOList.get(0);
			workOrder = insPlanTaskVO.getWorkOrder();
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("workTaskNum", workTaskNum);
			m.put("areaOrder", workOrder);
			List<InsPatrolAreaData> InsPatrolAreaDataList = insPatrolAreaDataDao.queryForFieldValues(m);
			if(InsPatrolAreaDataList!=null&&InsPatrolAreaDataList.size()>0){
				InsPatrolAreaData InsPatrolAreaData = InsPatrolAreaDataList.get(0);
				String frequency = InsPatrolAreaData.getFrequency();
				if(frequency!=null&&frequency.contains("日")){
					String lastInsDateStr = InsPatrolAreaData.getLastInsDateStr();
					if(lastInsDateStr!=null&&!"".equals(lastInsDateStr)){
						String[] s = lastInsDateStr.split("\\-");
						if(s.length==3){
							if(!s[2].equals(getToDate())||!s[0].equals(getToYear())||!s[1].equals(getToMonth())){
								List<InsPatrolAreaData> InsPatrolAreaDatas = insPatrolAreaDataDao.queryForEq("workTaskNum", workTaskNum);
								if(InsPatrolAreaDatas!=null){
									++workOrder;
									if(workOrder>InsPatrolAreaDatas.size()){
										workOrder=(long) 1;
									}
									insPlanTaskVO.setWorkOrder(workOrder);
									insPatrolTaskDao.update(insPlanTaskVO);
								}
							}
						}
					}
				}
			}
		}
		QueryBuilder<InsPatrolAreaData, Long> queryBuilder = insPatrolAreaDataDao
				.queryBuilder();
		Where<InsPatrolAreaData, Long> where = queryBuilder.where();
		where.eq("workTaskNum", workTaskNum);
		where.and();
/*		where.eq("facType", "路段");
		where.and();*/
		where.eq("frequencyType", "CYCLE");
		where.and();
		if(workOrder!=null){
			where.eq("areaOrder", workOrder);
			where.and();
		}
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
		return 	"CYCLE_日";
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
