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


public class TaskListKeyPointAdapter extends BaseAdapter implements OnClickListener{

	private List<List<String>> data;
	private Context context;
	
	private Button xxbtnKPD;// 开始1
	private Button xxbtnKPR;// 完成1
	private Button xxbtnKPM;// 完成1
	
	private int pos;
	private Toast toast;

	
	public TaskListKeyPointAdapter(List<List<String>> data,Context context){
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
			convertView = layout.inflate(R.layout.tasklist_keypoint_list, null);
		}
		
		//pos = position;
		toast = Toast.makeText(this.context, "", 1);
		toast.setGravity(Gravity.CENTER, 0, 0);
		
		
		xxbtnKPD = (Button)convertView.findViewById(R.id.xxbtnKPD);
		xxbtnKPD.setOnClickListener(this);
		
		xxbtnKPR = (Button)convertView.findViewById(R.id.xxbtnKPR);
		xxbtnKPR.setOnClickListener(this);

		xxbtnKPM = (Button)convertView.findViewById(R.id.xxbtnKPM);
		xxbtnKPM.setOnClickListener(this);

		TextView tv = (TextView)convertView.findViewById(R.id.tltv_content);
		TextView tlTv_Title = (TextView)convertView.findViewById(R.id.tlTv_Title);
		String title=data.get(position).get(0);
		tlTv_Title.setText(title);
		String text="";
		
		text = "档安号:" + data.get(position).get(1) + "\n" 
				+ "名称:"+ data.get(position).get(2)+ "\n"
				+ "GIS编号:" + data.get(position).get(3) + "\n"
				+ "所在路段:" +data.get(position).get(4)+ "\n"
				+"位置描述:"+data.get(position).get(5)+"\n"
				+"关键点类型:"+data.get(position).get(6)+"\n"
				+"管网类型:"+data.get(position).get(7)+"\n"
				+"等级:"+data.get(position).get(8)+"\n"
				+"档案时间:"+data.get(position).get(9)+"\n"
				+"最近检查时间:"+data.get(position).get(10);
		tv.setText(text);
		return convertView;
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.xxbtnKPD:
			xxbtnKPD.setEnabled(false);
			xxbtnKPD.setTextColor(Color.rgb(200, 200, 200));

			toast.setText("进入"+data.get(pos).get(1)+"路段");
			toast.show();
			break;
		case R.id.xxbtnKPR:
			xxbtnKPR.setEnabled(false);
			xxbtnKPR.setTextColor(Color.rgb(200, 200, 200));
	
			toast.setText("进入"+data.get(pos).get(1)+"路段");
			toast.show();
			break;
		case R.id.xxbtnKPM:
			break;
		default:
			break;
		}
	}

}
