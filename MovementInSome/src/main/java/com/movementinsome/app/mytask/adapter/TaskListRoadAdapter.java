package com.movementinsome.app.mytask.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.movementinsome.map.MapBizViewer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TaskListRoadAdapter extends BaseAdapter{

	private List<List<String>> data;
	private Context context;
	private boolean[] isClick;

	
	//private int pos;
	private Toast toast;

	
	public TaskListRoadAdapter(List<List<String>> data,Context context){
		this.data=data;
		this.context = context;
		
		isClick=new boolean[data.size()];
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			LayoutInflater layout = LayoutInflater.from(context);
			convertView = layout.inflate(R.layout.tasklist_road_list, null);
		}
		
		//pos = position;
		toast = Toast.makeText(this.context, "", 1);
		toast.setGravity(Gravity.CENTER, 0, 0);
		
		final Button xxbtnStart;// 开始1
		final Button xxbtnEnd;// 完成1
		final Button xxbtnMap;// 完成1
		
		xxbtnStart = (Button)convertView.findViewById(R.id.xxbtnStart);
		if(!isClick[position]){
			xxbtnStart.setEnabled(true);
			xxbtnStart.setTextColor(Color.BLACK);
		}else{
			xxbtnStart.setEnabled(false);
			xxbtnStart.setTextColor(Color.rgb(200, 200, 200));
		}
		xxbtnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				xxbtnStart.setEnabled(false);
				xxbtnStart.setTextColor(Color.rgb(200, 200, 200));
				isClick[position]=true;

				//data.get(position).get(4). = new Date();
				
				Date date=new Date();
		        SimpleDateFormat dateformat1=new SimpleDateFormat("yyyyMMdd_hhmmss");
		        String timeString=dateformat1.format(date);
		        data.get(position).set(4, timeString);
				toast.setText("进入"+data.get(position).get(1)+"路段");
				toast.show();
			}
		});
		

				
		
		xxbtnEnd = (Button)convertView.findViewById(R.id.xxbtnEnd);
		xxbtnEnd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				xxbtnEnd.setEnabled(false);
				xxbtnEnd.setTextColor(Color.rgb(200, 200, 200));

				Date date=new Date();
		        SimpleDateFormat dateformat1=new SimpleDateFormat("yyyyMMdd_hhmmss");
		        String timeString=dateformat1.format(date);
		        data.get(position).set(4, timeString);
				toast.setText("退出"+data.get(position).get(1)+"路段");
				toast.show();
			}
		});

		xxbtnMap = (Button)convertView.findViewById(R.id.xxbtnMap);
		xxbtnMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
/*				xxbtnEnd.setEnabled(false);
				xxbtnEnd.setTextColor(Color.rgb(200, 200, 200));*/

				Intent intent =new Intent();
				intent.putExtra("type", MapBizViewer.BIZ_MAP_OPERATE_INS_LOCATE);
				int mapid= Integer.parseInt(data.get(position).get(6).toString().trim());
				intent.putExtra("mapid", mapid);
				intent.setClass(context, MapBizViewer.class);
				context.startActivity(intent);
				/*toast.setText("进入"+data.get(position).get(1)+"路段");
				toast.show();*/
			}
		});

		TextView tv = (TextView)convertView.findViewById(R.id.tltv_content);
		TextView tlTv_Title = (TextView)convertView.findViewById(R.id.tlTv_Title);
		String title=data.get(position).get(0);
		tlTv_Title.setText(title);
		String text="";
		
		text = "路段:" + data.get(position).get(1) + "\n" 
				+ "等级:"+ data.get(position).get(2)+ "\n"
				+ "最近巡查：" + data.get(position).get(3) + "\n"
				+ "本次进入:" +data.get(position).get(4)+ "\n"
				+"本次退出:"+data.get(position).get(5);
		tv.setText(text);
		return convertView;
	}

}
