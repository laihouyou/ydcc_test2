package com.movementinsome.caice.okhttp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.caice.project.PoiOperation;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.vo.DetelePoiVo;
import com.movementinsome.caice.vo.LatlogVo2;
import com.movementinsome.caice.vo.MiningSurveyVO;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.map.MapViewer;
import com.pop.android.common.util.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by zzc on 2017/3/21.
 */

public class OkHttpRequest {

    private static final String TAG ="OkHttpRequest" ;
    private static int requestsNum = 0;
    private String fileStr;

    private static MapViewer mActivity;
    private  static BaiduMap mBaiduMap;
    private static List<Overlay> mOverlayList;

    private String idsStr;
    private static Gson gson;


    public OkHttpRequest(Activity activity, BaiduMap baiduMap,
                         List<Overlay> mOverlayList) {
        this.mBaiduMap = baiduMap;
        this.mActivity = (MapViewer)activity;
        this.mOverlayList=mOverlayList;
        gson=new Gson();
    }

    public static void IsUpdatePio(final List<SavePointVo> savePointVoList, final Context context,
                                   final MiningSurveyVO miningSurveyVO
    ){
//        final ProgressDialog[] progress = {null};
        String json=gson.toJson(savePointVoList);
        String url= OkHttpURL.serverUrl +"/"+ OkHttpURL.urlUpdatePoint;

        OkHttpUtils.postString()
                .tag(context)
                .url(url)
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
//                        progress[0] = new ProgressDialog(context);
//                        progress[0].setMessage("正在加载,请等待……");
//                        progress[0].setCancelable(false);
//                        progress[0].setCanceledOnTouchOutside(false);
//                        progress[0].show();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        ToastUtils.showToast(context,"提交数据失败");

                        try {
                            PoiOperation.setPointCompile(savePointVoList,"1");
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }

//                        if (progress[0].isShowing()){
//                            progress[0].dismiss();
//                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Dao<SavePointVo, Long> savePointVoLongDao = AppContext.getInstance()
                                    .getAppDbHelper().getDao(SavePointVo.class);
                            if (response != null && !response.equals("")) {
                                DetelePoiVo detelePoiVo = gson.fromJson(response, DetelePoiVo.class);
                                if (detelePoiVo != null && detelePoiVo.getStatus() == 0) {
                                    ToastUtils.showToast(context, "提交数据成功");


                                    String ids = detelePoiVo.getIds();
                                    if (ids != null && !ids.equals("")) {
                                        String[] idsData = ids.split(",");
                                        if (idsData.length > 0) {
                                            for (int i = 0; i < idsData.length; i++) {
                                                List<SavePointVo> savePointVos = savePointVoLongDao
                                                        .queryForEq("id", idsData[i]);
                                                if (savePointVos.size() == 1) {
                                                    PoiOperation.setPointCompile(savePointVos, "0");
                                                }
                                            }
                                        }
                                    }

                                } else {
                                    ToastUtils.showToast(context, "提交数据失败");
                                }
                            }
//                            if (progress[0].isShowing()) {
//                                progress[0].dismiss();
//                            }
                            showPoint(miningSurveyVO);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 通过工程ID获取工程marker数量
     */
    public static void getLatlogData(LatLng cenpt, final MiningSurveyVO miningSurveyVO, String acquisitionState) {
        if (mOverlayList!=null&&mOverlayList.size()>0){
            for (int i=0;i<mOverlayList.size();i++){
                mOverlayList.get(i).remove();
            }
        }

        String json=gson.toJson(miningSurveyVO);
        String url= OkHttpURL.serverUrl +"/"+ OkHttpURL.urlPointListview;


        OkHttpUtils.postString()
                .url(url)
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        showPoint(miningSurveyVO);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Dao<SavePointVo,Long> savePointVoLongDao=AppContext.getInstance().
                                    getAppDbHelper().getDao(SavePointVo.class);
                            List<String> poisVoIdList =new ArrayList<>();

//                            LatlogVo latlogVo = JSON.parseObject(response, LatlogVo.class);
                            LatlogVo2 latlogVo = JSON.parseObject(response, LatlogVo2.class);
                            if (latlogVo.getStatus() == 0) {

                                //将请求的数据更新到本地数据库中
                                if (latlogVo.getPois() != null && latlogVo.getPois().size() > 0) {
                                    for (int i = 0; i < latlogVo.getPois().size(); i++) {
                                        SavePointVo poisVo = latlogVo.getPois().get(i);
                                        //判断是不是当前用户的工程数据
                                        if (poisVo.getProjectId().equals
                                                (miningSurveyVO.getProjectId())) {
                                            List<SavePointVo> poisVos = savePointVoLongDao.
                                                    queryForEq("id", poisVo.getId());
                                            if (poisVos.size() == 0) {

                                                poisVo.setIsPresent("1");//已提交
                                                poisVo.setIsCompile("0");//未编辑

//                                                String contextJson = poisVo.getContext();
//                                                SavePointVo savePointVo = new SavePointVo();
//                                                if (contextJson != null && !contextJson.equals("")) {
//                                                    savePointVo = gson.fromJson(contextJson, SavePointVo.class);
//                                                }
//                                                savePointVo.setId(poisVo.getId());
//                                                savePointVo.setMarkerId(poisVo.getId());
//
//                                                savePointVo.setDataType(poisVo.getDataType());
//                                                savePointVo.setIsPresent("1");//已提交
//                                                savePointVo.setIsCompile("0");//未编辑
//                                                savePointVo.setContext(contextJson);
//                                                savePointVo.setProjectId(poisVo.getProjectId());
//                                                savePointVo.setProjectName(poisVo.getProjectName());
//                                                savePointVo.setLongitudeWg84(poisVo.getLongitudeWg84() + "");
//                                                savePointVo.setLatitudeWg84(poisVo.getLatitudeWg84() + "");
//                                                savePointVo.setDrawNum(poisVo.getDrawNum());
////                                                savePointVo.setUploadTime(poisVo.getCreateTimeStr());   //上传时间
//                                                savePointVo.setIsProjectShare(poisVo.getIsProjectShare());
//                                                savePointVo.setShareCode(poisVo.getShareCode());
//
//                                                savePointVo.setGatherType(poisVo.getGatherType());
//                                                savePointVo.setUserId(poisVo.getUsedId());
//                                                savePointVo.setUploadName(poisVo.getUploadName());
//                                                savePointVo.setGuid(poisVo.getGuid());
//                                                savePointVo.setDynamicFormVOId(poisVo.getDynamicFormVOId());
//

                                                int s = savePointVoLongDao.create(poisVo);
                                                if (s == 1) {

                                                }
                                            }
                                            //设置共享的数据
                                            else if (poisVos.size()==1){
                                                SavePointVo savePointVo=poisVos.get(0);
                                                savePointVo.setIsLeak(poisVo.getIsLeak());
                                                savePointVo.setCaliber_leak(poisVo.getCaliber_leak());
                                                savePointVo.setBurialDepth_leak(poisVo.getBurialDepth_leak());
                                                savePointVo.setCamera_leak(poisVo.getCamera_leak());
                                                savePointVo.setRemarks_leak(poisVo.getRemarks_leak());
                                                savePointVo.setUploadName_leak(poisVo.getUploadName_leak());
                                                savePointVo.setUploadTime_leak(poisVo.getUploadTime_leak());
                                                savePointVo.setUserId_leak(poisVo.getUserId_leak());
                                                savePointVo.setLongitudeBd09_leak(poisVo.getLongitudeBd09_leak());
                                                savePointVo.setLatitudeBd09_leak(poisVo.getLatitudeBd09_leak());
                                                savePointVo.setContext_leak(poisVo.getContext_leak());
                                                savePointVo.setLocationJson_leak(poisVo.getLocationJson_leak());
                                                savePointVo.setWater_leak(poisVo.getWater_leak());

                                                int s=savePointVoLongDao.update(savePointVo);
                                                if (s==1){

                                                }
                                            }
                                        } else if (poisVo.getProjectName() != null && poisVo.getShareCode() != null) {
                                            if (poisVo.getProjectName().equals(miningSurveyVO.getProjectName())
                                                    && poisVo.getShareCode().equals(miningSurveyVO.getShareCode())) {
                                                //将共享数据(不包括本工程数据)添加至集合
                                                poisVoIdList.add(poisVo.getId());

                                                List<SavePointVo> poisVos = savePointVoLongDao.
                                                        queryForEq("id", poisVo.getId());
                                                //共享工程如果本地没有，则入库
                                                if (poisVos.size() == 0) {

                                                    poisVo.setIsPresent("1");//已提交
                                                    poisVo.setIsCompile("0");//未编辑
                                                    int s = savePointVoLongDao.create(poisVo);
                                                    if (s == 1) {

                                                    }
                                                }
                                                //共享工程如果本地有，则更新
                                                else if (poisVos.size() == 1) {

                                                    poisVo.setIsPresent("1");//已提交
                                                    poisVo.setIsCompile("0");//未编辑

                                                    int s = savePointVoLongDao.update(poisVo);
                                                    if (s == 1) {

                                                    }
                                                }

                                            }
                                        }
                                    }

                                    List<SavePointVo> savePointVoIdList=new ArrayList<>();
                                    if (miningSurveyVO.getIsProjectShare()!=null
                                            &&miningSurveyVO.getIsProjectShare().equals("0")){
                                        //获取手机端共享数据非本工程数据id
                                        Map<String,Object> map=new HashMap<>();
                                        map.put(OkHttpParam.IS_PROJECT_SHARE,miningSurveyVO.getIsProjectShare());
                                        map.put(OkHttpParam.SHARE_CODE,miningSurveyVO.getShareCode());
                                        List<SavePointVo> savePointVoList=savePointVoLongDao.queryForFieldValues(map);
                                        if (savePointVoList.size()>0){
                                            for(SavePointVo savePointVo:savePointVoList){
                                                String asveId=savePointVo.getProjectId();
                                                String miningId=miningSurveyVO.getProjectId();
                                                if (!asveId.equals(miningId)){
                                                    savePointVoIdList.add(savePointVo);
                                                }
                                            }
                                        }
                                    }
                                    if (poisVoIdList.size()==0){
                                        if (savePointVoIdList.size()>0){
                                            for (SavePointVo savePointVo:savePointVoIdList){
                                                int s=savePointVoLongDao.delete(savePointVo);
                                                Log.i("tag",s+"");
                                            }
                                        }
                                    }else if (poisVoIdList.size()>0){
                                        if (savePointVoIdList.size()>0){
                                            for (SavePointVo savePointVo:savePointVoIdList){
                                                if (!poisVoIdList.contains(savePointVo.getId())){
                                                    int s=savePointVoLongDao.delete(savePointVo);
                                                    Log.i("tag",s+"");
                                                }
                                            }
                                        }
                                    }
                                }

                                showPoint(miningSurveyVO);


                            } else {
                                Toast.makeText(mActivity, "下载点位数据失败，请重新上传数据", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
    }

    public static void showPoint(MiningSurveyVO miningSurveyVO) {
        try {
            Dao<SavePointVo,Long> savePointVoLongDao= AppContext.getInstance().
                    getAppDbHelper().getDao(SavePointVo.class);
            //更新成功后查询本地设施数据库
            Map<String,Object> map=new HashMap<>();
//                                map.put("uploadName",AppContext.getInstance().getCurUserName());
            if (miningSurveyVO.getIsProjectShare().equals("0")){    //共享
                map.put(OkHttpParam.PROJECT_NAME,miningSurveyVO.getProjectName());
                map.put(OkHttpParam.SHARE_CODE,miningSurveyVO.getShareCode());
            }else if (miningSurveyVO.getIsProjectShare().equals("1")){     //不共享
                map.put(OkHttpParam.PROJECT_ID,miningSurveyVO.getProjectId());
            }
            List<SavePointVo> poisVoList=savePointVoLongDao.queryForFieldValues(map);
            if (poisVoList!=null){
                //请求成功后移除Mark点
                mActivity.showMaker(poisVoList,miningSurveyVO);
            }
        }catch (Exception e1){
            e1.printStackTrace();
        }
    }

    /**
     * 删除百度云的点以及本地文件
     */
    public static void DeleteMarkerPoint(final LatLng cenLat ,
                                         final String ids,
                                         final MiningSurveyVO miningSurveyVO, final String acquisitionState) {
        Map parameterMap = new HashMap();
        parameterMap.put(OkHttpParam.IDS, ids);

        String json=gson.toJson(parameterMap);
        String url= OkHttpURL.serverUrl +"/"+ OkHttpURL.urlDeletePoint;


        OkHttpUtils.postString()
                .url(url)
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        DetelePoiVo vo= JSON.parseObject(response,DetelePoiVo.class);
                            if (vo!=null&&vo.getStatus()==0){
                                try {
                                    PoiOperation.DeletePoi(
                                            vo.getIds()
                                    );
                                    mBaiduMap.hideInfoWindow();
                                    getLatlogData(cenLat,miningSurveyVO,acquisitionState);
                                    mActivity.moveProjcetUpdate();
                                    Toast.makeText(mActivity, "删除成功", Toast.LENGTH_SHORT).show();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                Toast.makeText(mActivity, "删除失败", Toast.LENGTH_SHORT).show();
                            }

                        }
                });
    }

    /**
     * 删除百度云的点以及本地文件
     */
    public static String DeleteMarkerPointSync(String ids, final String dateBaseIds
    ) throws IOException, SQLException {
        if (gson == null) {
            gson = new Gson();
        }
        Map parameterMap = new HashMap();
        parameterMap.put(OkHttpParam.IDS, ids);

        String json=gson.toJson(parameterMap);
        String url= OkHttpURL.serverUrl +"/"+ OkHttpURL.urlDeletePoint;


        Response re = OkHttpUtils.postString()
                .url(url)
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute();
        try {

            if (re.code() == 200) {
                String response = re.body().string();
                DetelePoiVo vo = JSON.parseObject(response, DetelePoiVo.class);
                if (vo != null && vo.getStatus() == 0) {    //成功
                    if (dateBaseIds != null && !dateBaseIds.equals("")) {
                        PoiOperation.DeletePoi(
                                vo.getIds() + "," + dateBaseIds
                        );
                    } else {
                        PoiOperation.DeletePoi(
                                vo.getIds()
                        );
                    }
                    return "0";
                } else if (vo != null && vo.getStatus() == -2) {  //服务器记录不存在
                    if (dateBaseIds != null && !dateBaseIds.equals("")) {
                        PoiOperation.DeletePoi(
                                vo.getIds() + "," + dateBaseIds
                        );
                    } else {
                        PoiOperation.DeletePoi(
                                vo.getIds()
                        );
                    }
                }
                return "1";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "1";
        }
    return "1";

    }


    /**
     * 提交数据 单个VO
     * BS服务器
     */
    public static boolean SubmitLatlngBS(final String drawNum,
                                      final List<SavePointVo> savePointVos, final boolean isEnd
            , final String contextJson, String ids
    ) throws SQLException {

        final SavePointVo savePointVo=savePointVos.get(0);

        savePointVo.setUploadName( AppContext.getInstance().getCurUserName());//  上传人
        savePointVo.setUsedId( AppContext.getInstance().getCurUser().getUserId());//  上传人id
        savePointVo.setDrawNum(drawNum);
        savePointVo.setContext(contextJson);

        String json=gson.toJson(savePointVos);
        String url= OkHttpURL.serverUrl +"/"+ OkHttpURL.urlGeodata;

        if (savePointVo.getDataType().equals(MapMeterMoveScope.POINT)) {   //采点


            try {
                Response res = OkHttpUtils.postString()
                        .content(json)
                        .url(url)
                        .mediaType(MediaType.parse("application/json; charset=utf-8"))
                        .build().execute();

                if (res.code() == 200) {
                    String response = res.body().string();
                    DetelePoiVo detelePoiVo = JSON.parseObject(response, DetelePoiVo.class);
                    if (detelePoiVo.getStatus() == 0) {

                        Map<String, Object> map = new HashMap<>();
                        map.put(OkHttpParam.ID, detelePoiVo.getIds());
                        map.put(OkHttpParam.CONTEXT_JSON, contextJson);
                        map.put(OkHttpParam.IS_END, isEnd);
                        map.put(OkHttpParam.IS_PRESENT, "1");
                        map.put(OkHttpParam.IS_COMPILE, "0");
                        PoiOperation.PoiCreate(savePointVo, map);

                        return true;

                    } else {
                        Toast.makeText(mActivity, "提交数据失败", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    Map<String, Object> map = new HashMap<>();
                    map.put(OkHttpParam.ID, null);
                    map.put(OkHttpParam.CONTEXT_JSON, contextJson);
                    map.put(OkHttpParam.IS_END, isEnd);
                    map.put(OkHttpParam.IS_PRESENT, "0");
                    map.put(OkHttpParam.IS_COMPILE, "0");

                    PoiOperation.PoiCreate(savePointVo, map);

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                return false;
            }

        } else if (savePointVo.getDataType().equals(MapMeterMoveScope.LINE)) {
            try {
//                String resp= SpringUtil.postData(AppContext.getInstance().getServerUrl()+"/"+OkHttpURL.urlGeodata,json);
                String resp= SpringUtil.postData(OkHttpURL.serverUrl +"/"+ OkHttpURL.urlGeodata,json);
                Log.e(TAG, "SubmitLatlngBS: "+resp );

                if (resp!=null) {        //请求成功
//                    String response = re.body().string();
                    DetelePoiVo isUpdateVo = JSON.parseObject(resp, DetelePoiVo.class);
                    if (isUpdateVo!=null&&isUpdateVo.getStatus() == 0) {

                        if (isEnd){
                            ids+=isUpdateVo.getIds();
                        }else {
                            ids+=isUpdateVo.getIds()+",";
                        }

                        Map<String,Object> map=new HashMap<>();
                        map.put(OkHttpParam.ID,isUpdateVo.getIds());
                        map.put(OkHttpParam.IDS,ids);
                        map.put(OkHttpParam.CONTEXT_JSON,contextJson);
                        map.put(OkHttpParam.IS_END,isEnd);
                        map.put(OkHttpParam.IS_PRESENT,"1");
                        map.put(OkHttpParam.IS_COMPILE,"0");
                        PoiOperation.PoiCreate(savePointVo,map);

                        return true;
                    } else {
                        return false;
                    }
                } else {
                    try {

                        Map<String,Object> map=new HashMap<>();
                        map.put(OkHttpParam.ID,null);
                        map.put(OkHttpParam.CONTEXT_JSON,contextJson);
                        map.put(OkHttpParam.IS_END,isEnd);
                        map.put(OkHttpParam.IS_PRESENT,"1");
                        map.put(OkHttpParam.IS_COMPILE,"0");
                        PoiOperation.PoiCreate(savePointVo,map);

                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Map<String,Object> map=new HashMap<>();
                    map.put(OkHttpParam.ID,null);
                    map.put(OkHttpParam.CONTEXT_JSON,contextJson);
                    map.put(OkHttpParam.IS_END,isEnd);
                    map.put(OkHttpParam.IS_PRESENT,"0");
                    map.put(OkHttpParam.IS_COMPILE,"0");
                    PoiOperation.PoiCreate(savePointVo,map);

                    return false;
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 提交数据  VO集合   (单纯的提交管线集合)
     * BS服务器
     */
    public static boolean SubmitPointListCreate(final List<SavePointVo> savePointVoList, final Context context
    ) throws SQLException {

        final ProgressDialog[] progress = {null};

        String json = gson.toJson(savePointVoList);
        String url= OkHttpURL.serverUrl +"/"+ OkHttpURL.urlGeodata;

        Response re= null;
        try {
            re = OkHttpUtils.postString()
                    .url(url)
                    .content(json)
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build()
                    .execute();

            if (re.code() == 200) {        //请求成功
                String response = re.body().string();
                DetelePoiVo detelePoiVo = JSON.parseObject(response, DetelePoiVo.class);
                if (detelePoiVo != null && detelePoiVo.getStatus() == 0) {

                    //说明整条管线所有点都提交成功
                    for (int i = 0; i < savePointVoList.size(); i++) {
                        Map<String, Object> map = new HashMap<>();
                        map.put(OkHttpParam.IS_PRESENT, "1");    ////提交成功
                        map.put(OkHttpParam.IS_COMPILE, "0");
                        map.put(OkHttpParam.IDS, detelePoiVo.getIds());
                        if (i==savePointVoList.size()-1){
                            map.put(OkHttpParam.IS_END,true);
                        }else {
                            map.put(OkHttpParam.IS_END,false);
                        }
                        PoiOperation.PoiCreate(savePointVoList.get(i), map);
                    }

                    return true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();

            //说明整条管线所有点都提交失败
            for (int i = 0; i < savePointVoList.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put(OkHttpParam.IS_PRESENT, "0");    //提交不成功
                map.put(OkHttpParam.IS_COMPILE, "0");
                try {
                    PoiOperation.PoiCreate(savePointVoList.get(i), map);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            return  false;
        }
        return true;
    }


    /**
     * 提交数据  VO集合
     * BS服务器
     */
    public static void SubmitPointList(final List<SavePointVo> savePointVoList, final Context context
    ) throws SQLException {

        final ProgressDialog[] progress = {null};

        String json = gson.toJson(savePointVoList);
        String url= OkHttpURL.serverUrl +"/"+ OkHttpURL.urlGeodata;

        OkHttpUtils.postString()
                .url(url)
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        progress[0] = new ProgressDialog(context);
                        progress[0].setMessage("正在同步数据,请等待……");
                        progress[0].setCancelable(false);
                        progress[0].setCanceledOnTouchOutside(false);
                        progress[0].show();

                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        Toast.makeText(context,
                                "上传点位数据失败,请检查网络设置", Toast.LENGTH_SHORT).show();
                        if (progress[0].isShowing()) {
                            progress[0].dismiss();
                        }
                        try {
                            PoiOperation.setPointPresent(savePointVoList,"0");
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            DetelePoiVo detelePoiVo = JSON.parseObject(response, DetelePoiVo.class);
                            if (detelePoiVo.getStatus() == 0) {
                                Toast.makeText(context, "提交数据成功", Toast.LENGTH_SHORT).show();
                                PoiOperation.setPointPresent(savePointVoList,"1");
                            } else {
                                Toast.makeText(context, "提交数据失败", Toast.LENGTH_SHORT).show();
                            }
                            if (progress[0].isShowing()) {
                                progress[0].dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    /**
     * 图片下载判断类
     * @param context
     * @param savePointVoList
     * @param miningSurveyVO
     */
    public static void Picture(Context context,List<SavePointVo> savePointVoList,MiningSurveyVO miningSurveyVO){
        //下载图片
        if (savePointVoList!=null&&savePointVoList.size()>0){
            for (int i = 0; i < savePointVoList.size(); i++) {
                SavePointVo savePointVo=savePointVoList.get(i);
                //是否是本工程数据
                if (miningSurveyVO.getProjectId().equals(savePointVo.getProjectId())){
                    CameraSub(context, savePointVo);
                }else if (savePointVo.getProjectName() != null && savePointVo.getShareCode() != null){
                    //判断工程是否是共享工程
                    if (savePointVo.getProjectName().equals(miningSurveyVO.getProjectName())
                            && savePointVo.getShareCode().equals(miningSurveyVO.getShareCode())){
                        CameraSub(context, savePointVo);
                    }
                }
            }
        }
    }

    private static void CameraSub(Context context, SavePointVo savePointVo) {
        String cameraPathStr=savePointVo.getCamera();
        CamerDrow(context, savePointVo, cameraPathStr);
        CamerDrow(context, savePointVo, savePointVo.getCamera_leak());
    }

    private static void CamerDrow(Context context, SavePointVo savePointVo, String cameraPathStr) {
        if (cameraPathStr!=null&&!cameraPathStr.equals("")){
            String [] cameraPathArr=cameraPathStr.split(",");
            if (cameraPathArr!=null&&cameraPathArr.length>0){
                for (int i1 = 0; i1 < cameraPathArr.length; i1++) {
                    String cameraPath=cameraPathArr[i1];
                    File cameraFile=new File(cameraPath);
                    if (!cameraFile.exists()){
                        String [] cameraNameArr=cameraPath.split("/");
                        if (cameraNameArr!=null&&cameraNameArr.length>0){
                            String cameraName=cameraNameArr[cameraNameArr.length-1];
                            if (cameraName!=null&&!cameraName.equals("")){
                                OkHttpRequest.DownloadPicture(context,cameraName,savePointVo.getGuid());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 图片下载类
     * @param context
     * @param camearName
     * @param guid
     */
    public static void DownloadPicture(final Context context, String camearName, String guid){
        final ProgressDialog[] progress = {null};

        String url= OkHttpURL.fileServer+SpringUtil._REST_DOWNLOADFILESIMPLE;
        Map<String,String> map=new HashMap<>();
        map.put("guid",guid);
        map.put("picName",camearName);

        String json=gson.toJson(map);

        OkHttpUtils
                .postString()
                .tag(mActivity)
                .url(url)
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new FileCallBack(AppContext.getInstance().getAppStoreMedioPath2(),camearName) {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        progress[0] = new ProgressDialog(context);
                        progress[0].setMessage("正在下载图片数据,请等待……");
                        progress[0].setCancelable(false);
                        progress[0].setCanceledOnTouchOutside(false);
                        progress[0].show();

                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        if (progress[0].isShowing()){
                            progress[0].dismiss();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Log.i("tag",response.getParent());
                    }
                });
    }

    /**
     * 提交数据  断网重提
     */
//    public static void SubmitLatlngBrokenNetwork(final String adderss, final String tableNum
//            , final String caliber, final String lng, final String lat, final LatLng cenpt
//            , final String drawPyte, final String drawNum, final String implementorName, final String projectId
//            , final String shareCode, final String isProjectShare, final String projectName, final String savePointVoId
//            , final HistoryCommitVO historyCommitVOde,Context context
//                                    ) throws SQLException {
//        final Dao<HistoryCommitVO,Long> historyCommitVOLongDao=AppContext.getInstance().getAppDbHelper().getDao(HistoryCommitVO.class);
//
//        Map parameterMap = new HashMap();
//        parameterMap.put(OkHttpParam.TITLE,AppContext.getInstance().getCurUserName());// title设置 上传人
//        parameterMap.put(OkHttpParam.GROUP_NAME, AppContext.getInstance().getCurUser().getGroupName());// 设置所属公司班组
//        if (projectId!=null){
//            parameterMap.put(OkHttpParam.PROJECT_ID,projectId);     //设置工程ID
//            parameterMap.put(OkHttpParam.PROJECT_NAME,projectName);     //设置名字
//        }
//        parameterMap.put(OkHttpParam.IS_PROJECT_SHARE,isProjectShare);     //设置是否共享
//        parameterMap.put(OkHttpParam.SHARE_CODE,shareCode);     //设置共享码
//        parameterMap.put(OkHttpParam.SAVE_POINTVO_ID,savePointVoId);     //手机本地数据库设施点ID
//
//        parameterMap.put(OkHttpParam.ADDRESS, adderss);
//        parameterMap.put(OkHttpParam.LATITUDE, lat);
//        parameterMap.put(OkHttpParam.LONGITUDE, lng);
//        parameterMap.put(OkHttpParam.COORD_TYPE, OkHttpParam.COORD_TYPE_VALUE);
//        parameterMap.put(OkHttpParam.GEOTABLE_ID, OkHttpParam.GEOIAVLE_VALUE);
//        parameterMap.put(OkHttpParam.AK, OkHttpParam.AK_VALUE);
//        parameterMap.put(OkHttpParam.FAC_NAME, tableNum);//设施编号
//        if (drawPyte.equals(MapMeterMoveScope.LINE)){
//            parameterMap.put(OkHttpParam.DATA_TYPE,MapMeterMoveScope.LINE);
//            parameterMap.put(OkHttpParam.DRAW_NUM,drawNum);
//        }
//
//        //还需要上传工程ID、工程是否共享和共享码信息
//            try {
//                Response re=
//                        post()
//                        .url(OkHttpURL.urlGeodata)
//                        .params(parameterMap)
//                        .build()
//                        .execute();
//
//                if (re.code()==200){        //请求成功
//                    String response=re.body().string();
//                    IsUpdateVo isUpdateVo = JSON.parseObject(response, IsUpdateVo.class);
//                    if (isUpdateVo.getStatus() == 0) {
//                        try {
//                            Dao<SavePointVo,Long> savePointVoLongDao=
//                                    AppContext.getInstance().getAppDbHelper().getDao(SavePointVo.class);
//                            SavePointVo savePointVo=savePointVoLongDao.queryForId(Long.parseLong(historyCommitVOde.getSavePointVoId()));
//                            //将LBS云返回的ID保存到本地数据库中
//                            if (savePointVo!=null){
//                                savePointVo.setMarkerId(isUpdateVo.getId()+"");
//                                int s=savePointVoLongDao.update(savePointVo);
//                                if (s==1){
//
//                                }
//                            }
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
//
////                        getLatlogData(cenpt,"");
//                        if (historyCommitVOde!=null){
//                            try {
//                                int s=historyCommitVOLongDao.delete(historyCommitVOde);
//                                if (s==1){
//
//                                }
//                            } catch (SQLException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    } else {
//                        Toast.makeText(mActivity, "提交数据失败", Toast.LENGTH_SHORT).show();
//                    }
//                }else {
//                    try {
//                        historyCommitVOde.setUploadTime(DateUtil.currDay());
//                        int s=historyCommitVOLongDao.update(historyCommitVOde);
//                        if (s==1){
//
//                        }
//                    } catch (SQLException e1) {
//                        e1.printStackTrace();
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//    }

    public List<Overlay> getmOverlayList() {
        return mOverlayList;
    }

    public void setmOverlayList(List<Overlay> mOverlayList) {
        this.mOverlayList = mOverlayList;
    }

}
