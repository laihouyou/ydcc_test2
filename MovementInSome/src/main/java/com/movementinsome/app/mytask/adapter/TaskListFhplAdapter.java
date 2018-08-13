package com.movementinsome.app.mytask.adapter;

import android.app.Activity;
import android.content.Context;
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
import com.movementinsome.database.vo.DrainageVO;
import com.movementinsome.easyform.formengineer.RunForm;
import com.movementinsome.kernel.initial.model.Module;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class TaskListFhplAdapter extends BaseAdapter{

	private Context context;
	private List<DrainageVO> data;
	private Module module;
	public TaskListFhplAdapter(Context context,List<DrainageVO> data,Module module){
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
		
		final DrainageVO drainageVO=data.get(position);
		String text="防洪指令："+drainageVO.getContent()+"\n"
					+"发送时间："+drainageVO.getReceiveTime()+"\n";
		phfs_content.setText(text);
		phfs_ssfk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(module!=null){
					Intent newFormInfo = new Intent(context,RunForm.class);
	        		newFormInfo.putExtra("template", module.getTemplate());
	        		newFormInfo.putExtra("pid", drainageVO.getId());
	        		newFormInfo.putExtra("taskNum",drainageVO.getWorkTaskNum());
	        		newFormInfo.putExtra("tableName", drainageVO.getTitle());
	        		newFormInfo.putExtra("delete", true);
	        		HashMap<String,String>params=new HashMap<String, String>();
	        		params.put("senderId", drainageVO.getSenderId());
	        		params.put("senderNum", drainageVO.getSenderNum());
	        		params.put("senderName", drainageVO.getSenderName());
	        		params.put("workTaskNum", drainageVO.getWorkTaskNum());
	        		newFormInfo.putExtra("iParams", params);
	        		((Activity) context).startActivityForResult(newFormInfo, Constant.FROM_REQUEST_CODE);
				}else{
					Toast.makeText(context, "没有相应的模板", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		phfs_wc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Dao<DrainageVO, Long> drainageVODao = AppContext.getInstance().getAppDbHelper().getDao(DrainageVO.class);
					drainageVODao.delete(drainageVO);
					Intent intent=new Intent(Constant.FHPL_ACTION);
					context.sendBroadcast(intent);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		return convertView;
	}
}
