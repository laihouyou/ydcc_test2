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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.app.pub.util.DensityUtil;
import com.movementinsome.easyform.formengineer.XmlGuiForm;

public class ExpGuiPgTextBox extends LinearLayout implements IXmlGuiFormFieldObject {

//	private BroadcastReceiver taskAssessReceiver = new  BroadcastReceiver (){
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// TODO Auto-generated method stub
//			String action = intent.getAction();
//			if (com.gddst.app.pub.Constant.ASSESS_GRADE.equals(action)) {
//				String s=theForm.getJsonValue();
//				String[] strList = s.split("\\*");
//				String StrVules ="";
//				for (int i = 0; i < strList.length; i++) {
//					String[] Strzhi = strList[i].split("\\,");
//					if(StrVules.equals("")){
//						StrVules+=Strzhi[1];
//					}else{
//					StrVules+=","+Strzhi[1];
//					}
//				}
//				String[] PgLeves=StrVules.split("\\,");
//				String StrPgLeves="";
//				int intLeves=10;
//				for (int j = 0; j < PgLeves.length; j++) {
//					String StrPgLeve=PgLeves[j].substring(2);
//					String LevesStr=PgLeves[j].substring(0,PgLeves[j].length()-2);
//					int Leves=Integer.parseInt(StrPgLeve);//4
//					
//					if(intLeves==10){
//						intLeves=Leves;
//						StrPgLeves=LevesStr+String.valueOf(intLeves);
//					}else if(intLeves>Leves){
//						intLeves=Leves;
//						StrPgLeves=LevesStr+String.valueOf(intLeves);
//					}
//				}
//				PgText.setText(StrPgLeves);
//			}
//		}
//
//	};
	
	XmlGuiForm theForm;
	TextView label;
	TextView PgText;
	Context context;
	String value;
	String rule;
	// 访问后台获取唯一编号(上传参数)
	String tableName;
	String id;
	
	public ExpGuiPgTextBox(final Context context,String labelText,String initialText
			,String value,String rule,boolean readOnly,String tableName,String id, XmlGuiForm theForm) {
		super(context);
		this.context = context;
		this.value = value;
		this.rule = rule;
		this.tableName = tableName;
		this.id = id;
		this.theForm = theForm;
		
		label = new TextView(context);
		label.setText(labelText);
		label.setEms(context.getResources().getInteger(R.integer.label_ems));
		label.setGravity(Gravity.CENTER_VERTICAL);
		label.setTextColor(Color.BLACK);
		label.setWidth(480);
		label.setHeight(DensityUtil.dip2px(context, 45));
		
		PgText = new TextView(context);
		PgText.setText("A1");
		PgText.setEms(context.getResources().getInteger(R.integer.label_ems));
		PgText.setGravity(Gravity.CENTER);
		PgText.setTextColor(Color.BLUE);
		PgText.setLayoutParams(new LayoutParams(80,DensityUtil.dip2px(context, 45)));
		
		this.addView(label);
		this.addView(PgText);
		
//		IntentFilter filter2 = new IntentFilter();
//		filter2.addAction(com.gddst.app.pub.Constant.ASSESS_GRADE);
//		context.registerReceiver(taskAssessReceiver, filter2);
		
		
	}

	public ExpGuiPgTextBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public String getValue()
	{
		return PgText.getText().toString();
	}
	
	public void setValue(String v)
	{
		PgText.setText(v);
	}

	@Override
	public void autoChangValue(String value) {
		// TODO Auto-generated method stub
		PgText.setText(value);
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
