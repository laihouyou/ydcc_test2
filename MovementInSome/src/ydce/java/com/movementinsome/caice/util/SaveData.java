package com.movementinsome.caice.util;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.okhttp.OkHttpRequest;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.database.vo.DynamicFormVO;
import com.movementinsome.database.vo.PicFileInfoVO;
import com.movementinsome.easyform.formengineer.XmlGuiForm;
import com.movementinsome.easyform.util.XmlFiledRuleOperater;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * 编辑数据操作类
 * Created by zzc on 2017/5/9.
 */

public class SaveData {

    /**
     * 编辑点、线数据  更新数据
     *
     */
    public static boolean EditPointDate(Context context, XmlGuiForm theForm, HashMap iparams
            , SavePointVo savePointVo,DynamicFormVO dynFormVo ,String dataType){
        try {

            ProjectVo projectVo =null;

            String dataJson=theForm.getJsonResults();

            /**
             * 修改本地数据库
             */

//            SavePointVo newSavePointVo= JSON.parseObject(dataJson,SavePointVo.class);      //修改过的信息
//
//            SavePointVo savePointVo= (SavePointVo) bundle.getSerializable(OkHttpParam.SAVEPOINTVO);   //原来的VO

            String contextStr=savePointVo.getContextStr();
            org.json.JSONObject jsonObject_contextStr=new org.json.JSONObject(contextStr);
            org.json.JSONObject formContext_json=new org.json.JSONObject(dataJson);

            //判断contextStr中的labelValueMap_formContex、FacPipBaseVo是不是json格式
            try {
                String labelValueMap_formContexStr=jsonObject_contextStr.getString(OkHttpParam.LABEL_VALUE_MAP_FORMCONTEX);
                JSONObject labelValueMap_formContexJson=new JSONObject(labelValueMap_formContexStr);
                jsonObject_contextStr.putOpt(OkHttpParam.LABEL_VALUE_MAP_FORMCONTEX,labelValueMap_formContexJson);
            }catch (JSONException e){
                e.printStackTrace();
            }
            try {
                String facPipBaseVoStr=jsonObject_contextStr.getString(OkHttpParam.FAC_PIP_BASE_VO);
                JSONObject facPipBaseVoJson=new JSONObject(facPipBaseVoStr);
                jsonObject_contextStr.put(OkHttpParam.FAC_PIP_BASE_VO,facPipBaseVoJson);
            }catch (JSONException e){
                e.printStackTrace();
            }

            //更新如下字段
            /**
             *   //设备名
             @DatabaseField
             private String implementorName;

             //口径
             @DatabaseField
             private String caliber;

             //类型
             @DatabaseField
             private String type;

             //设备点经度
             @DatabaseField
             private String longitude;

             //设备点纬度
             @DatabaseField
             private String latitude;

             //地面高程
             @DatabaseField
             private String groundElevation;

             //埋深
             @DatabaseField
             private String burialDepth;

             //发生地址
             @DatabaseField
             private String happenAddr;

             //行政区
             @DatabaseField
             private String administrativeRegion;

             //照片路径
             @DatabaseField
             private String camera;
             */

            String implementorName="";
            try {
                implementorName=formContext_json.getString(OkHttpParam.IMPLEMENTORNAME);
            }catch (Exception e){
                e.printStackTrace();
                implementorName=(String) iparams.get(OkHttpParam.IMPLEMENTORNAME);
            }

            String facType="";
            try {
                facType=formContext_json.getString(OkHttpParam.TYPE);
            }catch (Exception e){
                e.printStackTrace();
                facType=(String) iparams.get(OkHttpParam.TYPE);
            }

            //labelValueMap_formContex （中文字段）
            HashMap<String,String> labelValueMap_formMap= (HashMap<String, String>) iparams.get(OkHttpParam.LABEL_VALUE_MAP);
            JSONObject labelValueMapJson=new JSONObject(labelValueMap_formMap);
            jsonObject_contextStr.put(OkHttpParam.LABEL_VALUE_MAP_FORMCONTEX,labelValueMapJson);

            jsonObject_contextStr.put(OkHttpParam.FORM_CONTEXT,formContext_json);
            savePointVo.setContextStr(jsonObject_contextStr.toString());

            if (dataType.equals(MapMeterMoveScope.POINT)){
                String facName="";
                try {
                    facName=formContext_json.getString(OkHttpParam.FAC_NAME)==null
                            ?"":formContext_json.getString(OkHttpParam.FAC_NAME);
                }catch (Exception e){
                    e.printStackTrace();
                }
                savePointVo.setFacName(facName);
                savePointVo.setImplementorName(implementorName!=null?implementorName:"");
                savePointVo.setFacType(facType!=null?facType:"");       //现在暂时字段定位type
            }else if (dataType.equals(MapMeterMoveScope.LINE)){
                String pipName="";
                try {
                    pipName=formContext_json.getString(OkHttpParam.PIP_NAME)==null
                            ?"":formContext_json.getString(OkHttpParam.PIP_NAME);
                }catch (Exception e){
                    e.printStackTrace();
                }
                String pipMaterial="";
                try {
                    pipMaterial=formContext_json.getString(OkHttpParam.PIP_MATERIAL)==null
                            ?"":formContext_json.getString(OkHttpParam.PIP_MATERIAL);
                }catch (Exception e){
                    e.printStackTrace();
                }
                String pipType="";
                try {
                    pipType=formContext_json.getString(OkHttpParam.PIP_TYPE)==null
                            ?"":formContext_json.getString(OkHttpParam.PIP_TYPE);
                }catch (Exception e){
                    e.printStackTrace();
                }
                String layingType="";
                try {
                    layingType=formContext_json.getString(OkHttpParam.LAYING_TYPE)==null
                            ?"":formContext_json.getString(OkHttpParam.LAYING_TYPE);
                }catch (Exception e){
                    e.printStackTrace();
                }
                savePointVo.setPipName(pipName);
                savePointVo.setPipMaterial(pipMaterial);
                savePointVo.setPipType(pipType);
                savePointVo.setLayingType(layingType);
            }

            Dao<SavePointVo,Long> savePointVoLongDao= AppContext.getInstance().getAppDbHelper().getDao(SavePointVo.class);
            int update=savePointVoLongDao.update(savePointVo);
            if (update==1){
                //录入数据成功后更新时间
                Dao<ProjectVo,Long> miningSurveyVOLongDao=AppContext.getInstance().getAppDbHelper().getDao(ProjectVo.class);
                List<ProjectVo> projectVoList = miningSurveyVOLongDao.
                        queryForEq(OkHttpParam.PROJECT_ID,savePointVo.getProjectId());
                if (projectVoList !=null&& projectVoList.size()>0){
                    projectVo = projectVoList.get(0);

                }
                if (projectVo !=null){
                    projectVo.setProjectUpdatedDateStr(DateUtil.getNow());
                }
                int y=miningSurveyVOLongDao.update(projectVo);
                Log.i("s",y+"");

            }


            List<SavePointVo> savePointVoList=new ArrayList<>();
            savePointVoList.add(savePointVo);

            if (OkHttpRequest.IsUpdatePioSynchronize(savePointVoList,context)){
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

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 编辑普通线数据  更新数据
     *
     * @param dataJson  输入的数据集合
     */
    public static boolean EditLine(String dataJson,Bundle bundle,Context context){
        try {

            ProjectVo projectVo =null;

            /**
             * 修改本地数据库
             */

            Dao<SavePointVo,Long> saveLineVoLongDao= AppContext.getInstance()
                    .getAppDbHelper().getDao(SavePointVo.class);

//            SavePointVo savePointVo1= JSON.parseObject(dataJson,SavePointVo.class);      //修改过的信息

//            SavePointVo savePointVo= (SavePointVo) bundle.getSerializable("editLineVo");   //原来的VO

            List<String> ids= (List<String>) bundle.getSerializable("ids");     //原来的设施点ID集合
            if (ids!=null&&!ids.equals("")){
                SavePointVo savePointVo;
                List<SavePointVo> savePointVoList;
                for (int i=0;i<ids.size();i++){
                    String id=ids.get(i);
                    if (id!=null&&!id.equals("")){
                        savePointVoList=saveLineVoLongDao.queryForEq("id",id);
                        if (savePointVoList!=null&&savePointVoList.size()==1){
                            savePointVo=savePointVoList.get(0);

                            String contextStr=savePointVo.getContextStr();
                            org.json.JSONObject jsonObject_contextStr=new org.json.JSONObject(contextStr);
                            org.json.JSONObject formContext_json=new org.json.JSONObject(dataJson);
                            jsonObject_contextStr.put(OkHttpParam.FORM_CONTEXT,dataJson);

                            //更新如下字段
                            /**
                            //口径
                            @DatabaseField
                            private String caliber;
                            //所在道路
                            @DatabaseField
                            private String happenAddr;
                            //管材
                            @DatabaseField
                            private String tubularProduct;
                            //管线类型
                            @DatabaseField
                            private String pipType;
                            //敷设类型
                            @DatabaseField
                            private String layingType;
                            //行政区
                            @DatabaseField
                            private String administrativeRegion;
                            */

                            savePointVo.setContextStr(jsonObject_contextStr.toString());

                            savePointVo.setPipName(formContext_json.getString(OkHttpParam.PIP_NAME));
                            savePointVo.setPipMaterial(formContext_json.getString(OkHttpParam.PIP_MATERIAL));
                            savePointVo.setPipType(formContext_json.getString(OkHttpParam.PIP_TYPE));
                            savePointVo.setLayingType(formContext_json.getString(OkHttpParam.LAYING_TYPE));

                            int s=saveLineVoLongDao.update(savePointVo);
                            if (s==1){
                                //录入数据成功后更新时间
                                Dao<ProjectVo,Long> miningSurveyVOLongDao=AppContext.getInstance()
                                        .getAppDbHelper().getDao(ProjectVo.class);
                                List<ProjectVo> projectVoList = miningSurveyVOLongDao.
                                        queryForEq(OkHttpParam.PROJECT_ID,savePointVo.getProjectId());
                                if (projectVoList !=null&& projectVoList.size()>0){
                                    projectVo = projectVoList.get(0);

                                }
                                if (projectVo !=null){
                                    projectVo.setProjectUpdatedDateStr(DateUtil.getNow());
                                }
                                int y=miningSurveyVOLongDao.update(projectVo);
                                Log.i("s",y+"");

                                /**
                                 * 修改服务器
                                 */
                                List<SavePointVo> savePointVos=new ArrayList<>();
                                savePointVos.add(savePointVo);
                                OkHttpRequest.IsUpdatePio(savePointVos,context, projectVo);
                            }

                        }
                    }
                }
            }

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
