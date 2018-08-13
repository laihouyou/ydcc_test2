package com.movementinsome.app.server;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.app.mytask.handle.DownloadBaseHandle;
import com.movementinsome.app.mytask.handle.DownloadHandle;
import com.movementinsome.app.mytask.handle.PCDownloadHandle;
import com.movementinsome.app.mytask.handle.WsDownloadHandle;
import com.movementinsome.app.mytask.handle.XJYSDownloadHandle;
import com.movementinsome.app.mytask.handle.YHDownloadHandle;
import com.movementinsome.app.mytask.handle.ZSDownloadHandle;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.database.vo.InsTablePushTaskVo;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 异步线程下载任务设施
 * @author gddst
 *
 */
public class DownloadTask extends AsyncTask<String, Void, String>{

	//private SpringUtil springUtil;		//下载方法
	private Activity context;			//context对象应用
	private ProgressDialog progre;		//进度条显示
	private Handler myHandler;
	private InsTablePushTaskVo insTablePushTaskVo;// 推送对象
	private List<DownloadBaseHandle> downloadBaseHandleList;
	///-----------下载异常监控------------////

	public DownloadTask(Activity context,InsTablePushTaskVo insTablePushTaskVo,boolean showProgre,Handler myHandler) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.insTablePushTaskVo = insTablePushTaskVo;
		this.myHandler=myHandler;
		downloadBaseHandleList=new ArrayList<DownloadBaseHandle>();
		if(showProgre){
			progre = new ProgressDialog(context);
			progre.setCancelable(false);
			progre.setCanceledOnTouchOutside(false);
			progre.setMessage("正在下载,请勿退出,耐心等候……");
			progre.show();
		}else{
			Toast.makeText(context, "系统后台下载中……", 4).show();
		}
	}
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		if(insTablePushTaskVo==null){
			Toast.makeText(context, "推送消息有误,无法下载", 4).show();
			return "3";
		}
		//springUtil = new  SpringUtil(context.getApplicationContext());
		String url = AppContext.getInstance().getServerUrl();
		url += SpringUtil._REST_TASKDOWN;
		JSONObject ob=new JSONObject();
		try {
			ob.put("taskNum", insTablePushTaskVo.getTaskNum());
			ob.put("usUsercode", params[0]);
			ob.put("tableName", insTablePushTaskVo.getTitle());
			ob.put("imei", AppContext.getInstance().getPhoneIMEI());
			ob.put("taskCategory", insTablePushTaskVo.getTaskCategory());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		String result = 
				SpringUtil.postData(url, ob.toString());	
		downloadBaseHandleList.add(new DownloadHandle(result, insTablePushTaskVo));
		downloadBaseHandleList.add(new WsDownloadHandle(result, insTablePushTaskVo));
		downloadBaseHandleList.add(new XJYSDownloadHandle(result, insTablePushTaskVo));
		downloadBaseHandleList.add(new YHDownloadHandle(result, insTablePushTaskVo));
		downloadBaseHandleList.add(new ZSDownloadHandle(result, insTablePushTaskVo));
		downloadBaseHandleList.add(new PCDownloadHandle(result, insTablePushTaskVo));
		for(int i=0;i<downloadBaseHandleList.size();++i){
			String s=downloadBaseHandleList.get(i).handle();
			if(!"3".equals(s)){
				return s;
			}
		}
		return "3";
	}
	private void updatePush(){
		try {
			Dao<InsTablePushTaskVo, Long> insTablePushTaskDao = AppContext.getInstance().getAppDbHelper().getDao(InsTablePushTaskVo.class);
			Map<String , Object>m=new HashMap<String, Object>();
			m.put("taskNum", insTablePushTaskVo.getTaskNum());
			m.put("taskCategory", insTablePushTaskVo.getTaskCategory());
			m.put("title", insTablePushTaskVo.getTitle());
			List<InsTablePushTaskVo> insTablePushTaskVoList=insTablePushTaskDao.queryForFieldValuesArgs(m);
			if(insTablePushTaskVoList!=null&&insTablePushTaskVoList.size()>0){
				InsTablePushTaskVo insTablePushTaskVo=insTablePushTaskVoList.get(0);
				insTablePushTaskVo.setIsDownload("1");
				insTablePushTaskDao.update(insTablePushTaskVo);
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(progre != null)
			progre.dismiss();
		if(myHandler!=null){
			myHandler.sendEmptyMessage(10001);
		}
		if(result==null){
			Toast.makeText(context, "连接超时，请重新下载", 4).show();
		}else if("0".equals(result)){
			Toast.makeText(context, "下载任务为空，请重新下载", 4).show();
		}else if("3".equals(result)){
			Toast.makeText(context,"没有相应的任务类型", 4).show();
		}else if("1".equals(result)){
			Toast.makeText(context, "下载成功，请尽快完成", 4).show();
			TaskFeedBackAsyncTask taskFeedBackAsyncTask=
				new TaskFeedBackAsyncTask(context, false,false, insTablePushTaskVo.getTaskNum()
						, Constant.UPLOAD_STATUS_RECEIVE, insTablePushTaskVo.getGuid(), insTablePushTaskVo.getTitle(), insTablePushTaskVo.getTaskCategory(),null,null,null);
			taskFeedBackAsyncTask.execute("");
			if(myHandler!=null){
				myHandler.sendEmptyMessage(1);
				return;
			}
			context.finish();
		}
		
	}
}
