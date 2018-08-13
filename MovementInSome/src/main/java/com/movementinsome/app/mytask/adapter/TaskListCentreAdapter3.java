package com.movementinsome.app.mytask.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.app.mytask.handle.TaskListCentreBaseHandle;
import com.movementinsome.app.mytask.handle.TaskListCentreHandle;
import com.movementinsome.app.mytask.handle.WsTaskListCentreHandle;
import com.movementinsome.app.mytask.handle.YhTaskListCentreHandle;
import com.movementinsome.database.vo.BsRushRepairWorkOrder;
import com.movementinsome.database.vo.InsComplainantForm;
import com.movementinsome.database.vo.InsDatumInaccurate;
import com.movementinsome.database.vo.InsHiddenDangerReport;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.database.vo.InsTableSaveDataVo;
import com.movementinsome.database.vo.TaskUploadStateVO;
import com.movementinsome.database.vo.WsComplainantFormVO;
import com.movementinsome.kernel.initial.model.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskListCentreAdapter3 extends BaseExpandableListAdapter {

	private List<Map<String, Object>> data;
	private Activity context;
	private Handler myHandler;

	public TaskListCentreAdapter3(List<Map<String, Object>> data,
			Activity context, Handler myHandler) {
		this.data = data;
		this.context = context;
		this.myHandler = myHandler;
	}

	/*@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		InsTablePushTaskVo insTablePushTaskVo = (InsTablePushTaskVo) data.get(
				position).get("InsTablePushTaskVo");
		TaskUploadStateVO taskUploadStateVO = (TaskUploadStateVO) data.get(
				position).get("TaskUploadStateVO");
		InsTableSaveDataVo insTableSaveDataVo = (InsTableSaveDataVo) data.get(
				position).get("InsTableSaveDataVo");
		InsComplainantForm insComplainantForm = new InsComplainantForm();
		InsHiddenDangerReport insHiddenDangerReport = new InsHiddenDangerReport();
		// 当前任务表单配置
		List<Module> taskModule = (List<Module>) data.get(position).get(
				"Module");
		// 业务处理类
		final List<TaskListCentreBaseHandle> taskListCentreBaseHandleList = new ArrayList<TaskListCentreBaseHandle>();
		taskListCentreBaseHandleList.add(new TaskListCentreHandle(context,
				insTablePushTaskVo, taskUploadStateVO, insTableSaveDataVo,
				insComplainantForm, insHiddenDangerReport, taskModule));
		taskListCentreBaseHandleList.add(new WsTaskListCentreHandle(context,
				(InsTablePushTaskVo) (data.get(position)
						.get("InsTablePushTaskVo")), (TaskUploadStateVO) (data
						.get(position).get("TaskUploadStateVO")),
				(List<Module>) (data.get(position).get("Module")),
				(WsComplainantFormVO) data.get(position).get(
						"WsComplainantFormVO")));

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();

			//convertView = new MyView(context);
			convertView = View.inflate(context, R.layout.tasklist_centre_list,null);

			holder.tlLinearContent = (LinearLayout) convertView
					.findViewById(R.id.tlLinearContent);
			holder.tv = (TextView) convertView.findViewById(R.id.tltv_content);
			holder.tlTv_Title = (TextView) convertView
					.findViewById(R.id.tlTv_Title);
			holder.tlBn_wc = (Button) convertView.findViewById(R.id.tlBn_wc);// 完成
			holder.tlBn_t = (Button) convertView.findViewById(R.id.tlBn_t);
			holder.tlBn_t.setText("退单");
			holder.tlBn_loc = (Button) convertView.findViewById(R.id.tlBn_loc);

			// 下载
			holder.tlBn_xz = (Button) convertView.findViewById(R.id.tlBn_xz);
			// 显示详细内容
			holder.tlBn_xx = (Button) convertView.findViewById(R.id.tlBn_xx);

			// 开始
			holder.tlBn_ks = (Button) convertView.findViewById(R.id.tlBn_ks);
			// 填单
			holder.tlBn_td = (Button) convertView.findViewById(R.id.tlBn_td);
			holder.tlBn_th = (Button) convertView.findViewById(R.id.tlBn_th);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tlBn_t.setVisibility(View.VISIBLE);
		holder.tlBn_wc.setVisibility(View.VISIBLE);
		
		holder.tlBn_th.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).identicalHandler();
				}
			}
		});
		// 完成
		holder.tlBn_wc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).finishHandler();
				}
			}
		});
		// 退单
		holder.tlBn_t.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).returnTableHandler();
				}
			}
		});
		// 下载
		// Button tlBn_xz = (Button) convertView.findViewById(R.id.tlBn_xz);
		holder.tlBn_xz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).downloadHandler(
							myHandler);
				}
			}
		});
		// 显示详细内容
		// Button tlBn_xx = (Button) convertView.findViewById(R.id.tlBn_xx);
		holder.tlBn_xx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).showMsgHandler();
				}
			}
		});
		holder.tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).showMsgHandler();
				}
			}
		});
		// 开始
		// Button tlBn_ks = (Button) convertView.findViewById(R.id.tlBn_ks);
		holder.tlBn_ks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).startWorkHandler();
				}
			}
		});
		// 填单
		// Button tlBn_td = (Button) convertView.findViewById(R.id.tlBn_td);
		holder.tlBn_td.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).writeTable();
				}
			}
		});
		holder.tlTv_Title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).writeTable();
				}
			}
		});
		// 定位
		holder.tlBn_loc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).locHandler();
				}
			}
		});
		Map<String, View> v = new HashMap<String, View>();
		v.put("tlBn_td", holder.tlBn_td);
		v.put("tlBn_ks", holder.tlBn_ks);
		v.put("tlBn_xx", holder.tlBn_xx);
		v.put("tlBn_xz", holder.tlBn_xz);
		v.put("tlBn_t", holder.tlBn_t);
		v.put("tlBn_wc", holder.tlBn_wc);
		v.put("tlBn_loc", holder.tlBn_loc);
		v.put("tlBn_th", holder.tlBn_th);

		v.put("tv", holder.tv);
		v.put("tlTv_Title", holder.tlTv_Title);
		v.put("tlLinearContent", holder.tlLinearContent);
		for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
			taskListCentreBaseHandleList.get(i).controlUI(v);
		}
		return convertView;
	}*/

	static class ViewHolder {
		public LinearLayout tlLinearContent;
		public TextView tltv_titilename;
		public TextView tv;
		public TextView tlTv_Title;
		public LinearLayout tlBn_wc;// 完成
		public TextView tv_tlBn_wc;//完成任务显示
		public TextView img_tlBn_wc;//完成任务图标
		public RelativeLayout tlBn_t;
		public LinearLayout tlBn_t_xian;
		public RelativeLayout tlBn_loc;
		public RelativeLayout tlBn_xz;
		public LinearLayout tlBn_xz_xian;
		public LinearLayout tlBn_wc_xian;
		public RelativeLayout tlBn_xx;
		public RelativeLayout tlBn_ks;
		public RelativeLayout tlBn_td;
		public RelativeLayout tlBn_th;
	}

	
	private class MyView extends LinearLayout implements OnGestureListener, OnTouchListener{
		private LinearLayout tlLinearContent;
		private LinearLayout tlLinearContent2;
		private boolean isShow=false;
		private GestureDetector detector;		//实现切换页面

		public MyView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			LinearLayout view = (LinearLayout) View.inflate(context, R.layout.tasklist_centre_list,null);
			this.addView(view);
			tlLinearContent = (LinearLayout) findViewById(R.id.tlLinearContent);
			tlLinearContent2 = (LinearLayout) findViewById(R.id.tlLinearContent2);
			detector = new GestureDetector(this);
			setOnTouchListener(this);
		}
		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
			// TODO Auto-generated method stub
			detector.onTouchEvent(ev);  
			return true;
		}
		@Override
		public boolean onTouch(View arg0, MotionEvent event) {
			// TODO Auto-generated method stub
			 detector.onTouchEvent(event);
			 return false;
		}

		@Override
		public boolean onDown(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@SuppressLint("NewApi")
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			float x = tlLinearContent2.getX();
			if (e1.getX() - e2.getX() > 20) {
				if(!isShow){
					tlLinearContent2.setX(x-200);
					tlLinearContent.setX(-200);
					isShow=true;
				}
			} else if (e1.getX() - e2.getX() < -20) {
				if(isShow){
					tlLinearContent2.setX(x+200);
					tlLinearContent.setX(0);
					isShow=false;
				}
			}
			return false;
		}

		@Override
		public void onLongPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}
	}


	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Object getGroup(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}


	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return data.size();
	}


	@Override
	public long getGroupId(int position) {
		// TODO Auto-generated method stub
		return position;
	}


	@Override
	public View getGroupView(int position, boolean arg1, View convertView, ViewGroup arg3) {
		// TODO Auto-generated method stub
		InsTablePushTaskVo insTablePushTaskVo = (InsTablePushTaskVo) data.get(
				position).get("InsTablePushTaskVo");
		TaskUploadStateVO taskUploadStateVO = (TaskUploadStateVO) data.get(
				position).get("TaskUploadStateVO");
		InsTableSaveDataVo insTableSaveDataVo = (InsTableSaveDataVo) data.get(
				position).get("InsTableSaveDataVo");
		InsDatumInaccurate insDatumInaccurate = (InsDatumInaccurate) data.get(
				position).get("InsDatumInaccurate");
		InsComplainantForm insComplainantForm = new InsComplainantForm();
		InsHiddenDangerReport insHiddenDangerReport = new InsHiddenDangerReport();
		// 当前任务表单配置
		List<Module> taskModule = (List<Module>) data.get(position).get(
				"Module");
		// 业务处理类
		final List<TaskListCentreBaseHandle> taskListCentreBaseHandleList = new ArrayList<TaskListCentreBaseHandle>();
		taskListCentreBaseHandleList.add(new TaskListCentreHandle(context,
				insTablePushTaskVo, taskUploadStateVO, insTableSaveDataVo,
				insComplainantForm, insHiddenDangerReport,insDatumInaccurate, taskModule));
		taskListCentreBaseHandleList.add(new WsTaskListCentreHandle(context,
				(InsTablePushTaskVo) (data.get(position)
						.get("InsTablePushTaskVo")), (TaskUploadStateVO) (data
						.get(position).get("TaskUploadStateVO")),(InsDatumInaccurate) (data
								.get(position).get("InsDatumInaccurate")),
				(List<Module>) (data.get(position).get("Module")),
				(WsComplainantFormVO) data.get(position).get(
						"WsComplainantFormVO")));
		taskListCentreBaseHandleList.add(new YhTaskListCentreHandle(context, insTablePushTaskVo, taskUploadStateVO, taskModule,
				 (BsRushRepairWorkOrder)(data.get(position).get("BsRushRepairWorkOrder"))));
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();

			//convertView = new MyView(context);
			convertView = View.inflate(context, R.layout.tasklist_centre_list,null);

			holder.tlLinearContent = (LinearLayout) convertView
					.findViewById(R.id.tlLinearContent);
			holder.tv = (TextView) convertView.findViewById(R.id.tltv_content);
			holder.tlTv_Title = (TextView) convertView
					.findViewById(R.id.tlTv_Title);
			holder.tlBn_wc = (LinearLayout) convertView.findViewById(R.id.tlBn_wc);// 完成
			holder.tv_tlBn_wc = (TextView) convertView.findViewById(R.id.tv_tlBn_wc);//任务完成
			holder.img_tlBn_wc = (TextView) convertView.findViewById(R.id.img_tlBn_wc);//任务完成图标
			holder.tlBn_t = (RelativeLayout) convertView.findViewById(R.id.tlBn_t);
//			holder.tlBn_t.setText("退单");
			holder.tlBn_t_xian = (LinearLayout) convertView.findViewById(R.id.tlBn_t_xian);
			holder.tlBn_t_xian.setVisibility(View.VISIBLE);
			holder.tlBn_loc = (RelativeLayout) convertView.findViewById(R.id.tlBn_loc);

			// 下载
			holder.tlBn_xz = (RelativeLayout) convertView.findViewById(R.id.tlBn_xz);
			holder.tlBn_xz_xian = (LinearLayout) convertView.findViewById(R.id.tlBn_xz_xian);
			holder.tlBn_xz_xian.setVisibility(View.VISIBLE);
			holder.tlBn_wc_xian = (LinearLayout) convertView.findViewById(R.id.tlBn_wc_xian);
			// 显示详细内容
			holder.tlBn_xx = (RelativeLayout) convertView.findViewById(R.id.tlBn_xx);

			// 开始
			holder.tlBn_ks = (RelativeLayout) convertView.findViewById(R.id.tlBn_ks);
			// 填单
			holder.tlBn_td = (RelativeLayout) convertView.findViewById(R.id.tlBn_td);
			holder.tlBn_th = (RelativeLayout) convertView.findViewById(R.id.tlBn_th);
			holder.tltv_titilename = (TextView) convertView.findViewById(R.id.tltv_titilename);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tlBn_t.setVisibility(View.VISIBLE);
		holder.tlBn_wc.setVisibility(View.VISIBLE);
		
		holder.tlBn_th.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).identicalHandler();
				}
			}
		});
		// 完成
		holder.tlBn_wc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).finishHandler();
				}
			}
		});
		// 退单
		holder.tlBn_t.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).returnTableHandler();
				}
			}
		});
		// 下载
		// Button tlBn_xz = (Button) convertView.findViewById(R.id.tlBn_xz);
		holder.tlBn_xz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).downloadHandler(
							myHandler);
				}
			}
		});
		// 显示详细内容
		// Button tlBn_xx = (Button) convertView.findViewById(R.id.tlBn_xx);
		holder.tlBn_xx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).showMsgHandler();
				}
			}
		});
		holder.tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).showMsgHandler();
				}
			}
		});
		// 开始
		// Button tlBn_ks = (Button) convertView.findViewById(R.id.tlBn_ks);
		holder.tlBn_ks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).startWorkHandler();
				}
			}
		});
		// 填单
		// Button tlBn_td = (Button) convertView.findViewById(R.id.tlBn_td);
		holder.tlBn_td.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).writeTable();
				}
			}
		});
		holder.tlTv_Title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).writeTable();
				}
			}
		});
		// 定位
		holder.tlBn_loc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
					taskListCentreBaseHandleList.get(i).locHandler();
				}
			}
		});
		Map<String, View> v = new HashMap<String, View>();
		v.put("tlBn_td", holder.tlBn_td);
		v.put("tlBn_ks", holder.tlBn_ks);
		v.put("tlBn_xx", holder.tlBn_xx);
		v.put("tlBn_xz", holder.tlBn_xz);
		v.put("tlBn_t", holder.tlBn_t);
		v.put("tlBn_wc", holder.tlBn_wc);
		v.put("tlBn_wc_xian", holder.tlBn_wc_xian);
		v.put("tv_tlBn_wc", holder.tv_tlBn_wc);
		v.put("img_tlBn_wc", holder.img_tlBn_wc);
		v.put("tlBn_loc", holder.tlBn_loc);
		v.put("tlBn_th", holder.tlBn_th);

		v.put("tv", holder.tv);
		v.put("tlTv_Title", holder.tlTv_Title);
		v.put("tlLinearContent", holder.tlLinearContent);
		v.put("tltv_titilename", holder.tltv_titilename);
		for (int i = 0; i < taskListCentreBaseHandleList.size(); ++i) {
			taskListCentreBaseHandleList.get(i).controlUI(v);
		}
		return convertView;
	}


	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}
}
