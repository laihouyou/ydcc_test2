package com.movementinsome.app.dataop;

import android.content.Context;

import com.movementinsome.app.pub.Constant;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.initial.model.Module;

import java.util.List;

public class DataOperaterFactory {

	public static IDataOperater createDataOp(Context context,
			InsTablePushTaskVo insTablePushTaskVo, String guid,List<Module> lstModule) {
		IDataOperater dataOp = null;

		if (Constant.CYCLE_PLAN.equals(insTablePushTaskVo.getTitle())
				|| Constant.INTERIM_PLAN.equals(insTablePushTaskVo.getTitle())) {
			// initRoad();
			dataOp = new RoadDataOperater(context, insTablePushTaskVo, guid,lstModule);
		} else if (Constant.PLAN_VALVE_CYCLE.equals(insTablePushTaskVo.getTitle())
				|| Constant.PLAN_VALVE_TEMPORARY.equals(insTablePushTaskVo.getTitle())) {
			
			dataOp = new ValveDataOperater(context, insTablePushTaskVo, guid,lstModule);
			// initValve();
		}else if(// 中山
				Constant.PLAN_FAC_ZS_CYCLE.equals(insTablePushTaskVo.getTitle())
				|| Constant.PLAN_FAC_ZS_TEMPORARY.equals(insTablePushTaskVo.getTitle())
				|| Constant.PLAN_TIEPAI_CYCLE.equals(insTablePushTaskVo.getTitle())
				|| Constant.PLAN_TIEPAI_TEMPORARY.equals(insTablePushTaskVo.getTitle())){
			dataOp = new ZsPollingOperater(context, insTablePushTaskVo, guid,lstModule);
			
		}else if( Constant.PLAN_PATROL_SCHEDULE.equals(insTablePushTaskVo.getTitle())){
			dataOp = new ZsCycleRoadDataOperater(context, insTablePushTaskVo, guid,lstModule);
		}else if( Constant.PLAN_PATROL_ZS_CYCLE.equals(insTablePushTaskVo.getTitle())
				|| Constant.PLAN_PATROL_ZS_TEMPORARY.equals(insTablePushTaskVo.getTitle())){
			dataOp = new ZsRoadDataOperater(context, insTablePushTaskVo, guid, lstModule);
		} else if (Constant.PLAN_HYDRANT_CYCLE.equals(insTablePushTaskVo
				.getTitle())
				|| Constant.PLAN_HYDRANT_TEMPORARY.equals(insTablePushTaskVo
						.getTitle())) {
			dataOp = new FireplugDataOperater(context, insTablePushTaskVo, guid,lstModule);
		} else if (Constant.PLAN_CONSTRUCTION_CYCLE.equals(insTablePushTaskVo
				.getTitle())
				|| Constant.PLAN_CONSTRUCTION_TEMPORARY
						.equals(insTablePushTaskVo.getTitle())) {// 工地
			// initSite();
			dataOp = new SiteDataOperater(context, insTablePushTaskVo, guid,lstModule);
		} else if (Constant.XJGL_GJD_CYCLE
				.equals(insTablePushTaskVo.getTitle())
				|| Constant.XJGL_GJD_TEMPORARY.equals(insTablePushTaskVo
						.getTitle())) {// 关键点
			// initKeyPnt();
			dataOp = new KeyPntDataOperater(context, insTablePushTaskVo, guid,lstModule);
		}else if(Constant.PLAN_FAC_CYCLE.equals(insTablePushTaskVo.getTitle())
				|| Constant.PLAN_FAC_TEMPORARY.equals(insTablePushTaskVo.getTitle())){
			dataOp = new FacDataOperater(context, insTablePushTaskVo, guid, lstModule);
		}else if(Constant.ROUTINE_INS.equals(insTablePushTaskVo.getTitle())){
			dataOp = new SupervisionPointDataOperater(context, insTablePushTaskVo, guid, lstModule);
		}else if(Constant.LEAK_INS.equals(insTablePushTaskVo.getTitle())){
			dataOp = new LeakInsAreaOperater(context, insTablePushTaskVo, guid, lstModule);
		}else if(Constant.SPECIAL_INS.equals(insTablePushTaskVo.getTitle())){
			dataOp = new InsFacInfoOperater(context, insTablePushTaskVo, guid, lstModule);
		}else if(Constant.EMPHASIS_INS.equals(insTablePushTaskVo.getTitle())){
			dataOp = new EmphasisInsAreaOperater(context, insTablePushTaskVo, guid, lstModule);
		}else if(Constant.PLAN_PAICHA_TEMPORARY.equals(insTablePushTaskVo.getTitle())){
			dataOp = new PcRoadDataOperater(context, insTablePushTaskVo, guid, lstModule);
		}

		return dataOp;
	}

	public static IDataOperater createChkPntDataOp(Context context,
			InsTablePushTaskVo insTablePushTaskVo, String guid,List<Module> lstModule) {
		IDataOperater dataOp = null;
		
		if(Constant.PLAN_PAICHA_TEMPORARY.equals(insTablePushTaskVo.getTitle())){
			dataOp = new PcChkPntDataOperater(context,
					insTablePushTaskVo, guid,lstModule);
		}else{
			dataOp = new ChkPntDataOperater(context,
					insTablePushTaskVo, guid,lstModule);
		}
		return dataOp;
	}
}
