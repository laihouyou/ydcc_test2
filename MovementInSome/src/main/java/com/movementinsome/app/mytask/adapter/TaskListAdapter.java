package com.movementinsome.app.mytask.adapter;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.app.mytask.handle.TaskListBaseHandle;
import com.movementinsome.app.mytask.handle.TaskListHandle;
import com.movementinsome.app.mytask.handle.XJYSTaskListHandle;
import com.movementinsome.app.mytask.handle.YHTaskListHandle;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.database.vo.BsEmphasisInsArea;
import com.movementinsome.database.vo.BsInsFacInfo;
import com.movementinsome.database.vo.BsLeakInsArea;
import com.movementinsome.database.vo.BsPnInsTask;
import com.movementinsome.database.vo.BsRoutineInsArea;
import com.movementinsome.database.vo.BsSupervisionPoint;
import com.movementinsome.database.vo.InsDredgePTask;
import com.movementinsome.database.vo.InsFacMaintenanceData;
import com.movementinsome.database.vo.InsKeyPointPatrolData;
import com.movementinsome.database.vo.InsPatrolAreaData;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsPlanTaskVO;
import com.movementinsome.database.vo.InsSiteManage;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.database.vo.TaskUploadStateVO;
import com.movementinsome.kernel.initial.model.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskListAdapter extends BaseExpandableListAdapter {

	private List<Map<String, Object>> groupData;
	private List<List<Map<String, Object>>> childData;
	private Activity context;
	private Handler myHandler;
	private ExpandableListView tlcExListView;
	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;
	private String mModuleid;
	
/*<<<<<<< .mine
	private InsDredgePTask insDredgePTaskP;
	
	
	public TaskListAdapter(Activity context
			,List<Map<String, Object>> groupData,List<List<Map<String, Object>>> childData
			,Handler myHandler,ExpandableListView tlcExListView){
		this.context=context;
		this.groupData=groupData;
		this.childData=childData;
		this.myHandler=myHandler;
		this.tlcExListView=tlcExListView;
		animation = new RotateAnimation(0, -180,RotateAnimation.RELATIVE_TO_SELF, 0.5f,
=======*/

/*	public TaskListAdapter(Activity context,
			List<Map<String, Object>> groupData,
			List<List<Map<String, Object>>> childData, Handler myHandler,
			ExpandableListView tlcExListView) {
		this.context = context;
		this.groupData = groupData;
		this.childData = childData;
		this.myHandler = myHandler;
		this.tlcExListView = tlcExListView;
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
//>>>>>>> .r9553
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);
		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);
	}*/

	
	public TaskListAdapter(Activity context
			,List<Map<String, Object>> groupData,List<List<Map<String, Object>>> childData
			,Handler myHandler,ExpandableListView tlcExListView,String moduleid){
		this.context=context;
		this.groupData=groupData;
		this.childData=childData;
		this.myHandler=myHandler;
		this.tlcExListView=tlcExListView;
		this.mModuleid = moduleid;
		animation = new RotateAnimation(0, -180,RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);
		reverseAnimation = new RotateAnimation(-180, 0,RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);
	}
	


//>>>>>>> .r9553
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		if(childData!=null&&childData.size()>0
				&&childData.get(groupPosition)!=null
				&&childData.get(groupPosition).size()>0){
			return childData.get(groupPosition).get(childPosition);
		}else{
			return null;
		}
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder2 holder;
		if(convertView==null){
			holder = new ViewHolder2();
			
			convertView= View.inflate(context,
					R.layout.planning_work_list_item, null);
			
			holder.work_type_title=(TextView) convertView.findViewById(R.id.work_type_title);
			holder.work_message=(TextView) convertView.findViewById(R.id.work_message);
			holder.work_ation_loc=(Button) convertView.findViewById(R.id.work_ation_loc);
			holder.work_ation_msg=(Button) convertView.findViewById(R.id.work_ation_msg);// 信息
			holder.work_ation_td=(Button) convertView.findViewById(R.id.work_ation_td);
			holder.work_ation_wc=(Button) convertView.findViewById(R.id.work_ation_wc);
			holder.work_ation_wg=(Button) convertView.findViewById(R.id.work_ation_wg);	
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder2)convertView.getTag();
		}
		
		final List<Module> taskModule = (List<Module>) groupData.get(
				groupPosition).get("Module");
		final InsTablePushTaskVo insTablePushTaskVo = (InsTablePushTaskVo) groupData
				.get(groupPosition).get("InsTablePushTaskVo");
		final InsPatrolDataVO insPatrolDataVO = (InsPatrolDataVO) childData
				.get(groupPosition).get(childPosition).get("InsPatrolDataVO");
		final InsSiteManage insSiteManage = (InsSiteManage) childData
				.get(groupPosition).get(childPosition).get("InsSiteManage");
		final InsDredgePTask insDredgePTask = (InsDredgePTask) childData
				.get(groupPosition).get(childPosition).get("InsDredgePTask");

		final InsKeyPointPatrolData insKeyPointPatrolData = (InsKeyPointPatrolData) childData
				.get(groupPosition).get(childPosition)
				.get("InsKeyPointPatrolData");
		final InsFacMaintenanceData insFacMaintenanceData = (InsFacMaintenanceData) childData
				.get(groupPosition).get(childPosition)
				.get("InsFacMaintenanceData");
		BsSupervisionPoint bsSupervisionPoint =(BsSupervisionPoint) childData.get(groupPosition).get(childPosition)
				.get("BsSupervisionPoint");
		BsRoutineInsArea bsRoutineInsArea =(BsRoutineInsArea) childData.get(groupPosition).get(childPosition)
				.get("BsRoutineInsArea");
		BsInsFacInfo bsInsFacInfo =(BsInsFacInfo) childData.get(groupPosition).get(childPosition)
				.get("BsInsFacInfo");
		BsEmphasisInsArea bsEmphasisInsArea =(BsEmphasisInsArea) childData.get(groupPosition).get(childPosition)
				.get("BsEmphasisInsArea");
		BsLeakInsArea bsLeakInsArea =(BsLeakInsArea) childData.get(groupPosition).get(childPosition)
				.get("BsLeakInsArea");
		InsPatrolAreaData insPatrolAreaData = (InsPatrolAreaData) childData.get(groupPosition).get(childPosition)
				.get("InsPatrolAreaData");
		final List<TaskListBaseHandle> taskListBaseHandles=new ArrayList<TaskListBaseHandle>();
		taskListBaseHandles.add(new TaskListHandle(context, taskModule, insTablePushTaskVo, insPatrolDataVO, insSiteManage, insDredgePTask, insKeyPointPatrolData,insPatrolAreaData));
		taskListBaseHandles.add(new XJYSTaskListHandle(context, insTablePushTaskVo, insPatrolDataVO));
		taskListBaseHandles.add(new YHTaskListHandle(context, insTablePushTaskVo, bsSupervisionPoint, bsRoutineInsArea
				, bsInsFacInfo, bsEmphasisInsArea, bsLeakInsArea));
	
		holder.work_ation_msg.setVisibility(View.VISIBLE);
		holder.work_ation_msg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<taskListBaseHandles.size();++i){
					taskListBaseHandles.get(i).childShowMsg();
				}
			}
		});
		holder.work_ation_td.setVisibility(View.VISIBLE);
		holder.work_ation_td.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<taskListBaseHandles.size();++i){
					taskListBaseHandles.get(i).childWriteTableHandler();
				}
			}
		});
		holder.work_ation_wc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<taskListBaseHandles.size();++i){
					taskListBaseHandles.get(i).childFinish();
				}
			}
		});
		holder.work_ation_loc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<taskListBaseHandles.size();++i){
					taskListBaseHandles.get(i).childLocHandler();
				}
			}
		});
		holder.work_ation_wg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<taskListBaseHandles.size();++i){
					taskListBaseHandles.get(i).childComplete();
				}
			}
		});
		Map<String, View> vs=new HashMap<String, View>();
		vs.put("work_ation_msg", holder.work_ation_msg);
		vs.put("work_ation_td", holder.work_ation_td);
		vs.put("work_ation_wc", holder.work_ation_wc);
		vs.put("work_ation_loc", holder.work_ation_loc);
		vs.put("work_ation_wg", holder.work_ation_wg);

		vs.put("work_type_title", holder.work_type_title);
		vs.put("work_message", holder.work_message);
		for(int i=0;i<taskListBaseHandles.size();++i){
			taskListBaseHandles.get(i).childControlUI(vs);
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		if (null!=childData && 0!=childData.size())
			return childData.get(groupPosition).size();
		else
		    return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		if(groupData!=null&&groupData.size()>0)
			return groupData.get(groupPosition);
		else
		    return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		if(groupData!=null){
			return groupData.size();
		}else{
			return 0;
		}
		
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			
			LayoutInflater layout = LayoutInflater.from(context);
			convertView = layout.inflate(R.layout.tasklist_centre_list, null);
			
			holder.rl_bugchuli = (RelativeLayout) convertView.findViewById(R.id.rl_bugchuli);
			holder.tlBn_td = (RelativeLayout) convertView.findViewById(R.id.tlBn_td);// 填单
			holder.tlBn_ks = (RelativeLayout) convertView.findViewById(R.id.tlBn_ks);// 开始
			holder.tlBn_t = (RelativeLayout) convertView.findViewById(R.id.tlBn_t);// 退单
			holder.tlBn_t_xian = (LinearLayout) convertView.findViewById(R.id.tlBn_t_xian);//退单线
			holder.tlBn_wc = (LinearLayout) convertView.findViewById(R.id.tlBn_wc);// 完成
			holder.tlimage_job = (RelativeLayout) convertView
					.findViewById(R.id.tlimage_job);// 下拉
			holder.rl_bottombar = (RelativeLayout) convertView
					.findViewById(R.id.rl_bottombar);//底栏
			holder.job_x = (TextView) convertView.findViewById(R.id.job_x);//展开
			holder.tlBn_xx = (RelativeLayout) convertView.findViewById(R.id.tlBn_xx);// 详细
			holder.tv = (TextView) convertView.findViewById(R.id.tltv_content);// 内容
			holder.tlTv_Title = (TextView) convertView
					.findViewById(R.id.tlTv_Title);// 标题
			holder.tltv_titilename = (TextView) convertView
					.findViewById(R.id.tltv_titilename);// 新标题
			holder.tlBn_xz = (RelativeLayout) convertView.findViewById(R.id.tlBn_xz);// 下载
			holder.lys_ks = (LinearLayout) convertView.findViewById(R.id.tlLinearContent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final List<Module> taskModule = (List<Module>) groupData.get(
				groupPosition).get("Module");
		final InsTablePushTaskVo insTablePushTaskVo = (InsTablePushTaskVo) groupData
				.get(groupPosition).get("InsTablePushTaskVo");
		TaskUploadStateVO taskUploadStateVO = (TaskUploadStateVO) groupData
				.get(groupPosition).get("TaskUploadStateVO");
		final InsPlanTaskVO insPlanTaskVO = (InsPlanTaskVO) groupData.get(
				groupPosition).get("InsPlanTaskVO");
		final BsPnInsTask bsPnInsTask = (BsPnInsTask) groupData.get(
				groupPosition).get("BsPnInsTask");
		final List<TaskListBaseHandle> taskListBaseHandles=new ArrayList<TaskListBaseHandle>();
		taskListBaseHandles.add(new TaskListHandle(context,
				taskUploadStateVO, insPlanTaskVO, insTablePushTaskVo,
				taskModule));
		taskListBaseHandles.add(new XJYSTaskListHandle(context, insTablePushTaskVo
				, taskUploadStateVO, insPlanTaskVO));
		taskListBaseHandles.add(new YHTaskListHandle(context, insTablePushTaskVo, bsPnInsTask, taskUploadStateVO));
/*		Button tlBn_td = (Button) convertView.findViewById(R.id.tlBn_td);// 填单
		Button tlBn_ks = (Button) convertView.findViewById(R.id.tlBn_ks);// 开始
		Button tlBn_t = (Button) convertView.findViewById(R.id.tlBn_t);// 退单
		Button tlBn_wc = (Button) convertView.findViewById(R.id.tlBn_wc);// 完成
		final ImageView tlimage_job = (ImageView) convertView
				.findViewById(R.id.tlimage_job);// 下拉
		Button tlBn_xx = (Button) convertView.findViewById(R.id.tlBn_xx);// 详细
		TextView tv = (TextView) convertView.findViewById(R.id.tltv_content);// 内容
		TextView tlTv_Title = (TextView) convertView
				.findViewById(R.id.tlTv_Title);// 标题
		Button tlBn_xz = (Button) convertView.findViewById(R.id.tlBn_xz);// 下载
*/		holder.tlBn_t.setVisibility(View.VISIBLE);
		// 退单
		holder.tlBn_t.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<taskListBaseHandles.size();++i){
					taskListBaseHandles.get(i).groupReturnTableHandler();
				}
			}
		});
		// 开始
		holder.tlBn_ks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<taskListBaseHandles.size();++i){
					taskListBaseHandles.get(i).groupStartHandler(mModuleid);
				}
			}
		});
		// 标题
		holder.tlTv_Title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<taskListBaseHandles.size();++i){
					taskListBaseHandles.get(i).groupStartHandler(mModuleid);
				}
			}
		});
		// 填单
		holder.tlBn_td.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<taskListBaseHandles.size();++i){
					taskListBaseHandles.get(i).groupWriteTableHandler();
				}
			}
		});
		//Bug处理
		holder.rl_bugchuli.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		// 下拉
		holder.tlimage_job.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isExpanded) {
					holder.job_x.setAnimation(reverseAnimation);
					tlcExListView.collapseGroup(groupPosition);
					holder.rl_bottombar.setBackgroundResource(R.drawable.pgd_down_title);
				} else {

					holder.job_x.setAnimation(animation);
					tlcExListView.expandGroup(groupPosition);
					holder.rl_bottombar.setBackgroundResource(R.drawable.bule_bottombar);
				}
			}
		});
		// 下载
		holder.tlBn_xz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<taskListBaseHandles.size();++i){
					taskListBaseHandles.get(i).groupDownloadHandler(myHandler);
				}
			}
		});
		// 完成
		holder.tlBn_wc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<taskListBaseHandles.size();++i){
					taskListBaseHandles.get(i).groupFinishHandler();
				}
			}
		});

		holder.tltv_titilename.setText(Constant.TASK_TITLE_CN.get(insTablePushTaskVo
				.getTitle()));

		holder.tlBn_xx.setVisibility(View.VISIBLE);
		// 详细信息
		holder.tlBn_xx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<taskListBaseHandles.size();++i){
					taskListBaseHandles.get(i).groupShowMsg();
				}
			}
		});
		holder.tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<taskListBaseHandles.size();++i){
					taskListBaseHandles.get(i).groupShowMsg();
				}
			}
		});
		Map<String, View> vs = new HashMap<String, View>();
		vs.put("tlBn_ks", holder.tlBn_ks);
		vs.put("tlBn_td", holder.tlBn_td);
		vs.put("tlBn_xz", holder.tlBn_xz);
		vs.put("tlBn_wc", holder.tlBn_wc);
		vs.put("tlBn_xx", holder.tlBn_xx);
		vs.put("tlBn_t", holder.tlBn_t);
		vs.put("tlBn_t_xian", holder.tlBn_t_xian);
		

		vs.put("tv", holder.tv);
		vs.put("tlTv_Title", holder.tlTv_Title);
		vs.put("tltv_titilename", holder.tltv_titilename);
		vs.put("tlTv_Title", holder.tlTv_Title);
		vs.put("lys_ks", holder.lys_ks);
		vs.put("tlimage_job", holder.tlimage_job);
		for(int i=0;i<taskListBaseHandles.size();++i){
			taskListBaseHandles.get(i).groupControlUI(vs);
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	/*private void backDialog(final InsTablePushTaskVo insTablePushTaskVo) {
		new AlertDialog.Builder(context).setTitle("提示")
				.setIcon(android.R.drawable.ic_menu_help)
				.setMessage("确定退单，将删除数据！")
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
						TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
								context,
								true,
								false,
								insTablePushTaskVo.getTaskNum(),
								com.gddst.app.mytask.Constant.UPLOAD_STATUS_RETREAT,
								null, insTablePushTaskVo.getTitle(),
								insTablePushTaskVo.getTaskCategory(), null,
								null, null);
						taskFeedBackAsyncTask.execute();
					}
				}).show();
	}*/

	static class ViewHolder {
		public RelativeLayout rl_bugchuli;
		public RelativeLayout tlBn_td;// 填单
		public RelativeLayout tlBn_ks;// 开始
		public RelativeLayout tlBn_t ;// 退单
		public LinearLayout tlBn_t_xian;//退单线
		public LinearLayout tlBn_wc;// 完成
		public RelativeLayout tlimage_job ;// 下拉
		public TextView job_x;//展开
		public RelativeLayout tlBn_xx ;// 详细
		public TextView tv ;// 内容
		public TextView tlTv_Title ;// 标题
		public TextView tltv_titilename;
		public RelativeLayout tlBn_xz ;// 下载
		public LinearLayout lys_ks;
		public RelativeLayout rl_bottombar;//底栏
	}
	
	static class ViewHolder2 {
		public TextView work_type_title;
		public TextView work_message;
		public Button work_ation_loc;// 定位
		public Button work_ation_td;// 填单
		public Button work_ation_msg;// 信息
		public Button work_ation_wc;// 完成
		public Button work_ation_wg;// 完工
	}
}
