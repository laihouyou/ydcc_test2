package com.movementinsome.caice.okhttp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.caice.project.ProjectOperation;
import com.movementinsome.caice.view.CustomDialog;
import com.movementinsome.caice.vo.BaseRequestVo;
import com.movementinsome.caice.vo.DetelePoiVo;
import com.movementinsome.caice.vo.IsUpdateVo;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.map.MapViewer;
import com.pop.android.common.util.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

import static com.movementinsome.map.MapViewer.TAG;

/**
 * 工程请求类
 * Created by zzc on 2017/8/17.
 */

public class ProjectRequest {
    public static Gson gson=new Gson();

    /**
     *  向服务器提交工程
     * @param projectVo    工程
     * @param activity  上下文
     * @param isRecommit 是否是重新提交
     */
    public static void ProjectCreate(final ProjectVo projectVo
            , final Activity activity, final boolean isRecommit
    ) {
        final ProgressDialog[] progress = {null};

        String json = gson.toJson(projectVo);

        String serverUrl = OkHttpURL.serverUrl;
        if (serverUrl != null) {
            OkHttpUtils.postString()
//                .url(AppContext.getInstance().getServerUrl()+"/"+OkHttpURL.creatProject)
                    .url(serverUrl + "/" + OkHttpURL.creatProject)
                    .content(json)
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build()
                    .execute(new StringCallback() {

                        @Override
                        public void onBefore(Request request, int id) {
                            super.onBefore(request, id);
                            progress[0] = new ProgressDialog(activity);
                            progress[0].setMessage("正在加载数据,请等待……");
                            progress[0].setCancelable(false);
                            progress[0].setCanceledOnTouchOutside(false);
                            progress[0].show();

                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.i("tag", e.toString());
                            if (progress[0].isShowing()) {
                                progress[0].dismiss();
                            }
                            if (isRecommit) {
                                ToastUtils.showToast(activity, "工程重提失败，请稍后再试");
                            } else {
                                ToastUtils.showToast(activity, "提交工程失败，将在本地继续保存");
                                ProjectOperation.CreateMiningVo(projectVo, "0", isRecommit);
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.i("tag", response.toString());
                            if (progress[0].isShowing()) {
                                progress[0].dismiss();
                            }
                            IsUpdateVo isUpdateVo = gson.fromJson(response, IsUpdateVo.class);
                            if (isUpdateVo != null && isUpdateVo.getCode() == 0) {
                                ProjectOperation.CreateMiningVo(projectVo, "1", isRecommit);
                            } else if (isUpdateVo != null && isUpdateVo.getCode() == -1) {
                                //参数为空
                                //工程名为空
                                //工程名重复
                            }
                            Toast.makeText(activity, isUpdateVo.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            if (isRecommit) {
                ToastUtils.showToast(activity, "工程重提失败，请稍后再试");
            } else {
                ToastUtils.showToast(activity, "提交工程失败，将在本地继续保存");
                ProjectOperation.CreateMiningVo(projectVo, "0", isRecommit);
            }
        }
    }


    public static void ProjectList( final MapViewer activity) throws IOException {
        final ProgressDialog[] progress = {null};
        Map<String,Object> parameterMap=new HashMap<>();
        parameterMap.put(OkHttpParam.USER_ID,AppContext.getInstance().getCurUser().getUserId());
        String json=gson.toJson(parameterMap);
        String serverUrl=OkHttpURL.serverUrl;
        if (serverUrl==null){
            com.movementinsome.map.nearby.ToastUtils.show(AppContext.getInstance().getString(R.string.error_message2));
            return;
        }
        String url=serverUrl +"/"+OkHttpURL.listProject;
         OkHttpUtils.postString()
//                .url(AppContext.getInstance().getServerUrl() + "/" + OkHttpURL.listProject)
                .url(url)
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        progress[0] = new ProgressDialog(activity);
                        progress[0].setMessage("正在加载数据,请等待……");
                        progress[0].setCancelable(false);
                        progress[0].setCanceledOnTouchOutside(false);
                        progress[0].show();

                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        try {
                            ToastUtils.showToast(activity,"查询失败，请稍后再试.");
                            if (progress[0].isShowing()){
                                progress[0].dismiss();
                            }

                            Map<String,Object> map=new HashMap<>();
                            map.put(OkHttpParam.USER_ID,AppContext.getInstance().getCurUser().getUserId());
                            map.put(OkHttpParam.PROJECT_STATUS,OkHttpParam.PROJECT_STATUS_ONE);
                            List<ProjectVo> projectVoList=AppContext.getInstance()
                                    .getProjectVoDao().queryForFieldValues(map);
                            if (projectVoList.size()==1){
                                activity.GatherMove(projectVoList.get(0),false);
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("response",response);
                        try {
                            //请求成功更新到本地数据库
                            Dao<ProjectVo,Long> miningSurveyVOLongDao=AppContext.getInstance()
                                    .getAppDbHelper().getDao(ProjectVo.class);
                            BaseRequestVo baseRequestVo =gson.fromJson(response,BaseRequestVo.class);
                            if (baseRequestVo.getJsonData()!=null&& baseRequestVo.getJsonData().size()>0){
                                for (int i = 0; i< baseRequestVo.getJsonData().size(); i++){
                                    BaseRequestVo.JsonDataBean projectBean=  baseRequestVo.getJsonData().get(i);
                                    List<ProjectVo> projectVoList =miningSurveyVOLongDao
                                            .queryForEq(OkHttpParam.PROJECT_ID, projectBean.getProjectId());
                                    //只有服务器有本地没有才更新到本地
                                    if (projectVoList.size()==0){
                                        ProjectVo min=new ProjectVo();
                                        min.setProjectSubmitStatus("1");  //请求成功后把是否提交设为1
                                        min.setProjectId(projectBean.getProjectId());
                                        min.setAutoNumberLine(projectBean.getAutoNumberLine());
                                        min.setAutoNumber(projectBean.getAutoNumber());
                                        min.setProjectStatus(projectBean.getProjectStatus());   //请求成功后把工程设为未完成状态
                                        min.setProjectShareCode(projectBean.getProjectShareCode());
                                        min.setProjectCreateDateStr(projectBean.getProjectCreatedateStr());
                                        min.setProjectEndDateStr(projectBean.getProjectEnddateStr());
                                        min.setProjectUpdatedDateStr( projectBean.getProjectUpdateddateStr());
                                        min.setProjectName(projectBean.getProjectName());
                                        min.setProjectNum(projectBean.getProjectNum());
                                        min.setProjectType(projectBean.getProjectType());
                                        min.setUserId(projectBean.getUserId());
                                        min.setContextStr(projectBean.getContextStr());

                                        int s=miningSurveyVOLongDao.create(min);
                                        Log.d(TAG, "onResponse: "+s);

                                    }
                                }
                            }

                            //打开工程
                            Map<String,Object> map=new HashMap<>();
                            map.put(OkHttpParam.USER_ID,AppContext.getInstance().getCurUser().getUserId());
                            map.put(OkHttpParam.PROJECT_STATUS,OkHttpParam.PROJECT_STATUS_ONE);
                            List<ProjectVo> projectVoList=AppContext.getInstance()
                                    .getProjectVoDao().queryForFieldValues(map);
                            if (projectVoList.size()==1){
                                activity.GatherMove(projectVoList.get(0),false);
                            }

                            if (progress[0].isShowing()){
                                progress[0].dismiss();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                });


    }

    public static void ProjectDelete(final Map parameterMap, final Activity activity, final CustomDialog customDialog) throws IOException {
        final ProgressDialog[] progress = {null};
        String serverUrl=OkHttpURL.serverUrl;
        if (serverUrl==null){
            com.movementinsome.map.nearby.ToastUtils.show(AppContext.getInstance().getString(R.string.error_message2));
            return;
        }
         OkHttpUtils.postString()
//                .url(AppContext.getInstance().getServerUrl() + "/" + OkHttpURL.deleteProject)
                .url(serverUrl +"/"+OkHttpURL.deleteProject)
                .content(gson.toJson(parameterMap))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        progress[0] = new ProgressDialog(activity);
                        progress[0].setMessage("正在刪除工程数据,请等待……");
                        progress[0].setCancelable(false);
                        progress[0].setCanceledOnTouchOutside(false);
                        progress[0].show();

                    }

                    /**
                     * 请求结束调用
                     * @param id
                     */
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
                        ToastUtils.showToast(activity,"刪除失败，请稍后再试.");
                        if (progress[0].isShowing()){
                            progress[0].dismiss();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("response",response);
                        try {
                            IsUpdateVo isUpdateVo=gson.fromJson(response,IsUpdateVo.class);
                            if (isUpdateVo.getCode()==0){
                                ProjectOperation.DeteleProject(parameterMap,activity);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (progress[0].isShowing()){
                            progress[0].dismiss();
                        }
                    }
                });


    }

    public static void ProjectUpdate(final Map<String,Object> parameterMap,final ProjectVo projectVo, final Activity activity) {
        final ProgressDialog[] progress = {null};
        String serverUrl=OkHttpURL.serverUrl;
        if (serverUrl==null){
            com.movementinsome.map.nearby.ToastUtils.show(AppContext.getInstance().getString(R.string.error_message2));
            return;
        }
        OkHttpUtils.postString()
//                .url(AppContext.getInstance().getServerUrl()+"/"+OkHttpURL.creatProject)
                .url(serverUrl +"/"+OkHttpURL.updateProject)
                .content(gson.toJson(parameterMap))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        progress[0] = new ProgressDialog(activity);
                        progress[0].setMessage("正在同步工程,请等待……");
                        progress[0].setCancelable(false);
                        progress[0].setCanceledOnTouchOutside(false);
                        progress[0].show();

                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("tag", e.toString());
                        if (progress[0].isShowing()){
                            progress[0].dismiss();
                        }
                        ToastUtils.showToast(activity, "同步工程失败，请稍后再试");
                        try {
                            ProjectOperation.ProjectUpdate(projectVo);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("tag", response.toString());
                        if (progress[0].isShowing()){
                            progress[0].dismiss();
                        }
                        IsUpdateVo isUpdateVo = gson.fromJson(response, IsUpdateVo.class);
                        if (isUpdateVo.getCode() == 0) {
                            try {
                                ProjectOperation.ProjectUpdate(projectVo);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(activity, "同步成功", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public static DetelePoiVo ProjectShate(final JSONObject parameterJson, final Activity activity) throws IOException, JSONException, SQLException {
        final boolean[] isShate = {false};
        String url;
        String serverUrl=OkHttpURL.serverUrl;
        if (serverUrl!=null){
            url=serverUrl+"/"+OkHttpURL.shareProject;

            Response response_r = OkHttpUtils.postString()
                    .url(url)
                    .content(parameterJson.toString())
                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
                    .build()
                    .execute();

            if (response_r.code() == 200) {
                String response = response_r.body().string();
                if (response != null && !response.equals("")) {
                    DetelePoiVo detelePoiVo = com.alibaba.fastjson.JSONObject.parseObject(response, DetelePoiVo.class);
                    if (detelePoiVo.getCode() == 0) {
                        Dao<ProjectVo, Long> projectVoLongDao = AppContext.getInstance().getProjectVoDao();
                        String shareProjectId = parameterJson.getString(OkHttpParam.SHARED_PROJECT_ID);
                        String shareCode = parameterJson.getString(OkHttpParam.SHARE_CODE);
                        String projectId = parameterJson.getString(OkHttpParam.PROJECT_ID);
                        List<ProjectVo> projectList = projectVoLongDao.queryForEq(OkHttpParam.PROJECT_ID, projectId);

                        if (projectList.size() == 1) {
                            ProjectVo projectVo = projectList.get(0);
                            projectVo.setProjectShareCode(shareCode);
                            int s = projectVoLongDao.update(projectVo);
                            Log.i("tag", s + "");
                            if (s == 1) {
                                //还需要将扫描工程下面所有的设施都写上分享码
                                Dao<SavePointVo,Long> savePointVoDao=AppContext.getInstance().getSavePointVoDao();
                                List<SavePointVo> savePointVos=savePointVoDao
                                        .queryForEq(OkHttpParam.PROJECT_ID,projectVo.getProjectId());
                                for (int i = 0; i < savePointVos.size(); i++) {
                                    SavePointVo savePointVo=savePointVos.get(i);
                                    savePointVo.setShareCode(shareCode);
                                    int w=savePointVoDao.update(savePointVo);
                                    if (w==1){
                                        if (i==savePointVos.size()-1){
                                            isShate[0] = true;
                                            return detelePoiVo;
                                        }
                                    }
                                }
                            }

//                                                if (progress[0].isShowing()){
//                                                    progress[0].dismiss();
//                                                }
                        }
                    } else {
//                                        com.movementinsome.map.nearby.ToastUtils.show(detelePoiVo.getMsg());
                        return detelePoiVo;
                    }

                }
            }

//            OkHttpUtils.postString()
//                    .url(url)
//                    .content(parameterJson.toString())
//                    .mediaType(MediaType.parse("application/json; charset=utf-8"))
//                    .build()
//                    .execute(new StringDialogCallback(activity) {
//
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//                            if (progress[0].isShowing()){
//                                progress[0].dismiss();
//                            }
//                            com.movementinsome.map.nearby.ToastUtils.show(activity.getString(R.string.error_msg4));
//                        }
//
//                        @Override
//                        public void onResponse(String response, int id) {
//                            try {
//                                if (response!=null&&!response.equals("")){
//                                    DetelePoiVo detelePoiVo= com.alibaba.fastjson.JSONObject.parseObject(response,DetelePoiVo.class);
//                                    if (detelePoiVo.getCode()==0){
//                                        Dao<ProjectVo,Long> projectVoLongDao=AppContext.getInstance().getProjectVoDao();
//                                        String shareProjectId=parameterJson.getString(OkHttpParam.SHARED_PROJECT_ID);
//                                        String shareCode = parameterJson.getString(OkHttpParam.SHARE_CODE);
//                                        String projectId = parameterJson.getString(OkHttpParam.PROJECT_ID);
//                                        List<ProjectVo> projectList = projectVoLongDao.queryForEq(OkHttpParam.PROJECT_ID, projectId);
//                                        com.movementinsome.map.nearby.ToastUtils.show(
//                                                projectList.size()+"\n"+
//                                                        detelePoiVo.getMsg()+"\n"+
//                                                        detelePoiVo.getJsonData()+"\n"
//                                        );
//                                        if (projectList.size() == 1) {
//                                            ProjectVo projectVo = projectList.get(0);
//                                            projectVo.setProjectShareCode(shareCode);
//                                            int s = projectVoLongDao.update(projectVo);
//                                            Log.i("tag", s + "");
//                                            if (s==1){
//                                                isShate[0] =true;
//                                            }
//
////                                                if (progress[0].isShowing()){
////                                                    progress[0].dismiss();
////                                                }
//                                        }
//                                    }else {
////                                        com.movementinsome.map.nearby.ToastUtils.show(detelePoiVo.getMsg());
//                                    }
//                                }
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
//                            super.onResponse(response,id);
//                        }
//                    });
        }else {
//            com.movementinsome.map.nearby.ToastUtils.show(AppContext.getInstance().getString(R.string.error_message2));
        }
        return new DetelePoiVo();
    }

}
