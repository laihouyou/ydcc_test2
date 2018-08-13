package com.movementinsome.map.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.movementinsome.R;

import java.util.List;


public class IdentifyAdapter extends BaseAdapter {

	private Activity context;
	private int num;
	private List<String> list;
	private LayoutInflater inflater;
	private int temp = 0;
	//private IdentifyDialog activity;

	private int position = 0;
	private View  cview;

	public IdentifyAdapter(Activity context, int num, List<String> list,View cview) {
		super();
		this.context = context;
		this.num = num;
		//this.activity = activity;
		this.list = list;
		this.cview=cview;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.inflater.inflate(num, null);
            holder.tv = (TextView)convertView.findViewById(R.id.idf_list_tv);
            holder.tv.setText("("+(position+1)+")编号:"+list.get(position));
            
            holder.radioButton = (RadioButton)convertView.findViewById(R.id.idf_list_radio);
            
            holder.radioButton.setChecked(false);
            holder.radioButton.setId(position);
           // if(position == 0)  holder.radioButton.setChecked(true);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.radioButton.setId(position);
        }
		//convertView = inflater.inflate(num, null);
		//TextView tv = (TextView)convertView.findViewById(R.id.idf_list_tv);
		//tv.setText("("+(position+1)+")编号:"+list.get(position));
		//RadioButton radio = (RadioButton)convertView.findViewById(R.id.idf_list_radio);
		//radio.setId(position);
        
		if(position == 0) holder.radioButton.setChecked(true);
		
		holder.radioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				 if (isChecked) {
                    if (temp != -1) {
                        RadioButton tempButton = (RadioButton) cview.findViewById(temp);
                        if (tempButton != null) {
                            tempButton.setChecked(false);
                        }
                    }
                    temp = buttonView.getId();
                    setPosition(temp);
                }
			}
		});
		
		if (position == temp) {
            holder.radioButton.setChecked(true);
        } else {
            holder.radioButton.setChecked(false);
        }
        return convertView;
	}


	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}


	 private class ViewHolder {
		 TextView tv;
	     RadioButton radioButton;
	 }
	 
	    

}
