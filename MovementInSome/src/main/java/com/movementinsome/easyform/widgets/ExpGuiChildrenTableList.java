package com.movementinsome.easyform.widgets;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.easyform.formengineer.RunForm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpGuiChildrenTableList extends LinearLayout implements
		IXmlGuiFormFieldObject {

	private Context context;
	private Button addBnt;
	private ListView chiledenList;
	private List<JSONObject> childrenListData;
	private String labelText;
	private String childrenTableAction;
	private String childrenTableUpdateAction;
	private String template;

	public ExpGuiChildrenTableList(Context context, String labelText,
			String value, String childrenTableAction,String childrenTableUpdateAction,String template,boolean readOnly, final String taskNum) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.labelText = labelText;
		this.childrenTableAction = childrenTableAction;
		this.childrenTableUpdateAction=childrenTableUpdateAction;
		this.template=template;
		
		LinearLayout.LayoutParams paramsBnt = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		addBnt = new Button(context);
		addBnt.setText("添加"+labelText);
		addBnt.setLayoutParams(paramsBnt);
		addBnt.setBackgroundResource(R.drawable.map_layer_background);
		if(readOnly){
			addBnt.setEnabled(false);
		}
		addBnt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(ExpGuiChildrenTableList.this.template!=null){
					Intent intent = new Intent();
					intent.setClass(ExpGuiChildrenTableList.this.context, RunForm.class);
					intent.putExtra("childrenTableUpdateAction", ExpGuiChildrenTableList.this.childrenTableUpdateAction);
					intent.putExtra("template", ExpGuiChildrenTableList.this.template);
					HashMap<String, String> params1 = new HashMap<String, String>();
					params1.put("taskNum", taskNum);
					intent.putExtra("iParams", params1);
					intent.putExtra("isChildrenTable", true);
					intent.putExtra("isAddChildrenTable", true);
					ExpGuiChildrenTableList.this.context.startActivity(intent);
				}else{
					Intent intent = new Intent(ExpGuiChildrenTableList.this.childrenTableAction);
					intent.putExtra("childrenTableUpdateAction", ExpGuiChildrenTableList.this.childrenTableUpdateAction);
					
					ExpGuiChildrenTableList.this.context.startActivity(intent);
				}
			}
		});
		JSONArray vs = null;
		try {
			vs = new JSONArray(value);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		childrenListData=new ArrayList<JSONObject>();
		if(vs!=null){
			for(int i = 0;i<vs.length();++i){
				try {
					childrenListData.add(vs.getJSONObject(i));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		chiledenList = new ListView(context);
		chiledenList.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		if (childrenListData != null) {
			chiledenList.setAdapter(new ChiledenListAdapter());
			ChiledenListAdapter listAdapter = (ChiledenListAdapter) chiledenList
			.getAdapter();
			int totalHeight = 0;
			for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
				View listItem = listAdapter.getView(i, null,
						chiledenList);
				listItem.measure(0, 0); // 计算子项View 的宽高
				totalHeight += (listItem.getMeasuredHeight()*0.6); // 统计所有子项的总高度
			}
			LayoutParams params = (LayoutParams) chiledenList
					.getLayoutParams();
			params.height = totalHeight;
			chiledenList.setLayoutParams(params);
		}
		this.setOrientation(android.widget.LinearLayout.VERTICAL);
		this.addView(addBnt);
		this.addView(chiledenList);
		
		MyBroadcast myBroadcast=new MyBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(childrenTableUpdateAction);
		context.registerReceiver(myBroadcast, filter);
	}

	@Override
	public void autoChangValue(String value) {
		// TODO Auto-generated method stub

	}

	public String getValue() {
		JSONArray vs=new JSONArray();
		for(int i=0;i<childrenListData.size();++i){
			vs.put(childrenListData.get(i));
		}
		return vs.toString();
	}

	public void setValue(String v) {
		try {
			if(v!=null){
				JSONObject jo = new JSONObject(v);
				childrenListData.add(jo);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void upDateValue(String v,int position){
		try {
			childrenListData.remove(position);
			childrenListData.add(position, new JSONObject(v));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onChanged() {
		// TODO Auto-generated method stub
		Intent intent = new Intent("RunForm");
		intent.putExtra("req", "onChanged");
		intent.putExtra("value", "");
		context.sendBroadcast(intent);
	}

	private class ChiledenListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return childrenListData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return childrenListData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = View.inflate(context,
						R.layout.children_table_list_item, null);
			}
			TextView children_table_list_name = (TextView) convertView
					.findViewById(R.id.children_table_list_name);
			children_table_list_name.setText(labelText + (position + 1));
			Button children_table_list_del = (Button) convertView
					.findViewById(R.id.children_table_list_del);
			children_table_list_del.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(context)
					.setTitle("提示")
					.setIcon(android.R.drawable.ic_menu_help)
					.setMessage("确定删除数据！")
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
							childrenListData.remove(position);
							chiledenList.setAdapter(new ChiledenListAdapter());
							ChiledenListAdapter listAdapter = (ChiledenListAdapter) chiledenList
							.getAdapter();
							int totalHeight = 0;
							for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
								View listItem = listAdapter.getView(i, null,
										chiledenList);
								listItem.measure(0, 0); // 计算子项View 的宽高
								totalHeight += (listItem.getMeasuredHeight()*0.6); // 统计所有子项的总高度
							}
							LayoutParams params = (LayoutParams) chiledenList
									.getLayoutParams();
							params.height = totalHeight;
							chiledenList.setLayoutParams(params);
								}
					}).show();
				}
			});
			children_table_list_name.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(ExpGuiChildrenTableList.this.template!=null){
						Intent intent = new Intent();
						intent.setClass(ExpGuiChildrenTableList.this.context, RunForm.class);
						intent.putExtra("childrenTableUpdateAction", ExpGuiChildrenTableList.this.childrenTableUpdateAction);
						intent.putExtra("template", ExpGuiChildrenTableList.this.template);
						intent.putExtra("isChildrenTable", true);
						intent.putExtra("childrendata", childrenListData.get(position).toString());
						intent.putExtra("childrenTablePosition", position);
						intent.putExtra("isAddChildrenTable", false);
						ExpGuiChildrenTableList.this.context.startActivity(intent);
					}else{
						Intent intent = new Intent(ExpGuiChildrenTableList.this.childrenTableAction);
						intent.putExtra("childrenTableUpdateAction", ExpGuiChildrenTableList.this.childrenTableUpdateAction);
						intent.putExtra("childrendata", childrenListData.get(position).toString());
						
						ExpGuiChildrenTableList.this.context.startActivity(intent);
					}
				}
			});
			return convertView;
		}

	}

	private class MyBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if (intent != null) {
				int childrenTablePosition = intent.getIntExtra(
						"childrenTablePosition", 0);
				if (intent.getBooleanExtra("isAddChildrenTable", false)) {
					setValue(intent.getStringExtra("childrendata"));
					chiledenList.setAdapter(new ChiledenListAdapter());
					ChiledenListAdapter listAdapter = (ChiledenListAdapter) chiledenList
							.getAdapter();
					int totalHeight = 0;
					for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
						View listItem = listAdapter.getView(i, null,
								chiledenList);
						listItem.measure(0, 0); // 计算子项View 的宽高
						totalHeight += (listItem.getMeasuredHeight()*0.6); // 统计所有子项的总高度
					}
					LayoutParams params = (LayoutParams) chiledenList
							.getLayoutParams();
					params.height = totalHeight;
					chiledenList.setLayoutParams(params);
				} else {
					upDateValue(intent.getStringExtra("childrendata"),
							childrenTablePosition);
					chiledenList.setAdapter(new ChiledenListAdapter());

				}

			}
		}

	}

}
