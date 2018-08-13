/*
 * XmlGui application.
 * Written by Frank Ableson for IBM Developerworks
 * June 2010
 * Use the code as you wish -- no warranty of fitness, etc, etc.
 */
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

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpGuiPickOne extends LinearLayout implements IXmlGuiFormFieldObject {
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
	String []pvaluenums;	//与后台交互的值
	String cName;
	String cId;
	View fGroup;
	String mName;
	String iName;

	@SuppressLint("NewApi")
	public ExpGuiPickOne(final View fGroup,final Context context,String labelText,String options,String value,String rule
			,boolean readOnly,final String id,String fId,final String cId,String fName,final String cName,String mName,String iName) {
		super(context);
		this.context=context;
		this.id =id;
		this.labelText=labelText;
		this.cName =cName;
		this.cId = cId;
		this.fGroup = fGroup;
		this.mName = mName;
		this.iName=iName;
		
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
						if(ExpGuiPickOne.this.mName!=null){
							m.put("name", ExpGuiPickOne.this.mName);
						}
						List<TpconfigVO> configVos = tpconfigDao.queryForFieldValuesArgs(m);
						if(configVos!=null){
							opts = new String[configVos.size()];
							pvaluenums = new String[configVos.size()];
							for (int i=0;i<opts.length;++i) {
								opts[i]=configVos.get(i).getPvalue();
								pvaluenums[i]=configVos.get(i).getPvaluenum();
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
				pvaluenums = new String[]{""};
			}
		}else{
			opts = options.split("\\|");
			if (pvaluenums==null){
				pvaluenums = new String[]{""};
			}
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
		String value1= "village"+","+pvaluenums[0];
		EventBus.getDefault().post(value1);
		spinner.setText(opts[position]);
		spinner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builer = new AlertDialog.Builder(ExpGuiPickOne.this.context);
				AlertDialog dialog;
				builer.setSingleChoiceItems(opts, position, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						spinner.setText(opts[which]);
						position=which;
						// 当前控件有子控件
						setChildCheck(opts[which]);
						if (pvaluenums.length>which){
							String value= "village"+","+pvaluenums[which];
							EventBus.getDefault().post(value);
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

	public ExpGuiPickOne(Context context, AttributeSet attrs) {
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
					setChildCheck(value);
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
			String v = spinner.getText()+"";
			setChildCheck(v);
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
	private void setChildCheck(String value){
		if(cId!=null){
			String cids[] = cId.split(",");
			String cNames[] = null;
			if(ExpGuiPickOne.this.cName!=null){
				cNames = ExpGuiPickOne.this.cName.split(",");
			}
			for(int j =0;j<cids.length;++j){
				final Button cSpinner = (Button) fGroup.findViewById(Integer.parseInt(cids[j]));
				if(cSpinner!=null){
					//String value = opts[which];
					String name = ExpGuiPickOne.this.labelText;
					if(mName!=null&&!"".equals(mName)){
						name = mName;
					}
					if(value!=null&&name!=null){
						name = name.replace("*", "");
						Dao<TpconfigVO, Long> tpconfigDao;
						try {
							tpconfigDao = AppContext.getInstance().getAppDbHelper()
									.getDao(TpconfigVO.class);
							Map<String, Object> m = new HashMap<String, Object>();
							m.put("frontName", name);
							m.put("frontOption", value);
							if(cNames!=null){
								m.put("name", cNames[j]);
							}
							List<TpconfigVO> configVos = tpconfigDao.queryForFieldValuesArgs(m);
							if(configVos!=null){
								opts2 = new String[configVos.size()];
								final String[] pvaluenums=new String[configVos.size()];
								for (int i=0;i<opts2.length;++i) {
									opts2[i]=configVos.get(i).getPvalue();
									pvaluenums[i]=configVos.get(i).getPvaluenum();
								}
								if(opts2.length>0){
									cSpinner.setText(opts2[0]);
									String value1= "village"+","+pvaluenums[0];
									EventBus.getDefault().post(value1);
								}else{
									cSpinner.setText("");
								}
								cSpinner.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										AlertDialog.Builder builer = new AlertDialog.Builder(ExpGuiPickOne.this.context);
										AlertDialog dialog;
										builer.setSingleChoiceItems(opts2, 0, new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												// TODO Auto-generated method stub
												cSpinner.setText(opts2[which]);
												String value= "village"+","+pvaluenums[which];
												EventBus.getDefault().post(value);
												/*if(id!=null&&!"".equals(id)){
													Intent intent = new Intent(id);
													intent.putExtra("value", opts2[which]);
													intent.putExtra("name", labelText);
													context.sendBroadcast(intent);
												}*/
												dialog.dismiss();
											}
										});
										dialog = builer.create();
										dialog.show();
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
		}
	}

}
