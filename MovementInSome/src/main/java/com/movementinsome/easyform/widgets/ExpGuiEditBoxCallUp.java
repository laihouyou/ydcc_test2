package com.movementinsome.easyform.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.movementinsome.R;
import com.movementinsome.app.pub.util.DensityUtil;

public class ExpGuiEditBoxCallUp extends LinearLayout implements IXmlGuiFormFieldObject {
	TextView label;
	EditText txtBox;
	Context context;
	String value;
	String rule;
	Button btn;
	
	public ExpGuiEditBoxCallUp(final Context context,String labelText,String initialText,String value,String rule,boolean readOnly) {
		super(context);
		this.context = context;
		this.value = value;
		this.rule = rule;
		
		label = new TextView(context);
		label.setText(labelText);
		label.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		label.setEms(context.getResources().getInteger(R.integer.label_ems));
		label.setGravity(Gravity.CENTER_VERTICAL);
		label.setTextColor(Color.BLACK);
		btn = new Button(context);
		btn.setLayoutParams(new LayoutParams(
				DensityUtil.dip2px(context, 42),
				DensityUtil.dip2px(context, 42)));
		btn.setBackgroundResource(R.drawable.phone_callup);
		
		txtBox = new EditText(context);
		if (value != null){
			txtBox.setText(value);
		}else{
			txtBox.setText(initialText);
		}
		if(readOnly){
			txtBox.setEnabled(false);
		}
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String number = txtBox.getText().toString(); 
				if(!"".equals(number)){
					//用intent启动拨打电话  
					new AlertDialog.Builder(context).setTitle("提示")
					.setIcon(android.R.drawable.ic_menu_help)
					.setMessage("确定拨打"+number+"号码")
					.setPositiveButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					})
					.setNegativeButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number));  
							context.startActivity(intent);
						}
					}).show();
					  
				}else{
					Toast.makeText(context, "电话号不能为空！", Toast.LENGTH_LONG).show();
				}
			}
		});
		txtBox.setBackgroundResource(R.drawable.input_icon);
		txtBox.setTextSize(14);
		txtBox.setGravity(Gravity.CENTER_VERTICAL);
		txtBox.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,DensityUtil.dip2px(context, 45),1));
		LinearLayout ll = new LinearLayout(context);
		ll.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		ll.addView(txtBox);
		ll.addView(btn);
		ll.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(label);
		this.addView(ll);
		
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

	public ExpGuiEditBoxCallUp(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void makeNumeric()
	{
		DigitsKeyListener dkl = new DigitsKeyListener(true,true);
		txtBox.setKeyListener(dkl);
	}
	public void makeNumber(){
		txtBox.setInputType(InputType.TYPE_CLASS_NUMBER);
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
