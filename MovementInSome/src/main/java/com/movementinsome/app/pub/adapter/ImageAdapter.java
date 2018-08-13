package com.movementinsome.app.pub.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter{
	private List<Bitmap> listBitmap;
	private Context context;
	
	public ImageAdapter(Context context,List<Bitmap> listBitmap) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.listBitmap = listBitmap;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listBitmap.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		
		if(convertView == null){
			holder = new ViewHolder();
			holder.iv = new ImageView(context);
			convertView = holder.iv;
			holder.iv.setLayoutParams(new Gallery.LayoutParams(85, 85));
			holder.iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
			holder.iv.setPadding(5, 5, 5,5);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder)convertView.getTag();
		}
		Bitmap bm = listBitmap.get(position);
		holder.iv.setImageBitmap(bm);
		return convertView;
	}
	public final class ViewHolder{
        public ImageView iv;
    }
}
