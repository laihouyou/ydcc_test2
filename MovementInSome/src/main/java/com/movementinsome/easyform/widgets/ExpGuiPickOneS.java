package com.movementinsome.easyform.widgets;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.util.DensityUtil;
import com.movementinsome.database.vo.TpconfigVO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpGuiPickOneS extends LinearLayout implements IXmlGuiFormFieldObject{
	String tag = ExpGuiPickOne.class.getName();

	Context context;
	String value;
	String rule;
	
	TextView label;
	ArrayAdapter<String> aa;
	Button spinner;
	Button fSpinner;
	int position =0;
	String id;
	String labelText;
	String []opts;
	String cName;
	boolean[] selected;
	View fGroup;
	String cId;
	
	@SuppressLint("NewApi")
	public ExpGuiPickOneS(final View fGroup,final Context context,String labelText,String options,String value,String rule
			,boolean readOnly,final String id,String fId,final String cId,String fName,final String cName) {
		super(context);
		this.context=context;
		this.id =id;
		this.labelText=labelText;
		this.cName =cName;
		this.fGroup = fGroup;
		this.cId = cId;
		
		label = new TextView(context);
		label.setText(labelText);
		label.setEms(context.getResources().getInteger(R.integer.label_ems));
		label.setGravity(Gravity.CENTER_VERTICAL);
		label.setTextColor(Color.BLACK);
		spinner = new Button(context);
		if(id!=null){
			spinner.setId(Integer.parseInt(id));
		}
		if(fId!=null){// 当前控件有父控件
			fSpinner = (Button) fGroup.findViewById(Integer.parseInt(fId));
			if(fSpinner!=null){
				String fValue = fSpinner.getText()+"";
				if(fValue.equals("")){
					fValue=" ";
				}
				String name = fName;
				if(fValue!=null&&name!=null){
					name = name.replace("*", "");
					Dao<TpconfigVO, Long> tpconfigDao;
					try {
						tpconfigDao = AppContext.getInstance().getAppDbHelper()
								.getDao(TpconfigVO.class);
						Map<String, Object> m = new HashMap<String, Object>();
						m.put("frontName", name);
						m.put("frontOption", fValue);
						if(ExpGuiPickOneS.this.cName!=null){
							m.put("name", ExpGuiPickOneS.this.cName);
						}
						List<TpconfigVO> configVos = tpconfigDao.queryForFieldValuesArgs(m);
						if(configVos!=null){
							opts = new String[configVos.size()];
							for (int i=0;i<opts.length;++i) {
								opts[i]=configVos.get(i).getPvalue();
							}
						}
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(opts==null||opts.length==0){
				opts = new String[]{""};
			}
		}else{
			opts = options.split("\\|");
		}
		String[] values = (value == null?null:value.split("\\,"));

		aa = new ArrayAdapter<String>( context, R.anim.myspinner,opts);
		spinner.setBackgroundResource(R.drawable.radio_icon);
		spinner.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				DensityUtil.dip2px(context, 45)));
		
		if (values != null){
			for(String o:opts){
				boolean l =false;
				for(String v:values){
					if (o.equals(v)){
						l = true;
					}
				}
				if(l){
					break;
				}
				position++;
			}
		}
		if(position>=opts.length){
			position=0;
		}
		spinner.setText(opts[position]);
		spinner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builer = new AlertDialog.Builder(ExpGuiPickOneS.this.context);
				AlertDialog dialog;
				builer.setSingleChoiceItems(opts, position, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						spinner.setText(opts[which]);
						position=which;
						// 当前控件有子控件
						if(cId!=null){
							final Button cSpinner = (Button) fGroup.findViewById(Integer.parseInt(cId));
							if(cSpinner!=null){
								String value = opts[which];
								String name = ExpGuiPickOneS.this.labelText;
								if(value!=null&&name!=null){
									name = name.replace("*", "");
									Dao<TpconfigVO, Long> tpconfigDao;
									try {
										tpconfigDao = AppContext.getInstance().getAppDbHelper()
												.getDao(TpconfigVO.class);
										Map<String, Object> m = new HashMap<String, Object>();
										m.put("frontName", name);
										m.put("frontOption", value);
										if(ExpGuiPickOneS.this.cName!=null){
											m.put("name", ExpGuiPickOneS.this.cName);
										}
										List<TpconfigVO> configVos = tpconfigDao.queryForFieldValuesArgs(m);
										if(configVos!=null){
											opts2 = new String[configVos.size()];
											for (int i=0;i<opts2.length;++i) {
												opts2[i]=configVos.get(i).getPvalue();
											}
											/*if(opts2.length>0){
												cSpinner.setText(opts2[0]);
											}else{
												cSpinner.setText("");
											}*/
											selected=new boolean[opts2.length];
											cSpinner.setOnClickListener(new OnClickListener() {
												
												@Override
												public void onClick(View v) {
													// TODO Auto-generated method stub
													wxT4btnDialog(opts2, selected, cSpinner);
												}
											});
										}
										
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
						
						dialog.dismiss();
					}
				});
				dialog = builer.create();
				dialog.show();
			}
		});
		/*spinner.setAdapter(aa);
		if (values != null){
			int position =0;
			for(String o:opts){
				for(String v:values){
					if (o.equals(v)){
						spinner.setSelection(position);
						break;
					}
				}
				position++;
			}
		}*/
		if(readOnly){
			spinner.setEnabled(false);
		}
		this.addView(label);
		this.addView(spinner);
		
		spinner.addTextChangedListener(new TextWatcher() {
		    
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
		/*if(fId!=null&&!fId.equals("")){
			MyBroadcast myBroadcast=new MyBroadcast();
			IntentFilter filter = new IntentFilter();
			filter.addAction(fId);
			context.registerReceiver(myBroadcast, filter);
		}*/
	}

	public ExpGuiPickOneS(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	
	public String getValue()
	{
		return (String) spinner.getText().toString();
	}

	@Override
	public void autoChangValue(String value) {
		// TODO Auto-generated method stub
		this.value = value;
		int position = 0;
		if (value != null){
			for(String o:opts){
				boolean l =false;
				if (o.equals(value)){
					l = true;
				}
				if(l){
					spinner.setText(opts[position]);
					// 当前控件有子控件
					if(cId!=null){
						final Button cSpinner = (Button) fGroup.findViewById(Integer.parseInt(cId));
						if(cSpinner!=null){
							String name = ExpGuiPickOneS.this.labelText;
							if(value!=null&&name!=null){
								name = name.replace("*", "");
								Dao<TpconfigVO, Long> tpconfigDao;
								try {
									tpconfigDao = AppContext.getInstance().getAppDbHelper()
											.getDao(TpconfigVO.class);
									Map<String, Object> m = new HashMap<String, Object>();
									m.put("frontName", name);
									m.put("frontOption", value);
									if(ExpGuiPickOneS.this.cName!=null){
										m.put("name", ExpGuiPickOneS.this.cName);
									}
									List<TpconfigVO> configVos = tpconfigDao.queryForFieldValuesArgs(m);
									if(configVos!=null){
										opts2 = new String[configVos.size()];
										for (int i=0;i<opts2.length;++i) {
											opts2[i]=configVos.get(i).getPvalue();
										}
										if(opts2.length>0){
											cSpinner.setText(opts2[0]);
										}else{
											cSpinner.setText("");
										}
										selected=new boolean[opts2.length];
										cSpinner.setOnClickListener(new OnClickListener() {
											
											@Override
											public void onClick(View v) {
												// TODO Auto-generated method stub
												wxT4btnDialog(opts2, selected, cSpinner);
											}
										});
									}
									
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
					break;
				}
				position++;
			}
		}
	}

	@Override
	public void onChanged() {
		// TODO Auto-generated method stub
		try{
			Intent intent = new Intent("RunForm");
			intent.putExtra("req", "onChanged");  
			intent.putExtra("value","");
			context.sendBroadcast(intent);
		}catch(Exception ex){
			;
		}
	}
	private String []opts2;
	private class MyBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if (intent != null) {
				
			}
		}

	}
	private void setSpinnerValue(String name,String value,String v){
		
	}
	/**
	 * 自定义多选控件
	 * @param items ：字符数组
	 * @param selected: boolean 数组
	 * @param wxT4btn : 显示控件ID
	 */
	public void wxT4btnDialog(final String items[],final boolean selected[],final Button btn){
		final boolean[] selected1=new boolean[selected.length];
		for(int i=0;i<selected.length;++i){
			selected1[i]=selected[i];
		}
		AlertDialog.Builder builer = new AlertDialog.Builder(context);
		AlertDialog dialog;
		builer.setTitle("多选");
		builer.setMultiChoiceItems(items, selected1, new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				// TODO Auto-generated method stub
				selected1[which ] = isChecked ;
			}
		});
		builer.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String strValveFault = "";
				for (int i = 0; i < selected1.length; i++) {
					if(selected1[i] == true){
						if(strValveFault.length() > 0){
							strValveFault += ",";
						}
						strValveFault = strValveFault + items[i];
					}
				}
				for(int i=0;i<selected1.length;++i){
					selected[i]=selected1[i];
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
