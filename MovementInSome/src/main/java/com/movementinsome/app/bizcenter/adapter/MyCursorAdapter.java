package com.movementinsome.app.bizcenter.adapter;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movementinsome.R;


public class MyCursorAdapter extends CursorAdapter{

	private Cursor c;
	private String type,facNum,address,state,factype;

	public MyCursorAdapter(Context context, Cursor c,String type) {
		super(context, c);
		// TODO Auto-generated constructor stub
		this.c = c;
		this.type = type;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return c.getCount();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return super.getItem(position);
	}


	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		LinearLayout ptlist_Linear = (LinearLayout)view.findViewById(R.id.ptlist_Linear);
		TextView tvContent = (TextView) view.findViewById(R.id.ptlist_Content);
		TextView tvNumber = (TextView) view.findViewById(R.id.ptlist_Number);

		//计划任务列表
		if ("subtask".equals(type)) {
			facNum = cursor.getString(cursor.getColumnIndex("facNum"));
			address = cursor.getString(cursor.getColumnIndex("address"));
			factype = cursor.getString(cursor.getColumnIndex("type"));
			state = cursor.getString(cursor.getColumnIndex("state"));;// 获取任务的当前状态，是否巡检过
			//1.true:提交成功
			//2.0:尚没有填写
			//3.save:保存等待提交
			//4.busy:正在填写当中
			tvNumber.setTextColor(Color.parseColor("#6f6f6f"));
			if ("true".equals(state)) {
				ptlist_Linear.setBackgroundResource(R.drawable.task_true);
			} else if ("0".equals(state)) {
				ptlist_Linear.setBackgroundResource(R.drawable.task_false);
			} else if ("save".equals(state)) {
				ptlist_Linear.setBackgroundResource(R.drawable.task_save);
			}else if("busy".equals(state)){
				tvNumber.setTextColor(Color.RED);
				ptlist_Linear.setBackgroundResource(R.drawable.task_busy);
			}
			tvNumber.setText((cursor.getPosition() + 1)+"");
			tvContent.setText("设施编号：" + facNum + "\n设施类型：" +factype + "\n所在道路：" + address);
		}
		
		//用户设置自动提交
		if("history".equals(type)){
			String taskid = cursor.getString(cursor.getColumnIndexOrThrow("number"));
			String ts_time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
			String ts_name = cursor.getString(cursor.getColumnIndex("name"));
			String ts_type = cursor.getString(cursor.getColumnIndex("Type"));
			String ts_state = cursor.getString(cursor.getColumnIndexOrThrow("state"));
			if(ts_state.equals("save")){
				ts_state = "提交中";
			}else if(ts_state.equals("false")){
				ts_state = "数据错误";
			}else if(ts_state.equals("true")){
				ts_state = "已提交";
			}
			tvContent.setText("任务编号 : "+taskid+"\n任务名称 : "+ts_name+"\n提交时间 : "+ts_time
					+"\n操作类型 : "+ts_type+"\n任务状态 : "+ts_state);
			tvNumber.setText((cursor.getPosition() + 1)+"");
			ptlist_Linear.setBackgroundResource(R.drawable.task_false);
		}
		
//		if("bgfx".equals(type)){
//			String taskid = cursor.getString(cursor.getColumnIndexOrThrow("facility_number"));
//			String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
//			tvNumber.setText((cursor.getPosition() + 1)+"");
//			tvContent.setText("设施编号：" + taskid+"\n所在道路：" + address);
//		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return li.inflate(R.layout.pt_list_text, parent, false);
	}



}
