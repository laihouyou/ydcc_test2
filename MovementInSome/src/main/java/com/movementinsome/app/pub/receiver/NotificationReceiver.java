package com.movementinsome.app.pub.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.text.TextUtils;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.mytask.handle.PushDataBaseHandle;
import com.movementinsome.app.mytask.handle.PushDataHandle;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.pub.activity.PushTaskActivity;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.xmpp.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Broadcast receiver that handles push notification messages from the server.
 * This should be registered as receiver in AndroidManifest.xml.
 * 
 */
public final class NotificationReceiver extends BroadcastReceiver {

	private MediaPlayer mediaPlayer; // 启动手机铃声
	private Vibrator vibrator; // 启动手机震动
	static NotificationReceiver notificationReceiver = null;
	private Context context;
	private String pushState;
	private PushDataBaseHandle pushDataHandle;

	public static NotificationReceiver getInstance() {
		if (notificationReceiver == null) {
			notificationReceiver = new NotificationReceiver();
		}
		return notificationReceiver;
	}

	// 启动广播信息拦截
	public void start(Context context) {
		this.context = context;
		pushDataHandle = new PushDataHandle(context);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.ACTION_SHOW_NOTIFICATION);
		filter.addAction(Constants.ACTION_NOTIFICATION_CLICKED);
		filter.addAction(Constants.ACTION_NOTIFICATION_CLEARED);
		context.registerReceiver(notificationReceiver, filter);
	}

	// 停止广播信息拦截
	public void stop(Context context) {
		context.unregisterReceiver(notificationReceiver);
	}

	@SuppressWarnings("static-access")
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (Constants.ACTION_SHOW_NOTIFICATION.equals(action)) {
			String notificationTitle = intent
					.getStringExtra(Constants.NOTIFICATION_TITLE);
			String notificationMessage = intent
					.getStringExtra(Constants.NOTIFICATION_MESSAGE);
			pushState = intent
					.getStringExtra(Constants.NOTIFICATION_PUSH_STATE);
			// analysisMessage(notificationTitle,notificationMessage);
			pushDataHandle.analysisMessage(notificationTitle,
					notificationMessage, pushState);
		}

	}

	private void analysisMessage(String notificationTitle, String message) {
		try {

			JSONObject jsonObject = new JSONObject(message);

			if (Constant.PUSH_TITLE_MSG.equals(notificationTitle)) {// 信息()
				pushSetMessage(jsonObject);
			} else if (Constant.PUSH_TITLE_TASK.equals(notificationTitle)) {// 推送任务单消息
				pushNewTaskMessage(jsonObject);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// 将异常返回后台：告诉后台消息异常
			Toast.makeText(context, "解析出现异常", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	/**
	 * 1.如果消息推送的是:新工作任务
	 * 
	 * @throws JSONException
	 */
	private void pushNewTaskMessage(JSONObject jsonObject) throws JSONException {

		/* 使用Map时，在显示出来时无法与动态创建的控件结合 */
		String title = jsonObject.getString("title");// 表单标志
		String number = jsonObject.getString("taskNum");// 任务编号
		String content = jsonObject.getString("content");// 显示内容
		String taskCategory = jsonObject.getString("taskCategory"); // 任务分类
		String urgencyLevel = "";
		if (jsonObject.getString("urgencyLevel") != null) {
			urgencyLevel = jsonObject.getString("urgencyLevel"); // 紧急情况
		}
		InsTablePushTaskVo insTablePushTaskVo = new InsTablePushTaskVo();
		insTablePushTaskVo.setGuid(UUID.randomUUID().toString());
		insTablePushTaskVo.setTitle(title);// 表单标志
		insTablePushTaskVo.setTaskNum(number);// 任务编号
		JSONObject contentOb = new JSONObject(content);// 显示内容
		insTablePushTaskVo.setTaskCategory(taskCategory);// 分类
		String namesStr = "";
		String valuesStr = "";
		for (int i = 0; i < contentOb.length(); ++i) {
			String contentStr = contentOb.getString((i + 1) + "");
			if (contentStr != null) {
				String[] key_value = contentStr.split(",");
				if (key_value != null && key_value.length >= 2) {
					key_value[0] = "null".equals(key_value[0]) ? ""
							: key_value[0];
					key_value[1] = "null".equals(key_value[0]) ? ""
							: key_value[1];
					if (i == 0) {
						namesStr = key_value[0];
						valuesStr = key_value[1];
					} else {
						namesStr = namesStr + "," + key_value[0];
						valuesStr = valuesStr + "," + key_value[1];
					}
				}
			}
		}
		insTablePushTaskVo.setNames(namesStr);
		insTablePushTaskVo.setValues(valuesStr);
		Intent intentPushTask = new Intent(context, PushTaskActivity.class);
		// 判断是任务是否合格
		if (TextUtils.isEmpty(number) || number.equals("null")) {
			String message = "说明：后台推送了错误无(任务)工作任务，系统内部已经删除该任务，"
					+ "无需理会可直接关闭该页面！";
			intentPushTask.putExtra("title", Constant.PUSH_TITLE_MSG);
			intentPushTask.putExtra("content_title",
					Constant.PUSH_TITLE_MSG_COMMON);
			intentPushTask.putExtra("message", message);
			intentPushTask.putExtra("urgencyLevel", urgencyLevel);
		} else {
			if (isExistTable(number, title, taskCategory)) {
				String message = "说明：后台推送了相同的工作任务，系统内部已做屏蔽处理，"
						+ "无需理会可直接关闭该页面！";
				intentPushTask.putExtra("title", Constant.PUSH_TITLE_MSG);
				intentPushTask.putExtra("content_title",
						Constant.PUSH_TITLE_MSG_COMMON);
				intentPushTask.putExtra("message", message);
				intentPushTask.putExtra("urgencyLevel", urgencyLevel);
			} else {
				saveData(insTablePushTaskVo);
				intentPushTask.putExtra("title", Constant.PUSH_TITLE_TASK);
				intentPushTask.putExtra("content_title", title);
				intentPushTask.putExtra("urgencyLevel", urgencyLevel);
				intentPushTask.putExtra("insTablePushTaskVo",
						insTablePushTaskVo);

			}
		}
		TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
				context, false, false, number,
				Constant.UPLOAD_STATUS_ARRIVE, "", title,
				taskCategory, pushState, null, null);
		taskFeedBackAsyncTask.execute();
		context.startActivity(intentPushTask);
		showVibrator();
	}

	/**
	 * 4.如果消息推送的是:消息文本
	 */
	private void pushSetMessage(JSONObject jsonObject) throws JSONException {
		String title = jsonObject.getString("title");// 标志
		String content = jsonObject.getString("content");// 显示内容
		if (Constant.PUSH_TITLE_MSG_COMMON.equals(title)) {// 普通消息
			Intent intentPushTask = new Intent(context, PushTaskActivity.class);
			intentPushTask.putExtra("title", Constant.PUSH_TITLE_MSG);
			intentPushTask.putExtra("content_title",
					Constant.PUSH_TITLE_MSG_COMMON);
			intentPushTask.putExtra("message", content);
			context.startActivity(intentPushTask);
			showVibrator();
			TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
					context, false, false, null, null, null, title, null,
					pushState, null, null);
			taskFeedBackAsyncTask.execute();
		}
	}

	private void saveData(InsTablePushTaskVo insTablePushTaskVo) {
		try {
			Dao<InsTablePushTaskVo, Long> saveDataDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsTablePushTaskVo.class);
			saveDataDao.create(insTablePushTaskVo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Toast.makeText(context, "保存推送消息失败", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	private void showVibrator() {
		if (mediaPlayer == null) {
			mediaPlayer = MediaPlayer.create(context.getApplicationContext(),
					R.raw.push_message_1);
		}
		if (vibrator == null) {
			vibrator = (Vibrator) context
					.getSystemService(context.VIBRATOR_SERVICE);
		}
		mediaPlayer.start();
		vibrator.vibrate(1500);
	}

	private boolean isExistTable(String mainNumber, String title,
			String taskCategory) {
		try {
			Dao<InsTablePushTaskVo, Long> saveDataDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsTablePushTaskVo.class);
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("title", title);
			m.put("taskCategory", taskCategory);
			m.put("taskNum", mainNumber);
			List<InsTablePushTaskVo> insTableSaveDataList = saveDataDao
					.queryForFieldValuesArgs(m);
			if (insTableSaveDataList != null && insTableSaveDataList.size() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
