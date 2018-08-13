package com.movementinsome.app.mytask.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.mytask.ShowMsgActivity;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.database.vo.BsFacInfo;
import com.movementinsome.database.vo.BsInsContentSettingVO;
import com.movementinsome.database.vo.BsInsTypeSettingVO;
import com.movementinsome.easyform.formengineer.RunForm;
import com.movementinsome.kernel.initial.model.Module;
import com.movementinsome.kernel.util.MyDateTools;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceSearchAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater inflater;
	private List<BsFacInfo> listMsg;
	private List<Module> taskModule;
	
	public DeviceSearchAdapter(Context context,List<BsFacInfo> listMsg,List<Module> taskModule){
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.listMsg = listMsg;
		this.taskModule=taskModule;
	}
	
	
	@Override
	public int getCount() {
		return listMsg.size();
	}

	@Override
	public Object getItem(int position) {
		return listMsg.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if(convertView == null){
			convertView = inflater.inflate(R.layout.list_item_search, null);
		}
		holder.item_device_name = (TextView) convertView.findViewById(R.id.item_device_name);
		holder.item_device_num = (TextView) convertView.findViewById(R.id.item_device_num);
		holder.item_write_btn = (Button) convertView.findViewById(R.id.item_write_btn);
		holder.item_detail_btn = (Button) convertView.findViewById(R.id.item_detail_btn);
		holder.item_device_num.setText("设施编号: "+listMsg.get(position).getBsFacNum());
		String bsFacType =listMsg.get(position).getBsFacType();
		String text ="";
		if("视频监控".equals(bsFacType)){
			holder.item_device_num.setVisibility(View.VISIBLE);
			text ="设施名称："+listMsg.get(position).getBsFacName()+"\n"
					+ "阀门井编号："+listMsg.get(position).getBsIpsNum()+"\n"
					+ "设施类型："+listMsg.get(position).getBsFacType()+"\n"
					+ "所在位置："+listMsg.get(position).getBsFacAddr()+"\n"
					+ "设施坐标："+listMsg.get(position).getBsFacMap()+"\n"
					+ "标段："+listMsg.get(position).getBsFacRiName()+"\n"
					
					+ "里程："+listMsg.get(position).getBsMileage()+"\n"
					+ "型号："+listMsg.get(position).getBsModel()+"\n"
					+ "厂家："+listMsg.get(position).getBsFactory()+"\n"
					+ "规格："+listMsg.get(position).getBsSpecification()+"\n"
					+ "端站名称："+listMsg.get(position).getRtuNetName()+"\n"
					+ "巡检周期："+listMsg.get(position).getBsInsCycle();
		}else if("RTU设备".equals(bsFacType)){
			holder.item_device_num.setVisibility(View.VISIBLE);
			text ="设施名称："+listMsg.get(position).getBsFacName()+"\n"
					+ "阀门井编号："+listMsg.get(position).getBsIpsNum()+"\n"
					+ "设施类型："+listMsg.get(position).getBsFacType()+"\n"
					+ "所在位置："+listMsg.get(position).getBsFacAddr()+"\n"
					+ "设施坐标："+listMsg.get(position).getBsFacMap()+"\n"
					+ "标段："+listMsg.get(position).getBsFacRiName()+"\n"
					
					+ "里程："+listMsg.get(position).getBsMileage()+"\n"
					+ "型号："+listMsg.get(position).getBsModel()+"\n"
					+ "厂家："+listMsg.get(position).getBsFactory()+"\n"
					+ "规格："+listMsg.get(position).getBsSpecification()+"\n"
					+ "排气阀编号："+listMsg.get(position).getRtuValveNum()+"\n"
					+ "端站名称："+listMsg.get(position).getRtuNetName()+"\n"
					+ "巡检周期："+listMsg.get(position).getBsInsCycle();
		}else if("排气阀".equals(bsFacType)||"泄压阀".equals(bsFacType)){
			holder.item_device_num.setVisibility(View.VISIBLE);
			text ="设施名称："+listMsg.get(position).getBsFacName()+"\n"
					+ "阀门井编号："+listMsg.get(position).getBsIpsNum()+"\n"
					+ "设施类型："+listMsg.get(position).getBsFacType()+"\n"
					+ "所在位置："+listMsg.get(position).getBsFacAddr()+"\n"
					+ "设施坐标："+listMsg.get(position).getBsFacMap()+"\n"
					+ "标段："+listMsg.get(position).getBsFacRiName()+"\n"
					
					+ "里程："+listMsg.get(position).getBsMileage()+"\n"
					+ "型号："+listMsg.get(position).getBsModel()+"\n"
					+ "厂家："+listMsg.get(position).getBsFactory()+"\n"
					+ "规格："+listMsg.get(position).getBsSpecification()+"\n"
					+ "巡检周期："+listMsg.get(position).getBsInsCycle();
		}else if("直线点".equals(bsFacType)){
			holder.item_device_num.setVisibility(View.GONE);
			text ="图上点号："+listMsg.get(position).getBsFacNum()+"\n"
					+ "管线材料："+listMsg.get(position).getBsPipeMaterial()+"\n"
					+ "特征："+listMsg.get(position).getBsFeatures()+"\n"
					+ "附属物："+listMsg.get(position).getBsAppendages()+"\n"
					+ "地面："+listMsg.get(position).getBsGround()+"\n"
					+ "管顶："+listMsg.get(position).getBsPipeTop()+"\n"
					
					+ "管径或断面尺寸："+listMsg.get(position).getBsDiameter()+"\n"
					+ "埋深："+listMsg.get(position).getBsBurDepth()+"\n"
					+ "连接方向："+listMsg.get(position).getBsConnDirection();
		}else{
			holder.item_device_num.setVisibility(View.VISIBLE);
			text ="设施名称："+listMsg.get(position).getBsFacName()+"\n"
					+ "阀门井编号："+listMsg.get(position).getBsIpsNum()+"\n"
					+ "设施类型："+listMsg.get(position).getBsFacType()+"\n"
					+ "所在位置："+listMsg.get(position).getBsFacAddr()+"\n"
					+ "设施坐标："+listMsg.get(position).getBsFacMap()+"\n"
					+ "标段："+listMsg.get(position).getBsFacRiName()+"\n"
					
					+ "里程："+listMsg.get(position).getBsMileage()+"\n"
					+ "型号："+listMsg.get(position).getBsModel()+"\n"
					+ "厂家："+listMsg.get(position).getBsFactory()+"\n"
					+ "规格："+listMsg.get(position).getBsSpecification()+"\n"
					+ "转数："+listMsg.get(position).getBsRevolutions()+"\n"
					+ "有无延长杆："+listMsg.get(position).getBsExtensionRod();
		}
		holder.item_device_name.setText(text);
		holder.item_write_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					if(taskModule.size()>0){
						List<BsInsContentSettingVO> bsInsContentSettingList=new ArrayList<BsInsContentSettingVO>();
						Dao<BsInsTypeSettingVO, Long> BsInsTypeSettingDao = AppContext.getInstance().getAppDbHelper().getDao(BsInsTypeSettingVO.class);
						Dao<BsInsContentSettingVO, Long> BsInsContentSettingDao = AppContext.getInstance().getAppDbHelper().getDao(BsInsContentSettingVO.class);
						List<BsInsTypeSettingVO> bsInsTypeSettingList=BsInsTypeSettingDao.queryForEq("bsItsName", listMsg.get(position).getBsFacType());
						if(bsInsTypeSettingList!=null){
							for(BsInsTypeSettingVO bsInsTypeSettingVO:bsInsTypeSettingList){
								Map<String, Object> m=new HashMap<String, Object>();
								String dpt=AppContext.getInstance().getCurUser().getGroupName();
								if("管线巡查班组".equals(dpt)){
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
						Intent newFormInfo = new Intent(context,RunForm.class);
						newFormInfo.putExtra("delete", true);
						HashMap<String, String> params=new HashMap<String, String>();
						params.put("bsFacAddr", listMsg.get(position).getBsFacAddr());
						params.put("bsFacNum", listMsg.get(position).getBsFacNum());
						params.put("bsFacName", listMsg.get(position).getBsFacName());
						params.put("bsFacType", listMsg.get(position).getBsFacType());
						if(listMsg.get(position).getBsFacCaliber()!=null){
							params.put("bsFacCaliber", listMsg.get(position).getBsFacCaliber()+"");
						}
						params.put("bsFacMaterial", listMsg.get(position).getBsFacMaterial());
						if(listMsg.get(position).getBsFacDepth()!=null){
							params.put("bsFacDepth", listMsg.get(position).getBsFacDepth()+"");
						}
						params.put("bsFacRiName", listMsg.get(position).getBsFacRiName());
						params.put("gid", listMsg.get(position).getGid());
						
						params.put("bsReportDate", MyDateTools.date2String(new Date()));
						params.put("bsReportId", AppContext.getInstance().getCurUser().getUserId());
						params.put("bsReportNum", AppContext.getInstance().getCurUser().getUserName());
						params.put("bsReportName", AppContext.getInstance().getCurUser().getUserAlias());
						params.put("bsReportDeptId", AppContext.getInstance().getCurUser().getDeptId()+"");
						params.put("bsReportDeptNum", AppContext.getInstance().getCurUser().getDeptNum());
						params.put("bsReportDeptName", AppContext.getInstance().getCurUser().getDeptName());
						params.put("bsReportTeamId", AppContext.getInstance().getCurUser().getGroupId()+"");
						params.put("bsReportTeamNum", AppContext.getInstance().getCurUser().getGroupNum());
						params.put("bsReportTeamName", AppContext.getInstance().getCurUser().getGroupName());
						params.put("imei", AppContext.getInstance().getPhoneIMEI());
						HashMap<String, String> options=new HashMap<String, String>();
						options.put("bsInsReportContentLists", optionsStr);
						newFormInfo.putExtra("iParams", params);
						newFormInfo.putExtra("options", options);
		        		newFormInfo.putExtra("tableName", "BS_FAC_INFO");
		        		newFormInfo.putExtra("delete", true);
						newFormInfo.putExtra("template", taskModule.get(0).getTemplate());
						((Activity) context).startActivityForResult(newFormInfo, Constant.FROM_REQUEST_CODE);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		holder.item_detail_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent =new Intent();
				intent.putExtra("arrayId", R.array.xjys_fac_msg);
				ArrayList<String>msgData=new ArrayList<String>();
				msgData.add(listMsg.get(position).getBsFacNum());
				msgData.add(listMsg.get(position).getBsFacName());
				msgData.add(listMsg.get(position).getBsFacType());
				msgData.add(listMsg.get(position).getBsFacAddr());
				msgData.add(listMsg.get(position).getBsFacCaliber()+"");
				msgData.add(listMsg.get(position).getBsFacMaterial()+"");
				msgData.add(listMsg.get(position).getBsFacDepth()+"");
				msgData.add(listMsg.get(position).getBsFacRiName());
				intent.putExtra("msgData", msgData);
				intent.setClass(context, ShowMsgActivity.class);
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	
	class ViewHolder{
		TextView item_device_num,item_device_name;
		Button item_write_btn,item_detail_btn;
	}

}
