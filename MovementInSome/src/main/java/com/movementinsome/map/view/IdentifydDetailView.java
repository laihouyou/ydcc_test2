package com.movementinsome.map.view;


import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.movementinsome.R;

public class IdentifydDetailView extends RelativeLayout{

	public IdentifydDetailView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		View v=View.inflate(context, R.layout.identify_detail_view, null);
		addView(v);
	}

}
