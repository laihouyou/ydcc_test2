package com.movementinsome.app.mytask.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.movementinsome.R;

import java.util.List;
import java.util.Map;


public class TaskListDreRoadPlanAdapter extends BaseAdapter{

	public static final String TITLE="title";// 标题
	public static final String KEY="key";// 分类标示
	public static final String STATE="state";// 状态
	public static final String TASKNUMBER="taskNumber";//  编号
	public static final String CONTENT="content";// 工作内容
	public static final String WORK_TIME="work_time";// 工作时间
	public static final String DATA_OBJECT="data_object";// 数据对象
	public static final String WORK_COUNT="work_count";// 巡检次数
	public static final String NAMES="names";
	public static final String VALUES="values";
	
	private List<Map<String, Object>> data;
	private Context context;
	public TaskListDreRoadPlanAdapter(List<Map<String, Object>> data,Context context){
		this.data=data;
		this.context = context;
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
			LayoutInflater layout = LayoutInflater.from(context);
			convertView = layout.inflate(R.layout.tasklist_centre_list, null);
		}
		TextView tv = (TextView)convertView.findViewById(R.id.tltv_content);
		TextView tlTv_Title = (TextView)convertView.findViewById(R.id.tlTv_Title);
		String title=(String) data.get(position).get(TITLE);
		tlTv_Title.setText(title);
		String text="";
		text=(String) data.get(position).get(CONTENT);
		tv.setText(text);
		return convertView;
	}

}
