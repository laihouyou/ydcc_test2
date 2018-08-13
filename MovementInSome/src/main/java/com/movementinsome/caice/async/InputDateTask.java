package com.movementinsome.caice.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.caice.util.ExcelUtils;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.map.nearby.ToastUtils;

import java.io.IOException;
import java.sql.SQLException;

import jxl.read.biff.BiffException;

public class InputDateTask extends AsyncTask<String, Void, String> {
	private Context context;
	private ProgressDialog progressDialog;
	private String filepath;
	private int index;
	private ProjectVo projectVo;

	public InputDateTask(Context context,String filepath, int index, ProjectVo projectVo) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.filepath=filepath;
		this.index=index;
		this.projectVo = projectVo;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog = new ProgressDialog(context);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("导入数据中，请等待...");
		progressDialog.show();
		
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String result="";
		try {
			 result=ExcelUtils.readExcle(filepath,index, projectVo);
		} catch (IOException e) {
			e.printStackTrace();
			return "-1";
		} catch (BiffException e) {
			e.printStackTrace();
			return "-2";
		} catch (SQLException e) {
			e.printStackTrace();
			return "-3";
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
		if (result!=null){
			if (result.equals("-1")){
				ToastUtils.show(AppContext.getInstance().getString(R.string.error_msg));
			}else if (result.equals(-2)){
				ToastUtils.show(AppContext.getInstance().getString(R.string.error_msg2));
			}else if (result.equals(-3)){
				ToastUtils.show(AppContext.getInstance().getString(R.string.error_msg3));
			}else {
				ToastUtils.show(result);
			}
		}
	}
}
