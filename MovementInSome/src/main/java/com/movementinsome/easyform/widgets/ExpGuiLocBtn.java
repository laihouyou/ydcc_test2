package com.movementinsome.easyform.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.movementinsome.R;
import com.movementinsome.map.MapBizViewer;

public class ExpGuiLocBtn extends LinearLayout implements IXmlGuiFormFieldObject {

	TextView label;
	Context context;
	String value;
	public ExpGuiLocBtn(Context context,String labelText,final String value,final Integer qty,boolean readOnly) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.value = value;
		label = new TextView(context);
		label.setText(labelText);
		label.setEms(context.getResources().getInteger(R.integer.label_ems));
		label.setGravity(Gravity.CENTER_VERTICAL);
		label.setTextColor(Color.BLACK);
		
		Button loc = new Button(context);
		loc.setBackgroundResource(R.drawable.maplocation);
		if(readOnly){
			loc.setEnabled(false);
		}
		loc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(value!=null&&value.contains("Point")){
					Intent intent = new Intent();
					intent.setClass(ExpGuiLocBtn.this.context, MapBizViewer.class);
					intent.putExtra("strGraph", value);
					intent.putExtra("type", 10006);
					intent.putExtra("qty", qty);
					((Activity) ExpGuiLocBtn.this.context).startActivity(intent);
				}else{
					
					Toast.makeText(ExpGuiLocBtn.this.context, "没有可定位的坐标不能定位", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		this.addView(label); 
		this.addView(loc);
	}
	public String getValue()
	{
		return value;
	}
	@Override
	public void autoChangValue(String value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onChanged() {
		// TODO Auto-generated method stub
		
	}
	

}
