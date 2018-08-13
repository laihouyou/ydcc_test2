package com.movementinsome.easyform.widgets.michooser.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.movementinsome.R;

import java.io.File;
import java.util.ArrayList;

public class GridImageAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> dataList;
	private DisplayMetrics dm;

	public GridImageAdapter(Context c, ArrayList<String> dataList) {

		mContext = c;
		this.dataList = dataList;
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);

	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;

		if (convertView==null){
			holder = new ViewHolder();
			//创建一个相对布局relative
			holder.relative = new RelativeLayout(mContext);

			holder.iv = new ImageView(mContext);
			holder.iv.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, dipToPx(65)));
			holder.iv.setScaleType(ImageView.ScaleType.FIT_XY);
			holder.iv.setAdjustViewBounds(true);;

			holder.relative.addView(holder.iv);

			convertView = holder.relative;
			convertView.setTag(holder);
		}else {
			holder= (ViewHolder) convertView.getTag();
		}

		String path;
		if (dataList != null && position<dataList.size() )
			path = dataList.get(position);
		else
			path = "camera_default";
		Log.i("path", "path:"+path+"::position"+position);
		if (path.contains("default"))
			Glide.with(mContext).load(R.drawable.add_photo_icon).into(holder.iv);
//			imageView.setImageResource(R.drawable.add_photo_icon);
		else{
//            ImageManager2.from(mContext).displayImage(imageView, path,R.drawable.add_photo_icon,100,100);
			Glide.with(mContext)
					.load(new File(path))
					.error(R.drawable.add_photo_icon)
					.override(100,100)
					.into(holder.iv);
		}
		return holder.relative;
	}

	public final class ViewHolder{
		public ImageView iv;
		private RelativeLayout relative;
	}

	public int dipToPx(int dip) {
		return (int) (dip * dm.density + 0.5f);
	}

}
