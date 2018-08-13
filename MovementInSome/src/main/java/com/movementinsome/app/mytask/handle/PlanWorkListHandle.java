package com.movementinsome.app.mytask.handle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.mytask.ShowTaskActivity;
import com.movementinsome.app.mytask.ShowTaskMsgActivity;
import com.movementinsome.app.mytask.adapter.PlanWorkAdapter;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.pub.asynctask.RegionAnalyzeTask5;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.database.vo.BsEmphasisInsArea;
import com.movementinsome.database.vo.BsInsContentSettingVO;
import com.movementinsome.database.vo.BsInsFacInfo;
import com.movementinsome.database.vo.BsInsTypeSettingVO;
import com.movementinsome.database.vo.BsLeakInsArea;
import com.movementinsome.database.vo.BsPnInsTask;
import com.movementinsome.database.vo.BsRoutineInsArea;
import com.movementinsome.database.vo.BsSupervisionPoint;
import com.movementinsome.database.vo.InsCheckFacRoad;
import com.movementinsome.database.vo.InsKeyPointPatrolData;
import com.movementinsome.database.vo.InsPatrolAreaData;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsSiteManage;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.database.vo.TpconfigVO;
import com.movementinsome.easyform.formengineer.RunForm;
import com.movementinsome.kernel.initial.model.Module;
import com.movementinsome.kernel.util.MyDateTools;
import com.movementinsome.map.MapBizViewer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanWorkListHandle implements PlanWorkListBaseHandle{
	private InsTablePushTaskVo insTablePushTaskVo;
	private InsPatrolDataVO insPatrolDataVO;
	private InsSiteManage insSiteManage;
	private InsKeyPointPatrolData insKeyPointPatrolData;
	
	private BsSupervisionPoint bsSupervisionPoint;
	private BsRoutineInsArea bsRoutineInsArea;
	private BsInsFacInfo bsInsFacInfo;
	private BsEmphasisInsArea bsEmphasisInsArea;
	private BsLeakInsArea bsLeakInsArea;
	
	// 中山
	private InsPatrolAreaData insPatrolAreaData;
	
	//排查 
	private InsCheckFacRoad insCheckFacRoad;
	
	private String title;
	private Context context;
	private List<Module> lstModule;// 所有表单配置
	/*
	 * 	"PLAN_PATROL_CYCLE";// 巡检管理:计划巡检(路面)
		"PLAN_PATROL_TEMPORARY";// 巡检管理: 临时巡检(路面)
	
		"PLAN_KEY_POINT_CYCLE";// 计划关键点任务（周期计划）
		"PLAN_KEY_POINT_TEMPORARY";// 计划关键点任务（临时计划）
	
		"PLAN_CONSTRUCTION_CYCLE";// 计划工地巡查（周期计划）任务
		"PLAN_CONSTRUCTION_TEMPORARY";// 计划工地巡查（临时计划）任务
	
		"PLAN_VALVE_CYCLE";// 阀门计划巡查（周期计划）
		"PLAN_VALVE_TEMPORARY";// 阀门计划巡查（临时计划）
	
		"PLAN_HYDRANT_CYCLE";// 消火栓计划巡查（周期计划）
		"PLAN_HYDRANT_TEMPORARY";// 消火栓计划巡查（临时计划）任务
	
		"PLAN_FAC_CYCLE";// 西江计划巡查（周期计划）任务：PLAN_FAC_CYCLE，
		"PLAN_FAC_TEMPORARY";// 西江计划巡查（临时计划）任务：PLAN_FAC_TEMPORARY
	 * 
		"ROUTINE_INS";// 常规巡查BsSupervisionPoint,BsRoutineInsArea
		"SPECIAL_INS";// 专项巡查BsInsFacInfo
		"EMPHASIS_INS";// 重点区域巡查BsEmphasisInsArea
		"LEAK_INS";// 查漏BsLeakInsArea
		
		"PLAN_FAC_ZS_CYCLE";// 设施计划巡查（周期计划）任务：PLAN_FAC_CYCLE，
	  	"PLAN_FAC_ZS_TEMPORARY";// 设施计划巡查（临时计划）任务：PLAN_FAC_ZS_TEMPORARY，
	    "PLAN_TIEPAI_CYCLE";// 设施计划贴牌（周期计划）任务：PLAN_TIEPAI_CYCLE，
	    "PLAN_TIEPAI_TEMPORARY";// 设施计划贴牌（临时计划）任务：PLAN_TIEPAI_TEMPORARY，
	*/
	public PlanWorkListHandle(Context context,List<Module> lstModule,InsTablePushTaskVo insTablePushTaskVo,InsPatrolDataVO insPatrolDataVO
			,InsSiteManage insSiteManage,InsKeyPointPatrolData insKeyPointPatrolData
			,BsSupervisionPoint bsSupervisionPoint,BsRoutineInsArea bsRoutineInsArea
			,BsInsFacInfo bsInsFacInfo,BsEmphasisInsArea bsEmphasisInsArea,BsLeakInsArea bsLeakInsArea
			,InsPatrolAreaData insPatrolAreaData, InsCheckFacRoad insCheckFacRoad){
		this.insTablePushTaskVo = insTablePushTaskVo;
		this.insPatrolDataVO = insPatrolDataVO;
		this.insSiteManage = insSiteManage;
		this.insKeyPointPatrolData = insKeyPointPatrolData;
		this.bsSupervisionPoint = bsSupervisionPoint;
		this.bsRoutineInsArea = bsRoutineInsArea;
		this.bsInsFacInfo = bsInsFacInfo;
		this.bsEmphasisInsArea = bsEmphasisInsArea;
		this.bsLeakInsArea = bsLeakInsArea;
		this.context = context;
		this.lstModule = lstModule;
		this.insPatrolAreaData = insPatrolAreaData;
		this.insCheckFacRoad = insCheckFacRoad;
		
		if(insTablePushTaskVo!=null){
			title = insTablePushTaskVo.getTitle();
		}
	}
	@Override
	public void controlUI(Map<String, View> vs) {//巡检内部页面显示处理
		// TODO Auto-generated method stub
		TextView work_type_title = (TextView) vs.get("work_type_title");
		TextView work_message = (TextView) vs.get("work_message");
		Button work_ation_loc = (Button) vs.get("work_ation_loc");
		Button work_ation_msg = (Button) vs.get("work_ation_msg");
		Button work_ation_td = (Button) vs.get("work_ation_td");
		Button work_ation_fantask = (Button) vs.get("work_ation_fantask");
		LinearLayout planning_work_item = (LinearLayout) vs.get("planning_work_item");
		
		String insCount_s = "";
		if(Constant.CYCLE_PLAN.equals(title)||Constant.INTERIM_PLAN.equals(title)){
			if(insPatrolDataVO!=null){
				if("签到点".equals(insPatrolDataVO.getFacType())){
					work_type_title.setText(insPatrolDataVO.getName());
					work_message.setVisibility(View.VISIBLE);
					work_message.setText("签到点");
					work_ation_td.setVisibility(View.GONE);
					work_ation_loc.setVisibility(View.VISIBLE);
					
				}else if("路面".equals(insPatrolDataVO.getFacType())||"路段".equals(insPatrolDataVO.getFacType())){
					work_ation_td.setVisibility(View.GONE);
					work_ation_loc.setVisibility(View.GONE);
					work_message.setVisibility(View.VISIBLE);
					String lastInsDateStr=insPatrolDataVO.getLastInsDateStr();
					lastInsDateStr = lastInsDateStr==null ? "未巡" : lastInsDateStr;
					Long insCount=insPatrolDataVO.getInsCount();
					insCount=insCount==null?0:insCount;
					if (insCount == -1){
						insCount_s = "巡查次数已够";
					}else{
						insCount_s = String.valueOf(insCount.intValue());
					}
					

					work_message.setText(insPatrolDataVO.getFrequencyDesc());
					
					work_type_title.setText(insPatrolDataVO.getName()+insPatrolDataVO.getFacType()
								+"\n最后巡查日期:"+lastInsDateStr
								+"\n巡查次数:"+insCount_s);
					work_ation_loc.setVisibility(View.VISIBLE);
				}
			}
		}else if(Constant.XJGL_GJD_CYCLE.equals(title)||Constant.XJGL_GJD_TEMPORARY.equals(title)){
			if(insKeyPointPatrolData!=null){
				work_ation_loc.setVisibility(View.VISIBLE);
				work_message.setVisibility(View.VISIBLE);
				String lastInsDateStr=insKeyPointPatrolData.getLastInsDate();
				lastInsDateStr = lastInsDateStr==null ? "未巡" : lastInsDateStr;
				Long insCount=insKeyPointPatrolData.getInsCount();
				insCount=insCount==null?0:insCount;
				if (insCount == -1){
					insCount_s = "巡查次数已够";
				}else{
					insCount_s = String.valueOf(insCount.intValue());
				}
				work_type_title.setText("关键点名称:"+insKeyPointPatrolData.getKpaName()
							+"\n最后巡查日期:"+lastInsDateStr
							+"\n巡查次数:"+insCount_s);
				work_message.setText(insKeyPointPatrolData.getFrequencyDesc());
			}
		}else if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){
			if(insSiteManage!=null){
				work_ation_loc.setVisibility(View.VISIBLE);
				
				work_message.setVisibility(View.VISIBLE);
				String lastInsDateStr=insSiteManage.getLastInsDateStr();
				lastInsDateStr = lastInsDateStr==null ? "未巡" : lastInsDateStr;
				Long insCount=insSiteManage.getInsCount();
				insCount=insCount==null?0:insCount;
				if (insCount == -1){
					work_ation_td.setVisibility(View.GONE);
					insCount_s = "巡查次数已够";
				}else{
					work_ation_td.setVisibility(View.VISIBLE);
					insCount_s = String.valueOf(insCount.intValue());
				}
				work_type_title.setText("项目名称:"+insSiteManage.getProjectName()
							+"\n最后巡查日期:"+lastInsDateStr
							+"\n巡查次数:"+insCount_s);
				work_message.setText(insSiteManage.getFrequencyDesc());
				
				work_ation_msg.setVisibility(View.VISIBLE);
			}
		}else if(Constant.PLAN_VALVE_CYCLE.equals(title)||Constant.PLAN_VALVE_TEMPORARY.equals(title)
				||Constant.PLAN_HYDRANT_CYCLE.equals(title)||Constant.PLAN_HYDRANT_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				// 阀门和消火栓巡检
				Long insCount=insPatrolDataVO.getInsCount();
				insCount=insCount==null?0:insCount;
				if (insCount == -1){
					work_ation_td.setVisibility(View.GONE);
					insCount_s = "巡查次数已够";
				}else{
					work_ation_td.setVisibility(View.VISIBLE);
					insCount_s = String.valueOf(insCount.intValue());
				}
				
				work_message.setVisibility(View.VISIBLE);
				String lastInsDateStr=insPatrolDataVO.getLastInsDateStr();
				lastInsDateStr = lastInsDateStr==null ? "未巡" : lastInsDateStr;
				work_message.setText(insPatrolDataVO.getFrequencyDesc());
				
				work_type_title.setText(insPatrolDataVO.getName()+insPatrolDataVO.getFacType()
							+"\n最后巡查日期:"+lastInsDateStr
							+"\n巡查次数:"+insCount_s);
				work_ation_loc.setVisibility(View.VISIBLE);
			}
		}else if(Constant.PLAN_FAC_CYCLE.equals(title)||Constant.PLAN_FAC_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				Long insCount=insPatrolDataVO.getInsCount();
				insCount=insCount==null?0:insCount;
				if (insCount == -1){
					insCount_s = "巡查次数已够";
					work_ation_td.setVisibility(View.GONE);
				}else{
					insCount_s = String.valueOf(insCount.intValue());
					work_ation_td.setVisibility(View.VISIBLE);
				}
				work_ation_loc.setVisibility(View.GONE);
				work_message.setVisibility(View.VISIBLE);
				
				String lastInsDateStr=insPatrolDataVO.getLastInsDateStr();
				lastInsDateStr = lastInsDateStr==null ? "未巡" : lastInsDateStr;
				work_message.setText(insPatrolDataVO.getFrequencyDesc());
				String text = "";
				if(insPatrolDataVO.getName()!=null){
					text = insPatrolDataVO.getName()+insPatrolDataVO.getFacType()
					+"\n最后巡查日期:"+lastInsDateStr
					+"\n巡查次数:"+insCount_s;
				}else{
					text = insPatrolDataVO.getNum()+insPatrolDataVO.getFacType()
					+"\n最后巡查日期:"+lastInsDateStr
					+"\n巡查次数:"+insCount_s;
				}
				work_type_title.setText(text);
			}
		}else if(Constant.ROUTINE_INS.equals(title)){
			if(bsSupervisionPoint!=null){
				work_ation_loc.setVisibility(View.VISIBLE);
				work_message.setVisibility(View.GONE);
				work_ation_td.setVisibility(View.VISIBLE);
				work_ation_td.setText("签到");
//				work_ation_td.setLayoutParams(new LinearLayout.LayoutParams( 
//						DensityUtil.dip2px(context,45), DensityUtil.dip2px(context, 25)));
				work_message.setText(bsSupervisionPoint.getFrequencyDesc());
				
				String lastInsDateStr=bsSupervisionPoint.getLastInsDateStr();
				lastInsDateStr = lastInsDateStr==null ? "未巡" : lastInsDateStr;
				Long insCount=bsSupervisionPoint.getInsCount();
				insCount=insCount==null?0:insCount;
				if (insCount == -1){
					insCount_s = "巡查次数已够";
					planning_work_item.setBackgroundResource(R.drawable.wx_bg_blue);
				}else{
					insCount_s = String.valueOf(insCount.intValue());
					planning_work_item.setBackgroundResource(R.drawable.wx_bg_red);
				}
				work_type_title.setVisibility(View.VISIBLE);
				work_type_title.setGravity(Gravity.LEFT);
				work_type_title.setText("签到点名称:"+bsSupervisionPoint.getSpName()
						+"\n最后巡查日期:"+lastInsDateStr
						+"\n巡查次数:"+insCount_s+"/"+bsSupervisionPoint.getFrequencyNumber());
			}else if(bsRoutineInsArea!=null){
				work_ation_loc.setVisibility(View.VISIBLE);
				work_ation_td.setVisibility(View.VISIBLE);
//				work_ation_td.setLayoutParams(new LinearLayout.LayoutParams( 
//						ViewGroup.LayoutParams.WRAP_CONTENT, DensityUtil.dip2px(context, 25)));
				work_ation_td.setText("隐患上报");
				int w= work_ation_td.getWidth();
				work_message.setVisibility(View.VISIBLE);
				work_type_title.setVisibility(View.GONE);
				work_type_title.setGravity(Gravity.CENTER);
				work_message.setText("区域:"+bsRoutineInsArea.getAreaName()
						+"\n"+bsRoutineInsArea.getFrequencyDesc());
				planning_work_item.setBackgroundResource(R.drawable.wx_bg);
			}
		}else if(Constant.SPECIAL_INS.equals(title)){
			if(bsInsFacInfo!=null){
				work_ation_loc.setVisibility(View.VISIBLE);
				work_ation_td.setVisibility(View.VISIBLE);
				work_ation_msg.setVisibility(View.VISIBLE);
				work_message.setVisibility(View.VISIBLE);
				work_message.setText(bsInsFacInfo.getFrequencyDesc());
				
				String lastInsDateStr=bsInsFacInfo.getLastInsDateStr();
				lastInsDateStr = lastInsDateStr==null ? "未巡" : lastInsDateStr;
				Long insCount=bsInsFacInfo.getInsCount();
				insCount=insCount==null?0:insCount;
				if (insCount == -1){
					insCount_s = "巡查次数已够";
					planning_work_item.setBackgroundResource(R.drawable.wx_bg_blue);
				}else{
					insCount_s = String.valueOf(insCount.intValue());
					planning_work_item.setBackgroundResource(R.drawable.wx_bg_red);
				}
				work_type_title.setText("设施名称:"+bsInsFacInfo.getIfiFacType()
						+"\n设施编号:"+bsInsFacInfo.getIfiFacNum()
						+"\n所在道路:"+bsInsFacInfo.getIfiFacLaneWay()
						+"\n最后巡查日期:"+lastInsDateStr
						+"\n巡查次数:"+insCount_s+"/"+bsInsFacInfo.getFrequencyNumber());
			}
		}else if(Constant.EMPHASIS_INS.equals(title)){
			if(bsEmphasisInsArea!=null){
				work_ation_loc.setVisibility(View.VISIBLE);
				work_ation_td.setVisibility(View.VISIBLE);
				work_message.setVisibility(View.VISIBLE);
				work_message.setText(bsEmphasisInsArea.getFrequencyDesc());
				
				String lastInsDateStr=bsEmphasisInsArea.getLastInsDateStr();
				lastInsDateStr = lastInsDateStr==null ? "未巡" : lastInsDateStr;
				Long insCount=bsEmphasisInsArea.getInsCount();
				insCount=insCount==null?0:insCount;
				if (insCount == -1){
					insCount_s = "巡查次数已够";
					planning_work_item.setBackgroundResource(R.drawable.wx_bg_blue);
				}else{
					insCount_s = String.valueOf(insCount.intValue());
					planning_work_item.setBackgroundResource(R.drawable.wx_bg_red);
				}
				work_type_title.setText("区域名称:"+bsEmphasisInsArea.getAreaName()
						+"\n最后巡查日期:"+lastInsDateStr
						+"\n巡查次数:"+insCount_s+"/"+bsEmphasisInsArea.getFrequencyNumber());
			}
		}else if(Constant.LEAK_INS.equals(title)){
			if(bsLeakInsArea!=null){
				work_ation_loc.setVisibility(View.VISIBLE);
				work_ation_td.setVisibility(View.VISIBLE);
				work_message.setVisibility(View.VISIBLE);
				work_message.setText(bsLeakInsArea.getFrequencyDesc());
				
				String lastInsDateStr=bsLeakInsArea.getLastInsDateStr();
				lastInsDateStr = lastInsDateStr==null ? "未巡" : lastInsDateStr;
				Long insCount=bsLeakInsArea.getInsCount();
				insCount=insCount==null?0:insCount;
				if (insCount == -1){
					insCount_s = "巡查次数已够";
					planning_work_item.setBackgroundResource(R.drawable.wx_bg_blue);
				}else{
					insCount_s = String.valueOf(insCount.intValue());
					planning_work_item.setBackgroundResource(R.drawable.wx_bg_red);
				}
				work_type_title.setText("区域名称:"+bsLeakInsArea.getAreaName()
						+"\n最后巡查日期:"+lastInsDateStr
						+"\n巡查次数:"+insCount_s+"/"+bsLeakInsArea.getFrequencyNumber());
			}
		}else if(Constant.PLAN_FAC_ZS_CYCLE.equals(title)||Constant.PLAN_FAC_ZS_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				Long insCount=insPatrolDataVO.getInsCount();
				insCount=insCount==null?0:insCount;
				if (insCount == -1){
					insCount_s = "巡查次数已够";
				}else{
					insCount_s = String.valueOf(insCount.intValue());
				}
				work_ation_td.setVisibility(View.VISIBLE);
				work_message.setVisibility(View.VISIBLE);
				String lastInsDateStr=insPatrolDataVO.getLastInsDateStr();
				lastInsDateStr = lastInsDateStr==null ? "未巡" : lastInsDateStr;
				work_message.setText(insPatrolDataVO.getFrequencyDesc());
				
				work_type_title.setText(insPatrolDataVO.getName()+insPatrolDataVO.getFacType()
							+"\n最后巡查日期:"+lastInsDateStr
							+"\n巡查次数:"+insCount_s);
				work_ation_loc.setVisibility(View.VISIBLE);
			}else if(insPatrolAreaData!=null){
				work_ation_td.setVisibility(View.GONE);
				String text = insPatrolAreaData.getAreaName();
				work_type_title.setText(text);
				work_message.setVisibility(View.VISIBLE);
				work_message.setText("区域名称");
			}
		}else if(Constant.PLAN_TIEPAI_CYCLE.equals(title)||Constant.PLAN_TIEPAI_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				Long insCount=insPatrolDataVO.getInsCount();
				insCount=insCount==null?0:insCount;
				if (insCount == -1){
					insCount_s = "巡查次数已够";
				}else{
					insCount_s = String.valueOf(insCount.intValue());
				}
				work_ation_td.setVisibility(View.VISIBLE);
				work_message.setVisibility(View.VISIBLE);
				String lastInsDateStr=insPatrolDataVO.getLastInsDateStr();
				lastInsDateStr = lastInsDateStr==null ? "未巡" : lastInsDateStr;
				work_message.setText(insPatrolDataVO.getFrequencyDesc());
				
				work_type_title.setText(insPatrolDataVO.getName()+insPatrolDataVO.getFacType()
							+"\n最后巡查日期:"+lastInsDateStr
							+"\n巡查次数:"+insCount_s);
				work_ation_loc.setVisibility(View.VISIBLE);
			}else if(insPatrolAreaData!=null){
				work_ation_td.setVisibility(View.GONE);
				String text = insPatrolAreaData.getAreaName();
				work_type_title.setText(text);
				work_message.setVisibility(View.VISIBLE);
				work_message.setText("区域名称");
			}
		}else if(Constant.PLAN_PATROL_SCHEDULE.equals(title)||Constant.PLAN_PATROL_ZS_CYCLE.equals(title)
				||Constant.PLAN_PATROL_ZS_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				Long insCount=insPatrolDataVO.getInsCount();
				insCount=insCount==null?0:insCount;
				if (insCount == -1){
					insCount_s = "巡查次数已够";
				}else{
					insCount_s = String.valueOf(insCount.intValue());
				}
				if("签到点".equals(insPatrolDataVO.getFacType())){
					work_ation_td.setVisibility(View.GONE);
				}else{
					work_ation_td.setVisibility(View.VISIBLE);
				}
				work_message.setVisibility(View.VISIBLE);
				String lastInsDateStr=insPatrolDataVO.getLastInsDateStr();
				lastInsDateStr = lastInsDateStr==null ? "未巡" : lastInsDateStr;
				work_message.setText(insPatrolDataVO.getFrequencyDesc());
				
				work_type_title.setText(insPatrolDataVO.getName()+insPatrolDataVO.getFacType()
							+"\n最后巡查日期:"+lastInsDateStr
							+"\n巡查次数:"+insCount_s);
				work_ation_loc.setVisibility(View.VISIBLE);
			}else if(insPatrolAreaData!=null){
				work_ation_td.setVisibility(View.GONE);
				String text = insPatrolAreaData.getAreaName();
				work_type_title.setText(text);
				work_message.setVisibility(View.VISIBLE);
				work_message.setText("区域名称");
			}
		}else if(Constant.PLAN_PAICHA_TEMPORARY.equals(title)){
			if(insCheckFacRoad!=null){
				if("签到点".equals(insCheckFacRoad.getFacType())){
					work_ation_td.setVisibility(View.GONE);
				}else{
					work_ation_td.setVisibility(View.VISIBLE);
				}
				work_type_title.setText("设施类型:"+insCheckFacRoad.getFacType()
						+"\n设施编号:"+insCheckFacRoad.getFacNum()
						+"\n路段名称:"+insCheckFacRoad.getRoadName());
				work_ation_loc.setVisibility(View.VISIBLE);
				if(insCheckFacRoad.getState()==null||insCheckFacRoad.getState()==1){
					work_message.setText("待巡查");
					work_ation_td.setVisibility(View.VISIBLE);
					work_ation_fantask.setVisibility(View.VISIBLE);
				}else if(insCheckFacRoad.getState()==0){
					work_message.setText("未巡查");
					work_ation_td.setVisibility(View.GONE);
					work_ation_fantask.setVisibility(View.GONE);
				}else if(insCheckFacRoad.getState()==3){
					work_message.setText("已巡查");
					work_ation_td.setVisibility(View.GONE);
					work_ation_fantask.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	@Override
	public void lochandler() {//巡检中路段和（片区）的坐标（区域）定位
		// TODO Auto-generated method stub
		if(Constant.CYCLE_PLAN.equals(title)||Constant.INTERIM_PLAN.equals(title)){
			if(insPatrolDataVO!=null){
				String strGraph=null;
				strGraph=insPatrolDataVO.getGeometryStr();
				locMap(strGraph);
			}
		}else if(Constant.XJGL_GJD_CYCLE.equals(title)||Constant.XJGL_GJD_TEMPORARY.equals(title)){
			if(insKeyPointPatrolData!=null){
				String strGraph=null;
				strGraph=insKeyPointPatrolData.getKpaPosition();
				locMap(strGraph);
			}
		}else if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){
			if(insSiteManage!=null){
				String strGraph=null;
				strGraph=insSiteManage.getPrjBound();
				locMap(strGraph);
			}
		}else if(Constant.PLAN_VALVE_CYCLE.equals(title)||Constant.PLAN_VALVE_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				String strGraph=null;
				strGraph=insPatrolDataVO.getGeometryStr();
				locMap(strGraph);
			}
		}else if(Constant.PLAN_HYDRANT_CYCLE.equals(title)||Constant.PLAN_HYDRANT_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				String strGraph=null;
				strGraph=insPatrolDataVO.getGeometryStr();
				locMap(strGraph);
			}
		}else if(Constant.PLAN_FAC_CYCLE.equals(title)||Constant.PLAN_FAC_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				
			}
		}else if(Constant.ROUTINE_INS.equals(title)){
			if(bsSupervisionPoint!=null){
				String strGraph = null;
				strGraph = bsSupervisionPoint.getSpCoordinate();
				locMap(strGraph);
			}else if(bsRoutineInsArea!=null){
				String strGraph = null;
				strGraph = bsRoutineInsArea.getRiaCoordinate();
				locMap(strGraph);
			}
		}else if(Constant.SPECIAL_INS.equals(title)){
			if(bsInsFacInfo!=null){
				String strGraph=null;
				strGraph=bsInsFacInfo.getIfiFacCoordinate();
				locMap(strGraph);
			}
		}else if(Constant.EMPHASIS_INS.equals(title)){
			if(bsEmphasisInsArea!=null){
				String strGraph=null;
				strGraph=bsEmphasisInsArea.getEiaCoordinate();
				locMap(strGraph);
			}
		}else if(Constant.LEAK_INS.equals(title)){
			if(bsLeakInsArea!=null){
				String strGraph=null;
				strGraph=bsLeakInsArea.getLiaCoordinate();
				locMap(strGraph);
			}
		}else if(Constant.PLAN_FAC_ZS_CYCLE.equals(title)||Constant.PLAN_FAC_ZS_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				String strGraph=null;
				strGraph=insPatrolDataVO.getGeometryStr();
				locMap(strGraph);
			}else if(insPatrolAreaData!=null){
				String strGraph=null;
				strGraph=insPatrolAreaData.getGeometryStr();
				locMap(strGraph);
			}
		}else if(Constant.PLAN_TIEPAI_CYCLE.equals(title)||Constant.PLAN_TIEPAI_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				String strGraph=null;
				strGraph=insPatrolDataVO.getGeometryStr();
				locMap(strGraph);
			}else if(insPatrolAreaData!=null){
				String strGraph=null;
				strGraph=insPatrolAreaData.getGeometryStr();
				locMap(strGraph);
			}
		}else if(Constant.PLAN_PATROL_SCHEDULE.equals(title)
				||Constant.PLAN_PATROL_ZS_CYCLE.equals(title)
				||Constant.PLAN_PATROL_ZS_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				String strGraph=null;
				strGraph=insPatrolDataVO.getGeometryStr();
				locMap(strGraph);
			}else if(insPatrolAreaData!=null){
				String strGraph=null;
				strGraph=insPatrolAreaData.getGeometryStr();
				locMap(strGraph);
			}
		}else if(Constant.PLAN_PAICHA_TEMPORARY.equals(title)){
			if(insCheckFacRoad!=null){
				String strGraph=null;
				strGraph=insCheckFacRoad.getShapeStr();
				locMap(strGraph);
			}
		}
	}
	private void locMap(String strGraph){
		Intent intent=new Intent();
		intent.setClass(context, MapBizViewer.class);
		intent.putExtra("strGraph",strGraph );
		intent.putExtra("type", 10006);
		intent.putExtra("tableNum", insTablePushTaskVo.getTaskNum());
		((Activity)context).startActivity(intent);
	}

	@Override
	public void writeTable() {//巡检页面填单操作
		// TODO Auto-generated method stub
		if(Constant.CYCLE_PLAN.equals(title)||Constant.INTERIM_PLAN.equals(title)){
			if(insPatrolDataVO!=null){
				
			}
		}else if(Constant.XJGL_GJD_CYCLE.equals(title)||Constant.XJGL_GJD_TEMPORARY.equals(title)){
			if(insKeyPointPatrolData!=null){
				
			}
		}else if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){
			if(insSiteManage!=null){
				List<Module> mdList = new ArrayList<Module>();
				HashMap<String, String> params = new HashMap<String, String>();
				if (!"1".equals(insSiteManage.getIsFirst())) {
					for (Module m : lstModule) {
						String mId = insSiteManage.getAndroidForm();
						if (mId != null) {
							String[] mIds = mId.split(",");
							for (int i = 0; i < mIds.length; ++i) {
								if (mIds[i].equals(m.getId())) {
									mdList.add(m);
								}
							}
						}
					}
				} else {
					for (Module m : lstModule) {
						String mId = insSiteManage.getAndroidForm2();
						if (mId != null) {
							String[] mIds = mId.split(",");
							for (int i = 0; i < mIds.length; ++i) {
								if (mIds[i].equals(m.getId())) {
									mdList.add(m);
								}
							}
						}
					}
				}
				params.put("constructionNum", insSiteManage.getConstructionNum());
				if (insSiteManage.getSmId() != null) {
					params.put("smId", insSiteManage.getSmId() + "");
				}
				showModuleDialog(mdList, params,null, insSiteManage.getId(),true,true,true);
			}
		}else if(Constant.PLAN_VALVE_CYCLE.equals(title)||Constant.PLAN_VALVE_TEMPORARY.equals(title)||
				Constant.PLAN_HYDRANT_CYCLE.equals(title)||Constant.PLAN_HYDRANT_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				List<Module> taskModule=new ArrayList<Module>();
				for(Module m:lstModule){
					String mId=insPatrolDataVO.getAndroidForm();
					if(mId!=null){
						String []mIds=mId.split(",");
						for(int i=0;i<mIds.length;++i){
							if(mIds[i].equals(m.getId())){
								taskModule.add(m);
							}
						}
					}
				}
				HashMap<String, String>params=new HashMap<String, String>();
				params.put("bfNum", insPatrolDataVO.getNum());
				params.put("bsIpsNum", insPatrolDataVO.getNum());
				params.put("facType", insPatrolDataVO.getFacType());
				showModuleDialog(taskModule, params,null, insPatrolDataVO.getGuid(),true,true,true);
			}
		}else if(Constant.PLAN_FAC_CYCLE.equals(title)||Constant.PLAN_FAC_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				if("打卡点".equals(insPatrolDataVO.getFacType())){
					List<Module> taskModule=new ArrayList<Module>();
					for(Module m:lstModule){
						String mId=insPatrolDataVO.getAndroidForm();
						if(mId!=null){
							String []mIds=mId.split(",");
							for(int i=0;i<mIds.length;++i){
								if(mIds[i].equals(m.getId())){
									taskModule.add(m);
								}
							}
						}
					}
					HashMap<String, String> options=new HashMap<String, String>();
					HashMap<String, String>params=new HashMap<String, String>();
					params.put("bsFacNum", insPatrolDataVO.getNum());
					params.put("bsFacType", insPatrolDataVO.getFacType());
					params.put("bsReportDate", MyDateTools.date2String(new Date()));
					showModuleDialog(taskModule, params,options, insPatrolDataVO.getGuid(),true,true,true);
				}else{
					List<Module> taskModule=new ArrayList<Module>();
					for(Module m:lstModule){
						String mId=insPatrolDataVO.getAndroidForm();
						if(mId!=null){
							String []mIds=mId.split(",");
							for(int i=0;i<mIds.length;++i){
								if(mIds[i].equals(m.getId())){
									taskModule.add(m);
								}
							}
						}
					}
					List<BsInsContentSettingVO> bsInsContentSettingList=new ArrayList<BsInsContentSettingVO>();
					Dao<BsInsTypeSettingVO, Long> bsInsTypeSettingDao;
					try {
						bsInsTypeSettingDao = AppContext.getInstance().getAppDbHelper().getDao(BsInsTypeSettingVO.class);
						Dao<BsInsContentSettingVO, Long> BsInsContentSettingDao = AppContext.getInstance().getAppDbHelper().getDao(BsInsContentSettingVO.class);
						List<BsInsTypeSettingVO> bsInsTypeSettingList=bsInsTypeSettingDao.queryForEq("bsItsName", insPatrolDataVO.getFacType());
						if(bsInsTypeSettingList!=null){
							for(BsInsTypeSettingVO bsInsTypeSettingVO:bsInsTypeSettingList){
								Map<String, Object> m=new HashMap<String, Object>();
								String dpt=AppContext.getInstance().getCurUser().getGroupName();
								if("管线巡查班组".equals(dpt)
										||"A组".equals(dpt)
										||"B组".equals(dpt)
										||"C组".equals(dpt)
										||"D组".equals(dpt)){
									m.put("bsIcsLevel1", "1");
								}else if("维修班组".equals(dpt)){
									m.put("bsIcsLevel2", "1");
								}else if("技术股".equals(dpt)){
									m.put("bsIcsLevel3", "1");
								}else{
									Toast.makeText(context, "该部门不存在", Toast.LENGTH_LONG).show();
									return;
								}
								m.put("bsItsNum", bsInsTypeSettingVO.getBsItsNum());
								bsInsContentSettingList.addAll(BsInsContentSettingDao.queryForFieldValues(m));
							}	
						}
						String optionsStr="";
						for(BsInsContentSettingVO bsInsContentSettingVO:bsInsContentSettingList){
							String text=optionsStr.equals("") ? bsInsContentSettingVO.getBsIcsName()
									+":"+bsInsContentSettingVO.getBsIcsType()
									+":"+bsInsContentSettingVO.getBsIcsValue()
									:"|"+bsInsContentSettingVO.getBsIcsName()
									+":"+bsInsContentSettingVO.getBsIcsType()
									+":"+bsInsContentSettingVO.getBsIcsValue();
							optionsStr+=text;
						}
						if("".endsWith(optionsStr)){
							Toast.makeText(context, "没有可填内容", Toast.LENGTH_LONG).show();
							return;
						}
						HashMap<String, String> options=new HashMap<String, String>();
						options.put("bsInsReportContentLists", optionsStr);
						HashMap<String, String>params=new HashMap<String, String>();
						params.put("bsFacNum", insPatrolDataVO.getNum());
						params.put("bsFacType", insPatrolDataVO.getFacType());
						params.put("bsReportDate", MyDateTools.date2String(new Date()));
						params.put("bsadd", insPatrolDataVO.getName());
						showModuleDialog(taskModule, params,options, insPatrolDataVO.getGuid(),true,true,true);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}else if(Constant.ROUTINE_INS.equals(title)){
			if(bsSupervisionPoint!=null){
				List<Module> taskModule=new ArrayList<Module>();
				for(Module m:lstModule){
					String mId=bsSupervisionPoint.getReportTableNames();
					if(mId!=null){
						String []mIds=mId.split(",");
						for(int i=0;i<mIds.length;++i){
							if(mIds[i].equals(m.getId())){
								taskModule.add(m);
							}
						}
					}
				}
				HashMap<String, String>params=new HashMap<String, String>();
				params.put("spId", bsSupervisionPoint.getSpId()+"");
				params.put("pnitId", bsSupervisionPoint.getPnitId()+"");
				params.put("pnitNum", bsSupervisionPoint.getWorkTaskNum());
				params.put("sprDateStr", MyDateTools.date2String(new Date()));
				params.put("sprCoordinate", bsSupervisionPoint.getSpCoordinate());
				List<BsPnInsTask> bsPnInsTaskList=null;
				try {
					Dao<BsPnInsTask, Long> bsPnInsTaskDao = AppContext.getInstance().getAppDbHelper().getDao(BsPnInsTask.class);
					bsPnInsTaskList = bsPnInsTaskDao.queryForEq("pnitNum", bsSupervisionPoint.getWorkTaskNum());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(bsPnInsTaskList!=null&&bsPnInsTaskList.size()>0){
					params.put("pnitType",bsPnInsTaskList.get(0).getPnitType() );
				}
				showModuleDialog(taskModule, params,null, bsSupervisionPoint.getSpId()+"",true,true,false);
			}else if(bsRoutineInsArea!=null){
				List<Module> taskModule=new ArrayList<Module>();
				for(Module m:lstModule){
					String mId=bsRoutineInsArea.getReportTableNames();
					if(mId!=null){
						String []mIds=mId.split(",");
						for(int i=0;i<mIds.length;++i){
							if(mIds[i].equals(m.getId())){
								taskModule.add(m);
							}
						}
					}
				}
				HashMap<String, String>params=new HashMap<String, String>();
				params.put("riaId", bsRoutineInsArea.getRiaId()+"");
				params.put("pnitId", bsRoutineInsArea.getPnitId()+"");
				params.put("pnitNum", bsRoutineInsArea.getWorkTaskNum());
				List<BsPnInsTask> bsPnInsTaskList=null;
				try {
					Dao<BsPnInsTask, Long> bsPnInsTaskDao = AppContext.getInstance().getAppDbHelper().getDao(BsPnInsTask.class);
					bsPnInsTaskList = bsPnInsTaskDao.queryForEq("pnitNum", bsRoutineInsArea.getWorkTaskNum());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(bsPnInsTaskList!=null&&bsPnInsTaskList.size()>0){
					params.put("pnitType",bsPnInsTaskList.get(0).getPnitType() );
				}
				showModuleDialog(taskModule, params,null, bsRoutineInsArea.getRiaId()+"",true,false,false);
			}
		}else if(Constant.SPECIAL_INS.equals(title)){
			if(bsInsFacInfo!=null){
				List<Module> taskModule=new ArrayList<Module>();
				for(Module m:lstModule){
					String mId=bsInsFacInfo.getReportTableNames();
					if(mId!=null){
						String []mIds=mId.split(",");
						for(int i=0;i<mIds.length;++i){
							if(mIds[i].equals(m.getId())){
								taskModule.add(m);
							}
						}
					}
				}
				String optionsStr="";
				Dao<TpconfigVO, Long> tpconfigVODao;
				try {
					tpconfigVODao = AppContext.getInstance().getAppDbHelper().getDao(TpconfigVO.class);
					Map<String, Object> m=new HashMap<String, Object>();
					m.put("name", "巡查内容");
					m.put("frontName", "设施类型");
					m.put("frontOption", bsInsFacInfo.getIfiFacType());
					m.put("tableName", "0016");
					List<TpconfigVO> tpconfigVOList = tpconfigVODao.queryForFieldValues(m);
					for(TpconfigVO tpconfigVO : tpconfigVOList){
						String text=optionsStr.equals("") ? tpconfigVO.getPvalue()
								+":"+tpconfigVO.getMemo()
								:"|"+tpconfigVO.getPvalue()
								+":"+tpconfigVO.getMemo();
						optionsStr+=text;
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				HashMap<String, String> options=new HashMap<String, String>();
				options.put("bsInsReportContentLists", optionsStr);
				HashMap<String, String>params=new HashMap<String, String>();
				params.put("ifiId", bsInsFacInfo.getIfiId()+"");
				params.put("pnitId", bsInsFacInfo.getPnitId()+"");
				params.put("gid", bsInsFacInfo.getGid()+"");
				params.put("Coordinate", bsInsFacInfo.getIfiFacCoordinate()+"");
				params.put("pnitNum", bsInsFacInfo.getWorkTaskNum());
				List<BsPnInsTask> bsPnInsTaskList=null;
				try {
					Dao<BsPnInsTask, Long> bsPnInsTaskDao = AppContext.getInstance().getAppDbHelper().getDao(BsPnInsTask.class);
					bsPnInsTaskList = bsPnInsTaskDao.queryForEq("pnitNum", bsInsFacInfo.getWorkTaskNum());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(bsPnInsTaskList!=null&&bsPnInsTaskList.size()>0){
					params.put("pnitType",bsPnInsTaskList.get(0).getPnitType() );
				}
				
				params.put("ifiFacNum", bsInsFacInfo.getIfiFacNum());
				params.put("ifiFacType", bsInsFacInfo.getIfiFacType());
				params.put("ifiFacLaneWay", bsInsFacInfo.getIfiFacLaneWay());
				params.put("ifiFacAddr", bsInsFacInfo.getIfiFacAddr());
				if(bsInsFacInfo.getIfiFacDs()!=null){
					params.put("ifiFacDs", bsInsFacInfo.getIfiFacDs()+"");
				}
				params.put("manageUnit", bsInsFacInfo.getManageUnit());
				showModuleDialog2(taskModule, params,options, bsInsFacInfo.getIfiId()+"",true,true,false);
			}
		}else if(Constant.EMPHASIS_INS.equals(title)){
			if(bsEmphasisInsArea!=null){
				List<Module> taskModule=new ArrayList<Module>();
				for(Module m:lstModule){
					String mId=bsEmphasisInsArea.getReportTableNames();
					if(mId!=null){
						String []mIds=mId.split(",");
						for(int i=0;i<mIds.length;++i){
							if(mIds[i].equals(m.getId())){
								taskModule.add(m);
							}
						}
					}
				}
				HashMap<String, String>params=new HashMap<String, String>();
				params.put("eiaId", bsEmphasisInsArea.getEiaId()+"");
				params.put("pnitId", bsEmphasisInsArea.getPnitId()+"");
				params.put("pnitNum", bsEmphasisInsArea.getWorkTaskNum());
				List<BsPnInsTask> bsPnInsTaskList=null;
				try {
					Dao<BsPnInsTask, Long> bsPnInsTaskDao = AppContext.getInstance().getAppDbHelper().getDao(BsPnInsTask.class);
					bsPnInsTaskList = bsPnInsTaskDao.queryForEq("pnitNum", bsEmphasisInsArea.getWorkTaskNum());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(bsPnInsTaskList!=null&&bsPnInsTaskList.size()>0){
					params.put("pnitType",bsPnInsTaskList.get(0).getPnitType() );
				}
				showModuleDialog2(taskModule, params,null, bsEmphasisInsArea.getEiaId()+"",true,true,false);
			}
		}else if(Constant.LEAK_INS.equals(title)){
			if(bsLeakInsArea!=null){
				List<Module> taskModule=new ArrayList<Module>();
				for(Module m:lstModule){
					String mId=bsLeakInsArea.getReportTableNames();
					if(mId!=null){
						String []mIds=mId.split(",");
						for(int i=0;i<mIds.length;++i){
							if(mIds[i].equals(m.getId())){
								taskModule.add(m);
							}
						}
					}
				}
				HashMap<String, String>params=new HashMap<String, String>();
				params.put("liaId", bsLeakInsArea.getLiaId()+"");
				params.put("pnitId", bsLeakInsArea.getPnitId()+"");
				params.put("pnitNum", bsLeakInsArea.getWorkTaskNum());
				List<BsPnInsTask> bsPnInsTaskList=null;
				try {
					Dao<BsPnInsTask, Long> bsPnInsTaskDao = AppContext.getInstance().getAppDbHelper().getDao(BsPnInsTask.class);
					bsPnInsTaskList = bsPnInsTaskDao.queryForEq("pnitNum", bsLeakInsArea.getWorkTaskNum());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(bsPnInsTaskList!=null&&bsPnInsTaskList.size()>0){
					params.put("pnitType",bsPnInsTaskList.get(0).getPnitType() );
				}
				showModuleDialog(taskModule, params,null, bsLeakInsArea.getLiaId()+"",true,true,false);
			}
		}else if(Constant.PLAN_FAC_ZS_CYCLE.equals(title)||Constant.PLAN_FAC_ZS_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				List<Module> taskModule=new ArrayList<Module>();
				for(Module m:lstModule){
					String mId=insPatrolDataVO.getAndroidForm();
					if(mId!=null){
						String []mIds=mId.split(",");
						for(int i=0;i<mIds.length;++i){
							if(mIds[i].equals(m.getId())){
								taskModule.add(m);
							}
						}
					}
				}
				HashMap<String, String>params=new HashMap<String, String>();
				params.put("bfNum", insPatrolDataVO.getNum());
				params.put("bsIpsNum", insPatrolDataVO.getNum());
				params.put("facType", insPatrolDataVO.getFacType());
				showModuleDialog(taskModule, params,null, insPatrolDataVO.getGuid(),true,true,true);
			}
		}else if(Constant.PLAN_TIEPAI_CYCLE.equals(title)||Constant.PLAN_TIEPAI_TEMPORARY.equals(title)
				||Constant.PLAN_PATROL_SCHEDULE.equals(title)||Constant.PLAN_PATROL_ZS_CYCLE.equals(title)
				||Constant.PLAN_PATROL_ZS_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				List<Module> taskModule=new ArrayList<Module>();
				for(Module m:lstModule){
					String mId=insPatrolDataVO.getAndroidForm();
					if(mId!=null){
						String []mIds=mId.split(",");
						for(int i=0;i<mIds.length;++i){
							if(mIds[i].equals(m.getId())){
								taskModule.add(m);
							}
						}
					}
				}
				HashMap<String, String>params=new HashMap<String, String>();
				params.put("bfNum", insPatrolDataVO.getNum());
				params.put("bsIpsNum", insPatrolDataVO.getNum());
				params.put("facType", insPatrolDataVO.getFacType());
				showModuleDialog(taskModule, params,null, insPatrolDataVO.getGuid(),true,true,true);
			}
		}else if(Constant.PLAN_PAICHA_TEMPORARY.equals(title)){
			if(insCheckFacRoad!=null){
				if(insCheckFacRoad.getFacType().equals("阀门")){
					List<Module> taskModule=new ArrayList<Module>();
					for(Module m:lstModule){
						String mId=insCheckFacRoad.getAndroidForm();
						if(mId!=null){
							String []mIds=mId.split(",");
							for(int i=0;i<mIds.length;++i){
								if(mIds[i].equals(m.getId())){
									taskModule.add(m);
								}
							}
						}
					}
					HashMap<String, String>params=new HashMap<String, String>();
					params.put("guid", insCheckFacRoad.getGuid());
					params.put("gid", insCheckFacRoad.getGid());
					params.put("worktasknum", insCheckFacRoad.getWorkTaskNum());
					params.put("areaId", insCheckFacRoad.getAreaId()+"");
					params.put("id", insCheckFacRoad.getId()+"");
					showModuleDialog2(taskModule, params,null, String.valueOf(insCheckFacRoad.getId()),true,true,true);
				}else{
					List<Module> taskModule=new ArrayList<Module>();
					for(Module m:lstModule){
						String mId=insCheckFacRoad.getAndroidForm();
						if(mId!=null){
							String []mIds=mId.split(",");
							for(int i=0;i<mIds.length;++i){
								if(mIds[i].equals(m.getId())){
									taskModule.add(m);
								}
							}
						}
					}
					HashMap<String, String>params=new HashMap<String, String>();
					params.put("gid", insCheckFacRoad.getGid());
					params.put("worktasknum", insCheckFacRoad.getWorkTaskNum());
					params.put("areaId", insCheckFacRoad.getAreaId()+"");
					params.put("id", insCheckFacRoad.getId()+"");
					showModuleDialog(taskModule, params,null, String.valueOf(insCheckFacRoad.getId()),true,true,true);
				}
			}
		}
	}
	private void showModuleDialog(final List<Module> taskModule,final HashMap<String,String>params
			,final HashMap<String, String> options,final String pid,final boolean delete,final boolean isUpdate,boolean isShowDeleteBnt){
		if(taskModule==null){
			Toast.makeText(context, "没有相应的模板", Toast.LENGTH_LONG).show();
			return;
		}
		if(taskModule.size()==1){
			if(insTablePushTaskVo!=null){
				TaskFeedBackAsyncTask taskFeedBackAsyncTask=new TaskFeedBackAsyncTask(context
						, false,false, insTablePushTaskVo.getTaskNum(), Constant.UPLOAD_STATUS_WORK,
						null, insTablePushTaskVo.getTitle(), insTablePushTaskVo.getTaskCategory(),null,null,null);
				taskFeedBackAsyncTask.execute();
				Intent newFormInfo = new Intent(context,RunForm.class);
        		newFormInfo.putExtra("template", taskModule.get(0).getTemplate());
        		newFormInfo.putExtra("pid", pid);
        		newFormInfo.putExtra("taskNum", insTablePushTaskVo.getTaskNum());
        		newFormInfo.putExtra("tableName", insTablePushTaskVo.getTitle());
        		newFormInfo.putExtra("taskCategory", insTablePushTaskVo.getTaskCategory());
        		newFormInfo.putExtra("options", options);
        		newFormInfo.putExtra("delete", delete);
        		newFormInfo.putExtra("isUpdate", isUpdate);
        		newFormInfo.putExtra("iParams", params);
        		newFormInfo.putExtra("isShowDeleteBnt", isShowDeleteBnt);
        		((Activity) context).startActivityForResult(newFormInfo, Constant.FROM_REQUEST_CODE);
			}
			return;
		}
		String[] itmes =new String[taskModule.size()];
		
		for(int i=0;i<itmes.length;++i){
			itmes[i]=taskModule.get(i).getName();
		}
		AlertDialog.Builder vDialog = new AlertDialog.Builder(context);
		vDialog.setTitle("选择工作类型");
		vDialog.setItems(itmes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(insTablePushTaskVo!=null){
					TaskFeedBackAsyncTask taskFeedBackAsyncTask=new TaskFeedBackAsyncTask(context
							, false,false, insTablePushTaskVo.getTaskNum(), Constant.UPLOAD_STATUS_WORK,
							null, insTablePushTaskVo.getTitle(), insTablePushTaskVo.getTaskCategory(),null,null,null);
					taskFeedBackAsyncTask.execute();
					Intent newFormInfo = new Intent(context,RunForm.class);
	        		newFormInfo.putExtra("template", taskModule.get(which).getTemplate());
	        		newFormInfo.putExtra("pid", pid);
	        		newFormInfo.putExtra("taskNum", insTablePushTaskVo.getTaskNum());
	        		newFormInfo.putExtra("tableName", insTablePushTaskVo.getTitle());
	        		newFormInfo.putExtra("taskCategory", insTablePushTaskVo.getTaskCategory());
	        		if(Constant.BIZ_CONSTRUCTION_DISCLOSURE.equals(taskModule.get(which).getId())){
	        			newFormInfo.putExtra("delete", false);
	        		}else{
	        			newFormInfo.putExtra("delete", delete);
	        		}
	        		newFormInfo.putExtra("isUpdate", isUpdate);
	        		newFormInfo.putExtra("options", options);
	        		newFormInfo.putExtra("iParams", params);    			
	        		((Activity) context).startActivityForResult(newFormInfo, Constant.FROM_REQUEST_CODE);
				}
			}
		});
		vDialog.show();
	}
	
	private void showModuleDialog2(final List<Module> taskModule,final HashMap<String,String>params
			,final HashMap<String, String> options,final String pid,final boolean delete,final boolean isUpdate,boolean isShowDeleteBnt){
		if(taskModule==null){
			Toast.makeText(context, "没有相应的模板", Toast.LENGTH_LONG).show();
			return;
		}
		if(taskModule.size()==1){
			if(insTablePushTaskVo!=null){
				TaskFeedBackAsyncTask taskFeedBackAsyncTask=new TaskFeedBackAsyncTask(context
						, false,false, insTablePushTaskVo.getTaskNum(), Constant.UPLOAD_STATUS_WORK,
						null, insTablePushTaskVo.getTitle(), insTablePushTaskVo.getTaskCategory(),null,null,null);
				taskFeedBackAsyncTask.execute();
				Intent newFormInfo = new Intent(context,RunForm.class);
        		newFormInfo.putExtra("template", taskModule.get(0).getTemplate());
        		newFormInfo.putExtra("pid", pid);
        		newFormInfo.putExtra("taskNum", insTablePushTaskVo.getTaskNum());
        		newFormInfo.putExtra("tableName", insTablePushTaskVo.getTitle());
        		newFormInfo.putExtra("taskCategory", insTablePushTaskVo.getTaskCategory());
        		newFormInfo.putExtra("options", options);
        		newFormInfo.putExtra("delete", delete);
        		newFormInfo.putExtra("isUpdate", isUpdate);
        		newFormInfo.putExtra("iParams", params);
        		newFormInfo.putExtra("isShowDeleteBnt", isShowDeleteBnt);
        		((Activity) context).startActivityForResult(newFormInfo, Constant.FROM_REQUEST_CODE);
			}
			return;
		}
		String[] itmes =new String[taskModule.size()];
		
		for(int i=0;i<itmes.length;++i){
			itmes[i]=taskModule.get(i).getName();
		}
		
		View layerView = View.inflate(context, R.layout.tasklist_yhxj_dialog, null);

		ListView lv = (ListView) layerView
				.findViewById(R.id.tasklist_yhxj_List);
		lv.setAdapter(new ArrayAdapter<String>(context, R.anim.myspinner2,itmes ));
		final PopupWindow mPopupWindow;
		mPopupWindow = new PopupWindow(layerView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if(insTablePushTaskVo!=null){
					TaskFeedBackAsyncTask taskFeedBackAsyncTask=new TaskFeedBackAsyncTask(context
							, false,false, insTablePushTaskVo.getTaskNum(), Constant.UPLOAD_STATUS_WORK,
							null, insTablePushTaskVo.getTitle(), insTablePushTaskVo.getTaskCategory(),null,null,null);
					taskFeedBackAsyncTask.execute();
					Intent newFormInfo = new Intent(context,RunForm.class);
	        		newFormInfo.putExtra("template", taskModule.get(position).getTemplate());
	        		newFormInfo.putExtra("pid", pid);
	        		newFormInfo.putExtra("taskNum", insTablePushTaskVo.getTaskNum());
	        		newFormInfo.putExtra("tableName", insTablePushTaskVo.getTitle());
	        		newFormInfo.putExtra("taskCategory", insTablePushTaskVo.getTaskCategory());
	        		if(Constant.BIZ_CONSTRUCTION_DISCLOSURE.equals(taskModule.get(position).getId())){
	        			newFormInfo.putExtra("delete", false);
	        		}else{
	        			newFormInfo.putExtra("delete", delete);
	        		}
	        		newFormInfo.putExtra("isUpdate", isUpdate);
	        		newFormInfo.putExtra("options", options);
	        		newFormInfo.putExtra("iParams", params);    			
	        		((Activity) context).startActivityForResult(newFormInfo, Constant.FROM_REQUEST_CODE);
				}
				mPopupWindow.dismiss();
			}
		});

		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(),
				(Bitmap) null));
		
		
		mPopupWindow.showAtLocation(layerView, Gravity.CENTER, 0, 0);
	}

	@Override
	public void showMsg() {//巡检页面内的详细按钮
		// TODO Auto-generated method stub
		if(Constant.CYCLE_PLAN.equals(title)||Constant.INTERIM_PLAN.equals(title)){
			if(insPatrolDataVO!=null){
				
			}
		}else if(Constant.XJGL_GJD_CYCLE.equals(title)||Constant.XJGL_GJD_TEMPORARY.equals(title)){
			if(insKeyPointPatrolData!=null){
				
			}
		}else if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){
			if(insSiteManage!=null){
				Intent intent=new Intent();
				intent.setClass(context,ShowTaskActivity.class);
				intent.putExtra("insTablePushTaskVo", insTablePushTaskVo);
				intent.putExtra("insSiteManage", insSiteManage);
				context.startActivity(intent);
			}
		}else if(Constant.PLAN_VALVE_CYCLE.equals(title)||Constant.PLAN_VALVE_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				
			}
		}else if(Constant.PLAN_HYDRANT_CYCLE.equals(title)||Constant.PLAN_HYDRANT_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				
			}
		}else if(Constant.PLAN_FAC_CYCLE.equals(title)||Constant.PLAN_FAC_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				
			}
		}else if(Constant.ROUTINE_INS.equals(title)){
			if(bsSupervisionPoint!=null){
				
			}else if(bsRoutineInsArea!=null){
				
			}
		}else if(Constant.SPECIAL_INS.equals(title)){
			if(bsInsFacInfo!=null){
				Intent intent=new Intent();
				intent.setClass(context,ShowTaskMsgActivity.class);
				String names="巡查任务编号,设施名称,设施编号,所在道路,安装地址,口径,管理单位";
				
				String values=bsInsFacInfo.getPnitNum()+","
						+bsInsFacInfo.getIfiFacType()+","
						+bsInsFacInfo.getIfiFacNum()+","
						+bsInsFacInfo.getIfiFacLaneWay()+","
						+bsInsFacInfo.getIfiFacAddr()+","
						+bsInsFacInfo.getIfiFacDs()+","
						+bsInsFacInfo.getManageUnit();
				intent.putExtra("names", names);
				intent.putExtra("values", values);
				context.startActivity(intent);
			}
		}else if(Constant.EMPHASIS_INS.equals(title)){
			if(bsEmphasisInsArea!=null){
				
			}
		}else if(Constant.LEAK_INS.equals(title)){
			if(bsLeakInsArea!=null){
				
			}
		}else if(Constant.PLAN_FAC_ZS_CYCLE.equals(title)||Constant.PLAN_FAC_ZS_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				
			}else if (insPatrolAreaData!=null){
				
			}
		}else if(Constant.PLAN_TIEPAI_CYCLE.equals(title)||Constant.PLAN_TIEPAI_TEMPORARY.equals(title)
				||Constant.PLAN_PATROL_SCHEDULE.equals(title)||Constant.PLAN_PATROL_ZS_CYCLE.equals(title)
				||Constant.PLAN_PATROL_ZS_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				
			}else if (insPatrolAreaData!=null){
				
			}
		}
	}
	@Override
	public void titleOnClick(PlanWorkAdapter planWorkAdapter) {
		// TODO Auto-generated method stub
		if(Constant.ROUTINE_INS.equals(title)){
			if(bsRoutineInsArea!=null){
				
				List<Map<String, Object>> pdListRD =planWorkAdapter.getPdListRD();
				
					List<Map<String, Object>> pdListR = new ArrayList<Map<String,Object>>();
					for(int i=0; i<pdListRD.size();++i){
						Map<String, Object> m = pdListRD.get(i);
						BsSupervisionPoint bsSupervisionPoint =(BsSupervisionPoint) m.get("BsSupervisionPoint");
						if(bsSupervisionPoint==null||
							(!bsSupervisionPoint.getAreaNum().equals(bsRoutineInsArea.getAreaNum())
									&&!bsSupervisionPoint.isNoShow())){
							pdListR.add(m);
						}else if(bsSupervisionPoint!=null&&bsSupervisionPoint.getAreaNum().equals(bsRoutineInsArea.getAreaNum())){
							if(!bsSupervisionPoint.isNoShow()){
								bsSupervisionPoint.setNoShow(true);
							}else{
								bsSupervisionPoint.setNoShow(false);
								pdListR.add(m);
							}
							/*if(pdListM==null){
								pdListM = new ArrayList<Map<String,Object>>();
							}
							pdListM.add(m);*/
						}
					}
					planWorkAdapter.setPdList(pdListR);
					planWorkAdapter.notifyDataSetChanged();
				/*}else{
					if(pdListM.size()>0){
						int i=0;
						for(; i<pdList.size();++i){
							Map<String, Object> m = pdList.get(i);
							BsRoutineInsArea bsRa = (BsRoutineInsArea) m.get("BsRoutineInsArea");
							if(bsRa!=null&&bsRa.getAreaNum().equals(((BsSupervisionPoint)pdListM.get(0).get("bsSupervisionPoint")).getAreaNum())){
								break;
							}
						}
						pdList.addAll(i, pdListM);
						planWorkAdapter.setPdList(pdList);
						planWorkAdapter.notifyDataSetChanged();
					}
					pdListM = null;
				}*/
			}
		}
	}
	@Override
	public void tuidanTable() {
		// TODO Auto-generated method stub
		if(Constant.PLAN_PAICHA_TEMPORARY.equals(title)){
			new AlertDialog.Builder(context).setTitle("提示")
				.setIcon(android.R.drawable.ic_menu_help)
				.setMessage("是否删除该记录？")
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				})
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						RegionAnalyzeTask5 regionAnalyzeTask = new RegionAnalyzeTask5(context,insCheckFacRoad);
				        regionAnalyzeTask.execute("");
					}
				}).show();
		}
	}

}
