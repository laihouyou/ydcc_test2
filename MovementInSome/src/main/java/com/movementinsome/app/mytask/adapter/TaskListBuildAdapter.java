package com.movementinsome.app.mytask.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.movementinsome.R;

import java.util.List;


public class TaskListBuildAdapter extends BaseAdapter implements OnClickListener{

	private List<List<String>> data;
	private Context context;
	
	private Button xxbtnRecord;// 开始1
	private Button xxbtnMap;// 完成1
	
	private int pos;
	private Toast toast;

	
	public TaskListBuildAdapter(List<List<String>> data,Context context){
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
			convertView = layout.inflate(R.layout.tasklist_build_list, null);
		}
		
		pos = position;
		toast = Toast.makeText(this.context, "", 1);
		toast.setGravity(Gravity.CENTER, 0, 0);
		
		xxbtnRecord = (Button)convertView.findViewById(R.id.xxbtnRecord);
		xxbtnRecord.setOnClickListener(this);

		xxbtnMap = (Button)convertView.findViewById(R.id.xxbtnMap);
		xxbtnMap.setOnClickListener(this);
		
		TextView tv = (TextView)convertView.findViewById(R.id.tltv_content);
		TextView tlTv_Title = (TextView)convertView.findViewById(R.id.tlTv_Title);
		String title=data.get(position).get(0);
		tlTv_Title.setText(title);
		String text="";
		
		text = "项目名称" + data.get(position).get(1) + "\n" 
				+ "开工日期:"+ data.get(position).get(2)+ "\n"
				+ "建设单位：" + data.get(position).get(3) + "\n"
				+ "项目经理:" +data.get(position).get(4)+ "\n"
				+"施工单位:"+data.get(position).get(5)+"\n"
				+"联系电话:"+data.get(position).get(6)+"\n"
				+"项目地址:"+data.get(position).get(7)+"\n"
				+"工程进度:"+data.get(position).get(8)+"\n"
				+"最近检查日期:"+data.get(position).get(9)
				;
		tv.setText(text);
		return convertView;
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.xxbtnRecord:
			xxbtnRecord.setEnabled(false);
			xxbtnRecord.setTextColor(Color.rgb(200, 200, 200));

			toast.setText("填写工地巡查记录");
			toast.show();
			break;
		case R.id.xxbtnMap:
			break;
		default:
			break;
		}
	}

}
