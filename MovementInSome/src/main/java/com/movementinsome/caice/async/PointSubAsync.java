package com.movementinsome.caice.async;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.movementinsome.AppContext;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.okhttp.OkHttpRequest;
import com.movementinsome.caice.save.SaveFile;
import com.movementinsome.caice.util.CreateFiles;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.database.vo.DynamicFormVO;
import com.movementinsome.database.vo.PicFileInfoVO;
import com.movementinsome.easyform.formengineer.XmlGuiForm;
import com.movementinsome.easyform.util.XmlFiledRuleOperater;
import com.movementinsome.map.MapViewer;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 设施点提交异步任务
 */
public class PointSubAsync extends AsyncTask<String, Void, String> {
	private Activity context;
	private ProgressDialog progressDialog;
	private String contextJson;
	private HashMap iparams;
	private DynamicFormVO dynFormVo;
	private XmlGuiForm theForm;
	private SavePointVo savePointVo;
	private String dataType;


	public PointSubAsync(Context context,HashMap iparams,String dataType,
						 DynamicFormVO dynFormVo, XmlGuiForm theForm) {
		// TODO Auto-generated constructor stub
		this.context = (Activity) context;
		this.theForm=theForm;
		this.iparams=iparams;
		this.dynFormVo=dynFormVo;
		this.dataType=dataType;

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
		try {
			contextJson=theForm.getJsonResults();
			savePointVo=new SavePointVo();
//			savePointVo.setContext(contextJson);

			SaveFile saveFile=new SaveFile(context);

			//保存数据库
            if (dataType.equals(MapMeterMoveScope.POINT)){
                saveFile.SavePointDatabase(savePointVo,iparams,dynFormVo,contextJson);
            }else if (dataType.equals(MapMeterMoveScope.LINE)){
                saveFile.SaveLineDatabase(savePointVo,iparams,dynFormVo,contextJson);
            }

			List<SavePointVo> savePointVoList=new ArrayList<>();
			savePointVoList.add(savePointVo);

			CreateFiles createFiles=new CreateFiles();
			//复制照片到表号文件夹
			List<String> imageList=new ArrayList<>();
			if (savePointVo.getCamera()!=null&&!savePointVo.getCamera().equals("")){
				String [] imageByte= savePointVo.getCamera().split(",");
				if (imageByte.length>0){
					for (int i=0;i<imageByte.length;i++){
						String [] pathByte=imageByte[i].split("/");
						if (pathByte.length>0){
							imageList.add(pathByte[pathByte.length-1]);
						}

					}

//					String newImagePaths="";

					for (int j=0;j<imageByte.length;j++){
						File file = null;
						file = new File(MapViewer.MOVE_MINING_POINT+
								AppContext.getInstance().getCurUserName()+"/"+
								savePointVo.getFacPipBaseVo().getProjectName()+"/"+savePointVo.getFacName());
						if (!file.exists()) {
							file.mkdirs();
						}
						createFiles.mCopyFile(imageByte[j],
								MapViewer.MOVE_MINING_POINT+AppContext.getInstance().getCurUserName()+"/"+
										savePointVo.getFacPipBaseVo().getProjectName()+"/"+savePointVo.getFacName()+"/"+imageList.get(j));
//						if (j==imageByte.length-1){
//							newImagePaths+=MapViewer.MOVE_MINING_POINT+AppContext.getInstance().getCurUserName()+"/"+
//									savePointVo.getFacPipBaseVo().getProjectName()+"/"+savePointVo.getFacName()+"/"+imageList.get(j);
//						}else {
//							newImagePaths+=MapViewer.MOVE_MINING_POINT+AppContext.getInstance().getCurUserName()+"/"+
//									savePointVo.getFacPipBaseVo().getProjectName()+"/"+savePointVo.getFacName()+"/"+imageList.get(j)+",";
//						}
					}


//					//更新原来数据VO
//					Dao<DynamicFormVO, Long> dynamicFormDao = AppContext.getInstance().getAppDbHelper().getDao(DynamicFormVO.class);
//					PointCameraVO pointCameraVO= JSON.parseObject(dynFormVo.getContent(),PointCameraVO.class);
//					pointCameraVO.setCamera(newImagePaths);
//					dynFormVo.setContent(new Gson().toJson(pointCameraVO));
//					int q=dynamicFormDao.update(dynFormVo);
//					if (q==1){
//
//					}

				}


			}

			//提交表单数据
			if (OkHttpRequest.SubmitLatlngBS(savePointVoList)){
				//提交照片
				String attach = theForm.getJsonAttachResults();
				if (!"{}".equals(attach)) {
					JSONObject attachJson = new JSONObject(attach);

					//FileService fileService = AppContext.getInstance().getFileServer();

					String serverUrl = AppContext.getInstance().getFileServerUrl();//+MyPublicData.FILEATTACHUPLOAD;
//                    String serverUrl = "http://172.16.1.20:8086/fileService";
					Iterator it = attachJson.keys();
					int uploadImgNum = 0;
					int uploadImgSucceedNum = 0;
					String images = "";
					while (it.hasNext()) {
						String o = (String) it.next();
						String v = attachJson.getString(o);
						String[] fileNames = v.split("\\,");
						uploadImgNum += fileNames.length;
						for (String fileName : fileNames) {
							String guid = dynFormVo.getGuid();
							String label = null;
							if (fileName.contains(":childrentable:")) {
								String str[] = fileName.split(":");
								fileName = str[0];
								guid = str[2];
								label = "照片";
							} else {
								label = theForm.findField(o).getLabel();
							}
							PicFileInfoVO fpiVo = XmlFiledRuleOperater.getPictureLog(guid, label, fileName);
					/*if (fileService.isCompress()){
                        if (fileService.getType().equalsIgnoreCase("size")){

                        Bitmap bit = BitmapCompress.compressImageBySize(fileName, Integer.valueOf(fileService.getConfig()));

                        }else if (fileService.getType().equalsIgnoreCase("scale")){

                        }
                    }*/
							if (fpiVo.getIsUpload() != null && "true".equals(fpiVo.getIsUpload())) {
								++uploadImgSucceedNum;
							} else {
								File file = new File(fileName);
								String rlt = SpringUtil.attachFileUpload(serverUrl, fpiVo.toJson(), file);
								if (SpringUtil.FAILURE.equals(rlt)) {
									String rlt1 = SpringUtil.attachFileUpload(serverUrl, fpiVo.toJson(), file);
									if (SpringUtil.SUCCESS.equals(rlt)) {
										String s = (images.equals("") ? fpiVo.getPfiName() : "," + fpiVo.getPfiName());
										images += s;
										++uploadImgSucceedNum;
									}
								} else {
									String s = (images.equals("") ? fpiVo.getPfiName() : "," + fpiVo.getPfiName());
									images += s;
									++uploadImgSucceedNum;
								}
							}
						}

					}
					if (uploadImgNum == uploadImgSucceedNum && !"".equals(images)) {
						JSONObject jo = new JSONObject();
						jo.put("tableName", theForm.getTable());
						jo.put("moiNum", dynFormVo.getId());
						jo.put("imei", dynFormVo.getImei());
						jo.put("status", "1");
						jo.put("images", images);
						SpringUtil.imgStatusUpload(AppContext.getInstance().getServerUrl(), jo.toString());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "1";
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
				Intent intent = new Intent();
				intent.putExtra(OkHttpParam.PROJECT_STATUS, true);
				intent.putExtra("savePointVo", savePointVo);
				context.setResult(5, intent);
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
