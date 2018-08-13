package com.movementinsome.caice.okhttp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.caice.project.ProjectOperation;
import com.movementinsome.caice.view.CustomDialog;
import com.movementinsome.caice.vo.CreatProjectVo;
import com.movementinsome.caice.vo.IsUpdateVo;
import com.movementinsome.caice.vo.MiningSurveyVO;
import com.movementinsome.map.MapViewer;
import com.pop.android.common.util.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;

/**
 * 工程请求类
 * Created by zzc on 2017/8/17.
 */

public class ProjectRequest {
    public static Gson gson;

    public ProjectRequest() {
        gson = new Gson();
    }

    /**
     *  向服务器提交工程
     * @param miningSurveyVO    工程
     * @param activity  上下文
     * @param isRecommit 是否是重新提交
     */
    public static void ProjectCreate(final MiningSurveyVO miningSurveyVO
            , final Activity activity, final boolean isRecommit
    ) {
        final ProgressDialog[] progress = {null};

        String json=gson.toJson(miningSurveyVO);
        String url= OkHttpURL.serverUrl +"/"+OkHttpURL.creatProject;

        OkHttpUtils.postString()
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
                        Log.i("tag", e.toString());
                        if (progress[0].isShowing()){
                            progress[0].dismiss();
                        }
                        if (isRecommit){
                            ToastUtils.showToast(activity, "工程重提失败，请稍后再试");
                        }else {
                            ToastUtils.showToast(activity, "提交工程失败，将在本地继续保存");
                            ProjectOperation.CreateMiningVo(miningSurveyVO,"0",isRecommit);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("tag", response.toString());
                        if (progress[0].isShowing()){
                            progress[0].dismiss();
                        }
                        IsUpdateVo isUpdateVo = gson.fromJson(response, IsUpdateVo.class);
                        if (isUpdateVo!=null&&isUpdateVo.getStatus() == 0) {
                            ProjectOperation.CreateMiningVo(miningSurveyVO,"1",isRecommit);
                        } else if (isUpdateVo!=null&&isUpdateVo.getStatus() == -1){
                            //参数为空
                            //工程名为空
                            //工程名重复
                        }
                        Toast.makeText(activity, isUpdateVo.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public static void ProjectList(final MapViewer activity, final MiningSurveyVO miningSurveyVO) throws IOException {
        final ProgressDialog[] progress = {null};
        Map<String,String> parameterMap=new HashMap<>();
        parameterMap.put(OkHttpParam.USER_ID, AppContext.getInstance().getCurUser().getUserId());
        String json = gson.toJson(parameterMap);
        String url = OkHttpURL.serverUrl + "/" + OkHttpURL.listProject;

        OkHttpUtils.postString()
                .url(url)
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {

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
                        ToastUtils.showToast(activity, "查询失败，请稍后再试.");
                        if (progress[0].isShowing()) {
                            progress[0].dismiss();
                        }
                        if (miningSurveyVO != null) {
                            activity.GatherMove(miningSurveyVO, true);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("response", response);
                        try {
                            //请求成功更新到本地数据库
                            Dao<MiningSurveyVO, Long> miningSurveyVOLongDao = AppContext.getInstance()
                                    .getAppDbHelper().getDao(MiningSurveyVO.class);
                            CreatProjectVo creatProjectVo = gson.fromJson(response, CreatProjectVo.class);
                            if (creatProjectVo.getPois() != null && creatProjectVo.getPois().size() > 0) {
                                for (int i = 0; i < creatProjectVo.getPois().size(); i++) {
                                    List<MiningSurveyVO> miningSurveyVOList = miningSurveyVOLongDao
                                            .queryForEq(OkHttpParam.PROJECT_ID, creatProjectVo.getPois().get(i).getProjectId());
                                    //只有服务器有本地没有才更新到本地
                                    if (miningSurveyVOList.size() == 0) {
                                        MiningSurveyVO min = new MiningSurveyVO();
                                        CreatProjectVo.ProjectBean projectBean = creatProjectVo.getPois().get(i);

                                        min.setProjectId(projectBean.getProjectId());
                                        min.setAutoNumberLine(projectBean.getAutoNumberLine());
                                        min.setAutoNumber(projectBean.getAutoNumber());
                                        min.setIsCompile("0");  //请求成功后把是否编辑设为0
                                        min.setIsPresent("1");  //请求成功后把是否提交设为1
                                        min.setIsSubmit("0");   //请求成功后把工程设为未完成状态
                                        min.setIsProjectShare(projectBean.getIsProjectShare());
                                        min.setShareCode(projectBean.getShareCode());
                                        min.setPointVos(projectBean.getPointVos());
                                        min.setProjcetLineLenghts(projectBean.getProjcetLineLenghts());
                                        min.setProjectEDateStr(projectBean.getProjectEDate());
                                        min.setProjectEDateUpd(projectBean.getProjectEDateUpd());
                                        min.setProjectName(projectBean.getProjectName());
                                        min.setProjectNum(projectBean.getProjectNum());
                                        min.setProjectSDateStr(projectBean.getProjectSDate());
                                        min.setProjectType(projectBean.getProjectType());
                                        min.setTaskNum(projectBean.getTaskNum());
                                        min.setUsedName(projectBean.getUsedName());
                                        min.setUsedId(projectBean.getUsedId());
                                        min.setPointVos(projectBean.getPointVos());

                                        int s = miningSurveyVOLongDao.create(min);
                                    }
                                }
                            }

                            //打开工程
                            if (miningSurveyVO != null) {
                                MiningSurveyVO m = new MiningSurveyVO();
                                m = miningSurveyVOLongDao.queryForEq
                                        (OkHttpParam.PROJECT_ID, miningSurveyVO.getProjectId()).get(0);
                                if (!m.getProjectId().equals("")) {
                                    activity.GatherMove(m, true);
                                }
                            }

                            if (progress[0].isShowing()) {
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
        String url = OkHttpURL.serverUrl + "/" + OkHttpURL.deleteProject;
        String json = gson.toJson(parameterMap);

        OkHttpUtils.postString()
                .url(url)
                .content(json)
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
                        if (progress[0].isShowing()) {
                            progress[0].dismiss();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        ToastUtils.showToast(activity, "刪除失败，请稍后再试.");
                        if (progress[0].isShowing()) {
                            progress[0].dismiss();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("response", response);
                        try {
                            IsUpdateVo isUpdateVo = gson.fromJson(response, IsUpdateVo.class);
                            if (isUpdateVo.getStatus() == 0) {
                                ProjectOperation.DeteleProject(parameterMap, activity);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (progress[0].isShowing()) {
                            progress[0].dismiss();
                        }
                    }
                });


    }

    public static void ProjectUpdate(final MiningSurveyVO miningSurveyVO, final Activity activity) {
        final ProgressDialog[] progress = {null};
        String url = OkHttpURL.serverUrl + "/" + OkHttpURL.updateProject;
        String json = gson.toJson(miningSurveyVO);


        OkHttpUtils.postString()
                .url(url)
                .content(json)
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
                        if (progress[0].isShowing()) {
                            progress[0].dismiss();
                        }
                        ToastUtils.showToast(activity, "同步工程失败，请稍后再试");
                        try {
                            ProjectOperation.setProjectCompile(miningSurveyVO, "1");
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("tag", response.toString());
                        if (progress[0].isShowing()) {
                            progress[0].dismiss();
                        }
                        IsUpdateVo isUpdateVo = gson.fromJson(response, IsUpdateVo.class);
                        if (isUpdateVo.getStatus() == 0) {
                            try {
                                ProjectOperation.setProjectCompile(miningSurveyVO, "0");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(activity, "同步成功", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}
