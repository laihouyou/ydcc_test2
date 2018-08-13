package com.movementinsome.app.pub.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.pub.view.CreateDynamicView;
import com.movementinsome.app.server.DownloadTask;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.map.MapBizViewer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 任务单——消息推送
 * 
 * @author gddst
 * 
 */

public class PushTaskActivity extends FullActivity implements OnClickListener {

	/* 标题 */
	private TextView ptTvTitle; // 新任务单，后台消息（标题）

	public static boolean flgShow = false; // 是否正在显示任务信息

	/* 下载页面控件 */
	private Button ptBtnDTaskIn; // 接单
	private Button ptBtnDBackSingle; // 退单
	private ScrollView ptLayoutDTask; // 信息
	private RelativeLayout ptLayoutDContent; // 内容

	/* 消息显示 */
	private TextView ptTvMMsg; // 文本
	private EditText ptEtMReturn; // 回访信息
	private Button ptBtnMReply; // 回复
	private Button ptBtnMClear; // 清空
	private ImageView ptImageMBack; // 返回
	private LinearLayout ptLayoutMContent; // 内容
	private Button ptBtnMLoc;// 定位

	private String newTaskNumber; // 新的任务编号

	private String title; // 推送类型
	private String content_title; // 推送内容类型
	private String urgencyLevel;

	private String message; // 推送文本
	private String pointStr;

