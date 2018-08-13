package com.movementinsome.caice.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.caice.vo.CreatProjectVo;
import com.movementinsome.caice.vo.MiningSurveyVO;

import java.sql.SQLException;
import java.util.List;

public class CommitDateTask extends AsyncTask<String, Void, String> {
	private Activity activity;
	private ProgressDialog progressDialog;
	private String url;
	private String json;


	public CommitDateTask(Activity activity, String url,String json) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.url=url;
		this.json=json;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog = new ProgressDialog(activity);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("请求中...");
		progressDialog.show();
		
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generted method stub
		try {
			String response = SpringUtil.postData(url, json);
			if (response!=null){
				//请求成功更新到本地数据库
				Dao<MiningSurveyVO, Long> miningSurveyVOLongDao = AppContext.getInstance()
						.getAppDbHelper().getDao(MiningSurveyVO.class);
				CreatProjectVo creatProjectVo = new Gson().fromJson(response, CreatProjectVo.class);
				if (creatProjectVo.getPois() != null && creatProjectVo.getPois().size() > 0) {
					for (int i = 0; i < creatProjectVo.getPois().size(); i++) {
						List<MiningSurveyVO> miningSurveyVOList = miningSurveyVOLongDao
								.queryForEq("projectId", creatProjectVo.getPois().get(i).getProjectId());
						//只有服务器有本地没有才更新到本地
						if (miningSurveyVOList.size() == 0) {
							MiningSurveyVO min = new MiningSurveyVO();
							CreatProjectVo.ProjectBean projectBean = creatProjectVo.getPois().get(i);

							min.setProjectId(projectBean.getProjectId());
							min.setAutoNumberLine(projectBean.getAutoNumberLine());
							min.setAutoNumber(projectBean.getAutoNumber());
							min.setIsCompile("0");  //请求成功后把是否编辑设为0
							min.setIsPresent("1");  //请求成功后把是否提交设为1
							min.setIsSubmit("0");   //请求成功后把工程设为未完成状态
							min.setIsProjectShare(projectBean.getIsProjectShare());
							min.setShareCode(projectBean.getShareCode());
							min.setPointVos(projectBean.getPointVos());
							min.setProjcetLineLenghts(projectBean.getProjcetLineLenghts());
							min.setProjectEDateStr(projectBean.getProjectEDate());
							min.setProjectEDateUpd(projectBean.getProjectEDateUpd());
							min.setProjectName(projectBean.getProjectName());
							min.setProjectNum(projectBean.getProjectNum());
							min.setProjectSDateStr(projectBean.getProjectSDate());
							min.setProjectType(projectBean.getProjectType());
							min.setTaskNum(projectBean.getTaskNum());
							min.setUsedName(projectBean.getUsedName());
							min.setUsedId(projectBean.getUsedId());
							min.setPointVos(projectBean.getPointVos());

							int s = miningSurveyVOLongDao.create(min);
						}
					}
				}

				//打开工程
//			if (miningSurveyVO != null) {
//				ProjectVo m = new ProjectVo();
//				m = miningSurveyVOLongDao.queryForEq
//						("projectId", miningSurveyVO.getProjectId()).get(0);
//				if (!m.getProjectId().equals("")) {
//					activity.GatherMove(m, true);
//				}
//			}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
//		if(result!=null&&result.equals("Ok")){
//			alertDiaChildlog.dismiss();
//			Toast.makeText(context,"导出成功", Toast.LENGTH_SHORT).show();
//		}else if(result!=null&&result.equals("0")){
//			Toast.makeText(context,"选择的日期内没有相应的任务", Toast.LENGTH_SHORT).show();
//		}else{
//			Toast.makeText(context,"导出失败", Toast.LENGTH_SHORT).show();
//			File file = new File(fileNameStr);
//			if(file.exists()){
//				file.delete();
//			}
//		}
	}
}
