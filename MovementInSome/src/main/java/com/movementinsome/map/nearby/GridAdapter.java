package com.movementinsome.map.nearby;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.kernel.initial.model.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GridAdapter  extends BaseAdapter{

	private ArrayList<HashMap<String, Object>>data;
	private Context context;
	private List<Module> lstModule;
	public GridAdapter(Context context,ArrayList<HashMap<String, Object>> data,List<Module> lstModule){
		this.context=context;
		this.data=data;
		this.lstModule=lstModule;
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
			convertView=View.inflate(context, R.layout.nearbygridview_item, null);
		}
		ImageView img=(ImageView) convertView.findViewById(R.id.grid_img);
		img.setBackgroundResource((Integer) data.get(position).get("itemImage"));// R.drawable.ic_launcher
		img.setOnClickListener(new TableOnClickListener((String) data.get(position).get("intentId"), context,lstModule));
		TextView tv=(TextView) convertView.findViewById(R.id.grid_text);
		tv.setText((String) data.get(position).get("itemText"));
		return convertView;
	}

}
