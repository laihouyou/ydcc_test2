package com.movementinsome.caice.okhttp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.caice.project.PoiOperation;
import com.movementinsome.caice.util.DateUtil;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.vo.DetelePoiVo;
import com.movementinsome.caice.vo.HistoryCommitVO;
import com.movementinsome.caice.vo.IsUpdateVo;
import com.movementinsome.caice.vo.LatlogVo2;
import com.movementinsome.caice.vo.PointUpdateVo;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.map.MapViewer;
import com.movementinsome.map.nearby.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.zhy.http.okhttp.OkHttpUtils.post;

/**联网请求类
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
                                   final ProjectVo projectVo
    ){
        String serverUrl=OkHttpURL.serverUrl;
        if (serverUrl==null){
            ToastUtils.show(AppContext.getInstance().getString(R.string.error_message2));
            return;
        }
        OkHttpUtils.postString()
                .tag(context)
//                .url(AppContext.getInstance().getServerUrl()+"/"+OkHttpURL.urlUpdatePoint)
                .url(serverUrl +"/"+OkHttpURL.urlUpdatePoint)
                .content(gson.toJson(savePointVoList))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringDialogCallback((Activity) context) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        ToastUtils.show("提交数据失败");

                        try {
                            PoiOperation.setPointCompile(savePointVoList);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Dao<SavePointVo, Long> savePointVoLongDao = AppContext.getInstance()
                                    .getAppDbHelper().getDao(SavePointVo.class);
                            if (response != null && !response.equals("")) {
                                PointUpdateVo pointUpdateVo=gson.fromJson(response, PointUpdateVo.class);
                                if (pointUpdateVo != null && pointUpdateVo.getCode() == 0) {
                                    ToastUtils.show( "提交数据成功");


                                    if (savePointVoList.size()>0){
                                        PoiOperation.setPointCompile(savePointVoList);
                                    }

                                } else {
                                    ToastUtils.show("提交数据失败");
                                }
                            }
//                            mActivity.showMaker(projectVo,false);
                            EventBus.getDefault().post(OkHttpParam.SHOW_MARKER);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public static boolean IsUpdatePioSynchronize(final List<SavePointVo> savePointVoList, final Context context
    ) throws IOException, SQLException {
        String serverUrl = OkHttpURL.serverUrl;
        if (serverUrl == null) {
            ToastUtils.show(AppContext.getInstance().getString(R.string.error_message2));
            return false;
        }
        Response res = OkHttpUtils.postString()
                .tag(context)
//                .url(AppContext.getInstance().getServerUrl()+"/"+OkHttpURL.urlUpdatePoint)
                .url(serverUrl + "/" + OkHttpURL.urlUpdatePoint)
                .content(gson.toJson(savePointVoList))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute();

        if (res.code() == 200) {
            String response = res.body().string();
            if (!response.equals("")) {
                PointUpdateVo pointUpdateVo = gson.fromJson(response, PointUpdateVo.class);
                if (pointUpdateVo != null && pointUpdateVo.getCode() == 0) {

                    if (savePointVoList.size() > 0) {
                        PoiOperation.setPointCompile(savePointVoList);
                        return true;
                    }

                }
            }
//            mActivity.showMaker(projectVo,false);
            EventBus.getDefault().post(OkHttpParam.SHOW_MARKER);
        } else {
            PoiOperation.setPointCompile(savePointVoList);
        }
        return false;
    }


    /**
     * 通过工程ID获取工程marker数量
     */
    public static void getLatlogData(final ProjectVo projectVo, final Activity activity) {
        try {

            if (mOverlayList != null && mOverlayList.size() > 0) {
                for (int i = 0; i < mOverlayList.size(); i++) {
                    mOverlayList.get(i).remove();
                }
            }

            final Dao<SavePointVo, Long> savePointVoLongDao = AppContext.getInstance().
                    getSavePointVoDao();
            List<SavePointVo> save1 = savePointVoLongDao.queryForEq(OkHttpParam.PROJECT_ID, projectVo.getProjectId());
            //下载图片异步
            OkHttpRequest.Picture(activity, save1, projectVo);

            String serverUrl = OkHttpURL.serverUrl;
            if (serverUrl == null) {
                ToastUtils.show(AppContext.getInstance().getString(R.string.error_message2));
//                mActivity.showMaker(projectVo,false);
                EventBus.getDefault().post(OkHttpParam.SHOW_MARKER);
                return;
            }

            final ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(activity.getString(R.string.load_msg));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();

            String url = serverUrl + OkHttpURL.urlPointListview;
            String jsonStr = gson.toJson(projectVo);

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .readTimeout(300000L, TimeUnit.MILLISECONDS)
                    .build();

            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");//"类型,字节码"

            RequestBody requestBody = RequestBody.create(mediaType, jsonStr);

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
//                    mActivity.showMaker(projectVo,false);
                    EventBus.getDefault().post(OkHttpParam.SHOW_MARKER);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String responseStr = response.body().string();
                        List<SavePointVo> save2 = savePointVoLongDao.queryForAll();

                        LatlogVo2 latlogVo = gson.fromJson(responseStr, LatlogVo2.class);
                        if (latlogVo.getCode() == 0) {
                            //将请求的数据更新到本地数据库中
                            if (latlogVo.getJsonData() != null && latlogVo.getJsonData().size() > 0) {
                                for (int k = 0; k < latlogVo.getJsonData().size(); k++) {
                                    List<SavePointVo> savePointVos = latlogVo.getJsonData().get(k);
                                    for (int i = 0; i < savePointVos.size(); i++) {
                                        SavePointVo poisVo = savePointVos.get(i);
                                        //判断是不是当前用户的工程数据
                                        if (poisVo.getProjectId().equals
                                                (projectVo.getProjectId())) {
                                            List<SavePointVo> poisVos = savePointVoLongDao.
                                                    queryForEq(OkHttpParam.FAC_ID, poisVo.getFacId());
                                            if (poisVos.size() == 0) {

                                                poisVo.setFacSubmitStatus("1");//已提交

                                                int s = savePointVoLongDao.create(poisVo);
                                                if (s == 1) {

                                                }
                                            } else {
                                                if (poisVo.getUpdateSource().equals(OkHttpParam.PC)) {
                                                    poisVo.setUpdateSource(OkHttpParam.MOBILE);
                                                    int s = savePointVoLongDao.update(poisVo);
                                                    if (s == 1) {

                                                    }
                                                }
                                            }
                                        } else {     //共享数据
                                            List<SavePointVo> poisVos = savePointVoLongDao.
                                                    queryForEq(OkHttpParam.FAC_ID, poisVo.getFacId());
                                            //共享工程如果本地没有，则入库
                                            if (poisVos.size() == 0) {
                                                poisVo.setFacSubmitStatus("1");//已提交
                                                int s = savePointVoLongDao.create(poisVo);
                                                if (s == 1) {

                                                }
                                            }
                                            //共享工程如果本地有，则更新
                                            else if (poisVos.size() == 1) {

                                                poisVo.setFacSubmitStatus("1");//已提交

                                                int s = savePointVoLongDao.update(poisVo);
                                                if (s == 1) {

                                                }
                                            }
                                        }
                                    }
                                }
                            }
//                            mActivity.showMaker(projectVo,false);
                            EventBus.getDefault().post(OkHttpParam.SHOW_MARKER);
                        } else {
                            Toast.makeText(mActivity, "下载点位数据失败，请重新上传数据", Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过用户id直接获取该用户的所有设施数据以及共享数据
     * @param activity
     */
    public static void getFacilityList( final Activity activity){
        String serverUrl = OkHttpURL.serverUrl;
        if (serverUrl == null) {
            ToastUtils.show(AppContext.getInstance().getString(R.string.error_message2));
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(activity.getString(R.string.load_msg));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
//        progressDialog.show();

        String url = serverUrl + OkHttpURL.urlPointListUserId;
        Map<String,String> map=new HashMap<>();
        map.put(OkHttpParam.USER_ID,AppContext.getInstance().getCurUser().getUserId());
        String jsonStr = gson.toJson(map);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .readTimeout(300000L, TimeUnit.MILLISECONDS)
                .build();

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");//"类型,字节码"

        RequestBody requestBody = RequestBody.create(mediaType, jsonStr);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseStr = response.body().string();
                    Dao<SavePointVo,Long> savePointVoLongDao=AppContext.getInstance().getSavePointVoDao();
                    LatlogVo2 latlogVo = gson.fromJson(responseStr, LatlogVo2.class);
                    if (latlogVo.getCode() == 0) {
                        //将请求的数据更新到本地数据库中
                        if (latlogVo.getJsonData() != null && latlogVo.getJsonData().size() > 0) {
                            for (int k = 0; k < latlogVo.getJsonData().size(); k++) {
                                List<SavePointVo> savePointVos = latlogVo.getJsonData().get(k);
                                for (int i = 0; i < savePointVos.size(); i++) {
                                    SavePointVo poisVo = savePointVos.get(i);
                                    //判断是不是当前用户的工程数据
                                    if (poisVo.getUserId().equals
                                            (AppContext.getInstance().getCurUser().getUserId())) {
                                        List<SavePointVo> poisVos = savePointVoLongDao.
                                                queryForEq(OkHttpParam.FAC_ID, poisVo.getFacId());
                                        if (poisVos.size() == 0) {

                                            poisVo.setFacSubmitStatus("1");//已提交

                                            int s = savePointVoLongDao.create(poisVo);
                                            if (s == 1) {

                                            }
                                        } else {
                                            if (poisVo.getUpdateSource().equals(OkHttpParam.PC)) {
                                                poisVo.setUpdateSource(OkHttpParam.MOBILE);
                                                int s = savePointVoLongDao.update(poisVo);
                                                if (s == 1) {

                                                }
                                            }
                                        }
                                    } else {     //共享数据
                                        List<SavePointVo> poisVos = savePointVoLongDao.
                                                queryForEq(OkHttpParam.FAC_ID, poisVo.getFacId());
                                        //共享工程如果本地没有，则入库
                                        if (poisVos.size() == 0) {
                                            poisVo.setFacSubmitStatus("1");//已提交
                                            int s = savePointVoLongDao.create(poisVo);
                                            if (s == 1) {

                                            }
                                        }
                                        //共享工程如果本地有，则更新
                                        else if (poisVos.size() == 1) {

                                            poisVo.setFacSubmitStatus("1");//已提交

                                            int s = savePointVoLongDao.update(poisVo);
                                            if (s == 1) {

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(mActivity, "下载点位数据失败，请重新上传数据", Toast.LENGTH_SHORT).show();
                    }
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

            }

        });
    }


    public static List<SavePointVo> showPoint(ProjectVo projectVo) {
        try {
            Dao<SavePointVo,Long> savePointVoLongDao= AppContext.getInstance().
                    getAppDbHelper().getDao(SavePointVo.class);
            //更新成功后查询本地设施数据库
            Map<String,Object> map=new HashMap<>();
            if (projectVo.getProjectShareCode()!=null&&!projectVo.getProjectShareCode().equals("")){    //共享
                map.put(OkHttpParam.SHARE_CODE, projectVo.getProjectShareCode());
            }else {     //不共享
                map.put(OkHttpParam.PROJECT_ID, projectVo.getProjectId());
            }
            List<SavePointVo> poisVoList=savePointVoLongDao.queryForFieldValues(map);
            if (poisVoList!=null){
//                //请求成功后移除Mark点
//                mActivity.showMaker(poisVoList, projectVo);
                return poisVoList;
            }
        }catch (Exception e1){
            e1.printStackTrace();
        }
        return new ArrayList<SavePointVo>();
    }

    /**
     * 删除百度云的点以及本地文件
     */
    public static void DeleteMarkerPoint(
                                         final List<SavePointVo> savePointVoList_BS,
                                         final ProjectVo projectVo) {
        String json=JSONObject.toJSONString(savePointVoList_BS);

        String serverUrl = OkHttpURL.serverUrl;
        if (serverUrl == null) {
            com.movementinsome.map.nearby.ToastUtils.show(AppContext.getInstance().getString(R.string.error_message2));
            return;
        }
        OkHttpUtils.postString()
//                .url(AppContext.getInstance().getServerUrl()+"/"+OkHttpURL.urlDeletePoint)
                .url(serverUrl + "/" + OkHttpURL.urlDeletePoint)
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
                        PointUpdateVo vo = JSON.parseObject(response, PointUpdateVo.class);
                        if (vo != null && vo.getCode() == 0) {
                            try {
                                PoiOperation.DeletePoi(savePointVoList_BS);
                                mBaiduMap.hideInfoWindow();
//                                    getLatlogData(cenLat,projectVo,acquisitionState);
//                                mActivity.showMaker(projectVo,false);
                                EventBus.getDefault().post(OkHttpParam.SHOW_MARKER);
                                mActivity.moveProjcetUpdate();
                                Toast.makeText(mActivity, "删除成功", Toast.LENGTH_SHORT).show();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mActivity, "删除失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    /**
     * 删除百度云的点以及本地文件
     */
    public static String DeleteMarkerPointSync(List<SavePointVo> savePointVoList_BS,
                                               List<SavePointVo> chileListVos_DB
    ) throws IOException, SQLException {

        try {
            if (savePointVoList_BS.size()>0){
                if (gson == null) {
                    gson = new Gson();
                }

                String json = gson.toJson(savePointVoList_BS);
                String serverUrl = OkHttpURL.serverUrl;
                if (serverUrl == null) {
                    com.movementinsome.map.nearby.ToastUtils.show(AppContext.getInstance().getString(R.string.error_message2));
                    return "1";
                }
                Response re = OkHttpUtils.postString()
                        .url(serverUrl + "/" + OkHttpURL.urlDeletePoint)
                        .content(json)
                        .mediaType(MediaType.parse("application/json; charset=utf-8"))
                        .build()
                        .execute();

                if (re.code() == 200) {
                    String response = re.body().string();
                    DetelePoiVo vo = JSON.parseObject(response, DetelePoiVo.class);
                    if (vo != null && vo.getCode() == 0) {    //成功
                        savePointVoList_BS.addAll(chileListVos_DB);
                        PoiOperation.DeletePoi(savePointVoList_BS);

                        return "0";
                    } else if (vo != null && vo.getCode() == -1) {  //服务器记录不存在
                        savePointVoList_BS.addAll(chileListVos_DB);
                        PoiOperation.DeletePoi(savePointVoList_BS);
                        return "0";
                    }
                } else {
                    return "2";
                }
            }else {
                PoiOperation.DeletePoi(chileListVos_DB);
                return "0";
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
    public static boolean SubmitLatlngBS(final List<SavePointVo> savePointVos) throws SQLException {

        final SavePointVo savePointVo=savePointVos.get(0);

        if (savePointVo.getDataType().equals(MapMeterMoveScope.POINT)) {   //采点

            String json = gson.toJson(savePointVos);

            try {
                String serverUrl=OkHttpURL.serverUrl;
                if (serverUrl==null){
                    com.movementinsome.map.nearby.ToastUtils.show(AppContext.getInstance().getString(R.string.error_message2));
                    return false;
                }
                Response res = OkHttpUtils.postString()
//                        .url(AppContext.getInstance().getServerUrl()+"/"+OkHttpURL.urlGeodata)
                        .url(serverUrl + "/" + OkHttpURL.urlGeodata)
                        .content(json)
                        .mediaType(MediaType.parse("application/json; charset=utf-8"))
                        .build()
                        .execute();

                if (res.code() == 200) {
                    String response = res.body().string();
                    DetelePoiVo detelePoiVo = JSON.parseObject(response, DetelePoiVo.class);
                    if (detelePoiVo.getCode() == 0) {

                        Map<String, Object> map = new HashMap<>();
                        map.put(OkHttpParam.IS_PRESENT, "1");
                        PoiOperation.PoiCreate(savePointVo, map);

                        return true;

                    } else {
                        Map<String, Object> map = new HashMap<>();
                        map.put(OkHttpParam.IS_PRESENT, "0");

                        PoiOperation.PoiCreate(savePointVo, map);
                        Toast.makeText(mActivity, "提交数据失败", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else {
                    Map<String, Object> map = new HashMap<>();
                    map.put(OkHttpParam.IS_PRESENT, "0");

                    PoiOperation.PoiCreate(savePointVo, map);
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Map<String, Object> map = new HashMap<>();
                map.put(OkHttpParam.IS_PRESENT, "0");

                PoiOperation.PoiCreate(savePointVo, map);

                return false;
            }

        } else if (savePointVo.getDataType().equals(MapMeterMoveScope.LINE)) {
            try {
                String json=JSON.toJSONString(savePointVos);

//                String resp= SpringUtil.postData(AppContext.getInstance().getServerUrl()+"/"+OkHttpURL.urlGeodata,json);
                String serverUrl=OkHttpURL.serverUrl;
                if (serverUrl==null){
                    com.movementinsome.map.nearby.ToastUtils.show(AppContext.getInstance().getString(R.string.error_message2));
                    return false;
                }
                String resp= SpringUtil.postData(serverUrl +"/"+OkHttpURL.urlGeodata,json);
                Log.e(TAG, "SubmitLatlngBS: "+resp );

                if (resp!=null) {        //请求成功
//                    String response = re.body().string();
                    DetelePoiVo isUpdateVo = JSON.parseObject(resp, DetelePoiVo.class);
                    if (isUpdateVo!=null&&isUpdateVo.getCode() == 0) {

                        Map<String,Object> map=new HashMap<>();
                        map.put(OkHttpParam.IS_PRESENT,"1");
                        PoiOperation.PoiCreate(savePointVo,map);

                        return true;
                    } else {
                        return false;
                    }
                } else {
                    try {

                        Map<String,Object> map=new HashMap<>();
                        map.put(OkHttpParam.IS_PRESENT,"1");
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
                    map.put(OkHttpParam.IS_PRESENT,"0");
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
     * 重提数据 单个VO
     * BS服务器
     */
    public static boolean SubmitLatlngAllData(final List<SavePointVo> savePointVos) throws SQLException {
        String json = gson.toJson(savePointVos);
        try {
            String serverUrl = OkHttpURL.serverUrl;
            if (serverUrl == null) {
                com.movementinsome.map.nearby.ToastUtils.show(AppContext.getInstance().getString(R.string.error_message2));
                return false;
            }
            Response res = OkHttpUtils.postString()
                    .url(serverUrl + "/" + OkHttpURL.subAllData)
                    .content(json)
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build()
                    .execute();

            if (res.code() == 200) {
                String response = res.body().string();
                DetelePoiVo detelePoiVo = JSON.parseObject(response, DetelePoiVo.class);
                if (detelePoiVo.getCode() == 0) {

                    return true;

                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * 提交数据  VO集合   (单纯的提交管线集合)
     * BS服务器
     */
    public static boolean SubmitPointListCreate(final List<SavePointVo> savePointVoList, final Context context
    ) throws SQLException {

        final ProgressDialog[] progress = {null};

        String json = gson.toJson(savePointVoList);
        Response re= null;
        try {
            String serverUrl=OkHttpURL.serverUrl;
            if (serverUrl==null){
//                com.movementinsome.map.nearby.ToastUtils.show(AppContext.getInstance().getString(R.string.error_message2));
                return false;
            }
            re = OkHttpUtils.postString()
//                    .url(AppContext.getInstance().getServerUrl() + "/" + OkHttpURL.urlGeodata)
                    .url(serverUrl + "/" + OkHttpURL.urlGeodata)
                    .content(json)
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build()
                    .execute();

            if (re.code() == 200) {        //请求成功
                String response = re.body().string();
                DetelePoiVo detelePoiVo = JSON.parseObject(response, DetelePoiVo.class);
                if (detelePoiVo != null && detelePoiVo.getCode() == 0) {

                    //说明整条管线所有点都提交成功
                    for (int i = 0; i < savePointVoList.size(); i++) {
                        Map<String, Object> map = new HashMap<>();
                        map.put(OkHttpParam.IS_PRESENT, "1");    ////提交成功
                        map.put(OkHttpParam.IS_COMPILE, "0");
                        map.put(OkHttpParam.IDS, detelePoiVo.getJsonData());
//                        if (i==savePointVoList.size()-1){
//                            map.put(OkHttpParam.IS_END,true);
//                        }else {
//                            map.put(OkHttpParam.IS_END,false);
//                        }
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
     * 提交数据  VO集合   (单纯的提交管线集合)
     * BS服务器
     */
    public static void SubmitPointListCreateAsyn(final List<SavePointVo> savePointVoList, final SavePointVo line_add_pointVo,
                                                 final Activity activity, final ProjectVo projectVo
    ) {

        String json = gson.toJson(savePointVoList);
        try {
            String serverUrl = OkHttpURL.serverUrl;
            if (serverUrl == null) {
                com.movementinsome.map.nearby.ToastUtils.show(AppContext.getInstance().getString(R.string.error_message2));
                return;
            }
            OkHttpUtils.postString()
//                    .url(AppContext.getInstance().getServerUrl() + "/" + OkHttpURL.urlGeodata)
                    .url(serverUrl + "/" + OkHttpURL.urlGeodata)
                    .content(json)
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build()
                    .execute(new StringDialogCallback(activity) {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            DetelePoiVo detelePoiVo = JSON.parseObject(response, DetelePoiVo.class);
                            if (detelePoiVo != null && detelePoiVo.getCode() == 0) {

                                //说明整条管线所有点都提交成功
                                for (int i = 0; i < savePointVoList.size(); i++) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put(OkHttpParam.IS_PRESENT, "1");    ////提交成功
                                    map.put(OkHttpParam.IS_COMPILE, "0");
                                    map.put(OkHttpParam.IDS, detelePoiVo.getJsonData());
                                    try {
                                        PoiOperation.PoiCreate(savePointVoList.get(i), map);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                                List<SavePointVo> savePointVos = new ArrayList<>(1);
                                savePointVos.add(line_add_pointVo);
                                DeleteMarkerPoint(savePointVos, projectVo);
                                if (mActivity.customDialog.isShowing()) {
                                    mActivity.customDialog.dismiss();
                                }
                                EventBus.getDefault().post(OkHttpParam.DETELE_LINE);
                            }
                        }
                    });
        } catch (Exception e) {
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
        }
    }


    /**
     * 提交数据  VO集合
     * BS服务器
     */
    public static void SubmitPointList(final List<SavePointVo> savePointVoList, final Context context
    ) {

        final ProgressDialog[] progress = {null};

        String serverUrl=OkHttpURL.serverUrl;
        if (serverUrl==null){
            com.movementinsome.map.nearby.ToastUtils.show(AppContext.getInstance().getString(R.string.error_message2));
            return;
        }
        String json = gson.toJson(savePointVoList);
        OkHttpUtils.postString()
//                .url(AppContext.getInstance().getServerUrl() + "/" + OkHttpURL.urlGeodata)
                .url(serverUrl + "/" + OkHttpURL.urlGeodata)
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
                            if (detelePoiVo.getCode() == 0) {
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

    public static void UploadPoint(String paramsJson, File file, final Activity activity, final Handler handler) throws JSONException, IOException {
        Map<String,String> params=new HashMap<>();
        params.put(OkHttpParam.PROJECT_ID,paramsJson);
        String serverUrl = OkHttpURL.serverUrl;
        if (serverUrl != null) {
            String url=serverUrl+OkHttpURL.uploadFiles;
            OkHttpClient httpClient=new OkHttpClient.Builder()
                    .readTimeout(300000L, TimeUnit.MILLISECONDS)
                    .build();

            MediaType MEDIA_TYPE_PNG = MediaType.parse("text/x-markdown; charset=utf-8");

            RequestBody requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(OkHttpParam.MOBILE_PARAM,gson.toJson(params))
                    .addFormDataPart(OkHttpURL.FILE,file.getName(),RequestBody.create(MEDIA_TYPE_PNG,file))
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            final ProgressDialog progressDialog=new ProgressDialog(activity);
            progressDialog.setMessage(activity.getString(R.string.load_msg3));
            progressDialog.show();


            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Message message=new Message();
                    message.what=3;
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseStr=response.body().string();
                    Log.i("response",responseStr);
                    DetelePoiVo isUpdateVo=gson.fromJson(responseStr,DetelePoiVo.class);
                    if (isUpdateVo.getCode()==0){
                        try {
                            Dao<SavePointVo,Long> savePointVoLongDao=AppContext.getInstance().getSavePointVoDao();
                            JSONArray jsonArray=new JSONArray(isUpdateVo.getJsonData());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                org.json.JSONObject jsonObject= (org.json.JSONObject) jsonArray.get(i);
                                String projectId=jsonObject.getString(OkHttpParam.PROJECT_ID);
                                String facName=jsonObject.getString(OkHttpParam.FAC_NAME);
                                String contextStr=jsonObject.getString(OkHttpParam.CONTEXT_STR);
                                Map<String,Object> map=new HashMap<>();
                                map.put(OkHttpParam.PROJECT_ID,projectId);
                                map.put(OkHttpParam.FAC_NAME,facName);
                                List<SavePointVo> savePointVos=savePointVoLongDao.queryForFieldValues(map);
                                if (savePointVos.size()==1){
                                    savePointVos.get(0).setContextStr(contextStr);
                                    int s=savePointVoLongDao.update(savePointVos.get(0));
                                    Log.i("tag",s+"");
                                }
                                if (i==jsonArray.length()-1){
//                                    ToastUtils.show("上传数据成功");
                                    Message message=new Message();
                                    message.what=1;
                                    Bundle bundle=new Bundle();
                                    bundle.putSerializable(OkHttpParam.DETELE_POI_VO,isUpdateVo);
                                    message.setData(bundle);
                                    handler.sendMessage(message);
                                }
                            }
//                            ToastUtils.show("上传");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else {
//                        ToastUtils.show(isUpdateVo.getMsg());
                        Message message=new Message();
                        message.what=2;
                        Bundle bundle=new Bundle();
                        bundle.putSerializable(OkHttpParam.DETELE_POI_VO,isUpdateVo);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            });

//            OkHttpUtils.post()
//                    .url(url)
//                    .addFile(OkHttpURL.FILE,file.getName(),file)
//                    .addParams(OkHttpParam.MOBILE_PARAM,gson.toJson(params))
//                    .build()
//
//                    .execute(new StringDialogCallback(activity) {
//                        @Override
//                        public void inProgress(float progress, long total, int id) {
//                            super.inProgress(progress, total, id);
//                            this.progress[0].setMessage(activity.getString(R.string.load_msg3)+progress*100+"%");
//                        }
//
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//                            e.printStackTrace();
//                            ToastUtils.show(activity.getString(R.string.error_message2));
//                        }
//
//                        @Override
//                        public void onResponse(String response, int id) {
//                            Log.i("response",response);
////                            DetelePoiVo detelePoiVo=gson.fromJson(response,DetelePoiVo.class);
////                            if (detelePoiVo!=null&&detelePoiVo.getCode()==0){
////                                isOk[0] =true;
////                            }
//                        }
//                    });
        }else {
            com.movementinsome.map.nearby.ToastUtils.show(AppContext.getInstance().getString(R.string.error_message2));
        }
    }

    /**
     * 图片下载判断类
     * @param context
     * @param savePointVoList
     * @param projectVo
     */
    public static void Picture(Context context,List<SavePointVo> savePointVoList,ProjectVo projectVo){
        //下载图片
        if (savePointVoList!=null&&savePointVoList.size()>0){
            for (int i = 0; i < savePointVoList.size(); i++) {
                SavePointVo savePointVo=savePointVoList.get(i);
                //是否是本工程数据
                if (projectVo.getProjectId().equals(savePointVo.getProjectId())){
                    CameraSub(context, savePointVo);
                }else if (savePointVo.getFacPipBaseVo().getProjectName() != null && savePointVo.getShareCode() != null){
                    //判断工程是否是共享工程
                    if (savePointVo.getShareCode().equals(projectVo.getProjectShareCode())){
                        CameraSub(context, savePointVo);
                    }
                }
            }
        }
    }

    private static void CameraSub(Context context, SavePointVo savePointVo) {
        String cameraPathStr=savePointVo.getCamera();
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
        String fileServer=OkHttpURL.fileServer;
        if (fileServer==null){
            com.movementinsome.map.nearby.ToastUtils.show(AppContext.getInstance().getString(R.string.error_message2));
            return;
        }
        String url=fileServer+SpringUtil._REST_DOWNLOADFILESIMPLE;
//        String url="http://172.16.1.20:8086/fileService"+SpringUtil._REST_DOWNLOADFILESIMPLE;
        Map<String,String> map=new HashMap<>();
        map.put("guid",guid);
        map.put("picName",camearName);
        OkHttpUtils
                .postString()
                .tag(mActivity)
                .url(url)
                .content(gson.toJson(map))
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
    public static void SubmitLatlngBrokenNetwork(final String adderss, final String tableNum
            , final String caliber, final String lng, final String lat, final LatLng cenpt
            , final String drawPyte, final String drawNum, final String implementorName, final String projectId
            , final String shareCode, final String isProjectShare, final String projectName, final String savePointVoId
            , final HistoryCommitVO historyCommitVOde,Context context
                                    ) throws SQLException {
        final Dao<HistoryCommitVO,Long> historyCommitVOLongDao=AppContext.getInstance().getAppDbHelper().getDao(HistoryCommitVO.class);

        Map parameterMap = new HashMap();
        parameterMap.put(OkHttpParam.GROUP_NAME, AppContext.getInstance().getCurUser().getGroupName());// 设置所属公司班组
        if (projectId!=null){
            parameterMap.put(OkHttpParam.PROJECT_ID,projectId);     //设置工程ID
            parameterMap.put(OkHttpParam.PROJECT_NAME,projectName);     //设置名字
        }
        parameterMap.put(OkHttpParam.IS_PROJECT_SHARE,isProjectShare);     //设置是否共享
        parameterMap.put(OkHttpParam.SHARE_CODE,shareCode);     //设置共享码
        parameterMap.put(OkHttpParam.SAVE_POINTVO_ID,savePointVoId);     //手机本地数据库设施点ID

        parameterMap.put(OkHttpParam.HAPPEN_ADDR, adderss);
        parameterMap.put(OkHttpParam.LATITUDE, lat);
        parameterMap.put(OkHttpParam.LONGITUDE, lng);
        parameterMap.put(OkHttpParam.COORD_TYPE, OkHttpParam.COORD_TYPE_VALUE);
        parameterMap.put(OkHttpParam.GEOTABLE_ID, OkHttpParam.GEOIAVLE_VALUE);
        parameterMap.put(OkHttpParam.AK, OkHttpParam.AK_VALUE);
        parameterMap.put(OkHttpParam.FAC_NAME, tableNum);//设施编号
        if (drawPyte.equals(MapMeterMoveScope.LINE)){
            parameterMap.put(OkHttpParam.DATA_TYPE,MapMeterMoveScope.LINE);
            parameterMap.put(OkHttpParam.DRAW_NUM,drawNum);
        }

        //还需要上传工程ID、工程是否共享和共享码信息
            try {
                Response re=
                        post()
                        .url(OkHttpURL.urlGeodata)
                        .params(parameterMap)
                        .build()
                        .execute();

                if (re.code()==200){        //请求成功
                    String response=re.body().string();
                    IsUpdateVo isUpdateVo = JSON.parseObject(response, IsUpdateVo.class);
                    if (isUpdateVo.getCode() == 0) {
                        try {
                            Dao<SavePointVo,Long> savePointVoLongDao=
                                    AppContext.getInstance().getAppDbHelper().getDao(SavePointVo.class);
                            SavePointVo savePointVo=savePointVoLongDao.queryForId(Long.parseLong(historyCommitVOde.getSavePointVoId()));
                            //将LBS云返回的ID保存到本地数据库中
                            if (savePointVo!=null){
//                                savePointVo.setMarkerId(isUpdateVo.getId()+"");
                                int s=savePointVoLongDao.update(savePointVo);
                                if (s==1){

                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

//                        getLatlogData(cenpt,"");
                        if (historyCommitVOde!=null){
                            try {
                                int s=historyCommitVOLongDao.delete(historyCommitVOde);
                                if (s==1){

                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    } else {
                        Toast.makeText(mActivity, "提交数据失败", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    try {
                        historyCommitVOde.setUploadTime(DateUtil.currDay());
                        int s=historyCommitVOLongDao.update(historyCommitVOde);
                        if (s==1){

                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public List<Overlay> getmOverlayList() {
        return mOverlayList;
    }

    public void setmOverlayList(List<Overlay> mOverlayList) {
        this.mOverlayList = mOverlayList;
    }

}
