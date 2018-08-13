package com.movementinsome.app.pub.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.movementinsome.R;

public class UserHelpAdapter extends BaseAdapter{

	private String[] chapters;
	private Context context;
	
	public UserHelpAdapter(Context context,String[] chapters) {
		// TODO Auto-generated constructor stub
		this.chapters = chapters;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return chapters.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return chapters[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(context);
		convertView = inflater.inflate(R.layout.pt_list_text, null);
		TextView tv = (TextView) convertView.findViewById(R.id.ptlist_Content);
		TextView tvNumber =  (TextView) convertView.findViewById(R.id.ptlist_Number);
		tvNumber.setText((position+1)+"");
		tv.setTextColor(context.getResources().getColor(R.color.wheat));
		tv.setTextSize(16);
		tv.setText(chapters[position]);
		return convertView;
	}

}
