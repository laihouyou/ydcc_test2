package com.movementinsome.app.remind.road;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.movementinsome.app.remind.FreqData;
import com.movementinsome.app.remind.FreqObject;
import com.movementinsome.app.remind.IFreqData;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsPlanTaskVO;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class FreqCheckPoint extends FreqData implements IFreqData {

	final long CHECK_OUT_DIF = 1;
	
	@Override
	public List<InsPatrolDataVO> getData() {
		// TODO Auto-generated method stub
		List<InsPatrolDataVO> result = new ArrayList<InsPatrolDataVO>();
		List<InsPatrolDataVO> remove = new ArrayList();
		try {
			List<InsPlanTaskVO> lst = insPatrolTaskDao.queryForEq(
					"workTaskType", "PLAN_PATROL_CYCLE");
			for (InsPlanTaskVO task : lst) {
				List<InsPatrolDataVO> temp = insPatrolDataDao
						.query(queryPre(task.getWorkTaskNum()));
				for (InsPatrolDataVO data : temp) {
					Integer choice = 0;
					choice = checkInThisFreq(changChkObj(data));
					if (choice!=1) {
						//result.remove(data);
						remove.add(data);
					}
				}

				if (temp.size() > 0) {
					result.addAll(temp);
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

	@Override
	public List<InsPatrolDataVO> getData(String workTaskNum) {
		// TODO Auto-generated method stub
		List<InsPatrolDataVO> remove = new ArrayList();
		try {
			List<InsPatrolDataVO> result = insPatrolDataDao
					.query(queryPre(workTaskNum));
			for (InsPatrolDataVO data : result) {
				Integer choice = 0;

				choice = checkInThisFreq(changChkObj(data));
				if (choice!=1) {
					//result.remove(data);
					remove.add(data);
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

	@Override
	public Integer checkInThisFreq(FreqObject data) {
		// TODO Auto-generated method stub
		
		if (data.getLastInsDateStr() == null||"".equals(data.getLastInsDateStr())){
			return 1;
		}else if (getHours(getToDate2(),data.getLastInsDateStr())<=CHECK_OUT_DIF){  //签到1小时以内,不列入提示范围
			return 0;
		}else
			return 0;
	}

	@Override
	public PreparedQuery<InsPatrolDataVO> queryPre(String workTaskNum)
			throws SQLException {
		// TODO Auto-generated method stub
		QueryBuilder<InsPatrolDataVO, Long> queryBuilder = insPatrolDataDao
				.queryBuilder();
		Where<InsPatrolDataVO, Long> where = queryBuilder.where();
		where.eq("workTaskNum", workTaskNum);
		where.and();
		where.eq("facType", "签到点");
/*		where.and();
		//where.not();
		where.eq("lastInsDateStr", getToDate()); // 当天没有巡过的
*/		// queryBuilder.orderBy("id", true);
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
