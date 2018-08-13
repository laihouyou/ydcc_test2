package com.movementinsome.app.mytask.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.app.mytask.handle.MoveTaskListCentreBaseHandle;
import com.movementinsome.app.mytask.handle.MoveTaskListCentreHandle;
import com.movementinsome.caice.vo.ProjectVo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoveTaskListAdapter extends BaseAdapter{

	private Activity context;
	private List<Map<String, Object>>  data;
	public MoveTaskListAdapter(List<Map<String, Object>> data,
							   Activity context){
		this.context=context;
		this.data=data;
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
		final ProjectVo projectVo = (ProjectVo) data
				.get(position).get("ProjectVo");
		final List<MoveTaskListCentreBaseHandle> moveTaskListCentreBaseHandle=new ArrayList<MoveTaskListCentreBaseHandle>();
		moveTaskListCentreBaseHandle.add(new MoveTaskListCentreHandle(context, projectVo,this));
		final ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView=View.inflate(context, R.layout.move_tasklist_item, null);
			holder.move_name = (TextView)convertView.findViewById(R.id.move_name);
			holder.move_type = (TextView)convertView.findViewById(R.id.move_type);
			holder.move_value_num = (TextView)convertView.findViewById(R.id.move_value_num);
			holder.move_time = (TextView)convertView.findViewById(R.id.move_time);
			holder.move_stuas = (Button)convertView.findViewById(R.id.move_stuas);

			holder.move_check = (LinearLayout)convertView.findViewById(R.id.move_check);
			holder.move_surveying = (LinearLayout)convertView.findViewById(R.id.move_surveying);
			holder.move_delect = (LinearLayout)convertView.findViewById(R.id.move_delect);
			holder.move_finsh = (LinearLayout)convertView.findViewById(R.id.move_finsh);
			holder.facility_Re= (RelativeLayout) convertView.findViewById(R.id.facility_Re);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.move_check.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<moveTaskListCentreBaseHandle.size();++i){
					moveTaskListCentreBaseHandle.get(i).showMsgHandler();
				}
			}
		});

		holder.move_surveying.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<moveTaskListCentreBaseHandle.size();++i){
					moveTaskListCentreBaseHandle.get(i).startWorkHandler();
				}
			}
		});

		holder.move_delect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<moveTaskListCentreBaseHandle.size();++i){
					try {
						moveTaskListCentreBaseHandle.get(i).delect();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});

		holder.move_finsh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<moveTaskListCentreBaseHandle.size();++i){
					moveTaskListCentreBaseHandle.get(i).finishHandler();
				}
			}
		});
		holder.facility_Re.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<moveTaskListCentreBaseHandle.size();++i){
					moveTaskListCentreBaseHandle.get(i).facilityList();
				}
			}
		});

		Map<String, View> vs = new HashMap<String, View>();
		vs.put("move_name", holder.move_name);
		vs.put("move_type", holder.move_type);
		vs.put("move_value_num", holder.move_value_num);
		vs.put("move_time", holder.move_time);
		vs.put("move_surveying", holder.move_surveying);
		vs.put("move_finsh", holder.move_finsh);
		vs.put("move_stuas", holder.move_stuas);

		for(int i=0;i<moveTaskListCentreBaseHandle.size();++i){
			moveTaskListCentreBaseHandle.get(i).controlUI(vs);
		}

		return convertView;
	}

	static class ViewHolder {
		public TextView move_name;
		public TextView move_type;
		public TextView move_value_num;
		public TextView move_time;
		public Button move_stuas;

		public LinearLayout move_check;
		public LinearLayout move_surveying;
		public LinearLayout move_delect;
		public LinearLayout move_finsh;
		public RelativeLayout facility_Re;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
		notifyDataSetChanged();
	}
}
