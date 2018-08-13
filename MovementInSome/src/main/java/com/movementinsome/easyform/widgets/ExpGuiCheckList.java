package com.movementinsome.easyform.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.movementinsome.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.Vector;

public class ExpGuiCheckList extends LinearLayout implements
		IXmlGuiFormFieldObject {
	String tag = ExpGuiCheck.class.getName();

	Context context;
	String value = "";
	String rule;
	Vector<CheckBox> checkBoxsY = new Vector<CheckBox>();
	Vector<CheckBox> checkBoxsN = new Vector<CheckBox>();
	Vector<Spinner> spinner = new Vector<Spinner>();
	Vector<EditText> editText = new Vector<EditText>();
	Vector<Button> button = new Vector<Button>();
	Vector<View> view = new Vector<View>();
	String[] opts;
	String bsIcsName;
	String bsCheck;

	public ExpGuiCheckList(Context context, String options, String value,
			String rule, boolean readOnly,String bsIcsName,String bsCheck) {
		super(context);
		/*
		 * LinearLayout.LayoutParams paramsTv = new LinearLayout.LayoutParams(
		 * ViewGroup.LayoutParams.MATCH_PARENT,
		 * ViewGroup.LayoutParams.WRAP_CONTENT);
		 */
		this.context = context;
		this.value = value;
		this.rule = rule;
		this.bsIcsName = bsIcsName;
		this.bsCheck= bsCheck;

		// label.setLayoutParams(paramsTv);
		opts = options.split("\\|");
		String[] values = (value == null ? null : value.split("\\,"));

		this.setOrientation(android.widget.LinearLayout.VERTICAL);
		Random rd = new Random();
		/*
		 * 单选 radio （下拉式）单选 choice 多选 check （下拉式）多选 mselect 单行文本输入 text 多行文本输入
		 * memo 整型数字输入 number 浮点型输入 numeric 密码输入 password 时间输入 time 日期输入 date
		 * 时间日期输入 datetime
		 */
		for (String opt : opts) {
			String[] s = opt.split(":");
			View v = View.inflate(context, R.layout.check_list_item_view, null);

			if(s.length>2){
				TextView check_list_item_msg = (TextView) v
						.findViewById(R.id.check_list_item_msg);
				check_list_item_msg.setText(s[0]);
				LinearLayout check_list_item_msg_checkbox = (LinearLayout) v
						.findViewById(R.id.check_list_item_msg_checkbox);
				CheckBox check_list_item_msg_y = (CheckBox) v
						.findViewById(R.id.check_list_item_msg_y);
				CheckBox check_list_item_msg_n = (CheckBox) v
						.findViewById(R.id.check_list_item_msg_n);
				Spinner check_list_item_msg_spinner = (Spinner) v
						.findViewById(R.id.check_list_item_msg_spinner);
				EditText check_list_item_msg_textview = (EditText) v
						.findViewById(R.id.check_list_item_msg_textview);
				EditText check_list_item_msg_numview = (EditText) v
						.findViewById(R.id.check_list_item_msg_numview);
				EditText check_list_item_msg_numericview = (EditText) v
						.findViewById(R.id.check_list_item_msg_numericview);
				final Button check_list_item_msg_button = (Button) v
						.findViewById(R.id.check_list_item_msg_button);
				if (("choice".equals(s[1])||"radio".equals(s[1]))&&s.length>2) {
					String[] item = s[2].split(",");
					String[] item1 = new String[item.length + 1];
					for (int i = 0; i < item1.length; ++i) {
						if (i == 0) {
							item1[0] = "";
						} else {
							item1[i] = item[i - 1];
						}
					}
					check_list_item_msg_spinner.setVisibility(View.VISIBLE);
					check_list_item_msg_spinner
							.setAdapter(new ArrayAdapter<String>(context,
									R.anim.myspinner, item1));
					//spinner.add(check_list_item_msg_spinner);
					view.add(check_list_item_msg_spinner);
				} else if (("mselect".equals(s[1])||"check".equals(s[1]))&&s.length>2) {
					final String[] item = s[2].split(",");
					final boolean[] selected = new boolean[item.length];
					check_list_item_msg_button.setVisibility(View.VISIBLE);
					check_list_item_msg_button
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									wxT4btnDialog(item, selected,
											check_list_item_msg_button);
								}
							});
					//button.add(check_list_item_msg_button);
					view.add(check_list_item_msg_button);
				} else if ("text".equals(s[1])||"memo".equals(s[1])) {
					check_list_item_msg_textview.setVisibility(View.VISIBLE);
					//editText.add(check_list_item_msg_textview);
					view.add(check_list_item_msg_textview);
				} else if("number".equals(s[1])){
					check_list_item_msg_numview.setVisibility(View.VISIBLE);
					view.add(check_list_item_msg_numview);
				} else if("numeric".equals(s[1])){
					check_list_item_msg_numericview.setVisibility(View.VISIBLE);
					view.add(check_list_item_msg_numericview);
				}/*else {
					check_list_item_msg_checkbox.setVisibility(View.VISIBLE);
					checkBoxsY.add(check_list_item_msg_y);
					checkBoxsN.add(check_list_item_msg_n);
				}*/

				/*
				 * LinearLayout ll=new LinearLayout(context);
				 * ll.setOrientation(android.widget.LinearLayout.HORIZONTAL);
				 * ll.setBackgroundResource(R.drawable.map_layer_background);
				 * TextView msg=new TextView(context); msg.setEms(13);
				 * msg.setGravity(Gravity.CENTER_VERTICAL);
				 * msg.setTextColor(Color.BLACK); msg.setText(opt);
				 * msg.setTextSize(16); ll.addView(msg); ll.setLayoutParams(new
				 * LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, 105)); CheckBox
				 * cbY = new CheckBox(context); CheckBox cbN = new
				 * CheckBox(context);
				 * 
				 * //cbY.setId(rd.nextInt()); cbY.setText("是");
				 * cbY.setTextColor(Color.BLACK);
				 * 
				 * cbN.setText("否"); cbN.setTextColor(Color.BLACK);
				 */

				
			/*	if (values != null) {
					for (int i=0 ;i<values.length;++i) {
						String[] vss = values[i].split(":");
						if (vss.length == 2) {
							if(view.get(i) instanceof Spinner){
							}else if(view.get(i) instanceof Button){
								((Button)view.get(i)).setText(vss[1]);
							}else if(view.get(i) instanceof EditText){
								((EditText)view.get(i)).setText(vss[1]);
							}
							if ((check_list_item_msg_y.getText() + "").equals(vss[1])) {
								check_list_item_msg_y.setChecked(true);
							} else if ((check_list_item_msg_n.getText() + "")
									.equals(vss[1])) {
								check_list_item_msg_n.setChecked(true);
							}
						}
					}
				}*/
				 
				/*
				 * ll.addView(cbY); ll.addView(cbN);
				 */
			}
			this.addView(v);

			/*
			 * check_list_item_msg_y .setOnCheckedChangeListener(new
			 * CheckBox.OnCheckedChangeListener() { public void
			 * onCheckedChanged(CompoundButton btn, boolean value) { //
			 * value为CheckBox的值 onChanged(); } }); check_list_item_msg_n
			 * .setOnCheckedChangeListener(new
			 * CheckBox.OnCheckedChangeListener() { public void
			 * onCheckedChanged(CompoundButton btn, boolean value) { //
			 * value为CheckBox的值 onChanged(); } });
			 */
		}
	}

	public ExpGuiCheckList(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public String getValue() {
		JSONArray jr=new JSONArray();
		for (int i = 0; i < view.size(); ++i) {
			String []v=opts[i].split(":");
			JSONObject jo=new JSONObject();
			try {
				jo.put(bsIcsName, v[0]);
				if(view.get(i) instanceof Spinner){
					jo.put(bsCheck, ((Spinner)(view.get(i))).getSelectedItem().toString());
				}else if(view.get(i) instanceof Button){
					jo.put(bsCheck, ((Button)(view.get(i))).getText());
				}else if(view.get(i) instanceof EditText){
					jo.put(bsCheck,((EditText)(view.get(i))).getText());
				}
				jr.put(jo);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		/*value = "";
		for (int i = 0; i < view.size(); ++i) {
			String []v=opts[i].split(":");
			if(view.get(i) instanceof Spinner){
				if ("".equals(value)) {
					value = v[0] + ":"
							+ ((Spinner)(view.get(i))).getSelectedItem().toString();
				} else {
					value += "," + v[0] + ":"
							+ ((Spinner)(view.get(i))).getSelectedItem().toString();
				}
			}else if(view.get(i) instanceof Button){
				if ("".equals(value)) {
					value = v[0] + ":" + ((Button)(view.get(i))).getText();
				} else {
					value += "," + v[0] + ":" + ((Button)(view.get(i))).getText();
				}
			}else if(view.get(i) instanceof EditText){
				if ("".equals(value)) {
					value = v[0] + ":" + ((EditText)(view.get(i))).getText();
				} else {
					value += "," + v[0] + ":" + ((EditText)(view.get(i))).getText();
				}
			}else {
				if ("".equals(value)) {
					value = v[0]+":";
				} else {
					value += "," + v[0]+":";
				}
			}
		}*/
		/*for (int i = 0; i < checkBoxsY.size(); ++i) {
		  	String []v=opts[i].split(":");
			if (checkBoxsY.get(i).isChecked() && !checkBoxsN.get(i).isChecked()) {
				if ("".equals(value)) {
					value = v[0] + ":" + checkBoxsY.get(i).getText();
				} else {
					value += "," + v[0] + ":" + checkBoxsY.get(i).getText();
				}
			} else if (checkBoxsN.get(i).isChecked()
					&& !checkBoxsY.get(i).isChecked()) {
				if ("".equals(value)) {
					value = v[0] + ":" + checkBoxsN.get(i).getText();
				} else {
					value += "," + v[0] + ":" + checkBoxsN.get(i).getText();
				}
			} else {
				if ("".equals(value)) {
					value = v[0]+":";
				} else {
					value += "," + v[0]+":";
				}
			}

		}
		for (int i = 0; i < spinner.size(); ++i) {
			if ("".equals(value)) {
				value = v[0] + ":"
						+ spinner.get(i).getSelectedItem().toString();
			} else {
				value += "," + v[0] + ":"
						+ spinner.get(i).getSelectedItem().toString();
			}
		}
		for (int i = 0; i < editText.size(); ++i) {
			if ("".equals(value)) {
				value = v[0] + ":" + editText.get(i).getText();
			} else {
				value += "," + v[0] + ":" + editText.get(i).getText();
			}
		}
		for (int i = 0; i < button.size(); ++i) {
			if ("".equals(value)) {
				value = v[0] + ":" + button.get(i).getText();
			} else {
				value += "," + v[0] + ":" + button.get(i).getText();
			}
		}*/
		return jr.toString();
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
		intent.putExtra("value", "");
		context.sendBroadcast(intent);
	}

	/**
	 * 自定义多选控件
	 * 
	 * @param items
	 *            ：字符数组
	 * @param selected
	 *            : boolean 数组
	 * @param wxT4btn
	 *            : 显示控件ID
	 */
	public void wxT4btnDialog(final String items[], final boolean selected[],
			final Button btn) {
		final boolean[] selected1 = new boolean[selected.length];
		for (int i = 0; i < selected.length; ++i) {
			selected1[i] = selected[i];
		}
		AlertDialog.Builder builer = new AlertDialog.Builder(context);
		AlertDialog dialog;
		builer.setTitle("多选");
		builer.setMultiChoiceItems(items, selected1,
				new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						// TODO Auto-generated method stub
						selected1[which] = isChecked;
					}
				});
		builer.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String strValveFault = "";
				for (int i = 0; i < selected1.length; i++) {
					if (selected1[i] == true) {
						if (strValveFault.length() > 0) {
							strValveFault += ",";
						}
						strValveFault = strValveFault + items[i];
					}
				}
				for (int i = 0; i < selected1.length; ++i) {
					selected[i] = selected1[i];
				}
				btn.setText(strValveFault);

			}
		});
		builer.setPositiveButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog = builer.create();
		dialog.show();
	}
}
