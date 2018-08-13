package com.movementinsome.map.search.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;


public class SearchInfoAdapter extends BaseExpandableListAdapter {

	private boolean flgChild = false;
	private Context context;
	private List<String> group;
	private List<List<JSONObject>> child;	
	@SuppressWarnings("static-access")
	public SearchInfoAdapter(Context context,List<String> group,List<List<JSONObject>> child){
		this.context = context;
		this.group = group;
		this.child = child;
	}

	//-----------------Child----------------//
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return child.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (null!=child && 0!=child.size())
			return child.get(groupPosition).size();
		else
		    return 0;
		//return child.get(groupPosition).size();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		String string ="";
		try {
			string = child.get(groupPosition).get(childPosition).getString("title").toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		flgChild = true;
		return getGenericView(string);
	}

	//----------------Group----------------//
	@Override
	public Object getGroup(int groupPosition) {
		return group.get(groupPosition);
	}				

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}	

	@Override
	public int getGroupCount() {
		return group.size();
	}	

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String string = group.get(groupPosition);  
		flgChild = false;
		return getGenericView(string);
	}

	//创建组/子视图  
	public TextView getGenericView(String s) {  
		// Layout parameters for the ExpandableListView  
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(  
				ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		TextView text = new TextView(context);  
		text.setLayoutParams(lp);  
		// Center the text vertically  
		text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);  
		// Set the text starting position  
		text.setPadding(50, 5, 0, 5);  
		if(flgChild){
			text.setTextColor(Color.RED);
			text.setTextSize(18);
		}else{
			text.setTextColor(Color.BLUE);
			text.setTextSize(24);
		}
		text.setText(s);  
		return text;  
	}  

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}		

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}



}
