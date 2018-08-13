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
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.app.pub.util.DensityUtil;
import com.movementinsome.database.vo.GeometryVO;
import com.movementinsome.map.utils.GeometryUtility;

@SuppressLint("NewApi")
public class ExpGuiMapEditText extends LinearLayout  implements IXmlGuiFormFieldObject {
	String tag = ExpGuiMapEditText.class.getName();
	Context context;
	TextView label;
	MapEditText txtBox;
	TextView showTv;
	Button clickButton;
	String value;
	String rule;


	public ExpGuiMapEditText(Context context, String labelText, String initialText,String value,String rule,boolean readOnly) {
		super(context);
		
		this.context = context;
		this.rule = rule;

		label = new TextView(context);
		label.setText(labelText);
		label.setEms(context.getResources().getInteger(R.integer.label_ems));
		label.setGravity(Gravity.CENTER_VERTICAL);
		label.setTextColor(Color.BLACK);
		label.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT));
		showTv = new TextView(context);
		
		txtBox = new MapEditText(context,initialText,rule,readOnly,showTv);
		if(value!=null&&value.contains("Point")){
			GeometryVO geometryVO = GeometryUtility.coordToGeometry(value);
			if(geometryVO!=null&&geometryVO.getPoints()!=null&&geometryVO.getPoints().size()>=2){
				this.value = value=geometryVO.getPoints().get(0)+" "+geometryVO.getPoints().get(1);
			}
		}
		if(value!=null&&!"".equals(value)&&value.contains(" ")){
			showTv.setText("已获取坐标");
		}else if("maparea()".equals(value)){
			showTv.setText("");
		}else{
			showTv.setText(value);
		}
		showTv.setPadding(18, 0, 18, 0);
		showTv.setTextSize(14);
		showTv.setGravity(Gravity.CENTER_VERTICAL);
		showTv.setTextColor(Color.BLACK);
		showTv.setBackgroundResource(R.drawable.input_icon);
		showTv.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				DensityUtil.dip2px(context, 45)));
		
		txtBox.setText(value);
		txtBox.setTextSize(14);
		txtBox.setBackgroundResource(R.drawable.input_icon);
		//txtBox.setEnabled(false);
		//txtBox.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_CLASS_DATETIME);
		txtBox.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				DensityUtil.dip2px(context, 45)));
/*		clickButton = new Button(context);
		// clickButton.setBackgroundColor(R.drawable.arrow_drawer);
		// clickButton.setWidth(200);
		// clickButton.setHeight(200);
		clickButton.setVisibility(View.VISIBLE);
		clickButton.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));*/
		if(readOnly){
			txtBox.setEnabled(false);
		}
		this.addView(label);
		this.addView(showTv);
		
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

	public ExpGuiMapEditText(Context context, AttributeSet attrs) {
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
		showTv.setText(value);
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