	private Context context;
	private CreateDynamicView dynamicView; // 动态加载控件，显示消息推送内容
	public boolean flgLaterOn = false; // 是否后台加载
	private InsTablePushTaskVo insTablePushTaskVo;
	private Timer timer;// 定时器
	private Vibrator vibrator; // 启动手机震动
	private Thread thread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.push_task_activity);
		context = this;
		timer = new Timer();
		vibrator = (Vibrator) context
				.getSystemService(context.VIBRATOR_SERVICE);
		// 调用句柄
		getHandle();
		ptImageMBack.setVisibility(View.VISIBLE);
		Intent intent = getIntent();
		urgencyLevel = intent.getStringExtra("urgencyLevel");
		if (!("").equals(urgencyLevel) && urgencyLevel != null) {
			ptImageMBack.setVisibility(View.VISIBLE);
			pushtime();
		}
	}

	/**
	 * 获取控件句柄
	 */
	@SuppressWarnings("static-access")
	public void getHandle() {
		dynamicView = new CreateDynamicView(context);
		ptTvTitle = (TextView) findViewById(R.id.ptTvTitle);

		ptTvMMsg = (TextView) findViewById(R.id.ptTvMMsg);
		ptBtnMReply = (Button) findViewById(R.id.ptBtnMReply);
		ptBtnMClear = (Button) findViewById(R.id.ptBtnMClear);
		ptEtMReturn = (EditText) findViewById(R.id.ptEtMReturn);
		ptImageMBack = (ImageView) findViewById(R.id.ptImageMBack);
		ptLayoutMContent = (LinearLayout) findViewById(R.id.ptLayoutMContent);

		ptBtnDTaskIn = (Button) findViewById(R.id.ptBtnDTaskIn);
		ptBtnDBackSingle = (Button) findViewById(R.id.ptBtnDBackSingle);
		ptLayoutDTask = (ScrollView) findViewById(R.id.ptLayoutDTask);
		ptLayoutDContent = (RelativeLayout) findViewById(R.id.ptLayoutDContent);
		ptBtnMLoc = (Button) findViewById(R.id.ptBtnMLoc);

		ptBtnMReply.setOnClickListener(this); // 回复
		ptBtnMClear.setOnClickListener(this); // 清空
		ptImageMBack.setOnClickListener(this); // 返回
		ptBtnDTaskIn.setOnClickListener(this); // 接单
		ptBtnDBackSingle.setOnClickListener(this); // 退单
		ptBtnMLoc.setOnClickListener(this);

		// 改变控件焦点
		ptTvTitle.setFocusable(true);
		ptTvTitle.setFocusableInTouchMode(true);
		ptTvTitle.requestFocus();
		ptTvTitle.requestFocusFromTouch();

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 默认显示消息：
		ptLayoutDContent.setVisibility(View.GONE);
		ptLayoutMContent.setVisibility(View.VISIBLE);
		ptTvTitle.setText("后台消息");
		Intent intent = getIntent();
		// 消息推送解析的值
		title = intent.getStringExtra("title");
		content_title = intent.getStringExtra("content_title");

		message = intent.getStringExtra("message");
		pointStr = intent.getStringExtra("POINT");
		if (pointStr != null) {
			ptBtnMLoc.setVisibility(View.VISIBLE);
		} else {
			ptBtnMLoc.setVisibility(View.GONE);
		}
		if (Constant.PUSH_TITLE_TASK.equals(title)) {// 推送任务单消息
			/*
			 * List<InsTablePushTaskVo> insTablePushTaskVoList=null;
			 * Dao<InsTablePushTaskVo, Long> saveDataDao; try { saveDataDao =
			 * AppContext
			 * .getInstance().getAppDbHelper().getDao(InsTablePushTaskVo.class);
			 * Map< String, Object>m=new HashMap<String, Object>();
			 * m.put("taskNum", newTaskNumber); m.put("title", content_title);
			 * m.put("taskCategory", intent.getStringExtra("taskCategory"));
			 * insTablePushTaskVoList=saveDataDao.queryForFieldValuesArgs(m); }
			 * catch (SQLException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
			insTablePushTaskVo = (InsTablePushTaskVo) intent
					.getSerializableExtra("insTablePushTaskVo");
			if (insTablePushTaskVo != null) {
				newTaskNumber = insTablePushTaskVo.getTaskNum();// 获取新的任务编号
				String names = insTablePushTaskVo.getNames();
				String values = insTablePushTaskVo.getValues();

				ptTvTitle.setText("新任务单");
				ptLayoutMContent.setVisibility(View.GONE);
				ptLayoutDContent.setVisibility(View.VISIBLE);
				ptLayoutDTask.removeAllViews();
				// 调用动态创建控件，显示推送下来的信息
				ptLayoutDTask.addView(dynamicView
						.dynamicAddView2(names, values));

			} else {
				ptTvMMsg.setText("说明：后台推送错误,无工作任务" + "无需理会可直接关闭该页面！");
				return;
			}
			try {
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						myHamdler.sendEmptyMessage(1);
					}
				}, 0, 12000);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		} else if (Constant.PUSH_TITLE_MSG.equals(title)) {// 后台纯消息数据
			if (Constant.PUSH_TITLE_MSG_COMMON.equals(content_title)) {
				ptTvMMsg.setText(message.replace(";", "\n"));
			}
		}
	}

	private void pushtime() {
		// TODO Auto-generated method stub

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if (urgencyLevel.equals("紧急")) {
						Thread.sleep(1800000);
						myHamdler.sendEmptyMessage(2);
					} else if (urgencyLevel.equals("一般")) {
						Thread.sleep(3600000);
						myHamdler.sendEmptyMessage(2);
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();

		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// try {
		// if(urgencyLevel.equals("紧急")){
		// if(ss) {
		// Thread.sleep(1800000);
		// myHamdler.sendEmptyMessage(2);
		// }
		// }else if(urgencyLevel.equals("一般")){
		// if(ss) {
		// Thread.sleep(3600000);
		// myHamdler.sendEmptyMessage(2);
		// }
		// }
		//
		//
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }).start();

	}

	@SuppressWarnings("static-access")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ptBtnDBackSingle:// 退单
			backDialog();
			try {
				thread.interrupt();
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		case R.id.ptBtnDTaskIn:// 接单
			try {
				ptBtnDTaskIn.setEnabled(false);
				try {
					thread.interrupt();
				} catch (Exception e) {
					// TODO: handle exception
				}
				downLoadTask();
			} finally {
			}
			break;
		case R.id.ptImageMBack:// 返回
			finish();
			break;
		case R.id.ptBtnMReply:// 回复
			pToastShow("后期完善");
			break;
		case R.id.ptBtnMClear:// 清空
			ptEtMReturn.setText(null);
			break;
		case R.id.ptBtnMLoc:
			Intent intent = new Intent();
			intent.setClass(context, MapBizViewer.class);

			intent.putExtra("strGraph", pointStr);
			intent.putExtra("type", 10006);
			((Activity) context).startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 1.新任务单下载：异步线程
	 */
	private void downLoadTask() {

		if (TextUtils.isEmpty(newTaskNumber) || newTaskNumber.equals("null")) {
			pToastShow("任务编号,或任务名称为空,任务作废");
			return;
		}
		DownloadTask downloadTask = new DownloadTask(this, insTablePushTaskVo,
				true, myHamdler2);
		downloadTask.execute(AppContext.getInstance().getCurUser()
				.getUserName());
	}

	private void backDialog() {
//		new AlertDialog.Builder(context).setTitle("提示")
//				.setIcon(android.R.drawable.ic_menu_help)
//				.setMessage("确定退单，将删除数据！")
//				.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						dialog.dismiss();
//					}
//				})
//				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
//								PushTaskActivity.this,
//								true,
//								true,
//								insTablePushTaskVo.getTaskNum(),
//								com.gddst.app.mytask.Constant.UPLOAD_STATUS_RETREAT,
//								null, insTablePushTaskVo.getTitle(),
//								insTablePushTaskVo.getTaskCategory(), null,
//								null, null);
//						taskFeedBackAsyncTask.execute();
//					}
//				}).show();

		// v 是退单提示用的
		View v=View.inflate(context, R.layout.return_table_reason_view, null);
		final EditText return_table_reason_et = (EditText) v.findViewById(R.id.return_table_reason_et);
		new AlertDialog.Builder(context).setTitle("提示")
		.setIcon(android.R.drawable.ic_menu_help)
		.setMessage("确定退单，将删除数据！")
		.setView(v)
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
				String s = return_table_reason_et.getText()+"";
				if("".equals(s)){
					Toast.makeText(context, "退单原因不能为空", Toast.LENGTH_LONG).show();
				}else{
					TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
							context,
							true,
							true,
							insTablePushTaskVo.getTaskNum(),
							Constant.UPLOAD_STATUS_RETREAT,
							null, insTablePushTaskVo.getTitle(),
							insTablePushTaskVo.getTaskCategory(), null,
							s, null);
					taskFeedBackAsyncTask.execute();
				}
			}
		}).show();
	
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		timer.cancel();
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//		// 过滤按键动作
//		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//
//			moveTaskToBack(true);
//
//		}
//
//		return super.onKeyDown(keyCode, event);
//	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
        	return true;
		    }
		return super.onKeyDown(keyCode, event);
	}

	int count = 0;
	private Handler myHamdler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
//				BNTTSPlayer.playTTSText("收到新的消息，请查收", -1);
//				Login.xunFeiTTS.playTTS((Activity) context,"收到新的消息，请查收");
				vibrator.vibrate(1500);
				++count;
				if (count == 5) {
					timer.cancel();
				}
				break;

			case 2:
				TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
						PushTaskActivity.this, true, true,
						insTablePushTaskVo.getTaskNum(),
						Constant.UPLOAD_STATUS_RETREAT,
						null, insTablePushTaskVo.getTitle(),
						insTablePushTaskVo.getTaskCategory(), null, null, null);
				taskFeedBackAsyncTask.execute();
				break;

			default:
				break;
			}
		}

	};


	private Handler myHamdler2 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:// 下载成功
				finish();
				break;
			case 10001:// 下载操作完成
				ptBtnDTaskIn.setEnabled(true);
				break;

			default:
				break;
			}
		}

	};

}
