package com.movementinsome.caice.async;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.movementinsome.caice.util.ConstantDate;
import com.movementinsome.caice.vo.HistoryCommitVO;

import java.util.HashMap;
import java.util.List;

public class ResubmitSubAsync extends AsyncTask<String, Void, String> {
	private Activity context;
	private ProgressDialog progressDialog;
	private HashMap<Integer, Boolean> isSelected;
	private List<HistoryCommitVO> historyCommitVOList;

	public ResubmitSubAsync(Context context, HashMap<Integer, Boolean> isSelected, List<HistoryCommitVO> historyCommitVOList) {
		// TODO Auto-generated constructor stub
		this.context = (Activity) context;
		this.isSelected=isSelected;
		this.historyCommitVOList=historyCommitVOList;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog = new ProgressDialog(context);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("提交数据中，请稍后...");
		progressDialog.show();
		
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		if (isSelected.size()>0){
			for (int i=0;i<isSelected.size();i++){
				if (isSelected.get(i)){
//					try {
//						OkHttpRequest.SubmitLatlngBrokenNetwork(
//								historyCommitVOList.get(i).getAddr(),
//								historyCommitVOList.get(i).getTableNum(),
//								historyCommitVOList.get(i).getCaliber(),
//								historyCommitVOList.get(i).getLongitude(),
//								historyCommitVOList.get(i).getLatitude(),
//								new LatLng(Double.parseDouble(historyCommitVOList.get(i).getMyLatitude())
//										,Double.parseDouble(historyCommitVOList.get(i).getMyLongitude())),
//								historyCommitVOList.get(i).getDataType(),
//								historyCommitVOList.get(i).getDrawNum(),
//								"",
//								historyCommitVOList.get(i).getProjectId(),
//								historyCommitVOList.get(i).getShareCode(),
//								historyCommitVOList.get(i).getIsProjectShare(),
//								historyCommitVOList.get(i).getProjectName(),
//								historyCommitVOList.get(i).getSavePointVoId(),
//								historyCommitVOList.get(i),
//								context
//						);
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
				}
			}
		}
		return "0";
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		switch (result){
			case "0":
				AlertDialog.Builder bd = new AlertDialog.Builder(context);
				AlertDialog ad = bd.create();
				ad.setTitle("成功");
				ad.setMessage("提交数据成功");
				ad.show();
				//发广播通知刷新列表
				Intent intent=new Intent();
				intent.setPackage(context.getPackageName());
				intent.setAction(ConstantDate.UPDATALIST);
				context.sendBroadcast(intent);
				break;
			case "1":
				AlertDialog.Builder bd1 = new AlertDialog.Builder(context);
				AlertDialog ad1 = bd1.create();
				ad1.setTitle("错误");
				ad1.setMessage("提交数据失败");
				ad1.show();
				break;
		}
	}
}
