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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movementinsome.R;


@SuppressLint("NewApi")
public class ExpGuiPwd extends LinearLayout implements IXmlGuiFormFieldObject {
	Context context;
	String value;
	String rule;
	
	TextView label;
	EditText txtBox;
	
	public ExpGuiPwd(Context context,String labelText,String initialText,String value,String rule,boolean readOnly) {
		super(context);
		
		this.context = context;
		this.value = value;
		this.rule = rule;
		
		label = new TextView(context);
		label.setText(labelText);
		label.setEms(context.getResources().getInteger(R.integer.label_ems));
		label.setGravity(Gravity.CENTER_VERTICAL);
		label.setTextColor(Color.BLACK);
		txtBox = new EditText(context);
		if (value != null){
			txtBox.setText(value);
		}else{
			txtBox.setText(initialText);
		}		txtBox.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
		txtBox.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
		if(readOnly){
			txtBox.setEnabled(false);
		}
		this.addView(label);
		this.addView(txtBox);
		
		txtBox.addTextChangedListener(new TextWatcher() {
		    
		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
		        // TODO Auto-generated method stub
		    }
		    
		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count,
		            int after) {
		        // TODO Auto-generated method stub
		    }
		    
		    @Override
		    public void afterTextChanged(Editable s) {
		        // TODO Auto-generated method stub
		    	onChanged();
		    }
		});
	}

	public ExpGuiPwd(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void makeNumeric()
	{
		DigitsKeyListener dkl = new DigitsKeyListener(true,true);
		txtBox.setKeyListener(dkl);
	}
	public String getValue()
	{
		return txtBox.getText().toString();
	}
	
	public void setValue(String v)
	{
		txtBox.setText(v);
	}

	@Override
	public void autoChangValue(String value) {
		// TODO Auto-generated method stub
		txtBox.setText(value);
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
