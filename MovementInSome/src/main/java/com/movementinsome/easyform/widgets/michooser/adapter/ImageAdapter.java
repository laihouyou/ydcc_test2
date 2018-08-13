package com.movementinsome.easyform.widgets.michooser.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.movementinsome.R;

import java.util.List;

public class ImageAdapter extends BaseAdapter{
	private List<String> listPath;
	private Context context;
	
	public ImageAdapter(Context context,List<String> listPath) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.listPath = listPath;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listPath.size();
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

			//创建一个相对布局relative
			holder.relative = new RelativeLayout(context);

			holder.iv = new ImageView(context);
			holder.iv.setLayoutParams(new Gallery.LayoutParams(85, 85));
			holder.iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
			holder.iv.setPadding(5, 5, 5,5);

			holder.relative.addView(holder.iv);

			convertView = holder.relative;
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder)convertView.getTag();
		}
		String path = listPath.get(position);
//		ImageManager2.from(context).displayImage(holder.iv,
//				path, R.drawable.add_photo_icon, 30, 30);
		Glide.with(context)
				.load(path)
				.error(R.drawable.camera)
				.into(holder.iv);
		return convertView;
	}
	public final class ViewHolder{
        public ImageView iv;
        private RelativeLayout relative;
    }
}
