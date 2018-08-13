package com.movementinsome.app.remind;

import java.util.HashMap;
import java.util.Map;

import com.movementinsome.app.remind.area.PatrolAreaOperate;
import com.movementinsome.app.remind.emphasisInsArea.EmphasisInsAreaOperate;
import com.movementinsome.app.remind.fac.FacPlanOperate;
import com.movementinsome.app.remind.facPC.PcChkPntPlanOperate;
import com.movementinsome.app.remind.fireplug.FireplugPlanOperate;
import com.movementinsome.app.remind.insFacInfo.InsFacInfoOperate;
import com.movementinsome.app.remind.keypnt.KeypntPlanOperate;
import com.movementinsome.app.remind.leakInsArea.LeakInsAreaOperate;
import com.movementinsome.app.remind.road.ChkPntPlanOperate;
import com.movementinsome.app.remind.road.RoadPlanOperate;
import com.movementinsome.app.remind.routineInsArea.RoutineInsAreaOperate;
import com.movementinsome.app.remind.site.SitePlanOperate;
import com.movementinsome.app.remind.supervisionPoint.SupervisionPointPlanOperate;
import com.movementinsome.app.remind.valve.ValvePlanOperate;

public class InsPlanManage {

	public static enum PLAN_MANAGE_TYPE{
		ROAD,
		CHKPOINT,
		PC_CHKPOINT,
		SITE,
		KEYPOINT,
		VALVE,
		FIREPLUG,
		FAC,
		SUPERVISION_POINT,
		LEAK_INS_AREA,
		EMPHASIS_INS_AREA,
		FAC_INFO,
		ROUTINE_INS_AREA,
		ZS_PATROL_AREA,
		ZS_ROAD,
		PC_ROAD
	}
	
	static Map<PLAN_MANAGE_TYPE,IPlanOperate> planOperates = new HashMap<PLAN_MANAGE_TYPE,IPlanOperate>();
	
	public static IPlanOperate getInstance(PLAN_MANAGE_TYPE type){
		if (planOperates.containsKey(type)){
			return planOperates.get(type);
		}else{
			IPlanOperate planOperate = null;
			if (type==PLAN_MANAGE_TYPE.ROAD){
				planOperate = new RoadPlanOperate();
			}else if (type==PLAN_MANAGE_TYPE.CHKPOINT){
				planOperate = new ChkPntPlanOperate();
			}else if (type==PLAN_MANAGE_TYPE.PC_CHKPOINT){
				planOperate = new PcChkPntPlanOperate();
			}else if (type==PLAN_MANAGE_TYPE.SITE){
				planOperate = new SitePlanOperate();
			}else if(type==PLAN_MANAGE_TYPE.KEYPOINT){
				planOperate=new KeypntPlanOperate();
			}else if(type==PLAN_MANAGE_TYPE.VALVE){
				planOperate=new ValvePlanOperate();
			}else if(type==PLAN_MANAGE_TYPE.FIREPLUG){
				planOperate=new FireplugPlanOperate();
			}else if(type==PLAN_MANAGE_TYPE.FAC){
				planOperate=new FacPlanOperate();
			}else if(type == PLAN_MANAGE_TYPE.SUPERVISION_POINT){
				planOperate = new SupervisionPointPlanOperate();
			}else if(type == PLAN_MANAGE_TYPE.LEAK_INS_AREA){
				planOperate =new LeakInsAreaOperate();
			}else if(type == PLAN_MANAGE_TYPE.EMPHASIS_INS_AREA){
				planOperate =new EmphasisInsAreaOperate();
			}else if(type == PLAN_MANAGE_TYPE.FAC_INFO){
				planOperate = new InsFacInfoOperate();
			}else if(type == PLAN_MANAGE_TYPE.ROUTINE_INS_AREA){
				planOperate = new RoutineInsAreaOperate();
			}else if(type == PLAN_MANAGE_TYPE.ZS_PATROL_AREA){
				planOperate = new PatrolAreaOperate();
			}else if(type == PLAN_MANAGE_TYPE.ZS_ROAD){
				planOperate = new com.movementinsome.app.remind.roadZS.RoadPlanOperate();
			}else if(type == PLAN_MANAGE_TYPE.PC_ROAD){
				planOperate = new com.movementinsome.app.remind.roadPC.PcRoadPlanOperate();
			}
			planOperates.put(type, planOperate);

			return planOperate;
		}

	}
}
