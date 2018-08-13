/*
 * XmlGui application.
 * Written by Frank Ableson for IBM Developerworks
 * June 2010
 * Use the code as you wish -- no warranty of fitness, etc, etc.
 */
package com.movementinsome.easyform.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movementinsome.R;

import java.util.Random;
import java.util.Vector;


public class ExpGuiCheck extends LinearLayout implements IXmlGuiFormFieldObject {
	String tag = ExpGuiCheck.class.getName();
	TextView label;
	String ids="";
	Context context;
	String value = "";
	String rule;
	Vector<CheckBox> checkBoxs = new Vector<CheckBox>();
	
	public ExpGuiCheck(Context context,String labelText,String options,String value,String rule,boolean readOnly) {
		super(context);
		/*LinearLayout.LayoutParams paramsTv = new LinearLayout.LayoutParams( 
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT);*/
		this.context = context;
		this.value = value;
		this.rule = rule;
		
		label = new TextView(context);
		label.setText(labelText);
		label.setEms(context.getResources().getInteger(R.integer.label_ems));
		label.setGravity(Gravity.CENTER_VERTICAL);
		label.setTextColor(Color.BLACK);
	//	label.setLayoutParams(paramsTv);
		String []opts = options.split("\\|");
		String[] values = (value == null?null:value.split("\\,"));
		
		this.setOrientation(android.widget.LinearLayout.HORIZONTAL);
		this.addView(label);
		Random rd = new Random();
		for(String opt:opts){
			CheckBox cb = new CheckBox(context);
			cb.setId(rd.nextInt());
			cb.setText(opt);
			cb.setTextColor(Color.BLACK);
			if (values!=null){
				for(String v:values){
					if (opt.equals(v)){
						cb.setChecked(true);
						break;
					}
				}
			}
			if(readOnly){
				cb.setEnabled(false);
			}
			this.addView(cb);
			checkBoxs.add(cb);
			if ("".equals(ids)){
				ids = String.valueOf(cb.getId());
			}else{
				ids+=","+String.valueOf(cb.getId());
			}
			cb.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
			   public void onCheckedChanged(CompoundButton btn, boolean value) {
				    //value为CheckBox的值
				   onChanged();
				}
			});
		}
	}

	public ExpGuiCheck(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	
	public String getValue()
	{
		value = "";
		String [] id = ids.split("\\,");
		for(String i:id){
			for(CheckBox cb:checkBoxs){
				if (cb.getId() == Integer.valueOf(i)){
					if (cb.isChecked()){
						if ("".equals(value)){
							value = (String) cb.getText();
						}else{
							value += ","+cb.getText();
						}
					}
				}
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
