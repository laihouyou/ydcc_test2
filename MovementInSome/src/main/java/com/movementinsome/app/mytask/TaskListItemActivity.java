package com.movementinsome.app.mytask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.movementinsome.R;
import com.movementinsome.app.mytask.adapter.TaskListDreRoadPlanAdapter;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.activity.FullActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskListItemActivity extends FullActivity implements OnClickListener {

	private Button wbjh_btn_Back;// 返回
	private Button wbjh_btn_add;// 添加
	private Button wbjh_btn_down;// 下载

	private ListView wbjhListView;// 子列表
	private InsTablePushTaskVo insTablePushTaskVo;
	private List<Map<String, Object>> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_listitem_activity);
		wbjh_btn_Back = (Button) findViewById(R.id.wbjh_btn_Back);// 返回
		wbjh_btn_Back.setOnClickListener(this);
		wbjh_btn_add = (Button) findViewById(R.id.wbjh_btn_add);// 添加
		wbjh_btn_add.setOnClickListener(this);
		wbjh_btn_down= (Button) findViewById(R.id.wbjh_btn_down);// 下载
		Intent intent = getIntent();
		insTablePushTaskVo = (InsTablePushTaskVo) intent.getSerializableExtra("insTablePushTaskVo");

		wbjhListView = (ListView) findViewById(R.id.wbjhListView);// 子列表

		wbjhListView.setAdapter(new TaskListDreRoadPlanAdapter(data, this));
		wbjhListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				/*Intent intent = new Intent();
/*				if ((type + "(阀门)").equals(data.get(arg2).get(0))) {
					intent.setClass(TaskListItemActivity.this,
							WbfmjlActivity.class);
					startActivity(intent);
				} else if ((type + "(调压设备)").equals(data.get(arg2).get(0))) {
					intent.setClass(TaskListItemActivity.this,
							WbtyzgActivity.class);
					startActivity(intent);
				} else if (("计划性维修单").equals(data.get(arg2).get(0))) {
					intent.setClass(TaskListItemActivity.this,
							WxwxzyActivity.class);
					startActivity(intent);
				} else if (("现场抢修单").equals(data.get(arg2).get(0))) {
					intent.setClass(TaskListItemActivity.this,
							WxqxActivity.class);
					startActivity(intent);
				}*/
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.wbjh_btn_Back:
			finish();
			break;
		case R.id.wbjh_btn_add:
			dialogwbjh_btn_add();
			break;

		default:
			break;
		}

	}

	private void dialogwbjh_btn_add() {
		String[] itmes = { "地下燃气管网阀门维保记录表", "调压站,柜（箱）维保记录表" };
		AlertDialog.Builder vDialog = new AlertDialog.Builder(this);
		vDialog.setTitle("设施检查");
		if (data == null)
			data = new ArrayList<Map<String,Object>>();
		final List<String> d1 = new ArrayList<String>();
		vDialog.setItems(itmes, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();

			}
		});
		vDialog.setPositiveButton("返回", null);

		vDialog.show();
	}

}
