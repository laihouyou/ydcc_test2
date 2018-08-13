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
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.app.pub.util.DensityUtil;

@SuppressLint("NewApi")
public class ExpGuiDate extends LinearLayout  implements IXmlGuiFormFieldObject{
	String tag = ExpGuiDate.class.getName();
	Context context;
	TextView label;
	ArrayAdapter<String> aa;
	ArrowEditText txtBox;
	Button clickButton;
	String value;
	String rule;


	public ExpGuiDate(Context context, String labelText, String options,String value,String rule,boolean readOnly) {
		super(context);
		
		this.context = context;
		this.value = value;
		this.rule = rule;


		label = new TextView(context);
		label.setText(labelText);
		label.setEms(context.getResources().getInteger(R.integer.label_ems));
		label.setGravity(Gravity.CENTER_VERTICAL);
		label.setTextColor(Color.BLACK);
		txtBox = new ArrowEditText(context,"date",readOnly);
		txtBox.setTextSize(14);
		txtBox.setText(value);
		txtBox.setEnabled(false);
		txtBox.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_CLASS_DATETIME);
		txtBox.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				DensityUtil.dip2px(context, 45)));
		if(readOnly){
			txtBox.setClickable(false);
			txtBox.setEnabled(false);
		}
/*		clickButton = new Button(context);
		// clickButton.setBackgroundColor(R.drawable.arrow_drawer);
		// clickButton.setWidth(200);
		// clickButton.setHeight(200);
		clickButton.setVisibility(View.VISIBLE);
		clickButton.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));*/

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
		//this.addView(clickButton);

/*		clickButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				;
			}
		});*/
	}

	public ExpGuiDate(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public String getValue() {
		return txtBox.getText().toString();
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
