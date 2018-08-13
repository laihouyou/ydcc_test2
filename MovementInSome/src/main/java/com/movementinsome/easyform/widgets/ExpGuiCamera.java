/*
 * XmlGui application.
 * Written by Frank Ableson for IBM Developerworks
 * June 2010
 * Use the code as you wish -- no warranty of fitness, etc, etc.
 */
package com.movementinsome.easyform.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.app.pub.util.DensityUtil;
import com.movementinsome.easyform.widgets.AddCameraView.DataSetSupervioer;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class ExpGuiCamera extends LinearLayout implements IXmlGuiFormFieldObject {
	String tag = ExpGuiCamera.class.getName();
	Context context;
	TextView label;
	ArrayAdapter<String> aa;
	Button clickButton;
	String value;
	String rule;
	AddCameraView addCameraView;
	String bizType;

	public ExpGuiCamera(Context context, String labelText, String options,String value,String rule
			,String bizType,Integer qty,boolean readOnly,String photographAction) {
		super(context);
		this.context = context;
		this.value = value;
		this.rule = rule;
		this.bizType = bizType;

		label = new TextView(context);
		label.setText(labelText);	
		label.setEms(context.getResources().getInteger(R.integer.label_ems));
		label.setGravity(Gravity.CENTER_VERTICAL);
		label.setTextColor(Color.BLACK);
		label.setLayoutParams(new LinearLayout.LayoutParams( 
				ViewGroup.LayoutParams.WRAP_CONTENT, DensityUtil.dip2px(context, 71)));
		addCameraView=new AddCameraView(context,this,bizType,qty,photographAction);
		
		this.addView(label);
		GridView gv=null;
		if (value != null){
			ArrayList<String> imageList = new ArrayList<String>();
			String[] values = value.split("\\,");
			if (values!=null&&!"".equalsIgnoreCase(value)){
				for(String v:values){
					imageList.add(v);
				}
			}
			gv = addCameraView.getCameraView(imageList);
		}else{
			gv = addCameraView.getCameraView(new ArrayList<String>());
		}
		if(readOnly){
			gv.setEnabled(false);
		}
		this.addView(gv);
		addCameraView.setDataSetChangeListener(new DataSetSupervioer() {
            public void onChange() {
            	 onChanged();
            }
        });
		
	}

	public ExpGuiCamera(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public String getValue() {
		String value ="";
		for(String v:addCameraView.getImgePathList()){
			if ("".equals(value)){
				value = v;
			}else{
				value+=","+v;
			}
		}
		return value;
	}

	@Override
	public void autoChangValue(String value) {
		// TODO Auto-generated method stub
		this.value = value;
	}

	@Override
	public void onChanged() {
		// TODO Auto-generated method stub
		Intent intent = new Intent("RunForm");
		intent.putExtra("req", "onChanged");  
		intent.putExtra("value","");
		context.sendBroadcast(intent);
	}


}
