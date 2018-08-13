package com.movementinsome.app.mytask.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.database.vo.CoordinateVO;
import com.movementinsome.database.vo.TaskUploadStateVO;
import com.movementinsome.easyform.formengineer.RunForm;
import com.movementinsome.kernel.initial.model.Module;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class CoordinateAdapter extends BaseAdapter{

	private Context context;
	private List<CoordinateVO> data;
	private Module module;
	public CoordinateAdapter(Context context,List<CoordinateVO> data,Module module){
		this.context=context;
		this.data=data;
		this.module=module;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			convertView=View.inflate(context, R.layout.tasklist_fhpl_list, null);
		}
		TextView phfs_content=(TextView) convertView.findViewById(R.id.phfs_content);
		Button phfs_ssfk=(Button) convertView.findViewById(R.id.phfs_ssfk);
		Button phfs_wc=(Button) convertView.findViewById(R.id.phfs_wc);
		phfs_wc.setVisibility(View.VISIBLE);
		phfs_ssfk.setText("填单");
	
		final CoordinateVO coordinateVO=data.get(position);
		String state="";
		try {
			Dao<TaskUploadStateVO, Long> taskUploadStateDao = AppContext.getInstance().getAppDbHelper().getDao(TaskUploadStateVO.class);
			List<TaskUploadStateVO> taskUploadStateVOList=taskUploadStateDao.queryForEq("taskNum", coordinateVO.getWorkTaskNum());
			if(taskUploadStateVOList!=null&&taskUploadStateVOList.size()>0){
				state=taskUploadStateVOList.get(0).getStatus();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String text="任务编号："+coordinateVO.getWorkTaskNum()+"\n"
					+"地址："+coordinateVO.getWorkTaskAddr()+"\n"
					+"工作内容："+coordinateVO.getWorkTaskWork()+"\n"
					+"任务状态："+ Constant.TASK_STATUS.get(state);
		phfs_content.setText(text);
		phfs_ssfk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(module!=null){
					Intent newFormInfo = new Intent(context,RunForm.class);
	        		newFormInfo.putExtra("template", module.getTemplate());
	        		newFormInfo.putExtra("pid", coordinateVO.getId());
	        		newFormInfo.putExtra("taskNum",coordinateVO.getWorkTaskNum());
	        		newFormInfo.putExtra("tableName", coordinateVO.getTitle());
	        		newFormInfo.putExtra("taskCategory", coordinateVO.getTaskCategory());
	        		
	        		newFormInfo.putExtra("delete", true);
	        		HashMap<String,String>params=new HashMap<String, String>();
	        		params.put("workTaskAddr", coordinateVO.getWorkTaskAddr());
	        		params.put("workTaskWork", coordinateVO.getWorkTaskWork());
	        		params.put("receiveDate", coordinateVO.getReceiveTime());
	        		newFormInfo.putExtra("iParams", params);
	        		((Activity) context).startActivityForResult(newFormInfo, Constant.FROM_REQUEST_CODE);
	        		TaskFeedBackAsyncTask taskFeedBackAsyncTask=new TaskFeedBackAsyncTask(context
	        				, false,false, coordinateVO.getWorkTaskNum(), Constant.UPLOAD_STATUS_WORK,
	        				null, coordinateVO.getTitle(), coordinateVO.getTaskCategory(),null,null,null);
	        		taskFeedBackAsyncTask.execute();
				}else{
					Toast.makeText(context, "没有相应的模板", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		phfs_wc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(context)
				.setTitle("提示")
				.setIcon(android.R.drawable.ic_menu_help)
				.setMessage("确定要结束任务，将删除数据！")
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
						
							TaskFeedBackAsyncTask taskFeedBackAsyncTask=new TaskFeedBackAsyncTask(context
			        				, false,false, coordinateVO.getWorkTaskNum(), Constant.UPLOAD_STATUS_FINISH,
			        				null, coordinateVO.getTitle(), coordinateVO.getTaskCategory(),null,null,null);
			        		taskFeedBackAsyncTask.execute();
							
						
					}
				}).show();
			}
		});
		return convertView;
	}

}
