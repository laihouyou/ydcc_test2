package com.movementinsome.easyform.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
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

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

public class ExpGuiEditCodeBox extends LinearLayout implements
		IXmlGuiFormFieldObject {
	TextView label;
	EditText txtBox;
	Button btn;
	Context context;
	String value;
	String rule;
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String code = msg.getData().getString("code");
				txtBox.setText(code);
				break;

			default:
				break;
			}
		}
	};

	public ExpGuiEditCodeBox(Context context, String labelText,
			String initialText, String value, String rule, boolean readOnly) {
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
		txtBox = new EditText(context);
		
		btn = new Button(context);
		LinearLayout ll = new LinearLayout(context);
		if (value != null) {
			txtBox.setText(value);
		} else {
			txtBox.setText(initialText);
		}
		if (readOnly) {
			txtBox.setEnabled(false);
		}
		txtBox.setBackgroundResource(R.drawable.input_icon);
		txtBox.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT, 8));
		btn.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 1));
		btn.setBackgroundResource(R.drawable.code);
		txtBox.setBackgroundResource(R.drawable.input_bg);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AppContext.getInstance().setmHandle(mHandler);
				Intent intent =new Intent();
				intent.setClass(ExpGuiEditCodeBox.this.context, CaptureActivity.class);
				ExpGuiEditCodeBox.this.context.startActivity(intent);
			}
		});
		ll.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		ll.addView(txtBox);
		ll.addView(btn);
		this.addView(label);
		this.addView(ll);

		txtBox.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
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

	public ExpGuiEditCodeBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void makeNumeric() {
		DigitsKeyListener dkl = new DigitsKeyListener(true, true);
		txtBox.setKeyListener(dkl);
	}

	public void makeNumber() {
		txtBox.setInputType(InputType.TYPE_CLASS_NUMBER);
	}

	public String getValue() {
		return txtBox.getText().toString();
	}

	public void setValue(String v) {
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
		intent.putExtra("value", "");
		context.sendBroadcast(intent);
	}
}
