/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.movementinsome.caice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.movementinsome.R;
import com.movementinsome.caice.activity.OfflineActivity;

import java.util.ArrayList;


public class AllCitiesAdapter extends BaseAdapter {
	private Context mContext;
    private ArrayList<MKOLSearchRecord>  mList;
    private LayoutInflater layoutInflater;

    public AllCitiesAdapter(Context context ,ArrayList<MKOLSearchRecord> records) {
    	this.mList=records;
    	this.mContext=context;
        layoutInflater = LayoutInflater.from( context);
      
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NodeViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_all_cities, null);
            holder = new NodeViewHolder();
            holder.tv_city = (TextView) convertView.findViewById(R.id.tv_city);
            holder.tv_in_the_download = (TextView) convertView.findViewById(R.id.tv_in_the_download);
            holder.download_size = (TextView) convertView.findViewById(R.id.download_size);
            holder.download_man_size = (TextView) convertView.findViewById(R.id.download_man_size);
            holder.download_icon = (ImageView) convertView.findViewById(R.id.download_icon);
            convertView.setTag(holder);
        } else {
            holder = (NodeViewHolder) convertView.getTag();
        }
        
        holder.tv_city.setText(mList.get(position).cityName+"");
        holder.download_size.setText(OfflineActivity.formatDataSize(mList.get(position).size));
        
        return convertView;
    }

    private class NodeViewHolder {

        private TextView tv_city;
        private TextView tv_in_the_download;
        private TextView download_size;
        private TextView download_man_size;
        private ImageView download_icon;
    }

    public ArrayList<MKOLSearchRecord> getmList() {
        return mList;
    }

    public void setmList(ArrayList<MKOLSearchRecord> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

}
