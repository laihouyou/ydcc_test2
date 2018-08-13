package com.movementinsome.app.mytask.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.mytask.handle.PlanWorkListBaseHandle;
import com.movementinsome.app.mytask.handle.PlanWorkListHandle;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.database.vo.BsEmphasisInsArea;
import com.movementinsome.database.vo.BsInsFacInfo;
import com.movementinsome.database.vo.BsLeakInsArea;
import com.movementinsome.database.vo.BsRoutineInsArea;
import com.movementinsome.database.vo.BsSupervisionPoint;
import com.movementinsome.database.vo.InsCheckFacRoad;
import com.movementinsome.database.vo.InsKeyPointPatrolData;
import com.movementinsome.database.vo.InsPatrolAreaData;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsSiteManage;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.initial.model.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanWorkAdapter extends BaseAdapter{

	private Context context;
	private List<Map<String, Object>> pdList;
	private List<Map<String, Object>> pdListRD;
	private Handler handler;
	private InsTablePushTaskVo insTablePushTaskVo;
	private List<Module> lstModule;// 所有表单配置
	
	public PlanWorkAdapter(Context context,List<Map<String, Object>> pdList,Handler handler,InsTablePushTaskVo insTablePushTaskVo){
		this.context=context;
		this.pdList=pdList;
		pdListRD = pdList;
		this.handler=handler;
		this.insTablePushTaskVo=insTablePushTaskVo;
		lstModule = AppContext.getInstance().getModuleData();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pdList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return pdList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			
			convertView=View.inflate(context, R.layout.planning_work_list_item,null);
			
			holder.work_type_title=(TextView) convertView.findViewById(R.id.work_type_title);
			holder.work_message=(TextView) convertView.findViewById(R.id.work_message);
			holder.work_ation_loc=(Button) convertView.findViewById(R.id.work_ation_loc);
			holder.work_ation_msg=(Button) convertView.findViewById(R.id.work_ation_msg);// 信息
			holder.work_ation_td=(Button) convertView.findViewById(R.id.work_ation_td);
			holder.work_ation_zt = (Button) convertView.findViewById(R.id.work_ation_zt);// 暂停
			holder.work_ation_fantask = (Button) convertView.findViewById(R.id.work_ation_fantask);
			holder.planning_work_item = (LinearLayout) convertView.findViewById(R.id.planning_work_item);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.work_ation_loc.setVisibility(View.VISIBLE);
		holder.work_ation_td.setVisibility(View.VISIBLE);
		
		if(pdList!=null){
			final InsPatrolDataVO insPatrolDataVO=(InsPatrolDataVO) pdList.get(position).get("InsPatrolDataVO");
			final InsSiteManage insSiteManage=(InsSiteManage) pdList.get(position).get("InsSiteManage");
			final InsKeyPointPatrolData insKeyPointPatrolData= (InsKeyPointPatrolData) pdList.get(position).get("InsKeyPointPatrolData");
			
			final BsSupervisionPoint bsSupervisionPoint = (BsSupervisionPoint) pdList.get(position).get("BsSupervisionPoint");
			final BsRoutineInsArea bsRoutineInsArea = (BsRoutineInsArea) pdList.get(position).get("BsRoutineInsArea");
			final BsInsFacInfo bsInsFacInfo = (BsInsFacInfo) pdList.get(position).get("BsInsFacInfo");
			final BsEmphasisInsArea bsEmphasisInsArea = (BsEmphasisInsArea) pdList.get(position).get("BsEmphasisInsArea");
			final BsLeakInsArea bsLeakInsArea = (BsLeakInsArea) pdList.get(position).get("BsLeakInsArea");
			final List<PlanWorkListBaseHandle> planWorkListBaseHandleList = new ArrayList<PlanWorkListBaseHandle>();
			InsPatrolAreaData insPatrolAreaData = (InsPatrolAreaData) pdList.get(position).get("InsPatrolAreaData");
			final InsCheckFacRoad insCheckFacRoad = (InsCheckFacRoad) pdList.get(position).get("InsCheckFacRoad");
			planWorkListBaseHandleList.add(new PlanWorkListHandle(context, lstModule, insTablePushTaskVo, insPatrolDataVO, insSiteManage, insKeyPointPatrolData
					, bsSupervisionPoint, bsRoutineInsArea, bsInsFacInfo, bsEmphasisInsArea, bsLeakInsArea,insPatrolAreaData,insCheckFacRoad));
			Map<String, View> vs =new HashMap<String, View>();
			vs.put("work_type_title", holder.work_type_title);
			vs.put("work_message", holder.work_message);
			vs.put("work_ation_loc", holder.work_ation_loc);
			vs.put("work_ation_msg", holder.work_ation_msg);
			vs.put("work_ation_td", holder.work_ation_td);
			vs.put("work_ation_fantask", holder.work_ation_fantask);
			vs.put("planning_work_item", holder.planning_work_item);
			
			for(PlanWorkListBaseHandle planWorkListBaseHandle :planWorkListBaseHandleList){
				planWorkListBaseHandle.controlUI(vs);
			}
			holder.work_ation_loc.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(Constant.PLAN_PAICHA_TEMPORARY.equals(insTablePushTaskVo.getTitle())){
						try {
							Message message=new Message();
							message.what=3;
							Bundle canshu = new Bundle();
							canshu.putString("shapestr", 
								insCheckFacRoad.getShapeStr());
							message.setData(canshu);
							handler.sendMessage(message);
							} catch (Exception e) {
								// TODO: handle exception
							}
					}else{
						for(PlanWorkListBaseHandle planWorkListBaseHandle :planWorkListBaseHandleList){
							planWorkListBaseHandle.lochandler();
						}
					}
					
				}
			});
			holder.work_ation_td.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					for(PlanWorkListBaseHandle planWorkListBaseHandle :planWorkListBaseHandleList){
						planWorkListBaseHandle.writeTable();
					}
				}
			});
			holder.work_ation_fantask.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					for(PlanWorkListBaseHandle planWorkListBaseHandle :planWorkListBaseHandleList){
						planWorkListBaseHandle.tuidanTable();
					}
					handler.sendEmptyMessage(4);
				}
			});
			holder.work_ation_msg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					for(PlanWorkListBaseHandle planWorkListBaseHandle :planWorkListBaseHandleList){
						planWorkListBaseHandle.showMsg();
					}
				}
			});
			holder.work_message.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					for(PlanWorkListBaseHandle planWorkListBaseHandle :planWorkListBaseHandleList){
						planWorkListBaseHandle.titleOnClick(PlanWorkAdapter.this); 
					}
				}
			});
		}
		return convertView;
	}
	
	static class ViewHolder{
		public TextView work_type_title;
		public TextView work_message;
		public Button work_ation_loc;
		public Button work_ation_msg;// 信息
		public Button work_ation_td;
		public Button work_ation_zt;// 暂停
		public Button work_ation_fantask;
		public LinearLayout planning_work_item;
	}

	public List<Map<String, Object>> getPdList() {
		return pdList;
	}
	public void setPdList(List<Map<String, Object>> pdList) {
		this.pdList = pdList;
	}
	public List<Map<String, Object>> getPdListRD() {
		return pdListRD;
	}
	public void setPdListRD(List<Map<String, Object>> pdListRD) {
		this.pdListRD = pdListRD;
	}
	public Handler getHandler() {
		return handler;
	}
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
}
