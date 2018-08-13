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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.movementinsome.R;

import java.util.Random;


public class ExpGuiRadio extends LinearLayout implements IXmlGuiFormFieldObject {
	String tag = ExpGuiRadio.class.getName();
	Context context;
	String value;
	String rule;
	
	TextView label;
	ArrayAdapter<String> aa;
	//Spinner spinner;
	RadioGroup mRadioGroup;
	
	public ExpGuiRadio(Context context,String labelText,String options,String value,String rule,boolean readOnly) {
		super(context);
		
		this.context = context;
		this.value = value;
		this.rule = rule;
		
		LinearLayout.LayoutParams paramsTv = new LinearLayout.LayoutParams( 
				ViewGroup.LayoutParams.WRAP_CONTENT, 
				ViewGroup.LayoutParams.MATCH_PARENT);
		label = new TextView(context);
		label.setText(labelText);
		label.setEms(context.getResources().getInteger(R.integer.label_ems));
		label.setGravity(Gravity.CENTER_VERTICAL);
		label.setTextColor(Color.BLACK);
		label.setLayoutParams(paramsTv);
		mRadioGroup = new RadioGroup(context);
		mRadioGroup.setGravity(Gravity.CENTER_VERTICAL);
		String[] opts = options.split("\\|");
		String[] values = (value == null?null:value.split("\\,"));
		//aa = new ArrayAdapter<String>( context, android.R.layout.simple_spinner_item,opts);
		//spinner.setAdapter(aa);
		Random rd = new Random();
		for(int i=0;i<opts.length;++i){
			RadioButton rb = new RadioButton(context);
			rb.setId(i);
			rb.setTextColor(Color.BLACK);
			rb.setText(opts[i]);
			if (values!=null){
				for(String v:values){
					if (opts[i].equals(v)){
						rb.setChecked(true);
						break;
					}
			}
			}
			mRadioGroup.addView(rb,LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			mRadioGroup.setOrientation(HORIZONTAL);
			
			rb.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener(){
			   public void onCheckedChanged(CompoundButton btn, boolean value) {
				    //value为CheckBox的值
				   onChanged();
				}
			});		
		}
		if(readOnly){
			mRadioGroup.setEnabled(false);
		}
		
		mRadioGroup.setOnCheckedChangeListener(mChangeRadio);
		this.addView(label);
		this.addView(mRadioGroup);
	}

	public ExpGuiRadio(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	
	public String getValue()
	{
		return value;
	}
	
	private RadioGroup.OnCheckedChangeListener mChangeRadio = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
        	for(int i=0;i<group.getChildCount();++i){
	        	RadioButton rb = (RadioButton)group.getChildAt(i);
	        	/*if(checkedId!=rb.getId()){
	        		rb.setChecked(false);
	        	}else{
	        		rb.setChecked(true);
	        	}*/
	        	if (rb.getId() == checkedId){
		        	value = (String) rb.getText();
	        	}
        	}
        }
    };

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
