package com.movementinsome.caice.save;

import android.content.Context;

import com.google.gson.Gson;
import com.movementinsome.AppContext;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.util.CreateFiles;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.vo.FacPipBaseVo;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.database.vo.DynamicFormVO;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

/**文件保存类
 *
 * Created by zzc on 2017/5/10.
 */

public class SaveFile {
    private File path;
    private CreateFiles createFiles;
    private Context mContext;

    public SaveFile(Context context){
        this.mContext=context;

        if (createFiles==null){
            createFiles=new CreateFiles();
        }
    }

    public  void SavePointDatabase(SavePointVo savePointVo,HashMap iparams,DynamicFormVO dynFormVo,
                                   String formContext) {
        try {

            savePointVo.setFacId(UUID.randomUUID().toString());   //用当前毫秒值做ID
            savePointVo.setGuid(dynFormVo.getGuid());       //guid  关联附件唯一ID
            savePointVo.setUserId( AppContext.getInstance().getCurUser().getUserId());//  上传人id

            if (iparams != null) {
                org.json.JSONObject contextStr_json=new org.json.JSONObject();
                org.json.JSONObject formContext_json=new org.json.JSONObject(formContext);
                String facType="";
                try {
                    facType=formContext_json.getString(OkHttpParam.TYPE);
                }catch (Exception e){
                    e.printStackTrace();
                    facType="";
                }
                savePointVo.setFacType(facType);

                Gson gson=new Gson();
                String projectType=(String) iparams.get(OkHttpParam.PROJECT_TYPE);
                HashMap<String,String> labelValueMap= (HashMap<String, String>) iparams.get(OkHttpParam.LABEL_VALUE_MAP);
                String label_form_context=gson.toJson(labelValueMap);
                JSONObject label_form_contextJson=new JSONObject(label_form_context);

                FacPipBaseVo facPipBaseVo=new FacPipBaseVo(projectType);
                String facpipVoStr=gson.toJson(facPipBaseVo);
                JSONObject facpipVoStrJson=new JSONObject(facpipVoStr);

                contextStr_json.put(OkHttpParam.FORM_CONTEXT,formContext_json);
                contextStr_json.put(OkHttpParam.LABEL_VALUE_MAP_FORMCONTEX,label_form_contextJson);
                contextStr_json.put(OkHttpParam.FAC_PIP_BASE_VO, facpipVoStrJson);
                contextStr_json.put(OkHttpParam.IS_SUCCESSION,iparams.get(OkHttpParam.IS_SUCCESSION));
                contextStr_json.put(OkHttpParam.MAP_X,iparams.get(OkHttpParam.MAP_X));
                contextStr_json.put(OkHttpParam.MAP_Y,iparams.get(OkHttpParam.MAP_Y));
                contextStr_json.put(OkHttpParam.LOCATION_JSON,iparams.get(OkHttpParam.LOCATION_JSON));
                contextStr_json.put(OkHttpParam.UPDATE_SOURCE,OkHttpParam.MOBILE);
                contextStr_json.put(OkHttpParam.FORM_NAME,dynFormVo.getForm());

                savePointVo.setContextStr(contextStr_json.toString());

                String facName="";
                String implementorName="";
                try {
                    facName=formContext_json.getString(OkHttpParam.FAC_NAME);
                }catch (Exception e){
                    e.printStackTrace();
                    facName=(String) iparams.get(OkHttpParam.FAC_NAME);
                }
                try {
                    implementorName=formContext_json.getString(OkHttpParam.IMPLEMENTORNAME);
                }catch (Exception e){
                    e.printStackTrace();
                    implementorName=(String) iparams.get(OkHttpParam.IMPLEMENTORNAME);
                }
                savePointVo.setFacName(facName);
                savePointVo.setImplementorName(implementorName);

                savePointVo.setProjectId((String) iparams.get(OkHttpParam.PROJECT_ID));
                savePointVo.setShareCode((String) iparams.get(OkHttpParam.SHARE_CODE));
                savePointVo.setGatherType((String) iparams.get(OkHttpParam.GATHER_TYPE));   //采集类型
                savePointVo.setDataType(MapMeterMoveScope.POINT);       //设置数据类型

                savePointVo.setLongitude((String) iparams.get(OkHttpParam.LONGITUDE));
                savePointVo.setLatitude((String) iparams.get(OkHttpParam.LATITUDE));
                savePointVo.setLongitudeWg84((String) iparams.get(OkHttpParam.LONGITUDE_WG84));
                savePointVo.setLatitudeWg84((String) iparams.get(OkHttpParam.LATITUDE_WG84));

                //设置管线字段为空字符串
                savePointVo.setPipType("");
                savePointVo.setPipName("");
                savePointVo.setPipMaterial("");
                savePointVo.setLayingType("");
                savePointVo.setPointList("");

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SaveLineDatabase(SavePointVo saveLineVo,HashMap iparams,DynamicFormVO dynFormVo,
                                 String dataJson){
        try {

            saveLineVo.setDataType(MapMeterMoveScope.LINE);     //管线
            saveLineVo.setFacId(UUID.randomUUID().toString());
            saveLineVo.setUserId(AppContext.getInstance().getCurUser().getUserId());   //上传人id
            saveLineVo.setGuid(dynFormVo.getGuid());

            org.json.JSONObject dataJsonObject=new org.json.JSONObject(dataJson);
            saveLineVo.setPipName(dataJsonObject.getString(OkHttpParam.PIP_NAME));
            saveLineVo.setPipType((String) dataJsonObject.get(OkHttpParam.PIP_TYPE));
//            saveLineVo.setPipMaterial((String) dataJsonObject.get(OkHttpParam.PIP_MATERIAL));
            saveLineVo.setPipMaterial((String) dataJsonObject.get(OkHttpParam.TUBULAR_PRODUCT));
            saveLineVo.setLayingType((String) dataJsonObject.get(OkHttpParam.LAYING_TYPE));


            if (iparams != null) {
                org.json.JSONObject contextStr_json=new org.json.JSONObject();

                Gson gson=new Gson();
                HashMap<String,String> labelValueMap= (HashMap<String, String>) iparams.get(OkHttpParam.LABEL_VALUE_MAP);
                String label_form_context=gson.toJson(labelValueMap);
                JSONObject label_form_contextJson=new JSONObject(label_form_context);

                String projectType=(String) iparams.get(OkHttpParam.PROJECT_TYPE);
                FacPipBaseVo facPipBaseVo=new FacPipBaseVo(projectType);
                String facpipVoJson=gson.toJson(facPipBaseVo);
                JSONObject facpipVoStrJson=new JSONObject(facpipVoJson);

                contextStr_json.put(OkHttpParam.FORM_CONTEXT,dataJsonObject);
                contextStr_json.put(OkHttpParam.LABEL_VALUE_MAP_FORMCONTEX,label_form_contextJson);
                contextStr_json.put(OkHttpParam.FAC_PIP_BASE_VO,facpipVoStrJson);
                contextStr_json.put(OkHttpParam.IS_SUCCESSION,iparams.get(OkHttpParam.IS_SUCCESSION));
                contextStr_json.put(OkHttpParam.MAP_X,iparams.get(OkHttpParam.MAP_X));
                contextStr_json.put(OkHttpParam.MAP_Y,iparams.get(OkHttpParam.MAP_Y));
                contextStr_json.put(OkHttpParam.LOCATION_JSON,iparams.get(OkHttpParam.LOCATION_JSON));
                contextStr_json.put(OkHttpParam.UPDATE_SOURCE,OkHttpParam.MOBILE);

                contextStr_json.put(OkHttpParam.PIPELINE_LINGHT,iparams.get(OkHttpParam.PIPELINE_LINGHT));
                contextStr_json.put(OkHttpParam.CALIBER,iparams.get(OkHttpParam.CALIBER));
                contextStr_json.put(OkHttpParam.HAPPEN_ADDR,iparams.get(OkHttpParam.HAPPEN_ADDR));
                contextStr_json.put(OkHttpParam.ADMINISTRATIVE_REGION,iparams.get(OkHttpParam.ADMINISTRATIVE_REGION));
                contextStr_json.put(OkHttpParam.FAC_NAME_LIST,iparams.get(OkHttpParam.FAC_NAME_LIST));

                saveLineVo.setContextStr(contextStr_json.toString());

                saveLineVo.setProjectId((String) iparams.get(OkHttpParam.PROJECT_ID));
                saveLineVo.setShareCode((String) iparams.get(OkHttpParam.SHARE_CODE));
                saveLineVo.setGatherType((String) iparams.get(OkHttpParam.GATHER_TYPE));
                saveLineVo.setPointList((String)iparams.get(OkHttpParam.POINT_LIST));   //坐标集合
                saveLineVo.setLongitudeWg84((String)iparams.get(OkHttpParam.LONGITUDE_WG84));
                saveLineVo.setLatitudeWg84((String)iparams.get(OkHttpParam.LATITUDE_WG84));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
