package com.movementinsome.app.bizcenter;

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
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.database.vo.DynamicFormVO;
import com.movementinsome.easyform.formengineer.RunForm;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.kernel.initial.model.Module;
import com.movementinsome.kernel.util.MyDateTools;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoryTableActivity extends FullActivity implements OnClickListener {

	private ListView historyLvData;
	private ImageView historyImageBack; // 返回
	private Button historyImageRefresh; // 刷新
	private MyAdapter adapter;
	private List<DynamicFormVO> dynamicFormList;
	private List<Module> lstModule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.historty_table_activity);

		lstModule = AppContext.getInstance().getModuleData();
		
		historyLvData = (ListView) findViewById(R.id.historyLvData);
		historyImageBack = (ImageView) findViewById(R.id.historyImageBack);
		historyImageRefresh = (Button) findViewById(R.id.historyBtnRefresh);

		TextView emptyTextView = new TextView(this);
		emptyTextView.setLayoutParams(new LayoutParams(-1, -1));
		emptyTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
		emptyTextView.setText("暂无数据");
		emptyTextView.setVisibility(View.GONE);
		((ViewGroup) historyLvData.getParent()).addView(emptyTextView);
		historyLvData.setEmptyView(emptyTextView);

		historyImageBack.setOnClickListener(this);
		historyImageRefresh.setOnClickListener(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			Dao<DynamicFormVO, Long> dynamicFormDao = AppContext.getInstance()
					.getAppDbHelper().getDao(DynamicFormVO.class);
			//List<DynamicFormVO> dynamicFormS = dynamicFormDao.queryForAll();
			QueryBuilder<DynamicFormVO, Long> queryBuilder = dynamicFormDao.queryBuilder();
			Where<DynamicFormVO, Long> where = queryBuilder.where();
			where.isNotNull("form");
			where.and();
			where.eq("status", "0");
			dynamicFormList = dynamicFormDao.query(queryBuilder.prepare());
			/*for(int i=0;i<dynamicFormS.size();++i){
				for (int j = 0; j < lstModule.size(); ++j) {
					if (dynamicFormS.get(i).getForm()
							.equals(lstModule.get(j).getTemplate())) {
						if(lstModule.get(j).getHistory()!=null&&lstModule.get(j).getHistory().equals("true")){
							dynamicFormList.add(dynamicFormS.get(i));
						}
						//break;
					}
				}
			}*/
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (dynamicFormList == null) {
			dynamicFormList = new ArrayList<DynamicFormVO>();
		}
		adapter = new MyAdapter(this);
		historyLvData.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.historyImageBack:
			finish();
			break;

		case R.id.historyBtnRefresh:
			adapter.notifyDataSetChanged();
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
			return dynamicFormList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return dynamicFormList.get(arg0);
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
				convertView = View.inflate(context, R.layout.history_list_item,
						null);
			}
			LinearLayout history_list_item_ll = (LinearLayout) convertView
					.findViewById(R.id.history_list_item_ll);
			TextView history_list_item_num = (TextView) convertView
					.findViewById(R.id.history_list_item_num);
			history_list_item_num.setText((position + 1) + "");
			TextView history_list_item_content = (TextView) convertView
					.findViewById(R.id.history_list_item_content);
			Button history_list_item_del=(Button) convertView.findViewById(R.id.history_list_item_del);
			/*if (dynamicFormList.get(position).getStatus() == 0) {
				history_list_item_ll
						.setBackgroundResource(R.drawable.task_save);
			} else if (dynamicFormList.get(position).getStatus() == 2) {
				history_list_item_ll
						.setBackgroundResource(R.drawable.task_true);
			}*/
			String type = "未知";
			for (int i = 0; i < lstModule.size(); ++i) {
				if (dynamicFormList.get(position).getForm()
						.equals(lstModule.get(i).getTemplate())) {
					type = lstModule.get(i).getName();
				}
			}
			String content = "表单类型："
					+ type
					+ "\n保存日期："
					+ MyDateTools.date2String(dynamicFormList.get(position)
							.getCreateDate());
			history_list_item_content.setText(content);

			history_list_item_content.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//if(dynamicFormList.get(position).getPid()==null){
						Intent newFormInfo = new Intent(context,RunForm.class);
						newFormInfo.putExtra("id", dynamicFormList.get(position).getId());
						newFormInfo.putExtra("template", dynamicFormList.get(position).getForm());
						context.startActivity(newFormInfo);
					//}
				}
			});
			history_list_item_del.setOnClickListener(new OnClickListener() {
				
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
								Dao<DynamicFormVO, Long> dynamicFormDao = AppContext.getInstance()
										.getAppDbHelper().getDao(DynamicFormVO.class);
								dynamicFormDao.delete(dynamicFormList.get(position));
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							dynamicFormList.remove(position);
							adapter.notifyDataSetChanged();
						}
					}).show();
				}
			});
			return convertView;
		}

	}

}
