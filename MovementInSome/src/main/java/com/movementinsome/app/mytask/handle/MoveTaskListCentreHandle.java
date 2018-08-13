package com.movementinsome.app.mytask.handle;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.caice.activity.FacilityListActivity;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.okhttp.OkHttpRequest;
import com.movementinsome.caice.okhttp.ProjectRequest;
import com.movementinsome.caice.project.ProjectOperation;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.view.CustomDialog;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.kernel.util.ActivityUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LJQ on 2017/5/20.
 */

public class MoveTaskListCentreHandle<T>implements MoveTaskListCentreBaseHandle{
    private static Activity context;
    private ProjectVo projectVo;
    private CustomDialog customDialog;
    private CustomDialog.Builder builder;
    private BaseAdapter adapter;

    boolean point=true;
    boolean proje=true;

    public MoveTaskListCentreHandle (Activity context, ProjectVo projectVo, BaseAdapter adapter){
        this.context = context;
        this.projectVo = projectVo;
        this.adapter=adapter;
        AppContext.getInstance().setProjectVo(projectVo);
    }

    @Override
    public void showMsgHandler() {

        TabHost s = AppContext.getInstance().getmHost();
        s.setCurrentTabByTag("2");
        List<TextView> tvs = AppContext.getInstance().getTvs();
        List<ImageView> imgs = AppContext.getInstance().getImgs();
        for (int i = 0; i < tvs.size(); i++) {
            if (i == 0) {
                tvs.get(i).setSelected(true);
                imgs.get(i).setBackgroundResource(R.drawable.main_map_icon1);
            } else if (i == 1) {
                tvs.get(i).setSelected(false);
                imgs.get(i).setBackgroundResource(R.drawable.main_task_icon0);
            } else if (i == 2) {
                tvs.get(i).setSelected(false);
                imgs.get(i).setBackgroundResource(R.drawable.main_mine_icon0);
            }
        }
        AppContext.getInstance().setProjectVo(projectVo);
        Intent intent = new Intent();
        intent.setPackage(AppContext.getInstance().getPackageName());
        intent.putExtra("parameter","check");
        intent.setAction(Constant.PASSDATE);
        context.sendBroadcast(intent);

        Toast.makeText(context,"查看",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startWorkHandler() {
        TabHost s = AppContext.getInstance().getmHost();
        s.setCurrentTabByTag("2");
        List<TextView> tvs = AppContext.getInstance().getTvs();
        List<ImageView> imgs = AppContext.getInstance().getImgs();
        for (int i = 0; i < tvs.size(); i++) {
            if (i == 0) {
                tvs.get(i).setSelected(true);
                imgs.get(i).setBackgroundResource(R.drawable.main_map_icon1);
            } else if (i == 1) {
                tvs.get(i).setSelected(false);
                imgs.get(i).setBackgroundResource(R.drawable.main_task_icon0);
            } else if (i == 2) {
                tvs.get(i).setSelected(false);
                imgs.get(i).setBackgroundResource(R.drawable.main_mine_icon0);
            }
        }
        AppContext.getInstance().setProjectVo(projectVo);
        Intent intent = new Intent();
        intent.setPackage(AppContext.getInstance().getPackageName());
        intent.setAction(Constant.PASSDATE);
        intent.putExtra("parameter","move");
        context.sendBroadcast(intent);

    }

    @Override
    public void delect() throws SQLException {
        builder = new CustomDialog.Builder(context);
        customDialog=builder.cancelTouchout(false)
                .view(R.layout.move_mining_over_dlaio)
                .heightpx(ActivityUtil.getWindowsHetght(context))
                .widthpx(ActivityUtil.getWindowsWidth(context))
                .style(R.style.dialog)
                .setTitle("删除提醒")
                .setMsg("确认删除？")
                .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (projectVo.getProjectStatus().equals("1")){
                                Toast.makeText(context,"该工程正在采测中,请暂停采测后在操作",Toast.LENGTH_SHORT).show();
                                customDialog.dismiss();
                                return;
                            }
                            Dao<ProjectVo,Long> miningSurveyVOLongDao=AppContext.getInstance()
                                    .getAppDbHelper().getDao(ProjectVo.class);
                            Dao<SavePointVo, Long> savePointDao = AppContext
                                    .getInstance().getAppDbHelper()
                                    .getDao(SavePointVo.class);
                            Map<String ,Object> map=new HashMap();
                            map.put(OkHttpParam.PROJECT_ID, projectVo.getProjectId());
                            map.put(OkHttpParam.USER_ID,AppContext.getInstance().getCurUser().getUserId());
                            List<SavePointVo> savePointVoList=savePointDao.queryForFieldValues(map);
                            if (savePointVoList.size()==0){

                                //先联网删除服务器工程
                                String projectId= projectVo.getProjectId();
//                                Map<String,Object> map=new HashMap<>();
//                                map.put(OkHttpParam.PROJECT_ID,projectId);
                                if (projectVo.getProjectSubmitStatus()!=null){
                                    if (projectVo.getProjectSubmitStatus().equals("0")){      //未提交
                                        ProjectOperation.DeteleProject(map,context);
                                    }else if (projectVo.getProjectSubmitStatus().equals("1")){    //已提交
                                        ProjectRequest.ProjectDelete(map, context,customDialog);
                                    }
                                }else {
                                    ProjectOperation.DeteleProject(map,context);
                                }
                                customDialog.dismiss();
                            }else {
                                Toast.makeText(context,"请删除所有设施点与管线后再进行操作",Toast.LENGTH_SHORT).show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .addViewOnclick(R.id.cancelOverIm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                })
                .addViewOnclick(R.id.cancelOverBtn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                })
                .build()
        ;
        customDialog.show();
    }

    @Override
    public void finishHandler() {
        builder = new CustomDialog.Builder(context);
        customDialog=builder.cancelTouchout(false)
                .view(R.layout.move_mining_over_dlaio)
                .heightpx(ActivityUtil.getWindowsHetght(context))
                .widthpx(ActivityUtil.getWindowsWidth(context))
                .style(R.style.dialog)
                .setTitle("完成提醒")
                .setMsg("确认完成？")
                .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (projectVo !=null){
                                projectVo.setProjectStatus("2");    //已完成
                                Map<String,Object> parameterMap=new HashMap<>();
                                parameterMap.put(OkHttpParam.PROJECT_ID,projectVo.getProjectId());
                                parameterMap.put(OkHttpParam.PROJECT_STATUS,"2");
                                ProjectRequest.ProjectUpdate(parameterMap,projectVo, context);

                                customDialog.dismiss();
                                AppContext.getInstance().setProjectVo(projectVo);
                                Intent intent = new Intent();
                                intent.setPackage(context.getPackageName());
                                intent.putExtra("parameter", "complete");
                                intent.setAction(Constant.PASSDATE);
                                context.sendBroadcast(intent);

//                                int s=miningSurveyVOLongDao.update(projectVo);
//                                if (s==1){
//                                    customDialog.dismiss();
//                                    moveProjcetUpdate();
//                                    Toast.makeText(context,"完成成功",Toast.LENGTH_SHORT).show();
//
//                                    AppContext.getInstance().setProjectVo(projectVo);
//
//                                    Intent intent = new Intent();
//                                    intent.setPackage(context.getPackageName());
//                                    intent.putExtra("parameter","complete");
//                                    intent.setAction(Constant.PASSDATE);
//                                    context.sendBroadcast(intent);
//                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .addViewOnclick(R.id.cancelOverIm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                })
                .addViewOnclick(R.id.cancelOverBtn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                })
                .build()
        ;
        customDialog.show();
    }

    //提交
    @Override
    public void subHandler() {

        builder = new CustomDialog.Builder(context);
        customDialog=builder.cancelTouchout(false)
                .view(R.layout.move_mining_over_dlaio)
                .heightpx(ActivityUtil.getWindowsHetght(context))
                .widthpx(ActivityUtil.getWindowsWidth(context))
                .style(R.style.dialog)
                .setTitle("提交提醒")
                .setMsg("确认提交？")
                .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //获取当前工程提交状态
                        if (projectVo.getProjectSubmitStatus()!=null&& projectVo.getProjectSubmitStatus().equals("0")){    //工程未提交
                            ProjectRequest.ProjectCreate(projectVo,context,true);
                        }else if (projectVo.getProjectSubmitStatus()!=null&& projectVo.getProjectSubmitStatus().equals("1")){      //已提交
                            Map<String,Object> parameterMap=new HashMap<>();
                            parameterMap.put(OkHttpParam.PROJECT_ID,projectVo.getProjectId());
                            parameterMap.put(OkHttpParam.PROJECT_SUBMIT_STATUS,"1");
                            ProjectRequest.ProjectUpdate(parameterMap,projectVo,context);
                        }

                        //获取未提交的设施数据
                        List<SavePointVo> savePointVoList=getSavePointVoList();
                        List<SavePointVo> savePointVos_no_sub=new ArrayList<SavePointVo>();
                        List<SavePointVo> savePointVos_no_com=new ArrayList<SavePointVo>();
                        for (int i=0;i<savePointVoList.size();i++){
                            SavePointVo savePointVo=savePointVoList.get(i);
                            if (savePointVo.getFacSubmitStatus()!=null){
                                if (savePointVo.getFacSubmitStatus().equals("0")){
                                    //未提交
                                    savePointVos_no_sub.add(savePointVoList.get(i));
                                }
                            }else {
                                //没有字段说明是旧版本数据
                                savePointVos_no_sub.add(savePointVoList.get(i));
                            }
                        }

                        if (savePointVos_no_sub.size()>0) {
                            OkHttpRequest.SubmitPointList(savePointVos_no_sub, context);
                        }
                        if (savePointVos_no_com.size()>0) {
                            OkHttpRequest.IsUpdatePio(savePointVos_no_com, context, projectVo);
                        }

                        customDialog.dismiss();
                    }
                })
                .addViewOnclick(R.id.cancelOverIm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                })
                .addViewOnclick(R.id.cancelOverBtn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                })
                .build()
        ;
        customDialog.show();

    }

    //进入设施列表
    @Override
    public void facilityList() {
        Intent intent = new Intent(context, FacilityListActivity.class);
        intent.putExtra("projectVo", projectVo);
        context.startActivity(intent);
}

    @Override
    public void controlUI(Map<String, View> v) {
        TextView move_name = (TextView) v.get("move_name");
        TextView move_type = (TextView) v.get("move_type");
        TextView move_value_num = (TextView) v.get("move_value_num");
        TextView move_time = (TextView) v.get("move_time");
        TextView newest = (TextView) v.get("newest");
        LinearLayout move_surveying = (LinearLayout) v.get("move_surveying");
        LinearLayout move_finsh = (LinearLayout) v.get("move_finsh");
        LinearLayout move_sub = (LinearLayout) v.get("move_sub");
        ImageView move_logo = (ImageView) v.get("move_logo");

        AppContext.getInstance().setProjectVo(projectVo);

        switch (projectVo.getProjectType()){
            case "电力":
                move_logo.setBackgroundResource(R.drawable.power);
                break;

            case "电信":
                move_logo.setBackgroundResource(R.drawable.telegraphy);
                break;

            case "给水":
                move_logo.setBackgroundResource(R.drawable.water_delivery);
                break;

            case "排水":
                move_logo.setBackgroundResource(R.drawable.drain_away_water);
                break;

            case "燃气":
                move_logo.setBackgroundResource(R.drawable.combustion_gas);
                break;

            case "综合":
                move_logo.setBackgroundResource(R.drawable.synthesis);
                break;

            default:
                move_logo.setBackgroundResource(R.drawable.water_delivery);
                break;
        }

        if (projectVo.getProjectStatus().equals("2")){
            move_surveying.setVisibility(View.GONE);
            move_finsh.setVisibility(View.GONE);
        }
        Button move_stuas = (Button)v.get("move_stuas");
        move_name.setText(projectVo.getProjectName());
        move_type.setText("工程类型:"+ projectVo.getProjectType());

        if (projectVo.getProjectCreateDateStr()!=null&&!projectVo.getProjectCreateDateStr().equals("")){
            move_time.setText(projectVo.getProjectCreateDateStr().split(" ")[0]);      //工程创建时间
        }

        if(projectVo.getProjectStatus().equals("1")){
            move_stuas.setVisibility(View.VISIBLE);
        }


        //获取工程设施数据的提交情况
        List<SavePointVo> savePointVoList=getSavePointVoList();
        if (savePointVoList!=null&&savePointVoList.size()>0){
            for (int i=0;i<savePointVoList.size();i++){
                SavePointVo savePointVo=savePointVoList.get(i);
                if (savePointVo.getFacSubmitStatus()!=null){
                    if (savePointVo.getFacSubmitStatus().equals("0")){
                        //未提交
                        point=false;
                        break;
                    }else if (savePointVo.getFacSubmitStatus().equals("1")){
                        point=true;
                        break;
                    }
                }else {
                    //没有字段说明是旧版本数据
                    point=false;
                    break;
                }
            }
        }

        if (projectVo.getProjectSubmitStatus()!=null&& projectVo.getProjectSubmitStatus().equals("0")){     //未提交
            proje=false;
        } else if (projectVo.getProjectSubmitStatus() != null && projectVo.getProjectSubmitStatus().equals("1")) {       //已提交
            proje = true;
        } else {
            proje=false;
        }

        //根据工程与设施的提交状态显示提交按钮
        if (proje&&point){
            newest.setText("已同步");
            newest.setVisibility(View.VISIBLE);
            newest.setTextColor(context.getResources().getColor(R.color.lightgreen));
            move_sub.setVisibility(View.GONE);
        }else {
            newest.setText("未同步");
            newest.setVisibility(View.VISIBLE);
            newest.setTextColor(context.getResources().getColor(R.color.red));
            move_sub.setVisibility(View.VISIBLE);
        }


        try{
            Dao<SavePointVo, Long> savePointDao = AppContext
                    .getInstance().getAppDbHelper()
                    .getDao(SavePointVo.class);
            Map<String, Object> v2 = new HashMap<String, Object>();
            v2.put(OkHttpParam.PROJECT_ID, projectVo.getProjectId());
            List<SavePointVo> savePointList = savePointDao.queryForFieldValuesArgs(v2);
            List<SavePointVo> savePointVos=new ArrayList<>();
            if (savePointList.size()>0){
                for (int i=0;i<savePointList.size();i++){
                    if (savePointList.get(i).getDataType().equals(MapMeterMoveScope.POINT)){
                        savePointVos.add(savePointList.get(i));
                    }
                }
            }
            move_value_num.setText("设施点数量:"+savePointVos.size());
        }catch (Exception e){
            e.printStackTrace();
        }
        String ss = "";
    }

    public  static void moveProjcetUpdate(){
        Intent intent=new Intent();
        intent.setPackage(AppContext.getInstance().getPackageName());
        intent.setAction(com.movementinsome.app.pub.Constant.MOVE_TASK_LIST_UPDATE_ACTION);
        AppContext.getInstance().sendBroadcast(intent);
    }

    public List<SavePointVo> getSavePointVoList() {
        Dao<SavePointVo, Long> savePointDao = null;
        try {
            savePointDao = AppContext
                    .getInstance().getAppDbHelper()
                    .getDao(SavePointVo.class);
            List<SavePointVo> savePointVoList=savePointDao.queryForEq(OkHttpParam.PROJECT_ID, projectVo.getProjectId());
            return savePointVoList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
