package com.movementinsome.caice.async;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.okhttp.OkHttpRequest;
import com.movementinsome.caice.util.UUIDUtil;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.database.vo.DynamicFormVO;
import com.movementinsome.database.vo.PicFileInfoVO;
import com.movementinsome.easyform.util.XmlFiledRuleOperater;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 设施点提交异步任务
 */
public class SubAllDataAsync extends AsyncTask<String, Void, String> {
	private Activity context;
	private String contextJson;
	private List<SavePointVo> savePointVoList;
    private Handler handler;

	public SubAllDataAsync(Context context, List<SavePointVo> savePointVoList,Handler handler) {
		// TODO Auto-generated constructor stub
		this.context = (Activity) context;
		this.savePointVoList=savePointVoList;
		this.handler=handler;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
        Message msg = new Message();
        msg.what = 4;
        Bundle bundle=new Bundle();
        bundle.putInt("pos", savePointVoList.size());
        msg.setData(bundle);
        handler.sendMessage(msg);
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
            Dao<SavePointVo,Long> savePointVoLongDao=AppContext.getInstance().getSavePointVoDao();
            Dao<DynamicFormVO,Long> dynamicFormVOLongDao=AppContext.getInstance()
                    .getAppDbHelper().getDao(DynamicFormVO.class);
            for (int i = 0; i < savePointVoList.size(); i++) {
                Message msg = new Message();
                msg.what = 5;
                Bundle bundle=new Bundle();
                bundle.putInt("pos", savePointVoList.size());
                bundle.putInt("i",i+1);
                msg.setData(bundle);
                handler.sendMessage(msg);
                SavePointVo savePointVo = savePointVoList.get(i);

                //1将数据中不是json格式的数据转换成json
//                JSONObject contextJson = null;
//                try {
//                    contextJson = new JSONObject(contextStr);
//                } catch (JSONException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//
//                try {
//                    JSONObject formContext = contextJson.getJSONObject("formContext");
//                    JSONObject formContext1 = contextJson.getJSONObject("labelValueMap_formContex");
//                    JSONObject formContext2 = contextJson.getJSONObject("FacPipBaseVo");
//                } catch (JSONException e) {
//                    // TODO: handle exception
//                    String formContextStr = null;
//                    try {
//                        formContextStr = contextJson.getString("formContext");
//
//                    } catch (JSONException e1) {
//                        if (formContextStr == null) {
//                            continue;
//                        }
//                        e1.printStackTrace();
//                    }
//                    JSONObject formContextJson = null;
//                    try {
//                        formContextJson = new JSONObject(formContextStr);
//
//                    } catch (JSONException e1) {
//                        // TODO Auto-generated catch block
//                        e1.printStackTrace();
//                    }
//                    try {
//                        contextJson.put("formContext", formContextJson);
//                    } catch (JSONException e1) {
//                    }
//
//                    String labelValueMap_formContexStr = null;
//                    try {
//                        labelValueMap_formContexStr = contextJson.getString("labelValueMap_formContex");
//                    } catch (JSONException e1) {
//                        // TODO Auto-generated catch block
//                        e1.printStackTrace();
//                    }
//                    JSONObject labelValueMap_formContexStrJson = null;
//                    try {
//                        labelValueMap_formContexStrJson = new JSONObject(labelValueMap_formContexStr);
//                    } catch (JSONException e1) {
//                        // TODO Auto-generated catch block
//                        e1.printStackTrace();
//                    }
//                    try {
//                        contextJson.put("labelValueMap_formContex", labelValueMap_formContexStrJson);
//                    } catch (JSONException e1) {
//                        // TODO Auto-generated catch block
//                        e1.printStackTrace();
//                    }
//
//                    String FacPipBaseVoStr = null;
//                    try {
//                        FacPipBaseVoStr = contextJson.getString("FacPipBaseVo");
//                    } catch (JSONException e1) {
//                        // TODO Auto-generated catch block
//                        e1.printStackTrace();
//                    }
//                    JSONObject FacPipBaseVoJson = null;
//                    try {
//                        FacPipBaseVoJson = new JSONObject(FacPipBaseVoStr);
//                    } catch (JSONException e1) {
//                        // TODO Auto-generated catch block
//                        e1.printStackTrace();
//                    }
//                    try {
//                        contextJson.put("FacPipBaseVo", FacPipBaseVoJson);
//                    } catch (JSONException e1) {
//                        // TODO Auto-generated catch block
//                        e1.printStackTrace();
//                    }
//
//                    savePointVo.setContextStr(contextJson.toString());
//                    int s = savePointVoLongDao.update(savePointVo);
//                    Log.i("tag",s+"");
//                }


                //2替换本地表单
                String guid=savePointVo.getGuid();
                List<DynamicFormVO> dynamicFormVOS=dynamicFormVOLongDao.queryForEq(OkHttpParam.GUID,guid);      //guid 唯一关联码，不是主键
                if (dynamicFormVOS.size()==1){
                    DynamicFormVO dynamicFormVO=dynamicFormVOS.get(0);
                    String newGuid= UUIDUtil.getUUID();
                    dynamicFormVO.setGuid(newGuid);
                    int s=dynamicFormVOLongDao.update(dynamicFormVO);
                    Log.i("tag",s+"");

                    String contextStr = savePointVo.getContextStr();
                    JSONObject contextJson = new JSONObject(contextStr);
                    String dynFormContextStr=dynamicFormVO.getContent();
                    JSONObject dunFormJson=new JSONObject(dynFormContextStr);
                    contextJson.put(OkHttpParam.FORM_CONTEXT,dunFormJson);
                    savePointVo.setContextStr(contextJson.toString());

                    //3.提交数据以及照片
                    savePointVo.setGuid(dynamicFormVO.getGuid());
                    int a=savePointVoLongDao.update(savePointVo);
                    Log.i("tag",a+"");
                    List<SavePointVo> savePointVoList=new ArrayList<>(1);
                    savePointVoList.add(savePointVo);
                    //提交表单数据
                    if (OkHttpRequest.SubmitLatlngAllData(savePointVoList)){
						//提交照片
						String [] imageByte= savePointVo.getCamera().split(",");
						String serverUrl = AppContext.getInstance().getFileServerUrl();//+MyPublicData.FILEATTACHUPLOAD;
                        if (imageByte.length>0){
                            for(String fileName : imageByte){
                                if (!fileName.equals("")){
                                    File file = new File(fileName);
                                    if (file.exists()){
                                        String guid_ = dynamicFormVO.getGuid();
                                        String label = "照片";

                                        PicFileInfoVO fpiVo = XmlFiledRuleOperater.getPictureLog2(guid_, label, fileName);
                                        String rlt = SpringUtil.attachFileUpload(serverUrl, fpiVo.toJson(), file);
                                        if (SpringUtil.FAILURE.equals(rlt)) {
                                            String rlt1 = SpringUtil.attachFileUpload(serverUrl, fpiVo.toJson(), file);
                                            if (SpringUtil.SUCCESS.equals(rlt)) {
                                                Log.i("tag",rlt);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

//			contextJson=theForm.getJsonResults();
//			savePointVo=new SavePointVo();
////			savePointVo.setContext(contextJson);
//
//			SaveFile saveFile=new SaveFile(context);
//
//			//保存数据库
//			saveFile.SavePointDatabase(savePointVo,iparams,dynFormVo,contextJson);
//
//			List<SavePointVo> savePointVoList=new ArrayList<>();
//			savePointVoList.add(savePointVo);
//
//			CreateFiles createFiles=new CreateFiles();
//			//复制照片到表号文件夹
//			List<String> imageList=new ArrayList<>();
//			if (savePointVo.getCamera()!=null&&!savePointVo.getCamera().equals("")){
//				String [] imageByte= savePointVo.getCamera().split(",");
//				if (imageByte.length>0){
//					for (int i=0;i<imageByte.length;i++){
//						String [] pathByte=imageByte[i].split("/");
//						if (pathByte.length>0){
//							imageList.add(pathByte[pathByte.length-1]);
//						}
//
//					}
//
////					String newImagePaths="";
//
//					for (int j=0;j<imageByte.length;j++){
//						File file = null;
//						file = new File(MapViewer.MOVE_MINING_POINT+
//								AppContext.getInstance().getCurUserName()+"/"+
//								savePointVo.getFacPipBaseVo().getProjectName()+"/"+savePointVo.getFacName());
//						if (!file.exists()) {
//							file.mkdirs();
//						}
//						createFiles.mCopyFile(imageByte[j],
//								MapViewer.MOVE_MINING_POINT+AppContext.getInstance().getCurUserName()+"/"+
//										savePointVo.getFacPipBaseVo().getProjectName()+"/"+savePointVo.getFacName()+"/"+imageList.get(j));
////						if (j==imageByte.length-1){
////							newImagePaths+=MapViewer.MOVE_MINING_POINT+AppContext.getInstance().getCurUserName()+"/"+
////									savePointVo.getFacPipBaseVo().getProjectName()+"/"+savePointVo.getFacName()+"/"+imageList.get(j);
////						}else {
////							newImagePaths+=MapViewer.MOVE_MINING_POINT+AppContext.getInstance().getCurUserName()+"/"+
////									savePointVo.getFacPipBaseVo().getProjectName()+"/"+savePointVo.getFacName()+"/"+imageList.get(j)+",";
////						}
//					}
//
//
////					//更新原来数据VO
////					Dao<DynamicFormVO, Long> dynamicFormDao = AppContext.getInstance().getAppDbHelper().getDao(DynamicFormVO.class);
////					PointCameraVO pointCameraVO= JSON.parseObject(dynFormVo.getContent(),PointCameraVO.class);
////					pointCameraVO.setCamera(newImagePaths);
////					dynFormVo.setContent(new Gson().toJson(pointCameraVO));
////					int q=dynamicFormDao.update(dynFormVo);
////					if (q==1){
////
////					}
//
//				}
//
//
//			}
//
//			//提交表单数据
//			if (OkHttpRequest.SubmitLatlngBS(savePointVoList)){
//				//提交照片
//				String attach = theForm.getJsonAttachResults();
//				if (!"{}".equals(attach)) {
//					JSONObject attachJson = new JSONObject(attach);
//
//					//FileService fileService = AppContext.getInstance().getFileServer();
//
//					String serverUrl = AppContext.getInstance().getFileServerUrl();//+MyPublicData.FILEATTACHUPLOAD;
////                    String serverUrl = "http://172.16.1.20:8086/fileService";
//					Iterator it = attachJson.keys();
//					int uploadImgNum = 0;
//					int uploadImgSucceedNum = 0;
//					String images = "";
//					while (it.hasNext()) {
//						String o = (String) it.next();
//						String v = attachJson.getString(o);
//						String[] fileNames = v.split("\\,");
//						uploadImgNum += fileNames.length;
//						for (String fileName : fileNames) {
//							String guid = dynFormVo.getGuid();
//							String label = null;
//							if (fileName.contains(":childrentable:")) {
//								String str[] = fileName.split(":");
//								fileName = str[0];
//								guid = str[2];
//								label = "照片";
//							} else {
//								label = theForm.findField(o).getLabel();
//							}
//							PicFileInfoVO fpiVo = XmlFiledRuleOperater.getPictureLog(guid, label, fileName);
//					/*if (fileService.isCompress()){
//                        if (fileService.getType().equalsIgnoreCase("size")){
//
//                        Bitmap bit = BitmapCompress.compressImageBySize(fileName, Integer.valueOf(fileService.getConfig()));
//
//                        }else if (fileService.getType().equalsIgnoreCase("scale")){
//
//                        }
//                    }*/
//							if (fpiVo.getIsUpload() != null && "true".equals(fpiVo.getIsUpload())) {
//								++uploadImgSucceedNum;
//							} else {
//								File file = new File(fileName);
//								String rlt = SpringUtil.attachFileUpload(serverUrl, fpiVo.toJson(), file);
//								if (SpringUtil.FAILURE.equals(rlt)) {
//									String rlt1 = SpringUtil.attachFileUpload(serverUrl, fpiVo.toJson(), file);
//									if (SpringUtil.SUCCESS.equals(rlt)) {
//										String s = (images.equals("") ? fpiVo.getPfiName() : "," + fpiVo.getPfiName());
//										images += s;
//										++uploadImgSucceedNum;
//									}
//								} else {
//									String s = (images.equals("") ? fpiVo.getPfiName() : "," + fpiVo.getPfiName());
//									images += s;
//									++uploadImgSucceedNum;
//								}
//							}
//						}
//
//					}
//					if (uploadImgNum == uploadImgSucceedNum && !"".equals(images)) {
//						JSONObject jo = new JSONObject();
//						jo.put("tableName", theForm.getTable());
//						jo.put("moiNum", dynFormVo.getId());
//						jo.put("imei", dynFormVo.getImei());
//						jo.put("status", "1");
//						jo.put("images", images);
//						SpringUtil.imgStatusUpload(AppContext.getInstance().getServerUrl(), jo.toString());
//					}
//				}
//			}

		} catch (Exception e) {
			e.printStackTrace();
			return "1";
		}
		return "0";
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
        switch (result){
            case "0":
                Message msg = new Message();
                msg.what = 6;
                Bundle bundle=new Bundle();
                bundle.putInt("pos", 0);
                msg.setData(bundle);
                handler.sendMessage(msg);
                break;
            case "1":
                Message msg2 = new Message();
                msg2.what = 6;
                Bundle bundle2=new Bundle();
                bundle2.putInt("pos", 1);
                msg2.setData(bundle2);
                handler.sendMessage(msg2);
                break;
		}
	}
}
