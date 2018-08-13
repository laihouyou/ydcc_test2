package com.movementinsome.app.pub.util;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.movementinsome.R;


public class WidgetSetUtil {
	
	public void RunformLintyoutSet(LinearLayout linearLayout, Context context, boolean isfs) {
		linearLayout.setPadding(20, 0, 0, 0);
		if(isfs){
			linearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,DensityUtil.dip2px(context, 45)));
			linearLayout.setBackgroundResource(R.drawable.icon_linyout_back_);
		}
	}

}
