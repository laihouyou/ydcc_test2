package com.movementinsome.app.bizcenter.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movementinsome.R;

import java.util.HashMap;


public class BgfxCursorAdapter extends CursorAdapter{
	
	public static HashMap<Integer, Boolean> isCheck;
	public int visibility;

	public BgfxCursorAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
		isCheck = new HashMap<Integer, Boolean>();
		visibility = View.GONE;
		int size = c.getCount();
		for(int i = 0;i < size;i++){
			isCheck.put(i, false);
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return super.getItem(position);
	}
	
	public void shwoCheckBox(int visibility){
		this.visibility = visibility;
		this.notifyDataSetChanged();
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		LinearLayout layout = (LinearLayout)view.findViewById(R.id.ptlist_Linear);
		TextView tvNumber = (TextView)view.findViewById(R.id.ptlist_Number);
		TextView tvContent = (TextView)view.findViewById(R.id.ptlist_Content);
		TextView ptlistClose = (TextView)view.findViewById(R.id.ptlistClose);
		CheckBox cbBroken = (CheckBox)view.findViewById(R.id.ptCbBroken);
		
		cbBroken.setVisibility(visibility);
		cbBroken.setChecked(isCheck.get(cursor.getPosition()));
		ptlistClose.setVisibility(View.VISIBLE);
		String taskid = null;
		String MNO=null;// 点号
		String CEN_DEEP=null;// 埋深
		String SUBTYPE=null;// 类型
		try {
			taskid = cursor.getString(cursor.getColumnIndexOrThrow("EID"));
		} catch (Exception e) {
			// TODO: handle exception
			try {
				taskid = cursor.getString(cursor.getColumnIndexOrThrow("SID"));
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		String address = null;
		try {
			address = cursor.getString(cursor.getColumnIndexOrThrow("LANE_WAY"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		String ISAbnormal = "";
		try {
			ISAbnormal = cursor.getString(cursor.getColumnIndexOrThrow("ISAbnormal"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		String mustClose = "";
		try {
			mustClose = cursor.getString(cursor.getColumnIndexOrThrow("MustBeClosed"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		String Ord = "";
		try {
			Ord = cursor.getInt(cursor.getColumnIndexOrThrow("Ord"))+"";
			
			SUBTYPE=cursor.getString(cursor.getColumnIndex("SUBTYPE"));//类型
			CEN_DEEP=cursor.getString(cursor.getColumnIndex("CEN_DEEP"));//埋深
			MNO=cursor.getString(cursor.getColumnIndex("MNO"));// 点号
		} catch (Exception e) {
			// TODO: handle exception
		}
		String state = cursor.getString(cursor.getColumnIndex("state"));
		if(state.equals("true")){
			layout.setBackgroundResource(R.drawable.task_true);
		}else if(state.equals("busy")){
			layout.setBackgroundResource(R.drawable.task_busy);
		}else{
			layout.setBackgroundResource(R.drawable.task_false);
		}
		tvNumber.setText(Ord);
		tvContent.setText("点号：" + MNO+"\n埋深：" + CEN_DEEP+"\n类型：" + SUBTYPE+"\n所在道路：" + address);
		if(ISAbnormal.equals("true")){
			ISAbnormal = "<font color=\"#FFB90F\">是否异常：是</font><br> ";
		}else{
			ISAbnormal = "<font color=\"#0000ff\">是否异常：否</font><br> ";
		}
		if(mustClose.endsWith("true")){
			mustClose = "<font color=\"#ff0000\">必须关闭：是</font>";
		}else{
			mustClose = "<font color=\"#0000ff\">必须关闭：否</font>";
		}
		ptlistClose.setText(Html.fromHtml(ISAbnormal+mustClose));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return li.inflate(R.layout.pt_list_text, parent, false);
	}

}
