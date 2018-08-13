package com.movementinsome.app.bizcenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.database.vo.MyReceiveMsgVO;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.map.MapBizViewer;

import java.sql.SQLException;
import java.util.List;

public class ReceiveMsgActivity extends FullActivity implements OnClickListener  {

	private ImageView receive_msg_back;
	private ListView receive_msg_data;
	private List<MyReceiveMsgVO> myReceiveMsgVOList;
	private MyAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receive_msg_activity);
		receive_msg_back = (ImageView) findViewById(R.id.receive_msg_back);
		receive_msg_back.setOnClickListener(this);
		receive_msg_data = (ListView) findViewById(R.id.receive_msg_data);
		try {
			Dao<MyReceiveMsgVO, Long> myReceiveMsgVODao = AppContext.getInstance().getAppDbHelper().getDao(MyReceiveMsgVO.class);
			myReceiveMsgVOList = myReceiveMsgVODao.queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TextView emptyTextView = new TextView(this);
		emptyTextView.setLayoutParams(new LayoutParams(-1, -1));
		emptyTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
		emptyTextView.setText("暂无数据");
		emptyTextView.setVisibility(View.GONE);
		((ViewGroup) receive_msg_data.getParent()).addView(emptyTextView);
		receive_msg_data.setEmptyView(emptyTextView);
		if(myReceiveMsgVOList!=null){
			adapter=new MyAdapter(this);
			receive_msg_data.setAdapter(adapter);
		}
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.receive_msg_back:
			finish();
			break;

		default:
			break;
		}
	}
	class MyAdapter extends BaseAdapter {

		private Context context;

		public MyAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return myReceiveMsgVOList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return myReceiveMsgVOList.get(arg0);
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
				convertView = View.inflate(context, R.layout.receive_msg_list_item,
						null);
			}
			LinearLayout receive_msg_list_item_ll = (LinearLayout) convertView
					.findViewById(R.id.receive_msg_list_item_ll);
			TextView receive_msg_item_num = (TextView) convertView
					.findViewById(R.id.receive_msg_item_num);
			receive_msg_item_num.setText((position + 1) + "");
			TextView receive_msg_list_item_content = (TextView) convertView
					.findViewById(R.id.receive_msg_list_item_content);
			Button receive_msg_list_item_del=(Button) convertView.findViewById(R.id.receive_msg_list_item_del);
			Button receive_msg_list_item_loc = (Button) convertView.findViewById(R.id.receive_msg_list_item_loc);
			
			String content="接收时间："+myReceiveMsgVOList.get(position).getReceiveTime()+"\n"
							+"消息内容："+myReceiveMsgVOList.get(position).getReceiveMsg();
			receive_msg_list_item_content.setText(content);

			receive_msg_list_item_content.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
			receive_msg_list_item_del.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(context)
					.setTitle("提示")
					.setIcon(android.R.drawable.ic_menu_help)
					.setMessage("确定删除表单！")
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
							
							try {
								Dao<MyReceiveMsgVO, Long> myReceiveMsgVODao = AppContext.getInstance().getAppDbHelper().getDao(MyReceiveMsgVO.class);
								myReceiveMsgVODao.delete(myReceiveMsgVOList.get(position));
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							myReceiveMsgVOList.remove(position);
							adapter.notifyDataSetChanged();
						}
					}).show();
				}
			});
			if(myReceiveMsgVOList.get(position).getCoordinate()!=null){
				receive_msg_list_item_loc.setVisibility(View.VISIBLE);
				receive_msg_list_item_loc.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setClass(context, MapBizViewer.class);
						intent.putExtra("strGraph", myReceiveMsgVOList.get(position).getCoordinate());
						intent.putExtra("type", 10006);
						((Activity) context).startActivity(intent);
					}
				});
			}else{
				receive_msg_list_item_loc.setVisibility(View.GONE);
			}
			return convertView;
		}

	}

}
