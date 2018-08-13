package com.movementinsome.easyform.widgets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.movementinsome.R;
import com.movementinsome.app.pub.util.DensityUtil;
import com.movementinsome.kernel.util.MyDateTools;

import java.util.Date;

public class ExpGuiDateTime extends LinearLayout implements IXmlGuiFormFieldObject{
	
	String tag = ExpGuiDate.class.getName();
	Context context;
	TextView label;
	ArrowEditText txtBoxDate;
	ArrowEditText txtBoxTime;
	Button clickButton;
	String value;
	String rule;
	ArrowEditText minTime;
	ArrowEditText minDate;
	String minorDataId;
	String minorTimeId;

	public ExpGuiDateTime(final View fGroup,Context context, String labelText, String options
			,String value,String rule,boolean readOnly,String photographAction
			,String dataId,String timeId,String minorDataId, String minorTimeId) {
		super(context);
		
		this.minorTimeId = minorTimeId;
		this.minorDataId = minorDataId;
		this.context = context;
		this.value = value;
		this.rule = rule;

		label = new TextView(context);
		label.setText(labelText);
		label.setEms(context.getResources().getInteger(R.integer.label_ems));
		label.setGravity(Gravity.CENTER_VERTICAL);
		label.setTextColor(Color.BLACK);
		txtBoxDate = new ArrowEditText(context,"date",readOnly);
		txtBoxDate.setTextSize(14);
		txtBoxDate.setEnabled(false);
		if(dataId!=null&&!"".equals(dataId)){
			try{
				txtBoxDate.setId(Integer.parseInt(dataId));
			}catch(Exception e){
			}
		}
		txtBoxDate.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_CLASS_DATETIME);
		txtBoxDate.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				DensityUtil.dip2px(context, 45),1));
		txtBoxTime = new ArrowEditText(context,"time",readOnly);
		if(timeId!=null&&!"".equals(timeId)){
			try{
				txtBoxTime.setId(Integer.parseInt(timeId));
			}catch(Exception e){
			}
		}
		txtBoxTime.setEnabled(false);
		txtBoxTime.setTextSize(14);
		txtBoxTime.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_CLASS_DATETIME);
		txtBoxTime.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				DensityUtil.dip2px(context, 45),1));
		if(value!=null){
			String []values=value.split(" ");
			if(values.length==2){
				txtBoxDate.setText(values[0]);
				txtBoxTime.setText(values[1]);
			}
		}
		if(readOnly){
			txtBoxDate.setEnabled(false);
			txtBoxTime.setEnabled(false);
		}
		if(ExpGuiDateTime.this.minorDataId!=null&&!"".equals(ExpGuiDateTime.this.minorDataId)){
			minDate = (ArrowEditText) fGroup.findViewById(Integer.parseInt(ExpGuiDateTime.this.minorDataId));
		}
		if(ExpGuiDateTime.this.minorTimeId!=null&&!"".equals(ExpGuiDateTime.this.minorTimeId)){
			minTime = (ArrowEditText) fGroup.findViewById(Integer.parseInt(ExpGuiDateTime.this.minorTimeId));
		}
		this.addView(label);
		this.addView(txtBoxDate);
		this.addView(txtBoxTime);
		txtBoxDate.addTextChangedListener(new TextWatcher() {
		    
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
		
		txtBoxTime.addTextChangedListener(new TextWatcher() {
		    
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
		    	if(minDate!=null&&minTime!=null){
		    		Date d1 = MyDateTools.string2Date(minDate.getText()+" "+minTime.getText(), "yyyy-MM-dd HH:mm:ss");
					Date d0 = MyDateTools.string2Date(txtBoxDate.getText()+" "+txtBoxTime.getText(), "yyyy-MM-dd HH:mm:ss");
					if(d1!=null&&d0!=null&&d0.getTime()<d1.getTime()){
						Toast.makeText(ExpGuiDateTime.this.context, "时间不正确，请查看填写的时间", 1).show();
					}
		    	}
		    }
		});
		if(photographAction!=null&&!"".equals(photographAction)){
			MyBroadcast myBroadcast=new MyBroadcast();
			IntentFilter filter = new IntentFilter();
			filter.addAction(photographAction);
			context.registerReceiver(myBroadcast, filter);
		}
	}

	public ExpGuiDateTime(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public String getValue() {
		return txtBoxDate.getText().toString()+" "+txtBoxTime.getText().toString();
	}

	@Override
	public void autoChangValue(String value) {
		// TODO Auto-generated method stub
		if(value!=null){
			String []values=value.split(" ");
			if(values.length==2){
				txtBoxDate.setText(values[0]);
				txtBoxTime.setText(values[1]);
			}
		}
		
	}

	@Override
	public void onChanged() {
		// TODO Auto-generated method stub
		Intent intent = new Intent("RunForm");
		intent.putExtra("req", "onChanged");  
		intent.putExtra("value","");
		context.sendBroadcast(intent);
	}
	private class MyBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if (intent != null) {
				String date = MyDateTools.date2String(new Date());
				String[]d=date.split(" ");
				if(d.length==2){
					txtBoxDate.setText(d[0]);
					txtBoxTime.setText(d[1]);
				}
			}
		}

	}

}
