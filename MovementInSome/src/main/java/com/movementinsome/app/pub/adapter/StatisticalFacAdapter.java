package com.movementinsome.app.pub.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.caice.vo.SavePointVo;

import java.util.List;

public class StatisticalFacAdapter extends BaseAdapter{

	private List<SavePointVo> savePointList;
	private Context context;

	public StatisticalFacAdapter(Context context, List<SavePointVo> savePointList) {
		// TODO Auto-generated constructor stub
		this.savePointList = savePointList;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return savePointList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return savePointList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stubfinal ViewHolder holder;
		final ViewHolder holder;
		if(convertView==null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.statictical_fac_item, null);
			holder.stl_fac_time = (TextView)convertView.findViewById(R.id.stl_fac_time);
			holder.stl_fac_time.setText(savePointList.get(position).getFacPipBaseVo().getUploadTime());
			holder.stl_fac_device = (TextView)convertView.findViewById(R.id.stl_fac_device);
			holder.stl_fac_device.setText(savePointList.get(position).getFacName());
			holder.stl_fac_movetype = (TextView)convertView.findViewById(R.id.stl_fac_movetype);
			holder.stl_fac_movetype.setText(savePointList.get(position).getGatherType());
			holder.stl_fac_addr = (TextView)convertView.findViewById(R.id.stl_fac_addr);
			holder.stl_fac_addr.setText(savePointList.get(position).getHappenAddr());

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	static class ViewHolder {
		TextView stl_fac_time;
		TextView stl_fac_device;
		TextView stl_fac_movetype;
		TextView stl_fac_addr;
	}

}
