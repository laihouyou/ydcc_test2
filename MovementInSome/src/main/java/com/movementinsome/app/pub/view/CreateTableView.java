package com.movementinsome.app.pub.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Editable.Factory;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.app.pub.dialog.DateTimeDialog;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class CreateTableView {

	private Context context;
	private Resources res;

	public CreateTableView(Context context) {
		super();
		this.context = context;
		res = this.context.getResources();
	}
	public View getTextView(String titel,int inputType,Double Length,int id ,int height,int titelTextSize,int etTextSize,String text,String dateType,int id2){
		LinearLayout cLayout = new LinearLayout(context);
		LinearLayout.LayoutParams paramsLayout = new LinearLayout.LayoutParams( 
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT); 
		cLayout.setGravity(Gravity.CENTER_VERTICAL);
		cLayout.setLayoutParams(paramsLayout);
		cLayout.setPadding(0, 3, 0, 5);
		
		LinearLayout.LayoutParams paramsTv = new LinearLayout.LayoutParams( 
				ViewGroup.LayoutParams.WRAP_CONTENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT); 
		
		TextView tv = new TextView(context);
		tv.setText(titel);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setLayoutParams(paramsTv);
		tv.setTextColor(Color.BLACK);
		tv.setTextSize(titelTextSize);
		tv.setEms(4);
		cLayout.addView(tv);
		LinearLayout.LayoutParams paramsEt = new LinearLayout.LayoutParams( 
				ViewGroup.LayoutParams.MATCH_PARENT, 
				height,1.0f); 
		if("dateType".equals(dateType)){
			final TextView date=new TextView(context);
			date.setBackgroundResource(R.drawable.input_bg);
			date.setId(id);
			date.setPadding(5, 0, 5, 0);
			date.setTextColor(Color.BLACK);
			date.setTextSize(etTextSize);
			date.setFocusable(true);
			date.setEditableFactory(new Factory());
			date.setClickable(true);
			
			
			date.setLayoutParams(paramsEt);
			date.setGravity(Gravity.CENTER_VERTICAL);
			date.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DateTimeDialog.showDateDialog(context, date);
				}
			});
			
			final TextView time=new TextView(context);
			time.setBackgroundResource(R.drawable.input_bg);
			time.setId(id2);
			time.setPadding(5, 0, 5, 0);
			time.setTextColor(Color.BLACK);
			time.setTextSize(etTextSize);
			time.setFocusable(true);
			time.setEditableFactory(new Factory());
			
			time.setLayoutParams(paramsEt);
			time.setGravity(Gravity.CENTER_VERTICAL);
			if("".equals(text)){
				time.setText("");
			}else{
				try{
					Date d=new Date();
					d.setTime(Long.parseLong(text));
					DateFormat df0=android.text.format.DateFormat.getDateFormat(context);
					DateFormat df1=android.text.format.DateFormat.getTimeFormat(context);
					time.setText(df1.format(d));
					date.setText(df0.format(d));
					
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			
			time.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DateTimeDialog.showTimeDialog(context, time);
				}
			});
			
			cLayout.addView(date);
			cLayout.addView(time);
		}else{
			EditText et=new EditText(context);
			
			et.setBackgroundResource(R.drawable.input_bg);
			et.setId(id);
			et.setPadding(5, 0, 5, 0);
			et.setTextColor(Color.BLACK);
			et.setTextSize(etTextSize);
			et.setInputType(inputType);
			et.setText(text);
			et.setFocusable(true);
			et.setEditableFactory(new Factory());
			et.setEnabled(true);
			et.setFilters( new  InputFilter[]{ new  InputFilter.LengthFilter( (int)((double)Length )) });	
			et.setLayoutParams(paramsEt);
			et.setGravity(Gravity.CENTER_VERTICAL);
			cLayout.addView(et);
		}
		return cLayout;
		
	}
	public View getSpinner(String titel,int titelTextSize,List<String> items,int height,int id){
		LinearLayout cLayout = new LinearLayout(context);
		LinearLayout.LayoutParams paramsLayout = new LinearLayout.LayoutParams( 
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT); 
		cLayout.setGravity(Gravity.CENTER_VERTICAL);
		cLayout.setLayoutParams(paramsLayout);
		cLayout.setPadding(0, 3, 0, 5);
		
		LinearLayout.LayoutParams paramsTv = new LinearLayout.LayoutParams( 
				ViewGroup.LayoutParams.WRAP_CONTENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT); 
		
		TextView tv = new TextView(context);
		tv.setText(titel);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setLayoutParams(paramsTv);
		tv.setTextColor(Color.BLACK);
		tv.setTextSize(titelTextSize);
		tv.setEms(4);
		
		LinearLayout.LayoutParams paramsSp = new LinearLayout.LayoutParams( 
				ViewGroup.LayoutParams.MATCH_PARENT, 
				height); 
		Spinner spinner=new Spinner(context);
		spinner.setLayoutParams(paramsSp);
		spinner.setBackgroundResource(R.drawable.spinner_bg);
		spinner.setPadding(30, 0, 0, 0);
		spinner.setId(id);
		spinner.setAdapter(new ArrayAdapter<String>(context,R.anim.myspinner,items));
		
		cLayout.addView(tv);
		cLayout.addView(spinner);
		return cLayout;
	}
}
