package com.movementinsome.app.mytask.handle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.TextUtils;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.pub.activity.PushTaskActivity;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.database.vo.CoordinateVO;
import com.movementinsome.database.vo.DrainageVO;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.database.vo.MyReceiveMsgVO;
import com.movementinsome.kernel.util.JsonAnalysisUtil;
import com.movementinsome.kernel.util.MyDateTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressLint("NewApi")
public class PushDataHandle implements OnInitListener, PushDataBaseHandle {
	private Context context;
	private MediaPlayer mediaPlayer; // 启动手机铃声
	private Vibrator vibrator; // 启动手机震动

	// private TextToSpeech tts;// 语音

	public PushDataHandle(Context context) {
		this.context = context;
		mediaPlayer = MediaPlayer.create(context, R.raw.push_message_1);
		vibrator = (Vibrator) context
				.getSystemService(context.VIBRATOR_SERVICE);
		// tts = new TextToSpeech(context, this);
	}

	public void analysisMessage(String notificationTitle, String message,
			String pushState) {
		try {
			JSONObject jsonObject = new JSONObject(message);

			if (Constant.PUSH_TITLE_MSG.equals(notificationTitle)) {// 信息()
				pushSetMessage(jsonObject, pushState);
			} else if (Constant.PUSH_TITLE_TASK.equals(notificationTitle)) {// 推送任务单消息
				pushNewTaskMessage(jsonObject, pushState);
			} else if (Constant.PUSH_TITLE_CONTROL.equals(notificationTitle)) {
				pushControlMessage(jsonObject, pushState);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// 将异常返回后台：告诉后台消息异常
			Toast.makeText(context, "解析出现异常", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	/**
	 * title: 任务 taskCategory: 任务类型 taskNum: 任务编号 command: 指令
	 * discardTask(废弃任务)\startTask(开始任务)\pauseTask(暂停任务)\endTask(结束任务)
	 * 
	 * @throws JSONException
	 * 
	 */
	private void pushControlMessage(JSONObject jsonObject, String pushState)
			throws JSONException {
		String title = jsonObject.getString("title");
		String taskCategory = jsonObject.getString("taskCategory");
		String taskNum = jsonObject.getString("taskNum");
		String command = jsonObject.getString("command");
		if ("startTask".equals(command)) {
			TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
					context, false, false, taskNum,
					Constant.UPLOAD_STATUS_START, null,
					title, taskCategory, null, null, null, "true");
			taskFeedBackAsyncTask.execute();
		} else if ("pauseTask".equals(command)) {
			TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
					context, false, false, taskNum,
					Constant.UPLOAD_STATUS_PAUSE, null,
					title, taskCategory, null, null, null);
			taskFeedBackAsyncTask.execute();
		} else if ("endTask".equals(command)) {
			TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
					context, false, false, taskNum,
					Constant.UPLOAD_STATUS_DELETE, null,
					title, taskCategory, null, null, null);
			taskFeedBackAsyncTask.execute();
		} else if ("discardTask".equals(command)) {
			TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
					context, false, false, taskNum,
					Constant.UPLOAD_STATUS_ABANDONED,
					null, title, taskCategory, null, null, null);
			taskFeedBackAsyncTask.execute();
		}
	}

