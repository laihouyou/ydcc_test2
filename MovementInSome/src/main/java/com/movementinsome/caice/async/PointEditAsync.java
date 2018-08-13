package com.movementinsome.caice.async;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.movementinsome.R;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.util.SaveData;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.database.vo.DynamicFormVO;
import com.movementinsome.easyform.formengineer.XmlGuiForm;

import java.util.HashMap;

/**
 * 设施点提交异步任务
 */
public class PointEditAsync extends AsyncTask<String, Void, String> {
	private Activity context;
	private ProgressDialog progressDialog;
	private HashMap iparams;
	private DynamicFormVO dynFormVo;
	private XmlGuiForm theForm;
	private Bundle pointLineBundle;
	private String drawType;
	private SavePointVo savePointVo;

	public PointEditAsync(Context context, XmlGuiForm theForm, HashMap iparams
			, SavePointVo savePointVo, DynamicFormVO dynFormVo,String drawType) {
		// TODO Auto-generated constructor stub
		this.context = (Activity) context;
		this.theForm=theForm;
		this.iparams=iparams;
		this.dynFormVo=dynFormVo;
		this.savePointVo=savePointVo;
		this.drawType=drawType;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog = new ProgressDialog(context);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(context.getString(R.string.load_msg3));
		progressDialog.show();
		
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		if (SaveData.EditPointDate(
				context,
				theForm,
				iparams,savePointVo , dynFormVo,drawType)) {
			return "0";
		} else {
			return "1";
		}
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
				Intent intent=new Intent();
				intent.putExtra(OkHttpParam.PROJECT_STATUS,true);
				context.setResult(5,intent);
				context.finish();
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