	/**
	 * 1.如果消息推送的是:新工作任务
	 * 
	 * @throws JSONException
	 */
	private void pushNewTaskMessage(JSONObject jsonObject, String pushState)
			throws JSONException {

		/* 使用Map时，在显示出来时无法与动态创建的控件结合 */
		String title = jsonObject.getString("title");// 表单标志
		String number = jsonObject.getString("taskNum");// 任务编号
		String content = jsonObject.getString("content");// 显示内容
		String taskCategory = jsonObject.getString("taskCategory"); // 任务分类
		String urgencyLevel = "";
		Iterator<String> it = jsonObject.keys();
		while (it.hasNext()) {
			if (it.next().equals("urgencyLevel")) {
				urgencyLevel = jsonObject.getString("urgencyLevel"); // 紧急情况
			}
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
				// 通知有主界面有新的任务(更新任务数量)
				Intent intent = new Intent();
				intent.setAction("com.main.menu.view");
				context.sendBroadcast(intent);
			}
		}
		TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
				context, false, false, number,
				Constant.UPLOAD_STATUS_ARRIVE, "", title,
				taskCategory, pushState, null, null);
		taskFeedBackAsyncTask.execute();
		context.startActivity(intentPushTask);
		// showVibrator();
	}

	/**
	 * 4.如果消息推送的是:消息文本
	 */
	private void pushSetMessage(JSONObject jsonObject, String pushState)
			throws JSONException {
		String title = jsonObject.getString("title");// 标志
		if (Constant.PUSH_TITLE_MSG_COMMON.equals(title)) {// 普通消息

			MyReceiveMsgVO myReceiveMsgVO = new MyReceiveMsgVO();
			myReceiveMsgVO.setId(UUID.randomUUID().toString());
			myReceiveMsgVO.setReceiveTime(MyDateTools.date2String(new Date()));
			String content = jsonObject.getString("content");// 显示内容
			Intent intentPushTask = new Intent(context, PushTaskActivity.class);
			intentPushTask.putExtra("title", Constant.PUSH_TITLE_MSG);
			intentPushTask.putExtra("content_title",
					Constant.PUSH_TITLE_MSG_COMMON);
			String[] s = content.split("POINT");
			intentPushTask.putExtra("message", s[0]);
			myReceiveMsgVO.setReceiveMsg(s[0]);
			if (s.length >= 2) {
				intentPushTask.putExtra("POINT", "POINT" + s[s.length - 1]);
				myReceiveMsgVO.setCoordinate("POINT" + s[s.length - 1]);
			}
			try {
				Dao<MyReceiveMsgVO, Long> myReceiveMsgVODao = AppContext
						.getInstance().getAppDbHelper()
						.getDao(MyReceiveMsgVO.class);
				myReceiveMsgVODao.create(myReceiveMsgVO);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			context.startActivity(intentPushTask);
			showVibrator();
			TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
					context, false, false, null, null, null, title, null,
					pushState, null, null);
			taskFeedBackAsyncTask.execute();
		} else if (Constant.PUSH_TITLE_MSG_FINISH_TASK.equals(title)) {// 结束任务
			finishTaskMessage(jsonObject);
		} else if (Constant.PUSH_TITLE_MSG_DRAINAGE.equals(title)) {
			try {
				drainageMessage(jsonObject);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (Constant.PUSH_TITLE_MSG_COORDINATE.equals(title)) {
			try {
				coordinateMessage(jsonObject, pushState);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void drainageMessage(JSONObject jsonObject) throws SQLException {// 防洪
		DrainageVO drainageVO = new DrainageVO();
		JsonAnalysisUtil.setJsonObjectData(jsonObject, drainageVO);
		drainageVO.setId(UUID.randomUUID().toString());
		drainageVO.setReceiveTime(MyDateTools.date2String(new Date()));
		Dao<DrainageVO, Long> drainageVODao = AppContext.getInstance()
				.getAppDbHelper().getDao(DrainageVO.class);
		drainageVODao.create(drainageVO);
		Intent intent = new Intent();
		intent.setAction(Constant.FHPL_ACTION);
		context.sendBroadcast(intent);
		showVibrator();
	}

	private void coordinateMessage(JSONObject jsonObject, String pushState)
			throws SQLException {// 配合工作
		CoordinateVO coordinateVO = new CoordinateVO();
		JsonAnalysisUtil.setJsonObjectData(jsonObject, coordinateVO);

		Dao<CoordinateVO, Long> coordinateVODao = AppContext.getInstance()
				.getAppDbHelper().getDao(CoordinateVO.class);
		List<CoordinateVO> coordinateVOs = coordinateVODao.queryForEq(
				"workTaskNum", coordinateVO.getWorkTaskNum());
		if (coordinateVOs != null && coordinateVOs.size() > 0) {
			coordinateVO.setReceiveTime(MyDateTools.date2String(new Date()));
			coordinateVO.setId(coordinateVOs.get(0).getId());
			coordinateVODao.update(coordinateVO);
		} else {
			coordinateVO.setId(UUID.randomUUID().toString());
			coordinateVO.setReceiveTime(MyDateTools.date2String(new Date()));
			coordinateVODao.create(coordinateVO);
		}

		Intent intent = new Intent();
		intent.setAction(Constant.COORDINATE_ACTION);
		context.sendBroadcast(intent);
		showVibrator();
		TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
				context, false, false, coordinateVO.getWorkTaskNum(),
				Constant.UPLOAD_STATUS_ARRIVE, null,
				coordinateVO.getTitle(), coordinateVO.getTaskCategory(),
				pushState, null, null);
		taskFeedBackAsyncTask.execute();
	}

	private void finishTaskMessage(JSONObject jsonObject) throws JSONException {// 结束任务
		String taskNum = jsonObject.getString("taskNum");
		String taskTitle = jsonObject.getString("taskTitle");
		String taskCategory = jsonObject.getString("taskCategory");
		TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
				context, false, false, taskNum,
				Constant.UPLOAD_STATUS_FINISH, null,
				taskTitle, taskCategory, null, null, null);
		taskFeedBackAsyncTask.execute();
		Intent intentPushTask = new Intent(context, PushTaskActivity.class);
		String message = "提示：后台结束了任务编号为：" + taskNum + "的任务";
		intentPushTask.putExtra("title", Constant.PUSH_TITLE_MSG);
		intentPushTask
				.putExtra("content_title", Constant.PUSH_TITLE_MSG_COMMON);
		intentPushTask.putExtra("message", message);
		context.startActivity(intentPushTask);
		showVibrator();
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
		// mediaPlayer.start();
		// tts.speak("收到新的消息，请查收",TextToSpeech.QUEUE_ADD, null);

//		BNTTSPlayer.playTTSText("收到新的消息，请查收", -1);

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

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub

	}
}
